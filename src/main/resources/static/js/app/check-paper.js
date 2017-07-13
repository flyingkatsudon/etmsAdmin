define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var List = require('../grid/check-paper.js');
    var Toolbar = require('../toolbar/check-paper.js');
    var Template = require('text!/tpl/check-paper.html');

    return Backbone.View.extend({
        render: function () {
            this.$el.html(Template);

            this.toolbar = new Toolbar({el: '.hm-ui-search', parent: this}).render();
            this.list = new List({el: '.hm-ui-grid'}).render();

            $('.hm-ui-grid').hide();
            $('.notice').show();

        }, search: function (o) {
            this.list.search(o);
            this.list.$grid.jqGrid('setGridParam', {url: 'check/paper.json', datatype: 'json'}).trigger('reloadGrid');

            $('.hm-ui-grid').fadeIn(50);
            $('.notice').hide();
        }
    });
});