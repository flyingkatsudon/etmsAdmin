package com.humane.admin.etms.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("menu")
public class MenuController {

    @RequestMapping(value = "list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<MenuBean> menuList() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<MenuBean> list = new ArrayList<>();

        list.add(new MenuBean("응시율 통계", "M01"));
        //list.add(new MenuBean("전형 별", "M01S01", "M01", "status-attend", "1_1"));
        list.add(new MenuBean("계열 별", "M01S01", "M01", "status-attend", "1_1"));
        list.add(new MenuBean("모집단위 별", "M01S02", "M01", "status-dept", "1_2"));
        list.add(new MenuBean("고사실 별", "M01S03", "M01", "status-hall", "1_3"));
        list.add(new MenuBean("조 별", "M01S04", "M01", "status-group", "1_4"));//ss
        list.add(new MenuBean("수험생 별", "M01S05", "M01", "status-examinee", "1_5"));//ss

        list.add(new MenuBean("데이터 리스트", "M02"));
        list.add(new MenuBean("요약", "M02S01", "M02", "data-summary", "2_1"));
        list.add(new MenuBean("신분증 미소지자", "M02S02", "M02", "data-noIdCard", "2_2"));
        list.add(new MenuBean("재확인 대상자", "M02S03", "M02", "data-recheck", "2_3"));
        list.add(new MenuBean("타고사실 응시자", "M02S04", "M02", "data-otherHall", "2_4"));
        list.add(new MenuBean("답안지", "M02S05", "M02", "data-answerSheet", "2_5"));
        list.add(new MenuBean("감독관 서명", "M02S06", "M02", "data-signature", "2_6"));

        list.add(new MenuBean("Data 검색&amp;산출물", "M03"));
        list.add(new MenuBean("수험생별 리스트", "M03S01", "M03", "data-examinee", "3_1"));
        list.add(new MenuBean("산출물 다운로드", "M03S02", "M03", "data-report", "3_2"));

        list.add(new MenuBean("사용자관리", "M04"));
        list.add(new MenuBean("게시판&amp;공지사항 관리", "M04S01", "M04", "user-board", "4_1"));
        list.add(new MenuBean("시험 정보 관리", "M04S04", "M04", "user-exmInfo", "4_3"));

        list.add(new MenuBean("시스템 설정", "M05"));
        list.add(new MenuBean("고객사 정보", "M05S01", "M05", "system-client", "5_1"));
        list.add(new MenuBean("데이터 업로드", "M05S02", "M05", "system-upload", "5_2"));
        list.add(new MenuBean("시험정보 관리", "M05S03", "M05", "system-exam", "5_3"));
        list.add(new MenuBean("기술요원 관리", "M05S04", "M05", "user-staff", "5_4"));
        list.add(new MenuBean("디바이스 관리", "M05S05", "M05", "system-device", "5_5"));
        list.add(new MenuBean("APP 관리", "M05S06", "M05", "system-app", "5_6"));
        list.add(new MenuBean("로그보기", "M05S07", "M05", "system-log", "5_7"));
        list.add(new MenuBean("코드정보", "M05S08", "M05", "system-code", "5_8"));

        list.add(new MenuBean("데이터 검증", "M06"));
        list.add(new MenuBean("서버전송 리포트", "M06S01", "M06", "inspect-data", "6_1"));
        list.add(new MenuBean("Device정보 Import & Compare", "M06S02", "M06", "inspect-device", "6_2"));
        list.add(new MenuBean("응시율 Error detect", "M06S03", "M06", "inspect-apply", "6_3"));
        list.add(new MenuBean("수험생&감독관 서명누락리스트", "M06S04", "M06", "inspect-sign", "6_4"));
        list.add(new MenuBean("수험표 출력", "M06S05", "M06", "inspect-print", "6_5"));
        return list;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private class MenuBean {
        public String menuNm;
        public String menuCd;
        public String fkMenuCd;
        public String menuPath;
        public String iconImg;

        public MenuBean(String menuNm, String menuCd) {
            this.menuNm = menuNm;
            this.menuCd = menuCd;
        }

        public MenuBean(String menuNm, String menuCd, String fkMenuCd, String menuPath, String iconImg) {
            this.menuNm = menuNm;
            this.menuCd = menuCd;
            this.fkMenuCd = fkMenuCd;
            this.menuPath = menuPath;
            this.iconImg = iconImg;
        }
    }
}
