/**
 *
 */
define(function (require) {
    "use strict";

    require('jqgrid');
    require('jquid');

    var Backbone = require('backbone');

    return Backbone.View.extend({
        initialize: function (options) {
            var _this = this;
            $(window).unbind('resizeEnd.' + this.cid).bind('resizeEnd.' + this.cid, function () {
                _this.resize();
            });

            this.options = $.extend(true, {}, $.jgrid, options);
            this.$grid = $(document.createElement('table')).attr('id', $.uid());
            this.$pager = $(document.createElement('div')).attr('id', $.uid());
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
            var _this = this;
            this.$grid.jqGrid(_this.options.defaults);
            return this;
        }
    });
});