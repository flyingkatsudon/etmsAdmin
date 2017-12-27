package com.humane.etms.controller.admin;

import com.humane.etms.dto.*;
import com.humane.etms.mapper.SystemMapper;
import com.humane.etms.model.*;
import com.humane.etms.repository.AttendRepository;
import com.humane.etms.repository.StaffRepository;
import com.humane.etms.repository.UserAdmissionRepository;
import com.humane.etms.repository.UserRepository;
import com.humane.etms.service.SystemService;
import com.humane.util.jasperreports.JasperReportsExportHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.hibernate.HibernateQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
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
    public ResponseEntity reset(@RequestParam("admissionCd") String admissionCd, @RequestParam("attendCd") String attendCd, @RequestParam(defaultValue = "false") boolean photo) throws IOException {
        try {
            // 전형별 삭제 시
            if(admissionCd != null) {
                // 1. 시험코드를 갖지 않는 row는 미리 삭제
                HibernateQueryFactory queryFactory = new HibernateQueryFactory(entityManager.unwrap(Session.class));
                queryFactory.delete(QAttendDoc.attendDoc).where(QAttendDoc.attendDoc.admission.admissionCd.eq(admissionCd)).execute();
                queryFactory.delete(QUserAdmission.userAdmission).where(QUserAdmission.userAdmission.admission.admissionCd.eq(admissionCd)).execute();

                // 2. 전형이 가지는 시험코드 리스트 불러옴
                Iterable<Attend> list = attendRepository.findAll(new BooleanBuilder()
                        .and(QAttend.attend.admission.admissionCd.eq(admissionCd)));

                // 3. 리스트를 시험코드별로 삭제하는 메서드 반복 실행
                list.forEach(vo -> {
                    String tmp = vo.getAttendCd();
                    systemService.resetData(tmp, photo);
                });
            }
            // 시험별 삭제 시
            else systemService.resetData(attendCd, photo);

            return ResponseEntity.ok("삭제가 완료되었습니다.&nbsp;&nbsp;클릭하여 창을 종료하세요");
        } catch (Exception e) {
            e.printStackTrace();
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

    // 중간본부앱이 사용 - 함수명 바꿀 때 앱도 함께 바꿔야함
    @RequestMapping(value = "initMgr")
    public void initMgr(@RequestParam String admissionCd, @RequestParam String attendDate, @RequestParam String headNm, @RequestParam String bldgNm) throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(attendDate);
        systemService.initMgr(admissionCd, date, headNm, bldgNm);
    }

    // 출결앱이 사용 - 함수명 바꿀 때 앱도 함께 바꿔야함
    @RequestMapping(value = "initApp")
    public void initApp(@RequestParam String attendCd, @RequestParam String attendHallCd) {
        systemService.initApp(attendCd, attendHallCd);
    }

    // 계정정보 가져오기
    @RequestMapping(value = "account")
    public ResponseEntity account(AccountDto accountDto, Pageable pageable) {
        return ResponseEntity.ok(systemMapper.account(accountDto, pageable));
    }

    // 단말기정보 가져오기
    @RequestMapping(value = "device")
    public ResponseEntity getDevice(DeviceDto deviceDto, Pageable pageable) {
        return ResponseEntity.ok(systemMapper.getDevice(deviceDto, pageable));
    }

    // 전형정보 가져오기
    @RequestMapping(value = "admission")
    public ResponseEntity admission(Pageable pageable) {
        return ResponseEntity.ok(systemMapper.admission(pageable).getContent());
    }

    // 전형 세부정보
    @RequestMapping(value = "accountDetail")
    public ResponseEntity accountDetail(String userId, Pageable pageable) {
        return ResponseEntity.ok(systemMapper.accountDetail(userId, pageable).getContent());
    }

    // 계정에서 전형권한 삭제
    @RequestMapping(value = "delAdm")
    public void deleteAdmission(String userId) {
        systemMapper.deleteAdmission(userId);
    }

    // 계정 정보 수정
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

    // 계정 추가
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

    // 계정 삭제
    @RequestMapping(value = "delAccount")
    public void deleteAccount(String userId) {
        systemMapper.deleteAdmission(userId);
        systemMapper.deleteRole(userId);
        systemMapper.deleteAccount(userId);
    }

    // 계정 등록 시 id 체크
    @RequestMapping(value = "idCheck")
    public ResponseEntity idCheck(Pageable pageable) {
        return ResponseEntity.ok(systemMapper.idCheck(pageable).getContent());
    }

    // 시험정보 가져오기
    @RequestMapping(value = "attendInfo")
    public ResponseEntity attendInfo(AttendInfoDto param, Pageable pageable) {
        return ResponseEntity.ok(systemMapper.attendInfo(param, pageable).getContent());
    }

    // 시험정보 수정
    @RequestMapping(value = "modifyAttend")
    public ResponseEntity modifyAttend(@RequestBody AttendInfoDto param) {
        try {
            systemMapper.modifyAttend(param);
            return ResponseEntity.ok("시험정보가 변경되었습니다.&nbsp;&nbsp;클릭하여 창을 종료하세요");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("잠시 후 다시 시도하세요");
        }
    }

    // 중복처리조회
    @RequestMapping(value = "duplicate")
    public ResponseEntity duplicate(Pageable pageable) {
        return ResponseEntity.ok(systemMapper.duplicate(new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent());
    }

    // 중복처리 상세 내역
    @RequestMapping(value = "innerDuplicate")
    public ResponseEntity innerDuplicate(DuplicateDto param, Pageable pageable) {
        return ResponseEntity.ok(systemMapper.innerDuplicate(param, new PageRequest(0, Integer.MAX_VALUE, pageable.getSort())).getContent());
    }

    // 기술요원 정보
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

    // 기술요원 추가
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
                        //.and(QStaff.staff.staffNm.eq(param.getStaffNm()))
                        //.and(QStaff.staff.phoneNo.eq(param.getPhoneNo()))
                        .and(QStaff.staff.bldgNm.eq(param.getBldgNm()))
                        .and(QStaff.staff.attend.attendCd.eq(attend.getAttendCd()))
                );

                if (tmp == null) {
                    param.setAttendCd(attend.getAttendCd());
                    systemMapper.addStaff(param);
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이미 기술요원이 배정된 고사건물입니다");
                }
            }
            return ResponseEntity.ok("추가되었습니다");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관리자에게 문의하세요<br><br>" + e.getMessage());
        }
    }

    // 기술요원 수정
    @RequestMapping(value = "modifyStaff")
    public ResponseEntity modifyStaff(@RequestBody StaffDto param) {
        try {
            Attend attend = attendRepository.findOne(new BooleanBuilder()
                    .and(QAttend.attend.admission.admissionNm.eq(param.getAdmissionNm()))
                    .and(QAttend.attend.attendNm.eq(param.getAttendNm()))
                    .and(QAttend.attend.attendDate.eq(param.getAttendDate()))
                    .and(QAttend.attend.attendTime.eq(param.getAttendTime()))
            );

            if (attend == null) return ResponseEntity.ok("일치하는 고사건물을 찾을 수 없습니다. 다시 시도하세요");
            else {
                param.setAttendCd(attend.getAttendCd());

                Staff tmp;

                if ((param.get_staffNm().equals("") || param.get_staffNm() == null) ||
                        (param.get_phoneNo().equals("") || param.get_phoneNo() == null)) {
                    tmp = staffRepository.findOne(new BooleanBuilder()
                            .and(QStaff.staff.bldgNm.eq(param.get_bldgNm()))
                            .and(QStaff.staff.attend.attendCd.eq(param.getAttendCd()))
                    );

                    tmp.setStaffNm("");
                    tmp.setPhoneNo("");
                } else if (param.getAttendCd() != param.get_attendCd()) {
                    tmp = staffRepository.findOne(new BooleanBuilder()
                            .and(QStaff.staff.bldgNm.eq(param.get_bldgNm()))
                            .and(QStaff.staff.attend.attendCd.eq(param.get_attendCd()))
                    );

                    log.debug("{}", tmp);
                } else {
                    tmp = staffRepository.findOne(new BooleanBuilder()
                            .and(QStaff.staff.staffNm.eq(param.getStaffNm()))
                            .and(QStaff.staff.phoneNo.eq(param.getPhoneNo()))
                            .and(QStaff.staff.bldgNm.eq(param.getBldgNm()))
                            .and(QStaff.staff.attend.attendCd.eq(param.getAttendCd()))
                    );
                }
                if (tmp != null) systemMapper.modifyStaff(param);
            }
            return ResponseEntity.ok("변경되었습니다");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관리자에게 문의하세요<br><br>" + e.getMessage());
        }
    }

    // 기술요원 삭제
    @RequestMapping(value = "delStaff")
    public ResponseEntity delStaff(@RequestBody StaffDto param) {
        try {
            // 시험정보를 찾아 시험코드를 얻는다
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

    // 기술요원 전체 삭제
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
}