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
 * Perform SQL queries as a JDBC Stored Procedures using Spring JDBC.
 * 
 * Generated by camel build tools - do NOT edit this file!
 */
@Generated("org.apache.camel.maven.packaging.EndpointDslMojo")
public interface SqlStoredEndpointBuilderFactory {

    /**
     * Builder for endpoint for the SQL Stored Procedure component.
     */
    public interface SqlStoredEndpointBuilder
            extends
                EndpointProducerBuilder {
        default AdvancedSqlStoredEndpointBuilder advanced() {
            return (AdvancedSqlStoredEndpointBuilder) this;
        }

        /**
         * Enables or disables batch mode.
         * 
         * The option is a: <code>boolean</code> type.
         * 
         * Default: false
         * Group: producer
         * 
         * @param batch the value to set
         * @return the dsl builder
         */
        default SqlStoredEndpointBuilder batch(boolean batch) {
            doSetProperty("batch", batch);
            return this;
        }
        /**
         * Enables or disables batch mode.
         * 
         * The option will be converted to a <code>boolean</code> type.
         * 
         * Default: false
         * Group: producer
         * 
         * @param batch the value to set
         * @return the dsl builder
         */
        default SqlStoredEndpointBuilder batch(String batch) {
            doSetProperty("batch", batch);
            return this;
        }
        /**
         * Sets the DataSource to use to communicate with the database.
         * 
         * The option is a: <code>javax.sql.DataSource</code> type.
         * 
         * Group: producer
         * 
         * @param dataSource the value to set
         * @return the dsl builder
         */
        default SqlStoredEndpointBuilder dataSource(javax.sql.DataSource dataSource) {
            doSetProperty("dataSource", dataSource);
            return this;
        }
        /**
         * Sets the DataSource to use to communicate with the database.
         * 
         * The option will be converted to a <code>javax.sql.DataSource</code>
         * type.
         * 
         * Group: producer
         * 
         * @param dataSource the value to set
         * @return the dsl builder
         */
        default SqlStoredEndpointBuilder dataSource(String dataSource) {
            doSetProperty("dataSource", dataSource);
            return this;
        }
        /**
         * Whether this call is for a function.
         * 
         * The option is a: <code>boolean</code> type.
         * 
         * Default: false
         * Group: producer
         * 
         * @param function the value to set
         * @return the dsl builder
         */
        default SqlStoredEndpointBuilder function(boolean function) {
            doSetProperty("function", function);
            return this;
        }
        /**
         * Whether this call is for a function.
         * 
         * The option will be converted to a <code>boolean</code> type.
         * 
         * Default: false
         * Group: producer
         * 
         * @param function the value to set
         * @return the dsl builder
         */
        default SqlStoredEndpointBuilder function(String function) {
            doSetProperty("function", function);
            return this;
        }
        /**
         * If set, will ignore the results of the stored procedure template and
         * use the existing IN message as the OUT message for the continuation
         * of processing.
         * 
         * The option is a: <code>boolean</code> type.
         * 
         * Default: false
         * Group: producer
         * 
         * @param noop the value to set
         * @return the dsl builder
         */
        default SqlStoredEndpointBuilder noop(boolean noop) {
            doSetProperty("noop", noop);
            return this;
        }
        /**
         * If set, will ignore the results of the stored procedure template and
         * use the existing IN message as the OUT message for the continuation
         * of processing.
         * 
         * The option will be converted to a <code>boolean</code> type.
         * 
         * Default: false
         * Group: producer
         * 
         * @param noop the value to set
         * @return the dsl builder
         */
        default SqlStoredEndpointBuilder noop(String noop) {
            doSetProperty("noop", noop);
            return this;
        }
        /**
         * Store the template result in a header instead of the message body. By
         * default, outputHeader == null and the template result is stored in
         * the message body, any existing content in the message body is
         * discarded. If outputHeader is set, the value is used as the name of
         * the header to store the template result and the original message body
         * is preserved.
         * 
         * The option is a: <code>java.lang.String</code> type.
         * 
         * Group: producer
         * 
         * @param outputHeader the value to set
         * @return the dsl builder
         */
        default SqlStoredEndpointBuilder outputHeader(String outputHeader) {
            doSetProperty("outputHeader", outputHeader);
            return this;
        }
        /**
         * Whether to use the message body as the stored procedure template and
         * then headers for parameters. If this option is enabled then the
         * template in the uri is not used.
         * 
         * The option is a: <code>boolean</code> type.
         * 
         * Default: false
         * Group: producer
         * 
         * @param useMessageBodyForTemplate the value to set
         * @return the dsl builder
         */
        default SqlStoredEndpointBuilder useMessageBodyForTemplate(boolean useMessageBodyForTemplate) {
            doSetProperty("useMessageBodyForTemplate", useMessageBodyForTemplate);
            return this;
        }
        /**
         * Whether to use the message body as the stored procedure template and
         * then headers for parameters. If this option is enabled then the
         * template in the uri is not used.
         * 
         * The option will be converted to a <code>boolean</code> type.
         * 
         * Default: false
         * Group: producer
         * 
         * @param useMessageBodyForTemplate the value to set
         * @return the dsl builder
         */
        default SqlStoredEndpointBuilder useMessageBodyForTemplate(String useMessageBodyForTemplate) {
            doSetProperty("useMessageBodyForTemplate", useMessageBodyForTemplate);
            return this;
        }
    }

