define(function (require) {
    "use strict";

    require('layout');

    var Backbone = require('backbone');
    var Toolbar = require('./toolbar/toolbar-data-summary.js');
    var List = require('./grid/grid-data-summary.js');
    var Chart = require('./chart/chart-data-summary.js'); // 필터링 한 후에 적용

    var layout;

    return Backbone.View.extend({
        render: function () {
            layout = this.$el.layout();
            var chart = new Chart({el: layout.south.pane}).render();
            var list = new List({el: layout.center.pane}).render();
            var toolbar = new Toolbar({el: layout.north.pane, list: list}).render();
            $(window).trigger('resize');
        }
    });
});