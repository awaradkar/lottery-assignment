package com.poppulo.lottery.service.impl;

import com.poppulo.lottery.helper.EntityConversion;
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
import java.util.Timer;

@Service
public class LotteryServiceImpl implements LotteryService {

    @Autowired
    LotteryRepository lotteryRepository;

    @Override
    public Set<LotteryDTO> getAllTickets() {
        List<Lottery> dbLotteries = lotteryRepository.findAll();
        Set<LotteryDTO> lotteries = new HashSet<>();

        for(Lottery lottery:dbLotteries){
            Set<TicketLineDTO> ticketLineDTOS = new HashSet<>();
            for(TicketLine ticketLine:lottery.getLines()){
                TicketLineDTO dto = new TicketLineDTO();
                dto = (TicketLineDTO) EntityConversion.convertModel(ticketLine, dto,
                        EntityConversion.ConversionEnum.ENTITYTODTO.ordinal());
                ticketLineDTOS.add(dto);
            }
            LotteryDTO lotteryDTO = new LotteryDTO();
            lotteryDTO = (LotteryDTO) EntityConversion.convertModel(lottery, lotteryDTO,
                    EntityConversion.ConversionEnum.ENTITYTODTO.ordinal());
            lotteryDTO.setLines(ticketLineDTOS);
            lotteries.add(lotteryDTO);
        }

        return lotteries;
    }

    @Override
    public LotteryDTO createLotteryTicket(LotteryDTO lotteryDTO) throws Exception{

        Set<TicketLineDTO> ticketLineDTOs = lotteryDTO.getLines();

        if(ticketLineDTOs.size()<0){
            throw new Exception("Please provide the data for the ticket lines");
        }

        Set<TicketLine> ticketLines = processTickets(ticketLineDTOs);

        return null;
    }

    private Set<TicketLine> processTickets(Set<TicketLineDTO> ticketLineDTOs) throws Exception {

        Set<TicketLine> ticketLines = new HashSet<>();

        int count = 1;
        for(TicketLineDTO ticketLineDTO:ticketLineDTOs){
            int ticketNumber = validateTicket(ticketLineDTO, count);

            TicketLine ticketLine = new TicketLine();
            ticketLine.setNumber(ticketNumber);

            int digitsSum = 0;
            int lastDigit = ticketNumber%10;
            int previousDigit = ticketNumber%10;
            boolean fivePointer = true;
            boolean onePointer = false;

            while(ticketNumber>0){
                digitsSum = digitsSum+(ticketNumber%10);

                if(previousDigit==ticketNumber%10){
                    if(ticketNumber<10){
                        if(lastDigit!=previousDigit && lastDigit!=ticketNumber)
                            onePointer=true;
                    }
                    previousDigit=ticketNumber%10;
                }
                else fivePointer=false;

                ticketNumber = ticketNumber/10;
            }

            if(digitsSum==2)ticketLine.setPoints(10);
            else if(fivePointer)ticketLine.setPoints(5);
            else if(onePointer)ticketLine.setPoints(1);
            else ticketLine.setPoints(0);

            count++;
        }
        return ticketLines;
    }

    private int validateTicket(TicketLineDTO ticketLineDTO, int count) throws Exception {
        if(ticketLineDTO.getTicketNumber()==null){
            throw new Exception("Ticket Number not provided for Line:"+count);
        }

        if(ticketLineDTO.getTicketNumber().length()!=3){
            throw new Exception("Ticket Number of Line:"+count+" has "+ticketLineDTO.getTicketNumber().length()+" digits. Should be 3 digits");
        }

        try {
            int ticketNumber = Integer.parseInt(ticketLineDTO.getTicketNumber());
            return ticketNumber;
        }catch (Exception e){
            throw new Exception("Enter a valid ticket number for Line:"+count);
        }
    }
}
