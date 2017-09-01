
/**
 * Created by Jeremy on 2017. 8. 28..
 */

package com.humane.etms.controller.admin;

import com.humane.etms.dto.ExamineeDto;
import com.humane.etms.dto.OrderDto;
import com.humane.etms.mapper.StudentMapper;
import com.humane.etms.model.*;
import com.humane.etms.repository.AttendMapRepository;
import com.humane.etms.repository.AttendWaitHallRepository;
import com.humane.etms.repository.HallRepository;
import com.humane.util.jasperreports.JasperReportsExportHelper;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "student")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StudentController {

    @PersistenceContext
    private final EntityManager entityManager;
    private final AttendWaitHallRepository waitHallRepository;
    private final AttendMapRepository attendMapRepository;
    private final StudentMapper studentMapper;

    private static final String JSON = "json";

    /**
     * 고려대 면접고사용
     */
    @RequestMapping(value = "local/orderCnt")
    public boolean fromLocal(String admissionCd) {

        try {
            long check = studentMapper.orderCnt(admissionCd);

            if (check <= 0)
                return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @RequestMapping(value = "order.{format:json|pdf|xls|xlsx}")
    public ResponseEntity order(@PathVariable String format, ExamineeDto examineeDto, Pageable pageable) {

        switch (format) {
            case JSON:
                return ResponseEntity.ok(studentMapper.order(examineeDto, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/student-order.jrxml",
                        format,
                        studentMapper.order(examineeDto, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent()
                );
        }
    }

    @RequestMapping(value = "waitHall")
    public ResponseEntity waitHall(Pageable pageable) {
        try {
            return ResponseEntity.ok(studentMapper.waitHall(pageable).getContent());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("불러오지 못했습니다. 잠시 후 다시 시도하세요");
        }
    }

    private final HallRepository hallRepository;

    @RequestMapping(value = "checkHall")
    public boolean checkHall(@RequestBody Hall hall) {
        try {
            Hall tmp = hallRepository.findOne(new BooleanBuilder()
                    .and(QHall.hall.headNm.eq(hall.getHeadNm()))
                    .and(QHall.hall.bldgNm.eq(hall.getBldgNm()))
                    .and(QHall.hall.hallNm.eq(hall.getHallNm()))
            );

            if (tmp != null) return false;
            else return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @RequestMapping(value = "addHall")
    public String addHall(@RequestBody Hall hall) {
        try {

            Hall tmp = hallRepository.findOne(new BooleanBuilder()
                    .and(QHall.hall.headNm.eq(hall.getHeadNm()))
                    .and(QHall.hall.bldgNm.eq(hall.getBldgNm()))
                    .and(QHall.hall.hallNm.eq(hall.getHallNm()))
            );

            String hallCd;

            if (tmp != null) {
                hallCd = tmp.getHallCd();
            } else {
                studentMapper.addHall(hall);
                hallCd = hallRepository.findOne(new BooleanBuilder()
                        .and(QHall.hall.headNm.eq(hall.getHeadNm()))
                        .and(QHall.hall.bldgNm.eq(hall.getBldgNm()))
                        .and(QHall.hall.hallNm.eq(hall.getHallNm()))
                ).getHallCd();
            }

            return hallCd;

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @RequestMapping(value = "addAwh")
    public ResponseEntity addAwh(String attendCd, String hallCd, String groupNm) {
        try {
            // attendWaitHall이 있는지 검사
            AttendWaitHall attendWaitHall = waitHallRepository.findOne(new BooleanBuilder()
                    .and(QAttendWaitHall.attendWaitHall.attendCd.eq(attendCd))
                    .and(QAttendWaitHall.attendWaitHall.hallCd.eq(hallCd))
                    .and(QAttendWaitHall.attendWaitHall.groupNm.eq(groupNm))
            );

            // attendWaitHall이 존재하지 않으면 insert
            if (attendWaitHall == null) studentMapper.addAwh(attendCd, hallCd, groupNm);

            return ResponseEntity.ok("저장되었습니다");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관리자에게 문의하세요<br><br>" + e.getMessage());
        }
    }

    @RequestMapping(value = "delHall")
    public ResponseEntity delHall(String attendCd, String hallCd){
        try {
            // attend_wait_hall에서 찾아 비우고
            studentMapper.delAwh(attendCd, hallCd);

            // hall에서 찾아 비움
           // studentMapper.delHall(hallCd);
            return ResponseEntity.ok("삭제되었습니다");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관리자에게 문의하세요<br><br>" + e.getMessage());
        }
    }

    @RequestMapping(value = "delAwh")
    public ResponseEntity delAwh(String attendCd, String hallCd) {
        try {
            // attend_wait_hall에서 찾아 비움
            studentMapper.delAwh(attendCd, hallCd);
            return ResponseEntity.ok("삭제되었습니다");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관리자에게 문의하세요<br><br>" + e.getMessage());
        }
    }

    @RequestMapping(value = "delOrder")
    public ResponseEntity delOrder() {
        try {
            studentMapper.delOrder();
            return ResponseEntity.ok("삭제되었습니다");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관리자에게 문의하세요<br><br>" + e.getMessage());
        }
    }

    @RequestMapping(value = "ready")
    public long ready(String attendCd) {
        try {
            long result = studentMapper.ready(attendCd);
            return result;
        } catch (Exception e) {
            return 0;
        }
    }

    @RequestMapping(value = "assign")
    public ResponseEntity assign(@RequestBody OrderDto orderDto) {
        try {
            // 응시하지 않은 수험생에 조 정보가 남아있을 것을 우려, 필터링 없이 시험 내 모든 수험생을 받아
            List<AttendMap> attendMapList = (List<AttendMap>) attendMapRepository.findAll(new BooleanBuilder()
                            .and(QAttendMap.attendMap.attend.attendCd.eq(orderDto.getAttendCd()))
                    //.and(QAttendMap.attendMap.attendDttm.isNotNull())
            );

            List<Map<String, String>> groupList = orderDto.getGroupList();

            // cnt: 학생 인덱스, order: 면접 순서, debateNm: 토론 조 이름
            int cnt = 0;
            long order = 1;
            int debateNm = 0;

            // 조 배정
            for (int i = 0; i < groupList.size(); i++) {
                // 순번 배정
                for (int j = cnt; j < attendMapList.size(); j++) {
                    AttendMap attendMap = attendMapList.get(j);

                    if (attendMap.getAttendDttm() != null) {

                        // 1. 조 배정
                        String groupNm = groupList.get(i).get("groupNm");
                        attendMap.setGroupNm(groupNm);

                        // 2. 순번 배정
                        attendMap.setGroupOrder(order);

                        // 3. 토론 조 배정
                        // 순번이 3의 배수가되면 토론 조 이름에 +1을 한다
                        if (order % 3 == 1) debateNm++;

                        attendMap.setDebateNm(groupNm.substring(0, groupNm.length() - 1) + "-" + debateNm);
                        attendMap.setDebateOrder(null);

                        // 배정 끝난 수험생 업데이트
                        attendMapRepository.save(attendMap);
                        cnt++;
                        order++;

                        if (cnt % orderDto.getOrderCnt() == 0) {
                            order = 1;
                            debateNm = 0;
                            break;
                        } else if (cnt == attendMapList.size()) break;
                    } else {
                        // 응시하지 않은 수험생이면 조 정보를 null로 덮어씀
                        // 찌꺼기를 깔끔하게 null로 만들기 위함
                        attendMap.setGroupNm(null);
                        attendMap.setGroupOrder(null);
                        attendMap.setDebateNm(null);
                        attendMap.setDebateOrder(null);

                        attendMapRepository.save(attendMap);
                    }
                }
            }
            return ResponseEntity.ok("배정이 완료되었습니다");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok("나중에 다시 시도하세요<br><br>" + e.getMessage());
        }
    }
}
