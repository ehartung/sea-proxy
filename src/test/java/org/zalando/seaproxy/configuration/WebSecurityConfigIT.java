package org.zalando.seaproxy.configuration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.mockito.MockitoAnnotations;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;

import org.springframework.http.MediaType;

import org.springframework.security.web.FilterChainProxy;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.web.context.WebApplicationContext;

import org.zalando.seaproxy.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class WebSecurityConfigIT {

    private static final String FULL_AUTHENTICATION_REQUIRED =
        "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
            + "<oauth><error_description>Full authentication is required to access this resource</error_description>"
            + "<error>unauthorized</error></oauth>";

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Resource
    private FilterChainProxy springSecurityFilterChain;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // Setup web app context including Spring security filter chain
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilter(springSecurityFilterChain)
                                 .build();
    }

    @Test
    public void testShouldAllowUnauhorizedAccessToStatusPage() throws Exception {
        mockMvc.perform(get("/status.info").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(
            content().json("{status:\"OK\"}"));
    }

    @Test
    public void testShouldNotAllowUnauhorizedRequests() throws Exception {
        mockMvc.perform(get("/index").accept(MediaType.APPLICATION_XML)).andExpect(status().isUnauthorized()).andExpect(
            content().xml(FULL_AUTHENTICATION_REQUIRED));
    }
}
