(function ($) {
    var u = 0;
    $.uid = function () {
        u++;
        return 'jQ-uid-' + u;
    };
    $.fn.uid = function () {
        if (!this.length) {
            return 0;
        }
        var f = this.first(), i = f.attr('id');
        if (!i) {
            i = $.uid();
            f.attr('id', i);
        }
        return i;
    };
})(jQuery);