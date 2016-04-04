define(function (require) {
    "use strict";

    var Backbone = require('backbone');
    var Template = require('text!tpl/chart.status-dept.html');
    require('chartjs');

    var html = _.template(Template);

    return Backbone.View.extend({
        render : function(){
            this.$el.html(html);
            var ctx = this.$('canvas')[0].getContext('2d');

            $.ajax({
                url : 'chart/dept',
                success : function(data){
                    console.log(data);
                    data.datasets[0].fillColor = "rgba(220,220,220,0.5)";
                    data.datasets[0].strokeColor = "rgba(220,220,220,0.8)";
                    new Chart(ctx).Bar(data, option);
                }
            });





            /*//var lineBarChart = new Chart(ctx).LineBar(data, options);
            var lineBarChart = new Chart(ctx).LineBar(data);
            new Chart(ctx).LineBar(data, {
                barShowStroke: false
            }); this.$('#canvas').*/


        }
    });
});