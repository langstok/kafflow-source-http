package com.langstok.nlp.httpnaf.web.controller;

import com.langstok.nlp.httpnaf.configuration.HttpNafSourceConfiguration;
import com.langstok.nlp.httpnaf.enumeration.SupportedLanguage;
import com.langstok.nlp.httpnaf.service.NafService;
import com.langstok.nlp.httpnaf.web.dto.NafDto;
import ixa.kaflib.KAFDocument;
import org.apache.http.entity.ContentType;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.elasticsearch.action.get.GetResponse;
import org.jdom2.JDOMException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/public")
public class NafPublicController {

    private HttpNafSourceConfiguration httpNafSourceConfiguration;

    private NafService nafService;

    public NafPublicController(HttpNafSourceConfiguration httpNafSourceConfiguration, NafService nafService) {
        this.httpNafSourceConfiguration = httpNafSourceConfiguration;
        this.nafService = nafService;
    }

    @PostMapping("/naf")
    public ResponseEntity<String> createDocumentPoll(@RequestBody NafDto dto) throws Exception {
        KAFDocument kaf = nafService.create(dto);
        httpNafSourceConfiguration.sendMessage(kaf, ContentType.APPLICATION_JSON);
        GetResponse getResponse = nafService.getKafDocumentByIdPoll(dto.getPublicId(), SupportedLanguage.en.name());
        if(getResponse!=null && getResponse.isExists()) {
            nafService.delete(dto.getPublicId(), SupportedLanguage.en);
            return ResponseEntity.ok(getResponse.getSourceAsString());
        }
        return ResponseEntity.notFound().build();
    }

}
