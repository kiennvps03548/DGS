package dgs.example.pungkook.dgs_android.model;

/**
 * Created by mthu1 on 7/10/2017.
 */

public class InputItem {
    String Config_seq, machine_mater_code, config_code, config_name, config_value;

    public String getConfig_seq() {
        return Config_seq;
    }

    public void setConfig_seq(String config_seq) {
        Config_seq = config_seq;
    }

    public String getMachine_mater_code() {
        return machine_mater_code;
    }

    public void setMachine_mater_code(String machine_mater_code) {
        this.machine_mater_code = machine_mater_code;
    }

    public String getConfig_code() {
        return config_code;
    }

    public void setConfig_code(String config_code) {
        this.config_code = config_code;
    }

    public String getConfig_name() {
        return config_name;
    }

    public void setConfig_name(String config_name) {
        this.config_name = config_name;
    }

    public String getConfig_value() {
        return config_value;
    }

    public void setConfig_value(String config_value) {
        this.config_value = config_value;
    }
}
