package com.humane.etms.mapper;

import com.humane.etms.dto.AccountDto;
import com.humane.etms.dto.DeviceDto;
import com.humane.etms.dto.DuplicateDto;
import com.humane.etms.dto.StatusDto;
import com.humane.etms.model.Device;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Mapper
public interface SystemMapper {
    Page<AccountDto> account(@Param("param") AccountDto param, @Param("pageable") Pageable pageable);
    Page<AccountDto> admission(@Param("pageable") Pageable pageable);
    Page<AccountDto> accountDetail(@Param("userId") String userId, @Param("pageable") Pageable pageable);
    Page<Device> getDevice(@Param("param") DeviceDto deviceDto, @Param("pageable") Pageable pageable);
    void addAccount(@Param("userId") String userId, @Param("password") String password);
    void addRole(@Param("userId") String userId, @Param("roleName") String roleName);
    void deleteAccount(@Param("userId") String userId);
    void deleteRole(@Param("userId") String userId);

    void insertAdmission(@Param("userId") String userId, @Param("admissionCd") String admissionCd);
    void deleteAdmission(@Param("userId") String userId);
    void modifyUser(@Param("userId") String userId, @Param("password") String password);
    void modifyRole(@Param("userId") String userId, @Param("roleName") String roleName);

    Page<AccountDto> idCheck(@Param("pageable") Pageable pageable);

    Page<DuplicateDto> duplicate(@Param("pageable") Pageable pageable);
    Page<DuplicateDto> innerDuplicate(@Param("param") DuplicateDto duplicateDto, @Param("pageable") Pageable pageable);

    List<Map<String, Object>> getInfo();
    List<Map<String, Object>> getAttendResult();

    Page<Map<String, Object>> ready(@Param("param") StatusDto statusDto, @Param("pageable") Pageable pageable);
}