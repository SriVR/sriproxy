package org.sriproxy.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.sriproxy.client.ProxyClient;

public class ProxyHandler extends AbstractHandler
{
	
	@Override
    public void handle(String target,Request baseRequest,HttpServletRequest request,HttpServletResponse response) 
        throws IOException, ServletException
    {
		
		if(request.getMethod()=="OPTIONS")
		{
			baseRequest.setHandled(true);
			response.setStatus(HttpServletResponse.SC_OK);
			response.setHeader("Access-Control-Allow-Method", "GET, POST, OPTIONS, HEAD");
			response.setHeader("Access-Control-Allow-Headers", "authorization,Session-Token, confirmationpassword, Origin, Content-Type, Accept");
			response.setHeader("Access-Control-Allow-Origin", "*");
			
			return;

		}
		ProxyClient client = new ProxyClient();
		
		ContentResponse proxyResponse =client.getResponse(request, response);
		baseRequest.setHandled(true);
		response.setHeader("Access-Control-Allow-Origin", "*");
		if(proxyResponse==null)
		{
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("{\"code\": \"login\"}");
			return;
		}
		
		if(proxyResponse.getStatus()==200)
			response.setStatus(HttpServletResponse.SC_OK);
		
		response.setContentType(proxyResponse.getMediaType());
		
		response.getWriter().write(proxyResponse.getContentAsString());
    }

	
	
}