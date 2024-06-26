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
package org.apache.camel.dataformat.csv;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.camel.EndpointInject;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * This class tests standard marshalling
 */
public class CsvMarshalTest extends CamelTestSupport {
    @EndpointInject("mock:output")
    MockEndpoint output;

    @Test
    void shouldMarshalLists() throws Exception {
        output.expectedMessageCount(1);

        template.sendBody("direct:default", Arrays.<List> asList(
                Arrays.asList("1", "2", "3"),
                Arrays.asList("one", "two", "three")));
        output.assertIsSatisfied();

        String[] actuals = readOutputLines(0);
        assertArrayEquals(new String[] { "1,2,3", "one,two,three" }, actuals);
    }

    @Test
    void shouldMarshalListsOneRow() throws Exception {
        output.expectedMessageCount(1);

        template.sendBody("direct:default", Arrays.<List> asList(
                List.of("1"),
                List.of("one")));
        output.assertIsSatisfied();

        String[] actuals = readOutputLines(0);
        assertArrayEquals(new String[] { "1", "one" }, actuals);
    }

    @Test
    void shouldMarshalMaps() throws Exception {
        output.expectedMessageCount(1);

        template.sendBody("direct:default", Arrays.<Map> asList(
                TestUtils.asMap("A", "1", "B", "2", "C", "3"),
                TestUtils.asMap("A", "one", "B", "two", "C", "three")));
        output.assertIsSatisfied();

        assertArrayEquals(new String[] { "1,2,3", "one,two,three" }, readOutputLines(0));
    }

    @Test
    void shouldMarshalSingleMap() throws Exception {
        output.expectedMessageCount(1);

        template.sendBody("direct:default", TestUtils.asMap("A", "1", "B", "2", "C", "3"));
        output.assertIsSatisfied();

        assertArrayEquals(new String[] { "1,2,3" }, readOutputLines(0));
    }

    @Test
    void shouldHandleColumns() throws Exception {
        output.expectedMessageCount(1);

        template.sendBody("direct:headers", Arrays.<Map> asList(
                TestUtils.asMap("A", "1", "B", "2", "C", "3"),
                TestUtils.asMap("A", "one", "B", "two", "C", "three")));
        output.assertIsSatisfied();

        assertArrayEquals(new String[] { "A,C", "1,3", "one,three" }, readOutputLines(0));
    }

    @Test
    void shouldMarshalDifferentDynamicColumns() throws Exception {
        output.expectedMessageCount(2);

        template.sendBody("direct:default", TestUtils.asMap("A", "1", "B", "2"));
        template.sendBody("direct:default", TestUtils.asMap("X", "1", "Y", "2", "Z", "3"));

        output.assertIsSatisfied();
        assertArrayEquals(new String[] { "1,2" }, readOutputLines(0));
        assertArrayEquals(new String[] { "1,2,3" }, readOutputLines(1));
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                // Default format
                from("direct:default")
                        .marshal(new CsvDataFormat())
                        .to("mock:output");

                // Format with special headers
                from("direct:headers")
                        .marshal(new CsvDataFormat().setHeader(new String[] { "A", "C" }))
                        .to("mock:output");
            }
        };
    }

    private String[] readOutputLines(int index) {
        return output.getExchanges().get(index).getIn().getBody(String.class).split("\r\n|\r|\n");
    }
}
