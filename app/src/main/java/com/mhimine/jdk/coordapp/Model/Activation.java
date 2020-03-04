package com.mhimine.jdk.coordapp.Model;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Activation {
    private String deviceID;
    private String activationCode;

    public Activation(String deviceID) {
        this.deviceID = deviceID;
        this.activationCode = md5Slat(deviceID,"arsc");
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public String getActivationCode() {
        return activationCode;
    }

    @NonNull
    private static String md5Slat(String string, String slat) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest((string + slat).getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean verificationResults(String activation){
        boolean success = false;
        if (activation == null || activation.equals("")) {
            success = false;
        } else if (activation.equals(activationCode)) {
            success = true;
        }
        return success;
    }
}
