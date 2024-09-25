package com.fabbroniko.mapper;

import com.fabbroniko.sdi.annotation.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.function.Supplier;

@Component
public class JsonMapperSupplier implements Supplier<ObjectMapper> {

    @Override
    public ObjectMapper get() {
        return new ObjectMapper();
    }
}
