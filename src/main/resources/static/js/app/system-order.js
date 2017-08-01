define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var List = require('../grid/system-account.js');
    var Toolbar = require('../toolbar/system-account.js');
    var Template = require('text!/tpl/system-order.html');
    var BootstrapDialog = require('bootstrap-dialog');

    var InnerTemplate = require('text!/tpl/system-order.html');

    var ResponseDialog = require('../responseDialog.js');
    var responseDialog = new ResponseDialog();

    return Backbone.View.extend({
        render: function () {
            this.$el.html(Template);
            /*this.toolbar = new Toolbar({el: '.hm-ui-search', parent: this}).render();
             this.list = new List({el: '.hm-ui-grid', parent: this}).render();

             $('#add').click(function(){
             new AddAcount().render();
             });
             */

            $('#interview').click(function () {
                console.log('clicked');
            });

            $('#debating').click(function () {
                console.log('clicked');
            });

            $('#ordering').click(function () {
                console.log('clicked');

                $.ajax({
                    url: 'system/ready',
                    success: function (response) {
                        console.log(response);
                        var dialog = new BootstrapDialog({
                            title: '<h4>순서를 부여하는 중입니다</h4>',
                            message: InnerTemplate,
                            closable: true
                        });

                        dialog.realize();
                        dialog.getModalDialog().css('margin-top', '20%');
                        dialog.open();

                        $.ajax({
                            url: 'system/order',
                            error: function (response) {
                                responseDialog.notify({msg: response.responseJSON});
                            }, success: function (response) {
                                responseDialog.notify({msg: response});
                            }
                        });
                    }
                });
            });
        }
    });
});