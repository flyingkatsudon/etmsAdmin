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
                    {type: 'bar', backgroundColor: 'Tomato'}
                ]
            }
        }
    });
});