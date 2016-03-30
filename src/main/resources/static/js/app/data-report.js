/**
 *
 */
define(function(require) {
    "use strict";

    require('layout');

    var Backbone = require('backbone');
    var Grid = require('./grid/grid-data-report.js');

    var layout;
    var view = Backbone.View.extend({
        render : function() {
            layout = this.$el.layout();
            this.grid = new Grid({el: layout.center.pane}).render();

            $(window).trigger('resize');
        }
    });

    return view;
});