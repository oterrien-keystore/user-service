package com.ote.user;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("Test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UserServiceRunner.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRightServiceTest {

    @Autowired
    private UserRightRestControllerMock userRightRestControllerMock;

    @Test
    public void checkUserPrivilege() throws Exception {

        SoftAssertions assertions = new SoftAssertions();

        // Check with path Deal
        boolean hasPrivilege = userRightRestControllerMock.doesUserOwnPrivilegeForApplicationOnPerimeter("olivier.terrien", "SLA", "DEAL", "READ_ONLY").isGranted();
        assertions.assertThat(hasPrivilege).as("check READ_ONLY for DEAL").isTrue();

        hasPrivilege = userRightRestControllerMock.doesUserOwnPrivilegeForApplicationOnPerimeter("olivier.terrien", "SLA", "DEAL", "READ_WRITE").isGranted();
        assertions.assertThat(hasPrivilege).as("check READ_WRITE for DEAL").isFalse();

        // Check with path Deal/GLE
        hasPrivilege = userRightRestControllerMock.doesUserOwnPrivilegeForApplicationOnPerimeter("olivier.terrien", "SLA", "DEAL/GLE", "READ_ONLY").isGranted();
        assertions.assertThat(hasPrivilege).as("check READ_ONLY for DEAL/GLE").isTrue();

        hasPrivilege = userRightRestControllerMock.doesUserOwnPrivilegeForApplicationOnPerimeter("olivier.terrien", "SLA", "DEAL/GLE", "READ_WRITE").isGranted();
        assertions.assertThat(hasPrivilege).as("check READ_WRITE for DEAL/GLE").isTrue();


        assertions.assertAll();
    }
}
