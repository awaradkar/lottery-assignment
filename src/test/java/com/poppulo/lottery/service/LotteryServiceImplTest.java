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

    @Test
    public void testGetTicketById() throws IOException {
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
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(lottery1));
        LotteryDTO lotteryDTO1 = new LotteryDTO();
        lotteryDTO1.setId(lottery1.getId());
        TicketLineDTO ticketLineOutDTO1 = new TicketLineDTO();
        ticketLineOutDTO1.setTicketNumber("111");
        ticketLineOutDTO1.setId(1L);
        ticketLineOutDTO1.setPoints(5);

        TicketLineDTO ticketLineOutDTO2 = new TicketLineDTO();
        ticketLineOutDTO2.setTicketNumber("101");
        ticketLineOutDTO2.setId(2L);
        ticketLineOutDTO2.setPoints(10);
        lotteryDTO1.setLines(new TreeSet<>(Arrays.asList(ticketLineOutDTO1, ticketLineOutDTO2)));

        Mockito.when(lotteryHelper.getLotteryDTOFromModel(lottery1)).thenReturn(lotteryDTO1);

        LotteryDTO lotteryDTO = lotteryService.getTicket(1L);
        assertEquals(1L, lotteryDTO.getId());
        assertEquals(2, lotteryDTO.getLines().size());
    }

    @Test
    public void testAmendTicketById() throws Exception {

        TicketLineDTO ticketLineInputDTO1 = new TicketLineDTO();
        ticketLineInputDTO1.setTicketNumber("212");

        TicketLineDTO ticketLineInputDTO2 = new TicketLineDTO();
        ticketLineInputDTO2.setTicketNumber("211");

        Set<TicketLineDTO> inputDTOS = new HashSet<>(Arrays.asList(ticketLineInputDTO1, ticketLineInputDTO2));

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

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(lottery1));

        TicketLine ticketLine3 = new TicketLine();
        ticketLine3.setId(3L);
        ticketLine3.setTicketNumber("212");
        ticketLine3.setPoints(0);

        TicketLine ticketLine4 = new TicketLine();
        ticketLine4.setId(4L);
        ticketLine4.setTicketNumber("211");
        ticketLine4.setPoints(1);
        lottery1.getLines().addAll(new HashSet<>(Arrays.asList(ticketLine3, ticketLine4)));

        Mockito.when(lotteryHelper.processTickets(Mockito.anySet(), Mockito.any())).thenReturn(new HashSet<>(Arrays.asList(ticketLine3, ticketLine4)));
        Mockito.when(repository.save(Mockito.any(Lottery.class))).thenReturn(lottery1);

        LotteryDTO lotteryDTO1 = new LotteryDTO();
        lotteryDTO1.setId(lottery1.getId());
        TicketLineDTO ticketLineOutDTO1 = new TicketLineDTO();
        ticketLineOutDTO1.setTicketNumber("111");
        ticketLineOutDTO1.setId(1L);
        ticketLineOutDTO1.setPoints(5);

        TicketLineDTO ticketLineOutDTO2 = new TicketLineDTO();
        ticketLineOutDTO2.setTicketNumber("101");
        ticketLineOutDTO2.setId(2L);
        ticketLineOutDTO2.setPoints(10);

        TicketLineDTO ticketLineOutDTO5 = new TicketLineDTO();
        ticketLineOutDTO5.setTicketNumber("212");
        ticketLineOutDTO5.setId(5L);
        ticketLineOutDTO5.setPoints(0);

        TicketLineDTO ticketLineOutDTO6 = new TicketLineDTO();
        ticketLineOutDTO6.setTicketNumber("211");
        ticketLineOutDTO6.setId(6L);
        ticketLineOutDTO6.setPoints(1);

        lotteryDTO1.setLines(new TreeSet<>(Arrays.asList(ticketLineOutDTO1, ticketLineOutDTO2, ticketLineOutDTO5, ticketLineOutDTO6)));
        Mockito.when(lotteryHelper.getLotteryDTOFromModel(lottery1)).thenReturn(lotteryDTO1);

        LotteryDTO lotteryDTO = lotteryService.amendTicket(1L, inputDTOS);

        assertEquals(1L, lotteryDTO.getId());
        assertEquals(4, lotteryDTO.getLines().size());
    }

    @Test
    public void testTicketGetStatusById() throws Exception {

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

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(lottery1));
        lottery1.setStatus(TicketStatus.CHECKED);

        Mockito.lenient().when(repository.save(lottery1)).thenReturn(lottery1);

        LotteryDTO lotteryDTO1 = new LotteryDTO();
        lotteryDTO1.setId(lottery1.getId());
        TicketLineDTO ticketLineOutDTO1 = new TicketLineDTO();
        ticketLineOutDTO1.setTicketNumber("111");
        ticketLineOutDTO1.setId(1L);
        ticketLineOutDTO1.setPoints(5);

        TicketLineDTO ticketLineOutDTO2 = new TicketLineDTO();
        ticketLineOutDTO2.setTicketNumber("101");
        ticketLineOutDTO2.setId(2L);
        ticketLineOutDTO2.setPoints(10);

        lotteryDTO1.setStatus(lottery1.getStatus());

        lotteryDTO1.setLines(new TreeSet<>(Arrays.asList(ticketLineOutDTO1, ticketLineOutDTO2)));
        Mockito.when(lotteryHelper.getLotteryDTOFromModel(lottery1)).thenReturn(lotteryDTO1);

        LotteryDTO lotteryDTO = lotteryService.getTicket(1L);

        assertEquals(1L, lotteryDTO.getId());
        assertEquals(TicketStatus.CHECKED, lotteryDTO.getStatus());
    }
}
