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
                {name: 'attendDate', label: '시험일자'},
                {name: 'deptNm', label: '모집단위'},
                {name: 'majorNm', label: '전공'},
                {name: 'groupNm', label: '조'},
                {name: 'examineeCnt', label: '지원자수'},
                {name: 'attendCnt', label: '응시자수'},
                {name: 'attendPer', label: '응시율'},
                {name: 'absentCnt', label: '결시자수'},
                {name: 'absentPer', label: '결시율'}
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
            this.addExcel('export/group?type=xlsx');
            return this;
        }
    });
});