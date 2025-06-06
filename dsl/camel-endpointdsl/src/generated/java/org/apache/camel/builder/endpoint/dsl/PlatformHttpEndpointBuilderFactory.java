/* Generated by camel build tools - do NOT edit this file! */
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
package org.apache.camel.builder.endpoint.dsl;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.stream.*;
import javax.annotation.processing.Generated;
import org.apache.camel.builder.EndpointConsumerBuilder;
import org.apache.camel.builder.EndpointProducerBuilder;
import org.apache.camel.builder.endpoint.AbstractEndpointBuilder;

/**
 * Expose HTTP endpoints using the HTTP server available in the current
 * platform.
 * 
 * Generated by camel build tools - do NOT edit this file!
 */
@Generated("org.apache.camel.maven.packaging.EndpointDslMojo")
public interface PlatformHttpEndpointBuilderFactory {

    /**
     * Builder for endpoint for the Platform HTTP component.
     */
    public interface PlatformHttpEndpointBuilder
            extends
                EndpointConsumerBuilder {
        default AdvancedPlatformHttpEndpointBuilder advanced() {
            return (AdvancedPlatformHttpEndpointBuilder) this;
        }

        /**
         * The content type this endpoint accepts as an input, such as
         * application/xml or application/json. null or &amp;#42;/&amp;#42; mean
         * no restriction.
         * 
         * The option is a: <code>java.lang.String</code> type.
         * 
         * Group: consumer
         * 
         * @param consumes the value to set
         * @return the dsl builder
         */
        default PlatformHttpEndpointBuilder consumes(String consumes) {
            doSetProperty("consumes", consumes);
            return this;
        }
        /**
         * Sets which server can receive cookies.
         * 
         * The option is a: <code>java.lang.String</code> type.
         * 
         * Group: consumer
         * 
         * @param cookieDomain the value to set
         * @return the dsl builder
         */
        default PlatformHttpEndpointBuilder cookieDomain(String cookieDomain) {
            doSetProperty("cookieDomain", cookieDomain);
            return this;
        }
        /**
         * Sets whether to prevent client side scripts from accessing created
         * cookies.
         * 
         * The option is a: <code>boolean</code> type.
         * 
         * Default: false
         * Group: consumer
         * 
         * @param cookieHttpOnly the value to set
         * @return the dsl builder
         */
        default PlatformHttpEndpointBuilder cookieHttpOnly(boolean cookieHttpOnly) {
            doSetProperty("cookieHttpOnly", cookieHttpOnly);
            return this;
        }
        /**
         * Sets whether to prevent client side scripts from accessing created
         * cookies.
         * 
         * The option will be converted to a <code>boolean</code> type.
         * 
         * Default: false
         * Group: consumer
         * 
         * @param cookieHttpOnly the value to set
         * @return the dsl builder
         */
        default PlatformHttpEndpointBuilder cookieHttpOnly(String cookieHttpOnly) {
            doSetProperty("cookieHttpOnly", cookieHttpOnly);
            return this;
        }
        /**
         * Sets the maximum cookie age in seconds.
         * 
         * The option is a: <code>java.lang.Long</code> type.
         * 
         * Group: consumer
         * 
         * @param cookieMaxAge the value to set
         * @return the dsl builder
         */
        default PlatformHttpEndpointBuilder cookieMaxAge(Long cookieMaxAge) {
            doSetProperty("cookieMaxAge", cookieMaxAge);
            return this;
        }
        /**
         * Sets the maximum cookie age in seconds.
         * 
         * The option will be converted to a <code>java.lang.Long</code> type.
         * 
         * Group: consumer
         * 
         * @param cookieMaxAge the value to set
         * @return the dsl builder
         */
        default PlatformHttpEndpointBuilder cookieMaxAge(String cookieMaxAge) {
            doSetProperty("cookieMaxAge", cookieMaxAge);
            return this;
        }
        /**
         * Sets the URL path that must exist in the requested URL in order to
         * send the Cookie.
         * 
         * The option is a: <code>java.lang.String</code> type.
         * 
         * Default: /
         * Group: consumer
         * 
         * @param cookiePath the value to set
         * @return the dsl builder
         */
        default PlatformHttpEndpointBuilder cookiePath(String cookiePath) {
            doSetProperty("cookiePath", cookiePath);
            return this;
        }
        /**
         * Sets whether to prevent the browser from sending cookies along with
         * cross-site requests.
         * 
         * The option is a:
         * <code>org.apache.camel.component.platform.http.cookie.CookieConfiguration.CookieSameSite</code> type.
         * 
         * Default: Lax
         * Group: consumer
         * 
         * @param cookieSameSite the value to set
         * @return the dsl builder
         */
        default PlatformHttpEndpointBuilder cookieSameSite(org.apache.camel.component.platform.http.cookie.CookieConfiguration.CookieSameSite cookieSameSite) {
            doSetProperty("cookieSameSite", cookieSameSite);
            return this;
        }
        /**
         * Sets whether to prevent the browser from sending cookies along with
         * cross-site requests.
         * 
         * The option will be converted to a
         * <code>org.apache.camel.component.platform.http.cookie.CookieConfiguration.CookieSameSite</code> type.
         * 
         * Default: Lax
         * Group: consumer
         * 
         * @param cookieSameSite the value to set
         * @return the dsl builder
         */
        default PlatformHttpEndpointBuilder cookieSameSite(String cookieSameSite) {
            doSetProperty("cookieSameSite", cookieSameSite);
            return this;
        }
        /**
         * Sets whether the cookie is only sent to the server with an encrypted
         * request over HTTPS.
         * 
         * The option is a: <code>boolean</code> type.
         * 
         * Default: false
         * Group: consumer
         * 
         * @param cookieSecure the value to set
         * @return the dsl builder
         */
        default PlatformHttpEndpointBuilder cookieSecure(boolean cookieSecure) {
            doSetProperty("cookieSecure", cookieSecure);
            return this;
        }
        /**
         * Sets whether the cookie is only sent to the server with an encrypted
         * request over HTTPS.
         * 
         * The option will be converted to a <code>boolean</code> type.
         * 
         * Default: false
         * Group: consumer
         * 
         * @param cookieSecure the value to set
         * @return the dsl builder
         */
        default PlatformHttpEndpointBuilder cookieSecure(String cookieSecure) {
            doSetProperty("cookieSecure", cookieSecure);
            return this;
        }
        /**
         * A comma separated list of HTTP methods to serve, e.g. GET,POST . If
         * no methods are specified, all methods will be served.
         * 
         * The option is a: <code>java.lang.String</code> type.
         * 
         * Group: consumer
         * 
         * @param httpMethodRestrict the value to set
         * @return the dsl builder
         */
        default PlatformHttpEndpointBuilder httpMethodRestrict(String httpMethodRestrict) {
            doSetProperty("httpMethodRestrict", httpMethodRestrict);
            return this;
        }
        /**
         * Whether or not the consumer should try to find a target consumer by
         * matching the URI prefix if no exact match is found.
         * 
         * The option is a: <code>boolean</code> type.
         * 
         * Default: false
         * Group: consumer
         * 
         * @param matchOnUriPrefix the value to set
         * @return the dsl builder
         */
        default PlatformHttpEndpointBuilder matchOnUriPrefix(boolean matchOnUriPrefix) {
            doSetProperty("matchOnUriPrefix", matchOnUriPrefix);
            return this;
        }
        /**
         * Whether or not the consumer should try to find a target consumer by
         * matching the URI prefix if no exact match is found.
         * 
         * The option will be converted to a <code>boolean</code> type.
         * 
         * Default: false
         * Group: consumer
         * 
         * @param matchOnUriPrefix the value to set
         * @return the dsl builder
         */
        default PlatformHttpEndpointBuilder matchOnUriPrefix(String matchOnUriPrefix) {
            doSetProperty("matchOnUriPrefix", matchOnUriPrefix);
            return this;
        }
        /**
         * If enabled and an Exchange failed processing on the consumer side the
         * response's body won't contain the exception's stack trace.
         * 
         * The option is a: <code>boolean</code> type.
         * 
         * Default: true
         * Group: consumer
         * 
         * @param muteException the value to set
         * @return the dsl builder
         */
        default PlatformHttpEndpointBuilder muteException(boolean muteException) {
            doSetProperty("muteException", muteException);
            return this;
        }
        /**
         * If enabled and an Exchange failed processing on the consumer side the
         * response's body won't contain the exception's stack trace.
         * 
         * The option will be converted to a <code>boolean</code> type.
         * 
         * Default: true
         * Group: consumer
         * 
         * @param muteException the value to set
         * @return the dsl builder
         */
        default PlatformHttpEndpointBuilder muteException(String muteException) {
            doSetProperty("muteException", muteException);
            return this;
        }
        /**
         * The content type this endpoint produces, such as application/xml or
         * application/json.
         * 
         * The option is a: <code>java.lang.String</code> type.
         * 
         * Group: consumer
         * 
         * @param produces the value to set
         * @return the dsl builder
         */
        default PlatformHttpEndpointBuilder produces(String produces) {
            doSetProperty("produces", produces);
            return this;
        }
    }

