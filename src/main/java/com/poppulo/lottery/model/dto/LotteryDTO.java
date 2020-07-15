package com.poppulo.lottery.model.dto;

import com.poppulo.lottery.enums.TicketStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class LotteryDTO {

    private Long id;

    private TicketStatus status;

    private Set<TicketLineDTO> lines;
}
