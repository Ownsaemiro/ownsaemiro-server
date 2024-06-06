package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.blockchain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;
@Getter
public class Data {
    @JsonProperty("event_hash")
    private String eventHash;

    @JsonProperty("tickets")
    private List<BlockChainTicket> tickets;

    @JsonProperty("contract_address")
    private String contractAddress;
}