    /**
     * Advanced builder for endpoint for the SQL Stored Procedure component.
     */
    public interface AdvancedSqlStoredEndpointBuilder
            extends
                EndpointProducerBuilder {
        default SqlStoredEndpointBuilder basic() {
            return (SqlStoredEndpointBuilder) this;
        }

        /**
         * Whether the producer should be started lazy (on the first message).
         * By starting lazy you can use this to allow CamelContext and routes to
         * startup in situations where a producer may otherwise fail during
         * starting and cause the route to fail being started. By deferring this
         * startup to be lazy then the startup failure can be handled during
         * routing messages via Camel's routing error handlers. Beware that when
         * the first message is processed then creating and starting the
         * producer may take a little time and prolong the total processing time
         * of the processing.
         * 
         * The option is a: <code>boolean</code> type.
         * 
         * Default: false
         * Group: producer (advanced)
         * 
         * @param lazyStartProducer the value to set
         * @return the dsl builder
         */
        default AdvancedSqlStoredEndpointBuilder lazyStartProducer(boolean lazyStartProducer) {
            doSetProperty("lazyStartProducer", lazyStartProducer);
            return this;
        }
        /**
         * Whether the producer should be started lazy (on the first message).
         * By starting lazy you can use this to allow CamelContext and routes to
         * startup in situations where a producer may otherwise fail during
         * starting and cause the route to fail being started. By deferring this
         * startup to be lazy then the startup failure can be handled during
         * routing messages via Camel's routing error handlers. Beware that when
         * the first message is processed then creating and starting the
         * producer may take a little time and prolong the total processing time
         * of the processing.
         * 
         * The option will be converted to a <code>boolean</code> type.
         * 
         * Default: false
         * Group: producer (advanced)
         * 
         * @param lazyStartProducer the value to set
         * @return the dsl builder
         */
        default AdvancedSqlStoredEndpointBuilder lazyStartProducer(String lazyStartProducer) {
            doSetProperty("lazyStartProducer", lazyStartProducer);
            return this;
        }
        /**
         * Configures the Spring JdbcTemplate with the key/values from the Map.
         * This is a multi-value option with prefix: template.
         * 
         * The option is a: <code>java.util.Map&lt;java.lang.String,
         * java.lang.Object&gt;</code> type.
         * The option is multivalued, and you can use the
         * templateOptions(String, Object) method to add a value (call the
         * method multiple times to set more values).
         * 
         * Group: advanced
         * 
         * @param key the option key
         * @param value the option value
         * @return the dsl builder
         */
        default AdvancedSqlStoredEndpointBuilder templateOptions(String key, Object value) {
            doSetMultiValueProperty("templateOptions", "template." + key, value);
            return this;
        }
        /**
         * Configures the Spring JdbcTemplate with the key/values from the Map.
         * This is a multi-value option with prefix: template.
         * 
         * The option is a: <code>java.util.Map&lt;java.lang.String,
         * java.lang.Object&gt;</code> type.
         * The option is multivalued, and you can use the
         * templateOptions(String, Object) method to add a value (call the
         * method multiple times to set more values).
         * 
         * Group: advanced
         * 
         * @param values the values
         * @return the dsl builder
         */
        default AdvancedSqlStoredEndpointBuilder templateOptions(Map values) {
            doSetMultiValueProperties("templateOptions", "template.", values);
            return this;
        }
    }

