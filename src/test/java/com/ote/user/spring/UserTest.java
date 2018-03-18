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
public class UserTest {

    @Autowired
    private UserRestControllerMock userRestController;



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
            "user1, Readers, user1 should now belong to group 'Readers'",
            "user3, Readers, user3 should still belong to group 'Readers'"})
    @WithMockUser(username = "user1", password = "password")
    public void addSecurityGroup(String user, String securityGroup) throws Exception {

        userRestController.addSecurityGroup(user, securityGroup);
        List<SecurityGroupPayload> securityGroups = userRestController.getSecurityGroups(user);

        Assertions.assertThat(securityGroups.stream().map(SecurityGroupPayload::getCode).collect(Collectors.toList())).contains(securityGroup);

    }

    @ParameterizedTest
    @CsvSource({
            "user1, Readers, user1 should now belong to group 'Readers'",
            "user3, Readers, user3 should still belong to group 'Readers'"})
    @WithMockUser(username = "user1", password = "password")
    public void removeSecurityGroup(String user, String securityGroup) throws Exception {

        userRestController.removeSecurityGroup(user, securityGroup);
        List<SecurityGroupPayload> securityGroups = userRestController.getSecurityGroups(user);

        Assertions.assertThat(securityGroups.stream().map(SecurityGroupPayload::getCode).collect(Collectors.toList())).doesNotContain(securityGroup);

    }
}
