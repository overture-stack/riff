package bio.overture.riff;

import bio.overture.riff.service.RiffService;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.Collection;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public abstract class AbstractTest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Autowired
    protected Environment env;

    protected JSONParser jsonParser = new JSONParser();

    protected void assertArraysEqualIgnoreOrder(Object[] expected, Object[] actual) {
        assertArraysEqualIgnoreOrder(Arrays.asList(expected), Arrays.asList(actual));
    }

    protected void assertArraysEqualIgnoreOrder(Collection expected, Collection actual) {
        Assertions.assertTrue(
                expected.size() == actual.size() &&
                        expected.containsAll(actual) &&
                        actual.containsAll(expected)
        );
    }

    @Autowired
    protected RiffService service;

    @Container
    public static GenericContainer keycloak = new GenericContainer<>("quay.io/keycloak/keycloak:13.0.1")
            .withExposedPorts(8080, 8443)
            .withEnv("KEYCLOAK_USER", "admin")
            .withEnv("KEYCLOAK_PASSWORD", "admin");

    @DynamicPropertySource
    static void setDynamicKeycloakPort(DynamicPropertyRegistry registry) {
        registry.add("keycloak.auth-server-url",
                () -> String.format("http://localhost:%d/auth", keycloak.getFirstMappedPort()));
    }
}
