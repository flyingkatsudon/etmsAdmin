define(function (require) {
    "use strict";

    require('jqueryui');
    require('layout');
    require('layout.tab');

    var Menu = require('./main.menu');
    var Tabs = require('./main.tabs');

    $.extend(true, $.layout.defaults, {
        panes: {
            resizeable: false,
            closeable: false,
            spacing_open: 0,
            spacing_closed: 0
        }
    });

    window.layout = $('body').layout({
        north__paneSelector: '.header',
        center__paneSelector: '#tabs',
        west__paneSelector: '#left-menu',
        west__size: 250,
        center__childOptions: {
            north__paneSelector: '#tab-button',
            center__paneSelector: '#tab-panels',
            center__onresize: $.layout.callbacks.resizeTabLayout
        }
    });

    var tabs = new Tabs({el: '#tabs'}).render();
    var menu = new Menu({el: '#left-menu', tab: tabs}).render();

    $(window).bind('resize', function () {
        if (this.resizeTo) clearTimeout(this.resizeTo);
        this.resizeTo = setTimeout(function () {
            $(this).trigger('resizeEnd');
        }, 100);
    });

    $('body .header .user-info input[type="submit"]').button();

    $(window).on('resizeEnd', function () {
        layout.resizeAll();
    });

    $(window).trigger('resize');
});