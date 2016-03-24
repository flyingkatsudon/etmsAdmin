/**
 *
 */
define(function(require){
    "use strict";

    require('jqueryui');

    var Backbone = require('backbone');

    var view = Backbone.View.extend({
        options : {
            title : '',
            modal : true,
            close : function() { $(this).dialog('destroy').remove(); }
        },
        initialize : function(options) {
            var _this = this;
            this.options = $.extend({}, _this.options, options);
        },
        render : function() {
            var _this = this;
            this.$el.dialog(_this.options);
            return this;
        },
        getButtons : function() {
            return this.$el.dialog('option', 'buttons');
        },
        addButtons : function(buttons) {
            this.$el.dialog('option', 'buttons', buttons);
        }
    });
    return view;
});