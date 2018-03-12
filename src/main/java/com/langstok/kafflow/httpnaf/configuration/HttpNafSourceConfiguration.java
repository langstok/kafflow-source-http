package com.langstok.kafflow.httpnaf.configuration;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;

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

	public void sendMessage(KAFDocument kafDocument, Object contentType) {
		channels.output().send(MessageBuilder.createMessage(kafDocument,
				new MessageHeaders(Collections.singletonMap(MessageHeaders.CONTENT_TYPE, contentType))));
	}
}