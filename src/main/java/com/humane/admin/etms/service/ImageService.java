package com.humane.admin.etms.service;

import com.humane.admin.etms.api.ApiService;
import lombok.RequiredArgsConstructor;
import okhttp3.ResponseBody;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Response;
import rx.Observable;

import java.io.*;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImageService {
    private final ApiService apiService;

    @Value("${path.image.examinee:C:/api/image/examinee}") String pathImageExaminee;

    public InputStream getImageExaminee(String fileName) {
        try {
            File file = new File(pathImageExaminee + "/" + fileName);
            if (file.exists()) {
                return new FileInputStream(file);
            } else {
                Observable<Response<ResponseBody>> observable = apiService.imageExaminee(fileName);
                Response<ResponseBody> response = observable.toBlocking().first();
                if (response.isSuccessful()) {
                    IOUtils.write(response.body().bytes(), new FileOutputStream(file));
                    return response.body().byteStream();
                }
            }
        } catch (IOException e) {

        }
        return null;
    }
}
