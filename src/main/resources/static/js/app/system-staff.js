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

    var InnerToolbar = require('../toolbar/system-staff-inner.js');

    return Backbone.View.extend({
        render: function () {
            var _this = this;

            this.$el.html(Template);
            this.toolbar = new Toolbar({el: '.hm-ui-search', parent: this}).render();
            this.list = new List({el: '.hm-ui-grid', parent: this}).render();

            $('#add').click(function () {
                _this.add();
            });

            $('#del').click(function () {
                _this.del();
            });

        }, search: function (o) {
            this.list.search(o);
        },
        add: function () {
            var _this = this;

            var dialog = new BootstrapDialog({
                    title: '<h4>기술요원을 추가합니다</h4>',
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

                        // 기술요원 개별 추가
                        $('#addEach').click(function () {
                            var html = '<div class="container-fluid">' +
                                '<div class="row">' +
                                '<div class="col-lg-12">' +
                                '<h5><div id="staff" style="text-align: center; font-weight: normal; font-size: medium"></div></h5>' +
                                '</div>' +
                                '</div>' +
                                '</div>';

                            var dialog = new BootstrapDialog({
                                title: '<h5><div id="alert" style="font-weight: bold; font-size: medium; color: crimson"><span style="color: black">기술요원 정보를 확인하세요</span></div></h5>',
                                message: html,
                                closable: false,
                                onshown: function () {
                                    $('#staff').append(
                                        '<div style="margin: 1% 0 1% 3%;">' +
                                        '<div style="float: left">성&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;명</div>' +
                                        '<div style="width: 42%">' +
                                        '<input id="name" size="12" type="text" style="border-radius: 10px; padding: 1%; margin-left: 22%;"/>' +
                                        '</div>' +
                                        '</div>'
                                    );
                                    $('#staff').append(
                                        '<div style="margin:1% 0 1% 3%; width: 100%; float:left">' +
                                        '<div style="float:left">전화번호</div>' +
                                        '<div style="width: 42%; float:left">' +
                                        '<input id="first" size="4" type="text" style="border-radius: 10px; padding: 1%; margin-left: 20%">&nbsp;-&nbsp;' +
                                        '<input id="middle" size="4" type="text" style="border-radius: 10px; padding: 1%">&nbsp;-&nbsp;' +
                                        '<input id="last" size="4" type="text" style="border-radius: 10px; padding: 1%">' +
                                        '</div>' +
                                        '<div id="msg" style="margin:1% 0 1% 3%; width:45%; float:left; vertical-align: middle; color: crimson"></div>' +
                                        '</div>'
                                    );
                                    $('#staff').append(
                                        '<div id="line" style="margin: 10% 0 5% 0; border: 1px solid #d8d6d5"></div>'
                                    );
                                    $('#staff').append(
                                        '<div class="staff-toolbar">' + '전&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;형' +
                                        '<select id="staffAdm" class="staff-select"></select>' +
                                        '</div>'
                                    );
                                    $('#staff').append(
                                        '<div class="staff-toolbar">' + '시&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;험' +
                                        '<select id="staffAtn" class="staff-select"></select>' +
                                        '</div>'
                                    );
                                    $('#staff').append(
                                        '<div class="staff-toolbar">' + '시험일자' +
                                        '<select id="staffDate" class="staff-select"></select>' +
                                        '</div>'
                                    );
                                    $('#staff').append(
                                        '<div class="staff-toolbar">' + '시험시간' +
                                        '<select id="staffTime" class="staff-select"></div>'
                                    );
                                    $('#staff').append(
                                        '<div class="staff-toolbar">' + '고사건물' +
                                        '<select id="staffBldg" class="staff-select"></select>' +
                                        '</div>'
                                    );
                                    $('#staff').append(
                                        '<div style="margin:2% 0 1% 3%; width:45%; float:left; vertical-align: middle; color: crimson"><span id="msg2"></span></div>'
                                    );

                                    // 기술요원 추가 dialog에 사용될 툴바
                                    this.toolbar = new InnerToolbar({el: '#staff', parent: this}).render();

                                    $('#first, #middle, #last').keypress(function (event) {
                                        // 문자 처리
                                        var id = $(this)[0].id;

                                        var text = $('#' + id).val() + event.key;

                                        if (check_key(event) != 1) {
                                            $('#' + id).val('');
                                            //  event.returnValue = false;
                                            $('#msg').html('숫자만 입력할 수 있습니다');
                                            $('#msg').fadeOut(100);
                                            $('#msg').fadeIn(100);
                                            $('#msg').fadeOut(100);
                                            $('#msg').fadeIn(100);
                                            return false;
                                        } else {
                                            $('#msg').html('');

                                            if (id == 'first') {
                                                if (text.length > 3) {
                                                    $('#msg').html('3자리까지 입력 가능합니다');
                                                    $('#msg').fadeOut(100);
                                                    $('#msg').fadeIn(100);
                                                    $('#msg').fadeOut(100);
                                                    $('#msg').fadeIn(100);
                                                    return false;
                                                }
                                            } else {
                                                if (text.length > 4) {
                                                    $('#msg').html('4자리까지 입력 가능합니다');
                                                    $('#msg').fadeOut(100);
                                                    $('#msg').fadeIn(100);
                                                    $('#msg').fadeOut(100);
                                                    $('#msg').fadeIn(100);
                                                    return false;
                                                }
                                            }
                                            return;
                                        }

                                        // 문자 검사
                                        function check_key(event) {
                                            var char_ASCII = event.keyCode;

                                            //숫자
                                            if (char_ASCII >= 48 && char_ASCII <= 57)
                                                return 1;
                                            //영어
                                            else if ((char_ASCII >= 65 && char_ASCII <= 90) || (char_ASCII >= 97 && char_ASCII <= 122))
                                                return 2;
                                            //특수기호
                                            else if ((char_ASCII >= 33 && char_ASCII <= 47)
                                                || (char_ASCII >= 58 && char_ASCII <= 64)
                                                || (char_ASCII >= 91 && char_ASCII <= 96)
                                                || (char_ASCII >= 123 && char_ASCII <= 126))
                                                return 4;
                                            //한글
                                            else if ((char_ASCII >= 12592) || (char_ASCII <= 12687))
                                                return 3;
                                            else
                                                return 0;
                                        }
                                    });
                                },
                                buttons: [
                                    {
                                        label: '저장',
                                        cssClass: 'btn-success',
                                        action: function () {
                                            var staffNm = $('#name').val();

                                            if (staffNm.length == 0) {
                                                $('#msg').html('성명을 입력하세요');
                                                $('#msg').fadeOut(100);
                                                $('#msg').fadeIn(100);
                                                $('#msg').fadeOut(100);
                                                $('#msg').fadeIn(100);
                                                $('#name').css('border', '3px solid crimson');
                                                $('#name').focus();
                                                return false;
                                            }

                                            // 유효한 전화번호인지 검사
                                            var first = $('#first').val(), middle = $('#middle').val(), last = $('#last').val();

                                            first = first.replace(/^\s*|\s*$/g, ''); // 좌우 공백 제거
                                            middle = middle.replace(/^\s*|\s*$/g, ''); // 좌우 공백 제거
                                            last = last.replace(/^\s*|\s*$/g, ''); // 좌우 공백 제거

                                            if (first.length != 3) {
                                                $('#msg').html('3자리를 입력하세요');
                                                $('#msg').fadeOut(100);
                                                $('#msg').fadeIn(100);
                                                $('#msg').fadeOut(100);
                                                $('#msg').fadeIn(100);
                                                $('#first').css('border', '2px solid crimson');
                                                $('#first').focus();
                                                return false;
                                            }

                                            if (middle.length != 4) {
                                                $('#msg').html('4자리를 입력하세요');
                                                $('#msg').fadeOut(100);
                                                $('#msg').fadeIn(100);
                                                $('#msg').fadeOut(100);
                                                $('#msg').fadeIn(100);
                                                $('#middle').css('border', '2px solid crimson');
                                                $('#middle').focus();
                                                return false;
                                            }

                                            if (last.length != 4) {
                                                $('#msg').html('4자리를 입력하세요');
                                                $('#msg').fadeOut(100);
                                                $('#msg').fadeIn(100);
                                                $('#msg').fadeOut(100);
                                                $('#msg').fadeIn(100);
                                                $('#last').css('border', '2px solid crimson');
                                                $('#last').focus();
                                                return false;
                                            }

                                            if ($('#staffAdm').val() == '' || $('#staffBldg').val() == '' ||
                                                $('#staffAtn').val() == '' || $('#staffBldg').val() == '') {
                                                $('#msg2').html('콤보박스의 값을 모두 특정하세요');
                                                $('#msg2').fadeOut(100);
                                                $('#msg2').fadeIn(100);
                                                $('#msg2').fadeOut(100);
                                                $('#msg2').fadeIn(100);
                                                return false;
                                            }

                                            var param = {
                                                staffNm: $('#name').val(),
                                                phoneNo: $('#first').val() + '-' + $('#middle').val() + '-' + $('#last').val(),
                                                admissionNm: $('#staffAdm').val(),
                                                attendNm: $('#staffAtn').val(),
                                                attendDate: $('#staffDate').val(),
                                                attendTime: $('#staffTime').val(),
                                                bldgNm: $('#staffBldg').val()
                                            };

                                            $.ajax({
                                                url: 'system/addStaff',
                                                type: 'POST',
                                                contentType: "application/json",
                                                dataType: 'json',
                                                data: JSON.stringify(param),
                                                success: function (response) {
                                                    responseDialog.notify({msg: response});
                                                    $('#search').trigger('click');
                                                }, error: function (response) {
                                                    responseDialog.notify({
                                                        msg: response.responseJSON,
                                                        closeAll: false
                                                    });
                                                }
                                            })
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
                            dialog.getModalDialog().css('margin-top', '17%');
                            dialog.getModalDialog().css('width', '40%');
                            dialog.open();

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
                                onshown: function (dialog) {
                                    $('#close').click(function () {
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
                })
                ;

            dialog.realize();
            dialog.getModalDialog().css('margin-top', '20%');
            dialog.getModalDialog().css('width', '50%');
            dialog.getModalBody().css('text-align', 'center');
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
        del: function () {
            var dialog = new BootstrapDialog({
                message: '<h5 style="margin-left:10%">기술요원 정보를 모두 삭제합니다</h5>',
                buttons: [
                    {
                        label: '삭제',
                        cssClass: 'btn-delete',
                        action: function () {
                            $.ajax({
                                url: 'system/delStaffAll',
                                success: function (response) {
                                    $('#search').trigger('click');
                                    responseDialog.notify({msg: response});
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

            dialog.realize();
            dialog.getModalHeader().hide();
            dialog.getModalDialog().css('margin-top', '20%');
            dialog.open();
        }
    });
});