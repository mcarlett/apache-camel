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
package org.apache.camel.component.thymeleaf;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.apache.camel.test.junit5.TestSupport.body;

public class ThymeleafConcurrentTest extends ThymeleafAbstractBaseTest {

    @Test
    public void testNoConcurrentProducers() throws Exception {

        doSendMessages(1, 1);
    }

    @Test
    public void testConcurrentProducers() throws Exception {

        doSendMessages(10, 5);
    }

    private void doSendMessages(int files, int poolSize) throws Exception {

        MockEndpoint mock = getMockEndpoint(MOCK_RESULT);

        mock.expectedMessageCount(files);
        mock.assertNoDuplicates(body());
        mock.message(0).body().contains("Bye");

        ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        for (int i = 0; i < files; i++) {
            final int index = i;
            executor.submit(() -> {

                template.sendBody(DIRECT_START, "Hello " + index);

                return null;
            });
        }

        MockEndpoint.assertIsSatisfied(context);
        executor.shutdownNow();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {

        return new RouteBuilder() {

            public void configure() {

                from(DIRECT_START)
                        .to("thymeleaf:org/apache/camel/component/thymeleaf/concurrent.txt")
                        .to(MOCK_RESULT);
            }
        };
    }

}