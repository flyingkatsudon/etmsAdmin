/**
 * Created by Jeremy on 2017. 12. 13..
 */
define(function (require) {
    "use strict";

    var GridBase = require('../dist/jqgrid.js');

    var ResponseDialog = require('../responseDialog.js');
    var responseDialog = new ResponseDialog();

    return GridBase.extend({
        initialize: function (options) {
            this.param = options.param;
            var colModel = [
                {name: 'examineeCd', label: '수험번호'},
                {name: 'examineeNm', label: '수험생명'},
                /*{name: 'attendHeadNm', label: '응시고사본부'},*/
                {name: 'attendBldgNm', label: '응시고사건물'},
                {name: 'attendHallNm', label: '응시고사실'},
                {
                    name: 'isAttend',
                    label: '응시여부',
                    formatter: 'select',
                    editoptions: {value: {true: '응시', false: '결시'}}
                },
                {name: 'deviceNo', label: '단말기번호'},
                {name: 'paperCd', label: '답안지번호'},
                {name: 'regDttm', label: '등록시간'},
                {name: 'attendCd', hidden: true}

                /*
                 {name: 'admissionNm', label: '전형'},
                 {name: 'typeNm', label: '계열'},
                 {name: 'attendDate', label: '시험일자'},
                 {name: 'attendTime', label: '시험시간'},
                 {name: 'paperCd', label: '답안지번호'},
                 {name: 'duplicateCnt', label: '중복횟수'}*/
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }
            var opt = $.extend(true, {
                defaults: {
                    url: 'check/detect2?attendCd=' + this.param.attendCd + '&paperCd=' + this.param.paperCd,
                    colModel: colModel,
                    loadComplete: function(data){
                        var ids = $(this).getDataIDs(data);
                        $(this).setRowData('1', false, {background: "#afd5de"});

                        for (var i = 2; i < ids.length; i++) {
                            var rowData = $(this).getRowData(ids[i]);
                            if (rowData.isAttend == 'false') $(this).setRowData(ids[i], false, {background: "#FFD8D8"});
                        }
                    }
                }
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            //this.$grid.closest('.ui-jqgrid-bdiv').css('overflow-x', 'auto');
            //this.addExcel('check/paper.xlsx');
            return this;
        }
    });
});