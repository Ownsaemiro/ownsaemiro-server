package org.dongguk.ownsaemiro.ownsaemiroserver.security.info;

import org.dongguk.ownsaemiro.ownsaemiroserver.security.info.factory.Oauth2UserInfo;

import java.util.Map;

public class KakaoOauth2UserInfo extends Oauth2UserInfo {
    public KakaoOauth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return attributes.get("id").toString();
    }
}
