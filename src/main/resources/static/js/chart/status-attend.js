define(function (require) {
    "use strict";

    require('morris');

    var Backbone = require('backbone');

    return Backbone.View.extend({
        render: function () {
            this.chart = Morris.Bar({
                element: this.el.id,
                xkey: 'typeNm',
                ykeys: ['attendCnt', 'absentCnt'],
                labels: ['응시자수', '결시자수'],
                //resize: true
            });
            this.search();
            return this;
        }, search: function (o) {
            var _this = this;
            $.ajax({
                url: 'status/attend/chart',
                data: o
            }).done(function (response) {
                var data = [];

                for (var i = 0; i < response.length; i++) {
                    data.push({
                        typeNm: response[i].typeNm,
                        attendCnt: response[i].attendCnt,
                        absentCnt: response[i].absentCnt
                    });
                }
                _this.chart.setData(data);
            });
        }
    })
});