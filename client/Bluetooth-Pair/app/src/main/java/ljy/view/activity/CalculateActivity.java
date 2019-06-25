package ljy.view.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.OnClick;
import ljy.base.activity.BaseActivity;
import ljy.base.bean.SpeedDataBus;
import ljy.base.bean.SpeedDataSelectBus;
import ljy.base.constant.SpeedDataContant;
import ljy.bluetooth.R;
import ljy.mapping.SpeedData;
import ljy.utils.MyLog;
import ljy.view.dialog.CreateSpeedDataDialog;
import ljy.view.dialog.CreateSpeedDataSelectDialog;
import ljy.widget.TitleBar;

public class CalculateActivity extends BaseActivity {
    private static final String TAG = CalculateActivity.class.getSimpleName();
    /**
     * 计算过程中保留几位小数
     */
    private static final int DOT_NUMBER=3;
    @BindView(R.id.titlebar)
    TitleBar titlebar;
    @BindView(R.id.tablelayout_cal)
    TableLayout tablelayout_cal;
    @BindView(R.id.text_cal_liuliang)
    TextView text_cal_liuliang;
    @BindView(R.id.text_cal_waterwide)
    TextView text_cal_waterwide;
    @BindView(R.id.edit_cal_anbian)
    EditText edit_cal_anbian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            EventBus.getDefault().register(this);

