package com.poppulo.lottery.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "TICKET_MASTER")
public class Lottery {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "lottery")
    private Set<TicketLine> lines;
}