    /**
     * Advanced builder for endpoint for the Platform HTTP component.
     */
    public interface AdvancedPlatformHttpEndpointBuilder
            extends
                EndpointConsumerBuilder {
        default PlatformHttpEndpointBuilder basic() {
            return (PlatformHttpEndpointBuilder) this;
        }

        /**
         * When Camel is complete processing the message, and the HTTP server is
         * writing response. This option controls whether Camel should catch any
         * failure during writing response and store this on the Exchange, which
         * allows onCompletion/UnitOfWork to regard the Exchange as failed and
         * have access to the caused exception from the HTTP server.
         * 
         * The option is a: <code>boolean</code> type.
         * 
         * Default: false
         * Group: consumer
         * 
         * @param handleWriteResponseError the value to set
         * @return the dsl builder
         */
        default AdvancedPlatformHttpEndpointBuilder handleWriteResponseError(boolean handleWriteResponseError) {
            doSetProperty("handleWriteResponseError", handleWriteResponseError);
            return this;
        }
        /**
         * When Camel is complete processing the message, and the HTTP server is
         * writing response. This option controls whether Camel should catch any
         * failure during writing response and store this on the Exchange, which
         * allows onCompletion/UnitOfWork to regard the Exchange as failed and
         * have access to the caused exception from the HTTP server.
         * 
         * The option will be converted to a <code>boolean</code> type.
         * 
         * Default: false
         * Group: consumer
         * 
         * @param handleWriteResponseError the value to set
         * @return the dsl builder
         */
        default AdvancedPlatformHttpEndpointBuilder handleWriteResponseError(String handleWriteResponseError) {
            doSetProperty("handleWriteResponseError", handleWriteResponseError);
            return this;
        }
        /**
         * Whether to populate the message Body with a Map containing
         * application/x-www-form-urlencoded form properties.
         * 
         * The option is a: <code>boolean</code> type.
         * 
         * Default: true
         * Group: consumer
         * 
         * @param populateBodyWithForm the value to set
         * @return the dsl builder
         */
        default AdvancedPlatformHttpEndpointBuilder populateBodyWithForm(boolean populateBodyWithForm) {
            doSetProperty("populateBodyWithForm", populateBodyWithForm);
            return this;
        }
        /**
         * Whether to populate the message Body with a Map containing
         * application/x-www-form-urlencoded form properties.
         * 
         * The option will be converted to a <code>boolean</code> type.
         * 
         * Default: true
         * Group: consumer
         * 
         * @param populateBodyWithForm the value to set
         * @return the dsl builder
         */
        default AdvancedPlatformHttpEndpointBuilder populateBodyWithForm(String populateBodyWithForm) {
            doSetProperty("populateBodyWithForm", populateBodyWithForm);
            return this;
        }
        /**
         * The period in milliseconds after which the request should be timed
         * out.
         * 
         * The option is a: <code>long</code> type.
         * 
         * Group: consumer
         * 
         * @param requestTimeout the value to set
         * @return the dsl builder
         */
        default AdvancedPlatformHttpEndpointBuilder requestTimeout(long requestTimeout) {
            doSetProperty("requestTimeout", requestTimeout);
            return this;
        }
        /**
         * The period in milliseconds after which the request should be timed
         * out.
         * 
         * The option will be converted to a <code>long</code> type.
         * 
         * Group: consumer
         * 
         * @param requestTimeout the value to set
         * @return the dsl builder
         */
        default AdvancedPlatformHttpEndpointBuilder requestTimeout(String requestTimeout) {
            doSetProperty("requestTimeout", requestTimeout);
            return this;
        }
        /**
         * Whether to include HTTP request headers (Accept, User-Agent, etc.)
         * into HTTP response produced by this endpoint.
         * 
         * The option is a: <code>boolean</code> type.
         * 
         * Default: false
         * Group: consumer
         * 
         * @param returnHttpRequestHeaders the value to set
         * @return the dsl builder
         */
        default AdvancedPlatformHttpEndpointBuilder returnHttpRequestHeaders(boolean returnHttpRequestHeaders) {
            doSetProperty("returnHttpRequestHeaders", returnHttpRequestHeaders);
            return this;
        }
        /**
         * Whether to include HTTP request headers (Accept, User-Agent, etc.)
         * into HTTP response produced by this endpoint.
         * 
         * The option will be converted to a <code>boolean</code> type.
         * 
         * Default: false
         * Group: consumer
         * 
         * @param returnHttpRequestHeaders the value to set
         * @return the dsl builder
         */
        default AdvancedPlatformHttpEndpointBuilder returnHttpRequestHeaders(String returnHttpRequestHeaders) {
            doSetProperty("returnHttpRequestHeaders", returnHttpRequestHeaders);
            return this;
        }
        /**
         * Whether to use BodyHandler for the request. If set to false then the
         * request will no be read and parsed.
         * 
         * The option is a: <code>boolean</code> type.
         * 
         * Default: true
         * Group: consumer
         * 
         * @param useBodyHandler the value to set
         * @return the dsl builder
         */
        default AdvancedPlatformHttpEndpointBuilder useBodyHandler(boolean useBodyHandler) {
            doSetProperty("useBodyHandler", useBodyHandler);
            return this;
        }
        /**
         * Whether to use BodyHandler for the request. If set to false then the
         * request will no be read and parsed.
         * 
         * The option will be converted to a <code>boolean</code> type.
         * 
         * Default: true
         * Group: consumer
         * 
         * @param useBodyHandler the value to set
         * @return the dsl builder
         */
        default AdvancedPlatformHttpEndpointBuilder useBodyHandler(String useBodyHandler) {
            doSetProperty("useBodyHandler", useBodyHandler);
            return this;
        }
        /**
         * Whether to enable the Cookie Handler that allows Cookie addition,
         * expiry, and retrieval (currently only supported by
         * camel-platform-http-vertx).
         * 
         * The option is a: <code>boolean</code> type.
         * 
         * Default: false
         * Group: consumer
         * 
         * @param useCookieHandler the value to set
         * @return the dsl builder
         */
        default AdvancedPlatformHttpEndpointBuilder useCookieHandler(boolean useCookieHandler) {
            doSetProperty("useCookieHandler", useCookieHandler);
            return this;
        }
        /**
         * Whether to enable the Cookie Handler that allows Cookie addition,
         * expiry, and retrieval (currently only supported by
         * camel-platform-http-vertx).
         * 
         * The option will be converted to a <code>boolean</code> type.
         * 
         * Default: false
         * Group: consumer
         * 
         * @param useCookieHandler the value to set
         * @return the dsl builder
         */
        default AdvancedPlatformHttpEndpointBuilder useCookieHandler(String useCookieHandler) {
            doSetProperty("useCookieHandler", useCookieHandler);
            return this;
        }
        /**
         * Whether to use streaming for large requests and responses (currently
         * only supported by camel-platform-http-vertx).
         * 
         * The option is a: <code>boolean</code> type.
         * 
         * Default: false
         * Group: consumer
         * 
         * @param useStreaming the value to set
         * @return the dsl builder
         */
        default AdvancedPlatformHttpEndpointBuilder useStreaming(boolean useStreaming) {
            doSetProperty("useStreaming", useStreaming);
            return this;
        }
        /**
         * Whether to use streaming for large requests and responses (currently
         * only supported by camel-platform-http-vertx).
         * 
         * The option will be converted to a <code>boolean</code> type.
         * 
         * Default: false
         * Group: consumer
         * 
         * @param useStreaming the value to set
         * @return the dsl builder
         */
        default AdvancedPlatformHttpEndpointBuilder useStreaming(String useStreaming) {
            doSetProperty("useStreaming", useStreaming);
            return this;
        }
        /**
         * Allows for bridging the consumer to the Camel routing Error Handler,
         * which mean any exceptions (if possible) occurred while the Camel
         * consumer is trying to pickup incoming messages, or the likes, will
         * now be processed as a message and handled by the routing Error
         * Handler. Important: This is only possible if the 3rd party component
         * allows Camel to be alerted if an exception was thrown. Some
         * components handle this internally only, and therefore
         * bridgeErrorHandler is not possible. In other situations we may
         * improve the Camel component to hook into the 3rd party component and
         * make this possible for future releases. By default the consumer will
         * use the org.apache.camel.spi.ExceptionHandler to deal with
         * exceptions, that will be logged at WARN or ERROR level and ignored.
         * 
         * The option is a: <code>boolean</code> type.
         * 
         * Default: false
         * Group: consumer (advanced)
         * 
         * @param bridgeErrorHandler the value to set
         * @return the dsl builder
         */
        default AdvancedPlatformHttpEndpointBuilder bridgeErrorHandler(boolean bridgeErrorHandler) {
            doSetProperty("bridgeErrorHandler", bridgeErrorHandler);
            return this;
        }
        /**
         * Allows for bridging the consumer to the Camel routing Error Handler,
         * which mean any exceptions (if possible) occurred while the Camel
         * consumer is trying to pickup incoming messages, or the likes, will
         * now be processed as a message and handled by the routing Error
         * Handler. Important: This is only possible if the 3rd party component
         * allows Camel to be alerted if an exception was thrown. Some
         * components handle this internally only, and therefore
         * bridgeErrorHandler is not possible. In other situations we may
         * improve the Camel component to hook into the 3rd party component and
         * make this possible for future releases. By default the consumer will
         * use the org.apache.camel.spi.ExceptionHandler to deal with
         * exceptions, that will be logged at WARN or ERROR level and ignored.
         * 
         * The option will be converted to a <code>boolean</code> type.
         * 
         * Default: false
         * Group: consumer (advanced)
         * 
         * @param bridgeErrorHandler the value to set
         * @return the dsl builder
         */
        default AdvancedPlatformHttpEndpointBuilder bridgeErrorHandler(String bridgeErrorHandler) {
            doSetProperty("bridgeErrorHandler", bridgeErrorHandler);
            return this;
        }
        /**
         * To let the consumer use a custom ExceptionHandler. Notice if the
         * option bridgeErrorHandler is enabled then this option is not in use.
         * By default the consumer will deal with exceptions, that will be
         * logged at WARN or ERROR level and ignored.
         * 
         * The option is a: <code>org.apache.camel.spi.ExceptionHandler</code>
         * type.
         * 
         * Group: consumer (advanced)
         * 
         * @param exceptionHandler the value to set
         * @return the dsl builder
         */
        default AdvancedPlatformHttpEndpointBuilder exceptionHandler(org.apache.camel.spi.ExceptionHandler exceptionHandler) {
            doSetProperty("exceptionHandler", exceptionHandler);
            return this;
        }
        /**
         * To let the consumer use a custom ExceptionHandler. Notice if the
         * option bridgeErrorHandler is enabled then this option is not in use.
         * By default the consumer will deal with exceptions, that will be
         * logged at WARN or ERROR level and ignored.
         * 
         * The option will be converted to a
         * <code>org.apache.camel.spi.ExceptionHandler</code> type.
         * 
         * Group: consumer (advanced)
         * 
         * @param exceptionHandler the value to set
         * @return the dsl builder
         */
        default AdvancedPlatformHttpEndpointBuilder exceptionHandler(String exceptionHandler) {
            doSetProperty("exceptionHandler", exceptionHandler);
            return this;
        }
        /**
         * Sets the exchange pattern when the consumer creates an exchange.
         * 
         * The option is a: <code>org.apache.camel.ExchangePattern</code> type.
         * 
         * Group: consumer (advanced)
         * 
         * @param exchangePattern the value to set
         * @return the dsl builder
         */
        default AdvancedPlatformHttpEndpointBuilder exchangePattern(org.apache.camel.ExchangePattern exchangePattern) {
            doSetProperty("exchangePattern", exchangePattern);
            return this;
        }
        /**
         * Sets the exchange pattern when the consumer creates an exchange.
         * 
         * The option will be converted to a
         * <code>org.apache.camel.ExchangePattern</code> type.
         * 
         * Group: consumer (advanced)
         * 
         * @param exchangePattern the value to set
         * @return the dsl builder
         */
        default AdvancedPlatformHttpEndpointBuilder exchangePattern(String exchangePattern) {
            doSetProperty("exchangePattern", exchangePattern);
            return this;
        }
        /**
         * A comma or whitespace separated list of file extensions. Uploads
         * having these extensions will be stored locally. Null value or
         * asterisk () will allow all files.
         * 
         * The option is a: <code>java.lang.String</code> type.
         * 
         * Group: consumer (advanced)
         * 
         * @param fileNameExtWhitelist the value to set
         * @return the dsl builder
         */
        default AdvancedPlatformHttpEndpointBuilder fileNameExtWhitelist(String fileNameExtWhitelist) {
            doSetProperty("fileNameExtWhitelist", fileNameExtWhitelist);
            return this;
        }
        /**
         * To use a custom HeaderFilterStrategy to filter headers to and from
         * Camel message.
         * 
         * The option is a:
         * <code>org.apache.camel.spi.HeaderFilterStrategy</code> type.
         * 
         * Group: advanced
         * 
         * @param headerFilterStrategy the value to set
         * @return the dsl builder
         */
        default AdvancedPlatformHttpEndpointBuilder headerFilterStrategy(org.apache.camel.spi.HeaderFilterStrategy headerFilterStrategy) {
            doSetProperty("headerFilterStrategy", headerFilterStrategy);
            return this;
        }
        /**
         * To use a custom HeaderFilterStrategy to filter headers to and from
         * Camel message.
         * 
         * The option will be converted to a
         * <code>org.apache.camel.spi.HeaderFilterStrategy</code> type.
         * 
         * Group: advanced
         * 
         * @param headerFilterStrategy the value to set
         * @return the dsl builder
         */
        default AdvancedPlatformHttpEndpointBuilder headerFilterStrategy(String headerFilterStrategy) {
            doSetProperty("headerFilterStrategy", headerFilterStrategy);
            return this;
        }
        /**
         * An HTTP Server engine implementation to serve the requests of this
         * endpoint.
         * 
         * The option is a:
         * <code>org.apache.camel.component.platform.http.spi.PlatformHttpEngine</code> type.
         * 
         * Group: advanced
         * 
         * @param platformHttpEngine the value to set
         * @return the dsl builder
         */
        default AdvancedPlatformHttpEndpointBuilder platformHttpEngine(org.apache.camel.component.platform.http.spi.PlatformHttpEngine platformHttpEngine) {
            doSetProperty("platformHttpEngine", platformHttpEngine);
            return this;
        }
        /**
         * An HTTP Server engine implementation to serve the requests of this
         * endpoint.
         * 
         * The option will be converted to a
         * <code>org.apache.camel.component.platform.http.spi.PlatformHttpEngine</code> type.
         * 
         * Group: advanced
         * 
         * @param platformHttpEngine the value to set
         * @return the dsl builder
         */
        default AdvancedPlatformHttpEndpointBuilder platformHttpEngine(String platformHttpEngine) {
            doSetProperty("platformHttpEngine", platformHttpEngine);
            return this;
        }
    }

