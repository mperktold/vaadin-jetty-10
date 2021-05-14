# Vaadin Jetty 10

This project reproduces problems that occur when serving a Vaadin application with Jetty 10 over HTTPS.

The Jetty server is configured and started programmatically in [JettyMain](src/main/java/it/prodata/application/JettyMain.java).
Once started, the application can be accessed at `http://localhost:8080` and `https://localhost:8443`.

Note that a self-signed certificate is used for HTTPS, so the browser will warn about the certificate not being valid. This is expected for this application and must be ignored in order to test the application over HTTPS.

This project was created from https://start.vaadin.com.
