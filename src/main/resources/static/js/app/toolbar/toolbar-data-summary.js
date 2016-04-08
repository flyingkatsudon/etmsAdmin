/**
 *
 */
define(function (require) {
    "use strict";

    var Toolbar = require('./toolbar-base.js');
    var Template = require('text!tpl/toolbar.data-summary.html');

    var ToolbarModel = require('../model/model-status-toolbar');

    return Toolbar.extend({
        initialize: function (o) {
            this.el = o.el;
            this.parent = o.parent;
        },
        render: function () {
            this.$el.html(Template);
            this.$('#admissionNm').html(this.getOptions(ToolbarModel.getAdmissionNm()));
            this.$('#typeNm').html(this.getOptions(ToolbarModel.getTypeNm()));
            this.$('#weirdNm').html(this.getOptions(ToolbarModel.getWeirdNm()));
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate()));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime()));

            return this;
        },
        events: {
            'click #search': 'searchClicked',
            'change #admissionNm': 'admissionNmChanged',
            'change #weirdNm': 'weirdNmChanged',
            'change #attendDate': 'attendDateChanged',
            'change #attendTime': 'attendTimeChanged'
        },
        searchClicked: function (e) {
            e.preventDefault();

            var _this = this;
            if (this.parent) {
                this.parent.search({
                    admissionNm: _this.$('#admissionNm').val(),
                    typeNm: _this.$('#typeNm').val(),
                    weirdNm : _this.$('#weirdNm').val(),
                    attendDate : _this.$('#attendDate').val(),
                    attendTime : _this.$('#attendTime').val()
                });
            }
        },

        admissionNmChanged: function (e){
            var param = {
                admissionNm: e.currentTarget.value
            };
            this.$('#typeNm').html(this.getOptions(ToolbarModel.getTypeNm(param)));
            this.$('#weirdNm').html(this.getOptions(ToolbarModel.getWeirdNm(param)));
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate(param)));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
        },

        typeNmChanged: function (e){
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                typeNm: e.currentTarget.value
            };
            this.$('#weirdNm').html(this.getOptions(ToolbarModel.getWeirdNm(param)));
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate(param)));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
        },

        weirdNmChanged: function (e){
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                typeNm: this.$('#typeNm').val(),
                weirdNm: e.currentTarget.value
            };
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate(param)));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
        },

        attendDateChanged: function (e){
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                typeNm: this.$('#typeNm').val(),
                weirdNm: this.$('#weirdNm').val(),
                attendDate: e.currentTarget.value
            };
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
        }
    });
});