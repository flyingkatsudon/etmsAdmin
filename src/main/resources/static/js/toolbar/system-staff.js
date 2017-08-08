/**
 *
 */
define(function (require) {
    "use strict";

    var Toolbar = require('../dist/toolbar.js');
    var ToolbarModel = require('../model/model-status-toolbar.js');

    return Toolbar.extend({
        initialize: function (o) {
            this.el = o.el;
            this.parent = o.parent;
        },
        render: function () {
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm()));
            return this;
        },
        events: {
            'click #search': 'searchClicked'
        },
        searchClicked: function (e) {
            e.preventDefault();

            var _this = this;
            if (this.parent) {
                this.parent.search({
                    staffNm: _this.$('#staffNm').val(),
                    bldgNm: _this.$('#bldgNm').val(),
                    phoneNo: _this.$('#phoneNo').val()
                });
            }
        }
    });
});