package com.mhimine.jdk.coordapp.ObjectClass;

public class DeviceCheck {
    private int id;
    private String device_number;
    private String check_time;
    private String check_number;

    public DeviceCheck(int id, String device_number, String check_time, String check_number) {
        this.id = id;
        this.check_number = check_number;
        this.check_time = check_time;
        this.device_number = device_number;
    }

    public String getDevice_number() {
        return device_number;
    }
    public String getCheck_time() {
        return check_time;
    }
    public String getCheck_number(){
        return check_number;
    }
    public int getId(){
        return id;
    }

}
