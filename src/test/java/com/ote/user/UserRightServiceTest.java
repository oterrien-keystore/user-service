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
            "user1, TEST_SERVICE, PARENT, READ, TRUE, user1 should own READ on PARENT",
            "user1, TEST_SERVICE, PARENT, WRITE, FALSE, user should not own WRITE on PARENT",
            "user1, TEST_SERVICE, PARENT/CHILD, READ, TRUE, user1 should own READ on PARENT/CHILD (inherited from PARENT)",
            "user1, TEST_SERVICE, PARENT/CHILD, WRITE, TRUE, user1 should own WRITE on PARENT/CHILD",
            "user1, TEST_SERVICE, PARENT, ADMIN, TRUE, user1 should own ADMIN on PARENT",
            "user1, TEST_SERVICE, PARENT/CHILD, ADMIN, TRUE, user1 should own ADMIN on PARENT/CHILD (inherited from PARENT)",
            "user2, TEST_SERVICE, PARENT, WRITE, FALSE, user2 should not own WRITE on PARENT",
            "user2, TEST_SERVICE, PARENT/CHILD, READ, TRUE, user2 should own READ on PARENT",
            "user2, TEST_SERVICE, PARENT, READ, TRUE, user2 should own READ on PARENT from SecurityGroup",
            "user3, TEST_SERVICE, PARENT/CHILD, READ, TRUE, user3 should own READ on PARENT/CHILD"})
    public void checkUserRightPrivilege(String user, String application, String perimeter, String privilege, boolean expectation, String description) {

        // Check with path Deal
        boolean hasPrivilege = userRightRestControllerMock.doesUserOwnPrivilegeForApplicationOnPerimeter(user, application, perimeter, privilege);
        Assertions.assertThat(hasPrivilege).as(description).isEqualTo(expectation);
    }
}
