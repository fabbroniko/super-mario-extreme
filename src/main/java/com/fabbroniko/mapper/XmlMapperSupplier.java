package com.fabbroniko.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.util.function.Supplier;

public class XmlMapperSupplier implements Supplier<ObjectMapper> {

    @Override
    public ObjectMapper get() {
        return new XmlMapper();
    }
}
