define(function (require) {
    "use strict";

    require('layout');

    var Backbone = require('backbone');
    var Toolbar = require('./toolbar/toolbar-status.js');
    var List = require('./grid/grid-status-attend.js');
    var Chart = require('./chart/chart-status-attend.js');

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