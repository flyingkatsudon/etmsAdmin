define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var List = require('../grid/status-attend.js');
    var Summary = require('../grid/status-summary.js');
    //var Chart1 = require('../chart/status-attend.js');
    var Toolbar = require('../toolbar/status-attend.js');
    var Template = require('text!/tpl/status-attend.html');
    var InnerTemplate = require('text!/tpl/status-summary.html');

    return Backbone.View.extend({
        render: function () {
            /*
                각 페이지는 template에 toolbar, grid로 구성되어있다 (응시율 통계 메뉴는 summary 포함)
                sidebar.html의 <li> 태그에 있는 #... 에서 ...와 파일명을 일치시켜 파일 생성
             */
            this.$el.html(Template);
            this.$('#hm-ui-summary').html(InnerTemplate);
            this.toolbar = new Toolbar({el: '.hm-ui-search', parent: this}).render();
            /*this.chart1 = new Chart1({el: '#hm-ui-chart'}).render();*/
            this.summary = new Summary({el: '#hm-ui-summary', parent: this, url: 'status/all'});
            this.summary.render();
            this.list = new List({el: '.hm-ui-grid', parent: this}).render();
        }, search: function (o) {
            this.list.search(o);
            this.summary.render(o);
        }
        /*, renderChart: function (o) {
            this.chart1.search(o);
        }*/
    });
});