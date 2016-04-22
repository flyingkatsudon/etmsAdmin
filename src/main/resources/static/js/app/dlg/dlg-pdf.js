define(function (require) {
    "use strict";

    var Dlg = require('./dlg-base.js');
    var view = Dlg.extend({
        initialize : function(options) {
            var options = $.extend({}, {
                width : $(window).width() / 2 * 1.5,
                height : $(window).height() / 2 * 1.5
            }, options);
            this.constructor.__super__.initialize.call(this, options);
        },
        render : function() {
            this.constructor.__super__.render.call(this);
            this.$el.css('overflow', 'hidden');
            this.$el.html('<object type="application/pdf" data="' + this.options.url + '#view=fitW" width="100%" height="100%"><p>브라우저가 PDF를 지원하지 않습니다. <a href="' + this.options.url + '">다운로드</a>를 눌러 직접 다운로드 하십시오.</p></object>');
        }
    });
    return view;
});