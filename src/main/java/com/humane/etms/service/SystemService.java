package com.humane.etms.service;

import com.humane.etms.model.*;
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

    @Transactional
    public void resetData(boolean photo) {
        HibernateQueryFactory queryFactory = new HibernateQueryFactory(entityManager.unwrap(Session.class));

        QAttendMap attendMap = QAttendMap.attendMap;
        QExaminee examinee = QExaminee.examinee;
        QAttend attend = QAttend.attend;
        QAttendHall attendHall = QAttendHall.attendHall;
        QAdmission admission = QAdmission.admission;
        QAttendPaper attendPaper = QAttendPaper.attendPaper;
        QHall hall = QHall.hall;

        queryFactory.delete(attendHall).execute();

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

        HibernateUpdateClause updateMap = queryFactory.update(attendMap)
                .setNull(attendMap.attendDttm)
                .setNull(attendMap.idCheckDttm)
                .setNull(attendMap.isCheat)
                .setNull(attendMap.isCheck)
                .setNull(attendMap.isNoIdCard)
                .setNull(attendMap.isMidOut)
                .setNull(attendMap.memo)
                .setNull(attendMap.recheckDttm)
                .setNull(attendMap.attendHall);

        updateMap.execute();

        QAttendPaper attendPaper = QAttendPaper.attendPaper;
        HibernateDeleteClause deletePaper = queryFactory.delete(attendPaper);

        deletePaper.execute();

        QAttendHall attendHall = QAttendHall.attendHall;
        HibernateUpdateClause updateHall = queryFactory.update(attendHall).setNull(QAttendHall.attendHall.signDttm);

        updateHall.execute();

        imageService.deleteImage(pathNoIdCard, pathRecheck, pathSignature);
    }

    @Transactional
    public void initMgr(String admissionCd, Date attendDate, String headNm, String bldgNm) {
        HibernateQueryFactory queryFactory = new HibernateQueryFactory(entityManager.unwrap(Session.class));

        QAttendMap attendMap = QAttendMap.attendMap;

        HibernateUpdateClause updateMap = queryFactory.update(attendMap)
                .setNull(attendMap.isNoIdCard)
                .where(attendMap.attend.admission.admissionCd.eq(admissionCd))
                .where(attendMap.attend.attendDate.eq(attendDate))
                .where(attendMap.hall.headNm.eq(headNm))
                .where(attendMap.hall.bldgNm.eq(bldgNm));

        updateMap.execute();

        // imageService.deleteImage(pathNoIdCard);
    }

    @Transactional
    public void initApp(String attendCd, String attendHallCd) {
        HibernateQueryFactory queryFactory = new HibernateQueryFactory(entityManager.unwrap(Session.class));

        QAttendMap attendMap = QAttendMap.attendMap;

        HibernateUpdateClause updateMap = queryFactory.update(attendMap)
                .setNull(attendMap.attendDttm)
                .setNull(attendMap.idCheckDttm)
                .setNull(attendMap.isCheat)
                .setNull(attendMap.isCheck)
                .setNull(attendMap.isMidOut)
                .setNull(attendMap.memo)
                .setNull(attendMap.recheckDttm)
                .setNull(attendMap.attendHall)
                .where(attendMap.attend.attendCd.eq(attendCd))
                .where(attendMap.attendHall.hallCd.eq(attendHallCd));

        updateMap.execute();

        QAttendPaper attendPaper = QAttendPaper.attendPaper;
        HibernateDeleteClause deletePaper = queryFactory.delete(attendPaper)
                .where(attendPaper.attend.attendCd.eq(attendCd))
                .where(attendPaper.hall.hallCd.eq(attendHallCd));

        deletePaper.execute();

        QAttendHall attendHall = QAttendHall.attendHall;
        HibernateUpdateClause updateHall = queryFactory.update(attendHall)
                .setNull(QAttendHall.attendHall.signDttm)
                .where(attendHall.attend.attendCd.eq(attendCd))
                .where(attendHall.hall.hallCd.eq(attendHallCd));

        updateHall.execute();

        // imageService.deleteImage(pathRecheck, pathSignature);
    }
}
