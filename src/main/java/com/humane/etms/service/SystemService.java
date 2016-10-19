package com.humane.etms.service;

import com.humane.etms.model.*;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.hibernate.HibernateDeleteClause;
import com.querydsl.jpa.hibernate.HibernateQuery;
import com.querydsl.jpa.hibernate.HibernateQueryFactory;
import com.querydsl.jpa.hibernate.HibernateUpdateClause;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.io.File;
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
            queryFactory.delete(QHall.hall).execute();
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
    public ResponseEntity<String> initData(String attendCd, String attendHallCd) {
        HibernateQueryFactory queryFactory = new HibernateQueryFactory(entityManager.unwrap(Session.class));

        QAttendMap attendMap = QAttendMap.attendMap;

        // 사진목록
        HibernateQuery<String> selectMap = queryFactory.select(attendMap.examinee.examineeCd).from(attendMap);

        if (StringUtils.isNotEmpty(attendCd)) selectMap.where(attendMap.attend.attendCd.eq(attendCd));
        if (StringUtils.isNotEmpty(attendHallCd)) selectMap.where(attendMap.attendHall.hallCd.eq(attendHallCd));

        List<String> list = selectMap.fetch();

       // TODO : 이미지 선택 삭제
        // imageService.deleteImage(pathNoIdCard, list);

        HibernateUpdateClause updateMap = queryFactory.update(attendMap)
                .setNull(attendMap.attendDttm)
                .setNull(attendMap.idCheckDttm)
                .setNull(attendMap.isCheat)
                .setNull(attendMap.isCheck)
                .setNull(attendMap.isMidOut)
                .setNull(attendMap.isNoIdCard)
                .setNull(attendMap.memo)
                .setNull(attendMap.recheckDttm)
                .setNull(attendMap.attendHall);

        if (StringUtils.isNotEmpty(attendCd)) updateMap.where(attendMap.attend.attendCd.eq(attendCd));
        if (StringUtils.isNotEmpty(attendHallCd)) updateMap.where(attendMap.attendHall.hallCd.eq(attendHallCd));
        updateMap.execute();

        QAttendPaper attendPaper = QAttendPaper.attendPaper;
        HibernateDeleteClause deletePaper = queryFactory.delete(attendPaper);

        if (StringUtils.isNotEmpty(attendCd)) deletePaper.where(attendPaper.attend.attendCd.eq(attendCd));
        if (StringUtils.isNotEmpty(attendHallCd)) deletePaper.where(attendPaper.hall.hallCd.eq(attendHallCd));
        deletePaper.execute();


        QAttendHall attendHall = QAttendHall.attendHall;
        HibernateUpdateClause updateHall = queryFactory.update(attendHall).setNull(QAttendHall.attendHall.signDttm);

        if (StringUtils.isNotEmpty(attendCd)) updateHall.where(attendHall.attend.attendCd.eq(attendCd));
        if (StringUtils.isNotEmpty(attendHallCd)) updateHall.where(attendHall.hall.hallCd.eq(attendHallCd));
        updateHall.execute();

        imageService.deleteImage(pathNoIdCard, pathRecheck, pathSignature);

        return ResponseEntity.ok("OK");
    }
}
