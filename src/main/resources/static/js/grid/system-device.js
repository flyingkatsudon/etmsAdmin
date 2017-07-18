define(function (require) {
    "use strict";

    var GridBase = require('../dist/jqgrid.js');

    return GridBase.extend({
        initialize: function (options) {
            this.parent = options.parents;

            var colModel = [
                {name: 'deviceId', label: '단말기코드'},
                {name: 'deviceNo', label: '단말기번호'},
                {name: 'packageName', label: '서비스명'},
                {name: 'versionName', label: '서비스버전'},
                {name: 'checkCnt', label: '처리인원(단위: 명)'},
                {name: 'phoneNo', label: '전화번호'},
                {name: 'uuid', label: 'uuid'},
                {name: 'lastDttm', label: '갱신일자'}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: 'system/device',
                    colModel: colModel,
                    loadComplete: function (data) {
                        // 실제로 사용할 일이 없어 삭제함
                        // var ids = $(this).getDataIDs(data);
                        //
                        // for (var i = 0; i < ids.length; i++) {
                        //     var rowData = $(this).getRowData(ids[i]);
                        //
                        //     if (rowData.packageName.upperCase.indexOf('MGR')) {
                        //         // 중간본부 앱
                        //         $(this).setRowData(ids[i], false, {background: "#FFD8D8"});
                        //     }
                        //     // else {
                        //     //     // 출석 앱
                        //     //     $(this).setRowData(ids[i], false, {background: "#FFD8D8"});
                        //     // }
                        // }
                    },
                    loadError: function (err) {
                        debug.log(err);
                        alert("연결오류! 관리자에게 문의해 주세요.");
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