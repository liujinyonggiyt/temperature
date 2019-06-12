package ljy.base.bean;

import ljy.mapping.SpeedData;

public class SpeedDataBus {
    private int id;
    private SpeedData speedData;
    /**
     * checkbox是否选中
     */
    private boolean checked;
    public SpeedDataBus(int id) {
        this.id = id;
    }

    public SpeedDataBus(int id, SpeedData speedData, boolean checked) {
        this.id = id;
        this.speedData = speedData;
        this.checked = checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SpeedData getSpeedData() {
        return speedData;
    }

    public void setSpeedData(SpeedData speedData) {
        this.speedData = speedData;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
