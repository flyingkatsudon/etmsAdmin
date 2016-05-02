define(function (require) {
    "use strict";

    require('morris');

    var Backbone = require('backbone');

    return Backbone.View.extend({
        render: function () {
            Morris.Bar({
                element: this.el.id,
                data: [
                    {name: '테스트', attendCnt: 0, absentCnt: 654},
                    {name: '테스트', attendCnt: 2, absentCnt: 7029},
                    {name: '테스트', attendCnt: 1, absentCnt: 2403}
                ],
                xkey: 'name',
                ykeys: ['attendCnt', 'absentCnt'],
                labels: ['응시자수', '결시자수'],
                //resize: true
            });
            return this;
        }
    });
});