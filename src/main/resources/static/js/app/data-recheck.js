define(function (require) {
    "use strict";

    require('layout');

    var Backbone = require('backbone');
    var Toolbar = require('./toolbar/toolbar-data-recheck.js');
    var List = require('./grid/grid-data-recheck.js');

    var layout;

    return Backbone.View.extend({
        render: function () {
            layout = this.$el.layout();
            this.list = new List({el: layout.center.pane}).render();
            this.toolbar = new Toolbar({el: layout.north.pane, parent: this}).render();

            $(window).trigger('resize');
        },
        search: function (o){
            this.list.search(o);
        }
    });
});