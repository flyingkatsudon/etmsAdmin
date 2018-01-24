package com.humane.etms.mapper;

import com.humane.etms.dto.*;
import com.humane.etms.model.Device;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    Page<AttendInfoDto> attendBasic(@Param("param") AttendInfoDto param, @Param("pageable") Pageable pageable);

    Page<AttendInfoDto> attendDetail(@Param("param") AttendInfoDto param, @Param("pageable") Pageable pageable);

    Page<WaitHallDto> ahList(@Param("param") WaitHallDto param, @Param("pageable") Pageable pageable);

    void modifyAttend(@Param("param") AttendInfoDto param);

    void modifyAhList(@Param("param") WaitHallDto param);

    Page<DuplicateDto> duplicate(@Param("pageable") Pageable pageable);

    Page<DuplicateDto> innerDuplicate(@Param("param") DuplicateDto param, @Param("pageable") Pageable pageable);

    Page<StaffDto> staff(@Param("param") StaffDto param, @Param("pageable") Pageable pageable);

    Page<StaffDto> uploadStaff(@Param("param") StaffDto param, @Param("pageable") Pageable pageable);

    void addStaff(@Param("param") StaffDto param);

    void modifyStaff(@Param("param") StaffDto param);

    void delStaff(@Param("param") StaffDto param);

    void delStaffAll();
}