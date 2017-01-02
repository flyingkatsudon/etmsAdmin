define(function (require) {
    "use strict";

    var GridBase = require('../dist/jqgrid.js');
    var DlgPdf = require('../dist/dlg-pdf.js');
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
                {name: 'paperCnt', label: '답안지 매수'},
                {name: 'firstPaperCd', label: '원답안지'},
                {name: 'lastPaperCd', label: '최종답안지'},
                {name: 'paperList', label: '교체이력'}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    //url: 'data/paper.json',
                    colModel: colModel/*,
                    onSelectRow : function(rowid, status, e){
                        var param = $(this).jqGrid('getRowData', rowid);
                        if(param.paperCnt=="") {
                            BootstrapDialog.show({
                                title: '답안지 이력',
                                message: '답안지 정보가 업로드되지 않았습니다. 감독관에게 문의하세요.',
                                closable: true,
                                buttons: [{
                                    label: '닫기',
                                    action: function (dialog) {
                                        dialog.close();
                                    }
                                }]
                            });
                        }else {
                            new DlgPdf().setUrl('data/detail.pdf?' + $.param(param)).render();
                        }
                    }*/
                }
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            this.addExcel('data/paper.xlsx');
            return this;
        }
    });
});