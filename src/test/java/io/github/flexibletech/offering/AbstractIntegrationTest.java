package io.github.flexibletech.offering;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("it")
@SpringBootTest(classes = Application.class)
@Import(TestChannelBinderConfiguration.class)
public abstract class AbstractIntegrationTest {
    @Autowired
    protected ObjectMapper objectMapper;
}
