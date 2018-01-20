package com.ote.user.spring;

import com.ote.UserServiceRunner;
import com.ote.common.persistence.model.ApplicationEntity;
import com.ote.common.persistence.model.UserRightEntity;
import com.ote.common.persistence.repository.IApplicationJpaRepository;
import org.assertj.core.api.Assertions;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

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
    @CsvSource({"TEST_SERVICE, True", "UNKOWN, False"})
    public void readingApplication(String application, boolean expectedResult) {
        Assertions.assertThat(applicationJpaRepository.existsByCode(application)).isEqualTo(expectedResult);
        if (expectedResult) {
            ApplicationEntity entity = applicationJpaRepository.findByCode(application);
            Assertions.assertThat(entity).isNotNull();
            Assertions.assertThat(entity.getId()).isPositive();
            Assertions.assertThat(entity.getCode()).isEqualTo(application);
            Assertions.assertThatThrownBy(() -> System.out.println(entity.getUserRights())).isInstanceOf(LazyInitializationException.class);
        }
    }

    @Test
    public void readingApplicationWithDetails() {
        String application = "USER_SERVICE";
        Assumptions.assumeTrue(applicationJpaRepository.existsByCode(application));

        ApplicationEntity entity = applicationJpaRepository.findByCodeWithUserRights(application);
        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getId()).isPositive();
        Assertions.assertThat(entity.getCode()).isEqualTo(application);

        Set<UserRightEntity> userRightEntities = entity.getUserRights();
        Assertions.assertThat(userRightEntities).isNotEmpty();
        Assertions.assertThat(userRightEntities).hasSize(1);

        UserRightEntity userRightEntity = userRightEntities.stream().findFirst().orElse(null);
        Assumptions.assumeTrue(userRightEntity != null);

        Assertions.assertThat(userRightEntity.getApplication()).isNotNull();
        Assertions.assertThat(userRightEntity.getApplication().getCode()).isEqualTo(application);
        Assertions.assertThat(userRightEntity.getUser()).isNotNull();
        Assertions.assertThat(userRightEntity.getUser().getLogin()).isEqualTo("user1");

        Assertions.assertThat(userRightEntity.getDetails()).isNotEmpty();
        Assertions.assertThat(userRightEntity.getDetails()).hasSize(1);

    }
}