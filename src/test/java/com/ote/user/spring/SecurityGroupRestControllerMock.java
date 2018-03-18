package com.ote.user.spring;

import com.ote.common.JsonUtils;
import com.ote.common.payload.RightPayload;
import com.ote.common.payload.SecurityGroupPayload;
import com.ote.common.payload.UserPayload;
import com.ote.common.persistence.model.SecurityGroupEntity;
import com.ote.common.persistence.repository.ISecurityGroupJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assumptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@Service
@Profile("Test")
@Slf4j
public class SecurityGroupRestControllerMock {

    private static final String URI = "/api/v1/securityGroups";

    @Autowired
    private ISecurityGroupJpaRepository securityGroupRepository;

    @Autowired
    private WebConfigurationMock webConfiguration;

    public SecurityGroupPayload find(String group, boolean withRights, boolean withUsers) throws Exception {

        SecurityGroupEntity securityGroupEntity = securityGroupRepository.findByCode(group);
        Assumptions.assumeTrue(securityGroupEntity != null);

        MvcResult result = webConfiguration.getMockMvc().
                perform(get(URI + "/" + securityGroupEntity.getId()).
                        param("withRights", Boolean.toString(withRights)).
                        param("withUsers", Boolean.toString(withUsers)).
                        contentType(MediaType.APPLICATION_JSON_VALUE)).
                andReturn();

        return JsonUtils.parse(result.getResponse().getContentAsString(), SecurityGroupPayload.class);
    }

    public void addRight(String group, String application, String perimeter, String privilege) throws Exception {

        SecurityGroupEntity securityGroupEntity = securityGroupRepository.findByCode(group);
        Assumptions.assumeTrue(securityGroupEntity != null);

        RightPayload payload = new RightPayload();
        payload.setApplication(application);
        payload.setPerimeter(perimeter);
        payload.setPrivilege(privilege);

        webConfiguration.getMockMvc().
                perform(put(URI + "/" + securityGroupEntity.getId() + "/rights").
                        contentType(MediaType.APPLICATION_JSON_VALUE).
                        content(JsonUtils.serialize(payload))).
                andReturn();
    }

    public void removeRight(String group, String application, String perimeter, String privilege) throws Exception {

        SecurityGroupEntity securityGroupEntity = securityGroupRepository.findByCode(group);
        Assumptions.assumeTrue(securityGroupEntity != null);

        RightPayload payload = new RightPayload();
        payload.setApplication(application);
        payload.setPerimeter(perimeter);
        payload.setPrivilege(privilege);

        webConfiguration.getMockMvc().
                perform(delete(URI + "/" + securityGroupEntity.getId() + "/rights").
                        contentType(MediaType.APPLICATION_JSON_VALUE).
                        content(JsonUtils.serialize(payload))).
                andReturn();
    }

    public List<UserPayload> getUsers(String group) throws Exception {

        SecurityGroupEntity securityGroupEntity = securityGroupRepository.findByCode(group);
        Assumptions.assumeTrue(securityGroupEntity != null);

        MockHttpServletResponse response = webConfiguration.getMockMvc().
                perform(get(URI + "/" + securityGroupEntity.getId() + "/users")).
                andReturn().getResponse();

        return JsonUtils.parseFromJsonList(response.getContentAsString(), UserPayload.class);
    }

    public void addUser(String group, String userLogin) throws Exception {

        SecurityGroupEntity securityGroupEntity = securityGroupRepository.findByCode(group);
        Assumptions.assumeTrue(securityGroupEntity != null);

        webConfiguration.getMockMvc().
                perform(put(URI + "/" + securityGroupEntity.getId() + "/users").
                        contentType(MediaType.TEXT_PLAIN_VALUE).
                        content(userLogin)).
                andReturn();
    }

    public void removeUser(String group, String userLogin) throws Exception {

        SecurityGroupEntity securityGroupEntity = securityGroupRepository.findByCode(group);
        Assumptions.assumeTrue(securityGroupEntity != null);

        webConfiguration.getMockMvc().
                perform(delete(URI + "/" + securityGroupEntity.getId() + "/users").
                        contentType(MediaType.TEXT_PLAIN_VALUE).
                        content(userLogin)).
                andReturn();
    }
}
