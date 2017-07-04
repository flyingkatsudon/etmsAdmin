define(function (require) {
    "use strict";

    var Backbone  = require('backbone');
    var List      = require('../grid/system-device.js');
    var Toolbar   = require('../toolbar/system-device.js');
    var Template  = require('text!/tpl/system-device.html');

    return Backbone.View.extend({
        render: function () {
            this.$el.html(Template);
            this.toolbar = new Toolbar({el: '.hm-ui-search', parent: this}).render();
            this.list = new List({el: '.hm-ui-grid', parent: this}).render();
        }, search: function (o) {
            console.log(o);
            this.list.search(o);
        }
    });
});