package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.blockchain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class BlockChainTicket {
    @JsonProperty("ticket_number")
    Integer ticketNumber;
}
