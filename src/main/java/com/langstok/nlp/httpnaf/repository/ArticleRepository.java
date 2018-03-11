package com.langstok.nlp.httpnaf.repository;

import com.langstok.nlp.httpnaf.configuration.properties.DocumentProperties;
import com.langstok.nlp.httpnaf.enumeration.SupportedLanguage;
import org.apache.log4j.Logger;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;


@Service
@EnableConfigurationProperties(DocumentProperties.class)
public class ArticleRepository {

	private final static Logger logger = Logger.getLogger(ArticleRepository.class);

	private Client client;

	private DocumentProperties documentProperties;

	public ArticleRepository(Client client, DocumentProperties documentProperties) {
		this.client = client;
		this.documentProperties = documentProperties;
	}

	public GetResponse getKAFDocumentById(String id, String lang) {
		
		String index = documentProperties.getIndex();
		if(documentProperties.isIndexLanguageSuffix())
			index = index+"-"+lang;
		String type = documentProperties.getType();
		logger.debug("Get article id:" +id+" for index: "+ index +" and type: " + type);
		
		return client.prepareGet("articles-"+lang, "article", id).get();
	}

    public GetResponse getKAFDocumentByIdExeption(String id, String lang) throws Exception {

        String index = documentProperties.getIndex();
        if(documentProperties.isIndexLanguageSuffix())
            index = index+"-"+lang;
        String type = documentProperties.getType();
        logger.debug("Get article id:" +id+" for index: "+ index +" and type: " + type);

        GetResponse getResponse = client.prepareGet("articles-" + lang, "article", id).get();
        if(!getResponse.isExists())
            throw new Exception("Id not found: " + id);
        return getResponse;
    }

	public Path getKaf(String id, SupportedLanguage language) throws IOException {
		GetRequestBuilder get = client.prepareGet()
				.setIndex("articles-"+language.name())
				.setId(id);
		Map<String, Object> map = get.get().getSourceAsMap();
		byte[] data = Base64.getDecoder().decode((String)map.get("kaf"));
		Path path = Paths.get("./kaf.xml");
		Files.write(path, data);
		return path;
	}

	public DeleteResponse delete(String id, SupportedLanguage language) {
		DeleteRequestBuilder deleteRequestBuilder = client.prepareDelete()
				.setIndex("articles-"+language.name())
				.setType("article")
				.setId(id);

		return deleteRequestBuilder.get();
	}
}
