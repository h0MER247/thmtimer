package de.thm.mni.thmtimer.util;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
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

import android.util.Log;

public class Connection {
	
	private static String mBaseURI = "http://thmtimer.mni.thm.de:80";
	private static String mUsername;
	private static String mPassword;

	private static final String TAG = Connection.class.getSimpleName();

	
	/**
	 * Set the user name.
	 * 
	 * @param username	The user name used for authentication.
	 */
	public static void setUsername(String username) {
		
		mUsername = username;
	}
	
	/**
	 * Get the user name.
	 * 
	 * @return The user name used for authentication.
	 */
	public static String getUsername() {
		
		return mUsername;
	}
	
	/**
	 * Set the Password.
	 * 
	 * @param password	The user name used for authentication.
	 */
	public static void setPassword(String password) {
		
		mPassword = password;
	}
	
	/**
	 * Gets the Password.
	 * 
	 * @return The password used for authentication
	 */
	public static String getPassword() {
		
		return mPassword;
	}
	
	/**
	 * Make a server request.
	 * 
	 * @param url
	 *            The REST url. Domain name will be prepended. Must begin with a
	 *            slash.
	 * @param method
	 *            GET, POST, PUT, DELETE
	 * @param requestObject
	 *            The request object
	 * @param responseType
	 *            The type of the return value
	 * @throws HttpClientErrorException
	 *             When access is not allowed (4xx code)
	 * @throws ResourceAccessException
	 *             When an I/O Error occurs
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 * 
	 * @return The response body

	 */
	public static <T> T request(String url,
			                    HttpMethod method,
			                    Object requestObject,
			                    Class<T> responseType) throws ResourceAccessException,
			                                                  HttpClientErrorException  {
		
		if (mUsername == null || mPassword == null) {
			throw new IllegalStateException("Username and/or password not set");
		}
		if (!url.startsWith("/")) {
			throw new IllegalArgumentException("url must start with a forward slash!");
		}
		url = mBaseURI + url;

		Log.d(TAG, "URL:" + url);
		Log.d(TAG, "Method: " + method.toString());
		if(responseType != null)
			Log.d(TAG, "Response type: " + responseType.toString());

		// Populate the HTTP Basic Authentication header with the username and
		// password
		HttpAuthentication authHeader = new HttpBasicAuthentication(mUsername, mPassword);
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAuthorization(authHeader);
		HttpEntity<?> requestEntity = new HttpEntity<Object>(requestObject, requestHeaders);
					
		try {
			
			ObjectMapper mapper = new ObjectMapper();
			Log.d(TAG, "JSON:"+ mapper.writeValueAsString(requestObject));
		}
		catch (JsonGenerationException e) {}
		catch (JsonMappingException e) {}
		catch (IOException e) {}
	
		// Create a new RestTemplate instance
		RestTemplate restTemplate = new RestTemplate();
		
		// Add the String message converter
		restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
		
		// Send the request
		ResponseEntity<T> response = restTemplate.exchange(url, method, requestEntity, responseType);
		
		return response.getBody();
	}
	
	
	
	
	public static <T> T request(String url, HttpMethod method, Class<T> responseType) throws ResourceAccessException,
			HttpClientErrorException {
		if (mUsername == null || mPassword == null) {
			throw new IllegalStateException("Username and/or password not set");
		}
		if (!url.startsWith("/")) {
			throw new IllegalArgumentException("url must start with a forward slash!");
		}
		url = mBaseURI + url;

		Log.d(TAG, "URL:" + url);
		Log.d(TAG, "Method: " + method.toString());
		if(responseType != null)
			Log.d(TAG, "Response type: " + responseType.toString());

		// Populate the HTTP Basic Authentication header with the username and
		// password
		HttpAuthentication authHeader = new HttpBasicAuthentication(mUsername, mPassword);
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAuthorization(authHeader);
		HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
		
		// Create a new RestTemplate instance
		RestTemplate restTemplate = new RestTemplate();
		
		// Add the String message converter
		restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());

		// Send the request
		ResponseEntity<T> response = restTemplate.exchange(url, method, requestEntity, responseType);
		
		return response.getBody();
	}
}
