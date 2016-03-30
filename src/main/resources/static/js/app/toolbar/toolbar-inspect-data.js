define(function(require){
    "use strict"

    var Toolbar = require('./toolbar-base.js');
    var Template = require('text!tpl/toolbar.inspect-data.html');

    var ToolbarModel = require('../model/model-status-toolbar.js');

    return Toolbar.extend({
        initialize: function (o) {
           this.el = o.el;
           this.parent = o.parent;
        },
        render: function () {
            this.$el.html(Template);
            this.$('#attendTypeNm').html(this.getOptions(ToolbarModel.getAttendTypeNm()));
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate()));
            this.$('#deptNm').html(this.getOptions(ToolbarModel.getDeptNm()));
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm()));
            this.$('#scorerNm').html(this.getOptions(ToolbarModel.getScorerNm()));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm()));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm()));
            this.$('#isSent').html(this.getOptions(ToolbarModel.getIsSent()));
        },
        events:{
            'click #search': 'searchClicked',
            'click #attendTypeNm': 'attendTypeNmClicked',
            'click #attendDate': 'attendDateClicked',
            'click #deptNm': 'deptNmClicked',
            'click #majorNm': 'majorNmClicked',
            'click #scorerNm': 'scorerNmClicked',
            'click #bldgNm': 'bldgNmClicked',
            'click #hallNm': 'hallNmClicked',
            'click #isSent': 'isSentClicked'
        },
        searchClicked: function (e) {
            var _this = this;
            if(this.parent){
                this.parent.search({
                    admissionNm: _this.$('#admissionNm').val(),
                    attendDate: _this.$('#attendDate').val(),
                    deptNm: _this.$('#deptNm').val(),
                    majorNm: _this.$('#majorNm').val(),
                    scorerNm: _this.$('#scorerNm').val(),
                    bldgNm: _this.$('#bldgNm').val(),
                    hallNm: _this.$('#hallNm').val(),
                    isSent: _this.$('#isSent').val()
                });
            }
        },
        attendTypeNmClicked: function (e) {
            var param = {
                attendTypeNm: e.currentTarget.value
            }
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate(param)));
            this.$('#deptNm').html(this.getOptions(ToolbarModel.getDeptNm(param)));
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm(param)));
            this.$('#scorerNm').html(this.getOptions(ToolbarModel.getScorerNm(param)));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
            this.$('#isSent').html(this.getOptions(ToolbarModel.getIsSent(param)));
        },
        attendDateClicked: function (e) {
            var param = {
                attendTypeNm: this.$el('#attendTypeNm').val(),
                attendDate: e.currentTarget.value
            }
            this.$('#deptNm').html(this.getOptions(ToolbarModel.getDeptNm(param)));
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm(param)));
            this.$('#scorerNm').html(this.getOptions(ToolbarModel.getScorerNm(param)));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
            this.$('#isSent').html(this.getOptions(ToolbarModel.getIsSent(param)));
        },
        deptNmClicked: function (e) {
            var param = {
                attendTypeNm: this.$el('#attendTypeNm').val(),
                attendDate: this.$el('#attendDate').val(),
                deptNm: e.currentTarget.value
            }
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm(param)));
            this.$('#scorerNm').html(this.getOptions(ToolbarModel.getScorerNm(param)));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
            this.$('#isSent').html(this.getOptions(ToolbarModel.getIsSent(param)));
        },
        majorNmClicked: function (e) {
            var param = {
                attendTypeNm: this.$el('#attendTypeNm').val(),
                attendDate: this.$el('#attendDate').val(),
                deptNm: this.$el('#deptNm').val(),
                majorNm: e.currentTarget.value
            }
            this.$('#scorerNm').html(this.getOptions(ToolbarModel.getScorerNm(param)));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
            this.$('#isSent').html(this.getOptions(ToolbarModel.getIsSent(param)));
        },
        scorerNmClicked: function (e) {
            var param = {
                attendTypeNm: this.$el('#attendTypeNm').val(),
                attendDate: this.$el('#attendDate').val(),
                deptNm: this.$el('#deptNm').val(),
                majorNm: this.$el('#majorNm').val(),
                scorerNm: e.currentTarget.value
            }
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
            this.$('#isSent').html(this.getOptions(ToolbarModel.getIsSent(param)));
        },
        bldgNmClicked: function (e){
            var param = {
                attendTypeNm: this.$el('#attendTypeNm').val(),
                attendDate: this.$el('#attendDate').val(),
                deptNm: this.$el('#deptNm').val(),
                majorNm: this.$el('#majorNm').val(),
                scorerNm: this.$el('#scorerNm').val(),
                bldgNm: e.currentTarget.value
            }
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
            this.$('#isSent').html(this.getOptions(ToolbarModel.getIsSent(param)));
        },
        hallNmClicked: function (e){
            var param = {
                attendTypeNm: this.$el('#attendTypeNm').val(),
                attendDate: this.$el('#attendDate').val(),
                deptNm: this.$el('#deptNm').val(),
                majorNm: this.$el('#majorNm').val(),
                scorerNm: this.$el('#scorerNm').val(),
                bldgNm: this.$el('#bldgNm').val(),
                hallNm: e.currentTarget.value
            }
            this.$('#isSent').html(this.getOptions(ToolbarModel.getIsSent(param)));
        }
    });
});