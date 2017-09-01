package com.humane.etms.mapper;

import com.humane.etms.dto.ExamineeDto;
import com.humane.etms.dto.WaitHallDto;
import com.humane.etms.model.Hall;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Mapper
public interface StudentMapper {

    /**
     * 고려대 면접고사용
     *
     * author: jeremy(김민섭)
     *
     * // local에 저장된 순번 갯수
     * long orderCnt(@Param("param") String admissionCd)
     *
     * // 수험생 별 순번 정보
     * Page<ExamineeDto> order(@Param("param") ExamineeDto examineeDto, @Param("pageable") Pageable pageable)
     *
     * // 조별 대기실 리스트
     * Page<WaitHallDto> waitHall(@Param("pageable") Pageable pageable);
     */
    long orderCnt(@Param("param") String admissionCd);
    Page<ExamineeDto> order(@Param("param") ExamineeDto examineeDto, @Param("pageable") Pageable pageable);
    Page<WaitHallDto> waitHall(@Param("pageable") Pageable pageable);
    void delOrder();
    void addHall(@Param("param") Hall param);
    void addAwh(@Param("attendCd") String attendCd, @Param("hallCd") String hallCd, @Param("groupNm") String groupNm);
    void delHall(@Param("hallCd") String hallCd);
    void delAwh(@Param("attendCd") String attendCd, @Param("hallCd") String hallCd);

    long ready(@Param("attendCd") String attendCd);
}
