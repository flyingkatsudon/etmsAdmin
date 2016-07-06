package com.humane.etms.mapper;

import com.humane.etms.dto.ExamineeDto;
import com.humane.etms.dto.StatusDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Mapper
public interface DataMapper {
    Page<ExamineeDto> examinee(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

    Page<StatusDto> signature(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

    Page<StatusDto> paper(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

    Page<StatusDto> detail(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

}