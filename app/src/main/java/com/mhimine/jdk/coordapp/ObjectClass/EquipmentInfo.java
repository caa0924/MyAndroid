package com.mhimine.jdk.coordapp.ObjectClass;


public class EquipmentInfo {
    private String equip_name;
    private String equip_number;
    private String equip_type;
    private String file_number;
    private String craft_number;
    private String equip_state;
    private String factory_name;
    private String factory_number;
    private String factory_time;
    private String inService_time;
    private String belong_department;
    private String equip_classify;
    private String equip_value;
    private String service_years;
    private String equip_principal;

    public EquipmentInfo(String equip_name, String equip_number, String equip_type, String file_number, String craft_number, String equip_state, String factory_name, String factory_number, String factory_time, String inService_time, String belong_department, String equip_classify, String equip_value, String service_years, String equip_principal) {
        this.equip_name = equip_name;
        this.equip_number = equip_number;
        this.equip_type = equip_type;
        this.file_number = file_number;
        this.craft_number = craft_number;
        this.equip_state = equip_state;
        this.factory_name = factory_name;
        this.factory_number = factory_number;
        this.factory_time = factory_time;
        this.inService_time = inService_time;
        this.belong_department = belong_department;
        this.equip_classify = equip_classify;
        this.equip_value = equip_value;
        this.service_years = service_years;
        this.equip_principal = equip_principal;
    }

    public String getEquip_name() {
        return equip_name;
    }

    public String getEquip_number() {
        return equip_number;
    }

    public String getEquip_type() {
        return equip_type;
    }

    public String getFile_number() {
        return file_number;
    }

    public String getCraft_number() {
        return craft_number;
    }

    public String getEquip_state() {
        return equip_state;
    }

    public String getFactory_name() {
        return factory_name;
    }

    public String getFactory_number() {
        return factory_number;
    }

    public String getFactory_time() {
        return factory_time;
    }

    public String getInService_time() {
        return inService_time;
    }

    public String getBelong_department() {
        return belong_department;
    }

    public String getEquip_classify() {
        return equip_classify;
    }

    public String getEquip_value() {
        return equip_value;
    }

    public String getService_years() {
        return service_years;
    }

    public String getEquip_principal() {
        return equip_principal;
    }
}
