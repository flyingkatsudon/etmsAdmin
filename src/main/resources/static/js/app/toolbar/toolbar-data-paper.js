/**
 *
 */
define(function (require) {
    "use strict";

    var Toolbar = require('./toolbar-base.js');
    var Template = require('text!tpl/toolbar.data-paper.html');

    var ToolbarModel = require('../model/model-status-toolbar.js');

    return Toolbar.extend({
        initialize: function (o) {
            this.el = o.el;
            this.parent = o.parent;
        },
        render: function () {
            this.$el.html(Template);
            this.$('#admissionNm').html(this.getOptions(ToolbarModel.getAdmissionNm()));
            this.$('#typeNm').html(this.getOptions(ToolbarModel.getTypeNm()));
            this.$('#headNm').html(this.getOptions(ToolbarModel.getHeadNm()));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm()));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm()));
            return this;
        },
        events: {
            'click #search': 'searchClicked',
            'click #admissionNm': 'admissionNmChanged',
            'click #typeNm': 'typeNmChanged',
            'click #headNm': 'headNmChanged',
            'click #bldgNm': 'bldgNmChanged'
        },
        searchClicked: function (e) {
            e.preventDefault();

            var _this = this;
            if (this.list) {
                this.list.search({
                    admissionNm: _this.$('#admissionNm').val(),
                    typeNm : _this.$('#typeNm').val(),
                    headNm : _this.$('#headNm').val(),
                    bldgNm: _this.$('#bldgNm').val(),
                    hallNm : _this.$('#hallNm').val(),
                    examineeCd : _this.$('#examineeCd').val(),
                    examineeNm : _this.$('#examineeNm').val()
                });
            }
        },
        admissionNmChanged: function (e){
            var param = {
                admissionNm: e.currentTarget.value
            };
            this.$('#deptNm').html(this.getOptions(ToolbarModel.getDeptNm(param)));
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm(param)));
            this.$('#headNm').html(this.getOptions(ToolbarModel.getHeadNm(param)));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate(param)));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
        },
        typeNmChanged: function (e){
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                typeNm: e.currentTarget.value
            };
            this.$('#headNm').html(this.getOptions(ToolbarModel.getHeadNm(param)));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
        },
        headNmChanged: function (e){
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                typeNm: this.$('#typeNm').val(),
                headNm: e.currentTarget.value
            };
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
        },
        bldgNmChanged: function (e){
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                typeNm: this.$('#typeNm').val(),
                headNm: this.$('#headNm').val(),
                bldgNm: e.currentTarget.value
            };
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
        }
    });
});