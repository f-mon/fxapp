package it.fmoon.fxapp.support;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

import io.reactivex.Single;

@Component
public class HttpJsonClient {

	public <T> Single<T> sendAndReadValue(String uri,Object... path) {
		return sendAndRead(uri).map(map->{
			return PropertyUtils.getProperty(map, path);
		});
	}


	public Single<Map<String,Object>> sendAndRead(String uri) {
		try {
			return Single.fromFuture(sendAndRead(new URI(uri)));
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
	
	public CompletableFuture<Map<String,Object>> sendAndRead(URI uri) {
	    UncheckedObjectMapper objectMapper = new UncheckedObjectMapper();
	    HttpRequest request = HttpRequest.newBuilder(uri)
	          .header("Accept", "application/json")
	          .build();
	    return HttpClient.newHttpClient()
	          .sendAsync(request, BodyHandlers.ofString())
	          .thenApply(HttpResponse::body)
	          .thenApply(objectMapper::readValue);
	}


}
