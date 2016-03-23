define(function (require) {
    "use strict";

    require('layout');

    var Backbone = require('backbone');
    var Toolbar = require('./toolbar/toolbar-data-noIdCard.js');
    var List = require('./grid/grid-data-noIdCard.js');

    var layout;

    return Backbone.View.extend({
        render: function () {
            layout = this.$el.layout();
            var list = new List({el: layout.center.pane}).render();
            var toolbar = new Toolbar({el: layout.north.pane, list: list}).render();
            $(window).trigger('resize');
        }
    });
});