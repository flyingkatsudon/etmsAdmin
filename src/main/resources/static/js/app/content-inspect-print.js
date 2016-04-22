define(function (require) {
    "use strict";

    var Toolbar = require('./toolbar/toolbar-base.js');
    var Template = require('text!tpl/content.inspect-print.html');

    var ToolbarModel = require('./model/model-status-toolbar.js');

    var DlgPdf = require('./dlg/dlg-pdf.js');

    return Toolbar.extend({
        initialize: function (o) {
            this.el = o.el;
            this.parent = o.parent;
        },
        render: function () {
            this.$el.html(Template);
            this.$('#examNm').html(this.getOptions(ToolbarModel.getExamNm()));
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate()));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime()));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm()));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm()));

            this.$('#typeNm').html(this.getOptions(ToolbarModel.getTypeNm()));
            this.$('#deptNm').html(this.getOptions(ToolbarModel.getDeptNm()));
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm()));
        },
        events: {
            'change #examNm': 'examNmChanged',
            'change #bldgNm': 'bldgNmChanged',
            'change #hallNm': 'hallNmChanged',
            'change #attendDate': 'attendDateChanged',
            'change #attendTime': 'attendTimeChanged',

            'change #typeNm': 'typeNmChanged',
            'change #deptNm': 'deptNmChanged',
            'change #majorNm': 'majorNmChanged',

            'click #printHall': 'printHallClicked',
            'click #printDept': 'printDeptClicked',
            'click #printExamineeList': 'printExamineeListClicked',
            'click #printExaminee': 'printExamineeClicked'
        },

        examNmChanged: function (e) {
            var param = {
                examNm: e.currentTarget.value
            };
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate(param)));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
        },

        bldgNmChanged: function (e) {
            var param = {
                examNm: this.$('#examNm').val(),
                bldgNm: e.currentTarget.value
            };
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate(param)));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
        },

        hallNmChanged: function (e) {
            var param = {
                examNm: this.$('#examNm').val(),
                bldgNm: this.$('#bldgNm').val(),
                hallNm: e.currentTarget.value
            };
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate(param)));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
        },

        attendDateChanged: function (e) {
            var param = {
                examNm: this.$('#examNm').val(),
                bldgNm: this.$('#bldgNm').val(),
                hallNm: this.$('#hallNm').val(),
                attendDate: e.currentTarget.value
            };
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
        },

        typeNmChanged: function (e) {
            var param = {
                typeNm: e.currentTarget.value
            };
            this.$('#deptNm').html(this.getOptions(ToolbarModel.getDeptNm(param)));
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm(param)));
        },

        deptNmChanged: function (e) {
            var param = {
                typeNm: this.$('#typeNm').val(),
                deptNm: e.currentTarget.value
            };
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm(param)));
        },

        printHallClicked: function (e) {
            e.preventDefault();

            var param = {
                examNm: this.$('#examNm').val(),
                bldgNm: this.$('#bldgNm').val(),
                hallNm: this.$('#hallNm').val(),
                attendDate: this.$('#attendDate').val(),
                attendTime: this.$('#attendTime').val()
            };
            this.openPrintWindow(param);
        },

        printDeptClicked: function (e) {
            e.preventDefault();

            var param = {
                typeNm: this.$('#typeNm').val(),
                deptNm: this.$('#deptNm').val(),
                majorNm: this.$('#majorNm').val()
            };
            this.openPrintWindow(param);
        },

        printExamineeListClicked: function (e) {
            e.preventDefault();

            var param = {
                firstExamineeCd: this.$('#firstExamineeCd').val(),
                lastExamineeCd: this.$('#lastExamineeCd').val()
            };

            if (param.firstExamineeCd || param.lastExamineeCd) {
                this.openPrintWindow(param);
            }
        },

        printExamineeClicked: function (e) {
            e.preventDefault();

            var param = {
                examineeCd: this.$('#examineeCd').val(),
                examineeNm: this.$('#examineeNm').val()
            };

            if (param.examineeCd || param.examineeNm) {
                this.openPrintWindow(param);
            }
        },

        openPrintWindow: function (param) {
            new DlgPdf({url: 'report/examineeId?' + $.param(param)}).render();
        }
    });
});