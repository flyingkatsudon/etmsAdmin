/**
 *
 */
define(function (require) {
    "use strict";

    var Backbone = require('backbone');
    var Template = require('text!tpl/toolbar.data-signature.html');

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
                    admissionNm: _this.$('#admissionNm').val(),
                    typeNm : _this.$('#typeNm').val(),
                    headNm : _this.$('#headNm').val(),
                    bldgNm : _this.$('#bldgNm').val(),
                    hallNm : _this.$('#hallNm').val(),
                    examNm: _this.$('#examNm').val(),
                    attendDt : _this.$('#attendDt').val(),
                    attendTm : _this.$('#attendTm').val(),
                    isSignature : _this.$('#isSignature').val()
                });
            }
        }
    });
});