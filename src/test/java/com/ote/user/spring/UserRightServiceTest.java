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
            "user1, App1, PARENT, READ, True, user1 should own READ on PARENT for App1 (admin of App1/PARENT)",
            "user1, App1, PARENT, WRITE, True, user1 should own WRITE on PARENT for App1 (admin of App1/PARENT)",
            "user1, App1, PARENT, ADMIN, True, user1 should own ADMIN on PARENT for App1 (admin of App1/PARENT)",
            "user1, App1, PARENT/CHILD, ADMIN, True, user1 should own ADMIN on each children of PARENT for App1 (admin of App1/PARENT)",
            "user1, App1, PARENT/CHILD, WRITE, True, user1 should own each sub privilege from ADMIN on each children of PARENT for App1 (admin of App1/PARENT)",
            "user1, App2, PARENT, ADMIN, True, user1 should own ADMIN on PARENT for App2 (admin of App2/PARENT)",
            "user2, App1, PARENT, ADMIN, False, user2 should not own ADMIN on PARENT for App1",
            "user2, App1, PARENT, WRITE, True, user2 should own WRITE on PARENT for App1",
            "user2, App1, PARENT/CHILD, WRITE, True, user2 should own WRITE on each children of PARENT for App1",
            "user2, App1, PARENT, READ, True, user2 should own READ on PARENT for App1",
            "user2, App1, PARENT/CHILD, READ, True, user2 should own READ on PARENT for App1",
            "user3, App1, PARENT/CHILD, READ, True, user3 should own READ on PARENT/CHILD for App1"})
    public void checkUserRightPrivilege(String user, String application, String perimeter, String privilege, boolean expectation, String description) throws Exception {

        // Check with path Deal
        boolean hasPrivilege = userRightRestControllerMock.doesUserOwnPrivilegeForApplicationOnPerimeter(user, application, perimeter, privilege);
        Assertions.assertThat(hasPrivilege).as(description).isEqualTo(expectation);
    }

}
