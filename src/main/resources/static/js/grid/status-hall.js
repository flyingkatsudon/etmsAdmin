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
                {name: 'headNm', label: '고사본부'},
                {name: 'bldgNm', label: '고사건물'},
                {name: 'hallNm', label: '고사실'},
                {name: 'examineeCnt', label: '지원자수', formatter: 'integer', formatoptions: {thousandsSeparator: ','}},
                {name: 'attendCnt', label: '응시자수', formatter: 'integer', formatoptions: {thousandsSeparator: ','}},
                {name: 'otherHallCnt', label: '타응시자수', formatter: 'integer', formatoptions: {thousandsSeparator: ','}},
                {name: 'attendPer', label: '응시율', formatter: 'number', formatoptions: {suffix: '%'}},
                {name: 'absentCnt', label: '결시자수', formatter: 'integer', formatoptions: {thousandsSeparator: ','}},
                {name: 'absentPer', label: '결시율', formatter: 'number', formatoptions: {suffix: '%'}}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: 'status/hall.json',
                    colModel: colModel
                }
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            this.addExcel('status/hall.xlsx');
            return this;
        }
    });
});