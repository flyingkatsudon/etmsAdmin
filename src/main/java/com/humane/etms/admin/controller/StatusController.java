package com.humane.etms.admin.controller;

import com.humane.etms.admin.api.ApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import retrofit2.Response;

import java.io.IOException;

@Controller
@RequestMapping("status")
@Slf4j
public class StatusController {

    private final ApiService apiService;

    @Autowired
    public StatusController(ApiService apiService) {
        this.apiService = apiService;
    }

    @RequestMapping(value = "page/{page}", method = RequestMethod.GET)
    public String page(Model model, @PathVariable String page) {
        model.addAttribute("page", "status-" + page);
        return "tabLayout";
    }

    @RequestMapping(value = "attend", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> attend() throws IOException {
        Response<String> response = apiService.hall(null, 0, Integer.MAX_VALUE).execute();
        if (response.isSuccessful()) {
            return ResponseEntity.ok(response.body());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(response.raw().toString());
        }
    }
}
