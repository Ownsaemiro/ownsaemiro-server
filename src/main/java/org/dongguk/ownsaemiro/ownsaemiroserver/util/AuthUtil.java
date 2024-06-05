package org.dongguk.ownsaemiro.ownsaemiroserver.util;

import lombok.RequiredArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.constants.Constants;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@RequiredArgsConstructor
public class AuthUtil {
    public static String makePassword(){
        StringBuilder password = new StringBuilder();
        SecureRandom random = new SecureRandom();

        for(int i=0; i<12; i++){
            password.append(Constants.CHARACTERS.charAt(random.nextInt(Constants.CHARACTERS.length())));
        }
        return password.toString();
    }

    public static String makeOrderId(){
        StringBuilder orderId = new StringBuilder();
        SecureRandom random = new SecureRandom();

        for(int i=0; i<12; i++){
            orderId.append(Constants.ORDER_ID.charAt(random.nextInt(Constants.ORDER_ID.length())));
        }
        return orderId.toString();
    }

    public static String generateHash() {
        String input = makePassword();
        try {
            // SHA-256 알고리즘을 사용하여 MessageDigest 인스턴스를 생성합니다.
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // 입력 문자열을 바이트 배열로 변환하여 해시를 계산합니다.
            byte[] encodedHash = digest.digest(input.getBytes());

            // 해시 값을 16진수 문자열로 변환하여 반환합니다.
            return bytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String makeNickname(){
        String nickname = "osmr";
        SecureRandom random = new SecureRandom();

        for(int i=0; i<6; i++){
            nickname += Constants.CHARACTERS.charAt(random.nextInt(Constants.CHARACTERS.length()));
        }
        return nickname;
    }
    public static String removeDash(String tempPhoneNumber){
        String phoneNumber = "";
        for (String s : tempPhoneNumber.split("-")) {
            phoneNumber += s;
        }
        return phoneNumber;
    }
}
