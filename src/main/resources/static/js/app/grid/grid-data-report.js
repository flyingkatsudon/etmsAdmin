/**
 * menu
 */
define(function(require){
    "use strict";

    require('jquery.file.download');

    var Backbone = require('backbone');
    var Template = require('text!tpl/template.data-report.html');
    var DlgDownload = require('../dlg/dlg-download.js');

    return Backbone.View.extend({
        render: function () {
            this.$el.html(Template);
            return this;
        },
        events: {
            'click #attend' : 'attendClicked',
            'click #major' : 'majorClicked',
            'click #dept' : 'deptClicked',
            'click #hall' : 'hallClicked',
            'click #group' : 'groupClicked',
            'click #statusExaminee' : 'statusExamineeClicked',

            'click #noIdCard' : 'noIdCardClicked',
            'click #recheck' : 'recheckClicked',
            'click #otherHall' : 'otherHallClicked',
            'click #signature' : 'signatureClicked',
            'click #dataExaminee' : 'dataExamineeClicked',

            'click #allDown': 'allDownClicked'
        },
        attendClicked : function(e){
            e.preventDefault();
            var url = $(e.target).data('url');
            var dlgDownload = new DlgDownload({
                url : url
            }).render();
            return false;
        },
        majorClicked: function(e){
            e.preventDefault();
            var url = $(e.target).data('url');
            var dlgDownload = new DlgDownload({
                url: url
            }).render();
            return false;
        },
        deptClicked: function(e){
            e.preventDefault();
            var url = $(e.target).data('url');
            var dlgDownload = new DlgDownload({
                url: url
            }).render();
            return false;
        },
        hallClicked: function(e){
            e.preventDefault();
            var url = $(e.target).data('url');
            var dlgDownload = new DlgDownload({
                url: url
            }).render();
            return false;
        },
        groupClicked: function(e){
            e.preventDefault();
            var url = $(e.target).data('url');
            var dlgDownload = new DlgDownload({
                url: url
            }).render();
            return false;
        },
        statusExamineeClicked: function(e){
            e.preventDefault();
            var url = $(e.target).data('url');
            var dlgDownload = new DlgDownload({
                url: url
            }).render();
            return false;
        },




        noIdCardClicked: function(e){
            e.preventDefault();
            var url = $(e.target).data('url');
            var dlgDownload = new DlgDownload({
                url: url
            }).render();
            return false;
        },
        recheckClicked: function(e){
            e.preventDefault();
            var url = $(e.target).data('url');
            var dlgDownload = new DlgDownload({
                url: url
            }).render();
            return false;
        },
        otherHallClicked: function(e){
            e.preventDefault();
            var url = $(e.target).data('url');
            var dlgDownload = new DlgDownload({
                url: url
            }).render();
            return false;
        },
        signatureClicked: function(e){
            e.preventDefault();
            var url = $(e.target).data('url');
            var dlgDownload = new DlgDownload({
                url: url
            }).render();
            return false;
        },
        dataExamineeClicked: function(e){
            e.preventDefault();
            var url = $(e.target).data('url');
            var dlgDownload = new DlgDownload({
                url: url
            }).render();
            return false;
        },


        allDownClicked: function(e){
            e.preventDefault();
            var url = $(e.target).data('url');
            var dlgDownload = new DlgDownload({
                url: url
            }).render();
            return false;
        },
    });
});