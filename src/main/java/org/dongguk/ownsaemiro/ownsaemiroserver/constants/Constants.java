package org.dongguk.ownsaemiro.ownsaemiroserver.constants;

import org.dongguk.ownsaemiro.ownsaemiroserver.exception.CommonException;
import org.dongguk.ownsaemiro.ownsaemiroserver.exception.ErrorCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
    public static String ORDER_ID = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static String DEFAULT_IMAGE_NAME = "ownsaemiro_default.png";
    public static String DEFAULT_IMAGE = "https://ownsaemiro-bucket.s3.ap-northeast-2.amazonaws.com/ownsaemiro_default.png";
    public static String ALL = "ALL";
    public static String STR_CONNECTOR = " ~ ";
    public static String MINUTE = "분";
    public static String STR_SPORT_SUBJECT_PARSER = " : ";
    public static String STR_SPORT_TEAM_PARSER = " VS ";
    public static String ASSIGN_TICKET_COMPLETE_TITLE = "티켓 양도 성공!";
    public static String ASSIGN_TICKET_FAIL_TITLE = "티켓 양도 실패";
    public static String ASSIGN_TICKET_COMPLETE_CONTENT = "양도에 대해 당첨되셨습니다!, 구매내역에서 구매를 마무리해주세요~";
    public static String ASSIGN_TICKET_FAIL_CONTENT = "양도에 실패하셨습니다.. 다음 기회를 노려주세요!";
    public static String NOTIFICATION_DEFAULT_SOUND = "default";


    public static List<String> NO_NEED_AUTH = List.of(
            "/api/auth/sign-up",
            "/api/auth/sign-in",
            "/api/auth/check",
            "/api/oauth/login/naver",
            "/api/oauth/login/kakao",
            "/api/oauth/sign-up",
            "/api/admin/check-ticket"
    );

    public static List<String> NEED_USER_ROLE = List.of(
            "/api/events",
            "/api/events/search",
            "/api/events/likes",
            "/api/events/popular",
            "/api/events/recommends",
            "/api/events/{eventId}/remain",
            "/api/events/{eventId}/info",
            "/api/events/{eventId}/brief",
            "/api/events/{eventId}/top-review",
            "/api/events/{eventId}/seller",
            "/api/events/{eventId}/reviews",
            "/api/events/like",
            "/api/events/{eventId}/likes",
            "/api/events/{eventId}",
            "/api/assignments/tickets",
            "/api/assignment/tickets/{ticketId}",
            "/api/users/nickname",
            "/api/users/profile-image",
            "/api/users/wallets",
            "/api/users",
            "/api/users/events/likes",
            "/api/tickets/purchasing",
            "/api/tickets/purchasing/{ticketId}",
            "/api/tickets",
            "/api/tickets/{ticketId}",
            "/api/users/events/participate",
            "/api/tickets/assignment",
            "/api/users/fcm-token",
            "/api/users/notifications"

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

    public static List<String> REGIONS_SPORT = List.of(
            "Busan",
            "Suwon",
            "Ulsan",
            "Goyang",
            "Daegu",
            "Jamsil",
            "Changwon",
            "Wonju",
            "Anyang",
            "Daejeon",
            "Gwangju",
            "Uijeongbu",
            "Hwaseong",
            "Cheonan",
            "Seoul",
            "Incheon",
            "Gimcheon",
            "Ansan",
            "Gangneung",
            "Gimpo",
            "Yongin",
            "Bucheon",
            "Jeonju",
            "Pohang",
            "Jeju",
            "Gwangyang",
            "Mokdong",
            "Asan",
            "Chuncheon",
            "Cheongju",
            "Gunsan",
            "Jinju",
            "Milyang",
            "Sangju",
            "Masan",
            "Yangsan",
            "Suncheon",
            "Gimhae",
            "Pyeongchang",
            "Chungju",
            "Geoje"
    );

    public static List<String> REGION_CONCERT = List.of(
            "seuol",
            "gyeonggi",
            "incheon",
            "busan",
            "daegu",
            "gwangju",
            "daejeon",
            "ulsan",
            "sejong",
            "gyeongnam",
            "gyeongbuk",
            "jeonnam",
            "jeonbuk",
            "chungnam",
            "chungbuk",
            "gangwon",
            "jeju"
    );

    public static List<String> GENRE = List.of(
            "play",
            "musical",
            "classic",
            "traditional",
            "popular music",
            "dance",
            "popular dance",
            "circus/magic",
            "etc"
    );


    public static List<String> ORGANIZATION = List.of(
            "KBO",
            "KLEAGUE",
            "KBL"
    );

    public static List<String> WEEKDAYS = List.of(
            "monday",
            "tuesday",
            "wednesday",
            "thursday",
            "friday",
            "saturday",
            "sunday"
    );

    public static List<String> WEATHER = List.of(
            "Clear",
            "Partly Cloudy",
            "Snow",
            "Rain",
            "Cloudy",
            "Mostly Cloudy"
    );

    public static List<String> BASEBALL = List.of(
            "삼성",
            "두산",
            "롯데",
            "LG",
            "SSG",
            "SK",
            "NC",
            "한화",
            "KIA",
            "KT"
    );

    public static List<String> SOCCER = List.of(
            "제주 Utd",
            "인천 Utd",
            "대구 FC",
            "강원 FC",
            "수원 FC",
            "전북 현대",
            "포항 스틸러스",
            "대전 하나 시티즌",
            "광주 FC",
            "FC 서울",
            "수원 삼성"
    );

    public static final List<Double> TEMPERATURES = List.of(
            15.5, 22.3, 18.7, 30.2, 25.8, 10.1, 12.9, 28.4, 19.6, 23.0,
            16.4, 21.7, 17.5, 29.8, 24.1, 11.3, 14.8, 27.6, 18.2, 22.9,
            15.7, 20.3, 19.0, 31.4, 26.7, 12.5, 13.9, 29.2, 20.8, 21.1,
            17.2, 22.6, 18.1, 30.9, 25.0, 13.4, 15.6, 27.3, 19.7, 23.5,
            14.9, 21.5, 16.8, 28.7, 24.9, 10.8, 12.4, 26.5, 18.9, 22.7
    );


    public static Map<String, String> createSportsRequest(String info){
        String[] split = info.split(Constants.STR_SPORT_SUBJECT_PARSER);

        String organization = extractOrganization(split[0]);
        String[] teams = split[1].split(Constants.STR_SPORT_TEAM_PARSER);

        String home = teams[0];
        String away = teams[1];

        Random random = new Random();
        String weekday = WEEKDAYS.get(random.nextInt(WEEKDAYS.size()));
        String weather = WEATHER.get(random.nextInt(WEATHER.size()));
        double degree = TEMPERATURES.get(random.nextInt(TEMPERATURES.size()));
        String region = REGIONS_SPORT.get(random.nextInt(REGIONS_SPORT.size()));

        // 요청 맵 생성
        Map<String, String> request = new HashMap<>();
        request.put("organization", organization);
        request.put("weekday", weekday);
        request.put("home", home);
        request.put("away", away);
        request.put("weather", weather);
        request.put("degree", String.valueOf(degree));
        request.put("region", region);

        return request;
    }

    public static Map<String, String> createConcertRequest(){
        Random random = new Random();
        String weekday = WEEKDAYS.get(random.nextInt(WEEKDAYS.size()));
        String genre = GENRE.get(random.nextInt(GENRE.size()));
        String region = REGION_CONCERT.get(random.nextInt(REGION_CONCERT.size()));

        Map<String, String> request = new HashMap<>();
        request.put("weekday", weekday);
        request.put("region", region);
        request.put("genre", genre);

        return request;
    }

    private static String extractOrganization(String strOrganization){
        for (String organization : Constants.ORGANIZATION){
            if (organization.equals(strOrganization))
                return organization;
        }
        throw new CommonException(ErrorCode.INVALID_PARAMETER_FORMAT);
    }
}
