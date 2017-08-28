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

            this.$('#headNm').html(this.getOptions(ToolbarModel.getHeadNm()));
            this.$('#bldgNm').html(this.getOptions(ToolbarModel.getBldgNm()));

            return this;
        },
        events: {
            'click #search': 'searchClicked',
            'change #headNm': 'headNmChanged',
            'change #bldgNm': 'bldgNmChanged'
        },
        searchClicked: function (e) {
            e.preventDefault();

            var _this = this;
            if (this.parent) {
                this.parent.search({
                    headNm: _this.$('#headNm').val(),
                    bldgNm: _this.$('#bldgNm').val(),
                    hallNm: _this.$('#hallNm').val()
                });
            }
        },
        // override
        getOptions: function (options) {
            var html = '<option value="">직접입력</option>';

            if (options) {
                if (options.constructor === Array) {
                    for (var i = 0; i < options.length; i++) {
                        var obj = options[i];
                        html += '<option value="' + obj.key + '">' + obj.value + '</option>';
                    }
                } else {
                    var keys = Object.keys(options);
                    for (var i = 0; i < keys.length; i++) {
                        var key = keys[i], value = options[key];
                        html += '<option value="' + key + '">' + value + '</option>';
                    }
                }
            }
            return html;
        }
    });
});