package com.humane.etms.service;

import com.humane.etms.model.*;
import com.mysema.query.jpa.hibernate.HibernateDeleteClause;
import com.mysema.query.jpa.hibernate.HibernateQuery;
import com.mysema.query.jpa.hibernate.HibernateUpdateClause;
import com.mysema.query.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

    @Transactional
    public void resetData(boolean photo) {
        new HibernateDeleteClause(entityManager.unwrap(Session.class), QAttendHall.attendHall).execute();

        ScrollableResults scrollableResults = new HibernateQuery(entityManager.unwrap(Session.class))
                .from(QAttendMap.attendMap)
                .setFetchSize(Integer.MIN_VALUE)
                .scroll(ScrollMode.FORWARD_ONLY);

        while (scrollableResults.next()) {
            AttendMap attendMap = (AttendMap) scrollableResults.get(0);
            try {
                new HibernateDeleteClause(entityManager.unwrap(Session.class), QAttendMap.attendMap)
                        .where(QAttendMap.attendMap.examinee.eq(attendMap.getExaminee()))
                        .execute();
                new HibernateDeleteClause(entityManager.unwrap(Session.class), QExaminee.examinee)
                        .where(QExaminee.examinee.eq(attendMap.getExaminee()))
                        .execute();
            } catch (Exception ignored) {
            }
        }
        scrollableResults.close();

        new HibernateDeleteClause(entityManager.unwrap(Session.class), QHall.hall).execute();

        /**
         * SELECT DISTINCT ADMISSION_CD
         *   FROM ADMISSION
         *  INNER JOIN EXAM ON ADMISSION.ADMISSION_CD = EXAM.ADMISSION_CD
         */

        List<String> admissions = new JPAQuery(entityManager)
                .from(QAttend.attend)
                .distinct()
                .list(QAttend.attend.admission.admissionCd);

        new HibernateDeleteClause(entityManager.unwrap(Session.class), QAttend.attend).execute();
        new HibernateDeleteClause(entityManager.unwrap(Session.class), QAdmission.admission)
                .where(QAdmission.admission.admissionCd.in(admissions))
                .execute();

        // delete photo
        if(photo){
            imageService.deleteImage(pathExaminee, pathNoIdCard, pathRecheck, pathSignature);
        }
    }

    @Transactional
    public ResponseEntity<String> initData(boolean photo) {

        QAttendMap attendMap = QAttendMap.attendMap;
        new HibernateUpdateClause(entityManager.unwrap(Session.class), attendMap)
                .setNull(attendMap.attendDttm)
                .setNull(attendMap.idCheckDttm)
                .setNull(attendMap.isCheat)
                .setNull(attendMap.isCheck)
                .setNull(attendMap.isMidOut)
                .setNull(attendMap.isNoIdCard)
                .setNull(attendMap.memo)
                .setNull(attendMap.recheckDttm)
                .setNull(attendMap.attendHall)
                .execute();

        new HibernateDeleteClause(entityManager.unwrap(Session.class), QAttendPaper.attendPaper).execute();

        QAttendHall attendHall = QAttendHall.attendHall;
        new HibernateUpdateClause(entityManager.unwrap(Session.class), attendHall)
                .setNull(attendHall.signDttm)
                .execute();

        if(photo){
            imageService.deleteImage(pathNoIdCard, pathRecheck, pathSignature);
        }

        return ResponseEntity.ok("OK");
    }
}
