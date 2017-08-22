package com.humane.etms.controller;

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

    // windows
    //@Value("${path.image.examinee:C:/api/etms}") String pathRoot;
    //@Value("${path.image:C:/api/image}") String jpgRoot;

    // mac ex) userid: mac account
    @Value("${path.image.examinee:/Users/userid/Humane/api/etms}") String pathRoot;
    @Value("${path.image:/Users/userid/Humane/api/image}") String jpgRoot;

    private final StatusMapper statusMapper;
    private final DataMapper dataMapper;

    @RequestMapping(value = "staff.xlsx", method = RequestMethod.GET)
    public ResponseEntity staff() {
        return JasperReportsExportHelper.toResponseEntity(
                "jrxml/upload-staff.jrxml",
                "xlsx",
                null
        );
    }

    @RequestMapping(value = "hall.xlsx", method = RequestMethod.GET)
    public ResponseEntity hall() {
        return JasperReportsExportHelper.toResponseEntity(
                "jrxml/upload-hall.jrxml",
                "xlsx",
                null
        );
    }

    @RequestMapping(value = "examinee.xlsx", method = RequestMethod.GET)
    public ResponseEntity examinee() {
        return JasperReportsExportHelper.toResponseEntity(
                "jrxml/upload-examinee.jrxml",
                "xlsx",
                null
        );
    }

    @RequestMapping(value = "allData.zip", method = RequestMethod.GET)
    public ResponseEntity allData(StatusDto statusDto, ExamineeDto examineeDto) throws IOException, ZipException {
        Pageable pageable = new PageRequest(0, Integer.MAX_VALUE);

        // 압축파일 생성
        String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File file = new File(currentTime + "_allData.zip");
        ZipFile zipFile = new ZipFile(file);
        zipFile.setFileNameCharset("EUC-KR");

        String statusPath = "응시율 통계";
        String dataPath = "특이사항 리스트";
        String signPath = "감독관 서명";

        // entry 생성
        File fileAttend = JasperReportsExportHelper.toXlsxFile(
                "jrxml/status-attend.jrxml"
                , statusMapper.attend(statusDto, pageable).getContent());
        zipFile.addFile(statusPath, fileAttend);
        fileAttend.delete();

        File fileDept = JasperReportsExportHelper.toXlsxFile(
                "jrxml/status-dept.jrxml"
                , statusMapper.dept(statusDto, pageable).getContent());
        zipFile.addFile(statusPath, fileDept);
        fileDept.delete();

        File fileMajor = JasperReportsExportHelper.toXlsxFile(
                "jrxml/status-major.jrxml"
                , statusMapper.major(statusDto, pageable).getContent());
        zipFile.addFile(statusPath, fileMajor);
        fileMajor.delete();

        //1. xlsx 파일 생성
        File fileHall = JasperReportsExportHelper.toXlsxFile(
                "jrxml/status-hall.jrxml"
                , statusMapper.hall(statusDto, pageable).getContent());
        //2. zip파일에 추가시키기
        zipFile.addFile(statusPath, fileHall);
        //3. 추가시킨 후 xlsx파일 삭제
        fileHall.delete();

        // 1. xlsx 파일 생성
        File fileGroup = JasperReportsExportHelper.toXlsxFile(
                "jrxml/status-group.jrxml"
                , statusMapper.group(statusDto, pageable).getContent()
        );
        zipFile.addFile(statusPath, fileGroup);
        fileGroup.delete();

        File dataExaminee = JasperReportsExportHelper.toXlsxFile(
                "jrxml/data-examinee.jrxml"
                , dataMapper.examinee(examineeDto, pageable).getContent()
        );
        zipFile.addFile(dataExaminee);
        dataExaminee.delete();

        ExamineeDto dto = examineeDto;
        dto.setIsNoIdCard(true);
        dto.setIsAttend(true);
        File noIdCard = JasperReportsExportHelper.toXlsxFile(
                "jrxml/data-noIdCard.jrxml"
                , dataMapper.noIdCard(dto, pageable).getContent()
        );
        dto.setIsAttend(null);
        dto.setIsNoIdCard(null);
        zipFile.addFile(dataPath, noIdCard);
        noIdCard.delete();

        /*dto.setIsCheck(true);
        File recheck = JasperReportsExportHelper.toXlsxFile(
                "jrxml/data-recheck.jrxml"
                , dataMapper.examinee(dto, pageable).getContent()
        );
        dto.setIsCheck(null);
        zipFile.addFile(dataPath, recheck);
        recheck.delete();*/

        File paper = JasperReportsExportHelper.toXlsxFile(
                "jrxml/data-paper.jrxml"
                , dataMapper.paper(statusDto, pageable).getContent()
        );
        zipFile.addFile(paper);
        paper.delete();

        File signature = JasperReportsExportHelper.toXlsxFile(
                "jrxml/data-signature.jrxml"
                , dataMapper.signature(statusDto, pageable).getContent()
        );
        zipFile.addFile(signature);
        signature.delete();

        File noIdCardFolder = new File(jpgRoot + "/noIdCard");
        File[] noIdCardList = noIdCardFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg"));
        if (noIdCardList != null) {
            for (File f : noIdCardList) {
                if (f.isFile()) {
                    zipFile.addFile(dataPath + "/신분증 미소지자 사진", f);
                }
            }
        }


       /* File recheckFolder = new File(jpgRoot + "/recheck");
        File[] recheckList = recheckFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg"));

        if (recheckList != null){
            for (File f : recheckList) {
                if (f.isFile()) {
                    zipFile.addFile(dataPath + "/재확인 대상자 사진", f);
                }
            }
        }*/

        File signFolder = new File(jpgRoot + "/signature");
        File[] signList = signFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg"));

        if(signList != null){
            for (File f : signList) {
                if (f.isFile())
                    zipFile.addFile(signPath, f);
            }
        }

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
        headers.setContentLength(ba == null ? 0 : ba.length);
        headers.add("Content-Disposition", FileNameEncoder.encode("최종 산출물_출결.zip"));
        return new ResponseEntity<>(ba, headers, HttpStatus.OK);
    }
}