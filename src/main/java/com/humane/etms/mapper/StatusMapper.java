package com.humane.etms.mapper;

import com.humane.etms.dto.AttendInfoDto;
import com.humane.etms.dto.StatusDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Mapper
public interface StatusMapper {

    /**
     * all: 진행 현황, '응시율 통계' 메뉴에서만 사용됨
     * hallStat: 고사실별 응시율
     * attend: 전형별 응시율
     * dept: 모집 단위별 응시율
     * major: 전공별 응시율
     * hall: 고사싧별 응시율
     * group: 조별 응시율
     */

    // StatusMapper.xml에서 메서드명과 같은 id의 쿼리를 찾아간다
    StatusDto all(@Param("param") StatusDto param);

    StatusDto hallStat(@Param("param") StatusDto param);

    Page<StatusDto> attend(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

    Page<StatusDto> dept(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

    Page<StatusDto> major(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

    Page<StatusDto> hall(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

    Page<StatusDto> group(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

    Page<StatusDto> home(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);
}