define(function (require) {
    "use strict";

    require('jquery.ajaxForm');

    var Backbone = require('backbone');
    var Template = require('text!tpl/setting-data.html');
    var BootstrapDialog = require('bootstrap-dialog');

    return Backbone.View.extend({
        initialize: function (o) {
            this.el = o.el;
            this.parent = o.parent;
        }, render: function () {
            this.$el.html(Template);
            this.initForm('#frmUploadHall');
            this.initForm('#frmUploadExaminee');
        }, initForm: function (id) {
            this.$(id).ajaxForm({
                loading: function () {
                    BootstrapDialog.show({
                        title: '파일 업로드',
                        message: '업로드 중입니다. 잠시만 기다려주십시오.',
                        closable: false
                    })
                },
                failed: function () {
                    BootstrapDialog.show({
                        title: '파일 업로드',
                        message: '파일을 선택하세요.',
                        closable: true,
                        buttons: [{
                            label: '확인',
                            action: function (dialog) {
                                dialog.close();
                            }
                        }]
                    })
                },
                uploadSuccess: function () {
                    BootstrapDialog.closeAll();
                    BootstrapDialog.show({
                        title: '파일 업로드',
                        message: '업로드가 완료되었습니다.',
                        closable: true,
                        buttons: [{
                            label: '확인',
                            action: function (dialog) {
                                dialog.close();
                            }
                        }]
                    })
                },
                beforeSubmit: function (arr) {
                    for (var i in arr) {
                        if (arr[i].name == 'file' && arr[i].value == '') {
                            this.failed();
                            return false;
                        }
                        this.loading();
                    }
                },
                success: function () {
                    this.uploadSuccess();
                }
            });
        }, events: {
            'click #reset': 'resetClicked',
            'click #init': 'initClicked'
        }, initClicked: function (e) {
            BootstrapDialog.show({
                title: '데이터 초기화',
                message: '서버의 데이터를 초기상태로 돌리시겠습니까?.',
                closable: true,
                buttons: [{
                    label: '확인',
                    action: function (dialog) {
                        $.ajax({
                            url: 'system/init',
                            success: function (data) {
                                BootstrapDialog.closeAll();
                                BootstrapDialog.show({
                                    title: '서버 데이터 관리',
                                    message: '완료되었습니다.',
                                    closable: true,
                                    buttons: [{
                                        label: '확인',
                                        action: function (dialog) {
                                            dialog.close();
                                        }
                                    }]
                                });
                            }
                        });
                        dialog.close();
                    }
                }, {
                    label: '취소',
                    action: function (dialog) {
                        dialog.close();
                    }
                }]
            });
        }, resetClicked: function (e) {
            var _this = this;
            BootstrapDialog.show({
                title: '서버 데이터 관리',
                message: '삭제 하시겠습니까?',
                closable: true,
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
                    },
                    {
                        label: '사진 미포함',
                        action: function () {
                            BootstrapDialog.closeAll();
                            BootstrapDialog.show({
                                title: '서버 데이터 관리',
                                message: '진행 중입니다. 잠시만 기다려주세요.',
                                closable: false
                            });
                            _this.reset(false);
                        }
                    },*/
                    {
                        label: '닫기',
                        action: function (dialog) {
                            dialog.close();
                        }
                    }
                ]
            });
        },
        reset: function (o) {
            $.ajax({
                url: 'system/reset',
                data: {
                    photo: o
                },
                success: function (data) {
                    BootstrapDialog.closeAll();
                    BootstrapDialog.show({
                        title: '서버 데이터 관리',
                        message: '완료되었습니다.',
                        closable: true,
                        buttons: [{
                            label: '확인',
                            action: function (dialog) {
                                dialog.close();
                            }
                        }]
                    });
                }
            });
        }
    });
});