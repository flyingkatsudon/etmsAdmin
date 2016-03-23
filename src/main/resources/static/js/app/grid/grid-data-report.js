/**
 *
 */
define(function(require) {
    "use strict";

    //require('layout');
    //require('jqdownload');

    var Backbone = require('backbone');
    //var MainTitle = require('app/main.title');
    //var DlgDownload = require('app/dialog/dlgDownload');

    var html = [ '<%_.each(list, function(data){%>',
        '    <dl>',
        '        <dt><%=data.titleNM%></dt>',
        '        <dd>※<%=data.titleNM%></dd>',
        '        <dd><a href="#nothing" class="bt04 ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" data-url="<%=data.url%>">다운로드</a></dd>',
        '    </dl>',
        '<%});%>'];

    var view = Backbone.View.extend({
        bodyTemplate : _.template(html.join('')),
        initialize : function() {
            this.initLayout().render();
        },
        initLayout : function() {
            this.layout = this.$el.layout({
                north__size : 120,
                north__childOptions : {}
            });
            return this;
        },
        render : function() {
            var _this = this;
            var innerLayout = this.layout.children.north.layout1;

            this.maintitle = new MainTitle({
                el : innerLayout.north.pane,
                title : "산출물 다운로드",
                depth1 : "Data 검색&산출물",
                depth2 : "산출물 다운로드",
                disc : "산출물 다운로드를 하실 수 있습니다."
            }).render();

            _this.body1Div = $(document.createElement('div'));
            _this.body1Div.addClass('down-list');
            $(this.layout.center.pane).append(_this.body1Div);

            _this.body2Div = $(document.createElement('div'));
            _this.body2Div.addClass('down-list');
            $(this.layout.center.pane).append(_this.body2Div);

            this.renderReportData();
            this.renderTotalBtnData();

            // $(window).trigger('resize');
        },
        renderReportData : function() {
            this.body1Div.append(this.bodyTemplate({
                list : [
                    { titleNM : "계열별 통계표", url : "adm/statistic/allInfo/excel" },
                    { titleNM : "고사실별 통계표", url : "adm/statistic/hallInfo/excel" },
                    { titleNM : "모집단위별 통계표", url : "adm/statistic/unitInfo/excel" },
                    { titleNM : "수험생별리스트", url : "adm/data/examineeList/excel" }
                ]
            }));

            this.body2Div.append(this.bodyTemplate({
                list : [
                    { titleNM : "신분증미소지자리스트", url : "adm/data/rptIdentify" },
                    { titleNM : "신원재확인필요자리스트", url : "adm/data/rptValidate" },
                    { titleNM : "타고사실&예비고사장 리스트", url : "adm/data/rptOther" },
                    { titleNM : "감독관 서명", url : "adm/data/rptInspector" }
                ]
            }));

        },
        renderTotalBtnData : function() {
            var _this = this;
            $(this.layout.center.pane).append('<div class="clear-both"></div>');
            _this.totalDiv = $(document.createElement('div'));
            _this.totalDiv.addClass('bt-group01');
            _this.totalDiv.append('<span><a href="#nothing" class="bt01 ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only" id="btnExcel">전체 엑셀 다운로드</a></span>');
            //_this.totalDiv.append('<span><a href="#nothing" class="bt01-01" id="btnAllDelete">데이터 전체 삭제</a></span>');
            $(this.layout.center.pane).append(_this.totalDiv);
        },
        events : {
            'click .bt04' : 'btnReportData',
            'click .bt01' : 'btnTotalUploadData',
            'click .bt01-01' : 'btnTotalDeleteData'
        },
        btnReportData : function(e) {
            new DlgDownload({ url : e.target.dataset.url }).render();
        },
        btnTotalUploadData : function(e) {
            new DlgDownload({url : 'adm/data/downloadAll'}).render();
        },
        btnTotalDeleteData : function(e) {
            var _this = this;
            var dlg = $('<div title="확인">전체 데이터를 삭제하시겠습니까?</div>').dialog({
                modal : true
            });
            dlg.dialog("option", "buttons", [ {
                text : "확인",
                icons : {
                    primary : "ui-icon-heart"
                },
                click : function() {
                    $(this).dialog("close");
                }
            }, {
                text : "취소",
                icons : {
                    primary : "ui-icon-heart"
                },
                click : function() {
                    $(this).dialog("close");
                }
            } ]);
        }
    });

    return view;
});