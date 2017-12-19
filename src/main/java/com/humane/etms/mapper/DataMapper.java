package com.humane.etms.mapper;

import com.humane.etms.dto.ExamineeDto;
import com.humane.etms.dto.StatusDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface DataMapper {
    Page<ExamineeDto> sendPaperInfo(@Param("param") StatusDto param, @Param("name") String name, @Param("pageable") Pageable pageable);

    Page<ExamineeDto> cancelAttend(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

    Page<ExamineeDto> examinee(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

    Page<StatusDto> signature(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

    Page<StatusDto> paper(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

    Page<StatusDto> detail(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

    void checkIdCard(@Param("examineeCd") String examineeCd, @Param("idCheckDttm") Date idCheckDttm, @Param("attendCd") String attendCd);

    List<Map<String, String>> sqlEdit(@Param("sql") String sql);

    Page<ExamineeDto> noIdCard(@Param("param") ExamineeDto param, @Param("pageable") Pageable pageable);
}