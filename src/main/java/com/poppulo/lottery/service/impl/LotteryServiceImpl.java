package com.poppulo.lottery.service.impl;

import com.poppulo.lottery.enums.TicketStatus;
import com.poppulo.lottery.helper.LotteryHelper;
import com.poppulo.lottery.model.Lottery;
import com.poppulo.lottery.model.TicketLine;
import com.poppulo.lottery.model.dto.LotteryDTO;
import com.poppulo.lottery.model.dto.TicketLineDTO;
import com.poppulo.lottery.repository.LotteryRepository;
import com.poppulo.lottery.service.LotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class LotteryServiceImpl implements LotteryService {

    @Autowired
    LotteryRepository lotteryRepository;

    @Autowired
    LotteryHelper lotteryHelper;

    @Override
    public Set<LotteryDTO> getAllTickets() {
        List<Lottery> dbLotteries = lotteryRepository.findAll();
        Set<LotteryDTO> lotteries = new HashSet<>();
        for(Lottery lottery:dbLotteries){
            LotteryDTO lotteryDTO = lotteryHelper.getLotteryDTOFromModel(lottery);
            lotteries.add(lotteryDTO);
        }
        return lotteries;
    }

    @Override
    public LotteryDTO createLotteryTicket(LotteryDTO lotteryDTO) throws Exception{

        Set<TicketLineDTO> ticketLineDTOs = lotteryDTO.getLines();
        Lottery lottery = new Lottery();

        if(ticketLineDTOs.size()<0){
            throw new Exception("Please provide the data for the ticket lines");
        }

        lottery.setStatus(TicketStatus.UNCHECKED);
        Set<TicketLine> ticketLines = lotteryHelper.processTickets(ticketLineDTOs,lottery);
        lottery.setLines(ticketLines);
        lottery = lotteryRepository.save(lottery);
        lotteryDTO = lotteryHelper.getLotteryDTOFromModel(lottery);
        return lotteryDTO;
    }

    @Override
    public LotteryDTO getTicket(Long id) {
        return lotteryHelper.getLotteryDTOFromModel(lotteryRepository.findById(id).get());
    }

    @Override
    public LotteryDTO amendTicket(Long id, Set<TicketLineDTO> ticketLineDTOS) throws Exception {
        LotteryDTO lotteryDTOUpdate = null;

        Lottery lottery = lotteryRepository.findById(id).get();

        if(lottery!=null){
            if(lottery.getStatus()==TicketStatus.CHECKED){
                throw new Exception("Ticket is already Checked. Cannot Be Amended Further");
            }

            Set<TicketLine> ticketLines = lottery.getLines();
            ticketLines.addAll(lotteryHelper.processTickets(ticketLineDTOS, lottery));

            lottery.setLines(ticketLines);
            lottery = lotteryRepository.save(lottery);
            lotteryDTOUpdate = lotteryHelper.getLotteryDTOFromModel(lottery);
        }
        return lotteryDTOUpdate;
    }

    @Override
    public LotteryDTO getTicketStatus(Long id) {
        LotteryDTO lotteryDTO = null;
        Lottery lottery = lotteryRepository.findById(id).get();

        if(lottery!=null){
            lottery.setStatus(TicketStatus.CHECKED);
            lottery = lotteryRepository.save(lottery);
            lotteryDTO = lotteryHelper.getLotteryDTOFromModel(lottery);
        }
        return lotteryDTO;
    }
}
