package com.poppulo.lottery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.poppulo.lottery.enums.TicketStatus;
import com.poppulo.lottery.model.TicketLine;
import com.poppulo.lottery.model.dto.LotteryDTO;
import com.poppulo.lottery.model.dto.TicketLineDTO;
import com.poppulo.lottery.model.dto.UserDTO;
import com.poppulo.lottery.service.LotteryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.lang.reflect.Array;
import java.util.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LotteryControllerTest {

    @LocalServerPort
    private int port = 9090;
    public WireMockServer wireMockServer = new WireMockServer(port);

    private MockMvc mockMvc;

    @MockBean
    public LotteryService lotteryService;

    @Autowired
    private LotteryController lotteryController;

    @Before
    public void setUp() throws Exception {

        wireMockServer.stubFor(get(urlMatching("/api/v1/lottery/.*")).willReturn(
                aResponse()
                        .withStatus(200)
        ));

        mockMvc = MockMvcBuilders.standaloneSetup(lotteryController)
                .addPlaceholderValue("cors.allowed.origin", "*")
                .build();

        LotteryDTO lotteryDTO1 = new LotteryDTO();
        lotteryDTO1.setId(1L);

        TicketLineDTO ticketLineDTO1 = new TicketLineDTO();
        ticketLineDTO1.setId(1L);
        ticketLineDTO1.setTicketNumber("111");
        ticketLineDTO1.setPoints(5);

        TicketLineDTO ticketLineDTO2 = new TicketLineDTO();
        ticketLineDTO2.setId(2L);
        ticketLineDTO2.setTicketNumber("101");
        ticketLineDTO2.setPoints(10);

        TicketLineDTO ticketLineDTO3 = new TicketLineDTO();
        ticketLineDTO3.setId(3L);
        ticketLineDTO3.setTicketNumber("100");
        ticketLineDTO3.setPoints(1);

        TicketLineDTO ticketLineDTO4 = new TicketLineDTO();
        ticketLineDTO4.setId(3L);
        ticketLineDTO4.setTicketNumber("220");
        ticketLineDTO4.setPoints(0);

        lotteryDTO1.setLines(new TreeSet<>(Arrays.asList(ticketLineDTO1, ticketLineDTO2, ticketLineDTO3, ticketLineDTO4)));

        LotteryDTO lotteryDTO2 = new LotteryDTO();
        lotteryDTO1.setId(2L);

        TicketLineDTO ticketLineDTO5 = new TicketLineDTO();
        ticketLineDTO5.setId(1L);
        ticketLineDTO5.setTicketNumber("222");
        ticketLineDTO5.setPoints(10);

        TicketLineDTO ticketLineDTO6 = new TicketLineDTO();
        ticketLineDTO6.setId(2L);
        ticketLineDTO6.setTicketNumber("121");
        ticketLineDTO6.setPoints(0);

        lotteryDTO2.setLines(new HashSet<>(Arrays.asList(ticketLineDTO5, ticketLineDTO6)));
        Set<LotteryDTO> tickets = new HashSet<>(Arrays.asList(lotteryDTO1, lotteryDTO2));
        Mockito.when(lotteryService.getAllTickets()).thenReturn(tickets);
    }

    @Test
    public void getAllTicketsReturn200() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/lottery/ticket")
                .header("Content-Type", "application/json"))
                .andExpect(MockMvcResultMatchers.status().is(200));

        Mockito.verify(lotteryService).getAllTickets();

    }

    @Test
    public void createTicketReturn200() throws Exception {

        LotteryDTO lotteryDTO = new LotteryDTO();
        lotteryDTO.setId(1L);

        TicketLineDTO ticketLineDTO1 = new TicketLineDTO();
        ticketLineDTO1.setTicketNumber("111");

        TicketLineDTO ticketLineDTO2 = new TicketLineDTO();
        ticketLineDTO2.setTicketNumber("101");

        TicketLineDTO ticketLineDTO3 = new TicketLineDTO();
        ticketLineDTO3.setTicketNumber("100");

        TicketLineDTO ticketLineDTO4 = new TicketLineDTO();
        ticketLineDTO4.setTicketNumber("220");

        lotteryDTO.setLines(new TreeSet<>(Arrays.asList(ticketLineDTO1, ticketLineDTO2, ticketLineDTO3, ticketLineDTO4)));

        //Output object
        LotteryDTO lotteryOutDTO = new LotteryDTO();
        lotteryOutDTO.setId(1L);

        TicketLineDTO ticketLineOutDTO1 = new TicketLineDTO();
        ticketLineOutDTO1.setTicketNumber("111");
        ticketLineOutDTO1.setId(1L);
        ticketLineOutDTO1.setPoints(5);

        TicketLineDTO ticketLineOutDTO2 = new TicketLineDTO();
        ticketLineOutDTO2.setTicketNumber("101");
        ticketLineOutDTO2.setId(2L);
        ticketLineOutDTO2.setPoints(10);

        TicketLineDTO ticketLineOutDTO3 = new TicketLineDTO();
        ticketLineOutDTO3.setTicketNumber("100");
        ticketLineOutDTO3.setId(3L);
        ticketLineOutDTO3.setPoints(1);

        TicketLineDTO ticketLineOutDTO4 = new TicketLineDTO();
        ticketLineOutDTO4.setTicketNumber("220");
        ticketLineOutDTO4.setId(4L);
        ticketLineOutDTO4.setPoints(0);

        lotteryOutDTO.setLines(new TreeSet<>(Arrays.asList(ticketLineOutDTO1, ticketLineOutDTO2, ticketLineOutDTO3, ticketLineOutDTO4)));

        Mockito.when(lotteryService.createLotteryTicket(Mockito.any(LotteryDTO.class))).thenReturn(lotteryOutDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/lottery/ticket")
                .content(asJsonString(lotteryDTO))
                .header("Content-Type", "application/json"))
                .andExpect(MockMvcResultMatchers.status().is(200));

        Mockito.verify(lotteryService).createLotteryTicket(lotteryDTO);

    }

    @Test
    public void getTicketReturn200() throws Exception {
        //Output object
        LotteryDTO lotteryOutDTO = new LotteryDTO();
        lotteryOutDTO.setId(1L);

        TicketLineDTO ticketLineOutDTO1 = new TicketLineDTO();
        ticketLineOutDTO1.setTicketNumber("111");
        ticketLineOutDTO1.setId(1L);
        ticketLineOutDTO1.setPoints(5);

        TicketLineDTO ticketLineOutDTO2 = new TicketLineDTO();
        ticketLineOutDTO2.setTicketNumber("101");
        ticketLineOutDTO2.setId(2L);
        ticketLineOutDTO2.setPoints(10);

        TicketLineDTO ticketLineOutDTO3 = new TicketLineDTO();
        ticketLineOutDTO3.setTicketNumber("100");
        ticketLineOutDTO3.setId(3L);
        ticketLineOutDTO3.setPoints(1);

        TicketLineDTO ticketLineOutDTO4 = new TicketLineDTO();
        ticketLineOutDTO4.setTicketNumber("220");
        ticketLineOutDTO4.setId(4L);
        ticketLineOutDTO4.setPoints(0);

        lotteryOutDTO.setLines(new TreeSet<>(Arrays.asList(ticketLineOutDTO1, ticketLineOutDTO2, ticketLineOutDTO3, ticketLineOutDTO4)));

        Mockito.when(lotteryService.getTicket(1L)).thenReturn(lotteryOutDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/lottery/ticket/1")
                .header("Content-Type", "application/json"))
                .andExpect(MockMvcResultMatchers.status().is(200));

        Mockito.verify(lotteryService).getTicket(1L);

    }

    @Test
    public void amendTicketReturn200() throws Exception {
        //Output object
        TicketLineDTO ticketLineInputDTO1 = new TicketLineDTO();
        ticketLineInputDTO1.setTicketNumber("212");

        TicketLineDTO ticketLineInputDTO2 = new TicketLineDTO();
        ticketLineInputDTO2.setTicketNumber("211");

        Set<TicketLineDTO> inputDTOS = new HashSet<>(Arrays.asList(ticketLineInputDTO1, ticketLineInputDTO2));

        LotteryDTO lotteryOutDTO = new LotteryDTO();
        lotteryOutDTO.setId(1L);

        TicketLineDTO ticketLineOutDTO1 = new TicketLineDTO();
        ticketLineOutDTO1.setTicketNumber("111");
        ticketLineOutDTO1.setId(1L);
        ticketLineOutDTO1.setPoints(5);

        TicketLineDTO ticketLineOutDTO2 = new TicketLineDTO();
        ticketLineOutDTO2.setTicketNumber("101");
        ticketLineOutDTO2.setId(2L);
        ticketLineOutDTO2.setPoints(10);

        TicketLineDTO ticketLineOutDTO3 = new TicketLineDTO();
        ticketLineOutDTO3.setTicketNumber("100");
        ticketLineOutDTO3.setId(3L);
        ticketLineOutDTO3.setPoints(1);

        TicketLineDTO ticketLineOutDTO4 = new TicketLineDTO();
        ticketLineOutDTO4.setTicketNumber("220");
        ticketLineOutDTO4.setId(4L);
        ticketLineOutDTO4.setPoints(0);

        TicketLineDTO ticketLineOutDTO5 = new TicketLineDTO();
        ticketLineOutDTO5.setTicketNumber("212");
        ticketLineOutDTO5.setId(5L);
        ticketLineOutDTO5.setPoints(0);

        TicketLineDTO ticketLineOutDTO6 = new TicketLineDTO();
        ticketLineOutDTO6.setTicketNumber("211");
        ticketLineOutDTO6.setId(6L);
        ticketLineOutDTO6.setPoints(1);

        lotteryOutDTO.setLines(new TreeSet<>(Arrays.asList(ticketLineOutDTO1, ticketLineOutDTO2, ticketLineOutDTO3, ticketLineOutDTO4, ticketLineOutDTO5, ticketLineOutDTO6)));
        Mockito.when(lotteryService.amendTicket(new Long(Mockito.anyLong()), Mockito.anySet())).thenReturn(lotteryOutDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/lottery/ticket/1")
                .content(asJsonString(inputDTOS))
                .header("Content-Type", "application/json"))
                .andExpect(MockMvcResultMatchers.status().is(200));

    }

    @Test
    public void getTicketStatus200() throws Exception {
        //Output object
        LotteryDTO lotteryOutDTO = new LotteryDTO();
        lotteryOutDTO.setId(1L);
        lotteryOutDTO.setStatus(TicketStatus.CHECKED);

        TicketLineDTO ticketLineOutDTO1 = new TicketLineDTO();
        ticketLineOutDTO1.setTicketNumber("111");
        ticketLineOutDTO1.setId(1L);
        ticketLineOutDTO1.setPoints(5);

        TicketLineDTO ticketLineOutDTO2 = new TicketLineDTO();
        ticketLineOutDTO2.setTicketNumber("101");
        ticketLineOutDTO2.setId(2L);
        ticketLineOutDTO2.setPoints(10);

        TicketLineDTO ticketLineOutDTO3 = new TicketLineDTO();
        ticketLineOutDTO3.setTicketNumber("100");
        ticketLineOutDTO3.setId(3L);
        ticketLineOutDTO3.setPoints(1);

        TicketLineDTO ticketLineOutDTO4 = new TicketLineDTO();
        ticketLineOutDTO4.setTicketNumber("220");
        ticketLineOutDTO4.setId(4L);
        ticketLineOutDTO4.setPoints(0);

        lotteryOutDTO.setLines(new TreeSet<>(Arrays.asList(ticketLineOutDTO1, ticketLineOutDTO2, ticketLineOutDTO3, ticketLineOutDTO4)));
        Mockito.when(lotteryService.getTicketStatus(1L)).thenReturn(lotteryOutDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/lottery/status/1")
                .header("Content-Type", "application/json"))
                .andExpect(MockMvcResultMatchers.status().is(200));

        Mockito.verify(lotteryService).getTicketStatus(1L);
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
