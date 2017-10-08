package com.langstok.nlp.httpnaf.service;

import com.langstok.nlp.httpnaf.configuration.properties.DocumentProperties;
import com.langstok.nlp.httpnaf.configuration.properties.ElasticProperties;
import org.apache.log4j.Logger;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import com.langstok.nlp.httpnaf.configuration.properties.NafProperties;

import ixa.kaflib.KAFDocument;


@Service
@EnableConfigurationProperties(DocumentProperties.class)
public class ElasticSearchArticleRepository {

	private final static Logger logger = Logger.getLogger(ElasticSearchArticleRepository.class);

	private Client client;
	private NafService nafService;

	private DocumentProperties documentProperties;


	public ElasticSearchArticleRepository(Client client, NafService nafService, DocumentProperties documentProperties) {
		this.client = client;
		this.nafService = nafService;
		this.documentProperties = documentProperties;
	}

	public KAFDocument getKAFDocumentById(String id, String lang) throws Exception{
		
		String index = documentProperties.getIndex();
		if(documentProperties.isIndexLanguagePostFix())
			index = index+"-"+lang;
		String type = documentProperties.getType();
		logger.debug("Get article id:" +id+" for index: "+ index +" and type: " + type);
		
		GetResponse response = client.prepareGet("articles-"+lang, "article", id).get();
		if(response.getSourceAsString()==null) 
			throw new Exception("document not found");
		
		return nafService.create(nafService.mapArticleDocumentResponse(response));
	}

}
