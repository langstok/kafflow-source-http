package com.langstok.nlp.httpnaf.web.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.entity.ContentType;
import org.elasticsearch.client.Client;
import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.langstok.nlp.httpnaf.configuration.HttpNafSourceConfiguration;
import com.langstok.nlp.httpnaf.service.ElasticNafMapper;

@RestController
@RequestMapping(value="/api")
@ConditionalOnBean(Client.class)
public class ElasticSearchController {
	
	@Autowired
	ElasticNafMapper elasticNafMapper;
	
	@Autowired
	HttpNafSourceConfiguration httpNafSourceConfiguration;
	
	@RequestMapping(value="/elasticsearch", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void handleRequest(
			@RequestParam String id,
			@RequestParam(required=false, defaultValue="en") String lang
			) throws UnsupportedEncodingException, IOException, JDOMException {
		httpNafSourceConfiguration.sendMessage(elasticNafMapper.getKAFDocumentById(id, lang), ContentType.APPLICATION_JSON);
	} 

}
