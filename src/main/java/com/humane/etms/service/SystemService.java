package com.humane.etms.service;

import com.humane.etms.model.*;
import com.querydsl.jpa.hibernate.HibernateQueryFactory;
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

    @Transactional
    public void resetData(boolean photo) {
        HibernateQueryFactory queryFactory = new HibernateQueryFactory(entityManager.unwrap(Session.class));

        QAttendMap attendMap = QAttendMap.attendMap;
        QExaminee examinee = QExaminee.examinee;
        QAttend attend = QAttend.attend;
        QAttendHall attendHall = QAttendHall.attendHall;
        QAdmission admission = QAdmission.admission;

        queryFactory.delete(attendHall).execute();

        ScrollableResults scrollAttendMap = queryFactory.select(attendMap.examinee.examineeCd)
                .distinct()
                .from(attendMap)
                .scroll(ScrollMode.FORWARD_ONLY);

        while (scrollAttendMap.next()) {
            String examineeCd = scrollAttendMap.getString(0);
            queryFactory.delete(attendMap)
                    .where(attendMap.examinee.examineeCd.eq(examineeCd))
                    .execute();

            try {
                queryFactory.delete(examinee)
                        .where(examinee.examineeCd.eq(examineeCd))
                        .execute();
            } catch (Exception e) {
                log.error("{}", e.getMessage());
            }
        }
        scrollAttendMap.close();

        queryFactory.delete(attendHall).execute();

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
        if (photo) imageService.deleteImage(pathExaminee, pathNoIdCard, pathRecheck, pathSignature);
    }

    @Transactional
    public ResponseEntity<String> initData() {
        HibernateQueryFactory queryFactory = new HibernateQueryFactory(entityManager.unwrap(Session.class));

        QAttendMap attendMap = QAttendMap.attendMap;
        queryFactory.update(attendMap)
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

        queryFactory.delete(QAttendPaper.attendPaper).execute();

        queryFactory.update(QAttendHall.attendHall)
                .setNull(QAttendHall.attendHall.signDttm)
                .execute();

        imageService.deleteImage(pathNoIdCard, pathRecheck, pathSignature);

        return ResponseEntity.ok("OK");
    }
}
