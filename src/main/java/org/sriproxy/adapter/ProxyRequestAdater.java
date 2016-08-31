package org.sriproxy.adapter;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.client.api.ContentProvider;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.InputStreamContentProvider;
import org.eclipse.jetty.http.HttpFields;
import org.eclipse.jetty.http.HttpMethod;
import static java.util.Collections.list;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProxyRequestAdater {
	
	private Request proxyRequest;
	private List<String> ignoreHeaders = Arrays.asList("Host");
	public ProxyRequestAdater(HttpServletRequest orgReq, Request proxyReq ) {
		
		this.proxyRequest =proxyReq;
		String orgMethod =orgReq.getMethod();
		if(orgMethod.equals("GET"))
		{
			proxyRequest.method(HttpMethod.GET);
			List<String> paramList =list(orgReq.getParameterNames());
			
			for(String paramMap: paramList)
				proxyRequest.param(paramMap, orgReq.getParameterValues(paramMap)[0]);
			
		}
		else if(orgMethod.equals("POST")){
			proxyRequest.method(HttpMethod.POST);
			ContentProvider conProv;
			try {
				conProv = new InputStreamContentProvider(orgReq.getInputStream());
				proxyRequest.content(conProv);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		HttpFields httpFields = proxyRequest.getHeaders();
		
		List<String> headerNames=list(orgReq.getHeaderNames());
		 for (String currentKey : headerNames) {
			 
			 if(ignoreHeaders.contains(currentKey))
				 continue;
			 
			 if (httpFields.containsKey(currentKey))
				 httpFields.put(currentKey, orgReq.getHeader(currentKey));
			 else
				 httpFields.add(currentKey, orgReq.getHeader(currentKey));
	        
		 }
 
		
	}

	
	public Request getProxyRequest()
	{
		return proxyRequest;
	}
}
