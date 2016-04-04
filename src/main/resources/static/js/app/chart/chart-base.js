define(function (require) {

    require('chartjs');

    var Backbone = require('backbone');

    return Backbone.View.extend({
        getChart: function(data){
            var html = $("<canvas width='1650px' height='400px'></canvas>");

            var ctx = html.get(0).getContext("2d");
            //var lineBarChart = new Chart(ctx).LineBar(data, options);
            var lineBarChart = new Chart(ctx).LineBar(data);
            new Chart(ctx).LineBar(data, {
                barShowStroke: false
            });

            return html;
        }
    })
});