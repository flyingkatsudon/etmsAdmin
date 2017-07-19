define(function (require) {
    "use strict";

    var GridBase = require('./dist/jqgrid.js');

    return GridBase.extend({
        initialize: function (options) {
            this.param = options.param;

            var colModel = [
                {name: 'admissionNm', label: '전형명'},
                {name: 'attendNm', label: '시험명'},
                {name: 'examineeCd', label: '수험번호'},
                {name: 'examineeNm', label: '수험생명'},
                {name: 'bldgNm', label: '고사건물'},
                {name: 'hallNm', label: '고사실'},
                {name: 'isAttend', label: '현재출결상태', formatter: 'select', editoptions: {value: {true: '응시', false: '결시'}}},
                {name: 'logDttm', label: '로그시간'},
                {name: 'attendCd', hidden: true}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: 'system/checkedExmaminee?deviceId=' + this.param.examineeCd,
                    colModel: colModel,
                    loadComplete: function (data) {
                        var ids = $(this).getDataIDs(data);

                        for (var i = 1; i < ids.length; i++) {
                            var rowData = $(this).getRowData(ids[i]);
                            if (rowData.currentState == 'false') $(this).setRowData(ids[i], false, {background: "#FFD8D8"});
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