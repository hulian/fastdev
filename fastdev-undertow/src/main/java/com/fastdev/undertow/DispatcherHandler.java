package com.fastdev.undertow;

import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fastdev.core.dispatcher.Dispatcher;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.undercouch.bson4jackson.BsonFactory;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.form.FormData;
import io.undertow.server.handlers.form.FormDataParser;
import io.undertow.server.handlers.form.FormParserFactory;

public class DispatcherHandler  implements HttpHandler {

	private static final Logger logger = LoggerFactory.getLogger(DispatcherHandler.class);
	
	private final FormParserFactory formParserFactory;

	private Dispatcher dispatcher;
	
	private ObjectMapper bsonObjectMapper = new ObjectMapper(new BsonFactory());
	
	private ObjectMapper jsonObjectMapper = new ObjectMapper();
	
	public DispatcherHandler(Dispatcher dispatcher) {
		 this.jsonObjectMapper.setSerializationInclusion(Include.NON_NULL);
		 this.dispatcher = dispatcher;
		 this.formParserFactory = FormParserFactory.builder().build();
	}

	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {

		if (exchange.isInIoThread()) {
			exchange.dispatch(this);
			return;
		}

		exchange.startBlocking();
		try {

			Map<String, Object> params = new HashMap<>();
			if(exchange.getRequestMethod().equalToString("POST")){
				if(exchange.isBlocking()){
					FormDataParser parser = formParserFactory.createParser(exchange);
					FormData formData = parser.parseBlocking();
					Iterator<String> iterator = formData.iterator(); 
					while(iterator.hasNext()){
						String key = iterator.next();
						params.put(key,formData.get(key).getLast().getValue());
					}
				}
			}else{
				Iterator<Entry<String, Deque<String>>>  iterator = exchange.getQueryParameters().entrySet().iterator();
				while(iterator.hasNext()){
					Entry<String, Deque<String>> entry = iterator.next();
					params.put(entry.getKey(),entry.getValue().getLast());
				}
			}
			
			if(params.get("bson")!=null){
				bsonObjectMapper.writeValue(exchange.getOutputStream(), dispatcher.dispatch(params));
			}else{
				jsonObjectMapper.writeValue(exchange.getOutputStream(), dispatcher.dispatch(params));
			}
			
		} catch (Exception e) {
			logger.error("SERVER ERROR", e);
		}
		exchange.endExchange();

	}

}
