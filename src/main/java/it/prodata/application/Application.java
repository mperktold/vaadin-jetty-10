package it.prodata.application;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
//@SpringBootApplication
@Theme(value = "vaadinjetty10")
@PWA(name = "Vaadin Jetty 10", shortName = "Vaadin Jetty 10", offlineResources = {"images/logo.png"})
@Push
public class Application /*extends SpringBootServletInitializer*/ implements AppShellConfigurator {

    public static void main(String[] args) {
        //LaunchUtil.launchBrowserInDevelopmentMode(SpringApplication.run(Application.class, args));
    }

}
