package com.langstok.nlp.httpnaf.web.controller;

import org.apache.http.entity.ContentType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.langstok.nlp.httpnaf.configuration.HttpNafSourceConfiguration;
import com.langstok.nlp.httpnaf.service.ElasticSearchArticleRepository;

import ixa.kaflib.KAFDocument;

@RestController
@RequestMapping(value="/api")
public class ElasticSearchController {
	
	private ElasticSearchArticleRepository elasticSearchArticleRepository;
	private HttpNafSourceConfiguration httpNafSourceConfiguration;

	public ElasticSearchController(ElasticSearchArticleRepository elasticSearchArticleRepository, HttpNafSourceConfiguration httpNafSourceConfiguration) {
		this.elasticSearchArticleRepository = elasticSearchArticleRepository;
		this.httpNafSourceConfiguration = httpNafSourceConfiguration;
	}

	@RequestMapping(value="/elasticsearch", method = RequestMethod.GET)
	public ResponseEntity<String> handleRequest(
			@RequestParam String id,
			@RequestParam(required=false, defaultValue="en") String lang
			) throws Exception {
		KAFDocument kaf = elasticSearchArticleRepository.getKAFDocumentById(id, lang);
		httpNafSourceConfiguration.sendMessage(kaf, ContentType.APPLICATION_JSON);
		return ResponseEntity.ok(kaf.toString());
	} 

}
