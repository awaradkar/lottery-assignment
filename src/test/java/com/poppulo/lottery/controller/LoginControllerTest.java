package com.poppulo.lottery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.poppulo.lottery.model.dto.UserDTO;
import com.poppulo.lottery.security.JwtTokenUtil;
import com.poppulo.lottery.service.UserService;
import com.poppulo.lottery.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginControllerTest {

    @LocalServerPort
    private int port = 9090;
    public WireMockServer wireMockServer = new WireMockServer(port);

    private MockMvc mockMvc;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    UserService userService;

    @Autowired
    private LoginController loginController;

    UserDTO dto;

    @Before
    public void setUp() throws Exception {

        wireMockServer.stubFor(get(urlMatching("/api/v1/login/.*")).willReturn(
                aResponse()
                        .withStatus(200)
        ));

        mockMvc = MockMvcBuilders.standaloneSetup(loginController)
                .addPlaceholderValue("cors.allowed.origin", "*")
                .build();

        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBbmlydWRoOiQyYSQxMCRJLmxoUTMzSzhwcVcuNkc2LmJBbG1lUklBT1ZCcXQuSmtIQWhxbVZicmZyUlV" +
                "jenVOL3ZrZTpbQWRtaW5dIiwiZXhwIjoxNTk0ODIzMjgwLCJpYXQiOjE1OTQ4MDUyODB9.-afkH9kMmpbgk_w7DT8e0ezI1XAcAHMgpp1sW5q0mI5cb5sD3CA" +
                "nKgxAc5gdoudzuTp_udYGdy8Qjlv_jT0nPA";

        dto = new UserDTO();
        dto.setUserName("Anirudh");
        dto.setUserPassword("mumbai@123");
        dto.setUserRole("Admin");

        UserDTO userDTO= new UserDTO();
        userDTO.setUserId(UUID.fromString("64c5b323-fd1e-4553-a5a3-5eaf9048e512"));
        userDTO.setUserName("Anirudh");
        userDTO.setUserPassword("$2a$10$I.lhQ33K8pqW.6G6.bAlmeRIAOVBqt.JkHAhqmVbrfrRUczuN/vke");
        userDTO.setUserRole("Admin");

        Mockito.when(userService.loginUser(dto)).thenReturn(userDTO);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userDTO.getUserRole()));

        final UserDetails userDetails = new User(userDTO.getUserName(), userDTO.getUserPassword(),
                authorities);

        Mockito.when(jwtTokenUtil.generateToken(userDetails)).thenReturn(token);

        userDTO.setToken(token);
        userDTO.setUserPassword("");
    }

    @Test
    public void loginReturns200() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/login")
                .content(asJsonString(dto))
                .header("Content-Type", "application/json"))
                .andExpect(MockMvcResultMatchers.status().is(200));

        Mockito.verify(userService).loginUser(dto);

    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
