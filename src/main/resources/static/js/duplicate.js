define(function (require) {
    "use strict";

    var GridBase = require('./dist/jqgrid.js');
    var BootstrapDialog = require('bootstrap-dialog');
    var InnerDuplicate = require('./innerDuplicate.js');

    return GridBase.extend({
        initialize: function (options) {
            var colModel = [
                {name: 'attendNm', label: '시험명'},
                {name: 'attendDate', label: '시험일자'},
                {name: 'attendTime', label: '시험시간'},
                {name: 'examineeCd', label: '수험번호'},
                {name: 'examineeNm', label: '수험생명'},
                {name: 'duplicateCnt', label: '처리단말기'},
                {name: 'attendCd', hidden: true}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: 'system/duplicate',
                    colModel: colModel,
                    onCellSelect: function (rowid, index, contents, event) {
                        var colModel = $(this).jqGrid('getGridParam', 'colModel');
                        var rowData = $(this).jqGrid('getRowData', rowid);

                        var dialog = new BootstrapDialog({
                            title: '<h4>데이터를 확인하세요. 푸른 색상 영역이 마지막 처리 상태입니다</h4>',
                            size: 'size-wide',
                            onshown: function (dialog) {
                                var param = {
                                    attendCd: rowData.attendCd,
                                    examineeCd: rowData.examineeCd
                                };

                                dialog.list = new InnerDuplicate({el: dialog.$modalBody, param: param}).render();
                            }
                        });

                        dialog.realize();
                        dialog.getModalDialog().css('margin-top', '20%');
                        dialog.getModalDialog().css('width', '80%');
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