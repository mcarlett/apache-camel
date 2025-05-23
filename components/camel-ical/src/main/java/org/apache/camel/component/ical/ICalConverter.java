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
package org.apache.camel.component.ical;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.temporal.Temporal;
import java.util.Date;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.property.DateProperty;
import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.support.ExchangeHelper;

/**
 * ICal related converter.
 */
@Converter(generateLoader = true)
public final class ICalConverter {

    private ICalConverter() {
        // Helper class
    }

    @Converter(allowNull = true)
    public static Date toDate(DateProperty property) {
        Temporal t = property.getDate();
        if (t instanceof Instant ins) {
            return Date.from(ins);
        }
        return null;
    }

    @Converter(allowNull = true)
    public static Instant toInstant(DateProperty property) {
        Temporal t = property.getDate();
        if (t instanceof Instant ins) {
            return ins;
        }
        return null;
    }

    @Converter
    public static ByteArrayInputStream toStream(Calendar calendar, Exchange exchange) throws UnsupportedEncodingException {
        return new ByteArrayInputStream(calendar.toString().getBytes(ExchangeHelper.getCharsetName(exchange)));
    }

}
