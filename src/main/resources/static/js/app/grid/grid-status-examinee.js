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
                {name: 'admissionType', label: '전형'},
                {name: 'deptNm', label: '모집단위'},
                {name: 'majorNm', label: '전공'},
                {name: 'groupNm', label: '조'},
                {name: 'examineeCd', label: '수험번호'},
                {name: 'examineeNm', label: '수험생'},
                {name: 'attendDt', label: '시험일자'},
                {name: 'attendTm', label: '시험시간'},
                {name: 'isAttend', label: '응시여부'}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: 'status/examinee',
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