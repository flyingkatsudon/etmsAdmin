/**
 *
 */
define(function (require) {
    "use strict";

    var Toolbar = require('./toolbar-base.js');
    var Template = require('text!tpl/toolbar.status-hall.html');

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
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm()));

            return this;
        },
        events: {
            'click #search': 'searchClicked',
            'change #admissionNm': 'admissionNmChanged',
            'change #deptNm': 'deptNmChanged'
        },
        searchClicked: function (e) {
            var _this = this;
            if (this.list) {
                this.list.search({
                    admissionNm: _this.$('#admissionNm').val(),
                    admissionType: _this.$('#admissionType').val(),
                    deptNm: _this.$('#deptNm').val(),
                    majorNm: _this.$('#majorNm').val(),
                    headNm: _this.$('#headNm').val(),
                    bldgNm: _this.$('#bldgNm').val(),
                    hallNm: _this.$('#hallNm').val(),
                    attendDt: _this.$('#attendDt').val(),
                    attendTm: _this.$('#attendTm').val()
                });
            }
        },
        admissionNmChanged: function (e) {
            var param = {
                admissionNm: e.currentTarget.value
            };
            this.$('#deptNm').html(this.getOptions(ToolbarModel.getDeptNm(param)));
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm(param)));
        },
        deptNmChanged: function (e) {
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                deptNm: e.currentTarget.value
            };
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm(param)));
        }
    });
});