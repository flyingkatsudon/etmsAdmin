define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var List = require('../grid/system-account.js');
    var Toolbar = require('../toolbar/system-account.js');
    var SystemGroup = require('text!/tpl/system-group.html');
    var BootstrapDialog = require('bootstrap-dialog');

    var InnerTemplate = require('text!/tpl/system-order.html');

    return Backbone.View.extend({
        render: function () {
            var _this = this;
            this.$el.html(SystemGroup);
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
                                _this.completeDialog(response.responseJSON)
                            }, success: function (response) {
                                console.log('success');
                                _this.completeDialog(response);
                            }
                        });
                    }
                });
            });
        }, completeDialog: function (msg) {
            BootstrapDialog.closeAll();

            var dialog = new BootstrapDialog({
                title: '',
                message: '<h5 style="margin-left:20%">' + msg + '</h5>',
                closable: true
            });

            dialog.realize();
            dialog.getModalDialog().css('margin-top', '20%');
            dialog.getModalHeader().hide();
            dialog.getModalFooter().hide();
            dialog.open();
        }
    });
});