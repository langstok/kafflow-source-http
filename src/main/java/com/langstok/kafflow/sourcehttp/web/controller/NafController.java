package com.langstok.kafflow.sourcehttp.web.controller;

import com.langstok.kafflow.sourcehttp.service.HttpNafSource;
import com.langstok.kafflow.sourcehttp.service.NafService;
import com.langstok.kafflow.sourcehttp.web.dto.NafDto;
import ixa.kaflib.KAFDocument;
import org.apache.http.entity.ContentType;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

@RestController
@RequestMapping("/api")
public class NafController {

    private HttpNafSource httpNafSource;

    private NafService nafService;

    public NafController(HttpNafSource httpNafSource, NafService nafService) {
        this.httpNafSource = httpNafSource;
        this.nafService = nafService;
    }

    @PostMapping("/naf")
    public ResponseEntity createDocument(@RequestBody NafDto dto) {
        KAFDocument kaf = nafService.create(dto);
        httpNafSource.sendMessage(kaf, ContentType.APPLICATION_JSON);
        return ResponseEntity.accepted().build();
    }


    @GetMapping(value="/naf")
    public void getKaf(@RequestParam String id,
                       @RequestParam(defaultValue = "en") String language,
                       HttpServletResponse response) throws Exception{
        response.setContentType("application/x-download");
        response.setHeader("Content-disposition", "attachment; filename=kaf.xml");
        writeToResponse(response, nafService.getKaf(id, language));
    }

    private void writeToResponse(HttpServletResponse response, Path path) throws IOException {
        InputStream is = new FileInputStream(path.toFile());
        IOUtils.copy(is, response.getOutputStream());
        response.flushBuffer();
        is.close();
    }
}
