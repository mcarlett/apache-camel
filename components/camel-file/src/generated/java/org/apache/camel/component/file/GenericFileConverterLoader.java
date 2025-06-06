/* Generated by camel build tools - do NOT edit this file! */
package org.apache.camel.component.file;

import javax.annotation.processing.Generated;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.DeferredContextBinding;
import org.apache.camel.Exchange;
import org.apache.camel.TypeConversionException;
import org.apache.camel.TypeConverterLoaderException;
import org.apache.camel.spi.TypeConverterLoader;
import org.apache.camel.spi.TypeConverterRegistry;
import org.apache.camel.support.SimpleTypeConverter;
import org.apache.camel.support.TypeConverterSupport;
import org.apache.camel.util.DoubleMap;

/**
 * Generated by camel build tools - do NOT edit this file!
 */
@Generated("org.apache.camel.maven.packaging.TypeConverterLoaderGeneratorMojo")
@SuppressWarnings("unchecked")
@DeferredContextBinding
public final class GenericFileConverterLoader implements TypeConverterLoader, CamelContextAware {

    private CamelContext camelContext;

    public GenericFileConverterLoader() {
    }

    @Override
    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    @Override
    public CamelContext getCamelContext() {
        return camelContext;
    }

    @Override
    public void load(TypeConverterRegistry registry) throws TypeConverterLoaderException {
        registerConverters(registry);
        registerFallbackConverters(registry);
    }

    private void registerConverters(TypeConverterRegistry registry) {
        addTypeConverter(registry, java.io.InputStream.class, org.apache.camel.component.file.GenericFile.class, false,
            (type, exchange, value) -> {
                Object answer = org.apache.camel.component.file.GenericFileConverter.genericFileToInputStream((org.apache.camel.component.file.GenericFile) value, exchange);
                if (false && answer == null) {
                    answer = Void.class;
                }
                return answer;
            });
        addTypeConverter(registry, java.io.Reader.class, org.apache.camel.component.file.GenericFile.class, false,
            (type, exchange, value) -> {
                Object answer = org.apache.camel.component.file.GenericFileConverter.genericFileToReader((org.apache.camel.component.file.GenericFile) value, exchange);
                if (false && answer == null) {
                    answer = Void.class;
                }
                return answer;
            });
        addTypeConverter(registry, java.io.Serializable.class, org.apache.camel.component.file.GenericFile.class, false,
            (type, exchange, value) -> {
                Object answer = org.apache.camel.component.file.GenericFileConverter.genericFileToSerializable((org.apache.camel.component.file.GenericFile) value, exchange);
                if (false && answer == null) {
                    answer = Void.class;
                }
                return answer;
            });
        addTypeConverter(registry, java.lang.String.class, org.apache.camel.component.file.GenericFile.class, false,
            (type, exchange, value) -> {
                Object answer = org.apache.camel.component.file.GenericFileConverter.genericFileToString((org.apache.camel.component.file.GenericFile) value, exchange);
                if (false && answer == null) {
                    answer = Void.class;
                }
                return answer;
            });
    }

    private static void addTypeConverter(TypeConverterRegistry registry, Class<?> toType, Class<?> fromType, boolean allowNull, SimpleTypeConverter.ConversionMethod method) {
        registry.addTypeConverter(toType, fromType, new SimpleTypeConverter(allowNull, method));
    }

    private void registerFallbackConverters(TypeConverterRegistry registry) {
        addFallbackTypeConverter(registry, false, false, (type, exchange, value) -> org.apache.camel.component.file.GenericFileConverter.convertTo(type, exchange, value, registry));
    }

    private static void addFallbackTypeConverter(TypeConverterRegistry registry, boolean allowNull, boolean canPromote, SimpleTypeConverter.ConversionMethod method) {
        registry.addFallbackTypeConverter(new SimpleTypeConverter(allowNull, method), canPromote);
    }
}
