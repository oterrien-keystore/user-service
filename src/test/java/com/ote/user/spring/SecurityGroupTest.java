package com.ote.user.spring;

import com.ote.UserServiceRunner;
import com.ote.common.payload.RightPayload;
import com.ote.common.payload.SecurityGroupPayload;
import com.ote.common.payload.UserPayload;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.stream.Collectors;

@ActiveProfiles({"Test", "mockUserRight"})
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = UserServiceRunner.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class SecurityGroupTest {

    @Autowired
    private SecurityGroupRestControllerMock securityGroupRestController;

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
            "Readers, App2, PARENT, READ, True, Group 'Readers' should now have READ on PARENT for App2 once added",
            "Readers, App1, PARENT, READ, True, Group 'Readers' should already have READ on PARENT for App1 once added"})
    @WithMockUser(username = "user1", password = "password")
    public void addRight(String group, String application, String perimeter, String privilege, boolean expectation, String description) throws Exception {

        securityGroupRestController.addRight(group, application, perimeter, privilege);

        SecurityGroupPayload securityGroupPayload = securityGroupRestController.find(group, true, false);

        RightPayload rightPayload = new RightPayload();
        rightPayload.setApplication(application);
        rightPayload.setPerimeter(perimeter);
        rightPayload.setPrivilege(privilege);

        Assertions.assertThat(securityGroupPayload.getRights().stream().anyMatch(p -> p.equals(rightPayload))).
                as(description).
                isEqualTo(expectation);
    }

    @ParameterizedTest
    @CsvSource({
            "Readers, App2, PARENT, READ, False, Group 'Readers' should not have READ on PARENT for App2 once removed",
            "Readers, App1, PARENT, READ, False, Group 'Readers' should not have READ on PARENT for App1 once removed"})
    @WithMockUser(username = "user1", password = "password")
    public void removeRight(String group, String application, String perimeter, String privilege, boolean expectation, String description) throws Exception {

        securityGroupRestController.removeRight(group, application, perimeter, privilege);

        SecurityGroupPayload securityGroupPayload = securityGroupRestController.find(group, true, false);

        RightPayload rightPayload = new RightPayload();
        rightPayload.setApplication(application);
        rightPayload.setPerimeter(perimeter);
        rightPayload.setPrivilege(privilege);

        Assertions.assertThat(securityGroupPayload.getRights().stream().anyMatch(p -> p.equals(rightPayload))).
                as(description).
                isEqualTo(expectation);
    }

    @ParameterizedTest
    @CsvSource({
            "Readers, user1, user1 should now belong to group 'Readers'",
            "Readers, user3, user3 should still belong to group 'Readers'"})
    @WithMockUser(username = "user1", password = "password")
    public void addUsers(String group, String user) throws Exception {

        securityGroupRestController.addUser(group, user);
        List<UserPayload> users = securityGroupRestController.getUsers(group);

        Assertions.assertThat(users.stream().map(UserPayload::getLogin).collect(Collectors.toList())).contains(user);

    }
}
