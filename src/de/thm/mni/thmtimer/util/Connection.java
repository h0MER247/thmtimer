package de.thm.mni.thmtimer.util;

import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import de.thm.mni.thmtimer.util.AbstractAsyncActivity;
import android.util.Log;

public class Connection {
	private static String base_uri = "http://thmtimer.mni.thm.de:80";
	public static String username;
	public static String password;

	protected static final String TAG = Connection.class.getSimpleName();

	/**
	 * Make a server request.
	 * 
	 * @param url
	 *            The REST url. Domain name will be prepended. Must begin with a
	 *            slash.
	 * @param method
	 *            GET, POST, PUT, DELETE
	 * @param responseType
	 *            The type of the return value
	 * @throws HttpClientErrorException
	 *             When access is not allowed (4xx code)
	 * @throws ResourceAccessException
	 *             When an I/O Error occurs
	 * 
	 * @return The response body
	 */
	public static <T> T request(String url, HttpMethod method, Class<T> responseType) throws ResourceAccessException,
			HttpClientErrorException {
		if (username == null || password == null) {
			throw new IllegalStateException("Username and/or password not set");
		}
		if (!url.startsWith("/")) {
			throw new IllegalArgumentException("url must start with a forward slash!");
		}
		url = base_uri + url;

		Log.d(TAG, "URL:" + url);
		Log.d(TAG, "Method: " + method.toString());
		Log.d(TAG, "Response type: " + responseType.toString());

		// Populate the HTTP Basic Authentitcation header with the username and
		// password
		HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAuthorization(authHeader);
		HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);

		// Create a new RestTemplate instance
		RestTemplate restTemplate = new RestTemplate();

		// Add the String message converter
		restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());

		// Send the request
		ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, responseType);
		return response.getBody();
	}
}
