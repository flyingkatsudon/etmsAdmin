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
        },
        render: function () {
            this.$('#admissionNm').html(this.getOptions(ToolbarModel.getAdmissionNm()));
            this.$('#typeNm').html(this.getOptions(ToolbarModel.getTypeNm()));
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate()));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime()));
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm()));
            this.$('#attendHeadNm').html(this.getOptions(ToolbarModel.getAttendHeadNm()));
            this.$('#attendBldgNm').html(this.getOptions(ToolbarModel.getAttendBldgNm()));
            this.$('#attendHallNm').html(this.getOptions(ToolbarModel.getAttendHallNm()));
            return this;
        },
        events: {
            'click #search': 'searchClicked',
            'change #admissionNm': 'admissionNmChanged',
            'change #typeNm': 'typeNmChanged',
            'change #attendDate': 'attendDateChanged',
            'change #attendTime': 'attendTimeChanged',
            'change #majorNm': 'majorNmChanged',
            'change #attendHeadNm': 'attendHeadNmChanged',
            'change #attendBldgNm': 'attendBldgNmChanged',
            'change #attendHallNm': 'attendHallNmChanged',
            'change #isAttend': 'isAttendChanged'
        },
        searchClicked: function (e) {
            e.preventDefault();

            var _this = this;
            if (this.parent) {
                this.parent.search({
                    admissionNm: _this.$('#admissionNm').val(),
                    typeNm : _this.$('#typeNm').val(),
                    attendDate : _this.$('#attendDate').val(),
                    attendTime : _this.$('#attendTime').val(),
                    majorNm : _this.$('#majorNm').val(),
                    examineeCd: _this.$('#examineeCd').val(),
                    examineeNm: _this.$('#examineeNm').val(),
                    attendHeadNm: _this.$('#attendHeadNm').val(),
                    attendBldgNm: _this.$('#attendBldgNm').val(),
                    attendHallNm : _this.$('#attendHallNm').val(),
                    isAttend : _this.$('#isAttend').val(),
                    paperCd: _this.$('#paperCd').val()
                });
            }
        },
        admissionNmChanged: function (e) {
            var param = {
                admissionNm: e.currentTarget.value
            };
            this.$('#typeNm').html(this.getOptions(ToolbarModel.getTypeNm(param)));
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate(param)));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm(param)));
            this.$('#attendHeadNm').html(this.getOptions(ToolbarModel.getAttendHeadNm(param)));
            this.$('#attendBldgNm').html(this.getOptions(ToolbarModel.getAttendBldgNm(param)));
            this.$('#attendHallNm').html(this.getOptions(ToolbarModel.getAttendHallNm(param)));
        },
        typeNmChanged: function (e) {
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                typeNm: e.currentTarget.value
            };
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate(param)));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm(param)));
            this.$('#attendHeadNm').html(this.getOptions(ToolbarModel.getAttendHeadNm(param)));
            this.$('#attendBldgNm').html(this.getOptions(ToolbarModel.getAttendBldgNm(param)));
            this.$('#attendHallNm').html(this.getOptions(ToolbarModel.getAttendHallNm(param)));
        },
        attendDateChanged: function (e) {
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                typeNm: this.$('#typeNm').val(),
                attendDate: e.currentTarget.value
            };
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm(param)));
            this.$('#attendHeadNm').html(this.getOptions(ToolbarModel.getAttendHeadNm(param)));
            this.$('#attendBldgNm').html(this.getOptions(ToolbarModel.getAttendBldgNm(param)));
            this.$('#attendHallNm').html(this.getOptions(ToolbarModel.getAttendHallNm(param)));
        },
        attendTimeChanged: function (e) {
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                typeNm: this.$('#typeNm').val(),
                attendDate: this.$('#attendDate').val(),
                attendTime: e.currentTarget.value
            };
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm(param)));
            this.$('#attendHeadNm').html(this.getOptions(ToolbarModel.getAttendHeadNm(param)));
            this.$('#attendBldgNm').html(this.getOptions(ToolbarModel.getAttendBldgNm(param)));
            this.$('#attendHallNm').html(this.getOptions(ToolbarModel.getAttendHallNm(param)));
        },
        majorNmChanged: function (e) {
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                typeNm: this.$('#typeNm').val(),
                attendDate: this.$('#attendDate').val(),
                attendTime: this.$('#attendTime').val(),
                majorNm: e.currentTarget.value
            };
            this.$('#attendHeadNm').html(this.getOptions(ToolbarModel.getAttendHeadNm(param)));
            this.$('#attendBldgNm').html(this.getOptions(ToolbarModel.getAttendBldgNm(param)));
            this.$('#attendHallNm').html(this.getOptions(ToolbarModel.getAttendHallNm(param)));
        },
        attendHeadNmChanged: function (e) {
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                typeNm: this.$('#typeNm').val(),
                attendDate: this.$('#attendDate').val(),
                attendTime: this.$('#attendTime').val(),
                majorNm: this.$('#majorNm').val(),
                attendHeadNm: e.currentTarget.value
            };
            this.$('#attendBldgNm').html(this.getOptions(ToolbarModel.getAttendBldgNm(param)));
            this.$('#attendHallNm').html(this.getOptions(ToolbarModel.getAttendHallNm(param)));
        },

        attendBldgNmChanged: function (e) {
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                typeNm: this.$('#typeNm').val(),
                attendDate: this.$('#attendDate').val(),
                attendTime: this.$('#attendTime').val(),
                majorNm: this.$('#majorNm').val(),
                attendHeadNm: this.$('#attendHeadNm').val(),
                attendBldgNm: e.currentTarget.value
            };
            this.$('#attendHallNm').html(this.getOptions(ToolbarModel.getAttendHallNm(param)));
        }
    });
});