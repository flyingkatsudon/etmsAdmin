
/**
 * Created by Jeremy on 2017. 8. 28..
 */

package com.humane.etms.controller.admin;

import com.humane.etms.dto.ExamineeDto;
import com.humane.etms.dto.WaitHallDto;
import com.humane.etms.mapper.StudentMapper;
import com.humane.etms.model.AttendWaitHall;
import com.humane.etms.model.Hall;
import com.humane.etms.model.QAttendWaitHall;
import com.humane.etms.model.QHall;
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

@RestController
@RequestMapping(value = "student")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StudentController {

    @PersistenceContext
    private final EntityManager entityManager;
    private final AttendWaitHallRepository waitHallRepository;
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
                        "jrxml/upload-order.jrxml",
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

    @RequestMapping(value = "delAwh")
    public ResponseEntity delAwh(String attendCd, String hallCd) {
        try {
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
}
