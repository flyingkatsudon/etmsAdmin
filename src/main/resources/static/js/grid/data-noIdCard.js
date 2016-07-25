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
                {name: 'idCheckDttm', label: '신원확인시간'}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: 'data/noIdCard.json',
                    colModel: colModel,
                    onSelectRow: function (rowid, status, e) {
                        var rowdata = $(this).jqGrid('getRowData', rowid);
                        var url1 = 'image/examinee/' + rowdata.examineeCd + '.jpg'; // 원본
                        var url2 = 'image/noIdCard/' + rowdata.examineeCd + '.jpg'; // 대조본

                        var img = new Image();
                        img.src = url1;
                        // 원본사진 없는 경우
                        img.onerror = function () {
                            BootstrapDialog.show({
                                title: '신분증 미소지자',
                                message: '촬영한 사진이 업로드 전 입니다. 관리자에게 문의하세요.',
                                closable: true,
                                buttons: [{
                                    label: '닫기',
                                    action: function (dialog) {
                                        dialog.close();
                                    }
                                }]
                            });
                        };
                        // 대조본이 없는 경우
                        img.onload = function () {
                            img.src = url2;
                            img.onerror = function () {
                                BootstrapDialog.show({
                                    title: '신분증 미소지자',
                                    message: '촬영한 사진이 업로드 전 입니다. 잠시 후 다시 시도하세요.',
                                    closable: true,
                                    buttons: [{
                                        label: '닫기',
                                        action: function (dialog) {
                                            dialog.close();
                                        }
                                    }]
                                });
                            };
                            img.onload = function () {
                                BootstrapDialog.show({
                                    title: rowdata.examineeCd + '::' + rowdata.examineeNm,
                                    message: '<image src="' + url1 + '" width="400">&nbsp;&nbsp;<image src="' + url2 + '" width="400">',
                                    size: 'size-wide',
                                    closable: true,
                                    onshow: function (dialog) {
                                        if (rowdata.idCheckDttm != "") {
                                            dialog.getButton('check').disable();
                                        }
                                    },
                                    buttons: [{
                                        id: 'check',
                                        label: '신원 확인',
                                        cssClass: 'btn-primary',
                                        action: function (dialog) {
                                            $.ajax({
                                                url: 'data/checkIdCard?examineeCd=' + rowdata.examineeCd,
                                                success: function () {
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