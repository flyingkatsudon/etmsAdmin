define(function (require) {
    "use strict";

    require('layout');

    var Backbone = require('backbone');

    var Toolbar = require('./toolbar/toolbar-status');
    var List = require('./grid/grid-status-attend');

    var layout;

    return Backbone.View.extend({
        render: function () {
            layout = this.$el.layout();
            this.toolbar = new Toolbar({el: layout.north.pane}).render();
            this.list = new List({el: layout.center.pane, parent: this}).render();

            $(window).trigger('resize');
        },
        events : {
            'click #hm-ui-toolbar-search' : 'searchClicked'
        },
        searchClicked : function(e){
            var formData = this.toolbar.getSearch();
            this.list.reloadGrid(formData);
        }
    });
});