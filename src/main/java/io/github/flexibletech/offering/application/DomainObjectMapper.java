package io.github.flexibletech.offering.application;

public interface DomainObjectMapper {

    <D, T> T map(D object, Class<T> clazz);

}
