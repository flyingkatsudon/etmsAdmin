define(function (require) {
    "use strict";

    var Backbone = require('backbone');

    var Collection = Backbone.Collection.extend({
        url: 'model/toolbar',
        initialize: function () {
            this.fetch({
                async: false
                //data: $.param(param)
            });
        },
        openPrintWindow : function(param) {
            var collection = new Collection(param);
            console.log(collection);
            if (collection.length > 0) {
                var printWindow = window.open('../tpl/template.inspect-print.html', 'print', 'height=768,width=1024');

                printWindow.document.close(); // necessary for IE >= 10
                printWindow.focus(); // necessary for IE >= 10
            } else {
                alert('검색된 수험생이 없습니다!');
            }
        }
    });
    return new Collection();
});