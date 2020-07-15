package com.poppulo.lottery.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketLineDTO implements Comparable{

    private Long id;

    private String ticketNumber;

    private Integer points = 0;

    @Override
    public int compareTo(Object o) {
        TicketLineDTO ticketLine = (TicketLineDTO)o;

        return this.getPoints().compareTo(ticketLine.getPoints());
    }
}
