/**
 *
 */
define(function (require) {
    "use strict";
    var GridBase = require('./grid-base.js');

    return GridBase.extend({
        initialize: function (options) {
            var colModel = [
                {name: 'hallCd', label: '테스트'},
                {name: 'admissionNm', label: '구분'},
                {name: 'hmTypeNm', label: '전형'},
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
                    url: 'status/attend',
                    colModel: colModel
                }
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            return this;
        },
    });
});