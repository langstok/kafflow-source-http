package com.langstok.kafflow.httpnaf.service;

import com.langstok.kafflow.httpnaf.configuration.properties.NafProperties;
import com.langstok.kafflow.httpnaf.repository.ArticleRepository;
import com.langstok.kafflow.httpnaf.web.dto.NafDto;
import ixa.kaflib.KAFDocument;
import org.apache.log4j.Logger;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.jdom2.JDOMException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.text.SimpleDateFormat;

@Service
@EnableConfigurationProperties(NafProperties.class)
public class NafService {

    private static final Logger logger = Logger.getLogger(NafService.class);

    private static final String FILE_TYPE_HTTP = "HTTP";
    private static final String NAFVERSION = KAFDocument.class.getClass().getPackage().getImplementationVersion();
    private static SimpleDateFormat dateFormat;

    private ArticleRepository articleRepository;
    private RetryTemplate retryTemplate;
    private NafProperties nafProperties;


    public NafService(ArticleRepository articleRepository, RetryTemplate configuredRetry, NafProperties nafProperties) {
        this.articleRepository = articleRepository;
        this.retryTemplate = configuredRetry;
         dateFormat = new SimpleDateFormat(nafProperties.getCreationDateFormat());
    }


    public KAFDocument create(MultipartFile multipartFile) throws IOException, JDOMException {
        return KAFDocument.createFromStream(new BufferedReader(new InputStreamReader(multipartFile.getInputStream())));
    }


    public KAFDocument create(NafDto dto) {

        KAFDocument document = new KAFDocument(dto.getLanguage(), NAFVERSION);
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

        logger.info("KAFDocument received (publicId / uri): " + document.getPublic().publicId + " / " + document.getPublic().uri);
        return document;
    }


    public Path getKaf(String id, String language) throws IOException {
        return articleRepository.getKaf(id, language);
    }

    public GetResponse getKafDocumentByIdPoll(String id, String lang) throws Exception {
        return retryTemplate.execute(context -> articleRepository.getKAFDocumentByIdExeption(id, lang));
    }

    public DeleteResponse delete(String publicId, String language){
        return articleRepository.delete(publicId, language);
    }

}