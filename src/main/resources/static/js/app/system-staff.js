define(function (require) {
    "use strict";

    require('jquery.ajaxForm');

    var Backbone = require('backbone');
    var List = require('../grid/system-staff.js');
    var Toolbar = require('../toolbar/system-staff.js');
    var Template = require('text!/tpl/system-staff.html');

    var InnerTemplate = require('text!/tpl/system-updateStaff.html');

    var BootstrapDialog = require('bootstrap-dialog');
    var ResponseDialog = require('../responseDialog.js');
    var responseDialog = new ResponseDialog();

    return Backbone.View.extend({
        render: function () {
            var _this = this;

            this.$el.html(Template);
            this.toolbar = new Toolbar({el: '.hm-ui-search', parent: this}).render();
            this.list = new List({el: '.hm-ui-grid', parent: this}).render();

            $('#add').click(function () {
                _this.add();
            });

        }, search: function (o) {
            this.list.search(o);
        },
        add: function () {
            var _this = this;

            var dialog = new BootstrapDialog({
                title: '<h4>스태프를 추가합니다</h4>',
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
                    body.append(InnerTemplate);

                    // 스태프 개별 추가
                    $('#addEach').click(function () {

                    });

                    // 엑셀 파일로 업로드
                    $('#addForm').click(function () {
                        var html = '<form id="uploadStaff" action="upload/staff" method="post" enctype="multipart/form-data">' +
                            '<input type="file" name="file" style="width: 70%;" class="pull-left chosen"/>' +
                            '<input type="submit" style="width: 12%; padding: 2%" class="btn btn-success" value="등록"/>' +
                            '<input type="button" id="close" style="width: 12%; padding: 2%" class="btn pull-right" value="닫기"/>' +
                            '</form>';

                        var dialog = new BootstrapDialog({
                            title: '<h5><div id="alert" style="font-weight: bold; font-size: medium; color: crimson"><span style="color: black">엑셀 파일로 업로드합니다</span></div></h5>',
                            message: html,
                            onshown: function(dialog){
                                $('#close').click(function(){
                                    dialog.close();
                                });

                                _this.uploadForm('#uploadStaff');
                            }
                        });

                        dialog.realize();
                        dialog.getModalDialog().css('margin-top', '20%');
                        dialog.open();


                    });
                }
            });

            dialog.realize();
            dialog.getModalDialog().css('margin-top', '20%');
            dialog.getModalDialog().css('width', '50%');
            dialog.getModalBody().css('text-align', 'center');
            dialog.open();

        },
        uploadForm: function(id){
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
        }
    });
});