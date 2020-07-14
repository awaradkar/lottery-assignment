package com.poppulo.lottery.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class LotteryDTO {

    private Long id;

    private Set<TicketLineDTO> lines;
}
