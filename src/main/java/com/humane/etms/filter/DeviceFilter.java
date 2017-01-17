package com.humane.etms.filter;

import com.humane.etms.model.Device;
import com.humane.etms.model.QDevice;
import com.humane.etms.repository.DeviceRepository;
import com.humane.util.filter.RequestWrapper;
import com.querydsl.core.BooleanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class DeviceFilter extends GenericFilterBean {

    @Autowired
    DeviceRepository deviceRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        RequestWrapper requestWrapper = RequestWrapper.of(request);

        Map<String, String> headerMap = requestWrapper.headerMap();

        String uuid = headerMap.get("uuid"); // 단말기 고유번호
        String phoneNo = headerMap.get("phoneno"); // 전화번호. 없을수도 있다.
        String deviceId = headerMap.get("deviceid"); // 단말기 등록번호 ex : 1
        String packageName = headerMap.get("packagename"); // 앱 com.humane.etms2.app 등등.
        String versionName = headerMap.get("versionname"); // 앱 버전

        if (!StringUtils.isAnyEmpty(uuid, packageName)) {
            Device device = deviceRepository.findOne(new BooleanBuilder()
                    .and(QDevice.device.uuid.eq(uuid))
                    .and(QDevice.device.packageName.eq(packageName))
            );

            if (device == null) {
                device = new Device();
                device.setUuid(uuid);
                device.setPackageName(packageName);
            }

            device.setDeviceId(deviceId);
            device.setPhoneNo(phoneNo);
            device.setVersionName(versionName);
            device.setLastDttm(new DateTime().toDate());

            deviceRepository.save(device);
        }

        chain.doFilter(request, response);
    }
}
