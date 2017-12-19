define(function (require) {
    "use strict";

    var GridBase = require('../dist/jqgrid.js');
    var BootstrapDialog = require('bootstrap-dialog');

    return GridBase.extend({
        initialize: function (options) {
            var colModel = [
                {name: 'admissionNm', label: '전형'},
                {name: 'typeNm', label: '계열'},
                {name: 'attendDate', label: '시험일자'},
                {name: 'attendTime', label: '시험시간'},
                {name: 'examineeCd', label: '수험번호'},
                {name: 'examineeNm', label: '수험생명'},
                {name: 'paperCd', label: '답안지번호'},
                {
                    name: 'isAttend',
                    label: '응시여부',
                    formatter: 'select',
                    editoptions: {value: {true: '응시', false: '결시'}}
                },
                {name: 'attendHeadNm', label: '응시고사본부'},
                {name: 'attendBldgNm', label: '응시고사건물'},
                {name: 'attendHallNm', label: '응시고사실'},
                {name: 'regDttm', label: '등록시간'}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            for (var i = 0; i < colModel.length; i++) {
                var col = colModel[i];
                col['fixed'] = true;
                col['width'] = 150;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: options.url,
                    colModel: colModel,
                    onCellSelect: function (rowid, index, contents, event) {
                        var colModel = $(this).jqGrid('getGridParam', 'colModel');

                        var dialog = new BootstrapDialog({
                            title: '답안지 번호 수정',
                            message: '준비중입니다',
                        });

                        dialog.realize();

                        dialog.getModalDialog().css('margin-top', '20%');
                        dialog.getModalDialog().css('text-align', 'center');
                        dialog.getModalHeader().hide();
                        dialog.getModalFooter().hide();

                        dialog.open();
                    }
                }
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            this.constructor.__super__.render.call(this);
            this.$grid.closest('.ui-jqgrid-bdiv').css('overflow-x', 'auto');
            return this;
        }
    });
});