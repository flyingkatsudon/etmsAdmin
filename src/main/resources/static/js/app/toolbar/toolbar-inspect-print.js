define(function(require){
    "use strict"

    var Toolbar = require('./toolbar-base.js');
    var Template = require('text!tpl/template.inspect-print.html');

    var ToolbarModel = require('../model/model-status-toolbar.js');
    var InspectModel = require('../model/model-inspect-print.js');

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
        events:{
            'change #examNm': 'examNmChanged',
            'change #attendDate': 'attendDateChanged',
            'change #attendTime': 'attendTimeChanged',
            'change #bldgNm': 'bldgNmChanged',
            'change #hallNm': 'hallNmChanged',

            'change #typeNm': 'typeNmChanged',
            'change #deptNm': 'deptNmChanged',
            'change #majorNm': 'majorNmChanged',

            'click #printHall' : 'printHallClicked',
            'click #printDept' : 'printDeptClicked',
            'click #printExamineeList' : 'printExamineeListClicked',
            'click #printExaminee' : 'printExamineeClicked'
        },
        examNmChanged: function (e) {
            var param = {
                examNm: e.currentTarget.value
            }
            this.$('#examNm').html(this.getOptions(ToolbarModel.getExamNm(param)));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
        },
        attendDateChanged: function (e) {
            var param = {
                examNm: this.$('#examNm').val(),
                attendDate: e.currentTarget.value
            }
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
        },
        attendTimeChanged: function (e) {
            var param = {
                examNm: this.$('#examNm').val(),
                attendDate: this.$('#attendDate').val(),
                attendTime: e.currentTarget.value
            }
            this.$('bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
        },
        bldgNmChanged: function (e) {
            var param = {
                examNm: this.$('#examNm').val(),
                attendDate: this.$('#attendDate').val(),
                attendTime: this.$('#attendTime').val(),
                bldgNm: e.currentTarget.value
            }
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
        },

        typeNmChanged: function (e){
            var param = {
                typeNm: e.currentTarget.value
            }
            this.$('#deptNm').html(this.getOptions(ToolbarModel.getDeptNm(param)));
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm(param)));
        },
        deptNmChanged: function (e) {
            var param = {
                typeNm: this.$('#typeNm').val(),
                deptNm: e.currentTarget.value
            }
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm(param)));
        },
        printHallClicked : function(){
            var param = {
                examNm : this.$('#examNm').val(),
                bldgNm : this.$('#bldgNm').val(),
                hallNm: this.$('#hallNm').val(),
                attendDate: this.$('#attendDate').val(),
                attendTime: this. $('#attendTime').val()
            }
            InspectModel.openPrintWindow(param);
            //this.openPrintWindow(param);
        },/*
        printDeptClicked: function(e){
            e.preventDefault();
            var url = $(e.target).data('url');
            var dlgDownload = new DlgDownload({
                url: url
            }).render();
            return false;
        },
        printExamineeListClicked: function(e){
            e.preventDefault();
            var url = $(e.target).data('url');
            var dlgDownload = new DlgDownload({
                url: url
            }).render();
            return false;
        },
        printExamineeClicked: function(e){
            e.preventDefault();
            var url = $(e.target).data('url');
            var dlgDownload = new DlgDownload({
                url: url
            }).render();
            return false;
        }*/
    });
});