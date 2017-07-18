define(function (require) {
    "use strict";

    var GridBase = require('../dist/jqgrid.js');
    var BootstrapDialog = require('bootstrap-dialog');

    return GridBase.extend({
        initialize: function (options) {
            var colModel = [
                {name: 'admissionNm', label: '전형'},
                {name: 'typeNm', label: '계열'},
                {name: 'attendDate', label: '시험일자'},
                {name: 'attendTime', label: '시험시간'},
                {name: 'headNm', label: '고사본부'},
                {name: 'bldgNm', label: '고사건물'},
                {name: 'deptNm', label: '모집단위'},
                /*{name: 'majorNm', label: '전공'},*/
                {name: 'examineeCd', label: '수험번호'},
                {name: 'examineeNm', label: '수험생명'},
                {name: 'birth', label: '생년월일'},
                {name: 'attendDttm', label: '응시처리시간'},
                {name: 'idCheckDttm', label: '신원확인시간'},
                {
                    name: 'btnIdCheck', label: '신원확인', formatter: function (cellValue, option) {
                    var rowid = option.rowId;
                    return '<button class="btn-primary" id="checkBtn" value="' + rowid + '">사진보기</button>';
                }
                },
                {name: 'attendCd', hidden: true}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    //url: 'data/noIdCard.json',
                    colModel: colModel,
                    onCellSelect: function (rowid, index, contents, event) {
                        var colModel = $(this).jqGrid('getGridParam', 'colModel');
                        if (colModel[index].name == 'btnIdCheck') {
                            var _this = this;
                            var checkBtn = '<button id="checkBtn" value="' + rowid + '">사진보기</button>';
                            var rowdata = $(this).jqGrid('getRowData', rowid);
                            var url1 = 'image/examinee/' + rowdata.examineeCd + '.jpg'; // 원본
                            var url2 = 'image/noIdCard/' + rowdata.examineeCd + '.jpg'; // 대조본

                            var dialog = new BootstrapDialog({
                                title: '<h3>' + rowdata.deptNm + ' | ' + rowdata.examineeCd + ' | ' + rowdata.examineeNm + '</h3>',
                                // message: '이미지 로딩중입니다.',
                                message: '<div style="text-align:center"><image src="' + url1 + '" height="500px"/>&nbsp;&nbsp;&nbsp;&nbsp;<image src="' + url2 + '" style="height: 500px"/></div>',
                                size: 'size-wide',
                                closable: true,
                                onshow: function (dialog) {
                                    var img = new Image();
                                    img.src = url2;
                                    img.onload = function () {
                                        //  dialog.$modalBody.html('<image src="' + url1 + '" width="400">&nbsp;&nbsp;<image src="' + url2 + '" width="400">');
                                        if (rowdata.idCheckDttm) {
                                            dialog.getButton('idCheck').disable();
                                            dialog.getButton('cancel').show();
                                        }
                                    };
                                    img.onerror = function () {
                                        // dialog.$modalBody.html('잠시 후 다시 시도하세요');
                                        dialog.$modalBody.html('<div style="text-align:center"><image src="' + url1 + '" height="500px"/>&nbsp;&nbsp;&nbsp;&nbsp;<image src="image/noIdCard/img-default.jpg" width="400"/></div>');
                                        dialog.getButton('idCheck').remove();
                                        var tmp = new BootstrapDialog({
                                                message: '<h4 style="margin-left:10%; font-weight: normal">사진이 아직 업로드되지 않았습니다. 단말기를 확인해주세요</h4>',
                                            });

                                        tmp.realize();
                                        tmp.getModalDialog().css('margin-top', '20%');
                                        tmp.getModalFooter().css('padding', '1%');
                                        tmp.getModalHeader().hide();
                                        tmp.open();
                                    };
                                },
                                buttons: [
                                    {
                                        id: 'idCheck',
                                        label: '신원 확인',
                                        cssClass: 'btn-primary',
                                        action: function () {
                                            var innerDialog = new BootstrapDialog({
                                                message: '<h4 style="margin-left:10%; font-weight: normal">신원확인을 하면 <span style="color:red; font-weight: bold">취소</span>할 수 없습니다. 계속하시겠습니까?</h4>',
                                                closable: false,
                                                buttons: [{
                                                    label: '예',
                                                    cssClass: 'btn-primary',
                                                    action: function () {
                                                        $.ajax({
                                                            url: 'data/checkIdCard?examineeCd=' + rowdata.examineeCd + '&attendCd=' + rowdata.attendCd,
                                                            success: function (data) {
                                                                $(_this).jqGrid('setCell', rowid, 'idCheckDttm', data);
                                                                BootstrapDialog.closeAll();
                                                            }
                                                        });
                                                    }
                                                },{
                                                    label: '아니오',
                                                    action: function(dialog){
                                                        dialog.close();
                                                    }
                                                }]
                                            });

                                            innerDialog.realize();
                                            innerDialog.getModalDialog().css('margin-top', '20%');
                                            innerDialog.getModalFooter().css('padding', '1%');
                                            innerDialog.getModalHeader().hide();
                                            innerDialog.open();
                                        }
                                    }, {
                                        label: '닫기',
                                        action: function (dialog) {
                                            dialog.close();
                                        }
                                    }]
                            });

                            dialog.realize();
                            dialog.getModalDialog().css('margin-top', '5%');
                            dialog.open();
                        }
                    }
                }
            }, options);
            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            this.addExcel('data/noIdCard.xlsx');
            return this;
        }
    });
});