package com.langstok.nlp.httpnaf.web.controller;

import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.langstok.nlp.httpnaf.configuration.HttpNafSourceConfiguration;
import com.langstok.nlp.httpnaf.service.ElasticNafMapper;

import ixa.kaflib.KAFDocument;

@RestController
@RequestMapping(value="/api")
@ConditionalOnBean(Client.class)
public class ElasticSearchController {
	
	@Autowired
	ElasticNafMapper elasticNafMapper;
	
	@Autowired
	HttpNafSourceConfiguration httpNafSourceConfiguration;
	
	@RequestMapping(value="/elasticsearch", method = RequestMethod.GET)
	public ResponseEntity<String> handleRequest(
			@RequestParam String id,
			@RequestParam(required=false, defaultValue="en") String lang
			) throws Exception {
		KAFDocument kaf = elasticNafMapper.getKAFDocumentById(id, lang);
		httpNafSourceConfiguration.sendMessage(kaf, ContentType.APPLICATION_JSON);
		return ResponseEntity.status(HttpStatus.OK).body(kaf.toString());
	} 

}
