package org.sriproxy.client;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.sriproxy.WebClient;
import org.sriproxy.adapter.ProxyRequestAdater;




public class ProxyClient {
	
	
	
	String proxyUrl = "http://con.send.net.co/smartpayv02/api/";
	public ContentResponse getResponse(HttpServletRequest orgRequest, HttpServletResponse orgResponse) throws IOException
	{
		
		ContentResponse response =null;
		HttpClient client = WebClient.getClient();
		String uri =orgRequest.getRequestURI();
		Request proxyRequest = client.newRequest(proxyUrl+uri);
		
		ProxyRequestAdater proxyAdapter = new ProxyRequestAdater(orgRequest, proxyRequest);
		try {
			proxyRequest =proxyAdapter.getProxyRequest();
			 response =proxyRequest.send();
			
			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			return null;
			
		}
		return response;
	}

}
