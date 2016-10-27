package com.humane.etms.controller;

import com.blogspot.na5cent.exom.ExOM;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.humane.etms.form.FormExamineeVo;
import com.humane.etms.form.FormHallVo;
import com.humane.etms.model.*;
import com.humane.etms.repository.*;
import com.humane.util.file.FileUtils;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private final AttendMapRepository attendMapRepository;
    private final HallRepository hallRepository;
    private final ExamineeRepository examineeRepository;

    @Value("${path.image.examinee:C:/api/smps}") String pathRoot;

    @RequestMapping(value = "hall", method = RequestMethod.POST)
    public void hall(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        File file = FileUtils.saveFile(new File(pathRoot, "setting"), multipartFile);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            List<FormHallVo> hallList = ExOM.mapFromExcel(file).to(FormHallVo.class).map(1);
            hallList.forEach(dto -> {

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
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            log.error("{}", throwable.getMessage());
        }
    }

    @RequestMapping(value = "examinee", method = RequestMethod.POST)
    public void examinee(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        File file = FileUtils.saveFile(new File(pathRoot, "setting"), multipartFile);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

        try {
            List<FormExamineeVo> examineeList = ExOM.mapFromExcel(file).to(FormExamineeVo.class).map(1);
            log.debug("{}:", examineeList);

            examineeList.forEach(vo -> {

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

                AttendMap tmp = attendMapRepository.findOne(new BooleanBuilder()
                        .and(QAttendMap.attendMap.attend.attendCd.eq(attendMap.getAttend().getAttendCd()))
                        .and(QAttendMap.attendMap.examinee.examineeCd.eq(attendMap.getExaminee().getExamineeCd()))
                );

                if (tmp != null) attendMap.set_id(tmp.get_id());

                // 3.1 수험생정보 저장
                attendMapRepository.save(attendMap);
            });
        } catch (Throwable throwable) {
            log.error("{}", throwable.getMessage());
            throwable.printStackTrace();
        }
    }
}
