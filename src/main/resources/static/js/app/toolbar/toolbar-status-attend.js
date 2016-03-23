/**
 *
 */
define(function (require) {
    "use strict";

    var Backbone = require('backbone');
    var Template = require('text!tpl/toolbar.status-attend.html');

    return Backbone.View.extend({
        initialize: function (o) {
            this.el = o.el;
            this.parent = o.parent;
        },
        render: function () {
            this.$el.html(Template);
           // var hallCd = this.$('#hallCd').html(this.getOptions((this.list).options));
            return this;
        },
        events: {
            'click #search': 'searchClicked'
        },
        searchClicked: function (e) {
            var _this = this;
            if(this.parent){
                this.parent.search({
                    admissionNm: _this.$('#admissionNm').val(),
                    admissionType : _this.$('#admissionType').val()
                });
            }
        }
    });
});