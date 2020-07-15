package com.poppulo.lottery.service;

import com.poppulo.lottery.model.dto.LotteryDTO;
import com.poppulo.lottery.model.dto.TicketLineDTO;

import java.util.Set;

public interface LotteryService {
    Set<LotteryDTO> getAllTickets();

    LotteryDTO createLotteryTicket(LotteryDTO lotteryDTO) throws Exception;

    LotteryDTO getTicket(Long id);

    LotteryDTO amendTicket(Long id, Set<TicketLineDTO> lotteryDTO) throws Exception;

    LotteryDTO getTicketStatus(Long id);
}
