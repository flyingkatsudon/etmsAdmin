package com.humane.etms.controller.admin;

import com.humane.etms.dto.*;
import com.humane.etms.mapper.SystemMapper;
import com.humane.etms.model.*;
import com.humane.etms.repository.*;
import com.humane.etms.service.SystemService;
import com.humane.util.jasperreports.JasperReportsExportHelper;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping(value = "system")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SystemController {
    @PersistenceContext
    private final EntityManager entityManager;
    private final UserRepository userRepository;
    private final UserAdmissionRepository userAdmissionRepository;
    private final AttendWaitHallRepository waitHallRepository;
    private final UserRoleRepository userRoleRepository;
    private final AttendRepository attendRepository;
    private final StaffRepository staffRepository;
    private final SystemService systemService;
    private final SystemMapper systemMapper;

    private static final String JSON = "json";

    @RequestMapping(value = "download", method = RequestMethod.POST)
    public ResponseEntity download() {
        return ResponseEntity.ok("준비 중입니다.");
    }

    @RequestMapping(value = "reset")
    public ResponseEntity reset(@RequestParam(defaultValue = "false") boolean photo) throws IOException {
        try {
            systemService.resetData(photo);
            return ResponseEntity.ok("삭제가 완료되었습니다.&nbsp;&nbsp;클릭하여 창을 종료하세요");
        } catch (Exception e) {
            log.debug("{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관리자에게 문의하세요");
        }
    }

    @RequestMapping(value = "init")
    public ResponseEntity init() throws IOException {
        try {
            systemService.initData();
            return ResponseEntity.ok("초기화가 완료되었습니다.&nbsp;&nbsp;클릭하여 창을 종료하세요.");
        } catch (Exception e) {
            log.debug("{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관리자에게 문의하세요");
        }
    }

    @RequestMapping(value = "initMgr")
    public void initMgr(@RequestParam String admissionCd, @RequestParam String attendDate, @RequestParam String headNm, @RequestParam String bldgNm) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(attendDate);
        systemService.initMgr(admissionCd, date, headNm, bldgNm);
    }

    @RequestMapping(value = "initApp")
    public void initApp(@RequestParam String attendCd, @RequestParam String attendHallCd) {
        systemService.initApp(attendCd, attendHallCd);
    }

    @RequestMapping(value = "account")
    public ResponseEntity account(AccountDto accountDto, Pageable pageable) {
        return ResponseEntity.ok(systemMapper.account(accountDto, pageable));
    }

    @RequestMapping(value = "device")
    public ResponseEntity getDevice(DeviceDto deviceDto, Pageable pageable) {
        return ResponseEntity.ok(systemMapper.getDevice(deviceDto, pageable));
    }

    @RequestMapping(value = "admission")
    public ResponseEntity admission(Pageable pageable) {
        return ResponseEntity.ok(systemMapper.admission(pageable).getContent());
    }

    @RequestMapping(value = "accountDetail")
    public ResponseEntity accountDetail(String userId, Pageable pageable) {
        return ResponseEntity.ok(systemMapper.accountDetail(userId, pageable).getContent());
    }

    @RequestMapping(value = "delAdm")
    public void deleteAdmission(String userId) {
        systemMapper.deleteAdmission(userId);
    }

    @RequestMapping(value = "mod")
    public void modify(String userId, String roleName, String admissionCd, String password) {

        if (admissionCd != null) {
            // 기존 여부 확인
            UserAdmission find = userAdmissionRepository.findOne(new BooleanBuilder()
                    .and(QUserAdmission.userAdmission.user.userId.eq(userId))
                    .and(QUserAdmission.userAdmission.admission.admissionCd.eq(admissionCd)));

            if (find == null) systemMapper.insertAdmission(userId, admissionCd);
        } else {
            // 계정 정보 수정
            if (roleName != null) systemMapper.modifyRole(userId, roleName);
        }

        systemMapper.modifyUser(userId, password);
    }

    @RequestMapping(value = "addAccount")
    public void addAccount(String userId, String password, String roleName) {
        try {
            User findUser = userRepository.findOne(new BooleanBuilder()
                    .and(QUser.user.userId.eq(userId)));

            if (findUser == null) {
                systemMapper.addAccount(userId, password);
            }
        } catch (Exception e) {
            e.getStackTrace();
        } finally {
            systemMapper.addRole(userId, roleName);
        }
    }

    @RequestMapping(value = "delAccount")
    public void deleteAccount(String userId) {
        systemMapper.deleteAdmission(userId);
        systemMapper.deleteRole(userId);
        systemMapper.deleteAccount(userId);
    }

    @RequestMapping(value = "idCheck")
    public ResponseEntity idCheck(Pageable pageable) {
        return ResponseEntity.ok(systemMapper.idCheck(pageable).getContent());
    }

    @RequestMapping(value = "attendInfo")
    public ResponseEntity attendInfo(AttendInfoDto param, Pageable pageable) {
        return ResponseEntity.ok(systemMapper.attendInfo(param, pageable).getContent());
    }

    @RequestMapping(value = "modifyAttend")
    public ResponseEntity modifyAttend(@RequestBody AttendInfoDto param) {
        try {
            systemMapper.modifyAttend(param);
            return ResponseEntity.ok("시험정보가 변경되었습니다.&nbsp;&nbsp;클릭하여 창을 종료하세요");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("잠시 후 다시 시도하세요");
        }
    }

    @RequestMapping(value = "duplicate")
    public ResponseEntity duplicate(Pageable pageable) {
        return ResponseEntity.ok(systemMapper.duplicate(pageable).getContent());
    }

    @RequestMapping(value = "innerDuplicate")
    public ResponseEntity innerDuplicate(DuplicateDto param, Pageable pageable) {
        return ResponseEntity.ok(systemMapper.innerDuplicate(param, pageable).getContent());
    }

    @RequestMapping(value = "staff.{format:json|pdf|xls|xlsx}")
    public ResponseEntity staff(@PathVariable String format, StaffDto param, Pageable pageable) {

        switch (format) {
            case JSON:
                return ResponseEntity.ok(systemMapper.staff(param, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/upload-staff.jrxml",
                        format,
                        systemMapper.uploadStaff(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent()
                );
        }
    }

    @RequestMapping(value = "bldgNm")
    public ResponseEntity bldgNm(StaffDto param, Pageable pageable) {
        return ResponseEntity.ok(systemMapper.bldgNm(param, pageable).getContent());
    }

    @RequestMapping(value = "addStaff")
    public ResponseEntity addStaff(@RequestBody StaffDto param) {
        try {
            Attend attend = attendRepository.findOne(new BooleanBuilder()
                    .and(QAttend.attend.admission.admissionNm.eq(param.getAdmissionNm()))
                    .and(QAttend.attend.attendNm.eq(param.getAttendNm()))
                    .and(QAttend.attend.attendDate.eq(param.getAttendDate()))
                    .and(QAttend.attend.attendTime.eq(param.getAttendTime()))
            );

            if (attend != null) {
                Staff tmp = staffRepository.findOne(new BooleanBuilder()
                        .and(QStaff.staff.phoneNo.eq(param.getPhoneNo()))
                        .and(QStaff.staff.bldgNm.eq(param.getBldgNm()))
                        .and(QStaff.staff.staffNm.eq(param.getStaffNm()))
                        .and(QStaff.staff.attend.attendCd.eq(attend.getAttendCd()))
                );

                if (tmp == null) {
                    param.setAttendCd(attend.getAttendCd());
                    systemMapper.addStaff(param);
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미 등록된 기술요원입니다");
                }
            }
            return ResponseEntity.ok("추가되었습니다");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관리자에게 문의하세요<br><br>" + e.getMessage());
        }
    }

    @RequestMapping(value = "modifyStaff")
    public ResponseEntity modifyStaff(@RequestBody StaffDto param) {
        try {
            Attend attend = attendRepository.findOne(new BooleanBuilder()
                    .and(QAttend.attend.admission.admissionNm.eq(param.getAdmissionNm()))
                    .and(QAttend.attend.attendNm.eq(param.getAttendNm()))
                    .and(QAttend.attend.attendDate.eq(param.getAttendDate()))
                    .and(QAttend.attend.attendTime.eq(param.getAttendTime()))
            );

            if (attend == null) return ResponseEntity.ok("일치하는 전형을 찾을 수 없습니다. 다시 시도하세요");
            else {
                param.setAttendCd(attend.getAttendCd());

                Staff tmp = staffRepository.findOne(new BooleanBuilder()
                        .and(QStaff.staff.phoneNo.eq(param.getPhoneNo()))
                        .and(QStaff.staff.bldgNm.eq(param.getBldgNm()))
                        .and(QStaff.staff.staffNm.eq(param.getStaffNm()))
                        .and(QStaff.staff.attend.attendCd.eq(param.getAttendCd()))
                );

                if (tmp != null) systemMapper.modifyStaff(param);
            }
            return ResponseEntity.ok("변경되었습니다");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관리자에게 문의하세요<br><br>" + e.getMessage());
        }
    }

    @RequestMapping(value = "delStaff")
    public ResponseEntity delStaff(@RequestBody StaffDto param) {
        try {
            Attend attend = attendRepository.findOne(new BooleanBuilder()
                    .and(QAttend.attend.admission.admissionNm.eq(param.get_admissionNm()))
                    .and(QAttend.attend.attendNm.eq(param.get_attendNm()))
                    .and(QAttend.attend.attendDate.eq(param.get_attendDate()))
                    .and(QAttend.attend.attendTime.eq(param.get_attendTime()))
            );

            param.setAttendCd(attend.getAttendCd());

            systemMapper.delStaff(param);
            return ResponseEntity.ok("삭제되었습니다");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관리자에게 문의하세요<br><br>" + e.getMessage());
        }
    }

    @RequestMapping(value = "delStaffAll")
    public ResponseEntity delStaffAll() {
        try {
            systemMapper.delStaffAll();
            return ResponseEntity.ok("삭제되었습니다");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관리자에게 문의하세요<br><br>" + e.getMessage());
        }
    }

    /**
     * 고려대 면접고사용
     */
    @RequestMapping(value = "local/orderCnt")
    public boolean fromLocal(String admissionCd) {

        try {
            long check = systemMapper.orderCnt(admissionCd);

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
                return ResponseEntity.ok(systemMapper.order(examineeDto, pageable));
            default:
                return JasperReportsExportHelper.toResponseEntity(
                        "jrxml/system-order.jrxml",
                        format,
                        systemMapper.order(examineeDto, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent()
                );
        }
    }

    @RequestMapping(value = "waitHall")
    public ResponseEntity waitHall(Pageable pageable) {
        try {
            return ResponseEntity.ok(systemMapper.waitHall(pageable).getContent());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("불러오지 못했습니다. 잠시 후 다시 시도하세요");
        }
    }

    @RequestMapping(value = "delWaitHall")
    public void delWaitHall(String hallCd) {
        systemMapper.delWaitHall(hallCd);
    }

    private final HallRepository hallRepository;

    @RequestMapping(value = "addHall")
    public String addHall(@RequestBody Hall hall){
        try{
            systemMapper.addHall(hall);

            Hall tmp = hallRepository.findOne(new BooleanBuilder()
                    .and(QHall.hall.headNm.eq(hall.getHeadNm()))
                    .and(QHall.hall.bldgNm.eq(hall.getBldgNm()))
                    .and(QHall.hall.hallNm.eq(hall.getHallNm()))
            );

            String hallCd = null;

            if(tmp != null){
                hallCd = tmp.getHallCd();
            }

            return hallCd;

        }catch(Exception e){
            e.printStackTrace();
            return e.getMessage();

        }
    }

    @RequestMapping(value = "addWaitHall")
    public ResponseEntity addWaitHall(String hallCd, String groupNm) {
        try {
            if (hallCd != null) {
                // attendWaitHall이 있는지 검사
                AttendWaitHall attendWaitHall = waitHallRepository.findOne(new BooleanBuilder()
                        .and(QAttendWaitHall.attendWaitHall.hallCd.eq(hallCd))
                        .and(QAttendWaitHall.attendWaitHall.groupNm.eq(groupNm))
                );

                // attendWaitHall이 존재하지 않으면 insert
                if (attendWaitHall == null) systemMapper.addWaitHall(hallCd, groupNm);

                return ResponseEntity.ok("저장되었습니다");
            } else {
                // 1. hall 등록
                //systemMapper.addHall();

                // 2. attendWaithall 등록

                systemMapper.addWaitHall(null, groupNm);
                return ResponseEntity.ok("저장되었습니다");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관리자에게 문의하세요<br><br>" + e.getMessage());
        }
    }

    @RequestMapping(value = "delawh")
    public ResponseEntity delawh() {
        try {
            systemMapper.delWaitHall(null);
            return ResponseEntity.ok("삭제되었습니다");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관리자에게 문의하세요<br><br>" + e.getMessage());
        }
    }

    @RequestMapping(value = "delOrder")
    public ResponseEntity delOrder() {
        try {
            systemMapper.delOrder();
            return ResponseEntity.ok("삭제되었습니다");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관리자에게 문의하세요<br><br>" + e.getMessage());
        }
    }
}