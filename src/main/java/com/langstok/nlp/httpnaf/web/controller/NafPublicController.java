package com.langstok.nlp.httpnaf.web.controller;

import com.langstok.nlp.httpnaf.configuration.HttpNafSourceConfiguration;
import com.langstok.nlp.httpnaf.enumeration.SupportedLanguage;
import com.langstok.nlp.httpnaf.service.NafService;
import com.langstok.nlp.httpnaf.web.dto.NafDto;
import ixa.kaflib.KAFDocument;
import org.apache.http.entity.ContentType;
import org.apache.log4j.Logger;
import org.elasticsearch.action.get.GetResponse;
import org.jdom2.JDOMException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/public")
public class NafPublicController {

    private static final Logger logger = Logger.getLogger(NafController.class);

    private HttpNafSourceConfiguration httpNafSourceConfiguration;
    private NafService nafService;

    public NafPublicController(HttpNafSourceConfiguration httpNafSourceConfiguration, NafService nafService) {
        this.httpNafSourceConfiguration = httpNafSourceConfiguration;
        this.nafService = nafService;
    }

    @PostMapping("/naf")
    public ResponseEntity<String> createDocumentPoll(@RequestBody(required = false) NafDto dto,
                                                     @RequestPart(required = false) MultipartFile nafFile)
            throws IOException, JDOMException {

        KAFDocument kaf;
        if(dto!=null)
            kaf = nafService.create(dto);
        else if(nafFile!=null)
            kaf = nafService.create(nafFile);
        else
            return ResponseEntity.badRequest().build();

        httpNafSourceConfiguration.sendMessage(kaf, ContentType.APPLICATION_JSON);

        try {
            GetResponse getResponse = nafService.getKafDocumentByIdPoll(kaf.getPublic().publicId, SupportedLanguage.en.name());
            nafService.delete(dto.getPublicId(), SupportedLanguage.en);
            return ResponseEntity.ok(getResponse.getSourceAsString());
        }
        catch(Exception e){
            logger.error("No result within retry policy for document=" + kaf.getPublic().publicId);
        }
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).build();
    }


}
