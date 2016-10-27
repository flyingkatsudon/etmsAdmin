define(function (require) {
    "use strict";

    var GridBase = require('../dist/jqgrid.js');

    return GridBase.extend({
        initialize: function (options) {
            this.parent = options.parent;
            //var _this = this;
            var colModel = [
                {name: 'admissionNm', label: '전형'},
                {name: 'attendDate', label: '시험일자', hidden: true},
                {name: 'bldgNm', label: '고사건물'},
                {name: 'hallNm', label: '고사실'},
                {name: 'absentPer', label: '결시율', formatter: 'number', formatoptions: {suffix: '%'}}
            ];

            for (var i = 0; i < colModel.length; i++) {
                colModel[i].label = colModel[i].label === undefined ? colModel[i].name : colModel[i].label;
                colModel[i].sortable = false;
            }

            var opt = $.extend(true, {
                defaults: {
                    caption : '결시율 TOP 20',
                    url: 'status/home.json',
                    colModel: colModel,
                    sortname : 'absentPer',
                    sortorder : 'desc',
                    rowNum: 20,
                    rowList: ''
                },
            }, options);

            this.constructor.__super__.initialize.call(this, opt);
        },
        render: function () {
            // this.constructor.__super__.render.call(this);
            this.$el.empty().append(this.$grid);
            this.$grid.jqGrid(this.options.defaults);
            this.$('.ui-jqgrid .ui-jqgrid-bdiv').css('overflow-x', 'hidden');
            this.$grid.parents('div.ui-jqgrid-bdiv').css('min-height', '100px');
            this.$grid.jqGrid('setGridWidth', this.$el.width());

            if (this.options.defaults.url) this.$grid.jqGrid('setGridParam', {datatype: 'json'}).trigger('reloadGrid');

            return this;
        }
    });
});