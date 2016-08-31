package org.sriproxy;

import org.eclipse.jetty.client.HttpClient;

public class WebClient {
	
	
	private static  HttpClient client;

	public static HttpClient getClient() {
		return client;
	}

	public static void setClient(HttpClient httpclient) {
		client = httpclient;
	}

}
