package com.langstok.nlp.httpnaf.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.langstok.nlp.httpnaf.web.dto.NafDTO;

import ixa.kaflib.KAFDocument;

@Service
public class NafService {
	
	private final static Logger LOGGER = Logger.getLogger(NafService.class);
	
	private final static String NAFVERSION = "v1.naf";
	
	public KAFDocument create(NafDTO dto) {

		KAFDocument document = new KAFDocument(dto.getLanguage(), NAFVERSION);
		document.setRawText(dto.getRawText());		
	
		document.createPublic();
		document.getPublic().uri = dto.getUri();
		document.getPublic().publicId = dto.getPublicId();
				
		document.createFileDesc();
		document.getFileDesc().creationtime = dto.getCreationtime();
		document.getFileDesc().location = dto.getLocation();
		document.getFileDesc().section = dto.getSection();
		document.getFileDesc().title = dto.getTitle();
		document.getFileDesc().magazine = dto.getMagazine();
		document.getFileDesc().publisher = dto.getPublisher();
		document.getFileDesc().author = dto.getAuthor();
		document.getFileDesc().pages = dto.getPages();
		document.getFileDesc().filename = dto.getFilename();
		document.getFileDesc().filetype = dto.getFiletype();

		LOGGER.info("KAFDocument received (publicId / uri): " + document.getPublic().publicId + " / " + document.getPublic().uri);
		return document;
	}
}
