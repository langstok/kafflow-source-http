package com.langstok.nlp.httpnafsource.configuration;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.langstok.nlp.httpnafsource.service.NafService;
import com.langstok.nlp.httpnafsource.web.dto.NafDTO;

import ixa.kaflib.KAFDocument;


/**
 * A source module that listens for HTTP requests and emits the body as a message payload.
 * If the Content-Type matches 'text/*' or 'application/json', the payload will be a String,
 * otherwise the payload will be a byte array.
 *
 * @author Eric Bottard
 * @author Mark Fisher
 * @author Marius Bogoevici
 */
@Controller
@EnableBinding(Source.class)
public class HttpNafSourceConfiguration {

	@Autowired
	private Source channels;
	
	@Autowired
	NafService nafService;

	
	@RequestMapping(path = "${http.pathPattern:/api/naf}", method = RequestMethod.POST, consumes = "*/*")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void handleRequest(
			@RequestBody NafDTO body, 
			@RequestHeader(HttpHeaders.CONTENT_TYPE) Object contentType) {
		sendMessage(nafService.create(body), contentType);
	}

	private void sendMessage(KAFDocument kafDocument, Object contentType) {
		channels.output().send(MessageBuilder.createMessage(kafDocument,
				new MessageHeaders(Collections.singletonMap(MessageHeaders.CONTENT_TYPE, contentType))));
	}
}