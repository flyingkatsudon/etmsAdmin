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
            this.$('#typeNm').html(this.getOptions(ToolbarModel.getTypeNm()));
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate()));
            this.$('#deptNm').html(this.getOptions(ToolbarModel.getDeptNm()));
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm()));
            this.$('#examNm').html(this.getOptions(ToolbarModel.getExamNm()));
            this.$('#scorerNm').html(this.getOptions(ToolbarModel.getScorerNm()));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm()));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm()));
        },
        events:{
            'click #search': 'searchClicked',
            'change #typeNm': 'typeNmChanged',
            'change #attendDate': 'attendDateChanged',
            'change #deptNm': 'deptNmChanged',
            'change #majorNm': 'majorNmChanged',
            'change #scorerNm': 'scorerNmChanged',
            'change #bldgNm': 'bldgNmChanged',
            'change #hallNm': 'hallNmChanged'
        },
        searchClicked: function (e) {
            e.preventDefault();
            
            var _this = this;
            if(this.parent){
                this.parent.search({
                    admissionNm: _this.$('#admissionNm').val(),
                    attendDate: _this.$('#attendDate').val(),
                    deptNm: _this.$('#deptNm').val(),
                    majorNm: _this.$('#majorNm').val(),
                    examNm: _this.$('#examNm').val(),
                    scorerNm: _this.$('#scorerNm').val(),
                    isSend: _this.$('#isSend').val(),
                    bldgNm: _this.$('#bldgNm').val(),
                    hallNm: _this.$('#hallNm').val()
                });
            }
        },
        typeNmChanged: function (e) {
            var param = {
                typeNm: e.currentTarget.value
            };
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate(param)));
            this.$('#deptNm').html(this.getOptions(ToolbarModel.getDeptNm(param)));
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm(param)));
            this.$('#examNm').html(this.getOptions(ToolbarModel.getExamNm(param)));
            this.$('#scorerNm').html(this.getOptions(ToolbarModel.getScorerNm(param)));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
        },
        attendDateChanged: function (e) {
            var param = {
                typeNm: this.$('#typeNm').val(),
                attendDate: e.currentTarget.value
            };
            this.$('#deptNm').html(this.getOptions(ToolbarModel.getDeptNm(param)));
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm(param)));
            this.$('#examNm').html(this.getOptions(ToolbarModel.getExamNm(param)));
            this.$('#scorerNm').html(this.getOptions(ToolbarModel.getScorerNm(param)));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
        },
        deptNmChanged: function (e) {
            var param = {
                typeNm: this.$('#typeNm').val(),
                attendDate: this.$('#attendDate').val(),
                deptNm: e.currentTarget.value
            };
            this.$('#majorNm').html(this.getOptions(ToolbarModel.getMajorNm(param)));
            this.$('#examNm').html(this.getOptions(ToolbarModel.getExamNm(param)));
            this.$('#scorerNm').html(this.getOptions(ToolbarModel.getScorerNm(param)));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
        },
        majorNmChanged: function (e) {
            var param = {
                typeNm: this.$el('#typeNm').val(),
                attendDate: this.$el('#attendDate').val(),
                deptNm: this.$el('#deptNm').val(),
                majorNm: e.currentTarget.value
            };
            this.$('#examNm').html(this.getOptions(ToolbarModel.getExamNm(param)));
            this.$('#scorerNm').html(this.getOptions(ToolbarModel.getScorerNm(param)));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
        },
        examNmChanged: function (e) {
            var param = {
                typeNm: this.$('#typeNm').val(),
                attendDate: this.$('#attendDate').val(),
                deptNm: this.$('#deptNm').val(),
                majorNm: this.$('#major').val(),
                examNm: e.currentTarget.value
            };
            this.$('#scorerNm').html(this.getOptions(ToolbarModel.getScorerNm(param)));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
        },
        scorerNmChanged: function (e) {
            var param = {
                typeNm: this.$('#typeNm').val(),
                attendDate: this.$('#attendDate').val(),
                deptNm: this.$('#deptNm').val(),
                majorNm: this.$('#majorNm').val(),
                examNm: this.$('#examNm').val(),
                scorerNm: e.currentTarget.value
            };
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm(param)));
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
        },
        bldgNmChanged: function (e){
            var param = {
                typeNm: this.$('#typeNm').val(),
                attendDate: this.$('#attendDate').val(),
                deptNm: this.$('#deptNm').val(),
                majorNm: this.$('#majorNm').val(),
                examNm: this.$('#examNm').val(),
                scorerNm: this.$('#scorerNm').val(),
                bldgNm: e.currentTarget.value
            };
            this.$('#hallNm').html(this.getOptions(ToolbarModel.getHallNm(param)));
        }
    });
});