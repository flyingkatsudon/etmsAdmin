/**
 *
 */
define(function(require) {
    "use strict";

    require('layout');

    var Backbone = require('backbone');
    var Toolbar = require('./toolbar/toolbar-inspect-print.js');

    var layout;
    var view = Backbone.View.extend({
        render : function() {
            layout = this.$el.layout();
            this.toolbar = new Toolbar({el: layout.center.pane, parent:this}).render();
            $(window).trigger('resize');
        }
    });
    return view;
});