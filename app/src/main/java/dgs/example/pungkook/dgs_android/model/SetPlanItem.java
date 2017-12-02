package dgs.example.pungkook.dgs_android.model;

/**
 * Created by mthu1 on 7/10/2017.
 */

public class SetPlanItem {
    private String set_plan_date;
    private int  daily_seq;
    private long working_time;
    private  short working_target_hours;

    public String getSet_plan_date() {
        return set_plan_date;
    }

    public void setSet_plan_date(String set_plan_date) {
        this.set_plan_date = set_plan_date;
    }

    public int getDaily_seq() {
        return daily_seq;
    }

    public void setDaily_seq(int daily_seq) {
        this.daily_seq = daily_seq;
    }

    public long getWorking_time() {
        return working_time;
    }

    public void setWorking_time(long working_time) {
        this.working_time = working_time;
    }

    public short getWorking_target_hours() {
        return working_target_hours;
    }

    public void setWorking_target_hours(short working_target_hours) {
        this.working_target_hours = working_target_hours;
    }
}

