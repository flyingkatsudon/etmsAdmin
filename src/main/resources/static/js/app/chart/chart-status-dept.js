define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    return Backbone.View.extend({
        render: function () {
            this.$el.html("chart area");
            return this;
        }
    });
});