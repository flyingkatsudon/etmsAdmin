define(function (require) {
    "use strict";

    var GridBase = require('../dist/jqgrid.js');

    return GridBase.extend({
        initialize: function (options) {
            this.parent = options.parents;

            var colModel = [
                {name: 'deviceId', label: '디바이스 아이디'},
                {name: 'deviceNo', label: '디바이스 설정번호'},
                {name: 'lastDttm', label: '갱신일자'},
                {name: 'packageName', label: '서비스명'},
                {name: 'phoneNo', hidden: true},
                {name: 'uuid', hidden: true},
                {name: 'versionName', label: '서비스버전'}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: 'system/device',
                    colModel: colModel,
                    loadComplete: function (data) {
                        // var ids = $(this).getDataIDs(data);

                        // for (var i = 0; i < ids.length; i++) {
                        //     var rowData = $(this).getRowData(ids[i]);
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