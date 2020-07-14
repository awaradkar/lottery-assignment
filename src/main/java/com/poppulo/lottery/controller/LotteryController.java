package com.poppulo.lottery.controller;

import com.poppulo.lottery.model.dto.LotteryDTO;
import com.poppulo.lottery.service.LotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@CrossOrigin("*")
@RestController
@RequestMapping("api/v1/lottery")
public class LotteryController {

    @Autowired
    LotteryService lotteryService;

    @RequestMapping(method = RequestMethod.GET, produces = { "application/json" })
    public ResponseEntity<Set<LotteryDTO>> lotteryList() {
        Set<LotteryDTO> ticketList;

        ticketList = lotteryService.getAllTickets();

        return new ResponseEntity<>(ticketList, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, produces = { "application/json" })
    public ResponseEntity<LotteryDTO> createLotteryTicket(LotteryDTO lotteryDTO) throws Exception{

        lotteryDTO = lotteryService.createLotteryTicket(lotteryDTO);

        return new ResponseEntity<>(lotteryDTO, HttpStatus.OK);
    }
}
