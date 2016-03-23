/**
 *
 */
define(function (require) {
    "use strict";

    var Backbone = require('backbone');
    var Template = require('text!tpl/toolbar.data-recheck.html');

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
                    deptNm : _this.$('#deptNm').val(),
                    majorNm : _this.$('#majorNm').val(),
                    headNm : _this.$('#headNm').val(),
                    bldgNm : _this.$('#bldgNm').val(),
                    hallNm : _this.$('#hallNm').val(),
                    examineeCd : _this.$('#examineeCd').val(),
                    examineeNm : _this.$('#examineeNm').val(),
                    attendDt : _this.$('#attendDt').val(),
                    attendTm : _this.$('#attendTm').val()
                });
            }
        }
    });
});