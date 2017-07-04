package com.humane.etms.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DeviceDto implements Serializable {
    private String deviceId;
    private String packageName;
    private String uuid;
    private String phoneNo;
    private String deviceNo;
    private String versionName;
    private String lastDttm;
}