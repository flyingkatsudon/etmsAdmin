/**
 *
 */
define(function (require) {
    "use strict";
    var GridBase = require('./grid-base');

    return GridBase.extend({
        initialize: function (options) {
            var colModel = [
                {name: 'logSeq'},
                {name: 'logDttm'},
                {name: 'exmSeq', hidden: true},
                {name: 'exmDt'},
                {name: 'exmTm'},
                {name: 'hmTypeNm'},
                {name: 'hmKyNm'},
                {name: 'hmDeptNm'},
                {name: 'hallBldgNm'},
                {name: 'hallNm'},
                {name: 'scorerNm'},
                {name: 'cancelMemo'},
                {name: 'mblUid'},
                {name: 'cancelSeq'},
                {name: 'cancelDttm'}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: 'status/attend',
                    datatype:'json',
                    ajaxGridOptions : {contentType : 'application/json;charset=UTF-8'},
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