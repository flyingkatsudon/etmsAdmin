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
            'click #reset': 'resetClicked'
        }, resetClicked: function (e) {
            BootstrapDialog.show({
                title: '서버 데이터 관리',
                message: '삭제 하시겠습니까?',
                closable: true,
                buttons: [
                    {
                        label: '사진포함',
                        cssClass: 'btn-primary',
                        action: function () {
                            BootstrapDialog.closeAll();
                            BootstrapDialog.show({
                                title: '서버 데이터 관리',
                                message: '진행 중입니다. 잠시만 기다려주세요.',
                                closable: false
                            });
                            $.ajax({
                                url: 'system/reset.photo',
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
                            $.ajax({
                                url: 'system/reset.none',
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
                    },
                    {
                        label: '닫기',
                        action: function (dialog) {
                            dialog.close();
                        }
                    }
                ]
            });
        }
    });
});