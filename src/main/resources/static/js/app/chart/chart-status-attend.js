define(function (require) {
    "use strict";
    var Chart = require('./chart-base.js');

    return Chart.extend({
        url: 'status/attend/chart',
        options: {
            type: 'bar',
            data: {
                datasets: [
                    {type: 'bar', backgroundColor: '#0000CD'},
                    {type: 'line', backgroundColor: 'gray', fill: false, borderColor: 'gray'},
                    {type: 'bar', backgroundColor: 'tomato'},
                    {type: 'line', fill: false}
                ]
            },
        }
    });
});