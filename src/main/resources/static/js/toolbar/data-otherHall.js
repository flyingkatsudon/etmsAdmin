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
            this.$('#headNm').html(this.getOptions(ToolbarModel.getHeadNm()));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm()));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm()));
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
            'change #headNm': 'headNmChanged',
            'change #bldgNm': 'bldgNmChanged',
            'change #hallNm': 'hallNmChanged',
            'change #attendBldgNm': 'attendBldgNmChanged',
            'change #attendHallNm': 'attendHallNmChanged'
        },
        searchClicked: function (e) {
            e.preventDefault();

            var _this = this;
            if (this.parent) {
                this.parent.search({
                    admissionNm: _this.$('#admissionNm').val(),
                    typeNm: _this.$('#typeNm').val(),
                    attendDate : _this.$('#attendDate').val(),
                    attendTime : _this.$('#attendTime').val(),
                    headNm: _this.$('#headNm').val(),
                    bldgNm: _this.$('#bldgNm').val(),
                    hallNm : _this.$('#hallNm').val(),
                    attendBldgNm: _this.$('#attendBldgNm').val(),
                    attendHallNm : _this.$('#attendHallNm').val(),
                    examineeCd : _this.$('#examineeCd').val(),
                    examineeNm : _this.$('#examineeNm').val()
                });
            }
        },
        admissionNmChanged: function (e){
            var param = {
                admissionNm: e.currentTarget.value
            };
            this.$('#typeNm').html(this.getOptions(ToolbarModel.getTypeNm(param)));
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate(param)));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
            this.$('#headNm').html(this.getOptions(ToolbarModel.getHeadNm(param)));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
            this.$('#attendBldgNm').html(this.getOptions(ToolbarModel.getOtherBldgNm(param)));
            this.$('#attendHallNm').html(this.getOptions(ToolbarModel.getOtherHallNm(param)));
        },
        typeNmChanged: function (e){
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                typeNm: e.currentTarget.value
            };
            this.$('#typeNm').html(this.getOptions(ToolbarModel.getTypeNm(param)));
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate(param)));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
            this.$('#headNm').html(this.getOptions(ToolbarModel.getHeadNm(param)));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
            this.$('#attendBldgNm').html(this.getOptions(ToolbarModel.getOtherBldgNm(param)));
            this.$('#attendHallNm').html(this.getOptions(ToolbarModel.getOtherHallNm(param)));
        },
        attendDateChanged: function (e){
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                typeNm: this.$('#typeNm').val(),
                attendDate: e.currentTarget.value
            };
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
            this.$('#attendBldgNm').html(this.getOptions(ToolbarModel.getOtherBldgNm(param)));
            this.$('#attendHallNm').html(this.getOptions(ToolbarModel.getOtherHallNm(param)));
        },
        attendTimeChanged: function (e){
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                typeNm: this.$('#typeNm').val(),
                attendDate: this.$('#attendDate').val(),
                attendTime: e.currentTarget.value
            };
            this.$('#headNm').html(this.getOptions(ToolbarModel.getHeadNm(param)));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
            this.$('#attendBldgNm').html(this.getOptions(ToolbarModel.getOtherBldgNm(param)));
            this.$('#attendHallNm').html(this.getOptions(ToolbarModel.getOtherHallNm(param)));
        },
        headNmChanged: function (e){
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                typeNm: this.$('#typeNm').val(),
                attendDate: this.$('#attendDate').val(),
                attendTime: this.$('#attendTime').val(),
                headNm: e.currentTarget.value
            };
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
            this.$('#attendBldgNm').html(this.getOptions(ToolbarModel.getOtherBldgNm(param)));
            this.$('#attendHallNm').html(this.getOptions(ToolbarModel.getOtherHallNm(param)));
        },
        bldgNmChanged: function (e){
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                typeNm: this.$('#typeNm').val(),
                attendDate: this.$('#attendDate').val(),
                attendTime: this.$('#attendTime').val(),
                headNm: this.$('#headNm').val(),
                bldgNm: e.currentTarget.value
            };
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
            this.$('#attendBldgNm').html(this.getOptions(ToolbarModel.getOtherBldgNm(param)));
            this.$('#attendHallNm').html(this.getOptions(ToolbarModel.getOtherHallNm(param)));
        },
        hallNmChanged: function (e){
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                typeNm: this.$('#typeNm').val(),
                attendDate: this.$('#attendDate').val(),
                attendTime: this.$('#attendTime').val(),
                headNm: this.$('#headNm').val(),
                bldgNm: this.$('#bldgNm').val(),
                hallNm: e.currentTarget.value
            };
            this.$('#attendBldgNm').html(this.getOptions(ToolbarModel.getOtherBldgNm(param)));
            this.$('#attendHallNm').html(this.getOptions(ToolbarModel.getOtherHallNm(param)));
        },
        attendBldgNmChanged: function (e){
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                typeNm: this.$('#typeNm').val(),
                attendDate: this.$('#attendDate').val(),
                attendTime: this.$('#attendTime').val(),
                headNm: this.$('#headNm').val(),
                bldgNm: this.$('#bldgNm').val(),
                hallNm: this.$('#hallNm').val(),
                attendBldgNm: e.currentTarget.value
            };
            this.$('#attendHallNm').html(this.getOptions(ToolbarModel.getOtherHallNm(param)));
        }
    });
});