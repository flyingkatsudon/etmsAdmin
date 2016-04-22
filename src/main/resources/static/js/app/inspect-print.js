/**
 *
 */
define(function(require) {
    "use strict";

    require('layout');

    var Backbone = require('backbone');
    var Template = require('./content-inspect-print.js');

    var layout;
    var view = Backbone.View.extend({
        render : function() {
            layout = this.$el.layout();
            this.template = new Template({el: layout.center.pane, parent:this}).render();
            $(window).trigger('resize');
        }
    });
    return view;
});