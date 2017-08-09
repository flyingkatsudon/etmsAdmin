define(function (require) {
    "use strict";

    var GridBase = require('../dist/jqgrid.js');
    var BootstrapDialog = require('bootstrap-dialog');
    var ResponseDialog = require('../responseDialog.js');
    var responseDialog = new ResponseDialog();

    return GridBase.extend({
        initialize: function (options) {
            this.parent = options.parents;

            var colModel = [
                {name: 'admissionCd', hidden: true},
                {name: 'admissionNm', label: '전형명'},
                {name: 'attendDate', label: '시험일자'},
                {name: 'attendTime', label: '시험시간'},
                {name: 'staffNm', label: '성명'},
                {name: 'phoneNo', label: '전화번호'},
                {name: 'bldgNm', label: '고사건물'}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: 'system/staff',
                    colModel: colModel,
                    onSelectRow: function (rowid, status, e) {
                        var rowdata = $(this).jqGrid('getRowData', rowid);

                        var phone = rowdata.phoneNo.split('-');
                        var html = '<div class="container-fluid">' +
                            '<div class="row">' +
                            '<div class="col-lg-12">' +
                            '<h5><div id="staff" style="text-align: center; font-weight: normal; font-size: medium"></div></h5>' +
                            '</div>' +
                            '</div>' +
                            '</div>';

                        var dialog = new BootstrapDialog({
                            title: '<h5><div id="alert" style="font-weight: bold; font-size: medium; color: crimson"><span style="color: black">스태프 정보를 확인하세요</span></div></h5>',
                            message: html,
                            onshown: function () {

                                $('#staff').append(
                                    '<div style="margin:1% 0 1% 3%; width:47%;">' + '성&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;명' +
                                    '<input id="name" size="12" type="text" style="width: 60%; border-radius: 10px; padding: 1%; margin-left: 20%;" value="' + rowdata.staffNm + '"/>' +
                                    '</div>'
                                );
                                $('#staff').append(
                                    '<div style="margin:1% 0 1% 3%; width:47%; float:left">' + '전화번호' +
                                    '<input id="first" size="4" type="text" style="border-radius: 10px; padding: 1%; margin-left: 20%" value="' + phone[0] + '">&nbsp;-&nbsp;' +
                                    '<input id="middle" size="4" type="text" style="border-radius: 10px; padding: 1%;" value="' + phone[1] + '">&nbsp;-&nbsp;' +
                                    '<input id="last" size="4" type="text" style="border-radius: 10px; padding: 1%;" value="' + phone[2] + '">' +
                                    '</div>'
                                );
                                $('#staff').append(
                                    '<div id="msg" style="margin:1% 0 1% 3%; width:47%; float:left; vertical-align: middle; color: crimson"></div>'
                                );

                                $.ajax({
                                    url: 'system/bldgNm',
                                    success: function (response) {
                                        $('#staff').append(
                                            '<div style="margin:1% 0 1% 3%; width:47%;">' + '고사건물' +
                                            '<select id="select" style="width: 60%; margin-left: 20%; padding: 1%">' +
                                            '</select>' +
                                            '</div>'
                                        );

                                        for (var i = 0; i < response.length; i++) {
                                            var tmp = '';

                                            if (rowdata.bldgNm == response[i].bldgNm)
                                                tmp = '<option value = ' + response[i].bldgNm + ' selected>' + response[i].bldgNm + '</option>';
                                            else
                                                tmp = '<option value = ' + response[i].bldgNm + '>' + response[i].bldgNm + '</option>';
                                            $('#select').append(tmp);
                                        }
                                    }
                                });

                                $('#first, #middle, #last').keypress(function (event) {
                                    // 문자 처리
                                    var id = $(this)[0].id;

                                    var text = $('#' + id).val() + event.key;

                                    if (check_key(event) != 1) {
                                        $('#' + id).val('');
                                        //  event.returnValue = false;
                                        $("#msg").html("숫자만 입력할 수 있습니다.");
                                        return false;
                                    } else {
                                        $("#msg").html("");

                                        if (id == 'first') {
                                            if (text.length > 3) {
                                                $("#msg").html("3자리까지 입력 가능합니다");
                                                return false;
                                            }
                                        } else {
                                            if (text.length > 4) {
                                                $("#msg").html("4자리까지 입력 가능합니다");
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
                                    label: '삭제',
                                    cssClass: 'btn-delete',
                                    action: function () {

                                        var param = {
                                            staffNm: $('#name').val(),
                                            phoneNo: $('#first').val() + '-' + $('#middle').val() + '-' + $('#last').val(),
                                            bldgNm: $('#select').val(),
                                            // 기존 정보
                                            _staffNm: rowdata.staffNm,
                                            _phoneNo: rowdata.phoneNo,
                                            _bldgNm: rowdata.bldgNm
                                        };

                                        $.ajax({
                                            url: 'system/delStaff',
                                            type: 'POST',
                                            contentType: "application/json; charset=utf-8",
                                            data: JSON.stringify(param),
                                            success: function (response) {
                                                responseDialog.notify({msg: response});
                                                $('#search').trigger('click');
                                            }
                                        })
                                    }
                                },
                                {
                                    label: '저장',
                                    cssClass: 'btn-success',
                                    action: function () {
                                        var staffNm = $('#name').val();

                                        if (staffNm.length == 0) {
                                            $('#msg').html('성명을 입력하세요');
                                            $('#name').css('border', '3px solid crimson');
                                            $('#name').focus();
                                        }

                                        // 유효한 전화번호인지 검사
                                        var first = $('#first').val(), middle = $('#middle').val(), last = $('#last').val();

                                        first = first.replace(/^\s*|\s*$/g, ''); // 좌우 공백 제거
                                        middle = middle.replace(/^\s*|\s*$/g, ''); // 좌우 공백 제거
                                        last = last.replace(/^\s*|\s*$/g, ''); // 좌우 공백 제거

                                        if (first.length != 3) {
                                            $('#msg').html('3자리를 입력하세요');
                                            $('#first').css('border', '3px solid crimson');
                                            $('#first').focus();
                                        }

                                        if (middle.length != 4) {
                                            $('#msg').html('4자리를 입력하세요');
                                            $('#middle').css('border', '3px solid crimson');
                                            $('#middle').focus();
                                        }

                                        if (last.length != 4) {
                                            $('#msg').html('4자리를 입력하세요');
                                            $('#last').css('border', '3px solid crimson');
                                            $('#last').focus();
                                        }

                                        $('msg').html('');

                                        var param = {
                                            staffNm: $('#name').val(),
                                            phoneNo: $('#first').val() + '-' + $('#middle').val() + '-' + $('#last').val(),
                                            bldgNm: $('#select').val(),
                                            // 기존 정보
                                            _staffNm: rowdata.staffNm,
                                            _phoneNo: rowdata.phoneNo,
                                            _bldgNm: rowdata.bldgNm
                                        };

                                        $.ajax({
                                            url: 'system/modifyStaff',
                                            type: 'POST',
                                            contentType: "application/json; charset=utf-8",
                                            data: JSON.stringify(param),
                                            success: function (response) {
                                                responseDialog.notify({msg: response});
                                                $('#search').trigger('click');
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
                        dialog.getModalDialog().css('margin-top', '20%');
                        dialog.getModalDialog().css('width', '40%');
                        dialog.open();
                    }
                }
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            return this;
        }
    });
});