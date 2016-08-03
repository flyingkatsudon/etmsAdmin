package com.humane.etms.mapper;

import com.humane.etms.dto.StatusDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ModelMapper {
    List<StatusDto> toolbar(@Param("param") StatusDto param);
}