package com.poppulo.lottery.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "TICKET_LINE")

public class TicketLine {

    @GeneratedValue
    @Id
    private Long id;

    private String ticketNumber;

    private Integer points;

    @ManyToOne
    @JoinColumn(name = "lottery_id")
    private Lottery lottery;
}