    public interface SqlStoredBuilders {
        /**
         * SQL Stored Procedure (camel-sql)
         * Perform SQL queries as a JDBC Stored Procedures using Spring JDBC.
         * 
         * Category: database
         * Since: 2.17
         * Maven coordinates: org.apache.camel:camel-sql
         * 
         * @return the dsl builder for the headers' name.
         */
        default SqlStoredHeaderNameBuilder sqlStored() {
            return SqlStoredHeaderNameBuilder.INSTANCE;
        }
        /**
         * SQL Stored Procedure (camel-sql)
         * Perform SQL queries as a JDBC Stored Procedures using Spring JDBC.
         * 
         * Category: database
         * Since: 2.17
         * Maven coordinates: org.apache.camel:camel-sql
         * 
         * Syntax: <code>sql-stored:template</code>
         * 
         * Path parameter: template (required)
         * Sets the stored procedure template to perform. You can externalize
         * the template by using file: or classpath: as prefix and specify the
         * location of the file.
         * This option can also be loaded from an existing file, by prefixing
         * with file: or classpath: followed by the location of the file.
         * 
         * @param path template
         * @return the dsl builder
         */
        default SqlStoredEndpointBuilder sqlStored(String path) {
            return SqlStoredEndpointBuilderFactory.endpointBuilder("sql-stored", path);
        }
        /**
         * SQL Stored Procedure (camel-sql)
         * Perform SQL queries as a JDBC Stored Procedures using Spring JDBC.
         * 
         * Category: database
         * Since: 2.17
         * Maven coordinates: org.apache.camel:camel-sql
         * 
         * Syntax: <code>sql-stored:template</code>
         * 
         * Path parameter: template (required)
         * Sets the stored procedure template to perform. You can externalize
         * the template by using file: or classpath: as prefix and specify the
         * location of the file.
         * This option can also be loaded from an existing file, by prefixing
         * with file: or classpath: followed by the location of the file.
         * 
         * @param componentName to use a custom component name for the endpoint
         * instead of the default name
         * @param path template
         * @return the dsl builder
         */
        default SqlStoredEndpointBuilder sqlStored(String componentName, String path) {
            return SqlStoredEndpointBuilderFactory.endpointBuilder(componentName, path);
        }

    }
    /**
     * The builder of headers' name for the SQL Stored Procedure component.
     */
    public static class SqlStoredHeaderNameBuilder {
        /**
         * The internal instance of the builder used to access to all the
         * methods representing the name of headers.
         */
        private static final SqlStoredHeaderNameBuilder INSTANCE = new SqlStoredHeaderNameBuilder();

        /**
         * The template.
         * 
         * The option is a: {@code String} type.
         * 
         * Group: producer
         * 
         * @return the name of the header {@code SqlStoredTemplate}.
         */
        public String sqlStoredTemplate() {
            return "CamelSqlStoredTemplate";
        }
        /**
         * The parameters.
         * 
         * The option is a: {@code Iterator} type.
         * 
         * Group: producer
         * 
         * @return the name of the header {@code SqlStoredParameters}.
         */
        public String sqlStoredParameters() {
            return "CamelSqlStoredParameters";
        }
        /**
         * The update count.
         * 
         * The option is a: {@code Integer} type.
         * 
         * Group: producer
         * 
         * @return the name of the header {@code SqlStoredUpdateCount}.
         */
        public String sqlStoredUpdateCount() {
            return "CamelSqlStoredUpdateCount";
        }
    }
    static SqlStoredEndpointBuilder endpointBuilder(String componentName, String path) {
        class SqlStoredEndpointBuilderImpl extends AbstractEndpointBuilder implements SqlStoredEndpointBuilder, AdvancedSqlStoredEndpointBuilder {
            public SqlStoredEndpointBuilderImpl(String path) {
                super(componentName, path);
            }
        }
        return new SqlStoredEndpointBuilderImpl(path);
    }
}