package com.langstok.kafflow.sourcehttp.web.controller;

import com.langstok.kafflow.sourcehttp.configuration.properties.NafProperties;
import com.langstok.kafflow.sourcehttp.service.HttpNafSource;
import com.langstok.kafflow.sourcehttp.service.NafService;
import com.langstok.kafflow.sourcehttp.web.dto.NafDto;
import io.swagger.annotations.ApiOperation;
import ixa.kaflib.KAFDocument;
import org.apache.http.entity.ContentType;
import org.elasticsearch.action.get.GetResponse;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;

@RestController
@RequestMapping("/api/public")
@EnableConfigurationProperties(NafProperties.class)
public class NafPublicController {

    private Logger logger = LoggerFactory.getLogger(NafPublicController.class);


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
    public ResponseEntity<String> createDocumentPoll(@RequestBody(required = false) NafDto dto
//            , @RequestPart(required = false) MultipartFile nafFile
    )
            throws IOException, JDOMException {

        KAFDocument kaf;
        if(dto!=null)
            kaf = nafService.create(dto);
//        else if(nafFile!=null)
//            kaf = nafService.create(nafFile);
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

            byte[] data = Base64.getDecoder().decode((String) getResponse.getSourceAsMap().get(nafProperties.getFieldNaf()));
            String resultString = new String(data);
            return ResponseEntity.ok(resultString);
        }
        catch(Exception e){
            logger.error("No result within retry policy for document=" + kaf.getPublic().publicId);
        }
        return ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).build();
    }


}