    public interface PlatformHttpBuilders {
        /**
         * Platform HTTP (camel-platform-http)
         * Expose HTTP endpoints using the HTTP server available in the current
         * platform.
         * 
         * Category: http
         * Since: 3.0
         * Maven coordinates: org.apache.camel:camel-platform-http
         * 
         * Syntax: <code>platform-http:path</code>
         * 
         * Path parameter: path (required)
         * The path under which this endpoint serves the HTTP requests, for
         * proxy use 'proxy'
         * 
         * @param path path
         * @return the dsl builder
         */
        default PlatformHttpEndpointBuilder platformHttp(String path) {
            return PlatformHttpEndpointBuilderFactory.endpointBuilder("platform-http", path);
        }
        /**
         * Platform HTTP (camel-platform-http)
         * Expose HTTP endpoints using the HTTP server available in the current
         * platform.
         * 
         * Category: http
         * Since: 3.0
         * Maven coordinates: org.apache.camel:camel-platform-http
         * 
         * Syntax: <code>platform-http:path</code>
         * 
         * Path parameter: path (required)
         * The path under which this endpoint serves the HTTP requests, for
         * proxy use 'proxy'
         * 
         * @param componentName to use a custom component name for the endpoint
         * instead of the default name
         * @param path path
         * @return the dsl builder
         */
        default PlatformHttpEndpointBuilder platformHttp(String componentName, String path) {
            return PlatformHttpEndpointBuilderFactory.endpointBuilder(componentName, path);
        }

    }
    static PlatformHttpEndpointBuilder endpointBuilder(String componentName, String path) {
        class PlatformHttpEndpointBuilderImpl extends AbstractEndpointBuilder implements PlatformHttpEndpointBuilder, AdvancedPlatformHttpEndpointBuilder {
            public PlatformHttpEndpointBuilderImpl(String path) {
                super(componentName, path);
            }
        }
        return new PlatformHttpEndpointBuilderImpl(path);
    }
}