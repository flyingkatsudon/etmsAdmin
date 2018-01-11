package com.humane.etms.mapper;

import com.humane.etms.dto.StatusDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

@Mapper
public interface CheckMapper {
    Page<StatusDto> paper(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

    Page<StatusDto> detect1(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

    Page<StatusDto> detect2(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

    Page<StatusDto> invalid(@Param("way") String way, @Param("param") StatusDto param, @Param("pageable") Pageable pageable);

    Page<StatusDto> multiple(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

    Page<Map<String, Object>> getAttendMapLog(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

    Page<Map<String, Object>> getAttendPaperLog(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);
}