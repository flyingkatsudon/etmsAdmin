define(function (require) {
    "use strict";

    require('jquery.ajaxForm');

    var Backbone = require('backbone');
    var Template = require('text!tpl/setting-data.html');
    var BootstrapDialog = require('bootstrap-dialog');

    var SettingAttend = require('./../grid/setting-data.js');
    var DataToolbar = require('../toolbar/setting-data.js');

    var ResponseDialog = require('../responseDialog.js');
    var responseDialog = new ResponseDialog();

    return Backbone.View.extend({
        initialize: function (o) {
            this.el = o.el;
            this.parent = o.parent;
        }, render: function () {
            var _this = this;

            this.$el.html(Template);
            this.initForm('#frmUploadHall');
            this.initForm('#frmUploadExaminee');
            this.initForm('#frmUploadPhoto');

            // 시험 정보 관리 메뉴
            this.toolbar = new DataToolbar({el: '.hm-ui-search', parent: this}).render();
            this.list = new SettingAttend({el: '#attendInfo', parent: this}).render();

            $('#reset').click(function(){
                _this.resetClicked();
            });

            $('#init').click(function(){
                responseDialog.dialogFormat('초기화하면 복구할 수 없습니다. 그래도 삭제 하시겠습니까?', '초기화', 'system/init');
            });

        }, search: function (o) {
            this.list.search(o);
        }, initForm: function (id) {
            this.$(id).ajaxForm({
                beforeSubmit: function (arr) {
                    for (var i in arr) {
                        if (arr[i].name == 'file' && arr[i].value == '') {
                            responseDialog.notify({msg: '파일을 선택하세요'});
                            return false;
                        }
                        responseDialog.progress('업로드');
                    }
                },
                error: function (response) {
                    responseDialog.notify({msg: response.responseJSON});
                },
                success: function (response) {
                    $('#search').trigger('click');
                    responseDialog.move(response);
                }
            });
        },
        resetClicked: function (e) {
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
                        cssClass: 'btn-normal',
                        action: function (dialog) {
                            dialog.close();
                        }
                    }
                ]
            });

            dialog.realize();
            dialog.getModalDialog().css('margin-top', '20%');
            dialog.getModalHeader().hide();
            dialog.getModalFooter().css('padding', '1%');
            dialog.open();
        },
        reset: function (o) {

            $.ajax({
                beforeSend: function () {
                    responseDialog.progress('삭제');
                },
                url: 'system/reset',
                data: {
                    photo: o
                },
                success: function (response) {
                    $('#search').trigger('click');
                    responseDialog.move(response);
                },
                error: function (response) {
                    responseDialog.notify({msg: response.responseJSON});
                }
            });
        }
    });
});