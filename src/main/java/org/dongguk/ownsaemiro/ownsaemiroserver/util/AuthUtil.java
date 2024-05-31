package org.dongguk.ownsaemiro.ownsaemiroserver.util;

import lombok.RequiredArgsConstructor;
import org.dongguk.ownsaemiro.ownsaemiroserver.constants.Constants;

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
