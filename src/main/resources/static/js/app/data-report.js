define(function (require) {
    "use strict";

    require('layout');

    var Backbone = require('backbone');
    var List = require('./grid/grid-data-report.js');

    var layout;

    return Backbone.View.extend({
        render: function () {
            layout = this.$el.layout();
            var list = new List({el: layout.center.pane}).render();
            $(window).trigger('resize');
        }
    });
});