define(function (require) {
    "use strict";
    var Chart = require('./chart-base.js');

    return Chart.extend({
        url: 'chart/attend',
        options: {
            type: 'bar',
            data: {
                datasets: [
                    {type: 'bar', backgroundColor: 'MediumBlue'},
                    {type: 'line', backgroundColor: 'Gray', fill: false, borderColor: 'Gray'},
                    {type: 'bar', backgroundColor: 'Tomato'},
                    {type: 'line', fill: false}
                ]
            },
        }
    });
});