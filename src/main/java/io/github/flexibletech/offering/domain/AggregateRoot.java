package io.github.flexibletech.offering.domain;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AggregateRoot extends Auditable implements Entity {
}
