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
                {name: 'deptNm', label: '모집단위'},
                {name: 'majorNm', label: '전공'},
                {name: 'examineeCd', label: '수험번호'},
                {name: 'examineeNm', label: '수험생명'},
                {name: 'birth', label: '생년월일'},
                {name: 'headNm', label: '배정고사본부'},
                {name: 'bldgNm', label: '배정고사건물'},
                {name: 'hallNm', label: '배정고사실'},
                {name: 'attendHeadNm', label: '응시고사본부'},
                {name: 'attendBldgNm', label: '응시고사건물'},
                {name: 'attendHallNm', label: '응시고사실'},
                {name: 'isAttend', label: '응시여부', formatter: 'select', editoptions: {value: {true: '응시', false: '결시'}}},
                {name: 'insertType', label: '입력방식'},
                {name: 'isChangePaper', label: '답안지교체여부', formatter: 'select', editoptions: {value: {true: '교체', false: '미교체'}}},
                {name: 'lastPaperCd', label: '답안지번호'},
                {name: 'isOtherHall', hidden: true, label: '타고사실여부', formatter: 'select', editoptions: {value: {true: '예', false: '아니오'}}},
                {name: 'isCancelAttend', hidden: true, label: '응시취소여부', formatter: 'select', editoptions: {value: {true: '예', false: '아니오'}}}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }
/*

            var colModel = [];

            $.ajax({
                url: 'data/examinee.colmodel',
                async: false,
                success: function (data) {
                    colModel = data;
                }
            });
*/

            for(var i = 0; i < colModel.length; i++){
                var col = colModel[i];
                col['fixed'] = true;
                col['width'] = 150;
            }

            var opt = $.extend(true, {
                defaults: {
                    //url: 'data/examinee.json',
                    colModel: colModel
                }
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            this.$grid.closest('.ui-jqgrid-bdiv').css('overflow-x', 'auto');
            this.addExcel('data/examinee.xlsx');
            return this;
        }
    });
});