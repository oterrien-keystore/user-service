package com.ote.user.spring;

import com.ote.common.payload.SecurityGroupPayload;
import com.ote.common.persistence.model.SecurityGroupEntity;
import com.ote.common.persistence.model.UserEntity;
import com.ote.common.persistence.repository.ISecurityGroupJpaRepository;
import com.ote.common.persistence.repository.IUserJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assumptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@Service
@Profile("Test")
@Slf4j
public class UserRestControllerMock {

    private static final String URI = "/api/v1/users";

    @Autowired
    private IUserJpaRepository userRepository;

    @Autowired
    private ISecurityGroupJpaRepository securityGroupRepository;

    @Autowired
    private WebConfigurationMock webConfiguration;

    public List<SecurityGroupPayload> getSecurityGroups(String login) throws Exception {

        UserEntity userEntity = userRepository.findByLogin(login);
        Assumptions.assumeTrue(userEntity != null);

        return securityGroupRepository.findByUser(login).stream().map(SecurityGroupEntity::convert).collect(Collectors.toList());
    }

    public void addSecurityGroup(String login, String group) throws Exception {

        Assumptions.assumeTrue(userRepository.existsByLogin(login));
        Assumptions.assumeTrue(securityGroupRepository.existsByCode(group));

        UserEntity userEntity = userRepository.findByLogin(login);

        webConfiguration.getMockMvc().
                perform(put(URI + "/" + userEntity.getId() + "/securityGroups").
                        contentType(MediaType.TEXT_PLAIN_VALUE).
                        content(group)).
                andReturn();
    }

    public void removeSecurityGroup(String login, String group) throws Exception {

        Assumptions.assumeTrue(userRepository.existsByLogin(login));
        Assumptions.assumeTrue(securityGroupRepository.existsByCode(group));

        UserEntity userEntity = userRepository.findByLogin(login);

        webConfiguration.getMockMvc().
                perform(delete(URI + "/" + userEntity.getId() + "/securityGroups").
                        contentType(MediaType.TEXT_PLAIN_VALUE).
                        content(group)).
                andReturn();
    }
}
