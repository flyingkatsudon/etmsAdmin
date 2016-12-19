define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var Summary = require('../grid/status-summary.js');
    var Template = require('text!/tpl/home.html');

    var StatusTemplate = require('text!/tpl/home-status.html');
    var Toolbar = require('../toolbar/home-status.js');
    var AttendList = require('../grid/home-attend.js');
    var AbsentList = require('../grid/home-absent.js');

    return Backbone.View.extend({
        render: function () {
            this.$el.html(Template);
            this.summary = new Summary({el: '#hm-ui-summary', url:'status/all'}).render();
            this.$el.append(StatusTemplate);
            this.toolbar = new Toolbar({el: '.hm-ui-search', parent: this}).render();
            this.attendList = new AttendList({el: '.attend-grid'}).render();
            this.absentList = new AbsentList({el: '.absent-grid'}).render();
        }, search: function (o) {
            this.attendList.search(o);
            this.absentList.search(o);
        }
    });
});