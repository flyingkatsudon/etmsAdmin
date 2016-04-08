/**
 *
 */
define(function (require) {
    "use strict";
    var GridBase = require('./grid-base.js');

    return GridBase.extend({
        initialize: function (options) {
            var colModel = [
                {name: 'typeNm', label: '계열'},
                {name: 'attendDate', label: '시험일자'},
                {name: 'deptNm', label: '모집단위'},
                {name: 'majorNm', label: '전공'},
                {name: 'examNm', label: '시험명'},
                {name: 'bldgNm', label: '고사건물'},
                {name: 'hallNm', label: '고사실'},
                {name: 'scorerNm', label: '평가위원'},
                {name: 'isSend', label: '전송여부'}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    //url: 'data/examinee',
                    colModel: colModel
                }
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            this.addExcel('export/attend?type=excel');
            return this;
        }
    });
});