package com.humane.etms.service;

import com.humane.etms.model.*;
import com.humane.etms.repository.AdmissionRepository;
import com.humane.etms.repository.AttendRepository;
import com.humane.etms.repository.ExamineeRepository;
import com.humane.etms.repository.HallRepository;
import com.mysema.query.jpa.hibernate.HibernateDeleteClause;
import com.mysema.query.jpa.hibernate.HibernateQuery;
import com.mysema.query.jpa.impl.JPAQuery;
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
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SystemService {
    private final AdmissionRepository admissionRepository;
    private final AttendRepository attendRepository;
    private final HallRepository hallRepository;
    private final ExamineeRepository examineeRepository;

    @PersistenceContext private EntityManager entityManager;
    @Value("${path.image.examinee:C:/api/image/examinee}") String pathImageExaminee;

    @Transactional
    public void resetData(String choice) {
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

                // delete photo
                if (choice.equals("photo")) {
                    String examineeCd = attendMap.getExaminee().getExamineeCd();

                    ImageService imageService = new ImageService();
                    imageService.deleteImageExaminee(examineeCd);
                }

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
    }

    /*
    private Observable<File> imageExaminee(ApiService apiService, String fileName) {
        File path = new File(pathImageExaminee);

        if (!path.exists()) path.mkdirs();
        File file = new File(path, fileName);

        if (file.exists()) {
            return Observable.just(file);
        } else {
            return apiService.imageExaminee(fileName)
                    .flatMap(responseBody -> {
                        try (FileOutputStream fos = new FileOutputStream(file)) {
                            IOUtils.write(responseBody.bytes(), fos);
                            return Observable.just(file);
                        } catch (IOException e) {
                            return Observable.error(e);
                        }
                    });
        }
    }*/
}
