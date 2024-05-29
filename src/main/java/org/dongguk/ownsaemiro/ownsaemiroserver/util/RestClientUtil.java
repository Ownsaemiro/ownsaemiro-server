package org.dongguk.ownsaemiro.ownsaemiroserver.util;

import net.minidev.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.Objects;

@Component
public class RestClientUtil {

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
