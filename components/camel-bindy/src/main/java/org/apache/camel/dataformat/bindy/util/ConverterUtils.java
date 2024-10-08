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
package org.apache.camel.dataformat.bindy.util;

import org.apache.camel.dataformat.bindy.FormattingOptions;
import org.apache.camel.dataformat.bindy.annotation.BindyConverter;
import org.apache.camel.dataformat.bindy.annotation.DataField;
import org.apache.camel.dataformat.bindy.annotation.KeyValuePairField;

/**
 * To help return the char associated to the unicode string
 */
public final class ConverterUtils {

    private static final byte[] WINDOWS_RETURN_BYTES = { 13, 10 };
    private static final byte[] UNIX_RETURN_BYTES = { 10 };
    private static final byte[] MAC_RETURN_BYTES = { 13 };
    private static final String CRLF = "\r\n";
    private static final String LINE_BREAK = "\n";
    private static final String CARRIAGE_RETURN = "\r";

    private ConverterUtils() {
        // helper class
    }

    public static char getCharDelimiter(String separator) {
        if (separator.equals("\\u0001")) {
            return '\u0001';
        } else if (separator.equals("\\t") || separator.equals("\\u0009")) {
            return '\u0009';
        } else if (separator.length() > 1) {
            return separator.charAt(separator.length() - 1);
        } else {
            return separator.charAt(0);
        }
    }

    public static byte[] getByteReturn(String returnCharacter) {
        if (returnCharacter.equals("WINDOWS")) {
            return WINDOWS_RETURN_BYTES;
        } else if (returnCharacter.equals("UNIX")) {
            return UNIX_RETURN_BYTES;
        } else if (returnCharacter.equals("MAC")) {
            return MAC_RETURN_BYTES;
        } else {
            return returnCharacter.getBytes();
        }
    }

    public static String getStringCarriageReturn(String returnCharacter) {
        if (returnCharacter.equals("WINDOWS")) {
            return CRLF;
        } else if (returnCharacter.equals("UNIX")) {
            return LINE_BREAK;
        } else if (returnCharacter.equals("MAC")) {
            return CARRIAGE_RETURN;
        } else {
            return returnCharacter;
        }
    }

    public static FormattingOptions convert(DataField dataField, Class<?> clazz, BindyConverter converter, String locale) {
        return new FormattingOptions()
                .forClazz(clazz)
                .withPattern(dataField.pattern())
                .withLocale(locale)
                .withTimezone(dataField.timezone())
                .withPrecision(dataField.precision())
                .withRounding(dataField.rounding())
                .withImpliedDecimalSeparator(dataField.impliedDecimalSeparator())
                .withDecimalSeparator(dataField.decimalSeparator())
                .withBindyConverter(converter)
                .withGroupingSeparator(dataField.groupingSeparator());
    }

    public static FormattingOptions convert(
            KeyValuePairField dataField, Class<?> clazz, BindyConverter converter, String locale) {
        return new FormattingOptions()
                .forClazz(clazz)
                .withPattern(dataField.pattern())
                .withLocale(locale)
                .withTimezone(dataField.timezone())
                .withPrecision(dataField.precision())
                .withBindyConverter(converter)
                .withImpliedDecimalSeparator(dataField.impliedDecimalSeparator());
    }
}
