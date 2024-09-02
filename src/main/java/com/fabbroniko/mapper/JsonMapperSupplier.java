package com.fabbroniko.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.function.Supplier;

public class JsonMapperSupplier implements Supplier<ObjectMapper> {

    @Override
    public ObjectMapper get() {
        return new ObjectMapper();
    }
}
