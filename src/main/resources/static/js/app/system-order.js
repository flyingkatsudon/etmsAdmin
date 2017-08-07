define(function (require) {
    "use strict";

    require('jquery.ajaxForm');

    var Backbone = require('backbone');

    var List = require('../grid/system-order.js');
    var Toolbar = require('../toolbar/system-order.js');
    var Template = require('text!/tpl/system-order.html');
    var BootstrapDialog = require('bootstrap-dialog');
    var updateFrame = require('text!tpl/system-updateOrder.html');

    var ResponseDialog = require('../responseDialog.js');
    var responseDialog = new ResponseDialog();

    return Backbone.View.extend({
        render: function () {
            this.$el.html(Template);
            this.toolbar = new Toolbar({el: '.hm-ui-search', parent: this}).render();
            this.list = new List({el: '.hm-ui-grid', parent: this}).render();

            var _this = this;

            $('#refresh').click(function () {
                _this.onStart();
            });

            $('#waitHall').click(function () {
                _this.setWaitHall();
            });

        }, search: function (o) {
            this.list.search(o);
        },
        onStart: function () {
            var _this = this;

            // TODO: 토론면접 조가 있는지 여부 확인한 후 그것이 있다면 바로 뷰, 없다면 입력 창 띄우기
            $.ajax({
                url: 'system/local/orderCnt',
                success: function (response) {
                    // 순번이 저장되어 있으면
                    if (response) {
                        var dialog = new BootstrapDialog({
                            message: '<h5>순번 데이터가 저장되어 있습니다. 갱신하시겠습니까?</h5>',
                            buttons: [
                                {
                                    label: '갱신하기',
                                    cssClass: 'btn-success',
                                    action: function () {
                                        _this.uploadOrder();
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
                        dialog.getModalDialog().css('text-align', 'center');
                        dialog.getModalHeader().hide();

                        dialog.open();

                    }
                    // 순번이 저장되어 있지 않다면
                    else if (!response) {
                        _this.uploadOrder();
                        responseDialog.notify({
                            msg: '순번 데이터의 등록이 필요합니다. 클릭 후 진행하세요',
                            closeAll: false
                        });
                    }
                }
            });

        },
        uploadOrder: function () {
            var _this = this;

            BootstrapDialog.closeAll();
            var dialog = new BootstrapDialog({
                title: '<h4>업로드할 방법을 선택하세요</h4>',
                buttons: [
                    {
                        label: '닫기',
                        action: function (dialog) {
                            dialog.close();
                        }
                    }
                ],
                onshown: function (dialog) {

                    var body = dialog.$modalBody;
                    body.append(updateFrame);

                    // 1. 서버에서 내려받기
                    $('#server').click(function () {
                        alert('준비중');
                        //_this.fromServer();
                    });

                    // 2. 파일로 업로드
                    $('#file').click(function () {
                        _this.fromFile();
                    });
                }
            });

            dialog.realize();
            dialog.getModalDialog().css('margin-top', '18%');
            dialog.getModalDialog().css('width', '60%');
            dialog.getModalBody().css('text-align', 'center');
            dialog.open();

        },
        fromFile: function () {
            var _this = this;

            var html = '<form id="uploadOrder" action="upload/order" method="post" enctype="multipart/form-data">' +
                '<input type="file" name="file" style="width: 70%;" class="pull-left chosen"/>' +
                '<input type="submit" style="width: 12%; padding: 2%" class="btn btn-success" value="등록"/>' +
                '<input type="button" id="close" style="width: 12%; padding: 2%" class="btn pull-right" value="닫기"/>' +
                '</form>';

            var dialog = new BootstrapDialog({
                title: '<h5><div id="alert" style="font-weight: bold; font-size: medium; color: crimson"><span style="color: black">엑셀 파일로 업로드합니다</span></div></h5>',
                message: html,
                onshown: function () {

                    $('#close').click(function(){
                        BootstrapDialog.closeAll();
                    });

                    _this.uploadForm('#uploadOrder');
                }
            });

            dialog.realize();
            dialog.getModalDialog().css('margin-top', '20%');
            dialog.getModalBody().css('margin-bottom', '7%');
            dialog.open();

        },
        uploadForm: function (id) {
            $(id).ajaxForm({
                beforeSubmit: function (arr) {
                    for (var i in arr) {
                        if (arr[i].name == 'file' && arr[i].value == '') {
                            $('#alert').html('파일을 선택하세요!');
                            return false;
                        }
                    }
                    responseDialog.notify({
                        msg: '<div style="cursor: wait">업로드 중 입니다. 창이 사라지지 않으면 관리자에게 문의하세요</div>',
                        closable: false
                    });
                },
                error: function (response) {
                    responseDialog.notify({msg: response.responseJSON});
                },
                success: function (response) {
                    $('#search').trigger('click');
                    responseDialog.notify({msg: response});
                }
            });
        },
        setWaitHall: function(){
            console.log('clicked');
        }
    });
});