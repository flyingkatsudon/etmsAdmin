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
            this.$('#admissionNm').html(this.getOptions(Toolbar.getAdmissionNm()));

            return this;
        },
        events: {
            'click #search': 'searchClicked',
            'click #admissionNm': 'admissionNmChanged',
            'click #attendDate': 'attendDateChanged',
            'click #attendTime': 'attendTimeChanged'
        },
        searchClicked: function (e) {
            var _this = this;
            if (this.list) {
                this.list.search({
                    admissionNm: _this.$('#admissionNm').val(),
                    weirdNm : _this.$('#weirdNm').val(),
                    attendDate : _this.$('#attendDate').val(),
                    attendTime : _this.$('#attendTime').val()
                });
            }
        },

        admissionNmChanged: function (e){
            var param = {
                admissionNm: e.currentTarget.value
            }
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate(param)));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
        },

        attendDateChanged: function (e){
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                attendDate: e.currentTarget.value
            }
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
        }
    });
});