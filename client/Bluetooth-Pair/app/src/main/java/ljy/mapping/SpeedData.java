package ljy.mapping;

import org.litepal.crud.LitePalSupport;

public class SpeedData extends LitePalSupport {
    private static int nextId;
    private int order;
    /**
     * 1：蓝牙数据
     * 2：网络数据
     */
    private final int type;
    private float speed;
    private String time;
    /**
     * 查看功能里，是否被选中(不入库)
     */
    public boolean checked = false;

    public static void setNextId(int next){
        nextId = next;
    }

    public static int getNextId(){
        return nextId;
    }

    public SpeedData(int type, float speed, String time) {
        this.order = getNextId();
        this.type = type;
        this.speed = speed;
        this.time = time;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
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
