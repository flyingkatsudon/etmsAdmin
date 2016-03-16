/**
 *
 */
define(function (require) {
    "use strict";

    require('jqgrid');

    var uuid = require('uuid');
    var Backbone = require('backbone');

    $.extend($.jgrid, {
        defaults: {
            cmTemplate: {
                align: 'center'
            },
            caption: '',
            colModel: [],
            forceFit: true,
            sortable: true,
            autoencode: true,
            ignoreCase: true,
            gridview: true,
            hidegrid: false,
            jsonReader: {repeatitems: false},
            multiSort: true,
            rownumbers: true,
            rowNum: 30,
            rownumWidth: 50,
            rowList: [10, 20, 30, 2000],
            datatype: 'local',
            gridComplete: function () {
                $(window).trigger('resize');
            },
            prmNames: {search: null, nd: null},
            loadError: function (jqXHR) {
                if (jqXHR.status == '401') {
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
                } else {
                    alert('예외가 발생했습니다. 관리자에게 문의하세요.');
                }
            }
        },
        add: {},
        edit: {},
        del: {},
        nav: {
            search: false,
            view: false,
            add: false,
            edit: false,
            del: false,
            refresh: true,
            position: 'right'
        }
    });

    return Backbone.View.extend({
        initialize: function (options) {
            var _this = this;
            $(window).unbind('resizeEnd.' + this.cid).bind('resizeEnd.' + this.cid, function () {
                _this.resize();
            });

            this.options = $.extend(true, {}, $.jgrid, options);
            this.$grid = $(document.createElement('table')).attr('id', uuid.uuid());
            this.$pager = $(document.createElement('div')).attr('id', uuid.uuid());
        },
        resize: function () {
            var $P = this.$el;
            $P.find('.ui-jqgrid').each(function () {

                var t = $(this).filter(':visible').find('.ui-jqgrid-titlebar');
                var tHeight = t.css('display') == 'none' ? 2 : (t.outerHeight() + 2);
                var hHeight = $(this).filter(':visible').find('.ui-jqgrid-hdiv').outerHeight();
                var p = $(this).filter(':visible').find('.ui-jqgrid-pager');
                var pHeight = p.css('display') == 'none' ? 2 : p.outerHeight();

                var $grid = $(this).filter(':visible').find('.ui-jqgrid-btable');
                $grid.jqGrid('setGridHeight', $P.outerHeight() - (tHeight + hHeight + pHeight));
                $grid.jqGrid('setGridWidth', $P.outerWidth() - 2, true);
                var groupHeaders = $grid.jqGrid('getGridParam', 'groupHeader');
                if (groupHeaders !== null) {
                    $grid.jqGrid('destroyGroupHeader').jqGrid('setGroupHeaders', groupHeaders);
                }
            });
        },
        render: function () {
            this.$el.empty().append(this.$grid, this.$pager);
            this.options.defaults.pager = this.$pager;
            var _this = this;
            this.$grid.jqGrid(_this.options.defaults);
            this.$grid.jqGrid('navGrid', _this.$grid.getGridParam('pager'), _this.options.nav, _this.options.edit, _this.options.add, _this.options.del, _this.options.search, _this.options.view);
            return this;
        }
    });
});