package by.jrr.profile.controller;

import by.jrr.constant.Endpoint;
import by.jrr.profile.model.SubscriptionDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
//@Sql("/data.sql") //todo: make sure integration test don't change DB state against CI-Prod databases;
@WithMockUser
class SubscriptionControllerTest {


    @Autowired
    MockMvc mockMvc;

    @Test
    void requestSubscription() throws Exception {
        RequestBuilder request = post(Endpoint.SUBSCRIPTIONS_REQUEST)
                .contentType("application/x-www-form-urlencoded")
                .param(SubscriptionDto.FIELD_USER_PROFILE_ID, "10")
                .param(SubscriptionDto.FIELD_STREAM_PROFILE_ID, "16")
                .param(SubscriptionDto.FIELD_NOTES, "text notes");

        this.mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk());
    }
}
