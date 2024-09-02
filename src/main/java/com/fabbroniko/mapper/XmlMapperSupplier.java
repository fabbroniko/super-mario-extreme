package com.fabbroniko.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.example.annotation.Component;

import java.util.function.Supplier;

@Component
public class XmlMapperSupplier implements Supplier<ObjectMapper> {

    @Override
    public ObjectMapper get() {
        return new XmlMapper();
    }
}
