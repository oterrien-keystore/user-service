package com.ote.user;

import com.ote.user.checker.UserRightPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@Service
@Profile("Test")
public class UserRightRestControllerMock {

    private static final String URI = "/api/v1/rights";

    @Autowired
    private WebConfigurationMock webConfiguration;

    public UserRightPayload doesUserOwnPrivilegeForApplicationOnPerimeter(String user, String application, String perimeter, String privilege) {
        try {
            MvcResult result = webConfiguration.getMockMvc().perform(get(URI).
                    contentType(MediaType.APPLICATION_JSON_VALUE).
                    param("user", user).
                    param("application", application).
                    param("perimeter", perimeter).
                    param("privilege", privilege)).
                    andReturn();

            return JsonUtils.parse(result.getResponse().getContentAsString(), UserRightPayload.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
