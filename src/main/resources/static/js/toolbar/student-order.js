/**
 *
 */
define(function (require) {
    "use strict";

    var Toolbar = require('../dist/toolbar.js');
    var ToolbarModel = require('../model/model-status-toolbar.js');

    return Toolbar.extend({
        initialize: function (o) {
            this.el = o.el;
            this.parent = o.parent;
            this.param = o.param;
        },
        render: function () {
            this.$('#admissionNm').html(this.getOptions(ToolbarModel.getAdmissionNm()));
            this.$('#typeNm').html(this.getOptions(ToolbarModel.getTypeNm()));
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate()));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime()));
            this.$('#deptNm').html(this.getOptions(ToolbarModel.getDeptNm()));
            return this;
        },
        events: {
            'click #search': 'searchClicked',
            'change #admissionNm': 'admissionNmChanged',
            'change #typeNm': 'typeNmChanged',
            'change #examDate': 'examDateChanged'
        },
        searchClicked: function (e) {
            e.preventDefault();

            var _this = this;
            if (this.parent) {
                this.parent.search({
                    admissionNm: _this.$('#admissionNm').val(),
                    typeNm: _this.$('#typeNm').val(),
                    attendDate: _this.$('#attendDate').val(),
                    groupNm: _this.$('#groupNm').val(),
                    groupOrder: _this.$('#groupOrder').val(),
                    debateNm: _this.$('#debateNm').val(),
                    debateOrder: _this.$('#debateOrder').val()
                });
            }
        },
        admissionNmChanged: function (e) {
            var param = {
                admissionNm: e.currentTarget.value
            };
            this.$('#typeNm').html(this.getOptions(ToolbarModel.getTypeNm(param)));
            this.$('#examDate').html(this.getOptions(ToolbarModel.getExamDate(param)));
            this.$('#groupNm').html(this.getOptions(ToolbarModel.getGroupNm(param)));
        },
        typeNmChanged: function (e) {
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                typeNm: e.currentTarget.value
            };
            this.$('#examDate').html(this.getOptions(ToolbarModel.getExamDate(param)));
            this.$('#groupNm').html(this.getOptions(ToolbarModel.getGroupNm(param)));
        },
        examDateChanged: function (e) {
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                typeNm: this.$('#typeNm').val(),
                examDate: e.currentTarget.value
            };
            this.$('#groupNm').html(this.getOptions(ToolbarModel.getGroupNm(param)));
        }
    });
});