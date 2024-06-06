package org.dongguk.ownsaemiro.ownsaemiroserver.util;

import net.minidev.json.JSONObject;
import org.dongguk.ownsaemiro.ownsaemiroserver.constants.Constants;
import org.dongguk.ownsaemiro.ownsaemiroserver.dto.response.blockchain.BlockChainResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class RestClientUtil {
    @Value("${blockchain.publish}")
    private String publisherUrl;
    @Value("${blockchain.remain}")
    private String additionalUrl;
    private final RestClient restClient = RestClient.create();

    public JSONObject sendPostRequest(String url, JSONObject requestBody) {
        Map<String,Object> response = Objects.requireNonNull(restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody.toJSONString())
                .retrieve()
                .toEntity(Map.class).getBody());

        return new JSONObject(response);
    }

    public BlockChainResponse sendRequestToPublishTickets(Integer seat) {
        Map<String, Integer> seatInfo = new HashMap<>();
        seatInfo.put("seat", seat);

        JSONObject requestBody = new JSONObject(seatInfo);

        return Objects.requireNonNull(restClient.post()
                .uri(publisherUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody.toJSONString())
                .retrieve()
                .toEntity(BlockChainResponse.class).getBody());

    }

    public BlockChainResponse sendRequestToAdditionalTickets(String contractAddress, Integer seat, Integer lastNumber) {
        Map<String, Object> seatInfo = new HashMap<>();
        seatInfo.put("contract_address", contractAddress);
        seatInfo.put("seat", seat);
        seatInfo.put("last_ticket_number", lastNumber);


        JSONObject requestBody = new JSONObject(seatInfo);

        return Objects.requireNonNull(restClient.post()
                .uri(additionalUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody.toJSONString())
                .retrieve()
                .toEntity(BlockChainResponse.class).getBody());

    }

    public Map<String, Object> sendAppKakaoLoginRequest(String url, String accessToken){
        // TODO: 사용자 정보 어떻게 받을지 논의 후 수정할 예정
        return (Map<String, Object>) Objects.requireNonNull(restClient.get()
                .uri(url)
                .header(Constants.CONTENT_TYPE, Constants.CONTENT_VALUE)
                .header(Constants.PREFIX_AUTH, Constants.PREFIX_BEARER + accessToken)
                .retrieve()
                .toEntity(Map.class).getBody());
    }

    public Map<String, Object> sendAppNaverLoginRequest(String url, String accessToken){
        return (Map<String, Object>) Objects.requireNonNull(restClient.get()
                .uri(url)
                .header(Constants.PREFIX_AUTH, Constants.PREFIX_BEARER + accessToken)
                .retrieve()
                .toEntity(Map.class).getBody().get("response"));
    }

    public JSONObject sendPatchRequest(String url, JSONObject requestBody) {
        Map<String,Object> response = Objects.requireNonNull(restClient.patch()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody.toJSONString())
                .retrieve()
                .toEntity(Map.class).getBody());
        return new JSONObject(response);
    }
}
