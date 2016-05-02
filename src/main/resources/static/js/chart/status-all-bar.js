define(function (require) {
    "use strict";

    require('morris');

    var Backbone = require('backbone');

    return Backbone.View.extend({
        render: function () {
            this.chart = new Morris.Bar({
                element: this.el.id,
                data: [],
                xkey: 'name',
                ykeys: ['examineeCnt', 'attendCnt', 'absentCnt'],
                labels: ['지원자수', '응시자수', '결시자수'],
                //resize: true
            });
            this.search();
            return this;
        }, search: function (o) {
            var _this = this;
            $.ajax({
                url: 'status/all/chart',
                data: o
            }).done(function (response) {
                _this.chart.setData([{
                    name: '응시현황',
                    examineeCnt: response.examineeCnt,
                    attendCnt: response.attendCnt,
                    absentCnt: response.absentCnt
                }]);
            });
        }
    });
});