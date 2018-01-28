package com.ote.user.spring;

import com.ote.UserServiceRunner;
import com.ote.common.persistence.model.ApplicationEntity;
import com.ote.common.persistence.repository.IApplicationJpaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("Test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UserServiceRunner.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ApplicationTest {

    @Autowired
    private IApplicationJpaRepository applicationJpaRepository;

    @Autowired
    private DataConfigurationMock userRightDataConfiguration;

    @BeforeEach
    public void init() {
        userRightDataConfiguration.init();
    }

    @AfterEach
    public void clean() {
        userRightDataConfiguration.clean();
    }

    @ParameterizedTest
    @CsvSource({"App1, True", "UNKOWN, False"})
    public void readingApplication(String application, boolean expectedResult) {
        Assertions.assertThat(applicationJpaRepository.existsByCode(application)).isEqualTo(expectedResult);
        if (expectedResult) {
            ApplicationEntity entity = applicationJpaRepository.findByCode(application);
            Assertions.assertThat(entity).isNotNull();
            Assertions.assertThat(entity.getId()).isPositive();
            Assertions.assertThat(entity.getCode()).isEqualTo(application);
        }
    }
}
