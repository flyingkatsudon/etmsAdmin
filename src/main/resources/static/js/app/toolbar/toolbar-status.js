/**
 *
 */
define(function (require) {
    "use strict";

    var Backbone = require('backbone');
    var Template = require('text!tpl/toolbar-status.html');

    return Backbone.View.extend({
        render: function () {
            this.$el.html(Template);
            return this;
        }
    });
});