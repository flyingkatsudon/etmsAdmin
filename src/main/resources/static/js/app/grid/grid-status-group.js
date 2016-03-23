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
                {name: 'admissionType', label: '전형'}, // 음악특기자, 연기특기자 등
                {name: 'deptNm', label: '모집단위'},
                {name: 'majorNm', label: '전공'},
                {name: 'groupNm', label: '조'}, // 필드명 미정
                {name: 'attendPer', label: '응시율'},
                {name: 'examineeCnt', label: '지원자'},
                {name: 'attendCnt', label: '응시자'},
                {name: 'absentCnt', label: '결시자'}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: 'status/group',
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