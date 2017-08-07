package com.humane.etms.controller;

import com.blogspot.na5cent.exom.ExOM;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.humane.etms.form.FormExamineeVo;
import com.humane.etms.form.FormHallVo;
import com.humane.etms.form.FormWaitHall;
import com.humane.etms.model.*;
import com.humane.etms.repository.*;
import com.humane.util.file.FileUtils;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "upload")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UploadController {
    private final AdmissionRepository admissionRepository;
    private final AttendRepository attendRepository;
    private final AttendHallRepository attendHallRepository;
    private final AttendWaitHallRepository waitHallRepository;
    private final AttendMapRepository attendMapRepository;
    private final HallRepository hallRepository;
    private final ExamineeRepository examineeRepository;
    private final AttendDocRepository attendDocRepository;

    // windows
    //@Value("${path.image.examinee:C:/api/etms}") String pathRoot;
    // mac
    @Value("${path.image.examinee:/Users/Jeremy/Humane/api/etms}") String pathRoot;

    // 고려대 면접고사용
    public String validate(String str) {
        if (str.equals("") || str == null) return null;
        else return str;
    }

    // 대기실 별 조 할당 정보
    @RequestMapping(value = "waitHall", method = RequestMethod.POST)
    public ResponseEntity<String> waitHall(@RequestParam("file") MultipartFile multipartFile) throws IOException {

        // 파일이 없울경우 에러 리턴.
        if (multipartFile.isEmpty()) return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(null);

        File file = FileUtils.saveFile(new File(pathRoot, "setting"), multipartFile);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            // 1. excel 변환
            List<FormWaitHall> waitHallList = ExOM.mapFromExcel(file).to(FormWaitHall.class).map(1);
            log.debug("{}", waitHallList);

            // 2. 각 대기실 별 조 배정 현황, vo는 한 행을 의미함
            for (FormWaitHall vo : waitHallList) {

                // 우선 액셀의 각 row의 고사본부, 고사건물, 고사실로 DB 내에 존재하는 Hall을 찾음
                Hall tmpHall = hallRepository.findOne(new BooleanBuilder()
                        .and(QHall.hall.headNm.eq(vo.getHeadNm()))
                        .and(QHall.hall.bldgNm.eq(vo.getBldgNm()))
                        .and(QHall.hall.hallNm.eq(vo.getHallNm()))
                );

                // Hall이 존재하면
                if(tmpHall != null) {

                    // DB에 존재하는 AttendWaitHall을 찾음
                    AttendWaitHall tmp = waitHallRepository.findOne(new BooleanBuilder()
                            .and(QAttendWaitHall.attendWaitHall.division.eq(vo.getDivision()))
                            .and(QAttendWaitHall.attendWaitHall.groupNm.eq(vo.getGroupNm()))
                            .and(QAttendWaitHall.attendWaitHall.hallCd.eq(tmpHall.getHallCd()))
                    );

                    // 찾는 AttendWaitHall이 없다면
                    if (tmp == null) {

                        // 객체를 새로 생성하여 값을 set
                        AttendWaitHall attendWaitHall = new AttendWaitHall();
                        attendWaitHall.setDivision(vo.getDivision());
                        attendWaitHall.setGroupNm(vo.getGroupNm());
                        attendWaitHall.setHallCd(tmpHall.getHallCd());

                        // 모든 값을 저장하면 DB에 최종적으로 저장
                        waitHallRepository.save(attendWaitHall);
                    }
                }
            }

            return ResponseEntity.ok("업로드가 완료되었습니다");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            log.error("{}", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("양식 파일을 확인하세요<br><br>" + throwable.getMessage());
        }
    }

    @RequestMapping(value = "order", method = RequestMethod.POST)
    public ResponseEntity<String> order(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        // 파일이 없울경우 에러 리턴.
        if (multipartFile.isEmpty()) return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(null);

        File file = FileUtils.saveFile(new File(pathRoot, "setting"), multipartFile);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            // 1. excel 변환
            List<FormExamineeVo> orderList = ExOM.mapFromExcel(file).to(FormExamineeVo.class).map(1);
            log.debug("{}", orderList);

            // 2. 각 수험생 별 순번 저장
            for (FormExamineeVo vo : orderList) {

                AttendMap attendMap = attendMapRepository.findOne(new BooleanBuilder()
                        .and(QAttendMap.attendMap.attend.admission.admissionCd.eq(vo.getAdmissionCd()))
                        .and(QAttendMap.attendMap.attend.attendCd.eq(vo.getAttendCd()))
                        .and(QAttendMap.attendMap.examinee.examineeCd.eq(vo.getExamineeCd()))
                );

                if (attendMap != null) {
                    if (vo.getIsAttend()) {
                        attendMap.setGroupNm(validate(vo.getGroupNm()));
                        attendMap.setGroupOrder(validate(vo.getGroupOrder()));
                        attendMap.setDebateNm(validate(vo.getDebateNm()));
                        attendMap.setDebateOrder(validate(vo.getDebateOrder()));
                    } else {
                        attendMap.setGroupNm(null);
                        attendMap.setGroupOrder(null);
                        attendMap.setDebateNm(null);
                        attendMap.setDebateOrder(null);
                    }
                    attendMapRepository.save(attendMap);
                }
            }

            return ResponseEntity.ok("업로드가 완료되었습니다");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            log.error("{}", throwable.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("양식 파일을 확인하세요<br><br>" + throwable.getMessage());
        }
    }

    @RequestMapping(value = "hall", method = RequestMethod.POST)
    public ResponseEntity hall(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        File file = FileUtils.saveFile(new File(pathRoot, "setting"), multipartFile);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            List<FormHallVo> hallList = ExOM.mapFromExcel(file).to(FormHallVo.class).map(1);
            hallList.forEach(dto -> {
                if (dto != null && StringUtils.isNotEmpty(dto.getAdmissionCd())) {

                    Admission admission = mapper.convertValue(dto, Admission.class);
                    admission = admissionRepository.save(admission);

                    // 1. 시험정보 생성
                    Attend attend = mapper.convertValue(dto, Attend.class);
                    attend.setAdmission(admission);

                    // 2. 고사실정보 생성
                    Hall hall = mapper.convertValue(dto, Hall.class);
                    hall = hallRepository.save(hall);

                    // 3. 응시고사실 생성
                    AttendHall attendHall = mapper.convertValue(dto, AttendHall.class);
                    attendHall.setAttend(attend);
                    attendHall.setHall(hall);

                    // 4. 응시고사실 확인
                    AttendHall tmp = attendHallRepository.findOne(new BooleanBuilder()
                            .and(QAttendHall.attendHall.attend.attendCd.eq(attendHall.getAttend().getAttendCd()))
                            .and(QAttendHall.attendHall.hall.hallCd.eq(hall.getHallCd()))
                    );

                    if (tmp != null) {
                        attendHall.set_id(tmp.get_id());
                        attendHall.setSignDttm(tmp.getSignDttm());
                    }

                    // 5. 응시고사실 저장
                    attendRepository.save(attend);
                    attendHallRepository.save(attendHall);

                    // 6. 각서 저장
                    AttendDoc attendDoc = mapper.convertValue(dto, AttendDoc.class);
                    attendDoc.setAdmission(admission);

                    AttendDoc _tmp = attendDocRepository.findOne(new BooleanBuilder()
                            .and(QAttendDoc.attendDoc.admission.admissionCd.eq(admission.getAdmissionCd()))
                    );

                    if (_tmp != null) {
                        attendDoc.set_id(_tmp.get_id());
                    }

                    attendDocRepository.save(attendDoc);
                }
            });

            return ResponseEntity.ok("고사실 정보가 업로드되었습니다");

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            log.error("{}", throwable.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("양식 파일을 확인하세요<br><br>" + throwable.getMessage());
        }
    }

    @RequestMapping(value = "examinee", method = RequestMethod.POST)
    public ResponseEntity examinee(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        File file = FileUtils.saveFile(new File(pathRoot, "setting"), multipartFile);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

        try {
            List<FormExamineeVo> examineeList = ExOM.mapFromExcel(file).to(FormExamineeVo.class).map(1);
            log.debug("{}:", examineeList);

            examineeList.forEach(vo -> {
                if (vo != null && StringUtils.isNoneEmpty(vo.getAdmissionCd())) {

                    // 1. AttendHall 에서 고사실 및 시험정보를 가져온다.
                    QAttend attend = QAttendHall.attendHall.attend;
                    QHall hall = QAttendHall.attendHall.hall;

                    AttendHall attendHall = attendHallRepository.findOne(new BooleanBuilder()
                            .and(attend.attendDate.eq(dtf.parseLocalDateTime(vo.getAttendDate()).toDate()))
                            .and(attend.attendTime.eq(dtf.parseLocalDateTime(vo.getAttendTime()).toDate()))
                            .and(hall.headNm.eq(vo.getHeadNm()))
                            .and(hall.bldgNm.eq(vo.getBldgNm()))
                            .and(hall.hallNm.eq(vo.getHallNm()))
                    );

                    // 3. 수험생정보 생성
                    Examinee examinee = mapper.convertValue(vo, Examinee.class);
                    examineeRepository.save(examinee);

                    AttendMap attendMap = mapper.convertValue(vo, AttendMap.class);
                    attendMap.setAttend(attendHall.getAttend());
                    attendMap.setHall(attendHall.getHall());
                    attendMap.setExaminee(examinee);

                    if (vo.getGroupNm().equals("")) attendMap.setGroupNm(null); // 조 정보가 없으면 null로 처리

                    AttendMap tmp = attendMapRepository.findOne(new BooleanBuilder()
                            .and(QAttendMap.attendMap.attend.attendCd.eq(attendMap.getAttend().getAttendCd()))
                            .and(QAttendMap.attendMap.examinee.examineeCd.eq(attendMap.getExaminee().getExamineeCd()))
                    );

                    if (tmp != null) attendMap.set_id(tmp.get_id());

                    // 3.1 수험생정보 저장
                    attendMapRepository.save(attendMap);
                }
            });
            return ResponseEntity.ok("수험생 정보가 업로드되었습니다");

        } catch (Throwable throwable) {
            log.error("{}", throwable.getMessage());
            throwable.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("양식 파일을 확인하세요<br><br>" + throwable.getMessage());
        }
    }
}
