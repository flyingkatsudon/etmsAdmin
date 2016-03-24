define(function (require) {
    "use strict";

    var Backbone = require('backbone');

    var Collection = Backbone.Collection.extend({
        url: 'status/toolbar',
        initialize: function () {
            this.fetch();
        },
        parse: function (data) {
            console.log(data);
            return data;
        }
    });
    return new Collection();
});