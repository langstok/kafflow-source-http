package com.langstok.kafflow.sourcehttp.service;

import com.langstok.kafflow.sourcehttp.configuration.properties.NafProperties;
import com.langstok.kafflow.sourcehttp.repository.DocumentRepository;
import com.langstok.kafflow.sourcehttp.web.dto.NafDto;
import ixa.kaflib.KAFDocument;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.UUID;

@Service
@EnableConfigurationProperties(NafProperties.class)
public class NafService {

    private Logger logger = LoggerFactory.getLogger(NafService.class);

    private static final String FILE_TYPE_HTTP = "HTTP";
    private static final String NAFVERSION = KAFDocument.class.getClass().getPackage().getImplementationVersion();
    private static SimpleDateFormat dateFormat;

    private DocumentRepository documentRepository;
    private RetryTemplate retryTemplate;
    private NafProperties nafProperties;


    public NafService(DocumentRepository documentRepository, RetryTemplate configuredRetry, NafProperties nafProperties) {
        this.documentRepository = documentRepository;
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
        String id =  dto.getPublicId();
        if(id == null || id.isEmpty())
            id = UUID.randomUUID().toString();

        document.getPublic().publicId = id;

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
        return documentRepository.getKaf(id, language);
    }

    public GetResponse getKafDocumentByIdPoll(String id, String lang) throws Exception {
        return retryTemplate.execute(context -> documentRepository.getKAFDocumentByIdExeption(id, lang));
    }

    public DeleteResponse delete(String publicId, String language){
        return documentRepository.delete(publicId, language);
    }

}