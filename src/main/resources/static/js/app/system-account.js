define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var List = require('../grid/system-account.js');
    var Toolbar = require('../toolbar/system-account.js');
    var Template = require('text!/tpl/system-account.html');
    var AddAcount = require('../addAccount.js');

    return Backbone.View.extend({
        render: function () {
            this.$el.html(Template);
            this.toolbar = new Toolbar({el: '.hm-ui-search', parent: this}).render();
            this.list = new List({el: '.hm-ui-grid', parent: this}).render();

            $('#add').click(function(){
               new AddAcount().render();
            });

        }, search: function (o) {
            this.list.search(o);
        }
    });
});