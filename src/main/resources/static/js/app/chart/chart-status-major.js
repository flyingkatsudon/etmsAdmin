define(function (require) {
    "use strict";
    var Chart = require('./chart-base.js');

    return Chart.extend({
        url: 'chart/major',
        options: {
            type: 'bar',
            data: {datasets: [{type: 'line'}]}
        }
    });
});