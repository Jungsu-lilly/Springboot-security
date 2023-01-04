package com.cos.security1.config.oauth.provider;

public interface OAuth2UserInfo {
    String getProviderId();
    String getProvider();  // google, facebook, naver
    String getEmail();
    String getName();
}
