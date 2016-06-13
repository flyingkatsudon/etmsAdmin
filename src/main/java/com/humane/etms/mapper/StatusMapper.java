package com.humane.etms.mapper;

import com.humane.etms.dto.StatusDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Mapper
public interface StatusMapper {
    StatusDto all(@Param("param") StatusDto param);

    Page<StatusDto> attend(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

    Page<StatusDto> dept(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

    Page<StatusDto> major(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

    Page<StatusDto> hall(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

    Page<StatusDto> group(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

}