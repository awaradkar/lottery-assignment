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
    private Long id;

    private Integer number;

    private Integer points;

    @ManyToOne
    @JoinColumn
    private Lottery lottery;
}
