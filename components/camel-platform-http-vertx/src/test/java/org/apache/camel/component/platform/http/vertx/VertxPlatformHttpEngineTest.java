/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.platform.http.vertx;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import jakarta.activation.DataHandler;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.Cookie;
import io.vertx.core.http.impl.ServerCookie;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.SocketAddress;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.auth.properties.PropertyFileAuthentication;
import io.vertx.ext.web.handler.BasicAuthHandler;
import org.apache.camel.CamelContext;
import org.apache.camel.Message;
import org.apache.camel.attachment.AttachmentMessage;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.platform.http.HttpEndpointModel;
import org.apache.camel.component.platform.http.PlatformHttpComponent;
import org.apache.camel.component.platform.http.spi.Method;
import org.apache.camel.component.platform.http.spi.PlatformHttpEngine;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.apache.camel.spi.EmbeddedHttpService;
import org.apache.camel.spi.RestConfiguration;
import org.apache.camel.support.jsse.KeyManagersParameters;
import org.apache.camel.support.jsse.KeyStoreParameters;
import org.apache.camel.support.jsse.SSLContextParameters;
import org.apache.camel.support.jsse.SSLContextServerParameters;
import org.apache.camel.support.jsse.TrustManagersParameters;
import org.apache.camel.test.AvailablePortFinder;
import org.apache.hc.client5.http.utils.Base64;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class VertxPlatformHttpEngineTest {
    public static SSLContextParameters serverSSLParameters;
    public static SSLContextParameters clientSSLParameters;

    @BeforeAll
    public static void setUp() {
        serverSSLParameters = new SSLContextParameters();
        clientSSLParameters = new SSLContextParameters();

        KeyStoreParameters keystoreParameters = new KeyStoreParameters();
        keystoreParameters.setResource("jsse/service.jks");
        keystoreParameters.setPassword("security");

        KeyManagersParameters serviceSSLKeyManagers = new KeyManagersParameters();
        serviceSSLKeyManagers.setKeyPassword("security");
        serviceSSLKeyManagers.setKeyStore(keystoreParameters);

        serverSSLParameters.setKeyManagers(serviceSSLKeyManagers);

        KeyStoreParameters truststoreParameters = new KeyStoreParameters();
        truststoreParameters.setResource("jsse/truststore.jks");
        truststoreParameters.setPassword("storepass");

        TrustManagersParameters clientAuthServiceSSLTrustManagers = new TrustManagersParameters();
        clientAuthServiceSSLTrustManagers.setKeyStore(truststoreParameters);
        serverSSLParameters.setTrustManagers(clientAuthServiceSSLTrustManagers);
        SSLContextServerParameters clientAuthSSLContextServerParameters = new SSLContextServerParameters();
        clientAuthSSLContextServerParameters.setClientAuthentication("REQUIRE");
        serverSSLParameters.setServerParameters(clientAuthSSLContextServerParameters);

        TrustManagersParameters clientSSLTrustManagers = new TrustManagersParameters();
        clientSSLTrustManagers.setKeyStore(truststoreParameters);
        clientSSLParameters.setTrustManagers(clientSSLTrustManagers);

        KeyManagersParameters clientAuthClientSSLKeyManagers = new KeyManagersParameters();
        clientAuthClientSSLKeyManagers.setKeyPassword("security");
        clientAuthClientSSLKeyManagers.setKeyStore(keystoreParameters);
        clientSSLParameters.setKeyManagers(clientAuthClientSSLKeyManagers);
    }

    @Test
    public void testEngineSetup() throws Exception {
        final CamelContext context = createCamelContext();
        try {
            context.start();

            assertThat(VertxPlatformHttpRouter.lookup(context, VertxPlatformHttpRouter.getRouterNameFromPort(RestAssured.port)))
                    .isNotNull();
            assertThat(context.getComponent("platform-http")).isInstanceOfSatisfying(PlatformHttpComponent.class, component -> {
                assertThat(component.getEngine()).isInstanceOf(VertxPlatformHttpEngine.class);
            });

        } finally {
            context.stop();
        }
    }

    @Test
    public void testEngine() throws Exception {
        final CamelContext context = createCamelContext();
        VertxPlatformHttpServer platformHttpServer;

        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("platform-http:/get")
                            .routeId("get")
                            .setBody().constant("get");
                    from("platform-http:/post")
                            .routeId("post")
                            .transform().body(String.class, b -> b.toUpperCase());
                }
            });

            context.start();

            given()
                    .when()
                    .get("/get")
                    .then()
                    .statusCode(200)
                    .body(equalTo("get"));

            given()
                    .body("post")
                    .when()
                    .post("/post")
                    .then()
                    .statusCode(200)
                    .body(equalTo("POST"));

            PlatformHttpComponent phc = context.getComponent("platform-http", PlatformHttpComponent.class);
            assertEquals(2, phc.getHttpEndpoints().size());
            Iterator<HttpEndpointModel> it = phc.getHttpEndpoints().iterator();
            assertEquals("/get", it.next().getUri());
            assertEquals("/post", it.next().getUri());

            // should find engine in registry
            assertNotNull(context.getRegistry().findSingleByType(PlatformHttpEngine.class));
            EmbeddedHttpService server = context.getRegistry().findSingleByType(EmbeddedHttpService.class);
            assertNotNull(server);
            assertEquals("http", server.getScheme());
            assertEquals(RestAssured.port, server.getServerPort());

            platformHttpServer = context.hasService(VertxPlatformHttpServer.class);
            assertNotNull(platformHttpServer.getVertx());
        } finally {
            context.stop();
        }

        assertNull(platformHttpServer.getVertx());
    }

    @Test
    public void testSlowConsumer() throws Exception {
        final CamelContext context = createCamelContext();

        try {
            context.getRegistry().bind(
                    "vertx-options",
                    new VertxOptions()
                            .setMaxEventLoopExecuteTime(2)
                            .setMaxEventLoopExecuteTimeUnit(TimeUnit.SECONDS));

            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("platform-http:/get")
                            .routeId("get")
                            .process(e -> Thread.sleep(TimeUnit.SECONDS.toMillis(3)))
                            .setBody().constant("get");
                }
            });

            context.start();

            given()
                    .when()
                    .get("/get")
                    .then()
                    .statusCode(200)
                    .body(equalTo("get"));

        } finally {
            context.stop();
        }
    }

    @Test
    public void testSlowConsumerWithTimeout() throws Exception {
        final CamelContext context = createCamelContext();

        try {
            context.getRegistry().bind(
                    "vertx-options",
                    new VertxOptions()
                            .setMaxEventLoopExecuteTime(2)
                            .setMaxEventLoopExecuteTimeUnit(TimeUnit.SECONDS));

            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("platform-http:/get?requestTimeout=500")
                            .routeId("get")
                            .delay(1000)
                            .setBody().constant("get");
                }
            });

            context.start();

            given()
                    .when()
                    .get("/get")
                    .then()
                    .statusCode(503);

        } finally {
            context.stop();
        }
    }

    @Test
    public void testTimeoutNotExceeded() throws Exception {
        final CamelContext context = createCamelContext();

        String response = "Request did not time out";
        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("platform-http:/get?requestTimeout=500")
                            .routeId("get")
                            .setBody().constant(response);
                }
            });

            context.start();

            given()
                    .when()
                    .get("/get")
                    .then()
                    .statusCode(200)
                    .body(equalTo(response));

        } finally {
            context.stop();
        }
    }

    @Test
    public void testFailingConsumer() throws Exception {
        final CamelContext context = createCamelContext();

        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("platform-http:/get")
                            .routeId("get")
                            .process(exchange -> {
                                throw new RuntimeException();
                            });
                }
            });

            context.start();

            given()
                    .when()
                    .get("/get")
                    .then()
                    .statusCode(500);

        } finally {
            context.stop();
        }
    }

    @Test
    public void testEngineSSL() throws Exception {
        final CamelContext context
                = createCamelContext(configuration -> configuration.setSslContextParameters(serverSSLParameters));

        try {
            context.getRegistry().bind("clientSSLContextParameters", clientSSLParameters);
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("platform-http:/")
                            .transform().body(String.class, b -> b.toUpperCase());
                }
            });

            context.start();

            String result = context.createFluentProducerTemplate()
                    .toF("https://localhost:%d?sslContextParameters=#clientSSLContextParameters", RestAssured.port)
                    .withBody("test")
                    .request(String.class);

            assertThat(result).isEqualTo("TEST");
        } finally {
            context.stop();
        }
    }

    @Test
    public void testEngineGlobalSSL() throws Exception {
        final CamelContext context = createCamelContext(configuration -> configuration.setUseGlobalSslContextParameters(true));

        try {
            context.setSSLContextParameters(serverSSLParameters);
            context.getRegistry().bind("clientSSLContextParameters", clientSSLParameters);

            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("platform-http:/")
                            .transform().body(String.class, b -> b.toUpperCase());
                }
            });

            context.start();

            String result = context.createFluentProducerTemplate()
                    .toF("https://localhost:%d?sslContextParameters=#clientSSLContextParameters", RestAssured.port)
                    .withBody("test")
                    .request(String.class);

            assertThat(result).isEqualTo("TEST");
        } finally {
            context.stop();
        }
    }

    @Test
    public void testEngineCORS() throws Exception {
        final CamelContext context = createCamelContext(configuration -> {
            configuration.getCors().setEnabled(true);
            configuration.getCors().setMethods(Arrays.asList("GET", "POST"));
        });

        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("platform-http:/")
                            .transform().constant("cors");
                }
            });

            context.start();

            final String origin = "http://custom.origin.quarkus";
            final String methods = "GET,POST";
            final String headers = "X-Custom";

            given()
                    .header("Origin", origin)
                    .header("Access-Control-Request-Method", methods)
                    .header("Access-Control-Request-Headers", headers)
                    .when()
                    .get("/")
                    .then()
                    .statusCode(200)
                    .header("Access-Control-Allow-Origin", origin)
                    .header("Access-Control-Allow-Methods", methods)
                    .header("Access-Control-Allow-Headers", headers);
        } finally {
            context.stop();
        }
    }

    @Test
    public void testMatchOnUriPrefix() throws Exception {
        final CamelContext context = createCamelContext();
        try {
            final String greeting = "Hello Camel";
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("platform-http:/greeting/{name}?matchOnUriPrefix=true")
                            .transform().simple("Hello ${header.name}");
                }
            });

            context.start();

            given()
                    .when()
                    .get("/greeting")
                    .then()
                    .statusCode(404);

            given()
                    .when()
                    .get("/greeting/Camel")
                    .then()
                    .statusCode(200)
                    .body(equalTo(greeting));

            given()
                    .when()
                    .get("/greeting/Camel/other/path/")
                    .then()
                    .statusCode(200)
                    .body(equalTo(greeting));
        } finally {
            context.stop();
        }
    }

    @Test
    public void testMultipleFileUpload() throws Exception {
        final List<String> attachmentIds = List.of("myFirstTestFile", "mySecondTestFile");

        String tmpDirectory = null;
        List<File> tempFiles = new ArrayList<>(attachmentIds.size());
        for (String attachmentId : attachmentIds) {
            final String fileContent = "Test multipart upload content " + attachmentId;
            File tempFile;
            if (tmpDirectory == null) {
                tempFile = File.createTempFile("platform-http-" + attachmentId, ".txt");
            } else {
                tempFile = File.createTempFile("platform-http-" + attachmentId, ".txt", new File(tmpDirectory));
            }

            tempFiles.add(tempFile);
            tmpDirectory = tempFile.getParent();
            Files.write(tempFile.toPath(), fileContent.getBytes(StandardCharsets.UTF_8));
        }

        final CamelContext context = createCamelContext(configuration -> {
            VertxPlatformHttpServerConfiguration.BodyHandler bodyHandler
                    = new VertxPlatformHttpServerConfiguration.BodyHandler();
            // turn on file uploads
            bodyHandler.setHandleFileUploads(true);
            bodyHandler.setUploadsDirectory(tempFiles.get(0).getParent());
            configuration.setBodyHandler(bodyHandler);
        });

        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("platform-http:/upload")
                            .process(exchange -> {
                                AttachmentMessage message = exchange.getMessage(AttachmentMessage.class);

                                String result = "";
                                for (String attachmentId : attachmentIds) {
                                    DataHandler attachment = message.getAttachment(attachmentId);
                                    result += attachment.getContent();
                                }

                                exchange.getIn().setHeader("ConcatFileContent", result);
                            })
                            .setHeader("UploadedAttachments", simple("${headers.CamelAttachmentsSize}"));
                }
            });

            context.start();

            given()
                    .multiPart(attachmentIds.get(0), tempFiles.get(0))
                    .multiPart(attachmentIds.get(1), tempFiles.get(1))
                    .when()
                    .post("/upload")
                    .then()
                    .statusCode(204)
                    .body(emptyOrNullString())
                    .header("UploadedAttachments", is(String.valueOf(attachmentIds.size())))
                    .header("ConcatFileContent",
                            is("Test multipart upload content myFirstTestFileTest multipart upload content mySecondTestFile"));
        } finally {
            context.stop();
        }
    }

    @Test
    public void testSingleFileUpload() throws Exception {
        final String attachmentId = "myTestFile";
        final String fileContent = "Test multipart upload content";
        final File tempFile = File.createTempFile("platform-http", ".txt");
        final CamelContext context = createCamelContext(configuration -> {
            VertxPlatformHttpServerConfiguration.BodyHandler bodyHandler
                    = new VertxPlatformHttpServerConfiguration.BodyHandler();
            // turn on file uploads
            bodyHandler.setHandleFileUploads(true);
            bodyHandler.setUploadsDirectory(tempFile.getParent());
            configuration.setBodyHandler(bodyHandler);
        });

        try {
            Files.write(tempFile.toPath(), fileContent.getBytes(StandardCharsets.UTF_8));

            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("platform-http:/upload")
                            .process(exchange -> {
                                AttachmentMessage message = exchange.getMessage(AttachmentMessage.class);
                                DataHandler attachment = message.getAttachment(attachmentId);
                                exchange.getMessage().setHeader("myDataHandler", attachment);
                            })
                            .setHeader("UploadedFileContentType", simple("${header.CamelFileContentType}"))
                            .setHeader("UploadedFileSize", simple("${header.CamelFileLength}"));
                }
            });

            context.start();

            given()
                    .multiPart(attachmentId, tempFile)
                    .when()
                    .post("/upload")
                    .then()
                    .statusCode(200)
                    .header("myDataHandler", containsString("jakarta.activation.DataHandler"))
                    .header("UploadedFileContentType", is("text/plain"))
                    .header("UploadedFileSize", is(String.valueOf(fileContent.getBytes().length)))
                    .body(is(fileContent));
        } finally {
            context.stop();
        }
    }

    @Test
    public void testFormPost() throws Exception {
        final CamelContext context = createCamelContext();

        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("platform-http:/form/post")
                            .convertBodyTo(String.class);
                }
            });

            context.start();

            given()
                    .formParam("foo", "bar")
                    .formParam("cheese", "wine")
                    .when()
                    .post("/form/post")
                    .then()
                    .statusCode(200)
                    .body(is("{foo=bar, cheese=wine}"));
        } finally {
            context.stop();
        }
    }

    @Test
    public void testTextContentPost() throws Exception {
        final CamelContext context = createCamelContext();

        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("platform-http:/text/post")
                            .log("POST:/test/post has body ${body}");
                }
            });

            context.start();

            String payload = "Hello World";
            given()
                    .contentType(ContentType.TEXT)
                    .body(payload)
                    .when()
                    .post("/text/post")
                    .then()
                    .statusCode(200)
                    .body(is(payload));
        } finally {
            context.stop();
        }
    }

    @Test
    public void testRestCORSWitchConsumes() throws Exception {
        final CamelContext context = createCamelContext();

        try {
            context.addRoutes(new RouteBuilder() {

                @Override
                public void configure() {
                    restConfiguration().component("platform-http").enableCORS(true);

                    rest("/rest")
                            .post()
                            .consumes("application/json")
                            .to("direct:rest");

                    from("direct:rest")
                            .setBody(simple("Hello ${body}"));
                }
            });

            context.start();

            final String origin = "http://custom.origin.quarkus";

            given()
                    .header("Origin", origin)
                    .when()
                    .options("/rest")
                    .then()
                    .statusCode(204)
                    .header("Access-Control-Allow-Origin", RestConfiguration.CORS_ACCESS_CONTROL_ALLOW_ORIGIN)
                    .header("Access-Control-Allow-Methods", RestConfiguration.CORS_ACCESS_CONTROL_ALLOW_METHODS)
                    .header("Access-Control-Allow-Headers", RestConfiguration.CORS_ACCESS_CONTROL_ALLOW_HEADERS)
                    .header("Access-Control-Max-Age", RestConfiguration.CORS_ACCESS_CONTROL_MAX_AGE);
        } finally {
            context.stop();
        }
    }

    @Test
    public void testBodyClientRequestValidation() throws Exception {
        final CamelContext context = createCamelContext();

        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    restConfiguration().component("platform-http");

                    rest("/rest")
                            .post("/validate/body")
                            .clientRequestValidation(true)
                            .param().name("body").type(RestParamType.body).required(true).endParam()
                            .to("direct:rest");
                    from("direct:rest")
                            .setBody(simple("Hello ${body}"));
                }
            });

            context.start();

            given()
                    .when()
                    .post("/rest/validate/body")
                    .then()
                    .statusCode(400)
                    .body(is("The request body is missing."));

            given()
                    .body(" ")
                    .when()
                    .post("/rest/validate/body")
                    .then()
                    .statusCode(400)
                    .body(is("The request body is missing."));

            given()
                    .body("Camel Platform HTTP Vert.x")
                    .when()
                    .post("/rest/validate/body")
                    .then()
                    .statusCode(200)
                    .body(is("Hello Camel Platform HTTP Vert.x"));
        } finally {
            context.stop();
        }
    }

    @Test
    public void testUserAuthentication() throws Exception {
        Vertx vertx = Vertx.vertx();
        AuthenticationProvider authProvider = PropertyFileAuthentication.create(vertx, "authentication/auth.properties");
        BasicAuthHandler basicAuthHandler = BasicAuthHandler.create(authProvider);

        CamelContext context = createCamelContext();
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("platform-http:/secure")
                        .process(exchange -> {
                            Message message = exchange.getMessage();
                            message.setBody("Received message with the Authorization="
                                            + exchange.getMessage().getHeader("Authorization"));

                            User user = message.getHeader(VertxPlatformHttpConstants.AUTHENTICATED_USER, User.class);
                            assertThat(user).isNotNull();

                            JsonObject principal = user.principal();
                            assertThat(principal).isNotNull();
                            assertThat(principal.getString("username")).isEqualTo("camel");
                        });
            }
        });

        context.getRegistry().bind("vertx", vertx);

        try {
            context.start();

            VertxPlatformHttpRouter router
                    = VertxPlatformHttpRouter.lookup(context, VertxPlatformHttpRouter.getRouterNameFromPort(RestAssured.port));
            router.route().order(0).handler(basicAuthHandler);

            RestAssured.get("/secure")
                    .then()
                    .statusCode(401);

            RestAssured.given()
                    .auth()
                    .basic("camel", "s3cr3t")
                    .get("/secure")
                    .then()
                    .statusCode(200)
                    .body(is("Received message with the Authorization=Basic "
                             + Base64.encodeBase64String("camel:s3cr3t".getBytes())));
        } finally {
            context.stop();
            vertx.close();
        }
    }

    @Test
    public void testRequestBodyAllowed() throws Exception {
        final CamelContext context = createCamelContext();

        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("platform-http:/echo")
                            .setBody().simple("${body}");
                }
            });

            context.start();

            for (Method method : Method.values()) {
                ValidatableResponse validatableResponse = given()
                        .contentType(ContentType.JSON)
                        .when()
                        .body("{\"method\": \"" + method + "\"}")
                        .request(method.name(), "/echo")
                        .then()
                        .statusCode(200);

                Matcher<String> expectedBody;
                if (method.equals(Method.HEAD)) {
                    // HEAD response body is ignored
                    validatableResponse.body(emptyString());
                } else {
                    validatableResponse.body("method", equalTo(method.name()));
                }
            }
        } finally {
            context.stop();
        }
    }

    @Test
    public void testRequestBodyAllowedFormUrlEncoded() throws Exception {
        // Methods that are allowed a request body by Vert.x web for application/x-www-form-urlencoded
        final List<Method> methodsWithBodyAllowed = List.of(Method.POST, Method.PUT, Method.PATCH, Method.DELETE);
        final CamelContext context = createCamelContext();

        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("platform-http:/test")
                            .setBody().simple("Hello ${body[method]}");
                }
            });

            context.start();

            RequestSpecification request = given()
                    .when()
                    .contentType(ContentType.URLENC);

            for (Method method : Method.values()) {
                if (methodsWithBodyAllowed.contains(method)) {
                    request.body("method=" + method)
                            .request(method.name(), "/test")
                            .then()
                            .statusCode(200)
                            .body(equalTo("Hello " + method));
                } else {
                    request.body(method)
                            .request(method.name(), "/test")
                            .then()
                            .statusCode(500);
                }
            }
        } finally {
            context.stop();
        }
    }

    @Test
    public void responseHeaders() throws Exception {
        final CamelContext context = createCamelContext();

        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("platform-http:/test")
                            .setHeader("nonEmptyFromRoute", constant("nonEmptyFromRouteValue"))
                            .setHeader("emptyFromRoute", constant(""))
                            .setBody().simple("Hello World");
                }
            });

            context.start();

            RestAssured.given()
                    .header("nonEmpty", "nonEmptyValue")
                    .header("empty", "")
                    .get("/test")
                    .then()
                    .statusCode(200)
                    .body(equalTo("Hello World"))
                    .header("nonEmpty", "nonEmptyValue")
                    .header("empty", "")
                    .header("nonEmptyFromRoute", "nonEmptyFromRouteValue")
                    .header("emptyFromRoute", "");
        } finally {
            context.stop();
        }
    }

    @Test
    public void responseMultipleHeaders() throws Exception {
        final CamelContext context = createCamelContext();

        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("platform-http:/test")
                            .setHeader("nonEmptyFromRoute", constant("nonEmptyFromRouteValue"))
                            .setBody().simple("Hello World");
                }
            });

            context.start();

            RestAssured.given()
                    .header("nonEmpty", "nonEmptyValue")
                    .header("empty", "")
                    .get("/test?duplicated=1&duplicated=2")
                    .then()
                    .statusCode(200)
                    .body(equalTo("Hello World"))
                    .header("nonEmpty", "nonEmptyValue")
                    .header("nonEmptyFromRoute", "nonEmptyFromRouteValue");
        } finally {
            context.stop();
        }
    }

    @Test
    public void testConsumerSuspended() throws Exception {
        final CamelContext context = createCamelContext();

        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("platform-http:/get")
                            .routeId("get")
                            .setBody().constant("get");
                }
            });

            context.start();

            given()
                    .when()
                    .get("/get")
                    .then()
                    .statusCode(200)
                    .body(equalTo("get"));

            context.getRouteController().suspendRoute("get");

            given()
                    .when()
                    .get("/get")
                    .then()
                    .statusCode(503);

        } finally {
            context.stop();
        }
    }

    @Test
    public void testInvalidContentTypeClientRequestValidation() throws Exception {
        final CamelContext context = createCamelContext();

        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    restConfiguration()
                            .component("platform-http")
                            .bindingMode(RestBindingMode.json)
                            .clientRequestValidation(true);

                    rest("/rest")
                            .post("/validate/body").consumes("text/plain").produces("application/json")
                            .to("direct:rest");
                    from("direct:rest")
                            .setBody(simple("Hello ${body}"));
                }
            });

            context.start();

            given()
                    .when()
                    .body("{\"name\": \"Donald\"}")
                    .contentType("application/json")
                    .post("/rest/validate/body")
                    .then()
                    .statusCode(415);
        } finally {
            context.stop();
        }
    }

    @Test
    public void testLocalAddressHeader() throws Exception {
        final CamelContext context = createCamelContext();

        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("platform-http:/local/address")
                            .process(exchange -> {
                                Message message = exchange.getMessage();
                                SocketAddress address
                                        = message.getHeader(VertxPlatformHttpConstants.LOCAL_ADDRESS, SocketAddress.class);
                                message.setBody(address.hostAddress());
                            });
                }
            });

            context.start();

            get("/local/address")
                    .then()
                    .statusCode(200)
                    .body(notNullValue());
        } finally {
            context.stop();
        }
    }

    @Test
    public void testRemoteAddressHeader() throws Exception {
        final CamelContext context = createCamelContext();

        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("platform-http:/remote/address")
                            .process(exchange -> {
                                Message message = exchange.getMessage();
                                SocketAddress address
                                        = message.getHeader(VertxPlatformHttpConstants.REMOTE_ADDRESS, SocketAddress.class);
                                message.setBody(address.hostAddress());
                            });
                }
            });

            context.start();

            get("/remote/address")
                    .then()
                    .statusCode(200)
                    .body(notNullValue());
        } finally {
            context.stop();
        }
    }

    @Test
    public void testVertxRequestResponseObjects() throws Exception {
        final CamelContext context = createCamelContext();

        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("platform-http:/vertx/objects")
                            .process(exchange -> {
                                HttpMessage message = exchange.getMessage(HttpMessage.class);
                                String p = message.getRequest().path();
                                message.getResponse().putHeader("beer", "Heineken");
                                message.setBody("request path: " + p);
                            });
                }
            });

            context.start();

            get("/vertx/objects")
                    .then()
                    .statusCode(200)
                    .header("beer", "Heineken")
                    .body(is("request path: /vertx/objects"));
        } finally {
            context.stop();
        }
    }

    @Test
    public void testAddCookie() throws Exception {
        final CamelContext context = createCamelContext();

        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("platform-http:/add")
                            .process(exchange -> {
                                HttpMessage message = (HttpMessage) exchange.getMessage();
                                message.getRequest().response().addCookie(Cookie.cookie("foo", "bar"));
                            })
                            .setBody().constant("add");
                }
            });

            context.start();

            given()
                    .header("cookie", "foo=bar")
                    .when()
                    .get("/add")
                    .then()
                    .statusCode(200)
                    .header("set-cookie", "foo=bar")
                    .body(equalTo("add"));

        } finally {
            context.stop();
        }
    }

    @Test
    public void testRemoveCookie() throws Exception {
        final CamelContext context = createCamelContext();

        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("platform-http:/remove")
                            .process(exchange -> {
                                HttpMessage message = (HttpMessage) exchange.getMessage();
                                Cookie removed = message.getRequest().response().removeCookie("foo");
                                assertNotNull(removed);
                                assertEquals("foo", removed.getName());
                                assertEquals("", removed.getValue());
                            })
                            .setBody().constant("remove");
                }
            });

            context.start();

            given()
                    .header("cookie", "foo=bar")
                    .when()
                    .get("/remove")
                    .then()
                    .statusCode(200)
                    .header("set-cookie", startsWith("foo=; Max-Age=0; Expires="))
                    .body(equalTo("remove"));

        } finally {
            context.stop();
        }
    }

    @Test
    public void testReplaceCookie() throws Exception {
        final CamelContext context = createCamelContext();

        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("platform-http:/replace")
                            .process(exchange -> {
                                HttpMessage message = (HttpMessage) exchange.getMessage();
                                assertEquals(1, message.getRequest().cookieCount());
                                message.getRequest().response()
                                        .addCookie(Cookie.cookie("XSRF-TOKEN", "88533580000c314").setPath("/"));
                                Map<String, Cookie> deprecatedMap = message.getRequest().cookieMap();
                                assertFalse(((ServerCookie) deprecatedMap.get("XSRF-TOKEN")).isFromUserAgent());
                                assertEquals("/", deprecatedMap.get("XSRF-TOKEN").getPath());
                            })
                            .setBody().constant("replace");
                }
            });

            context.start();

            given()
                    .header("cookie", "XSRF-TOKEN=c359b44aef83415")
                    .when()
                    .get("/replace")
                    .then()
                    .statusCode(200)
                    .header("set-cookie", "XSRF-TOKEN=88533580000c314; Path=/")
                    .body(equalTo("replace"));

        } finally {
            context.stop();
        }
    }

    @Test
    public void testResponseTypeConversionErrorHandled() throws Exception {
        final CamelContext context = createCamelContext();

        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("platform-http:/error/response")
                            // Set the response to something that can't be type converted
                            .setBody().constant(Collections.EMPTY_SET);
                }
            });

            context.start();

            get("/error/response")
                    .then()
                    .statusCode(500);
        } finally {
            context.stop();
        }
    }

    @Test
    public void testResponseBadQueryParamErrorHandled() throws Exception {
        final CamelContext context = createCamelContext();

        try {
            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() {
                    from("platform-http:/error/response")
                            .setBody().constant("Error");
                }
            });

            context.start();

            // Add a query param that Vert.x cannot handle
            get("/error/response?::")
                    .then()
                    .statusCode(500);
        } finally {
            context.stop();
        }
    }

    static CamelContext createCamelContext() throws Exception {
        return createCamelContext(null);
    }

    static CamelContext createCamelContext(ServerConfigurationCustomizer customizer) throws Exception {
        int port = AvailablePortFinder.getNextAvailable();
        VertxPlatformHttpServerConfiguration conf = new VertxPlatformHttpServerConfiguration();
        conf.setBindPort(port);

        RestAssured.port = port;

        if (customizer != null) {
            customizer.customize(conf);
        }

        CamelContext context = new DefaultCamelContext();
        context.addService(new VertxPlatformHttpServer(conf));
        return context;
    }

    interface ServerConfigurationCustomizer {
        void customize(VertxPlatformHttpServerConfiguration configuration);
    }
}
