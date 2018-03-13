package com.langstok.kafflow.httpnaf.repository;

import com.langstok.kafflow.httpnaf.configuration.properties.ElasticProperties;
import org.apache.log4j.Logger;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.Client;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;


@Service
@EnableConfigurationProperties(ElasticProperties.class)
public class ArticleRepository {

	private final static Logger logger = Logger.getLogger(ArticleRepository.class);

	private Client client;
	private ElasticProperties elasticsearchProperties;


	public ArticleRepository(Client client, ElasticProperties elasticProperties) {
		this.client = client;
		this.elasticsearchProperties = elasticProperties;
	}

    public GetResponse getKAFDocumentByIdExeption(String id, String language) throws Exception {

		String index = getIndex(language);
		String type = elasticsearchProperties.getType();

		logger.info("Poll " + index + "/" + type + "/" + id);
		GetResponse getResponse = client.prepareGet(index, type, id).get();
        if(!getResponse.isExists())
            throw new Exception("Document not found: " + id);
        return getResponse;
    }

	public Path getKaf(String id, String language) throws IOException {

		String index = getIndex(language);
		String type = elasticsearchProperties.getType();

		logger.info("Get " + index + "/" + type + "/" + id);
		GetRequestBuilder get = client.prepareGet()
				.setIndex(index)
				.setType(type)
				.setId(id);

		Map<String, Object> map = get.get().getSourceAsMap();
		byte[] data = Base64.getDecoder().decode((String)map.get(elasticsearchProperties.getFieldNaf()));
		Path path = Paths.get("./" + id + "-kaf.xml");
		Files.write(path, data);
		return path;
	}

	public DeleteResponse delete(String id, String language) {
		String index = getIndex(language);
		String type = elasticsearchProperties.getType();

		logger.info("Delete " + index + "/" + type + "/" + id);
		DeleteRequestBuilder deleteRequestBuilder = client.prepareDelete()
				.setIndex(index)
				.setType(type)
				.setId(id);

		return deleteRequestBuilder.get();
	}

	private String getIndex(String language){
		if(elasticsearchProperties.isIndexLanguageSuffix())
			return elasticsearchProperties.getIndex()+"-"+language;
		return elasticsearchProperties.getIndex();
	}
}
