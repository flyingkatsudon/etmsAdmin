define(function (require) {
    "use strict";

    require('jquery.ajaxForm');

    var Backbone = require('backbone');
    var Template = require('text!tpl/setting-data.html');
    var BootstrapDialog = require('bootstrap-dialog');

    var SettingAttend = require('./../grid/setting-data.js');
    var DataToolbar = require('../toolbar/setting-data.js');

    return Backbone.View.extend({
        initialize: function (o) {
            this.el = o.el;
            this.parent = o.parent;
        }, render: function () {
            this.$el.html(Template);
            this.initForm('#frmUploadHall');
            this.initForm('#frmUploadExaminee');

            // 시험 정보 관리 메뉴
            this.toolbar = new DataToolbar({el: '.hm-ui-search', parent: this}).render();
            this.list = new SettingAttend({el: '#attendInfo', parent: this}).render();

        }, search: function (o) {
            this.list.search(o);
        }, initForm: function (id) {
            var _this = this;

            this.$(id).ajaxForm({
                beforeSubmit: function (arr) {
                    for (var i in arr) {
                        if (arr[i].name == 'file' && arr[i].value == '') {
                            this.error();
                            return false;
                        }

                        var dialog = new BootstrapDialog({
                            message: '<div style="cursor: wait"><h5 style="margin-left: 20%; font-weight:normal">업로드 중 입니다. 창이 사라지지 않으면 관리자에게 문의하세요</h5></div>'
                        });

                        dialog.realize();
                        dialog.getModalDialog().css('margin-top', '20%');
                        dialog.getModalHeader().hide();
                        dialog.getModalFooter().hide();
                        dialog.open();
                    }
                },
                error: function (response) {
                    _this.completeDialog(response);
                },
                success: function (response) {
                    _this.completeDialog(response);
                }
            });
        }, events: {
            'click #reset': 'resetClicked',
            'click #init': 'initClicked'
        }, resetClicked: function (e) {
            var _this = this;
            var dialog = new BootstrapDialog({
                message: '<h5 style="margin-left:10%">삭제하면 복구할 수 없습니다. 그래도 삭제 하시겠습니까?</h5>',
                buttons: [
                    /*{
                     label: '사진포함',
                     cssClass: 'btn-primary',
                     action: function () {
                     BootstrapDialog.closeAll();
                     BootstrapDialog.show({
                     title: '서버 데이터 관리',
                     message: '진행 중입니다. 잠시만 기다려주세요.',
                     closable: false
                     });
                     _this.reset(true);
                     }
                     },*/
                    {
                        label: '사진 미포함',
                        action: function () {
                            _this.reset(false);
                        }
                    },
                    {
                        label: '닫기',
                        action: function (dialog) {
                            dialog.close();
                        }
                    }
                ]
            });

            dialog.realize();
            dialog.getModalDialog().css('margin-top', '20%');
            dialog.getModalHeader().hide();
            dialog.open();
        },
        reset: function (o) {
            var _this = this;

            $.ajax({
                url: 'system/reset',
                data: {
                    photo: o
                },
                success: function (response) {
                    _this.completeDialog(response);
                }, error: function (response){
                    _this.completeDialog(response.JSON);
                }
            });

        }, initClicked: function (e) {
            var _this = this;

            var dialog = new BootstrapDialog({
                message: '<h5 style="margin-left:10%">서버의 데이터를 초기상태로 돌리시겠습니까?</h5>',
                closable: true,
                buttons: [
                    {
                        label: '초기화',
                        cssClass: 'btn-primary',
                        action: function () {
                            $.ajax({
                                url: 'system/init',
                                success: function (response) {
                                    _this.completeDialog(response);
                                }
                            });
                        }
                    }, {
                        label: '닫기',
                        action: function (dialog) {
                            dialog.close();
                        }
                    }
                ]
            });

            dialog.realize();
            dialog.getModalDialog().css('margin-top', '20%');
            dialog.getModalHeader().hide();
            dialog.open();

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
        }
    });
});