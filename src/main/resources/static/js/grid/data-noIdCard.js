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
                {name: 'deptNm', label: '모집단위'},
                {name: 'majorNm', label: '전공'},
                {name: 'examineeCd', label: '수험번호'},
                {name: 'examineeNm', label: '수험생명'},
                {name: 'birth', label: '생년월일'},
                {name: 'attendHeadNm', label: '응시고사본부'},
                {name: 'attendBldgNm', label: '응시고사건물'},
                {name: 'attendHallNm', label: '응시고사실'},
                {
                    name: 'isOtherHall',
                    label: '타고사실여부',
                    formatter: 'select',
                    editoptions: {value: {true: '예', false: '아니오'}}
                },
                {name: 'idCheckDttm', label: '신원확인시간'},
                {
                    name: 'btnIdCheck', label: '신원확인', formatter: function (cellValue, option) {
                    var rowid = option.rowId;
                    return '<button id="checkBtn" value="' + rowid + '">확인</button>';
                    }
                },
                {name: 'attendCd', hidden: true}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: 'data/noIdCard.json',
                    colModel: colModel,
                    onCellSelect: function (rowid, index, contents, event) {
                        var colModel = $(this).jqGrid('getGridParam', 'colModel');
                        if (colModel[index].name == 'btnIdCheck') {
                            var _this = this;
                            var checkBtn = '<button id="checkBtn" value="' + rowid + '">확인</button>';
                            var rowdata = $(this).jqGrid('getRowData', rowid);
                            var url1 = 'image/examinee/' + rowdata.examineeCd + '.jpg'; // 원본
                            var url2 = 'image/noIdCard/' + rowdata.examineeCd + '.jpg'; // 대조본

                            BootstrapDialog.show({
                                title: rowdata.examineeCd + '::' + rowdata.examineeNm,
                                message: '이미지 로딩중입니다.',
                                size: 'size-wide',
                                closable: true,
                                onshow: function (dialog) {
                                    var img = new Image();
                                    img.src = url2;
                                    img.onload = function () {
                                        dialog.$modalBody.html('<image src="' + url1 + '" width="400">&nbsp;&nbsp;<image src="' + url2 + '" width="400">');
                                        if (rowdata.idCheckDttm) {
                                            dialog.getButton('idCheck').disable();
                                        }
                                    };
                                    img.onerror = function () {
                                        dialog.$modalBody.html('잠시 후 다시 시도하세요');
                                        dialog.getButton('idCheck').remove();
                                    }
                                },
                                buttons: [
                                    {
                                        id: 'idCheck',
                                        label: '신원 확인',
                                        cssClass: 'btn-primary',
                                        action: function (dialog) {
                                            $.ajax({
                                                url: 'data/checkIdCard?examineeCd=' + rowdata.examineeCd + '&attendCd=' + rowdata.attendCd,
                                                success: function (data) {
                                                    $(_this).jqGrid('setCell', rowid, 'idCheckDttm', data);
                                                    dialog.close();
                                                }
                                            });
                                        }
                                    }, {
                                        label: '닫기',
                                        action: function (dialog) {
                                            dialog.close();
                                        }
                                    }]
                            });
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