/**
 *
 */
define(function (require) {
    "use strict";
    var GridBase = require('./grid-base.js');

    return GridBase.extend({
        initialize: function (options) {
            var colModel = [
                {name: 'admissionNm', label: '구분'},
                {name: 'weirdNm', label: '특이사항'}, // 필드명 미생성
                {name: 'typeNm', label: '계열'},  // 인문, 자연, 예체능 등
                {name: 'attendCnt', label: '응시자'},
                {name: 'nonTargetCnt', label: '비대상자'},
                {name: 'targetCnt', label: '대상자'},
                {name: 'attendDt', label: '시험일자'},
                {name: 'attendTm', label: '시험시간'}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    //url: 'data/summary',
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