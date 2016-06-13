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
                {name: 'isCheck', label: '확인', formatter: 'select', editoptions: {value: {true: 'Y', false: 'N'}}}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: 'data/recheck.json',
                    colModel: colModel,
                    onSelectRow: function (rowid, status, e) {
                        var rowdata = $(this).jqGrid('getRowData', rowid); // 선택된 row의 데이터 가져오기
                        var url1 = 'image/examinee/' + rowdata.examineeCd + '.jpg'; // 원본
                        var url2 = 'image/recheck/' + rowdata.examineeCd + '.jpg'; // 대조본
                        BootstrapDialog.show({
                            title : rowdata.examineeCd + '::' + rowdata.examineeNm,
                            message: '<image src="' + url1 + '"><image src="' + url2 + '">',
                            size: 'size-wide',
                            closable: false,
                            buttons: [{
                                label: '닫기',
                                action: function (dialog) {
                                    dialog.close();
                                }
                            }]
                        });
                    }
                }
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            this.addExcel('data/recheck.xlsx');
            return this;
        }
    });
});