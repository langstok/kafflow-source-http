package com.langstok.kafflow.httpnaf.web.controller;

import com.langstok.kafflow.httpnaf.configuration.properties.NafProperties;
import com.langstok.kafflow.httpnaf.service.HttpNafSource;
import com.langstok.kafflow.httpnaf.service.NafService;
import com.langstok.kafflow.httpnaf.web.dto.NafDto;
import io.swagger.annotations.ApiOperation;
import ixa.kaflib.KAFDocument;
import org.apache.http.entity.ContentType;
import org.apache.log4j.Logger;
import org.elasticsearch.action.get.GetResponse;
import org.jdom2.JDOMException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@RestController
@RequestMapping("/api/public")
@EnableConfigurationProperties(NafProperties.class)
public class NafPublicController {

    private static final Logger logger = Logger.getLogger(NafController.class);

    private HttpNafSource httpNafSource;
    private NafService nafService;
    private NafProperties nafProperties;

    public NafPublicController(HttpNafSource httpNafSource, NafService nafService, NafProperties nafProperties) {
        this.httpNafSource = httpNafSource;
        this.nafService = nafService;
        this.nafProperties = nafProperties;
    }

    @PostMapping("/naf")
    @ApiOperation(value = "Synchronous pipeline call", notes = "For testing purposes only!")
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


        if(!nafProperties.getSupportedLanguages().contains(kaf.getLang()))
            return ResponseEntity.badRequest().body(
                    "Unsupported language="+kaf.getLang()
                    + " supported languages="+ Arrays.toString(nafProperties.getSupportedLanguages().toArray()));


        httpNafSource.sendMessage(kaf, ContentType.APPLICATION_JSON);

        try {
            GetResponse getResponse = nafService.getKafDocumentByIdPoll(kaf.getPublic().publicId, kaf.getLang());
            nafService.delete(dto.getPublicId(), kaf.getLang());
            return ResponseEntity.ok(getResponse.getSourceAsString());
        }
        catch(Exception e){
            logger.error("No result within retry policy for document=" + kaf.getPublic().publicId);
        }
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).build();
    }


}
