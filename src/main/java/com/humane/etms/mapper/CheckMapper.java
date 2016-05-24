package com.humane.etms.mapper;

import com.humane.etms.dto.StatusDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface CheckMapper {
    Page<StatusDto> signature(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

    Page<StatusDto> send(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

    Page<StatusDto> device(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

}