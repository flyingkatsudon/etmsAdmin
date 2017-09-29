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
                {name: 'lastAssignPaperCd', hidden: true}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: 'system/attendInfo',
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

                                $('#detailPart2').append('<div style="margin:2% 0 0 3%;">답안지&nbsp;&nbsp;&nbsp;&nbsp;매수<input type="text" id="paperCnt" class="set-short" value="' + rowData.paperCnt + '">&nbsp;장</div>');
                                $('#detailPart2').append('<div style="margin:3% 0 0 3%;">답안지 자리수<input type="text" id="paperLen" class="set-short" value="' + rowData.paperLen + '">&nbsp;자리</div>');

                                if(rowData.paperHeader == null) rowData.paperHeader = '';
                                $('#detailPart2').append('<div style="margin:3% 0 0 3%;">답안지&nbsp;&nbsp;&nbsp;&nbsp;헤더<input type="text" id="paperHeader" style="width: 20%; text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value="' + rowData.paperHeader + '"></div>');

                                if(rowData.firstAssignPaperCd == null) rowData.firstAssignPaperCd = '';
                                $('#detailPart2').append('<div style="margin:3% 0 0 3%;">답안지&nbsp;&nbsp;&nbsp;&nbsp;시작<input type="text" id="firstAssignPaperCd" style="width: 20%; text-align:center; border-radius: 10px; padding: 1%; margin-left: 10%; color: #727272" value="' + rowData.firstAssignPaperCd + '"></div>');

                                if(rowData.lastAssignPaperCd == null) rowData.lastAssignPaperCd = '';
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
                            buttons: [
                                {
                                    id: 'modify',
                                    label: '변경 내용 저장',
                                    cssClass: 'btn-primary',
                                    action: function (dialog) {

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
                                            lastAssignPaperCd: $('#lastAssignPaperCd').val()
                                        };

                                        if(param.attendNm == '' || param.typeNm == '' || param.atDate == '' || param.atTime == ''
                                                                || param.paperCnt == '' || param.paperLen == '' || param.attendLen == ''){
                                            $('#basicPart2').append('<div style="margin:10% 0 0 0; text-align:center"><h4 style="margin-top: 1%; color:crimson">빈 항목을 확인하세요</h4></div>');

                                            return false;
                                        }


                                        if(param.paperHeader == '' || param.paperHeader == '-') param.paperHeader = null;

                                        $.ajax({
                                            url: 'system/modifyAttend',
                                            type: 'POST',
                                            contentType: "application/json; charset=utf-8",
                                            data: JSON.stringify(param),
                                            success: function(response){
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
        }
    });
});