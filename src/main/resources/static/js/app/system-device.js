define(function (require) {
    "use strict";

    var Backbone = require('backbone');
    var List = require('../grid/system-device.js');
    var Toolbar = require('../toolbar/system-device.js');
    var Template = require('text!/tpl/system-device.html');
    var Duplicate = require('../duplicate.js');

    var BootstrapDialog = require('bootstrap-dialog');

    return Backbone.View.extend({
        render: function () {
            this.$el.html(Template);
            this.toolbar = new Toolbar({el: '.hm-ui-search', parent: this}).render();
            this.list = new List({el: '.hm-ui-grid', parent: this}).render();

            // 중복 처리 조회
            $('#duplicate').click(function () {
                var dialog = new BootstrapDialog({
                    title: '<h4>2개 이상의 단말기에서 처리된 수험생입니다. 출결 여부를 확인하세요</h4>',
                    onshown: function(dialog){
                        dialog.list = new Duplicate({el: dialog.$modalBody}).render();
                    },
                    buttons: [
                        {
                            label: '닫기',
                            action: function (dialog) {
                                dialog.close();
                            }
                        }
                    ]
                });

                dialog.realize();
                dialog.getModalDialog().css('margin-top', '20%');
                dialog.getModalDialog().css('width', '60%');
                dialog.open();

            });

        }, search: function (o) {
            console.log(o);
            this.list.search(o);
        }
    });
});