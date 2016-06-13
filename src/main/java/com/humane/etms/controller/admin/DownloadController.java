package com.humane.etms.controller.admin;

import com.humane.etms.dto.ExamineeDto;
import com.humane.etms.dto.StatusDto;
import com.humane.etms.mapper.DataMapper;
import com.humane.etms.mapper.StatusMapper;
import com.humane.util.file.FileNameEncoder;
import com.humane.util.jasperreports.JasperReportsExportHelper;
import com.humane.util.zip4j.ZipFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping(value = "download")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DownloadController {

    @RequestMapping(value = "hall.xlsx", method = RequestMethod.GET)
    public ResponseEntity hall(){
        return JasperReportsExportHelper.toResponseEntity(
                "jrxml/upload-hall.jrxml",
                "xlsx",
                null
        );
    }

    @RequestMapping(value = "examinee.xlsx", method = RequestMethod.GET)
    public ResponseEntity examinee(){
        return JasperReportsExportHelper.toResponseEntity(
                "jrxml/upload-examinee.jrxml",
                "xlsx",
                null
        );
    }

    @Value("${path.image.examinee:C:/api/etms}") String pathRoot;
    private final StatusMapper statusMapper;
    private final DataMapper dataMapper;

    @RequestMapping(value = "allData.zip", method = RequestMethod.GET)
    public ResponseEntity allData() throws IOException, ZipException {
        Pageable pageable = new PageRequest(0, Integer.MAX_VALUE);

        // 압축파일 생성
        String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File file = new File(currentTime + "_allData.zip");
        ZipFile zipFile = new ZipFile(file);

        String statusPath = "응시율 통계";
        String dataPath = "특이사항 리스트";

        // entry 생성
        File fileAttend = JasperReportsExportHelper.toXlsxFile(
                "jrxml/status-attend.jrxml"
                ,statusMapper.attend(new StatusDto(), pageable).getContent());
        zipFile.addFile(statusPath, fileAttend);
        fileAttend.delete();

        File fileDept = JasperReportsExportHelper.toXlsxFile(
                "jrxml/status-dept.jrxml"
                , statusMapper.dept(new StatusDto(), pageable).getContent());
        zipFile.addFile(statusPath, fileDept);
        fileDept.delete();

        File fileMajor = JasperReportsExportHelper.toXlsxFile(
                "jrxml/status-major.jrxml"
                , statusMapper.major(new StatusDto(), pageable).getContent());
        zipFile.addFile(statusPath, fileMajor);
        fileMajor.delete();

        //1. xlsx 파일 생성
        File fileHall = JasperReportsExportHelper.toXlsxFile(
                "jrxml/status-hall.jrxml"
                , statusMapper.hall(new StatusDto(), pageable).getContent());
        //2. zip파일에 추가시키기
        zipFile.addFile(statusPath, fileHall);
        //3. 추가시킨 후 xlsx파일 삭제
        fileHall.delete();

        // 1. xlsx 파일 생성
        File fileGroup = JasperReportsExportHelper.toXlsxFile(
                "jrxml/status-group.jrxml"
                , statusMapper.group(new StatusDto(), pageable).getContent()
        );
        zipFile.addFile(statusPath, fileGroup);
        fileGroup.delete();

        File dataExaminee = JasperReportsExportHelper.toXlsxFile(
                "jrxml/data-examinee.jrxml"
                , dataMapper.examinee(new ExamineeDto(), pageable).getContent()
        );
        zipFile.addFile(dataExaminee);
        dataExaminee.delete();

        ExamineeDto dto = new ExamineeDto();
        dto.setIsNoIdCard(true);
        File noIdCard = JasperReportsExportHelper.toXlsxFile(
                "jrxml/data-noIdCard.jrxml"
                , dataMapper.examinee(dto, pageable).getContent()
        );
        dto.setIsNoIdCard(false);
        zipFile.addFile(dataPath, noIdCard);
        noIdCard.delete();

        dto.setIsCheck(true);
        File recheck = JasperReportsExportHelper.toXlsxFile(
                "jrxml/data-recheck.jrxml"
                , dataMapper.examinee(dto, pageable).getContent()
        );
        dto.setIsCheck(false);
        zipFile.addFile(dataPath, recheck);
        recheck.delete();

        dto.setIsOtherHall(true);
        File otherHall = JasperReportsExportHelper.toXlsxFile(
                "jrxml/data-otherHall.jrxml"
                , dataMapper.examinee(dto, pageable).getContent()
        );
        dto.setIsOtherHall(false);
        zipFile.addFile(dataPath, otherHall);
        otherHall.delete();

        byte[] ba = null;

        // 압축파일 내보내기
        try (FileInputStream fis = new FileInputStream(file)) {
            ba = IOUtils.toByteArray(fis);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            file.delete();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Set-Cookie", "fileDownload=true; path=/");
        headers.setContentType(MediaType.parseMediaType("application/zip"));
        headers.setContentLength(ba.length);
        headers.add("Content-Disposition", FileNameEncoder.encode("최종 산출물_ETMS.zip"));
        return new ResponseEntity<>(ba, headers, HttpStatus.OK);
    }

    public void manager() throws ZipException {
        String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File file = new File(currentTime + "manager.zip");
        ZipFile zipFile = new ZipFile(file);
    }
}