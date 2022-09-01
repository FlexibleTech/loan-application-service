package io.github.flexibletech.offering.infrastructure.persistence;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class LoanApplicationIdGenerator extends SequenceStyleGenerator {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyMMdd");
    private static final String PREFIX = "LOANAPP";
    private static final String SUFFIX_FORMAT = "%05d";

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        var sequence = (long) super.generate(session, object);
        return PREFIX + LocalDateTime.now().format(FORMATTER) + String.format(SUFFIX_FORMAT, sequence);
    }

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        super.configure(new LongType(), params, serviceRegistry);
    }
}
