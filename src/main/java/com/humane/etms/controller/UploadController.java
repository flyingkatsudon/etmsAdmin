package com.humane.etms.controller;

import com.blogspot.na5cent.exom.ExOM;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.humane.etms.form.FormExamineeVo;
import com.humane.etms.form.FormHallVo;
import com.humane.etms.form.FormStaffVo;
import com.humane.etms.model.*;
import com.humane.etms.repository.*;
import com.humane.util.file.FileUtils;
import com.humane.util.zip4j.ZipFile;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.model.FileHeader;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    private final AttendMapRepository attendMapRepository;
    private final HallRepository hallRepository;
    private final ExamineeRepository examineeRepository;
    private final AttendDocRepository attendDocRepository;
    private final StaffRepository staffRepository;

    // windows
    @Value("${path.image.examinee:C:/api/etms}") String pathRoot;
    // mac
    //@Value("${path.image.examinee:/Users/Jeremy/Humane/api/etms}") String pathRoot;

    @Value("${path.image.examinee:C:/api}") String root;

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

                    // 시험 내 시작 답안지, 마지막 답안지 번호 체크
                    if (attend.getFirstAssignPaperCd() == null || attend.getFirstAssignPaperCd().equals(""))
                        attend.setFirstAssignPaperCd(null);

                    if (attend.getLastAssignPaperCd() == null || attend.getLastAssignPaperCd().equals(""))
                        attend.setLastAssignPaperCd(null);

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

    @RequestMapping(value = "staff", method = RequestMethod.POST)
    public ResponseEntity staff(@RequestParam("file") MultipartFile multipartFile) throws Throwable {
        File file = FileUtils.saveFile(new File(pathRoot, "setting"), multipartFile);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            List<FormStaffVo> staffList = ExOM.mapFromExcel(file).to(FormStaffVo.class).map(1);

            for (FormStaffVo vo : staffList) {
                Staff staff = mapper.convertValue(vo, Staff.class);

                // 기술요원 이름과 전화번호가 있는 것들에 한해서 업로드 진행
                if (!staff.getStaffNm().isEmpty() || !staff.getPhoneNo().isEmpty()) {
                    log.debug("{}", staff);
                    Attend attend = attendRepository.findOne(new BooleanBuilder()
                            .and(QAttend.attend.admission.admissionNm.eq(vo.getAdmissionNm()))
                            .and(QAttend.attend.attendCd.eq(vo.getAttendCd()))
                            .and(QAttend.attend.attendDate.eq(vo.getAttendDate()))
                            .and(QAttend.attend.attendTime.eq(vo.getAttendTime()))
                    );

                    // 시험정보를 알아내 시험코드를 입력한다
                    if (attend != null) {
                        staff.setAttend(attend);
                        Staff tmp = staffRepository.findOne(new BooleanBuilder()
                                //.and(QStaff.staff.staffNm.eq(staff.getStaffNm()))
                                //.and(QStaff.staff.phoneNo.eq(staff.getPhoneNo()))
                                .and(QStaff.staff.bldgNm.eq(staff.getBldgNm()))
                                .and(QStaff.staff.attend.attendCd.eq(attend.getAttendCd()))
                        );

                        staff.setAttend(attend);

                        if (tmp != null) staff.set_id(tmp.get_id());
                        staffRepository.save(staff);

                    } else {
                        return ResponseEntity.ok("일치하는 고사건물을 찾지 못했습니다. 양식을 다시 확인하세요");
                    }
                }
            }
            return ResponseEntity.ok("기술요원이 추가되었습니다");

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            log.error("{}", throwable.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("양식 파일을 확인하세요. 한 시험에 중복된 고사건물이 존재합니다<br><br>" + throwable.getMessage());
        }
    }

    @RequestMapping(value = "photo", method = RequestMethod.POST)
    public ResponseEntity examineePhoto(@RequestPart("file") MultipartFile multipartFile) throws IOException {
        String path = root + "/image/examinee";

        if (multipartFile.getOriginalFilename().endsWith(".zip")) {
            try {
                File file = FileUtils.saveFile(new File(path, "photo_backup"), multipartFile);

                ZipFile zipFile = new ZipFile(file);
                String charset = zipFile.getCharset();
                log.debug("Detected charset : {}", charset);
                zipFile.setFileNameCharset(charset);

                List<FileHeader> fileHeaders = zipFile.getFileHeaders();
                for (FileHeader fileHeader : fileHeaders) {
                    String fileName = fileHeader.getFileName();

                    Examinee examinee = examineeRepository.findOne(new BooleanBuilder()
                            .and(QExaminee.examinee.examineeCd.eq(fileName.replace(".jpg", "")))
                    );

                    if(examinee != null) {
                        String tmp = examinee.getExamineeCd() + ".jpg";
                        if (tmp.equals(fileName) && fileName.endsWith(".jpg")) {
                            zipFile.extractFile(fileHeader, path);
                        }
                    }
                }
                return ResponseEntity.ok("완료되었습니다");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일의 용량이 10MB를 초과하거나 존재하지 않는 수험생입니다" + e.getMessage());
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("zip 파일이 아닙니다");
        }
    }
}
