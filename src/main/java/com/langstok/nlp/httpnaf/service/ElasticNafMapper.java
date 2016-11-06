package com.langstok.nlp.httpnaf.service;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.langstok.nlp.httpnaf.configuration.ModuleProperties;
import com.langstok.nlp.httpnaf.web.dto.NafDto;

import ixa.kaflib.KAFDocument;


@Service
@EnableConfigurationProperties(ModuleProperties.class)
public class ElasticNafMapper {

	private final static Logger LOGGER = Logger.getLogger(ElasticNafMapper.class);

	@Autowired
	private Client client;

	@Autowired
	ModuleProperties moduleProperties;

	@Autowired
	NafService nafService;
	
	private Map<String,String> nafMapping;

	public KAFDocument getKAFDocumentById(String id, String lang) throws Exception{
		
		String index = moduleProperties.getElasticSearchIndex();
		if(moduleProperties.isElasticSearchIndexLangPostFix()) 
			index = index+"-"+lang;
		String type = moduleProperties.getElasticSearchType();
		LOGGER.debug("Get article id:" +id+" for index: "+ index +" and type: " + type);
		
		GetResponse response = client.prepareGet("articles-"+lang, "article", id).get();
		if(response.getSourceAsString()==null) 
			throw new Exception("document not found");
		
		return nafService.create(mapResponse(response));
	}




	private NafDto mapResponse(GetResponse response) throws JsonParseException, JsonMappingException, IOException{
		
		final ObjectNode node = new ObjectMapper().readValue(response.getSourceAsString(), ObjectNode.class);
		NafDto dto = new NafDto();
		dto.setPublicId(response.getId());
		
		if (nafMapping.containsKey("author") && node.has(nafMapping.get("author"))) {
			dto.setAuthor(node.get("author").asText());
		} 
		if (nafMapping.containsKey("creationTime") && node.has(nafMapping.get("creationTime"))) {
			dto.setCreationtime(new Date(node.get(nafMapping.get("creationTime")).asLong()));
		} 
		if (nafMapping.containsKey("fileName") && node.has(nafMapping.get("fileName"))) {
			dto.setFilename(node.get(nafMapping.get("fileName")).asText());
		}
		if (nafMapping.containsKey("fileType") && node.has(nafMapping.get("fileType"))) {
			dto.setFiletype(node.get(nafMapping.get("fileType")).asText());
		} 
		if (nafMapping.containsKey("language") && node.has(nafMapping.get("language"))) {
			dto.setLanguage(node.get(nafMapping.get("language")).asText());
		} 
		if (nafMapping.containsKey("location") && node.has(nafMapping.get("location"))) {
			dto.setLocation(node.get(nafMapping.get("location")).asText());
		} 
		if (nafMapping.containsKey("magazine") && node.has(nafMapping.get("magazine"))) {
			dto.setMagazine(node.get(nafMapping.get("magazine")).asText());
		} 
		if (nafMapping.containsKey("naf") && node.has(nafMapping.get("naf"))) {
			dto.setNaf(node.get(nafMapping.get("naf")).asText());
		} 
		if (nafMapping.containsKey("pages") && node.has(nafMapping.get("pages"))) {
			dto.setPages(node.get(nafMapping.get("pages")).asInt());
		} 
		if (nafMapping.containsKey("publisher") && node.has(nafMapping.get("publisher"))) {
			dto.setPublisher(node.get(nafMapping.get("publisher")).asText());
		}
		if (nafMapping.containsKey("rawText") && node.has(nafMapping.get("rawText"))) {
			dto.setRawText(node.get(nafMapping.get("rawText")).asText());
		}
		if (nafMapping.containsKey("section") && node.has(nafMapping.get("section"))) {
			dto.setSection(node.get(nafMapping.get("section")).asText());
		}
		if (nafMapping.containsKey("title") && node.has(nafMapping.get("title"))) {
			dto.setTitle(node.get(nafMapping.get("title")).asText());
		}
		if (nafMapping.containsKey("uri") && node.has(nafMapping.get("uri"))) {
			dto.setUri(node.get(nafMapping.get("uri")).asText());
		} 
		return dto;
	}

	@PostConstruct
	private void init(){
		nafMapping = moduleProperties.getElasticSearchNafMapping();
		LOGGER.info("NAF mapping: " + nafMapping.toString());
	}

}
