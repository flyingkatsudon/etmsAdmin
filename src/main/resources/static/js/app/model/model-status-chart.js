define(function (require) {
    "use strict";

    var Backbone = require('backbone');

    return Backbone.Collection.extend({
        initialize: function () {
            this.fetch({
                async: false
            });
        },
        getData: function () {
            var data = this.models[0].attributes;
            var line =  {
                type: "line"
            };
            var bar =  {
                type: "bar"
            };
            var datasets = data["datasets"];
            for (var i = 0; i < datasets.length; i++) {
                if (datasets[i].label === "응시율") $.extend(datasets[i], line);
                else if (datasets[i].label === "결시율") $.extend(datasets[i], bar);
            }
            return data;
        },
        getLabels: function (obj) {
            //console.log(Object.keys(obj));
            return this.getObjectList(obj, 'labels', 'labels');
        },
        getDatasets: function (obj) {
            //console.log(Object.keys(obj));
            return this.getObjectList(obj, 'datasets', 'datasets');
        }
    });
});