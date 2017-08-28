package com.humane.etms.mapper;

import com.humane.etms.dto.*;
import com.humane.etms.model.Device;
import com.humane.etms.model.Hall;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Mapper
public interface SystemMapper {

    /**
     * 공통
     *
     * author: jeremy(김민섭)
     *
     * // 시험정보
     * Page<AttendInfoDto> attendInfo(@Param("param") AttendInfoDto param, @Param("pageable") Pageable pageable);
     *
     * // 시험정보 수정
     * void modifyAttend(@Param("param") AttendInfoDto param);
     *
     * // 두 대 이상의 단말기에서 한 수험생을 처리한 리스트
     * Page<DuplicateDto> duplicate(@Param("pageable") Pageable pageable);
     *
     * // 해당 수험생의 처리이력
     * Page<DuplicateDto> innerDuplicate(@Param("param") DuplicateDto duplicateDto, @Param("pageable") Pageable pageable);
     *
     */

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

    Page<AttendInfoDto> attendInfo(@Param("param") AttendInfoDto param, @Param("pageable") Pageable pageable);

    void modifyAttend(@Param("param") AttendInfoDto param);

    Page<DuplicateDto> duplicate(@Param("pageable") Pageable pageable);

    Page<DuplicateDto> innerDuplicate(@Param("param") DuplicateDto param, @Param("pageable") Pageable pageable);

    Page<StaffDto> staff(@Param("param") StaffDto param, @Param("pageable") Pageable pageable);

    Page<StaffDto> bldgNm(@Param("param") StaffDto param, @Param("pageable") Pageable pageable);

    void addStaff(@Param("param") StaffDto param);

    void modifyStaff(@Param("param") StaffDto param);

    void delStaff(@Param("param") StaffDto param);

    void delStaffAll();

    /**
     * 고려대 면접고사용
     *
     * author: jeremy(김민섭)
     *
     * // local에 저장된 순번 갯수
     * long orderCnt(@Param("param") String admissionCd)
     *
     * // 수험생 별 순번 정보
     * Page<ExamineeDto> order(@Param("param") ExamineeDto examineeDto, @Param("pageable") Pageable pageable)
     *
     * // 조별 대기실 리스트
     * Page<WaitHallDto> waitHall(@Param("pageable") Pageable pageable);
     */
    long orderCnt(@Param("param") String admissionCd);
    Page<ExamineeDto> order(@Param("param") ExamineeDto examineeDto, @Param("pageable") Pageable pageable);
    Page<WaitHallDto> waitHall(@Param("pageable") Pageable pageable);
    void delAwh(@Param("hallCd") String hallCd);
    void delOrder();
    void addHall(@Param("param") Hall param);
    void addAwh(@Param("hallCd") String hallCd, @Param("groupNm") String groupNm);
    void delHall(@Param("hallCd") String hallCd);
}