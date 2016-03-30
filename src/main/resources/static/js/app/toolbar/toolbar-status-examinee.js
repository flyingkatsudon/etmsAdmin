define(function (require) {
    "use strict";

    var Toolbar = require('./toolbar-base.js');
    var Template = require('text!tpl/toolbar.status-examinee.html');

    var ToolbarModel = require('../model/model-status-toolbar.js');

    return Toolbar.extend({
        initialize: function (o) {
            this.el = o.el;
            this.parent = o.parent;
        },
        render: function () {
            this.$el.html(Template);
            this.$('#admissionNm').html(this.getOptions(ToolbarModel.getAdmissionNm()));
            this.$('#deptNm').html(this.getOptions(ToolbarModel.getDeptNm()));
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm()));
            this.$('#groupNm').html(this.getOptions(ToolbarModel.getGroupNm()));
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate()));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime()));
            return this;
        },
        events: {
            'click #search': 'searchClicked',
            'change #admissionNm': 'admissionNmChanged',
            'change #deptNm': 'deptNmChanged',
            'change #majorNm': 'majorNmChanged',
            'change #groupNm': 'groupNmChanged',
            'change #attendDate': 'attendDateChanged'
        },
        searchClicked: function (e) {
            var _this = this;
            if (this.parent) {
                this.parent.search({
                    admissionNm: _this.$('#admissionNm').val(),
                    attendTypeNm : _this.$('#attendTypeNm').val(),
                    deptNm : _this.$('#deptNm').val(),
                    majorNm : _this.$('#majorNm').val(),
                    groupNm : _this.$('#groupNm').val(),
                    attendDate : _this.$('#attendDate').val(),
                    attendTime : _this.$('#attendTime').val(),
                    examineeCd: _this.$('#examineeCd').val(),
                    examineeNm:_this.$('#examineeNm').val()
                });
            }
        },
        admissionNmChanged: function (e){
            var param = {
                admissionNm: e.currentTarget.value
            }
            this.$('#deptNm').html(this.getOptions(ToolbarModel.getDeptNm(param)));
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm(param)));
            this.$('#groupNm').html(this.getOptions(ToolbarModel.getGroupNm(param)));
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate(param)));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
        },

        deptNmChanged: function (e){
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                deptNm: e.currentTarget.value
            }
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm(param)));
            this.$('#groupNm').html(this.getOptions(ToolbarModel.getGroupNm(param)));
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate(param)));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
        },

        majorNmChanged: function (e){
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                deptNm: this.$('#deptNm').val(),
                majorNm: e.currentTarget.value
            }
            this.$('#groupNm').html(this.getOptions(ToolbarModel.getGroupNm(param)));
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate(param)));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
        },

        groupNmChanged: function (e){
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                deptNm: this.$('#deptNm').val(),
                majorNm: this.$('#majorNm').val(),
                groupNm: e.currentTarget.value
            }
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate(param)));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
        },

        attendDateChanged: function (e){
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                deptNm: this.$('#deptNm').val(),
                majorNm: this.$('#majorNm').val(),
                groupNm: this.$('#groupNm').val(),
                attendDate: e.currentTarget.value
            }
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
        }
    });
});