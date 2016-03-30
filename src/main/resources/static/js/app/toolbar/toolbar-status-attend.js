/**
 *
 */
define(function (require) {
    "use strict";

    var Toolbar = require('./toolbar-base.js');
    var Template = require('text!tpl/toolbar.status-attend.html');

    var ToolbarModel = require('../model/model-status-toolbar.js');

    return Toolbar.extend({
        initialize: function (o) {
            this.el = o.el;
            this.parent = o.parent;
        },
        render: function () {
            this.$el.html(Template);
            this.$('#admissionNm').html(this.getOptions(ToolbarModel.getAdmissionNm()));
            return this;
        },
        events: {
            'click #search': 'searchClicked',
            'click #admissionNm': 'admissionNmChanged'
        },
        searchClicked: function (e) {
            var _this = this;
            if (this.parent) {
                this.parent.search({
                    admissionNm: _this.$('#admissionNm').val(),
                    attendTypeNm: _this.$('#attendTypeNm').val()
                });
            }
        },
        admissionNmChanged: function (e) {
            var param = {
                admissionNm: e.currentTarget.value
            };
            this.$('#attendTypeNm').html(this.getOptions(ToolbarModel.getAttendTypeNm(param)));
        }
    });
});