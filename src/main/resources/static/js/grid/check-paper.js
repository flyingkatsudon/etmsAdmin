define(function (require) {
    "use strict";

    var GridBase = require('../dist/jqgrid.js');

    return GridBase.extend({
        initialize: function (options) {
            var colModel = [
                {name: 'admissionNm', label: '전형'},
                {name: 'typeNm', label: '계열'},
                {name: 'attendDate', label: '시험일자'},
                {name: 'attendTime', label: '시험시간'},
                {name: 'majorNm', label: '전공'},
                {name: 'examineeCd', label: '수험번호'},
                {name: 'examineeNm', label: '수험생명'},
                {name: 'attendHeadNm', label: '응시고사본부'},
                {name: 'attendBldgNm', label: '응시고사건물'},
                {name: 'attendHallNm', label: '응시고사실'},
                {
                    name: 'isAttend',
                    label: '응시여부',
                    formatter: 'select',
                    editoptions: {value: {true: '응시', false: '미응시'}}
                },
                {name: 'lastPaperCd', label: '답안지번호'}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: 'check/paper.json',
                    colModel: colModel
                    , loadComplete: function (data) {
                        var ids = $(this).getDataIDs(data);

                        for (var i = 0; i < ids.length; i++) {
                            var rowData = $(this).getRowData(ids[i]);
                            if (rowData.attendHallNm == '' && rowData.lastPaperCd != '') {
                                $(this).setRowData(ids[i], false, {background: '#f5a7a4'});
                            }
                            else if (rowData.attendHallNm != '' && rowData.lastPaperCd == '') {
                                $(this).setRowData(ids[i], false, {background: '#f5a7a4'});
                            }
                        }
                    }
                }
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            this.addExcel('check/paper.xlsx');
            return this;
        }
    });
});