/**
 *
 */
define(function (require) {
    "use strict";

    var Backbone = require('backbone');
    var Template = require('text!tpl/toolbar.status-examinee.html');

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
                    admissionCd: _this.$('#admissionCd').val(),
                    admissionType : _this.$('#admissionType').val(),
                    deptNm : _this.$('#deptNm').val(),
                    majorNm : _this.$('#majorNm').val(),
                    groupNm : _this.$('#groupNm').val(),
                    attendDt : _this.$('#attendDt').val(),
                    attendTm : _this.$('#attendTm').val(),
                    examineeCd : _this.$('#examineeCd').val(),
                    examineeNm : _this.$('#examineeNm').val()
                });
            }
        }
    });
});