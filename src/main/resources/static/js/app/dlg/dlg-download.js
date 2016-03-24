/**
 *
 */
define(function(require) {
    "use strict";

    require('jquery.file.download');

    var Dlg = require('./dlg-base.js');

    var view = Dlg.extend({
        initialize : function(opt) {
            var options = $.extend(opt, {
                title : '다운로드'
            });
            this.constructor.__super__.initialize.call(this, options);
        },
        render : function() {
            var _this = this;
            this.constructor.__super__.render.call(this);
            this.$el.css('overflow', 'hidden');
            var html = '';
            html += '<div class="progressbar">';
            html += '    <div class="progress-label">파일 생성중...</div>';
            html += '</div>';
            html += '<div class="progress-text">파일 생성시 많은 시간이 소요될 수 있습니다...</div>'
            this.$el.html(html);
            this.$el.find('.progress-label').css({
                'position' : 'absolute',
                'left' : '35%',
                'top' : '4px',
                'font-weight' : 'bold',
                'text-shadow' : '1px 1px 0 #fff'
            });

            this.$el.find('.progressbar').css({
                'position' : 'relative'
            }).progressbar({
                value : false
            });

            $.fileDownload(_this.options.url, {
                successCallback : function(url) {
                    _this.$el.dialog('close');
                },
                failCallback : function(responseHtml, url) {
                    _this.$el.html(responseHtml ? responseHtml : '다운로드에 실패하였습니다!');
                }
            });
        }
    });
    return view;
});