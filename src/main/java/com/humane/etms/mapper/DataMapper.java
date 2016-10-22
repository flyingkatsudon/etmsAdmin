package com.humane.etms.mapper;

import com.humane.etms.dto.ExamineeDto;
import com.humane.etms.dto.StatusDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

@Mapper
public interface DataMapper {
    Page<ExamineeDto> uplus(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

    Page<ExamineeDto> examinee(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

    Page<StatusDto> signature(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

    Page<StatusDto> paper(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

    Page<StatusDto> detail(@Param("param") StatusDto param, @Param("pageable") Pageable pageable);

    void checkIdCard(@Param("examineeCd") String examineeCd, @Param("idCheckDttm") Date idCheckDttm, @Param("attendCd") String attendCd);

    void recheck(@Param("examineeCd") String examineeCd, @Param("recheckDttm") Date recheckDttm, @Param("attendCd") String attendCd);
}