            titlebar.setTitle("计算");
            titlebar.setBackgroundResource(R.color.blue);
            titlebar.setImmersive(true);
            titlebar.setTitleColor(Color.WHITE);

        }catch (Exception e){
            MyLog.e(Tag, e.getMessage(), e);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_calculate;
    }

    /**
     * 监听事件
     *
     * @param view
     */
    @OnClick({R.id.btn_cal_add, R.id.btn_cal_cal, R.id.btn_cal_init})
    public void onViewClicked(View view) {
        try {
            switch (view.getId()) {
                case R.id.btn_cal_add:{//添加
                    addLineSolt();
                    break;
                }
                case R.id.btn_cal_cal:{//计算
//                    float beginDistanceMin = 0;//起点距最小值
//                    float beginDistanceMax = 0;//起点距最大值
//                    float anbian = Float.parseFloat(edit_cal_anbian.getText().toString());
//                    float totalFlow = 0F;//总流量
                    BigDecimal beginDistanceMin = new BigDecimal(0);
                    BigDecimal beginDistanceMax = new BigDecimal(0);
                    BigDecimal anbian = new BigDecimal(edit_cal_anbian.getText().toString());
                    BigDecimal totalFlow = new BigDecimal(0);
                    for(int i=1,len=tablelayout_cal.getChildCount()-1;i<len;++i){
                        TableRow tableRow1 = (TableRow) tablelayout_cal.getChildAt(i);
                        TableRow tableRow2 = (TableRow) tablelayout_cal.getChildAt(i+1);

//                        float speed1 = 0;
                        BigDecimal speed1 = new BigDecimal(0);
                        if(i==1){//第一个，最后一个速度为0
                            setSpeedText(tableRow1, "0");
                            setDefaultDianText(tableRow1);
                        }else{
//                            speed1 = getSpeed(tableRow1);//速度
                            speed1 = getSpeedBigDecimal(tableRow1);
                        }
//                        float speed2 = 0;
                        BigDecimal speed2 = new BigDecimal(0);
                        if(i==len-1){//第一个，最后一个速度为0
                            setSpeedText(tableRow2, "0");
                            setDefaultDianText(tableRow2);
                        }else{
//                            speed2 = getSpeed(tableRow2);//速度
                            speed2 = getSpeedBigDecimal(tableRow2);
                        }


//                        float beginDistance1 = getBeginDistance(tableRow1);//起点距
                        BigDecimal beginDistance1 = getBeginDistanceBigDecimal(tableRow1);
                        if(i == 1){
                            beginDistanceMin = beginDistance1;
                            beginDistanceMax = beginDistance1;
                        }else{
                            if(beginDistance1.compareTo(beginDistanceMin)==-1){//小于
                                beginDistanceMin = beginDistance1;
                            }
                            if(beginDistance1.compareTo(beginDistanceMax)==1){//大于
                                beginDistanceMax = beginDistance1;
                            }
                        }

//                        float beginDistance2 = getBeginDistance(tableRow2);//起点距
                        BigDecimal beginDistance2 = getBeginDistanceBigDecimal(tableRow2);
                        if(i == len-1){
                            if(beginDistance2.compareTo(beginDistanceMin)==-1){//小于
                                beginDistanceMin = beginDistance2;
                            }
                            if(beginDistance2.compareTo(beginDistanceMax)==1){//大于
                                beginDistanceMax = beginDistance2;
                            }
                        }


//                        float deep1 = getWaterDeepBigDecimal(tableRow1);//水深
                        BigDecimal deep1 = getWaterDeepBigDecimal(tableRow1);
//                        float deep2 = getWaterDeepBigDecimal(tableRow2);//水深
                        BigDecimal deep2 = getWaterDeepBigDecimal(tableRow2);
                        System.out.println("---------------------第"+i+"个部分");
                        //垂线平均
                        BigDecimal chuixianAvg1  =  BigDecimal.valueOf(0.9).multiply(speed1).setScale(DOT_NUMBER,  BigDecimal.ROUND_HALF_UP);
//                        float  chuixianAvg1 = 0.9F*speed1;
                        System.out.println("chuixianAvg1="+chuixianAvg1);
//                        BigDecimal b2  =  BigDecimal.valueOf(0.9*speed2);
                        BigDecimal chuixianAvg2  =  BigDecimal.valueOf(0.9).multiply(speed2).setScale(DOT_NUMBER,  BigDecimal.ROUND_HALF_UP);
//                        float  chuixianAvg2 = 0.9F*speed2;
                        System.out.println("chuixianAvg2="+chuixianAvg2);
                        //平均流速
                        BigDecimal speedAvg = speed1.add(speed2).divide(BigDecimal.valueOf(2)).setScale(DOT_NUMBER,  BigDecimal.ROUND_HALF_UP);
//                        float speedAvg = (speed1+speed2)/2;
                        System.out.println("speedAvg="+speedAvg);
                        //平均水深
                        BigDecimal deepAvg = deep1.add(deep2).divide(BigDecimal.valueOf(2)).setScale(DOT_NUMBER,  BigDecimal.ROUND_HALF_UP);
//                        float deepAvg = (deep1+deep2)/2;
                        System.out.println("deepAvg:"+deepAvg);
                        //间距
                        BigDecimal distance = beginDistance2.subtract(beginDistance1).setScale(DOT_NUMBER,  BigDecimal.ROUND_HALF_UP).abs();
//                        float distance = Math.abs(beginDistance2-beginDistance1);
                        System.out.println("distance:"+distance);
                        //部分
                        BigDecimal part =deepAvg.multiply(distance).setScale(DOT_NUMBER,  BigDecimal.ROUND_HALF_UP);
//                        float part = deepAvg*distance;
                        System.out.println("part："+part);
                        //部分平均（第一部分=第二个垂线平均*岸边系数；最后一部分=倒数第二个垂线平均*岸边系数）;中间的部分=（前一个垂线平均+后一个垂线平均）/2
                        BigDecimal partAvg;
                        if(i==1){//第一部分
                            partAvg = chuixianAvg2.multiply(anbian).setScale(DOT_NUMBER,  BigDecimal.ROUND_HALF_UP);
//                            partAvg =chuixianAvg2*anbian;
                        }else if(i == len-1){//最后一部分
                            partAvg = chuixianAvg1.multiply(anbian).setScale(DOT_NUMBER,  BigDecimal.ROUND_HALF_UP);
//                            partAvg = chuixianAvg1*anbian;
                        }else{//中间部分
                            partAvg = chuixianAvg1.add(chuixianAvg1).divide(BigDecimal.valueOf(2)).setScale(DOT_NUMBER,  BigDecimal.ROUND_HALF_UP);
//                            partAvg = (chuixianAvg1+chuixianAvg1)/2;
                        }
                        System.out.println("partAvg："+partAvg);
                        BigDecimal partFlow = partAvg.multiply(part).setScale(DOT_NUMBER,  BigDecimal.ROUND_HALF_UP);
//                        float partFlow = partAvg*part;
                        System.out.println("partFlow："+partFlow);
//                        totalFlow+=partFlow;
                        totalFlow = totalFlow.add(partFlow);
                    }

                    //总流量
                    float totalFlowFloat = totalFlow.setScale(DOT_NUMBER,  BigDecimal.ROUND_HALF_UP).floatValue();
                    text_cal_liuliang.setText(""+totalFlowFloat);
                    //水面宽
                    float water = beginDistanceMax.subtract(beginDistanceMin).setScale(DOT_NUMBER,  BigDecimal.ROUND_HALF_UP).floatValue();
                    text_cal_waterwide.setText(""+water);
                    break;
                }
                case R.id.btn_cal_init:{//初始化数据

                    edit_cal_anbian.setText("0.7");
                    for(int line=1;line<=7;++line){
                        addLineSolt();

                        TableRow tableRow = (TableRow) tablelayout_cal.getChildAt(line);
                        setSpeedText(tableRow, ""+getInitSpeed(line));
                        setBeginDistance(tableRow, ""+getInitBeginDiatance(line));
                        setWaterDeep(tableRow, ""+getInitWaterDeep(line));
                    }
                    break;
                }
            }
        }catch (Exception e){
            MyLog.e(Tag, e.getMessage(), e);
        }
    }
    private float getInitWaterDeep(int line){
        if(1==line){
            return 0F;
        }
        if(2==line){
            return 0.35F;
        }
        if(3==line){
            return 0.54F;
        }
        if(4==line){
            return 0.56F;
        }
        if(5==line){
            return 0.36F;
        }
        if(6==line){
            return 0.33F;
        }
        if(7==line){
            return 0F;
        }
        return 0F;
    }

    private float getInitBeginDiatance(int line){
        if(1==line){
            return 133.4F;
        }
        if(2==line){
            return 135.4F;
        }
        if(3==line){
            return 141.8F;
        }
        if(4==line){
            return 147.5F;
        }
        if(5==line){
            return 152.0F;
        }
        if(6==line){
            return 156.0F;
        }
        if(7==line){
            return 156.7F;
        }
        return 0F;
    }
    private float getInitSpeed(int line){
        if(1==line){
            return 0F;
        }
        if(2==line){
            return 0.95F;
        }
        if(3==line){
            return 1.19F;
        }
        if(4==line){
            return 1.32F;
        }
        if(5==line){
            return 1.33F;
        }
        if(6==line){
            return 1.15F;
        }
        if(7==line){
            return 0F;
        }
        return 0F;
    }

    /**
     * 添加一行槽点
     */
    private void addLineSolt(){
        TableRow tableRow = new TableRow(CalculateActivity.this);

        TextView delete = new TextView(CalculateActivity.this);//删除
        delete.setText("删");
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tablelayout_cal.removeView(tableRow);
                resetRowNumber();//重置序号
                //第一行，最后一行速度置零
                if(tablelayout_cal.getChildCount()>1){
                    TableRow firstRow = (TableRow) tablelayout_cal.getChildAt(1);
                    setDefaultDianText(firstRow);
                    setSpeedText(firstRow, "0");
                }
                if(tablelayout_cal.getChildCount()>2){
                    TableRow lastRow = (TableRow) tablelayout_cal.getChildAt(tablelayout_cal.getChildCount()-1);
                    setDefaultDianText(lastRow);
                    setSpeedText(lastRow, "0");
                }

            }
        });
        tableRow.addView(delete);

        TextView rowNumber = new TextView(CalculateActivity.this);//序号
        rowNumber.setText(""+tablelayout_cal.getChildCount());
        tableRow.addView(rowNumber);

        TextView dian = new TextView(CalculateActivity.this);//点号
        dian.setText(".");
        dian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateSpeedDataSelectDialog dialog = new CreateSpeedDataSelectDialog(CalculateActivity.this, tablelayout_cal.indexOfChild(tableRow));
                dialog.show();
            }
        });
        tableRow.addView(dian);

        EditText speed = new EditText(CalculateActivity.this);//速度
        speed.setText("0");
        tableRow.addView(speed);

        EditText distance = new EditText(CalculateActivity.this);//起点距
        distance.setText("0");
        tableRow.addView(distance);

        EditText deep = new EditText(CalculateActivity.this);//水深
        deep.setText("0");
        tableRow.addView(deep);

        tablelayout_cal.addView(tableRow);
    }

    private void resetRowNumber(){
        for(int i=1,size=tablelayout_cal.getChildCount();i<size;++i){
            TableRow tableRow = (TableRow) tablelayout_cal.getChildAt(i);
            setNumberText(tableRow, ""+i);
        }

    }

    /**
     * EventBus 异步
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SpeedDataSelectBus speedDataBus) {
        switch (speedDataBus.getId()) {
            case SpeedDataContant.ON_SELECT_SPEED_DATA:{
                SpeedData speedData = speedDataBus.getSpeedData();
                int index = speedDataBus.getRowIndex();
                if(index<2||index>=(tablelayout_cal.getChildCount()-1)){//第一行，最后一行排除
                    MyLog.e(TAG, "选择速度 index越界：rowIndex:"+index+", count:"+tablelayout_cal.getChildCount());
                    return;
                }
                TableRow tableRow = (TableRow) tablelayout_cal.getChildAt(index);
                setDianText(tableRow, ""+speedDataBus.getOrder());

                setSpeedText(tableRow, ""+speedData.getSpeed());
                break;
            }
        }
    }

    private void setNumberText(TableRow tableRow, String text){
        TextView rowNumber = (TextView) tableRow.getChildAt(1);//序号
        rowNumber.setText(text);
    }

    private void setDianText(TableRow tableRow, String text){
        TextView dian = (TextView) tableRow.getChildAt(2);//点号
        dian.setText(text);
    }
    private void setDefaultDianText(TableRow tableRow){
        TextView dian = (TextView) tableRow.getChildAt(2);//点号
        dian.setText(".");
    }
    private void setSpeedText(TableRow tableRow, String text){
        EditText speed = (EditText) tableRow.getChildAt(3);//速度
        speed.setText(text);
    }

    private float getSpeed(TableRow tableRow){
        EditText speedEdit1 = (EditText) tableRow.getChildAt(3);
        return Float.parseFloat(speedEdit1.getText().toString());//速度
    }
    private BigDecimal getSpeedBigDecimal(TableRow tableRow){
        EditText speedEdit1 = (EditText) tableRow.getChildAt(3);
        return new BigDecimal(speedEdit1.getText().toString());//速度
    }
    private void setBeginDistance(TableRow tableRow, String text){
        EditText distanceEdit1 = (EditText) tableRow.getChildAt(4);//起点距
        distanceEdit1.setText(text);
    }
    private float getBeginDistance(TableRow tableRow){
        EditText distanceEdit1 = (EditText) tableRow.getChildAt(4);
        return Float.parseFloat(distanceEdit1.getText().toString());//起点距
    }
    private BigDecimal getBeginDistanceBigDecimal(TableRow tableRow){
        EditText distanceEdit1 = (EditText) tableRow.getChildAt(4);
        return new BigDecimal(distanceEdit1.getText().toString());//起点距
    }
    private void setWaterDeep(TableRow tableRow, String text){
        EditText deepEdit1 = (EditText) tableRow.getChildAt(5);//水深
        deepEdit1.setText(text);
    }
    private float getWaterDeep(TableRow tableRow){
        EditText deepEdit1 = (EditText) tableRow.getChildAt(5);
        return Float.parseFloat(deepEdit1.getText().toString());//水深
    }
    private BigDecimal getWaterDeepBigDecimal(TableRow tableRow){
        EditText deepEdit1 = (EditText) tableRow.getChildAt(5);
        return new BigDecimal(deepEdit1.getText().toString());//水深
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
