/**
 *
 */
define(function (require) {
    "use strict";

    var Backbone = require('backbone');
    var Template = require('text!tpl/toolbar.status.html');

    return Backbone.View.extend({
        initialize: function (o) {
            this.list = o.list;
            this.el = o.el;
        },
        render: function () {
            this.$el.html(Template);
            return this;
        },
        events: {
            'click #search': 'searchClicked'
        },
        searchClicked: function (e) {
            var _this = this;
            if (this.list) {
                this.list.search({
                    hallCd: _this.$('#hallCd').val(),
                    typeNm : _this.$('#typeNm').val(),
                    attendDate : _this.$('#attendDate').val(),
                    attendTime : _this.$('#attendTime').val()
                });
            }
        }
    });
});