/*
 * JettyMain  2021-05-14
 *
 * Copyright (c) Pro Data GmbH & ASA KG. All rights reserved.
 */

package it.prodata.application;

import com.vaadin.flow.server.InitParameters;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.server.startup.RouteRegistryInitializer;

import org.atmosphere.cpr.ApplicationConfig;
import org.eclipse.jetty.alpn.server.ALPNServerConnectionFactory;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.http2.HTTP2Cipher;
import org.eclipse.jetty.http2.server.HTTP2CServerConnectionFactory;
import org.eclipse.jetty.http2.server.HTTP2ServerConnectionFactory;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * JettyMain
 *
 * @author Matthias Perktold
 * @since 2021-05-14
 */
public class JettyMain {

	public static void main(String[] args) throws Exception {
		var webApp = new WebAppContext();
		webApp.setBaseResource(Resource.newClassPathResource("/META-INF"));
		webApp.setAttribute(MetaInfConfiguration.CONTAINER_JAR_PATTERN, ".*");
		webApp.setConfigurationDiscovered(true);
		webApp.getServletContext().setExtendedListenerTypes(true);
		webApp.setThrowUnavailableOnStartupException(true);

		ServletHolder holder = webApp.addServlet(VaadinServlet.class, "/*");
		holder.setAsyncSupported(true);
//		holder.setInitParameter(InitParameters.SERVLET_PARAMETER_PRODUCTION_MODE	, "false");
//		holder.setInitParameter(InitParameters.SERVLET_PARAMETER_HEARTBEAT_INTERVAL	, "10");
		holder.setInitParameter(InitParameters.SERVLET_PARAMETER_ENABLE_PNPM		, Boolean.toString(true));
//		holder.setInitParameter(ApplicationConfig.BROADCASTER_ASYNC_WRITE_THREADPOOL_MAXSIZE, "-1");

		var server = new Server();
		server.setHandler(webApp);

		var httpConfig = new HttpConfiguration();
		var httpConnector = new ServerConnector(server, new HttpConnectionFactory(httpConfig), new HTTP2CServerConnectionFactory(httpConfig));
		server.addConnector(httpConnector);
		httpConnector.setPort(8080);

		var httpsConfig = new HttpConfiguration();
		httpsConfig.setSecureScheme("https");
		httpsConfig.setSecurePort(8443);
		httpsConfig.addCustomizer(new SecureRequestCustomizer(false));
		var h2 = new HTTP2ServerConnectionFactory(httpsConfig);

		var alpn = new ALPNServerConnectionFactory();
		alpn.setDefaultProtocol(HttpVersion.HTTP_1_1.asString());

		var ssl = new SslConnectionFactory(serverSslContextFactory(), alpn.getProtocol());

		var httpsConnector = new ServerConnector(server, ssl, alpn, h2, new HttpConnectionFactory(httpsConfig));
		httpsConnector.setPort(8443);
		server.addConnector(httpsConnector);

		server.start();
		server.join();
	}

	private static SslContextFactory.Server serverSslContextFactory() {
		var factory = new SslContextFactory.Server();
		factory.setKeyStorePath("./keystore.pfx");
		factory.setKeyStorePassword("OBF:1z0f1vu91vv11z0f");
		factory.setKeyStoreType("PKCS12");
		factory.setCipherComparator(HTTP2Cipher.COMPARATOR);
		factory.setProvider("Conscrypt");
		return factory;
	}
}
