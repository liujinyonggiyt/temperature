package ljy.base.bean;

import ljy.mapping.SpeedData;

public class SpeedDataSelectBus {
    private int id;
    private SpeedData speedData;
    /**
     * 在table中的行索引
     */
    private int rowIndex;
    /**
     * 在存储列表中的序号
     */
    private int order;

    public SpeedDataSelectBus(int id, SpeedData speedData, int rowIndex, int order) {
        this.id = id;
        this.speedData = speedData;
        this.rowIndex = rowIndex;
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
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

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }
}
