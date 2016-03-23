define(function (require) {
    "use strict";

    require('layout');

    var Backbone = require('backbone');
    var Toolbar = require('./toolbar/toolbar-status-hall.js');
    var List = require('./grid/grid-status-hall.js');
    var Chart = require('./chart/chart-status-hall.js'); // 날짜 선택 후에 사용

    var layout;

    return Backbone.View.extend({
        render: function () {
            layout = this.$el.layout();
            var list = new List({el: layout.center.pane}).render();
            var toolbar = new Toolbar({el: layout.north.pane, list: list}).render();
            $(window).trigger('resize');
        }
    });
});