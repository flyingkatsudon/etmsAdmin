define(function (require) {
    "use strict";

    var multiline = require('multiline');

    var Chart = require('chartjs');
    var Backbone = require('backbone');

    var html = multiline(function () {
        /*!
         <div id="canvas" class="hm-ui-tab-panel">
         <canvas></canvas>
         </div>
         */
    });

    return Backbone.View.extend({
        render: function (param) {
            var _this = this;
            var data;

            if (this.url) {
                $.ajax({
                    async: false,
                    url: _this.url,
                    data: {q: JSON.stringify(param)},
                    success: function (result) {
                        data = result;
                    }
                });
            }

            if (data) {
                this.$el.html(html);
                var ctx = this.$('canvas')[0].getContext('2d');
                var chartData = $.extend(true, {
                    data: data, options: {
                        responsive: true,
                        maintainAspectRatio: false
                    }
                }, _this.options);
                this.chart = new Chart(ctx, chartData);
                this.chart.resize();
            }
            return this;
        },
        search: function (o) {
            this.render(o);
        }
    });
});