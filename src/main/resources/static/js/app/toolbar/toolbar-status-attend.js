/**
 *
 */
define(function (require) {
    "use strict";

    var Backbone = require('backbone');
    var Template = require('text!tpl/toolbar.status-attend.html');

    return Backbone.View.extend({
        initialize: function (o) {
            this.el = o.el;
            this.parent = o.parent;
        },
        render: function () {
            this.$el.html(Template);

           // var hallCd = this.$('#hallCd').html(this.getOptions((this.list).options));

            return this;
        },
/*
        getOptions: function(options){
            // 확인 차 수정한 부분
            console.log("In getOptions");
            console.log("Object.keys(options): " + Object.keys(options));
            console.log("options.defaults.colModel[0].name: " + options.defaults.colModel[0].name);

            options.defaults.colModel[0].name
            if (options) {
                var html = '<option value="">전체</option>';
                for(var i = 0; i < options.defaults.colModel.length; i++){
                    var obj = options.defaults.colModel[i];
                    html += '<option value="' + obj.name + '">' + obj.label + '</option>';
                }
            }
            return html;
        },*/
        events: {
            'click #search': 'searchClicked'
        },
        searchClicked: function (e) {
            var _this = this;
            if(this.parent){
                parent.search({
                    admissionNm: _this.$('#admissionNm').val(),
                    admissionType : _this.$('#admissionType').val()
                });
            }
        }
    });
});