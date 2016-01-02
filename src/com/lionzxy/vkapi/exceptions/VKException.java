package com.lionzxy.vkapi.exceptions;

import com.lionzxy.vkapi.VKUser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.StringReader;
import java.util.HashMap;

/**
 * Created by LionZXY on 31.12.2015.
 * LeaveBot
 */
public class VKException extends RuntimeException {
    private int error_code;

    public boolean isFix = false;

    public static StringBuilder errors = new StringBuilder();

    public static HashMap<Integer, String> error;

    static {
        error = new HashMap<>();
        error.put(-2, "Parse error when detected JSON answer");
        error.put(-1, "Is not error");
        error.put(1, "Unknown error. Repeat request later");
        error.put(2, "App is disable. Use other appcode");
        error.put(3, "Not found method");
        error.put(4, "Invalid signature");
        error.put(5, "Authorization failed.");
        error.put(6, "Too many request per second. Repeat request later");
        error.put(7, "Permission error. User disable personal message or add you in black list"); //Чаще всего, если у человека заблокированна личка
        error.put(8, "Invalid query");
        error.put(9, "Too much of the same type of action. Repeat request later");
        error.put(10, "Server error. Repeat request later");
        error.put(11, "User must be authorization or App must be enable");
        error.put(14, "Must be entered captha");
        error.put(15, "Permission error");
        error.put(16, "Request HTTPS protocol");
        error.put(17, "Need user validation");
        error.put(20, "Request only for standalone app");
        error.put(21, "Request only for standalone app");
        error.put(100, "Invalid signature");

    }

    public VKException(String answer, VKUser vkUser) {
        error_code = getIdException(answer);
        VKUser.log.print("Error #" + error_code + ". " + error.get(error_code));
        isFix = tryFixError(vkUser);
        VKUser.log.print(". Is Fix: " + isFix);
        errors.append("Error #").append(error_code).append(". ").append(error.get(error_code)).append(". Is Fix: ").append(isFix).append('\n');
    }

    public int getErrorCode() {
        return error_code;
    }

    public static boolean isException(String answer) {
        try {
            return ((JSONObject) new JSONParser().parse(new StringReader(answer))).get("error") != null;
        } catch (Exception e) {
            return true;
        }
    }

    public static int getIdException(String answer) {
        if (!isException(answer))
            return -1;
        try {
            return Integer.parseInt((((JSONObject) ((JSONObject) new JSONParser().parse(new StringReader(answer))).get("error")).get("error_code")).toString());
        } catch (Exception e) {
            e.printStackTrace();
            return -2;
        }
    }

    public boolean tryFixError() {
        switch (getErrorCode()) {
            case 1:
            case 10:
                VKUser.sleep(60000);
                return true;
            case 9:
                VKUser.sleep(60000 * 15);
                return true;
            case 6:
                VKUser.sleep(1000);
                return true;
            case 14:
                VKUser.sleep(60000 * 15);
                return true;
            default:
                printStackTrace();
                return false;
        }
    }

    public boolean tryFixError(VKUser vk) {
        switch (getErrorCode()) {
            case 2:
            case 5:
            case 11:
            case 15:
            case 17:
            case 20:
            case 21:
                vk.getAccesToken();
                return true;
            default:
                return tryFixError();
        }
    }
}
