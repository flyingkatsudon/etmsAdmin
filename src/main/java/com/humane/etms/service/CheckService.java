/**
 * Created by Jeremy on 2017. 12. 15..
 */
package com.humane.etms.service;

import com.humane.etms.dto.StatusDto;
import com.humane.etms.mapper.CheckMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CheckService {

    private final CheckMapper checkMapper;

    public List<Map<String, Object>> getDetailLog(StatusDto param, Pageable pageable) throws ParseException {

        // 응시로그: OOO 수험생이 [시간]에 OO번 단말기로 [본부/건물/고사실]에서 응시처리 되었습니다
        // 결시로그: OOO 수험생이 OO번 단말기로 응시취소 되었습니다
        Page<Map<String, Object>> attendMapLog = checkMapper.getAttendMapLog(param, pageable);
        List<Map<String, Object>> mapLog = new ArrayList<>();


        attendMapLog.forEach(map -> {
            Map<String, Object> tmp = new HashMap<>();
            int i = 0;

            String examinee = "[" + map.get("examineeCd").toString() + ", " + map.get("examineeNm").toString() + "]";
            String deviceNo = map.get("deviceNo").toString();
            String location = "[" + map.get("headNm").toString() + "|" + map.get("bldgNm").toString() + "|" + map.get("hallNm").toString() + "]";
            String actionDttm = map.get("attendDttm").toString();
            String logDttm = "[" + map.get("logDttm").toString() + "]";

            tmp.put("actionDttm", map.get("attendDttm"));
            tmp.put("logDttm", map.get("logDttm"));

            if (!map.get("attendDttm").toString().isEmpty()) {
                String action = logDttm + ": <span style='font-weight: bold'>" + deviceNo + "</span> -> " + examinee + " - " + location + ", <span style='font-weight: bold'> 응시처리 </span>(" + actionDttm + ")<br>";
                tmp.put("action", action);
            } else {
                String action = logDttm + ": <span style='font-weight: bold'>" + deviceNo + "</span> -> " + examinee + " - <span style='color: crimson; font-weight: bold'>응시취소.</span><br>";
                tmp.put("action", action);
            }

            mapLog.add(i, tmp);
            i++;
        });

        // 답안지매칭로그: OOO 수험생이 [시간]에 OO번 단말기로 [본부/건물/고사실]에서 OOO 답안지에 매칭되었습니다
        // 답안지교체로그: OOO 수험생이 [시간]에 OO번 단말기로 [본부/건물/고사실]에서 OOO 답안지를 PPP 답안지로 교체하였습니다
        Page<Map<String, Object>> attendPaperLog = checkMapper.getAttendPaperLog(param, pageable);
        List<Map<String, Object>> paperLog = new ArrayList<>();

        attendPaperLog.forEach(map -> {
            int j = 0;
            Map<String, Object> tmp = new HashMap<>();

            String examinee = "[" + map.get("examineeCd").toString() + ", " + map.get("examineeNm").toString() + "]";
            String deviceNo = map.get("deviceNo").toString();
            String location = "[" + map.get("headNm").toString() + "|" + map.get("bldgNm").toString() + "|" + map.get("hallNm").toString() + "]";
            String newPaperCd = map.get("newPaperCd").toString();
            String paperCd = map.get("paperCd").toString();
            String regDttm = map.get("regDttm").toString();
            String logDttm = "[" + map.get("logDttm").toString() + "]";

            tmp.put("actionDttm", map.get("regDttm"));
            tmp.put("logDttm", map.get("logDttm"));

            if (newPaperCd.isEmpty()) {
                String action = logDttm + ": <span style='font-weight: bold'>" + deviceNo + "</span> -> " + examinee + " - " + location + ", <span style='color: #535ff9; font-weight: bold'>답안지 매칭</span> (" + regDttm + ", <span style='color:blue; font-weight: bold'>" + paperCd + "</span>)<br>";
                tmp.put("action", action);
            } else {
                String action = logDttm + ": <span style='font-weight: bold'>" + deviceNo + "</span> -> " + examinee + " - " + location + ", <span style='color: crimson; font-weight: bold'>답안지 교체 (" + paperCd + " -> " + newPaperCd + ")</span><br>";
                tmp.put("action", action);
            }

            paperLog.add(j, tmp);
            j++;
        });

        return mergeMap(mapLog, paperLog);
    }

    // 출결 로그와 답안지 매칭 로그를 하나로 합친다
    private List<Map<String, Object>> mergeMap(List<Map<String, Object>> mapLog, List<Map<String, Object>> paperLog) throws ParseException {

        List<Map<String, Object>> merge = new ArrayList<>();

        for (int i = 0; i < mapLog.size(); i++) {
            merge.add(i, mapLog.get(i));
        }

        for (int j = 0; j < paperLog.size(); j++) {
            merge.add(mapLog.size() + j, paperLog.get(j));
        }

        for (int k = 0; k < merge.size(); k++) {

            for (int l = k + 1; l < merge.size(); l++) {

                Map<String, Object> tmp = merge.get(k);
                Date logDttm1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(tmp.get("logDttm").toString());

                Map<String, Object> inner_tmp = merge.get(l);
                Date logDttm2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(inner_tmp.get("logDttm").toString());

                // 앞 날짜보다 뒷 날짜가 나중일 경우 -> 교체해야
                int compare1st = logDttm1.compareTo(logDttm2);
                if (compare1st == -1) {

                    Map<String, Object> t = tmp;
                    merge.set(k, inner_tmp);
                    merge.set(l, t);

                } else if (compare1st == 0) {

                    Date actionDttm1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(tmp.get("actionDttm").toString());
                    Date actionDttm2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(inner_tmp.get("actionDttm").toString());

                    int compare2nd = actionDttm1.compareTo(actionDttm2);
                    // 뒷 시간이 더 크면
                    if (compare2nd == -1) {

                        Map<String, Object> t = tmp;
                        merge.set(k, inner_tmp);
                        merge.set(l, t);

                    }
                }
            }
        }
        return merge;
    }
}

