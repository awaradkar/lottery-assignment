package com.poppulo.lottery.helper;

import com.poppulo.lottery.model.Lottery;
import com.poppulo.lottery.model.TicketLine;
import com.poppulo.lottery.model.dto.LotteryDTO;
import com.poppulo.lottery.model.dto.TicketLineDTO;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@Service
public class LotteryHelper {

    public LotteryDTO getLotteryDTOFromModel(Lottery lottery) {
        LotteryDTO lotteryDTO = null;
        if (lottery != null) {
            Set<TicketLineDTO> ticketLineDTOS = new TreeSet<>();
            for (TicketLine ticketLine : lottery.getLines()) {
                TicketLineDTO dto = new TicketLineDTO();
                dto = (TicketLineDTO) EntityConversion.convertModel(ticketLine, dto,
                        EntityConversion.ConversionEnum.ENTITYTODTO.ordinal());
                ticketLineDTOS.add(dto);
            }
            lotteryDTO = new LotteryDTO();
            lotteryDTO = (LotteryDTO) EntityConversion.convertModel(lottery, lotteryDTO,
                    EntityConversion.ConversionEnum.ENTITYTODTO.ordinal());
            lotteryDTO.setLines(ticketLineDTOS);
        }
        return lotteryDTO;
    }

    public Set<TicketLine> processTickets(Set<TicketLineDTO> ticketLineDTOs, Lottery lottery) throws Exception {

        Set<TicketLine> ticketLines = new HashSet<>();

        int count = 1;
        for (TicketLineDTO ticketLineDTO : ticketLineDTOs) {
            String ticketNumber = validateTicket(ticketLineDTO, count);

            TicketLine ticketLine = setTicketLineObject(ticketNumber);
            ticketLine.setLottery(lottery);
            ticketLines.add(ticketLine);

            count++;
        }
        return ticketLines;
    }

    public TicketLine setTicketLineObject(String ticketNumber) {
        TicketLine ticketLine = new TicketLine();
        ticketLine.setTicketNumber(ticketNumber);

        int digitsSum = 0;
        int lastDigit = ticketNumber.charAt(2) - '0';
        int secondDIgit = ticketNumber.charAt(1) - '0';
        int firstDigit = ticketNumber.charAt(0) - '0';
        boolean fivePointer = false;
        boolean onePointer = false;

        digitsSum = lastDigit+secondDIgit+firstDigit;
        if(lastDigit==firstDigit && secondDIgit==firstDigit)fivePointer=true;
        if(firstDigit!=secondDIgit&&firstDigit!=lastDigit)onePointer=true;

        if (digitsSum == 2) ticketLine.setPoints(10);
        else if (fivePointer) ticketLine.setPoints(5);
        else if (onePointer) ticketLine.setPoints(1);
        else ticketLine.setPoints(0);

        return ticketLine;
    }

    public String validateTicket(TicketLineDTO ticketLineDTO, int count) throws Exception {
        if (ticketLineDTO == null) {
            throw new Exception("Error Processing Request");
        }
        if (ticketLineDTO.getTicketNumber() == null) {
            throw new Exception("Ticket Number not provided for Line:" + count);
        }

        if (ticketLineDTO.getTicketNumber().length() != 3) {
            throw new Exception("Ticket Number of Line:" + count + " has " + ticketLineDTO.getTicketNumber().length() + " digits. Should be 3 digits");
        }

        try {
            Integer.parseInt(ticketLineDTO.getTicketNumber());

            for (int i =0 ; i<ticketLineDTO.getTicketNumber().length();i++){
                if(Integer.parseInt(String.valueOf(ticketLineDTO.getTicketNumber().charAt(i)))>2){
                    throw new Exception("Ticket Number of Line:" + count + " is invalid. Digits can only be 0, 1 or 2");
                }
            }

            return ticketLineDTO.getTicketNumber();
        } catch (Exception e) {
            throw new Exception("Enter a valid ticket number for Line:" + count);
        }
    }

}
