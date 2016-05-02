define(function (require) {
    "use strict";

    require('morris');

    var Backbone = require('backbone');

    return Backbone.View.extend({
        render: function () {
            this.chart = Morris.Bar({
                element: this.el.id,
                data: [
                    {name: '테스트', attendCnt: 0, absentCnt: 654},
                    {name: '테스트', attendCnt: 2, absentCnt: 7029},
                    {name: '테스트', attendCnt: 1, absentCnt: 2403}
                ],
                xkey: 'name',
                ykeys: ['attendCnt', 'absentCnt'],
                labels: ['응시자수', '결시자수']
            });
            this.search();
            this.resize();
            return this;
        }, resize: function () {
            var _this = this;
            $(window).bind('resizeEnd.Morris' + this.cid, function () {
                _this.chart.redraw();
            });
        }, close: function () {
            $(window).unbind('resizeEnd.Morris' + this.cid);
        }, search: function (o) {

        }
    });
});