/**
 * Created by Jeremy on 2017. 12. 12..
 */
define(function (require) {
    "use strict";

    var GridBase = require('../dist/jqgrid.js');

    var BootstrapDialog = require('bootstrap-dialog');
    var DuplicateDetail = require('../grid/check-paper-detail.js');

    var ResponseDialog = require('../responseDialog.js');
    var responseDialog = new ResponseDialog();

    return GridBase.extend({
        initialize: function (options) {
            this.param = options.param;

            var colModel = [
                {name: 'admissionNm', label: '전형'},
                {name: 'typeNm', label: '계열'},
                {name: 'attendDate', label: '시험일자'},
                {name: 'attendTime', label: '시험시간'},
                {name: 'examineeCd', label: '수험번호'},
                {name: 'examineeNm', label: '수험생명'},
                /*{name: 'actionCnt', label: '처리횟수'},*/
                {name: 'isAttend', label: '현재출결상태', formatter: 'select', editoptions: {value: {true: '응시', false: '결시'}}},
                {name: 'logDttm', label: '최종출결처리'},
                {name: 'attendCd', hidden: true}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    colModel: colModel,
                    loadComplete: function (data) {
                        var ids = $(this).getDataIDs(data);

                        for (var i = 0; i < ids.length; i++) {
                            var rowData = $(this).getRowData(ids[i]);
                            if (rowData.isAttend == 'false') $(this).setRowData(ids[i], false, {background: "#FFD8D8"});
                        }
                    },
                    onCellSelect: function (rowid, index, contents, event) {
                        var rowData = $(this).jqGrid('getRowData', rowid);

                        $.ajax({
                            url: 'check/detail?attendCd=' + rowData.attendCd + '&examineeCd=' + rowData.examineeCd,
                            success: function (response) {

                                var html = '';
                                for(var i=0; i<response.length; i++){
                                    html += response[i].action;
                                }

                                var dialog = new BootstrapDialog({
                                    title: '<h4>다중 처리 이력을 확인하세요</h4>',
                                    message: html,
                                    buttons: [
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
                                dialog.getModalDialog().css('width', '60%');
                                dialog.open();
                            }
                        });
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