/**
 * Created by Jeremy on 2017. 12. 14..
 */
define(function (require) {
    "use strict";

    var GridBase = require('../dist/jqgrid.js');

    var ResponseDialog = require('../responseDialog.js');
    var responseDialog = new ResponseDialog();

    return GridBase.extend({
        initialize: function (options) {
            this.param = options.param;

            var colModel = [
                {name: 'examineeCd', label: '수험번호'},
                {name: 'examineeNm', label: '수험생명'},
                {name: 'deviceId', label: '단말기코드'},
                {name: 'deviceNo', label: '단말기번호'},
                {name: 'paperCd', label: '답안지번호'},
                {name: 'attendBldgNm', label: '고사건물'},
                {name: 'attendHallNm', label: '고사실'},
                {name: 'actionCnt', label: '처리횟수'},
                {
                    name: 'isAttend',
                    label: '현재출결상태',
                    formatter: 'select',
                    editoptions: {value: {true: '응시', false: '결시'}}
                },
                {name: 'logDttm', label: '최종처리시간'},
                {name: 'attendCd', hidden: true}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    colModel: colModel,
                    loadComplete: function (data) {
                        var ids = $(this).getDataIDs(data);

                        for (var i = 0; i < ids.length; i++) {
                            var rowData = $(this).getRowData(ids[i]);
                            if (rowData.isAttend == 'false') $(this).setRowData(ids[i], false, {background: "#FFD8D8"});
                        }
                    }
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