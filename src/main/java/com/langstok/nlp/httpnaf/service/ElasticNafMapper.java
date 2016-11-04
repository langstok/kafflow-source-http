package com.langstok.nlp.httpnaf.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.langstok.nlp.httpnaf.configuration.ModuleProperties;
import com.langstok.nlp.httpnaf.web.dto.ArticleDto;
import com.langstok.nlp.httpnaf.web.dto.NafDto;

import ixa.kaflib.KAFDocument;


@Service
@ConditionalOnBean(Client.class)
@EnableConfigurationProperties(ModuleProperties.class)
public class ElasticNafMapper {
	
	private final static Logger LOGGER = Logger.getLogger(ElasticNafMapper.class);
	
	@Autowired
	private Client client;
	
	@Autowired
	ModuleProperties moduleProperties;
	
	@Autowired
	NafService nafService;
	
	private ObjectMapper mapper = new ObjectMapper();

	public KAFDocument getKAFDocumentById(String lang, String id) throws UnsupportedEncodingException, IOException, JDOMException{
		return nafService.create(mapArticleToKafDto(mapArticleResponse(client.prepareGet("articles-"+lang, "article", id).get())));
	}
	
	
	private NafDto mapArticleToKafDto(ArticleDto article){
		NafDto dto = new NafDto();
		dto.setPublicId(article.getId());
		dto.setCreationtime(article.getDateRetrieved());
		dto.setLanguage(article.getLanguage());
		dto.setLocation(article.getOutletCountry());
		dto.setMagazine(article.getOutletName());
		dto.setTitle(article.getTitle());
		dto.setRawText(article.getText());
		dto.setUri(article.getUri());
		return dto;
	}
	
	private ArticleDto mapArticleResponse(GetResponse response){
		if(response==null || !response.isExists())
			return null;
		
		try {
			ArticleDto dto = mapper.readValue(response.getSourceAsString(), ArticleDto.class);
			dto.setId(response.getId());
			return dto;
		} catch (JsonParseException e) {
			LOGGER.error("JsonParseException source:" + response.getSourceAsString());
			LOGGER.error("JsonParseException", e);
			return null;

		} catch (JsonMappingException e) {
			LOGGER.error("JsonMappingException source:" + response.getSourceAsString());
			LOGGER.error("JsonMappingException", e);
			return null;

		} catch (IOException e) {
			LOGGER.error("IOException source:" + response.getSourceAsString());
			LOGGER.error("IOException", e);
			return null;
		}
	}


}
