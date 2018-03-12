package com.langstok.nafflow.httpnaf.service;

import ixa.kaflib.KAFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;

import java.util.Collections;

@Controller
@EnableBinding(Source.class)
public class HttpNafSource {

	private Source channels;

	public HttpNafSource(Source channels) {
		this.channels = channels;
	}

	public void sendMessage(KAFDocument kafDocument, Object contentType) {
		channels.output().send(MessageBuilder.createMessage(kafDocument,
				new MessageHeaders(Collections.singletonMap(MessageHeaders.CONTENT_TYPE, contentType))));
	}
}