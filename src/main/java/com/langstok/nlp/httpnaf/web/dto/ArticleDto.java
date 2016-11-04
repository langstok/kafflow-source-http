package com.langstok.nlp.httpnaf.web.dto;

import java.util.Date;

public class ArticleDto {

	private String id;
	
	private String uri;	
			
	private String outletName;
	private String outletCountry;
	
	private String language;
	
	private String title;
	private String text;
	
	private Date dateRetrieved;
	

	public String getId() {
		return id;
	}




	public void setId(String id) {
		this.id = id;
	}




	public String getUri() {
		return uri;
	}




	public void setUri(String uri) {
		this.uri = uri;
	}




	public String getOutletName() {
		return outletName;
	}




	public void setOutletName(String outletName) {
		this.outletName = outletName;
	}




	public String getOutletCountry() {
		return outletCountry;
	}




	public void setOutletCountry(String outletCountry) {
		this.outletCountry = outletCountry;
	}




	public String getLanguage() {
		return language;
	}




	public void setLanguage(String language) {
		this.language = language;
	}




	public String getTitle() {
		return title;
	}




	public void setTitle(String title) {
		this.title = title;
	}




	public String getText() {
		return text;
	}




	public void setText(String text) {
		this.text = text;
	}




	public Date getDateRetrieved() {
		return dateRetrieved;
	}




	public void setDateRetrieved(Date dateRetrieved) {
		this.dateRetrieved = dateRetrieved;
	}




	@Override
	public String toString() {
		return "ArticleDto [uri=" + uri + ", title=" + title + "]";
	}
	
}
