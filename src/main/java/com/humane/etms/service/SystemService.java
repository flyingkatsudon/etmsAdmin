package com.humane.etms.service;

import com.humane.etms.mapper.DataMapper;
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
    @PersistenceContext private EntityManager entityManager;

    @Value("${path.image.examinee:C:/api/image/examinee}") String pathExaminee;
    @Value("${path.image.noIdCard:C:/api/image/noIdCard}") String pathNoIdCard;
    @Value("${path.image.recheck:C:/api/image/recheck}") String pathRecheck;
    @Value("${path.image.signature:C:/api/image/signature}") String pathSignature;

    private final ImageService imageService;
    private final AttendMapRepository attendMapRepository;
    private final AttendHallRepository attendHallRepository;
    private final AttendPaperRepository attendPaperRepository;
    private final DataMapper mapper;

    @Transactional
    public void resetData(boolean photo) {
        HibernateQueryFactory queryFactory = new HibernateQueryFactory(entityManager.unwrap(Session.class));

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

        queryFactory.delete(attendHall).execute();
        queryFactory.delete(attendMapLog).execute();
        queryFactory.delete(attendPaperLog).execute();
        queryFactory.delete(device).execute();
        queryFactory.delete(attendDoc).execute();

        ScrollableResults scrollAttendMap = queryFactory.select(attendMap.examinee.examineeCd)
                .distinct()
                .from(attendMap)
                .scroll(ScrollMode.FORWARD_ONLY);

        while (scrollAttendMap.next()) {
            String examineeCd = scrollAttendMap.getString(0);
            queryFactory.delete(attendPaper)
                    .where(attendPaper.examinee.examineeCd.eq(examineeCd))
                    .execute();

            queryFactory.delete(attendMap)
                    .where(attendMap.examinee.examineeCd.eq(examineeCd))
                    .execute();

            try {
                queryFactory.delete(examinee)
                        .where(examinee.examineeCd.eq(examineeCd))
                        .execute();

                if (photo) new File(pathExaminee, examineeCd + ".jpg").delete();
            } catch (Exception e) {
                log.error("{}", e.getMessage());
            }
        }
        scrollAttendMap.close();

        queryFactory.delete(attendHall).execute();

        try {
            queryFactory.delete(hall).execute();
        } catch (Exception ignored) {
        }

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
                log.error("{}", e.getMessage());
            }
        }

        // delete photo
        if (photo) imageService.deleteImage(pathNoIdCard, pathRecheck, pathSignature);
    }

    @Transactional
    public void initData() {
        HibernateQueryFactory queryFactory = new HibernateQueryFactory(entityManager.unwrap(Session.class));

        QAttendMap attendMap = QAttendMap.attendMap;

        // smps initialize
        List<String> list = queryFactory.select(attendMap.examinee.examineeCd)
                .from(attendMap)
                .where(attendMap.groupOrder.isNotNull())
                .fetch();

        if(list != null){
            for (String examineeCd  : list) {
                mapper.initGroupOrder(examineeCd);
            }
        }

        HibernateUpdateClause updateMap = queryFactory.update(attendMap)
                .setNull(attendMap.attendDttm)
                .setNull(attendMap.isCheat)
                .setNull(attendMap.isMidOut)
                .setNull(attendMap.memo)
                .setNull(attendMap.attendHall)
                .setNull(attendMap.groupOrder);

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

        imageService.deleteImage(pathNoIdCard, pathRecheck, pathSignature);
    }

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

        if(attendMaps != null){
            for (AttendMap map : attendMaps) {
                attendMapRepository.save(map);
            }
        }

        // imageService.deleteImage(pathNoIdCard);
    }

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

        if(attendMaps != null){
            for (AttendMap map : attendMaps) {
                map.setAttendDttm(null);
                map.setIsCheat(null);
                map.setIsMidOut(null);
                map.setMemo(null);
                map.setAttendHall(null);
                map.setIsScanner(null);
                map.setGroupOrder(null);

                mapper.initGroupOrder(map.getExaminee().getExamineeCd());

                attendMapRepository.save(map);
            }
        }

        List<AttendPaper> attendPapers = queryFactory.select(attendPaper)
                .from(attendPaper)
                .where(attendPaper.attend.attendCd.eq(attendCd))
                .where(attendPaper.hall.hallCd.eq(attendHallCd))
                .fetch();

        if(attendPapers != null){
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

        if(attendHalls != null){
            for (AttendHall hall : attendHalls) {
                hall.setSignDttm(null);
                attendHallRepository.save(hall);
            }
        }

        // imageService.deleteImage(pathRecheck, pathSignature);
    }
}
