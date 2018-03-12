package com.langstok.kafflow.httpnaf.web.dto;

import com.langstok.kafflow.httpnaf.enumeration.SupportedLanguage;

import java.util.Date;

public class NafDto {

	private String author = "";
	private String title = "";
	private String publisher = "";
	private String section = "";
	private String location = "";
	private String magazine = "";
	private String filename = "";
	private String filetype = "";
	private Integer pages = 0;
	private Date creationtime = new Date();
	
	private String publicId = "";
	private String uri = "";
	
	private String rawText = "";
	private SupportedLanguage language = SupportedLanguage.en;
	
	private String kaf;
	
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getMagazine() {
		return magazine;
	}
	public void setMagazine(String magazine) {
		this.magazine = magazine;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFiletype() {
		return filetype;
	}
	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}
	public Integer getPages() {
		return pages;
	}
	public void setPages(Integer pages) {
		this.pages = pages;
	}
	
	public Date getCreationtime() {
		return creationtime;
	}
	public void setCreationtime(Date creationtime) {
		this.creationtime = creationtime;
	}
	public String getPublicId() {
		return publicId;
	}
	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getRawText() {
		return rawText;
	}
	public void setRawText(String rawText) {
		this.rawText = rawText;
	}
	public SupportedLanguage getLanguage() {
		return language;
	}
	public void setLanguage(SupportedLanguage language) {
		this.language = language;
	}
	public String getKaf() {
		return kaf;
	}
	public void setKaf(String kaf) {
		this.kaf = kaf;
	}
	
}
