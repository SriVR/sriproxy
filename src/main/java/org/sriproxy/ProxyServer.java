package org.sriproxy;


import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.sriproxy.handler.ProxyHandler;

public class ProxyServer {
	
	public static void main(String[] args) throws Exception {
	   
		 Server server = new Server(8080);
		 
		 
		 SslContextFactory sslContextFactory = new SslContextFactory();
		
		 sslContextFactory.setKeyStorePath("/home/devsri/project/proxy/sriproxy/cert/sendco.jks");
		 sslContextFactory.setKeyStorePassword("sendcokey");
		 sslContextFactory.setKeyManagerPassword("sendcokey");
		 SslConnectionFactory sslConnectionFactory = new SslConnectionFactory(sslContextFactory, org.eclipse.jetty.http.HttpVersion.HTTP_1_1.toString());

		 HttpConfiguration config = new HttpConfiguration();
		 config.setSecureScheme("https");
		 config.setSecurePort(8443);
		 config.setOutputBufferSize(32786);
		 config.setRequestHeaderSize(8192);
		 config.setResponseHeaderSize(8192);
		 HttpConfiguration sslConfiguration = new HttpConfiguration(config);
		 sslConfiguration.addCustomizer(new SecureRequestCustomizer());
		 HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory(sslConfiguration);

		 ServerConnector connector = new ServerConnector(server, sslConnectionFactory, httpConnectionFactory);
		 connector.setPort(8443);  
		 server.addConnector(connector);
		 ContextHandler context = new ContextHandler();
		 context.setContextPath("/*");
         context.setResourceBase(".");
         context.setClassLoader(Thread.currentThread().getContextClassLoader());
         server.setHandler(context);
         context.setHandler(new ProxyHandler());
	     server.start();
	     server.join();
	     
	     
	     
	     HttpClient client = new HttpClient(new SslContextFactory());
	     
	     try {
				client.setMaxRequestsQueuedPerDestination(10);
				client.setMaxConnectionsPerDestination(200); 
				client.setConnectTimeout(30000); 
				client.start();
				
				WebClient.setClient(client);
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	}

}
