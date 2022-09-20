package io.github.flexibletech.offering.application;

import org.modelmapper.ModelMapper;

public abstract class AbstractApplicationServiceTest {

    protected ModelMapper newModelMapper() {
        var mapper = new ModelMapper();
        mapper.getConfiguration().setAmbiguityIgnored(true);

        return mapper;
    }

}
