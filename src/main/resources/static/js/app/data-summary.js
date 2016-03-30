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
            this.chart = new Chart({el: layout.south.pane}).render();
            this.list = new List({el: layout.center.pane}).render();
            this.toolbar = new Toolbar({el: layout.north.pane, parent: this}).render();
            $(window).trigger('resize');
        },

        search: function(o){
            this.list.search(o);
            this.chart.search(o);
        }
    });
});