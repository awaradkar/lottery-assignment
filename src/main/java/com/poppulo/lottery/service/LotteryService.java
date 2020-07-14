package com.poppulo.lottery.service;

import com.poppulo.lottery.model.dto.LotteryDTO;

import java.util.Set;

public interface LotteryService {
    Set<LotteryDTO> getAllTickets();

    LotteryDTO createLotteryTicket(LotteryDTO lotteryDTO) throws Exception;
}
