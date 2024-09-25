package com.fabbroniko.mapper;

import com.fabbroniko.sdi.annotation.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.util.function.Supplier;

@Component
public class XmlMapperSupplier implements Supplier<ObjectMapper> {

    @Override
    public ObjectMapper get() {
        return new XmlMapper();
    }
}
