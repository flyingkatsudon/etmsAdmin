define(function (require) {
    "use strict";

    require('jquery.ajaxForm');

    var Backbone = require('backbone');
    var Template = require('text!tpl/setting-data.html');
    var BootstrapDialog = require('bootstrap-dialog');

    var SettingAttend = require('./../grid/setting-data.js');
    var DataToolbar = require('../toolbar/setting-data.js');

    var ResponseDialog = require('../responseDialog.js');
    var responseDialog = new ResponseDialog();
    var attendList = [], admissionList = [];

    return Backbone.View.extend({
        initialize: function (o) {
            this.el = o.el;
            this.parent = o.parent;
        }, render: function () {
            var _this = this;

            this.$el.html(Template);
            this.initForm('#frmUploadWaitHall');
            this.initForm('#frmUploadHall');
            this.initForm('#frmUploadExaminee');
            this.initForm('#frmUploadPhoto');

            // 시험 정보 관리 메뉴
            this.toolbar = new DataToolbar({el: '.hm-ui-search', parent: this}).render();
            this.list = new SettingAttend({el: '#attendInfo', parent: this}).render();

            $('#reset').click(function () {
                _this.resetClicked();
            });

            $('#init').click(function () {
                responseDialog.dialogFormat('초기화하면 복구할 수 없습니다. 그래도 삭제 하시겠습니까?', '초기화', 'system/init');
            });

            $('#delAwhAll').click(function(){
                // TODO: 추후 재사용
                //responseDialog.dialogFormat('삭제하면 복구할 수 없습니다. 그래도 삭제 하시겠습니까?', '삭제', 'student/delAwh');
                // 시연용
                responseDialog.notify({msg: '준비중입니다'});
            });

            $('#delOrderAll').click(function(){
                responseDialog.dialogFormat('삭제하면 복구할 수 없습니다. 그래도 삭제 하시겠습니까?', '삭제', 'student/delOrder');
            });

        }, search: function (o) {
            this.list.search(o);
        }, initForm: function (id) {
            this.$(id).ajaxForm({
                beforeSubmit: function (arr) {
                    for (var i in arr) {
                        if (arr[i].name == 'file' && arr[i].value == '') {
                            responseDialog.notify({msg: '파일을 선택하세요'});
                            return false;
                        }
                        responseDialog.progress('업로드');
                    }
                },
                error: function (response) {
                    responseDialog.notify({msg: response.responseJSON});
                },
                success: function (response) {
                    $('#search').trigger('click');
                    responseDialog.move(response);
                }
            });
        },
        resetClicked: function (e) {
            var text = '<select id="admissionCd"><option value="">전체</option>';

            var _this = this;
            var dialog = new BootstrapDialog({
                message: '<h5 style="margin-left:10%">삭제할 전형을 선택하세요&nbsp;&nbsp;&nbsp;&nbsp;' + this.getAdmissionList(text) + '</h5>',
                closable: true,
                buttons: [
                    {
                        label: '계속',
                        cssClass: 'btn-primary',
                        action: function () {

                            var admissionCd = $('#admissionCd').val();
                            var admissionNm = _this.getAdmissionNm(admissionCd);

                            // 전형을 선택하였으면 시험을 선택하는 창이 나옴
                            if (admissionCd != '') {
                                _this.drawDialog(admissionCd);
                            }
                            // 전형을 선택하지 않으면 삭제 여부를 묻는 창이 나옴
                            else {
                                var innerDialog = new BootstrapDialog({
                                    title: '<h4>' + admissionNm + '&nbsp;삭제</h4>',
                                    message: '<h5 style="text-align: center">삭제하면 복구할 수 없습니다. 그래도 삭제 하시겠습니까?</h5>',
                                    buttons: [
                                        /*{
                                         label: '사진포함',
                                         cssClass: 'btn-primary',
                                         action: function () {
                                         BootstrapDialog.closeAll();
                                         BootstrapDialog.show({
                                         title: '서버 데이터 관리',
                                         message: '진행 중입니다. 잠시만 기다려주세요.',
                                         closable: false
                                         });
                                         _this.reset(true);
                                         }
                                         },*/
                                        {
                                            label: '사진 미포함',
                                            action: function () {

                                                console.log(admissionCd);
                                                if (admissionCd == undefined) admissionCd = '';
                                                _this.reset(admissionCd, null, false);
                                            }
                                        },
                                        {
                                            label: '닫기',
                                            action: function (innerDialog) {
                                                innerDialog.close();
                                            }
                                        }
                                    ]
                                });

                                innerDialog.realize();
                                innerDialog.getModalDialog().css('margin-top', '19%');
                                innerDialog.open();
                            }
                        }
                    },
                    {
                        label: '닫기',
                        action: function (dialog) {
                            dialog.close();
                        }
                    }
                ]
            });

            dialog.realize();
            dialog.getModalDialog().css('margin-top', '20%');
            dialog.getModalHeader().hide();
            dialog.open();

            /*

             var _this = this;
             var dialog = new BootstrapDialog({
             message: '<h5 style="margin-left:10%">삭제하면 복구할 수 없습니다. 그래도 삭제 하시겠습니까?</h5>',
             buttons: [
             /!*{
             label: '사진포함',
             cssClass: 'btn-primary',
             action: function () {
             BootstrapDialog.closeAll();
             BootstrapDialog.show({
             title: '서버 데이터 관리',
             message: '진행 중입니다. 잠시만 기다려주세요.',
             closable: false
             });
             _this.reset(true);
             }
             },*!/
             {
             label: '사진 미포함',
             action: function () {
             _this.reset(false);
             }
             },
             {
             label: '닫기',
             cssClass: 'btn-normal',
             action: function (dialog) {
             dialog.close();
             }
             }
             ]
             });

             dialog.realize();
             dialog.getModalDialog().css('margin-top', '20%');
             dialog.getModalHeader().hide();
             dialog.getModalFooter().css('padding', '1%');
             dialog.open();
             */
        },
        reset: function (admissionCd, attendCd, o) {
            // 전형단위 삭제
            $.ajax({
                beforeSend: function () {
                    responseDialog.progress('삭제');
                },
                url: 'system/reset',
                data: {
                    admissionCd: admissionCd,
                    attendCd: attendCd,
                    photo: o
                },
                success: function (response) {
                    $('#search').trigger('click');
                    responseDialog.move(response);
                },
                error: function (response) {
                    responseDialog.notify({msg: response.responseJSON});
                }
            });
            // 시험단위 삭제
            /*$.ajax({
             beforeSend: function () {
             responseDialog.progress('삭제');
             },
             url: 'system/reset?attendCd=' + attendCd,
             data: {
             photo: o
             },
             success: function (response) {
             $('#search').trigger('click');
             responseDialog.move(response);
             },
             error: function (response) {
             responseDialog.notify({msg: response.responseJSON});
             }
             });*/
        },
        drawDialog: function (admissionCd) {
            var _this = this;

            var text = '<select id="attendCd"><option value="">전체</option>';
            var dialog = new BootstrapDialog({
                message: '<h5 style="margin-left:10%">삭제할 시험을 선택하세요&nbsp;&nbsp;&nbsp;&nbsp;' + this.getAttendList(admissionCd, text) + '</h5>',
                closable: true,
                buttons: [
                    {
                        label: '계속',
                        cssClass: 'btn-primary',
                        action: function () {

                            var attendCd = $('#attendCd').val();
                            var attendNm = _this.getAttendNm(attendCd);

                            var innerDialog = new BootstrapDialog({
                                title: '<h4>' + _this.getAdmissionNm(admissionCd) + '&nbsp;/&nbsp;' + attendNm + '&nbsp;삭제</h4>',
                                message: '<h5 style="text-align: center">삭제하면 복구할 수 없습니다. 그래도 삭제 하시겠습니까?</h5>',
                                buttons: [
                                    /*{
                                     label: '사진포함',
                                     cssClass: 'btn-primary',
                                     action: function () {
                                     BootstrapDialog.closeAll();
                                     BootstrapDialog.show({
                                     title: '서버 데이터 관리',
                                     message: '진행 중입니다. 잠시만 기다려주세요.',
                                     closable: false
                                     });
                                     _this.reset(true);
                                     }
                                     },*/
                                    {
                                        label: '사진 미포함',
                                        action: function () {

                                            if (admissionCd == undefined) admissionCd = '';
                                            if (attendCd == undefined) attendCd = '';

                                            if (attendCd == '') _this.reset(admissionCd, null, false);
                                            else _this.reset(null, attendCd, false);
                                        }
                                    },
                                    {
                                        label: '닫기',
                                        action: function (innerDialog) {
                                            innerDialog.close();
                                        }
                                    }
                                ]
                            });

                            innerDialog.realize();
                            innerDialog.getModalDialog().css('margin-top', '19%');
                            innerDialog.open();

                        }
                    },
                    {
                        label: '닫기',
                        action: function (dialog) {
                            dialog.close();
                        }
                    }
                ]
            });

            dialog.realize();
            dialog.getModalDialog().css('margin-top', '20%');
            dialog.getModalHeader().hide();
            dialog.open();

        },
        getAdmissionList: function (text) {
            $.ajax({
                url: 'system/attendInfo',
                async: false,
                success: function (response) {

                    var tmp = [];

                    for (var i = 0; i < response.length; i++) {
                        var flag = true;

                        for (var j = 0; j < i; j++) {
                            if (response[i].admissionCd == response[j].admissionCd) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) tmp.push({
                            admissionCd: response[i].admissionCd,
                            admissionNm: response[i].admissionNm
                        });
                    }

                    for (var i = 0; i < tmp.length; i++) {
                        text += '<option value="' + tmp[i].admissionCd + '">' + tmp[i].admissionNm + '</option>';
                        admissionList.push({admissionCd: tmp[i].admissionCd, admissionNm: tmp[i].admissionNm});
                    }
                    text += '</select>';
                }
            });
            return text;
        },
        getAdmissionNm: function (admissionCd) {

            var admissionNm = '전형 전체';

            for (var i = 0; i < admissionList.length; i++) {
                if (admissionList[i].admissionCd == admissionCd)
                    admissionNm = admissionList[i].admissionNm;
            }

            return admissionNm;

        },
        getAttendList: function (admissionCd, text) {
            $.ajax({
                url: 'system/attendInfo',
                async: false,
                success: function (response) {
                    for (var i = 0; i < response.length; i++) {
                        if (response[i].admissionCd == admissionCd) {
                            text += '<option value="' + response[i].attendCd + '">' + response[i].attendNm + '&nbsp;(&nbsp;' + response[i].attendDate + '&nbsp;/&nbsp;' + response[i].attendTime + '&nbsp;)</option>';
                            attendList.push({attendCd: response[i].attendCd, attendNm: response[i].attendNm});
                        }
                    }
                    text += '</select>';
                }
            });
            return text;
        },
        getAttendNm: function (attendCd) {

            var attendNm = '시험 전체';

            for (var i = 0; i < attendList.length; i++) {
                if (attendList[i].attendCd == attendCd)
                    attendNm = attendList[i].attendNm;
            }

            return attendNm;

        }
    });
});