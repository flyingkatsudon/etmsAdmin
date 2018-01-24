define(function (require) {
    "use strict";

    var GridBase = require('../dist/jqgrid.js');
    var BootstrapDialog = require('bootstrap-dialog');

    var dlgDetail = require('text!tpl/attend-detail.html');

    var ResponseDialog = require('../responseDialog.js');
    var responseDialog = new ResponseDialog();

    require('jquery-simple-datetimepicker');

    return GridBase.extend({
        initialize: function (options) {
            var _this = this;
            this.parent = options.parent;
            var colModel = [
                {name: 'admissionNm', label: '전형'},
                {name: 'typeNm', label: '계열'},
                {name: 'attendNm', label: '시험명'},
                {name: 'attendDate', label: '시험일자'},
                {name: 'attendTime', label: '시험시간'},

                {name: 'admissionCd', hidden: true},
                {name: 'attendCd', hidden: true},
                {name: 'attendLen', hidden: true},
                {name: 'isPaperChange', hidden: true},
                {name: 'isUseGroup', hidden: true},
                {name: 'isAssignedGroup', hidden: true},
                {name: 'paperCnt', hidden: true},
                {name: 'paperLen', hidden: true},
                {name: 'paperHeader', hidden: true},
                {name: 'firstAssignPaperCd', hidden: true},
                {name: 'lastAssignPaperCd', hidden: true},
                {name: 'isEmptyHall', hidden: true}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: 'system/attendBasic',
                    colModel: colModel,
                    onCellSelect: function (rowid, index, contents, event) {
                        var colModel = $(this).jqGrid('getGridParam', 'colModel');
                        var rowData = $(this).jqGrid('getRowData', rowid);

                        var dialog = new BootstrapDialog({
                            title: '<h3>' + rowData.admissionNm + '&nbsp;&nbsp;|&nbsp;&nbsp;' + rowData.attendNm + '</h3>',
                            size: 'size-wide',
                            closable: false,
                            onshown: function (dialogRef) {

                                var body = dialogRef.$modalBody;
                                body.append(dlgDetail);

                                _this.drawDetailPart1(rowData);
                                _this.drawDetailPart2(rowData);

                                // '조 배정'을 사용하는 기능은 '사용'일 때만 버튼이 표시되도록
                                _this.drawGroupBtn(rowData.isAssignedGroup, rowData.isEmptyHall, rowData.attendCd); // 기본값

                                $('input[name=isAssignedGroup]').change(function () {
                                    _this.drawGroupBtn($(this).val(), rowData.isEmptyHall, rowData.attendCd); // 변경 시
                                });

                            },
                            buttons: [
                                {
                                    id: 'modify',
                                    label: '변경 내용 저장',
                                    cssClass: 'btn-primary',
                                    action: function (dialog) {

                                        $.ajax({
                                            url: 'system/modifyAttend',
                                            type: 'POST',
                                            contentType: "application/json; charset=utf-8",
                                            data: JSON.stringify(_this.setParam(rowData)),
                                            success: function (response) {
                                                responseDialog.notify({msg: response});
                                                $('#search').trigger('click');
                                            }
                                        });
                                        dialog.close();
                                    }
                                }, {
                                    label: '닫기',
                                    action: function (dialog) {
                                        dialog.close();
                                    }
                                }
                            ]
                        }); // dialog

                        dialog.realize();
                        dialog.getModalDialog().css('margin-top', '5%');
                        dialog.open();
                    }
                }
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            return this;
        },
        drawDetailPart1: function (rowData) {
            $('#basicPart1').append('<div style="margin:3% 0 0 3%;">' + '시험구분' + '<input type="text" id="atNm" class="set-basic" value="' + rowData.attendNm + '"></div>');
            $('#basicPart1').append('<div style="margin:3% 0 0 3%;">' + '계&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;열' + '<input type="text" id="typeNm" class="set-basic"  value="' + rowData.typeNm + '"></div>');
            $('#basicPart1').append('<div style="margin:3% 0 0 3%;">' + '수험번호' + '<input type="text" id="attendLen" class="set-short" value="' + rowData.attendLen + '">&nbsp;자리</div>');

            $('#basicPart2').append('<div style="margin:3% 0 0 3%;">' + '시험일자' + '<input type="text" id="attendDate" class="set-basic"  value="' + rowData.attendDate + '"></div>');
            $('#basicPart2').append('<div style="margin:3% 0 0 3%;">' + '시험시간' + '<input type="text" id="attendTime" class="set-basic"  value="' + rowData.attendTime + '"></div>');

            $('#detailPart1').append(
                '<div style="margin:4% 0 5% 3%;">' + '답안지 교체'
                + '<input style="margin: 0 2% 0 15%;" type="radio" id="changePaper" name="isPaperChange" value=true>사용'
                + '<input style="margin: 0 2% 0 10%;" type="radio" id="notChangePaper" name="isPaperChange" value=false>미사용</div>');

            switch (rowData.isPaperChange) {
                case 'true':
                    $('#changePaper').attr('checked', 'checked');
                    break;
                case 'false':
                    $('#notChangePaper').attr('checked', 'checked');
                    break;
                default:
                    $('#notChangePaper').attr('checked', 'checked');
                    break;
            }

            $('#detailPart1').append(
                '<div style="margin:6% 0 0 3%;">' + '조&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;사용'
                + '<input style="margin: 0 2% 0 15%;" type="radio" id="useGroup" name="isUseGroup" value=true>사용'
                + '<input style="margin: 0 2% 0 10%;" type="radio" id="notUseGroup" name="isUseGroup" value=false>미사용</div>');

            switch (rowData.isUseGroup) {
                case 'true':
                    $('#useGroup').attr('checked', 'checked');
                    break;
                case 'false':
                    $('#notUseGroup').attr('checked', 'checked');
                    break;
                default:
                    $('#notUseGroup').attr('checked', 'checked');
                    break;
            }

            $('#detailPart1').append(
                '<div style="margin:6% 0 0 3%;">' + '조&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;배정'
                + '<input style="margin: 0 2% 0 15%;" type="radio" id="assignedGroup" name="isAssignedGroup" value=true>사용'
                + '<input style="margin: 0 2% 0 10%;" type="radio" id="notAssignedGroup" name="isAssignedGroup" value=false>미사용</div>');

            switch (rowData.isAssignedGroup) {
                case 'true':
                    $('#assignedGroup').attr('checked', 'checked');
                    break;
                case 'false':
                    $('#notAssignedGroup').attr('checked', 'checked');
                    break;
                default:
                    $('#notAssignedGroup').attr('checked', 'checked');
                    break;
            }

            $('#detailPart1').append('<div id="groupBtn"></div>');
        },
        drawDetailPart2: function (rowData) {
            $('#detailPart2').append('<div style="margin:2% 0 0 3%;">답안지&nbsp;&nbsp;&nbsp;&nbsp;매수<input type="text" id="paperCnt" class="set-short" value="' + rowData.paperCnt + '">&nbsp;장</div>');
            $('#detailPart2').append('<div style="margin:3% 0 0 3%;">답안지 자리수<input type="text" id="paperLen" class="set-short" value="' + rowData.paperLen + '">&nbsp;자리</div>');

            if (rowData.paperHeader == undefined) rowData.paperHeader = '';
            $('#detailPart2').append('<div style="margin:3% 0 0 3%;">답안지&nbsp;&nbsp;&nbsp;&nbsp;헤더<input type="text" id="paperHeader" style="width: 20%; text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value="' + rowData.paperHeader + '"></div>');

            if (rowData.firstAssignPaperCd == undefined) rowData.firstAssignPaperCd = '';
            $('#detailPart2').append('<div style="margin:3% 0 0 3%;">답안지&nbsp;&nbsp;&nbsp;&nbsp;시작<input type="text" id="firstAssignPaperCd" style="width: 20%; text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value="' + rowData.firstAssignPaperCd + '"></div>');

            if (rowData.lastAssignPaperCd == undefined) rowData.lastAssignPaperCd = '';
            $('#detailPart2').append('<div style="margin:3% 0 0 3%;">답안지&nbsp;&nbsp;&nbsp;&nbsp;종료<input type="text" id="lastAssignPaperCd" style="width: 20%; text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value="' + rowData.lastAssignPaperCd + '"></div>');

            $('#attendDate').appendDtpicker({
                autodateOnStart: false,
                dateOnly: true,
                dateFormat: 'YYYY-MM-DD'
            });

            $('#attendTime').appendDtpicker({
                timeOnly: true,
                autodateOnStart: false
            });
        },
        drawGroupList: function (attendCd) {

            var _this = this;

            var html = '';

            $.ajax({
                url: 'system/ahList?attendCd=' + attendCd,
                success: function (response) {

                    for (var i = 0; i < response.length; i++) {

                        if (response[i].groupNmList == undefined) response[i].groupNmList = '';

                        var tmp =
                            '<h5><div class="col-lg-12">' +
                            '<div style="text-align:center; font-weight: normal; font-size: medium">' +
                            '<div class="col-lg-4">' +
                            '<div style="float:left">고사건물</div>' + '<div><input disabled type="text" name="bldg" id="bldgNm' + i + '" class="set-basic"  value="' + response[i].bldgNm + '"></div></div>' +
                            '<div class="col-lg-4">' +
                            '<div style="float:left">&nbsp;&nbsp;&nbsp;&nbsp;고사실</div>' + '<div><input disabled type="text" name="hall" id="hallNm' + i + '" class="set-basic"  value="' + response[i].hallNm + '"></div></div>' +
                            '<div class="col-lg-4">' +
                            '<div style="float:left">배&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;정</div>' + '<div><input type="text" id="groupNmList' + i + '" class="set-basic"  value="' + response[i].groupNmList + '"></div></div>' +
                            '</div></h5>';

                        if (i != response.length - 1)
                            tmp += '<div class="col-lg-12"><div id="line" style="margin: 3% 0 3% 0; border: 1px solid #d8d6d5"></div></div>';

                        html += tmp;
                    }

                    var innerDialog = new BootstrapDialog({
                        message: html,
                        buttons: [
                            {
                                id: 'innerModify',
                                label: '변경 내용 저장',
                                cssClass: 'btn-primary',
                                action: function (dialog) {

                                    _this.setInnerParam(attendCd, response);

                                    dialog.close();
                                }
                            }, {
                                label: '닫기',
                                action: function (dialog) {
                                    dialog.close();
                                }
                            }
                        ]
                    });

                    innerDialog.realize();
                    innerDialog.getModalDialog().css('margin-top', '20%');
                    innerDialog.getModalDialog().css('width', '70%');
                    innerDialog.getModalHeader().hide();
                    innerDialog.getModalFooter().css('border', '0px solid #ffffff');
                    innerDialog.open();
                }
            });


        },
        drawGroupBtn: function (isAssignedGroup, isEmptyHall, attendCd) {
            var _this = this;

            // '조 배정'을 사용하면
            if (isAssignedGroup == 'true') {
                // '배정' 구역을 보이도록 함
                $('#groupBtn').show();
                // 등록된 조가 있으면 조 '수정'
                if (isEmptyHall == 'true') {
                    $('#groupBtn').html(
                        '<div style="margin:8% 0 0 3%;">' + '배&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;정'
                        + '<button class="btn-success" style="margin-left: 15%; width: 40%; padding: 2%; border-radius: 10px;" id="modifyGroup">조 수정</button>');
                } else {
                    $('#groupBtn').html(
                        '<div style="margin:8% 0 0 3%;">' + '배&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;정'
                        + '<button class="btn-primary" style="margin-left: 15%; width: 40%; padding: 2%; border-radius: 10px;" id="modifyGroup">조 등록</button>');
                }
            } else {
                $('#groupBtn').hide()
            }

            $('#modifyGroup').click(function () {
                _this.drawGroupList(attendCd);
            });

        },
        setParam: function (rowData) {

            var param = {
                admissionCd: rowData.admissionCd,
                admissionNm: rowData.admissionNm,
                attendCd: rowData.attendCd,
                attendNm: $('#atNm').val(),
                typeNm: $('#typeNm').val(),
                attendLen: $('#attendLen').val(),
                atDate: $('#attendDate').val(),
                atTime: $('#attendTime').val(),
                isPaperChange: $('input[name=isPaperChange]:checked').val(),
                isUseGroup: $('input[name=isUseGroup]:checked').val(),
                isAssignedGroup: $('input[name=isAssignedGroup]:checked').val(),
                paperCnt: $('#paperCnt').val(),
                paperLen: $('#paperLen').val(),
                paperHeader: $('#paperHeader').val(),
                firstAssignPaperCd: $('#firstAssignPaperCd').val(),
                lastAssignPaperCd: $('#lastAssignPaperCd').val(),
                groupNmList: $('#groupNmList').val()
            };

            if (param.attendNm == '' || param.typeNm == '' || param.atDate == '' || param.atTime == ''
                || param.paperCnt == '' || param.paperLen == '' || param.attendLen == '') {
                $('#basicPart2').append('<div style="margin:10% 0 0 0; text-align:center"><h4 style="margin-top: 1%; color:crimson">빈 항목을 확인하세요</h4></div>');

                return false;
            }

            if (param.paperHeader == '' || param.paperHeader == '-') param.paperHeader = null;

            if (param.firstAssignPaperCd == '' || param.firstAssignPaperCd == '-') param.firstAssignPaperCd = null;
            if (param.lastAssignPaperCd == '' || param.lastAssignPaperCd == '-') param.lastAssignPaperCd = null;

            if (param.groupNmList == '') param.groupNmList = null;

            return param;
        },
        setInnerParam: function (attendCd, response) {

            for (var i = 0; i < response.length; i++) {
                var tmp = {
                    id: response[i].id,
                    attendCd: attendCd,
                    hallCd: response[i].hallCd,
                    groupNmList: $('#groupNmList' + i).val()
                };

                if (tmp.groupNmList == '') tmp.groupNmList = null;

                $.ajax({
                    url: 'system/modifyAhList',
                    type: 'POST',
                    contentType: "application/json; charset=utf-8",
                    data: JSON.stringify(tmp),
                    success: function (response) {
                        responseDialog.notify({msg: response});
                        $('#search').trigger('click');
                    }
                });
            }
        }
    });
});