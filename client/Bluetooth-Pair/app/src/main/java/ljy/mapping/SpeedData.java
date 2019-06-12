package ljy.mapping;

import org.litepal.crud.LitePalSupport;

import ljy.mrg.SqliteMrg;

public class SpeedData extends LitePalSupport {
    /**
     * 1：蓝牙数据
     * 2：网络数据
     */
    private int type;
    private float speed;
    private String time;

    /**
     * 查看功能里，是否被选中(不入库)
     *
     */
    public boolean checked = false;

    public SpeedData(int type, float speed, String time) {
        this.type = type;
        this.speed = speed;
        this.time = time;
    }

    public void copyFrom(SpeedData srcData){
        setType(srcData.getType());
        setSpeed(srcData.getSpeed());
        setTime(srcData.getTime());
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
