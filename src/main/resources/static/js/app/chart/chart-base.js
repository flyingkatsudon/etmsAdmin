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
        initialize: function () {
            var _this = this;
            $(window).unbind('resizeEnd.' + this.cid).bind('resizeEnd.' + this.cid, function () {
                if (_this.chart)
                    _this.chart.resize();
            });
        },
        render: function (param) {
            var _this = this;
            var data;

            $.each(param, function (key, value) {
                if (value === "" || value === null) {
                    delete param[key];
                }
            });

            if (this.url) {
                $.ajax({
                    async: false,
                    url: _this.url,
                    data: param,
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
            }
            return this;
        },
        search: function (o) {
            this.render(o);
        }
    });
});