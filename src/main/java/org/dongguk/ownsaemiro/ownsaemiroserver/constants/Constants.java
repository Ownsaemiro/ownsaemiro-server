package org.dongguk.ownsaemiro.ownsaemiroserver.constants;

import java.util.List;

public class Constants {
    public static String CLAIM_USER_ID = "uuid";
    public static String CLAIM_USER_ROLE = "role";
    public static String PREFIX_BEARER = "Bearer ";
    public static String PREFIX_AUTH = "Authorization";
    public static String ACCESS_COOKIE_NAME = "access_token";
    public static String REFRESH_COOKIE_NAME = "refresh_token";
    public static String CONTENT_TYPE = "Content-type";
    public static String CONTENT_VALUE = "application/x-www-form-urlencoded;charset=utf-8";
    public static String NAVER_LOGIN_PATH = "https://openapi.naver.com/v1/nid/me";
    public static String KAKAO_LOGIN_PATH = "https://kapi.kakao.com/v2/user/me";
    public static String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_+=<>?";
    public static String DEFAULT_IMAGE = "DEFAULT_IMAGE";
    public static String ALL = "전체";
    public static String STR_CONNECTOR = " ~ ";
    public static String MINUTE = "분";

    public static List<String> NO_NEED_AUTH = List.of(
            "/api/auth/sign-up",
            "/api/auth/sign-in",
            "/api/auth/check",
            "/oauth/login/naver",
            "/oauth/login/kakao",
            "/api/oauth/sign-up"
    );

    public static List<String> NEED_USER_ROLE = List.of(
            "/api/events",
            "/api/events/search",
            "/api/events/{eventId}/like",
            "/api/events/likes"
    );

    public static List<String> NEED_SELLER_ROLE = List.of(
            "/api/seller/histories",
            "/api/seller/events",
            "/api/seller/apply",
            "/api/seller/apply/search"
    );

    public static List<String> NEED_ADMIN_ROLE = List.of(
            "/api/admin/register",
            "/api/admin/register/detail",
            "/api/admin/register/search",
            "/api/admin/banned"
    );
}
