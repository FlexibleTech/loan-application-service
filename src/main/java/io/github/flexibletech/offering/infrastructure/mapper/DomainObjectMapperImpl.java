package io.github.flexibletech.offering.infrastructure.mapper;

import io.github.flexibletech.offering.application.DomainObjectMapper;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DomainObjectMapperImpl implements DomainObjectMapper {
    private final ModelMapper mapper;

    public DomainObjectMapperImpl(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public <D, T> T map(D object, Class<T> clazz) {
        try {
            return mapper.map(object, clazz);
        } catch (MappingException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

}
