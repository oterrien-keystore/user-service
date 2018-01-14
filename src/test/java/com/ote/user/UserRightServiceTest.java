package com.ote.user;

import com.ote.UserServiceRunner;
import org.assertj.core.api.Assertions;
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

    @ParameterizedTest
    @CsvSource({
            "user1, TEST_SERVICE, PARENT, READ, TRUE",
            "user1, TEST_SERVICE, PARENT, WRITE, FALSE",
            "user1, TEST_SERVICE, PARENT/CHILD, READ, TRUE",
            "user1, TEST_SERVICE, PARENT/CHILD, WRITE, TRUE"})
    public void checkUserPrivilege(String user, String application, String perimeter, String privilege, boolean expectation) {

        // Check with path Deal
        boolean hasPrivilege = userRightRestControllerMock.doesUserOwnPrivilegeForApplicationOnPerimeter(user, application, perimeter, privilege);
        Assertions.assertThat(hasPrivilege).as("check READ for PARENT").isEqualTo(expectation);
    }
}
