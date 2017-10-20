package com.langstok.nlp.httpnaf.web.controller;

import com.langstok.nlp.httpnaf.configuration.HttpNafSourceConfiguration;
import com.langstok.nlp.httpnaf.enumeration.SupportedLanguage;
import com.langstok.nlp.httpnaf.repository.ArticleDocumentRepository;
import com.langstok.nlp.httpnaf.service.NafService;
import com.langstok.nlp.httpnaf.web.dto.NafDto;
import ixa.kaflib.KAFDocument;
import org.apache.http.entity.ContentType;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.jdom2.JDOMException;
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

    private HttpNafSourceConfiguration httpNafSourceConfiguration;

    private NafService nafService;


    public NafController(HttpNafSourceConfiguration httpNafSourceConfiguration, NafService nafService) {
        this.httpNafSourceConfiguration = httpNafSourceConfiguration;
        this.nafService = nafService;
    }

    @PostMapping("/naf")
    public ResponseEntity<String> createDocument(@RequestBody NafDto dto) throws IOException, JDOMException {
        KAFDocument kaf = nafService.create(dto);
        httpNafSourceConfiguration.sendMessage(kaf, ContentType.APPLICATION_JSON);
        return ResponseEntity.ok(kaf.toString());
    }

    @GetMapping(value="/kaf")
    public void downloadSynonymsDict(@RequestParam String id,
                                     @RequestParam(defaultValue = "en") SupportedLanguage language,
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
