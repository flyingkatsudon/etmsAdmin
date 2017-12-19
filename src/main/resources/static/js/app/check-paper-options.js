define(function (require) {
    "use strict";
    var Backbone = require('backbone');

    var List = require('../grid/check-paper-options.js');
    var Toolbar = require('../toolbar/check-paper-options.js');
    var Template = require('text!/tpl/check-paper-options.html');
    var Multiple = require('../grid/check-paper-multiple.js');

    var ResponseDialog = require('../responseDialog.js');
    var responseDialog = new ResponseDialog();

    return Backbone.View.extend({
        render: function () {
            var _this = this;
            this.$el.html(Template);

            this.toolbar = new Toolbar({el: '.hm-ui-search', parent: this}).render();
            this.multiple = new Multiple({el: '#multiple', parent: this}).render();

            $('#checkDigit').click(function () {

                var url = _this.getUrl('digit');

                $.ajax({
                    url: url,
                    success: function (response) {

                        $('#cntInvalidDigit').html(response.content.length);

                        if (response.length == 0) {
                            responseDialog.notify({msg: '검증이 완료되었습니다. 답안지 자릿수가 모두 일치합니다'});
                        }
                        else {
                            $('#cntInvalidDigit').html(response.content.length + '&nbsp;건');
                            responseDialog.notify({msg: '검증이 완료되었습니다. ' + response.content.length + ' 건의 답안지를 확인하세요'});
                            _this.list = new List({el: '#detectResult', parent: this, url: url}).render();
                            $('#detectResult').fadeIn(500);
                        }
                    }

                })
            });

            $('#checkRange').click(function () {

                var url = _this.getUrl('range');

                $.ajax({
                    url: url,
                    success: function (response) {

                        $('#cntInvalidRange').html(response.content.length);

                        if (response.length == 0) {
                            $('#cntInvalidRange').html(response.content.length + '&nbsp;건');
                            responseDialog.notify({msg: '검증이 완료되었습니다. 답안지 범위가 모두 일치합니다'});
                        }
                        else {
                            $('#cntInvalidRange').html(response.content.length + '&nbsp;건');
                            responseDialog.notify({msg: '검증이 완료되었습니다. ' + response.content.length + ' 건의 답안지를 확인하세요'});
                            _this.list = new List({el: '#detectResult', parent: this, url: url}).render();
                            $('#detectResult').fadeIn(500);
                        }
                    }

                })
            });

            $('#checkChar').click(function () {

                var url = _this.getUrl('char');

                $.ajax({
                    url: url,
                    success: function (response) {

                        $('#cntInvalidChar').html(response.content.length);

                        if (response.length == 0) {
                            responseDialog.notify({msg: '검증이 완료되었습니다. 특수 문자가 존재하지 않습니다'});
                        }
                        else {
                            $('#cntInvalidChar').html(response.content.length + '&nbsp;건');
                            responseDialog.notify({msg: '검증이 완료되었습니다. ' + response.content.length + ' 건의 답안지를 확인하세요'});
                            _this.list = new List({el: '#detectResult', parent: this, url: url}).render();
                            $('#detectResult').fadeIn(500);
                        }
                    }

                })
            });
        },
        getUrl: function (way) {

            var param = {
                admissionNm: this.$('#admissionNm').val(),
                typeNm: this.$('#typeNm').val(),
                attendDate: this.$('#attendDate').val(),
                attendTime: this.$('#attendTime').val()
            };

            return 'check/invalid?admissionNm=' + param.admissionNm + '&typeNm=' + param.typeNm + '&attendDate=' + param.attendDate + '&attendTime=' + param.attendTime + '&way=' + way;

        },
        search: function(o){
            this.multiple.search(o);
            this.multiple.$grid.jqGrid('setGridParam', {url: 'check/multiple', datatype: 'json'}).trigger('reloadGrid');
            $('#multiple').fadeIn(500);
        }
    });
});