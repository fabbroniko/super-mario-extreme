package com.fabbroniko.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.annotation.Component;

import java.util.function.Supplier;

@Component
public class JsonMapperSupplier implements Supplier<ObjectMapper> {

    @Override
    public ObjectMapper get() {
        return new ObjectMapper();
    }
}
