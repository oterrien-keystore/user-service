package com.ote.user.spring;

import com.ote.UserServiceRunner;
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
public class UserRightServiceTest {

    @Autowired
    private UserRightRestControllerMock userRightRestControllerMock;

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
    @CsvSource({
            "user1, USER_SERVICE, PARENT, READ, True, user1 should own READ on PARENT for USER_SERVICE",
            "user1, USER_SERVICE, PARENT, WRITE, False, user1 should not own WRITE on PARENT for TEST_SERVICE",
            "user1, TEST_SERVICE, PARENT, WRITE, True, user1 should own WRITE on PARENT for TEST_SERVICE",
            "user1, TEST_SERVICE, PARENT, READ, True, user1 should own READ on PARENT  for TEST_SERVICE (WRITE provides READ)",
            "user1, TEST_SERVICE, PARENT, ADMIN, False, user1 should not own ADMIN on PARENT for TEST_SERVICE",
            "user1, TEST_SERVICE, PARENT/CHILD, WRITE, True, user1 should own WRITE on PARENT/CHILD for TEST_SERVICE (WRITE is inherited from PARENT)",
            "user1, TEST_SERVICE, PARENT/CHILD, READ, True, user1 should own READ on PARENT/CHILD for TEST_SERVICE (WRITE is inherited from PARENT and provides READ)",
            "user2, TEST_SERVICE, PARENT/CHILD, READ, True, user2 should own READ on PARENT for TEST_SERVICE",
            "user2, TEST_SERVICE, PARENT, READ, False, user2 should not own READ on PARENT for TEST_SERVICE",
            "user2, TEST_SERVICE, PARENT/CHILD, WRITE, False, user2 should not own WRITE on PARENT for TEST_SERVICE"})
    public void checkUserRightPrivilege(String user, String application, String perimeter, String privilege, boolean expectation, String description) {

        // Check with path Deal
        boolean hasPrivilege = userRightRestControllerMock.doesUserOwnPrivilegeForApplicationOnPerimeter(user, application, perimeter, privilege);
        Assertions.assertThat(hasPrivilege).as(description).isEqualTo(expectation);
    }
}
