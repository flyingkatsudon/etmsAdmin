/**
 *
 */
define(function (require) {
    "use strict";

    var Toolbar = require('./toolbar-base.js');
    var Template = require('text!tpl/toolbar.status-dept.html');

    var ToolbarModel = require('../model/model-status-toolbar.js');

    return Toolbar.extend({
        initialize: function (o) {
            this.list = o.list;
            this.el = o.el;
        },
        render: function () {
            this.$el.html(Template);
            this.$('#admissionNm').html(this.getOptions(ToolbarModel.getAdmissionNm()));
            this.$('#deptNm').html(this.getOptions(ToolbarModel.getDeptNm()));
            return this;
        },
        events: {
            'click #search': 'searchClicked',
            'change #admissionNm': 'admissionNmChanged'
        },
        searchClicked: function (e) {
            var _this = this;
            if (this.list) {
                this.list.search({
                    admissionNm: _this.$('#admissionNm').val(),
                    admissionType: _this.$('#admissionType').val(),
                    deptNm: _this.$('#deptNm').val()
                });
            }
        },
        admissionNmChanged: function (e) {
            var param = {
                admissionNm: e.currentTarget.value
            };
            this.$('#deptNm').html(this.getOptions(ToolbarModel.getDeptNm(param)));
        }
    });
});