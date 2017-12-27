package com.humane.etms.service;

import com.humane.etms.model.*;
import com.humane.etms.repository.AttendHallRepository;
import com.humane.etms.repository.AttendMapRepository;
import com.humane.etms.repository.AttendPaperRepository;
import com.querydsl.jpa.hibernate.HibernateDeleteClause;
import com.querydsl.jpa.hibernate.HibernateQueryFactory;
import com.querydsl.jpa.hibernate.HibernateUpdateClause;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SystemService {
    @PersistenceContext
    private EntityManager entityManager;

    @Value("${path.image.examinee:C:/api/image/examinee}")
    String pathExaminee;
    @Value("${path.image.noIdCard:C:/api/image/noIdCard}")
    String pathNoIdCard;
    @Value("${path.image.recheck:C:/api/image/recheck}")
    String pathRecheck;
    @Value("${path.image.signature:C:/api/image/signature}")
    String pathSignature;

    private final ImageService imageService;
    private final AttendMapRepository attendMapRepository;
    private final AttendHallRepository attendHallRepository;
    private final AttendPaperRepository attendPaperRepository;

    // 데이터 삭제
    @Transactional
    public void resetData(String attendCd, boolean photo) {
        HibernateQueryFactory queryFactory = new HibernateQueryFactory(entityManager.unwrap(Session.class));

        // 1. 시험를 선택하여 삭제하는 경우
        if (attendCd != null) {
            // 테이블 정의
            QAttendDoc attendDoc = QAttendDoc.attendDoc;
            QAttendMap attendMap = QAttendMap.attendMap;
            QAttendMapLog attendMapLog = QAttendMapLog.attendMapLog;
            QExaminee examinee = QExaminee.examinee;
            QAttend attend = QAttend.attend;
            QAttendHall attendHall = QAttendHall.attendHall;
            QAdmission admission = QAdmission.admission;
            QAttendPaper attendPaper = QAttendPaper.attendPaper;
            QAttendPaperLog attendPaperLog = QAttendPaperLog.attendPaperLog;
            QHall hall = QHall.hall;
            QDevice device = QDevice.device;
            QStaff staff = QStaff.staff;
            QAttendManage attendManage = QAttendManage.attendManage;
            QAttendManageLog attendManageLog = QAttendManageLog.attendManageLog;

            // 1-1. 1차 삭제
            queryFactory.delete(staff).where(staff.attend.attendCd.eq(attendCd)).execute();
            queryFactory.delete(attendHall).where(attendHall.attend.attendCd.eq(attendCd)).execute();
            queryFactory.delete(device).execute();

            // 전형별 삭제 구현 전까지 보류
            //queryFactory.delete(attendDoc).execute();

            // 1-2. 2차 삭제 (수험생) - 해당 시험을 응시하는 수험생만 삭제해야함
            ScrollableResults scrollAttendMap = queryFactory.select(attendMap.examinee.examineeCd)
                    .distinct()
                    .from(attendMap)
                    .where(attendMap.attend.attendCd.eq(attendCd))
                    .scroll(ScrollMode.FORWARD_ONLY);

            while (scrollAttendMap.next()) {
                String examineeCd = scrollAttendMap.getString(0);
                try {
                    queryFactory.delete(examinee)
                            .where(examinee.examineeCd.eq(examineeCd))
                            .execute();
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("{}", e.getMessage());
                }
            }
            scrollAttendMap.close();

            // 1-3. 3차 삭제
            queryFactory.delete(attendManage).where(attendManage.attend.attendCd.eq(attendCd)).execute();
            queryFactory.delete(attendPaper).where(attendPaper.attend.attendCd.eq(attendCd)).execute();
            queryFactory.delete(attendMap).where(attendMap.attend.attendCd.eq(attendCd)).execute();

            queryFactory.delete(attendManageLog).where(attendManageLog.attend.attendCd.eq(attendCd)).execute();
            queryFactory.delete(attendPaperLog).where(attendPaperLog.attend.attendCd.eq(attendCd)).execute();
            queryFactory.delete(attendMapLog).where(attendMapLog.attend.attendCd.eq(attendCd)).execute();

            try {
                queryFactory.delete(hall).execute();
            } catch (Exception ignored) {
            }

            // 1-4. 4차 삭제 (attend, admission)
            ScrollableResults scrollAttend = queryFactory.select(attend.admission.admissionCd)
                    .distinct()
                    .from(attend)
                    .where(attend.attendCd.eq(attendCd))
                    .scroll(ScrollMode.FORWARD_ONLY);

            while (scrollAttend.next()) {
                String admissionCd = scrollAttend.getString(0);
                queryFactory.delete(attend)
                        .where(attend.admission.admissionCd.eq(admissionCd))
                        .where(attend.attendCd.eq(attendCd))
                        .execute();

                try {
                    queryFactory.delete(admission)
                            .where(admission.admissionCd.eq(admissionCd))
                            .execute();
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("{}", e.getMessage());
                }
            }
        }
        // 2. 전체를 삭제하는 경우
        else {
            QAttendDoc attendDoc = QAttendDoc.attendDoc;
            QAttendMap attendMap = QAttendMap.attendMap;
            QAttendMapLog attendMapLog = QAttendMapLog.attendMapLog;
            QExaminee examinee = QExaminee.examinee;
            QAttend attend = QAttend.attend;
            QAttendHall attendHall = QAttendHall.attendHall;
            QAdmission admission = QAdmission.admission;
            QAttendPaper attendPaper = QAttendPaper.attendPaper;
            QAttendPaperLog attendPaperLog = QAttendPaperLog.attendPaperLog;
            QHall hall = QHall.hall;
            QDevice device = QDevice.device;
            QStaff staff = QStaff.staff;
            QAttendManage attendManage = QAttendManage.attendManage;
            QAttendManageLog attendManageLog = QAttendManageLog.attendManageLog;

            // 1차 삭제
            queryFactory.delete(staff).execute();
            queryFactory.delete(attendHall).execute();
            queryFactory.delete(device).execute();
            queryFactory.delete(attendDoc).execute();

            // 2차 삭제 (수험생) - 평가에 영향을 끼치지 않는 수험생만 골라냄
            ScrollableResults scrollAttendMap = queryFactory.select(attendMap.examinee.examineeCd)
                    .distinct()
                    .from(attendMap)
                    .scroll(ScrollMode.FORWARD_ONLY);

            while (scrollAttendMap.next()) {
                String examineeCd = scrollAttendMap.getString(0);

                try {
                    queryFactory.delete(examinee)
                            .where(examinee.examineeCd.eq(examineeCd))
                            .execute();

                    if (photo) new File(pathExaminee, examineeCd + ".jpg").delete();
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("{}", e.getMessage());
                }
            }
            scrollAttendMap.close();

            // 3차 삭제
            queryFactory.delete(attendManage).execute();
            queryFactory.delete(attendPaper).execute();
            queryFactory.delete(attendMap).execute();

            queryFactory.delete(attendManageLog).execute();
            queryFactory.delete(attendPaperLog).execute();
            queryFactory.delete(attendMapLog).execute();

            try {
                queryFactory.delete(hall).execute();
            } catch (Exception ignored) {
            }

            // 4차 삭제
            ScrollableResults scrollAttend = queryFactory.select(attend.admission.admissionCd)
                    .distinct()
                    .from(attend)
                    .scroll(ScrollMode.FORWARD_ONLY);

            while (scrollAttend.next()) {
                String admissionCd = scrollAttend.getString(0);
                queryFactory.delete(attend)
                        .where(attend.admission.admissionCd.eq(admissionCd))
                        .execute();

                try {
                    queryFactory.delete(admission)
                            .where(admission.admissionCd.eq(admissionCd))
                            .execute();
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("{}", e.getMessage());
                }
            }
        }

        // delete photo
        if (photo) imageService.deleteImage(pathNoIdCard, pathRecheck, pathSignature);
    }

    // 데이터 초기화
    @Transactional
    public void initData() {
        HibernateQueryFactory queryFactory = new HibernateQueryFactory(entityManager.unwrap(Session.class));

        QAttendMap attendMap = QAttendMap.attendMap;

        HibernateUpdateClause updateMap = queryFactory.update(attendMap)
                .setNull(attendMap.attendDttm)
                .setNull(attendMap.isCheat)
                .setNull(attendMap.isMidOut)
                .setNull(attendMap.memo)
                .setNull(attendMap.attendHall)
                .setNull(attendMap.isScanner)
                .setNull(attendMap.deviceId);

        updateMap.execute();

        QAttendPaper attendPaper = QAttendPaper.attendPaper;
        HibernateDeleteClause deletePaper = queryFactory.delete(attendPaper);

        deletePaper.execute();

        QAttendHall attendHall = QAttendHall.attendHall;
        HibernateUpdateClause updateHall = queryFactory.update(attendHall).setNull(QAttendHall.attendHall.signDttm);

        updateHall.execute();

        QAttendManage attendManage = QAttendManage.attendManage;
        HibernateDeleteClause deleteManage = queryFactory.delete(attendManage);

        deleteManage.execute();

        QAttendManageLog attendManageLog = QAttendManageLog.attendManageLog;
        HibernateDeleteClause deleteManageLog = queryFactory.delete(attendManageLog);

        deleteManageLog.execute();

        QAttendPaperLog attendPaperLog = QAttendPaperLog.attendPaperLog;
        HibernateDeleteClause deletePaperLog = queryFactory.delete(attendPaperLog);

        deletePaperLog.execute();

        QAttendMapLog attendMapLog = QAttendMapLog.attendMapLog;
        HibernateDeleteClause deleteMapLog = queryFactory.delete(attendMapLog);

        deleteMapLog.execute();

        QDevice device = QDevice.device;
        HibernateDeleteClause deleteDevice = queryFactory.delete(device);

        deleteDevice.execute();

        imageService.deleteImage(pathNoIdCard, pathRecheck, pathSignature);
    }

    // 중간본부 앱에서 서버 초기화 액션을 하는 경우 -> 함수명 바꿀 때 반드시 앱도 함께 바꾸어줘야 함
    @Transactional
    public void initMgr(String admissionCd, Date attendDate, String headNm, String bldgNm) {
        HibernateQueryFactory queryFactory = new HibernateQueryFactory(entityManager.unwrap(Session.class));

        QAttendMap attendMap = QAttendMap.attendMap;

        List<AttendMap> attendMaps = queryFactory.select(attendMap)
                .from(attendMap)
                .where(attendMap.attend.admission.admissionCd.eq(admissionCd))
                .where(attendMap.attend.attendDate.eq(attendDate))
                .where(attendMap.hall.headNm.eq(headNm))
                .where(attendMap.hall.bldgNm.eq(bldgNm))
                .fetch();

        if (attendMaps != null) {
            for (AttendMap map : attendMaps) {
                attendMapRepository.save(map);
            }
        }

        // imageService.deleteImage(pathNoIdCard);
    }

    // 출결 앱에서 서버 초기화 액션을 하는 경우 -> 함수명 바꿀 때 반드시 앱도 함께 바꾸어줘야 함
    @Transactional
    public void initApp(String attendCd, String attendHallCd) {
        HibernateQueryFactory queryFactory = new HibernateQueryFactory(entityManager.unwrap(Session.class));

        QAttendMap attendMap = QAttendMap.attendMap;
        QAttendPaper attendPaper = QAttendPaper.attendPaper;

        List<AttendMap> attendMaps = queryFactory.select(attendMap)
                .from(attendMap)
                .where(attendMap.attend.attendCd.eq(attendCd))
                .where(attendMap.attendHall.hallCd.eq(attendHallCd))
                .fetch();

        if (attendMaps != null) {
            for (AttendMap map : attendMaps) {
                map.setAttendDttm(null);
                map.setIsCheat(null);
                map.setIsMidOut(null);
                map.setMemo(null);
                map.setAttendHall(null);
                map.setIsScanner(null);
                map.setDeviceId(null);

                attendMapRepository.save(map);
            }
        }

        List<AttendPaper> attendPapers = queryFactory.select(attendPaper)
                .from(attendPaper)
                .where(attendPaper.attend.attendCd.eq(attendCd))
                .where(attendPaper.hall.hallCd.eq(attendHallCd))
                .fetch();

        if (attendPapers != null) {
            for (AttendPaper paper : attendPapers) {
                attendPaperRepository.delete(paper);
            }
        }

        QAttendHall attendHall = QAttendHall.attendHall;
        List<AttendHall> attendHalls = queryFactory.select(attendHall)
                .from(attendHall)
                .where(attendHall.attend.attendCd.eq(attendCd))
                .where(attendHall.hall.hallCd.eq(attendHallCd))
                .fetch();

        if (attendHalls != null) {
            for (AttendHall hall : attendHalls) {
                hall.setSignDttm(null);
                attendHallRepository.save(hall);
            }
        }

        // imageService.deleteImage(pathRecheck, pathSignature);
    }
}
