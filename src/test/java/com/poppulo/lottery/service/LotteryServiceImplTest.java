package com.poppulo.lottery.service;

import com.poppulo.lottery.enums.TicketStatus;
import com.poppulo.lottery.helper.LotteryHelper;
import com.poppulo.lottery.model.Lottery;
import com.poppulo.lottery.model.TicketLine;
import com.poppulo.lottery.model.dto.LotteryDTO;
import com.poppulo.lottery.model.dto.TicketLineDTO;
import com.poppulo.lottery.repository.LotteryRepository;
import com.poppulo.lottery.service.impl.LotteryServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class LotteryServiceImplTest {

    @Mock
    private LotteryHelper lotteryHelper;

    @Mock
    private LotteryRepository repository;

    @InjectMocks
    private LotteryServiceImpl lotteryService;

    @Before
    public void setUp() throws Exception {

        Lottery lottery1 = new Lottery();
        lottery1.setId(1L);

        TicketLine ticketLine1 = new TicketLine();
        ticketLine1.setId(1L);
        ticketLine1.setTicketNumber("111");
        ticketLine1.setPoints(5);

        TicketLine ticketLine2 = new TicketLine();
        ticketLine2.setId(2L);
        ticketLine2.setTicketNumber("101");
        ticketLine2.setPoints(10);

        lottery1.setLines(new HashSet<>(Arrays.asList(ticketLine1, ticketLine2)));

        Lottery lottery2 = new Lottery();
        lottery2.setId(2L);

        TicketLine ticketLine3 = new TicketLine();
        ticketLine3.setId(1L);
        ticketLine3.setTicketNumber("222");
        ticketLine3.setPoints(10);

        TicketLine ticketLine4 = new TicketLine();
        ticketLine4.setId(2L);
        ticketLine4.setTicketNumber("121");
        ticketLine4.setPoints(0);

        lottery2.setLines(new HashSet<>(Arrays.asList(ticketLine3, ticketLine4)));
        List<Lottery> tickets = Arrays.asList(lottery1, lottery2);

        Mockito.when(repository.findAll()).thenReturn(tickets);
        LotteryDTO lotteryDTO1 = new LotteryDTO();lotteryDTO1.setId(lottery1.getId());
        LotteryDTO lotteryDTO2 = new LotteryDTO();lotteryDTO2.setId(lottery2.getId());
        Mockito.when(lotteryHelper.getLotteryDTOFromModel(lottery1)).thenReturn(lotteryDTO1);
        Mockito.when(lotteryHelper.getLotteryDTOFromModel(lottery2)).thenReturn(lotteryDTO2);


    }

    @Test
    public void testGetAllticketsCount() throws IOException {
        Set<LotteryDTO> lotteryDTOS = lotteryService.getAllTickets();
        assertEquals(2, lotteryDTOS.size());
    }

    @Test
    public void createticketForSuccess() throws Exception {
        LotteryDTO lotteryDTO = new LotteryDTO();

        TicketLineDTO ticketLineDTO1 = new TicketLineDTO();
        ticketLineDTO1.setTicketNumber("111");

        TicketLineDTO ticketLineDTO2 = new TicketLineDTO();
        ticketLineDTO2.setTicketNumber("101");

        lotteryDTO.setStatus(TicketStatus.UNCHECKED);
        lotteryDTO.setLines(new TreeSet<>(Arrays.asList(ticketLineDTO1, ticketLineDTO2)));

        Lottery lottery1 = new Lottery();
        lottery1.setId(1L);

        TicketLine ticketLine1 = new TicketLine();
        ticketLine1.setId(1L);
        ticketLine1.setTicketNumber("111");
        ticketLine1.setPoints(5);

        TicketLine ticketLine2 = new TicketLine();
        ticketLine2.setId(2L);
        ticketLine2.setTicketNumber("101");
        ticketLine2.setPoints(10);

        lottery1.setLines(new HashSet<>(Arrays.asList(ticketLine1, ticketLine2)));

        Mockito.when(lotteryHelper.processTickets(Mockito.anySet(), Mockito.any())).thenReturn(new HashSet<>(Arrays.asList(ticketLine1, ticketLine2)));
        Mockito.when(repository.save(Mockito.any(Lottery.class))).thenReturn(lottery1);

        LotteryDTO lotteryDTO1 = new LotteryDTO();lotteryDTO1.setId(lottery1.getId());
        Mockito.when(lotteryHelper.getLotteryDTOFromModel(lottery1)).thenReturn(lotteryDTO1);

        LotteryDTO lotteryDTOOP = lotteryService.createLotteryTicket(lotteryDTO);
        assertEquals(lotteryDTOOP, lotteryDTO1);
    }
}
