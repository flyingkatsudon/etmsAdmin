/**
 *
 */
define(function (require) {
    "use strict";
    var GridBase = require('./grid-base.js');

    return GridBase.extend({
        initialize: function (options) {
            var colModel = [
                {name: 'admissionNm', label: '전형'},
                {name: 'typeNm', label: '계열'},
                {name: 'weirdNm', label: '특이사항'},
                {name: 'attendDate', label: '시험일자'},
                {name: 'attendTime', label: '시험시간'},
                {name: 'attendCnt', label: '응시자수', formatter: 'integer', formatoptions: {thousandsSeparator: ','}},
                {name: 'nonTargetCnt', label: '비대상자', formatter: 'integer', formatoptions: {thousandsSeparator: ','}},
                {name: 'targetCnt', label: '대상자', formatter: 'integer', formatoptions: {thousandsSeparator: ','}}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: 'data/examinee/list',
                    colModel: colModel
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