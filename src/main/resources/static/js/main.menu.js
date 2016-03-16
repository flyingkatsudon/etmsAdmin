/**
 * menu
 */
define(function (require) {
    "use strict";

    require('jqueryui');

    var Backbone = require('backbone');
    var _ = require('underscore');
    var $ = require('jquery');

    var title = _.template(require('text!tpl/menu.title.html'));
    var menu = _.template(require('text!tpl/menu.sub.html'));

    var content = _.template(require('text!tpl/layout.content.html'));

    return Backbone.View.extend({
        initialize: function (options) {
            this.tab = options.tab;
            var _this = this;
            $.get('menu/list', function (result) {
                _this.menuList = result;
                _this.render();
            });
        },
        render: function () {
            // 데이터가 순차적으로 내려오지 않았을 경우를 대비하여 대메뉴와 소메뉴 생성을 별도로 분리한다.
            this.renderTitle();
            this.renderMenu();
        },
        // 대메뉴 생성
        renderTitle: function () {
            var models = this.menuList;
            for (var i in models) {
                if (models[i].fkMenuCd === undefined) {
                    this.$el.append(title({menuNm: models[i].menuNm, menuCd: models[i].menuCd}));
                }
            }
        },
        // 소메뉴 생성
        renderMenu: function () {
            var models = this.menuList;
            for (var i in models) {
                if (models[i].fkMenuCd !== undefined) {
                    var title = this.$('.ui-menu-title[data-menu_cd=' + models[i].fkMenuCd + ']');
                    var last = title.nextUntil('.ui-menu-title').last();

                    var position;
                    if (last.length > 0) position = last; else position = title;
                    position.after(menu({
                        menuPath: models[i].menuPath,
                        menuCd: models[i].menuCd,
                        menuNm: models[i].menuNm,
                        iconImg: models[i].iconImg
                    }));
                }
            }
        },
        events: {
            'click .ui-menu-button': 'menuClicked'
        },
        menuClicked: function (e) {
            var _this = this;

            var $target = e.target.tagName != 'LI' ? $(e.target).parent() : $(e.target);
            var path = $target.data('path');
            var menuCd = $target.data('menu_cd');
            var menuNm = $target.text();

            var findedTab = _this.tab.findTab(menuCd);
            if (findedTab != undefined) {
                _this.tab.selectTab(findedTab);
                return false;
            }

            require(['js/app/' + path], function (View) {
                _this.tab.addTab(menuCd, path, menuNm, content({id: path}));
                new View({el: '#' + path}).render();
            }, function (e) {
                var dlg = $(document.createElement('div')).dialog({
                    title: '경고',
                    modal: true,
                    create: function () {
                        this.innerHTML = '화면생성실패!';
                    },
                    close: function () {
                        $(this).dialog('destroy').remove();
                    }
                });
                dlg.dialog("option", "buttons", [{
                    text: "Ok", click: function () {
                        $(this).dialog("close");
                    }
                }]);
            });
        }
    });
});