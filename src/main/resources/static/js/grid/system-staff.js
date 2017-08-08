define(function (require) {
    "use strict";

    var GridBase = require('../dist/jqgrid.js');
    var BootstrapDialog = require('bootstrap-dialog');

    return GridBase.extend({
        initialize: function (options) {
            this.parent = options.parents;

            var colModel = [
                {name: 'staffNm', label: '성명'},
                {name: 'phoneNo', label: '전화번호'},
                {name: 'bldgNm', label: '고사건물'}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
            }

            var opt = $.extend(true, {
                defaults: {
                    url: 'system/staff',
                    colModel: colModel,
                    onSelectRow: function (rowid, status, e) {
                        var rowdata = $(this).jqGrid('getRowData', rowid);
                        //var url1 = 'image/signature/' + rowdata.attendCd + '_' + rowdata.hallCd + '_1_all_sign.jpg';
                        //var url2 = 'image/signature/' + rowdata.attendCd + '_' + rowdata.hallCd + '_2_all_sign.jpg';
                        var url = 'image/signature/' + rowdata.attendCd + '_' + rowdata.hallCd + '_all_sign.jpg';

                        if (rowdata.isSignature && rowdata.isSignature == 'false') return false;

                        var dialog = new BootstrapDialog({
                            title: '<h3>' + rowdata.bldgNm + ' ' + rowdata.hallNm + '</h3>',
                            //message: '<image src="' + url1 + '"><image src="' + url2 + '">',
                            message: '<div style="text-align:center"><image src="' + url + '" style="width:60%"></div>',
                            size: 'size-wide',
                            closable: true,
                            buttons: [{
                                label: '닫기',
                                action: function (dialog) {
                                    dialog.close();
                                }
                            }]
                        });

                        dialog.realize();
                        dialog.open();
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