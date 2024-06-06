package org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.blockchain;

import lombok.Getter;

@Getter
public class BlockChainResponse {
    private Boolean success;
    private Data data;
    private BlockChainError error;
}

