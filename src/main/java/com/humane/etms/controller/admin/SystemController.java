package com.humane.etms.controller.admin;

import com.humane.etms.dto.AccountDto;
import com.humane.etms.dto.DeviceDto;
import com.humane.etms.dto.StatusDto;
import com.humane.etms.mapper.SystemMapper;
import com.humane.etms.model.*;
import com.humane.etms.repository.UserAdmissionRepository;
import com.humane.etms.repository.UserRepository;
import com.humane.etms.repository.UserRoleRepository;
import com.humane.etms.service.SystemService;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping(value = "system", method = RequestMethod.GET)
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SystemController {
    @PersistenceContext
    private final EntityManager entityManager;
    private final UserRepository userRepository;
    private final UserAdmissionRepository userAdmissionRepository;
    private final UserRoleRepository userRoleRepository;
    private final SystemService systemService;
    private final SystemMapper systemMapper;

    @RequestMapping(value = "download", method = RequestMethod.POST)
    public ResponseEntity download() {
        return ResponseEntity.ok("준비 중입니다.");
    }

    @RequestMapping(value = "reset")
    public ResponseEntity reset(@RequestParam(defaultValue = "false") boolean photo) throws IOException {
        try {
            systemService.resetData(photo);
            return ResponseEntity.ok("초기화가 완료되었습니다.&nbsp;&nbsp;클릭하여 창을 종료하세요");
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

    /**
     * 고려대 면접고사용
     */

    @RequestMapping(value = "ready")
    public ResponseEntity ready(StatusDto statusDto, Pageable pageable) {
        return ResponseEntity.ok(systemMapper.ready(statusDto, pageable).getContent());
    }

    @RequestMapping(value = "order")
    public ResponseEntity order() {
        try {
            systemService.setOrder();
        } catch (Exception e) {
            log.error("{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관리자에게 문의하세요");
        }

        return ResponseEntity.ok("완료되었습니다");
    }
}