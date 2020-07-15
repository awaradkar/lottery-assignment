package com.poppulo.lottery.controller;

import com.poppulo.lottery.exception.EntityNotFoundException;
import com.poppulo.lottery.model.ErrorDetails;
import com.poppulo.lottery.model.dto.LotteryDTO;
import com.poppulo.lottery.model.dto.TicketLineDTO;
import com.poppulo.lottery.service.LotteryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Set;

@CrossOrigin("*")
@RestController
@RequestMapping("api/v1/lottery")
public class LotteryController {

    @Autowired
    public LotteryService lotteryService;

    @RequestMapping(value = "/ticket", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<Set<LotteryDTO>> lotteryList() {
        Set<LotteryDTO> ticketList;

        ticketList = lotteryService.getAllTickets();

        return new ResponseEntity<>(ticketList, HttpStatus.OK);
    }

    @RequestMapping(value = "/ticket", method = RequestMethod.POST, produces = {"application/json"})
    public ResponseEntity<Object> createLotteryTicket(@RequestBody LotteryDTO lotteryDTO) throws Exception {

        if(lotteryDTO.getLines().size()==0){
            ErrorDetails errorDetails = new ErrorDetails(new Date(), "Please provide lines to add for ticket", "No data for addition of lines");
            return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
        }

        lotteryDTO = lotteryService.createLotteryTicket(lotteryDTO);

        return new ResponseEntity<>(lotteryDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "/ticket/{id}" , method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity<LotteryDTO> getTicket(@PathVariable("id") Long id) {
        LotteryDTO lotteryDTO = lotteryService.getTicket(id);

        if (lotteryDTO != null) {
            return new ResponseEntity<>(lotteryDTO, HttpStatus.OK);
        } else {
            throw new EntityNotFoundException("" + id);
        }

    }

    @RequestMapping(value = "/ticket/{id}", method = RequestMethod.PUT, produces = {"application/json"})
    public ResponseEntity<Object> amendTicket(@PathVariable("id") Long id, @RequestBody Set<TicketLineDTO> ticketLineDTOS) throws Exception {

        if(id==0){
            ErrorDetails errorDetails = new ErrorDetails(new Date(), "Invalid Id passed", "Id cannot be zero");
            return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
        }

        if(ticketLineDTOS.size()==0){
            ErrorDetails errorDetails = new ErrorDetails(new Date(), "Please provide lines to add for ticket", "No data for addition of lines");
            return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
        }
        LotteryDTO lotteryDTO = lotteryService.amendTicket(id, ticketLineDTOS);

        if (lotteryDTO != null) {
            return new ResponseEntity<>(lotteryDTO, HttpStatus.OK);
        } else {
            throw new EntityNotFoundException("" + id);
        }

    }

    @RequestMapping(value = "/status/{id}", method = RequestMethod.PUT, produces = {"application/json"})
    public ResponseEntity<Object> getTicketStatus(@PathVariable("id") Long id) throws Exception {

        if(id==0){
            ErrorDetails errorDetails = new ErrorDetails(new Date(), "Invalid Id passed", "Id cannot be zero");
            return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
        }
        LotteryDTO lotteryDTO = lotteryService.getTicketStatus(id);

        if (lotteryDTO != null) {
            return new ResponseEntity<>(lotteryDTO, HttpStatus.OK);
        } else {
            throw new EntityNotFoundException("" + id);
        }

    }
}
