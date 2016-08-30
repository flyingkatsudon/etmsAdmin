define(function (require) {
    "use strict";
    var Backbone = require('backbone');


    return Backbone.View.extend({
        initialize: function (o) {
            this.el = o.el;
        },
        render: function () {
            var _this = this;
            $.ajax({
                url: 'status/all',
                success: function (response) {
                    _this.$('#examineeCnt').html(response.examineeCnt+"명");
                    _this.$('#attendCnt').html(response.attendCnt+"명");
                    _this.$('#absentCnt').html(response.absentCnt+"명");
                    _this.$('#attendPer').html(response.attendPer+"%");
                    _this.$('#absentPer').html(response.absentPer+"%");
                }
            });
        }
    });
});