package com.langstok.nlp.httpnaf.web.controller;

import com.langstok.nlp.httpnaf.service.NafService;
import org.apache.http.entity.ContentType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.langstok.nlp.httpnaf.configuration.HttpNafSourceConfiguration;

import ixa.kaflib.KAFDocument;

@RestController
@RequestMapping(value="/api")
public class ElasticSearchController {
	
	private NafService nafService;
	private HttpNafSourceConfiguration httpNafSourceConfiguration;

	public ElasticSearchController(NafService nafService, HttpNafSourceConfiguration httpNafSourceConfiguration) {
		this.nafService = nafService;
		this.httpNafSourceConfiguration = httpNafSourceConfiguration;
	}

	@RequestMapping(value="/elasticsearch", method = RequestMethod.GET)
	public ResponseEntity<String> processDocument(
			@RequestParam String id,
			@RequestParam(required=false, defaultValue="en") String lang
			) throws Exception {
		KAFDocument kaf = nafService.getKAFDocumentById(id, lang);
		httpNafSourceConfiguration.sendMessage(kaf, ContentType.APPLICATION_JSON);
		return ResponseEntity.ok(kaf.toString());
	} 

}
