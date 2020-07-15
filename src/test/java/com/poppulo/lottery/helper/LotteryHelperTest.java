package com.poppulo.lottery.helper;

import com.poppulo.lottery.model.Lottery;
import com.poppulo.lottery.model.TicketLine;
import com.poppulo.lottery.model.dto.LotteryDTO;
import com.poppulo.lottery.model.dto.TicketLineDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class LotteryHelperTest {

    @InjectMocks
    private LotteryHelper lotteryHelper;

    @Test
    public void testGetLotteryDTOFromModel(){
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

        LotteryDTO lotteryDTOS = lotteryHelper.getLotteryDTOFromModel(lottery1);
        assertEquals(lottery1.getLines().size(), lotteryDTOS.getLines().size());
        assertEquals(lottery1.getId(), lotteryDTOS.getId());
        assertEquals(lottery1.getStatus(), lotteryDTOS.getStatus());
    }

    @Test
    public void testProcessTickets() throws Exception {
        TicketLineDTO ticketLineInputDTO1 = new TicketLineDTO();
        ticketLineInputDTO1.setTicketNumber("212");

        TicketLineDTO ticketLineInputDTO2 = new TicketLineDTO();
        ticketLineInputDTO2.setTicketNumber("211");

        TicketLineDTO ticketLineInputDTO3 = new TicketLineDTO();
        ticketLineInputDTO3.setTicketNumber("222");

        TicketLineDTO ticketLineInputDTO4 = new TicketLineDTO();
        ticketLineInputDTO4.setTicketNumber("101");

        Set<TicketLineDTO> inputDTOS = new HashSet<>(Arrays.asList(ticketLineInputDTO1, ticketLineInputDTO2, ticketLineInputDTO3, ticketLineInputDTO4));

        Set<TicketLine> ticketLines = lotteryHelper.processTickets(inputDTOS, new Lottery());

        assertEquals(ticketLines.size(), 4);

        for (TicketLine ticketLine:ticketLines){
            if(ticketLine.getTicketNumber().equals("212")) assertEquals(0, ticketLine.getPoints());
            else if (ticketLine.getTicketNumber().equals("211")) assertEquals(1, ticketLine.getPoints());
            else if (ticketLine.getTicketNumber().equals("222")) assertEquals(5, ticketLine.getPoints());
            else if (ticketLine.getTicketNumber().equals("101")) assertEquals(10, ticketLine.getPoints());
        }
    }
}
