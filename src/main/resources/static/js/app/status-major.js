define(function (require) {
    "use strict";

    require('layout');

    var Backbone = require('backbone');
    var Toolbar = require('./toolbar/toolbar-status-major.js');
    var List = require('./grid/grid-status-major.js');
    var Chart = require('./chart/chart-status-major.js');

    var layout;

    return Backbone.View.extend({
        render: function () {
            layout = this.$el.layout({
                south : {
                    size : '0.4'
                }
            });
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