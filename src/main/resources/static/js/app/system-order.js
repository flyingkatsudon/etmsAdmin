define(function (require) {
    "use strict";

    require('jquery.ajaxForm');

    var Backbone = require('backbone');

    var List = require('../grid/system-order.js');
    var Toolbar = require('../toolbar/system-order.js');
    var Template = require('text!/tpl/system-order.html');
    var BootstrapDialog = require('bootstrap-dialog');

    var UpdateOrder = require('text!tpl/system-updateOrder.html');
    var WaitHall = require('text!/tpl/system-waitHall.html');
    var AddWaitHall = require('text!/tpl/system-addWaitHall.html');
    var InnerToolbar = require('../toolbar/system-waitHall.js');

    var ResponseDialog = require('../responseDialog.js');
    var responseDialog = new ResponseDialog();

    var param = {}; // 추가될 고사실 정보

    return Backbone.View.extend({
        render: function () {
            this.$el.html(Template);
            this.toolbar = new Toolbar({el: '.hm-ui-search', parent: this}).render();
            this.list = new List({el: '.hm-ui-grid', parent: this}).render();

            var _this = this;

            $('#refresh').click(function () {
                _this.onStart();
            });

            $('#upload').click(function () {
                _this.fromFile('formWaitHall', 'waitHall');
            });

            $('#waitHall').click(function () {
                $.ajax({
                    url: 'system/waitHall',
                    success: function (response) {
                        var id = _this.getSorted(response);
                        _this.setWaitHall(response, id);
                    }
                });
            });

            $('#add').click(function () {
                $.ajax({
                    url: 'system/waitHall',
                    success: function (response) {
                        var id = _this.getSorted(response);
                        _this.addHall(response, id);
                    }
                });
            });
        },
        getSorted: function (response) {
            for (var i = 0; i < response.length - 1; i++) {
                for (var j = i; j < response.length; j++) {
                    if (response[i].id >= response[j].id) {
                        var tmp = response[i].id;
                        response[i].id = response[j].id;
                        response[j].id = tmp;
                    }
                }
            }
            return response[response.length - 1].id;
        },
        search: function (o) {
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
                    body.append(UpdateOrder);

                    // 1. 서버에서 내려받기
                    $('#server').click(function () {
                        alert('준비중');
                        //_this.fromServer();
                    });

                    // 2. 파일로 업로드
                    $('#file').click(function () {
                        _this.fromFile('formOrder', 'order');
                    });
                }
            });

            dialog.realize();
            dialog.getModalDialog().css('margin-top', '18%');
            dialog.getModalDialog().css('width', '60%');
            dialog.getModalBody().css('text-align', 'center');
            dialog.open();

        },
        fromFile: function (id, value) {
            var _this = this;

            var html = '<form id="' + id + '" action="upload/' + value + '" method="post" enctype="multipart/form-data">' +
                '<input type="file" name="file" style="width: 70%;" class="pull-left chosen"/>' +
                '<input type="submit" style="width: 12%; padding: 2%" class="btn btn-success" value="등록"/>' +
                '<input type="button" id="close" style="width: 12%; padding: 2%" class="btn pull-right" value="닫기"/>' +
                '</form>';

            var dialog = new BootstrapDialog({
                title: '<h5><div id="alert" style="font-weight: bold; font-size: medium; color: crimson"><span style="color: black">엑셀 파일로 업로드합니다</span></div></h5>',
                message: html,
                onshown: function () {

                    $('#close').click(function () {
                        BootstrapDialog.closeAll();
                    });

                    _this.uploadForm('#' + id);
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
        setWaitHall: function (response, id) {
            var _this = this;

            // 조를 추가할 때 그 값에 붙일 id
            var id = response.length;

            // 대기실 정보가 없다면 입력창부터 띄운다
            if (response.length == 0) {
                _this.addWaitHall(response, id);

                responseDialog.notify({
                    msg: '대기실 정보가 없습니다. 클릭하여 대기실을 추가하세요',
                    closeAll: false
                });

                return false;

            } else {

                var dialog = new BootstrapDialog({
                    title: '<h3>대기실 별 조 배정 내역</h3>',
                    size: 'size-wide',
                    onshown: function (dialogRef) {

                        var body = dialogRef.$modalBody;
                        body.append(WaitHall);

                        for (var i = 0; i < response.length; i++) {
                            // 중복된 값은 나타낼 필요 없음
                            var flag = true;
                            for (var j = 0; j < i; j++) {
                                // 중복된 값이 있으면 flag값을 바꾼다
                                if (response[i].hallCd == response[j].hallCd) {
                                    flag = false;
                                }
                            }

                            // 중복검사 후, name=hall의 라디오 버튼을 만듦
                            if (flag) {
                                $('#hall').append('<div style="width: 25%; float: left">' +
                                    '<input style="margin: 0 2% 0 15%; float: left" type="radio" id="' + i + '" name="hall" value="' + response[i].hallCd + '">' + response[i].hallNm + '</div>');
                            }
                        }
                        // 대기실 편집
                        _this.modifyWaitHall(response, id);
                    }, // onshown
                    buttons: [
                        {
                            label: '선택 저장',
                            cssClass: 'btn-primary',
                            action: function () {
                                // 조 편집
                                _this.modifyGroup();
                            }
                        },
                        {
                            label: '닫기',
                            action: function (dialog) {
                                dialog.close();
                            }
                        }
                    ]
                }); // dialog

                dialog.realize();
                dialog.getModalDialog().css('margin-top', '15%');
                dialog.open();
            }
        },
        addHall: function (response, id) {

            var _this = this;

            var dialog = new BootstrapDialog({
                closable: false,
                onshown: function (dialogRef) {
                    var body = dialogRef.$modalBody;
                    body.append(AddWaitHall);

                    // 대기실 추가 dialog에 사용될 툴바
                    this.toolbar = new InnerToolbar({el: '#hallInfo', parent: this}).render();

                    // '직접입력'이 선택되면 텍스트박스 나타냄
                    $('#headNm').change(function () {
                        if ($('#headNm').val() != '') {
                            $('#headTxt').hide();
                            $('#headTxt').val('');
                        }
                        else $('#headTxt').show();
                    });

                    $('#bldgNm').change(function () {
                        if ($('#bldgNm').val() != '') {
                            $('#bldgTxt').hide();
                            $('#bldgTxt').val('');
                        }
                        else $('#bldgTxt').show();
                    });

                    $('#innerAddHall').click(function () {
                        var flag = true;

                        // 항목 중 빈 란 체크
                        $('[name=head]').each(function () {
                            if ($(this).val() == '') flag = false;
                            else {
                                param.headNm = $(this).val();
                                flag = true;
                            }
                        });

                        $('[name=bldg]').each(function () {
                            if ($(this).val() == '') flag = false;
                            else {
                                param.bldgNm = $(this).val();
                                flag = true;
                            }
                        });

                        $('[name=hall]').each(function () {
                            if ($(this).val() == '') flag = false;
                            else {
                                param.hallNm = $(this).val();
                                flag = true;
                            }
                        });

                        // 고사본부 고사건물 대기실 중 공백이 있다면
                        if (!flag && param.headNm != '' && param.bldgNm != '' && param.hallNm != '') {
                            $('#notice').html('빈 값이나 콤보박스를 특정하세요');
                            $('#notice').fadeOut(100);
                            $('#notice').fadeIn(100);
                            $('#notice').fadeOut(100);
                            $('#notice').fadeIn(100);
                            return false;
                        } else {
                            // 이미 존재하는 대기실인지 검색, isExist: 대기실 존재여부
                            var isExist = true;
                            for (var i = 0; i < response.length; i++) {
                                if (response[i].headNm == param.headNm && response[i].bldgNm == param.bldgNm
                                    && response[i].hallNm == param.hallNm) isExist = false;
                            }

                            if (!isExist) {
                                $('#innerLine').fadeOut(100);
                                $('#innerGroupInfo').fadeOut(100);

                                $('#notice').html('이미 존재하는 대기실입니다');
                                $('#notice').fadeOut(100);
                                $('#notice').fadeIn(100);
                                $('#notice').fadeOut(100);
                                $('#notice').fadeIn(100);
                                return false;
                            } else {
                                $('#notice').html('');
                                $('#innerLine').fadeIn(100);
                                $('#innerGroupInfo').fadeIn(100);

                                // 대기실 추가 후에는 바꾸지 못함
                                $('#headNm').attr('disabled', true);
                                $('#headNm').css('background', '#fbf7f7');
                                $('#headNm').css('color', 'graytext');
                                $('#headNm').attr('cursor', 'non-allowed');

                                $('#headTxt').attr('disabled', true);
                                $('#headTxt').css('background', '#fbf7f7');
                                $('#headTxt').css('color', 'graytext');
                                $('#headTxt').attr('cursor', 'non-allowed');

                                $('#bldgNm').attr('disabled', true);
                                $('#bldgNm').css('background', '#fbf7f7');
                                $('#bldgNm').css('color', 'graytext');
                                $('#bldgNm').attr('cursor', 'non-allowed');

                                $('#bldgTxt').attr('disabled', true);
                                $('#bldgTxt').css('background', '#fbf7f7');
                                $('#bldgTxt').css('color', 'graytext');
                                $('#bldgTxt').attr('cursor', 'non-allowed');

                                $('#hallTxt').attr('disabled', true);
                                $('#hallTxt').css('background', '#fbf7f7');
                                $('#hallTxt').css('color', 'graytext');
                                $('#hallTxt').attr('cursor', 'non-allowed');
                            }
                        }
                    });

                    // 조 추가 버튼 클릭 시
                    $('#innerAddGroup').click(function () {
                        id++;
                        $('#notice').html('');
                        $('#innerNotice').html('<span style="color:crimson">조 추가 시 값을 기입하지 않으면 추가되지 않습니다</span>');
                        $('#innerGroup').append('<div style="width: 15%; float: left">' +
                            '<input type="text" size="2" style="margin-rig ht: 10%;" id="' + id + '" name="newGroup">조</div>');
                    });
                },
                buttons: [
                    {
                        label: '선택 저장',
                        cssClass: 'btn-primary',
                        action: function () {

                            // 대기실이 추가되어 있지 않다면
                            if (param.headNm == undefined || param.bldgNm == undefined || param.hallNm == undefined) {
                                $('#notice').html('대기실을 먼저 추가하세요');
                                $('#notice').fadeOut(100);
                                $('#notice').fadeIn(100);
                                $('#notice').fadeOut(100);
                                $('#notice').fadeIn(100);

                                return false;
                            } else {
                                $('#notice').html('');
                            }

                            // 조 검사
                            var tmp = [], groupList = [];

                            $('input[name=newGroup]').each(function () {

                                if ($(this).val() == '') return false;
                                tmp.push({
                                    id: id,
                                    groupNm: $(this).val() + '조'
                                });

                                id++;
                            });
                            // 입력된 조가 없으면
                            if (tmp.length == 0) {
                                $('#innerNotice').html('<span style="color:crimson">최소 1개의 조를 배정하세요</span>');
                                $('#innerNotice').fadeOut(100);
                                $('#innerNotice').fadeIn(100);
                                $('#innerNotice').fadeOut(100);
                                $('#innerNotice').fadeIn(100);
                            } else {
                                // 대기실 insert
                                $.ajax({
                                    url: 'system/addHall',
                                    type: 'POST',
                                    contentType: 'application/json; charset=utf-8',
                                    data: JSON.stringify(param),
                                    success: function (response) {
                                        // 중복검사
                                        for (var i = 0; i < tmp.length; i++) {
                                            for (var j = i + 1; j < tmp.length; j++) {
                                                if (tmp[i].groupNm == tmp[j].groupNm) {
                                                    $('#innerNotice').html('<span style="color:crimson">중복된 이름이 있습니다. 확인 후 수정하세요</span>');
                                                    $('#innerNotice').fadeOut(100);
                                                    $('#innerNotice').fadeIn(100);
                                                    $('#innerNotice').fadeOut(100);
                                                    $('#innerNotice').fadeIn(100);

                                                    return false;
                                                }
                                            }

                                            groupList.push({
                                                id: tmp[i].id,
                                                groupNm: tmp[i].groupNm
                                            });
                                        }

                                        // 중복이 없다면 insert
                                        _this.insertGroup(response, groupList);

                                    },
                                    error: function (response) {
                                        responseDialog.notify({msg: response});
                                        return false;
                                    }
                                }); // ajax
                            } // 대기실 insert
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
            dialog.getModalDialog().css('margin-top', '10%');
            dialog.open();
        },
        // 대기실 및 조 수정
        modifyWaitHall: function (response, id) {

            // 각 대기실명을 클릭 시 생성된 조 정보 호출
            $('input[name=hall]').click(function () {

                // #group에 append 되어있는 값 없애기
                $('#group').html('');
                $('#groupInfo').fadeIn(500);
                $('#line').fadeIn(100);
                $('#addGroup').fadeIn(200);

                // 해당하는 groupNm만 append 함
                for (var k = 0; k < response.length; k++) {
                    if (response[k].hallCd == $(this).val()) {
                        if (response[k].groupNm == undefined) {
                            id += 1;
                            $('#group').append('<div style="width: 15%; float: left">' +
                                '<input type="text" size="2" style="margin-right: 10%;" id="' + id + '" name="newGroup">조</div>');
                        }
                        else {
                            $('#group').append('<div style="width: 15%; float: left"><input style="margin-right: 10%" type="checkbox" id="' + k + '"' +
                                'name="group" value=' + response[k].groupNm + ' checked><label style="font-size: large; font-weight: normal" for=' + k + '>' + response[k].groupNm + '</label></div>');
                        }
                    }
                }
            });

            $('#addGroup').click(function () {
                id += 1;
                $('#noticeGroup').html('조 추가 시 값을 기입하지 않으면 추가되지 않습니다');
                $('#group').append('<div style="width: 15%; float: left">' +
                    '<input type="text" size="2" style="margin-right: 10%;" id="' + id + '" name="newGroup">조</div>');
            });

            $('#close').click(function () {
                $('#line').fadeOut(230);
                $('#groupInfo').fadeOut(200);
            });
        },
        // '선택 저장' 버튼 클릭 시
        modifyGroup: function () {
            var _this = this;

            // ori: 기존에 있던 조 이름 중에 유지되는 이름
            // newGroup: 새로 입력한 조, tmp: 업로드할 조 리스트
            var ori = [], newList = [], tmp = [];

            // 기존에 저장되어 있던 조 중에 선택된 조
            $('input[name=group]:checked').each(function () {
                ori.push({
                    id: $(this)[0].id,
                    groupNm: $(this).val()
                });
                tmp.push({
                    id: $(this)[0].id,
                    groupNm: $(this).val()
                });
            });

            // '조 추가' 버튼을 통해 새로 입력된 조
            $('input[name=newGroup]').each(function () {
                if ($(this).val() != '') {
                    tmp.push({
                        id: $(this)[0].id,
                        groupNm: $(this).val() + '조'
                    });
                    newList.push({
                        id: $(this)[0].id,
                        groupNm: $(this).val() + '조'
                    });
                }
            });

            // 기존에 있는 조 인지 검사
            for (var i = 0; i < newList.length; i++) {
                for (var j = 0; j < ori.length; j++) {
                    if (newList[i].groupNm == ori[j].groupNm) {
                        $('#noticeGroup').html('중복된 이름이 있습니다. 확인 후 수정하세요');
                        return false;
                    }
                }
            }

            if (tmp.length == 0) {
                $('#noticeGroup').html('최소 1개의 조를 배정하세요');
                return false;
            }
            // 선택된 대기실의 데이터를 모두 삭제함
            var hallCd = $('input[name=hall]:checked').val();
            $.ajax({
                url: 'system/delWaitHall?hallCd=' + hallCd,
                success: function () {
                    _this.insertGroup(hallCd, tmp);
                } // success
            }); // ajax
        },
        insertGroup: function (hallCd, insertList) {
            // 조 리스트를 새로 insert
            for (var i = 0; i < insertList.length; i++) {
                $.ajax({
                    url: 'system/addWaitHall?hallCd=' + hallCd + '&groupNm=' + insertList[i].groupNm,
                    success: function (response) {
                        responseDialog.notify({msg: response});
                    }
                });
            }
        }
    });
});