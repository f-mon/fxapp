package it.fmoon.fxapp.support;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletionException;

public class UncheckedObjectMapper extends com.fasterxml.jackson.databind.ObjectMapper {
	
    public Map<String,Object> readValue(String content) {
	    try {
	        return this.readerFor(Map.class).readValue(content);
	    } catch (IOException ioe) {
	        throw new CompletionException(ioe);
	    }
    }
    
}