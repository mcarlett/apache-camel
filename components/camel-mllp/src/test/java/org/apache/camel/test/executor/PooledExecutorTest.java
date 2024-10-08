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
package org.apache.camel.test.executor;

import java.util.concurrent.RejectedExecutionException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PooledExecutorTest {
    static final int THREAD_COUNT = 2;
    Logger log = LoggerFactory.getLogger(this.getClass());
    TestExecutor instance;

    @BeforeEach
    public void setUp() {
        instance = new TestExecutor(THREAD_COUNT);
    }

    @AfterEach
    public void tearDown() {
        instance.stop();
    }

    /**
     * Description of test.
     *
     * @throws Exception in the event of a test error.
     */
    @Test
    public void testAddRunnable() throws Exception {
        int runnableCount = 3;
        int runCount = 5;

        log.info("Starting first set of runnables");
        startRunnables(runnableCount, runCount);

        log.info("Starting second set of runnables");
        startRunnables(runnableCount, runCount);
    }

    void startRunnables(int runnableCount, int runCount) {
        for (int id = 1; id <= runnableCount; ++id) {
            try {
                instance.addRunnable(new TestRunnable(id, runCount));
            } catch (RejectedExecutionException rejectedEx) {
                log.warn("Unable to add Runnable {}", id, rejectedEx);
            }
        }
    }
}
