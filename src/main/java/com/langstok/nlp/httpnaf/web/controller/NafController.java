package com.langstok.nlp.httpnaf.web.controller;

import com.langstok.nlp.httpnaf.configuration.HttpNafSourceConfiguration;
import com.langstok.nlp.httpnaf.service.NafService;
import com.langstok.nlp.httpnaf.web.dto.NafDto;
import ixa.kaflib.KAFDocument;
import org.apache.http.entity.ContentType;
import org.jdom2.JDOMException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

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
}
