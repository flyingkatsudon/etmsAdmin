/**
 *
 */
define(function (require) {
    "use strict";

    require('jqueryui');

    var Backbone = require('backbone');
    var _ = require('underscore');

    var tabTemplate = _.template(require('text!tpl/tab.head.html'));
    var contentTemplate = _.template(require('text!tpl/tab.content.html'));


    var view = Backbone.View.extend({
        tabCount: 0,
        render: function () {
            this.$el.tabs({
                activate: function (evt, ui) {
                    $(window).trigger('resize');
                }
            });
            return this;
        },
        addTab: function (cd, href, label, content) {
            var findId = this.findTab(cd);
            if (findId !== undefined) {
                this.selectTab(findId);
                return false;
            }

            if (content.search('login-box') != -1) {
                $('<div title="세션종료">로그인 정보가 없습니다.<br/>다시 로그인 해주세요.</div>').dialog({
                    modal: true,
                    close: function () {
                        $(this).dialog('destroy').remove();
                        location.reload(true);
                    },
                    buttons: {
                        '확인': function () {
                            $(this).dialog('close');
                        }
                    }
                });
                return false;
            }

            this.tabCount++;
            this.$el.find(".ui-tabs-nav").append(tabTemplate({tabCount: this.tabCount, menuCd: cd, label: label}));
            this.$el.find('#tab-panels').append(contentTemplate({tabCount: this.tabCount, content: content}));

            this.$el.tabs('refresh');
            this.selectTab(this.findTab(cd));
        },
        findTab: function (cd) {
            var rtn;
            this.$el.find('.ui-tabs-nav li a').each(function (i) {
                if ($(this).data('menu_cd') == cd) {
                    rtn = i;
                    return false;
                }
            });
            return rtn;
        },
        selectTab: function (i) {
            this.$el.tabs('option', 'active', i);
        },
        events: {
            'click span.ui-icon-close': 'closeTab'
        },
        closeTab: function (e) {
            var selectedTab = $(e.target).closest('li');
            var panelId = selectedTab.remove().attr('aria-controls');
            this.$el.find('#tab-panels').find('#' + panelId).remove();
            this.$el.tabs("refresh");
        }
    });

    return view;
});