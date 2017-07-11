define(function (require) {
    "use strict";

    var Backbone = require('backbone');
    var Template = require('text!tpl/data-report.html');
    var DlgDownload = require('../dist/dlg-download.js');

    var Toolbar = require('../dist/toolbar.js');
    var ToolbarModel = require('../model/model-status-toolbar.js');

    var BootstrapDialog = require('bootstrap-dialog');

    return Toolbar.extend({
        initialize: function (o) {
            this.el = o.el;
            this.parent = o.parent;
            this.dlgDownload = new DlgDownload();
        },
        render: function () {
            this.$el.html(Template);
            this.$('#admissionNm').html(this.getOptions(ToolbarModel.getAdmissionNm()));
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate()));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime()));
        },
        events: {
            'click .btn': 'buttonClicked',
            'click .noIdCard': 'noIdCardClicked',
            'change #admissionNm': 'admissionNmChanged',
            'change #attendDate': 'attendDateChanged',
            'click .attachment': 'attachmentClicked'
        },
        buttonClicked: function (e) {
            e.preventDefault();

            var param = {
                admissionNm: this.$('#admissionNm').val(),
                attendDate: this.$('#attendDate').val(),
                attendTime: this.$('#attendTime').val()
            };

            var url = e.currentTarget.form.action;
            this.dlgDownload.render({url: url + "?admissionNm=" + param.admissionNm + "&attendTime=" + param.attendTime + "&attendDate=" + param.attendDate});

            return false;
        },
        admissionNmChanged: function (e) {
            var param = {
                admissionNm: e.currentTarget.value
            };
            this.$('#attendDate').html(this.getOptions(ToolbarModel.getAttendDate(param)));
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
        },
        attendDateChanged: function (e) {
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                attendDate: e.currentTarget.value
            };
            this.$('#attendTime').html(this.getOptions(ToolbarModel.getAttendTime(param)));
        },
        attachmentClicked: function (e) {
            var param = {
                admissionNm: this.$('#admissionNm').val(),
                attendDate: this.$('#attendDate').val(),
                attendTime: this.$('#attendTime').val()
            };

            var _this = this;

            $.ajax({
                url: 'data/attachment.json' + '?admissionNm=' + param.admissionNm + '&attendDate=' + param.attendDate + '&attendTime=' + param.attendTime,
                success: function (e) {
                    if (e.content.length == 0) {
                        _this.completeDialog('신분증 미소지자가 존재하지 않습니다');
                        return false;
                    } else {
                        _this.dlgDownload.render({url: 'data/attachment.PDF?admissionNm=' + param.admissionNm + "&attendTime=" + param.attendTime + "&attendDate=" + param.attendDate});
                    }
                }
            });
        }, completeDialog: function (msg) {
            BootstrapDialog.closeAll();

            var dialog = new BootstrapDialog({
                title: '',
                message: '<h5 style="margin-left:20%">' + msg + '</h5>',
                closable: true
            });

            dialog.realize();
            dialog.getModalDialog().css('margin-top', '20%');
            dialog.getModalHeader().hide();
            dialog.getModalFooter().hide();
            dialog.open();

        }, errorDialog: function (msg) {
            BootstrapDialog.closeAll();

            var dialog = new BootstrapDialog({
                title: '',
                message: '<h5 style="margin-left:5%">' + msg + '</h5>',
                closable: true
            });

            dialog.realize();
            dialog.getModalDialog().css('margin-top', '20%');
            dialog.getModalHeader().hide();
            dialog.getModalFooter().hide();
            dialog.open();
        }

    });
});