define(function (require) {
    "use strict";

    require('layout');

    var Backbone = require('backbone');
    var Toolbar = require('./toolbar/toolbar-status-hall.js');
    var List = require('./grid/grid-status-hall.js');
    var BarChart = require('./chart/chart-status-hall.js'); // 날짜 선택 후에 사용

    var layout;

    return Backbone.View.extend({
        render: function () {
            layout = this.$el.layout({
                south : {
                    size : '0.4'
                }
            });
            this.chart = new BarChart({el: layout.south.pane}).render();// 날짜 선택 후에 사용
            this.list = new List({el: layout.center.pane}).render();
            this.toolbar = new Toolbar({el: layout.north.pane, parent: this}).render();

            $(window).trigger('resize');
        },
        search : function(o){
            this.list.search(o);
            this.chart.search(o);
        }
    });
});