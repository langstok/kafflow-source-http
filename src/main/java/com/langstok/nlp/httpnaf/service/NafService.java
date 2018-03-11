package com.langstok.nlp.httpnaf.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.langstok.nlp.httpnaf.configuration.properties.NafProperties;
import com.langstok.nlp.httpnaf.enumeration.SupportedLanguage;
import com.langstok.nlp.httpnaf.repository.ArticleRepository;
import com.langstok.nlp.httpnaf.web.dto.NafDto;
import ixa.kaflib.KAFDocument;
import org.apache.log4j.Logger;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.jdom2.JDOMException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
@EnableConfigurationProperties(NafProperties.class)
public class NafService {

    private static final Logger logger = Logger.getLogger(NafService.class);

    public static final String AUTHOR = "author";
    public static final String CREATION_TIME = "creationTime";
    public static final String FILE_NAME = "fileName";
    public static final String FILE_TYPE = "fileType";
    public static final String LANGUAGE = "language";
    public static final String LOCATION = "location";
    public static final String MAGAZINE = "magazine";
    public static final String KAF = "kaf";
    public static final String PAGES = "pages";
    public static final String PUBLISHER = "publisher";
    public static final String RAWTEXT = "rawText";
    public static final String SECTION = "section";
    public static final String TITLE = "title";
    public static final String URI = "uri";

    public static final String FILE_TYPE_HTTP = "HTTP";


    private static final String NAFVERSION = "v1.naf";
    private SimpleDateFormat dateFormat;

    private NafProperties nafProperties;
    private ArticleRepository articleRepository;
    private RetryTemplate retryTemplate;


    private Map<String, String> nafMapping;

    public NafService(ArticleRepository articleRepository, NafProperties nafProperties, RetryTemplate configuredRetry) {
        logger.info("Init naf service: " + nafProperties);
        this.articleRepository = articleRepository;

        this.nafProperties = nafProperties;
        this.dateFormat = new SimpleDateFormat(nafProperties.getCreationDateFormat());
        this.nafMapping = nafProperties.getMapping();
        this.retryTemplate = configuredRetry;
    }


    public KAFDocument create(NafDto dto) throws IOException, JDOMException {

        KAFDocument document = null;

        if (dto.getKaf() != null && !dto.getKaf().isEmpty()) {
            if (logger.isDebugEnabled()) logger.debug("KAF nr of lines: " + countLines(dto.getKaf()));
            document = KAFDocument.createFromStream(new StringReader(dto.getKaf()));
        } else {
            document = new KAFDocument(dto.getLanguage().name(), NAFVERSION);
            document.setRawText(dto.getRawText());

            document.createPublic();
            document.getPublic().uri = dto.getUri();
            document.getPublic().publicId = dto.getPublicId();

            document.createFileDesc();
            document.getFileDesc().creationtime = dateFormat.format(dto.getCreationtime());
            document.getFileDesc().location = dto.getLocation();
            document.getFileDesc().section = dto.getSection();
            document.getFileDesc().title = dto.getTitle();
            document.getFileDesc().magazine = dto.getMagazine();
            document.getFileDesc().publisher = dto.getPublisher();
            document.getFileDesc().author = dto.getAuthor();
            document.getFileDesc().pages = dto.getPages();
            document.getFileDesc().filename = dto.getFilename();
            document.getFileDesc().filetype = FILE_TYPE_HTTP;
        }

        logger.info("KAFDocument received (publicId / uri): " + document.getPublic().publicId + " / " + document.getPublic().uri);
        return document;
    }

    public NafDto mapArticleDocumentResponse(GetResponse response) throws IOException {

        final ObjectNode node = new ObjectMapper().readValue(response.getSourceAsString(), ObjectNode.class);
        NafDto dto = new NafDto();
        dto.setPublicId(response.getId());

        if (nafMapping.containsKey(AUTHOR) && node.has(nafMapping.get(AUTHOR))) {
            dto.setAuthor(node.get(AUTHOR).asText());
        }
        if (nafMapping.containsKey(CREATION_TIME) && node.has(nafMapping.get(CREATION_TIME))) {
            dto.setCreationtime(new Date(node.get(nafMapping.get(CREATION_TIME)).asLong()));
        }
        if (nafMapping.containsKey(FILE_NAME) && node.has(nafMapping.get(FILE_NAME))) {
            dto.setFilename(node.get(nafMapping.get(FILE_NAME)).asText());
        }
        if (nafMapping.containsKey(FILE_TYPE) && node.has(nafMapping.get(FILE_TYPE))) {
            dto.setFiletype(node.get(nafMapping.get(FILE_TYPE)).asText());
        }
        if (nafMapping.containsKey(LANGUAGE) && node.has(nafMapping.get(LANGUAGE))) {
            dto.setLanguage(SupportedLanguage.valueOf(node.get(nafMapping.get(LANGUAGE)).asText()));
        }
        if (nafMapping.containsKey(LOCATION) && node.has(nafMapping.get(LOCATION))) {
            dto.setLocation(node.get(nafMapping.get(LOCATION)).asText());
        }
        if (nafMapping.containsKey(MAGAZINE) && node.has(nafMapping.get(MAGAZINE))) {
            dto.setMagazine(node.get(nafMapping.get(MAGAZINE)).asText());
        }
        if (nafMapping.containsKey(KAF) && node.has(nafMapping.get(KAF))) {
            dto.setKaf(node.get(nafMapping.get(KAF)).asText());
        }
        if (nafMapping.containsKey(PAGES) && node.has(nafMapping.get(PAGES))) {
            dto.setPages(node.get(nafMapping.get(PAGES)).asInt());
        }
        if (nafMapping.containsKey(PUBLISHER) && node.has(nafMapping.get(PUBLISHER))) {
            dto.setPublisher(node.get(nafMapping.get(PUBLISHER)).asText());
        }
        if (nafMapping.containsKey(SECTION) && node.has(nafMapping.get(SECTION))) {
            dto.setSection(node.get(nafMapping.get(SECTION)).asText());
        }
        if (nafMapping.containsKey(TITLE) && node.has(nafMapping.get(TITLE))) {
            dto.setTitle(node.get(nafMapping.get(TITLE)).asText());
        }
        if (nafMapping.containsKey(RAWTEXT) && node.has(nafMapping.get(RAWTEXT))) {
            String text = node.get(nafMapping.get(RAWTEXT)).textValue();
            if (nafProperties.isAddTitleToText())
                dto.setRawText(mergeTitle(dto.getTitle(), text));
            else
                dto.setRawText(text);
        }
        if (nafMapping.containsKey(URI) && node.has(nafMapping.get(URI))) {
            dto.setUri(node.get(nafMapping.get(URI)).asText());
        }
        return dto;
    }

    private static int countLines(String str) {
        String[] lines = str.split("\r\n|\r|\n");
        return lines.length;
    }

    private static String mergeTitle(String title, String rawText) {
        return title + System.lineSeparator() + rawText;
    }

    public Path getKaf(String id, SupportedLanguage language) throws IOException {
        return articleRepository.getKaf(id, language);
    }


    public KAFDocument getKAFDocumentById(String id, String lang) throws Exception {
        return create(mapArticleDocumentResponse(articleRepository.getKAFDocumentById(id, lang)));
    }

    public GetResponse getKafDocumentByIdPoll(String id, String lang) throws Exception {
        return retryTemplate.execute(context -> articleRepository.getKAFDocumentByIdExeption(id, lang));
    }



    public DeleteResponse delete(String publicId, SupportedLanguage language) {
        return articleRepository.delete(publicId, language);
    }
}