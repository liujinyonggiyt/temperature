package ljy.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import ljy.base.activity.BaseActivity;
import ljy.base.bean.BluRxBean;
import ljy.base.bean.SpeedDataBus;
import ljy.base.constant.SpeedDataContant;
import ljy.bluetooth.R;
import ljy.mapping.SpeedData;
import ljy.misc.SpeedDataItemAdapter;
import ljy.mrg.SqliteMrg;
import ljy.mrg.SystemTimeMrg;
import ljy.utils.MyLog;
import ljy.view.dialog.CreateSpeedDataDialog;
import ljy.widget.TitleBar;

public class SpeedDataListActivity extends BaseActivity {
    private static final String TAG = SpeedDataListActivity.class.getSimpleName();
    @BindView(R.id.titlebar)
    TitleBar titlebar;

    @BindView(R.id.speed_data_listView)
    ListView listView;
    @BindView(R.id.textView_xuanze)
    TextView textView_xuanze;
    @BindView(R.id.all_check_btn)
    TextView allCheckBox;
    @BindView(R.id.textView_quxiao)
    TextView textView_quxiao;
    @BindView(R.id.rizhi_textView_shanchu)
    TextView rizhi_textView_shanchu;


    private List<SpeedData> speedDataList = new ArrayList<>();
    SpeedDataItemAdapter adapter;
    View footer;//获取“正在加载数据”的显示字样，要在new出适配器之前进行显示
    private final int sizePerPage = 20;
    private int maxPage;
    private boolean loadFinish = true;

    private int num_xuanze = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            EventBus.getDefault().register(this);

            titlebar.setTitle("存储的数据列表");
            titlebar.setBackgroundResource(R.color.blue);
            titlebar.setImmersive(true);
            titlebar.setTitleColor(Color.WHITE);

            footer = getLayoutInflater().inflate(R.layout.loading, null);
            listView.setOnScrollListener(new ScrollLinstener());
            speedDataList.addAll(findByPage(1));
            //初始化选中数量
            for(SpeedData speedData:speedDataList){
                if(speedData.checked){
                    num_xuanze++;
                }
            }

            adapter = new SpeedDataItemAdapter(SpeedDataListActivity.this, R.layout.speed_list_item, speedDataList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SpeedData speedData = speedDataList.get(position);
                    CreateSpeedDataDialog createSpeedDataDialog = new CreateSpeedDataDialog(SpeedDataListActivity.this, speedData);
                    createSpeedDataDialog.show();
                }
            });
            listView.removeFooterView(footer);

            int maxSize = SqliteMrg.getInstance().count(SpeedData.class);
            maxPage = maxSize%sizePerPage==0?(maxSize/sizePerPage):(maxSize/sizePerPage+1);

            textView_xuanze.setText("已选中"+num_xuanze+"项");

            rizhi_textView_shanchu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<SpeedData> delitem=new ArrayList<SpeedData>();
                    for(Iterator<SpeedData> iter = speedDataList.iterator();iter.hasNext();){
                        SpeedData speedData = iter.next();
                        if(speedData.checked){
                            iter.remove();//从缓存中删除
                            delitem.add(speedData);
                            speedData.delete();
                        }
                    }

                    adapter.notifyDataSetChanged();
                    num_xuanze=0;
                    textView_xuanze.setText("已选中"+num_xuanze+"项");
                }
            });

           textView_quxiao.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   for(int i=0;i<speedDataList.size();i++){
                       SpeedData speedData = speedDataList.get(i);
                       if(speedData.checked){
                           speedData.checked=false;
                       }
                   }
                   allCheckBox.setTextColor(getResources().getColor(R.color.red));
                   num_xuanze=0;
                   textView_xuanze.setText("已选中"+num_xuanze+"项");
                   adapter.notifyDataSetChanged();
               }
           });
        }catch (Exception e){
            MyLog.e(Tag, e.getMessage(), e);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_speed_data_list;
    }


    /**
     * EventBus 异步
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SpeedDataBus speedDataBus) {
        switch (speedDataBus.getId()) {
            case SpeedDataContant.ON_SPEED_LIST_ITEM_CHECKED:{
                SpeedData speedData = speedDataBus.getSpeedData();
                if(speedDataBus.isChecked() != speedData.checked) {//选项有变化
                    if (speedDataBus.isChecked()) {//未选中=》选中
                        num_xuanze++;
                        speedData.checked = true;
                    } else {//选中=》未选中
                        num_xuanze--;
                        speedData.checked = false;
                    }
                    textView_xuanze.setText("已选中" + num_xuanze + "项");
                    adapter.notifyDataSetChanged();
                }
                break;
            }
            case SpeedDataContant.ON_SPEED_DATA_SAVE:{
                adapter.notifyDataSetChanged();
                break;
            }
        }
    }
    /**
     * 监听事件
     *
     * @param view
     */
    @OnClick({R.id.all_check_btn})
    public void onViewClicked(View view) {
        try {
            switch (view.getId()) {
                case R.id.all_check_btn:{//全选
                    for(int i=0;i<speedDataList.size();i++){
                        SpeedData speedData = speedDataList.get(i);
                        if(speedData.checked==false){
                            speedData.checked=true;
                        }
                    }

                    allCheckBox.setTextColor(getResources().getColor(R.color.lv));
                    adapter.notifyDataSetChanged();
                    num_xuanze=speedDataList.size();
                    textView_xuanze.setText("已选中"+num_xuanze+"项");
                    break;
                }
            }
        }catch (Exception e){
            MyLog.e(Tag, e.getMessage(), e);
        }
    }


    private List<SpeedData> findByPage(int page){
        return SqliteMrg.getInstance().findByPage(SpeedData.class, page, sizePerPage);
    }

    private class ScrollLinstener implements AbsListView.OnScrollListener {

        /**
         * 这个方法时在滑动的状态发生改变时调用，一般不需要在里面处理什么事件
         * @param view
         * @param scrollState
         */
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
//            MyLog.i(TAG, "onScrollStateChanged!curState:"+scrollState);
        }

        /**
         * 当滑动屏幕的时候就会触发该事件
         * @param view  当前显示条目的视图组件
         * @param firstVisibleItem  第一个条目
         * @param visibleItemCount  当前屏幕中的条目总数
         * @param totalItemCount 需要显示的全部条目总数（开始打开应用时是0，加载出来后是20，然后是40...）
         */
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            final int loadTotal = totalItemCount;
            //获取当前屏幕最后Item的id
            int lastItemId = listView.getLastVisiblePosition();
            if((lastItemId+1) == totalItemCount){//达到数据的最后一条记录
                if(totalItemCount>0){
                    //记录当前页
                    int curPage = totalItemCount%sizePerPage==0?totalItemCount/sizePerPage:totalItemCount/sizePerPage+1;
                    int nextPage = curPage+1;//记录下一页
                    if(nextPage <= maxPage && loadFinish){
                        loadFinish = false;
                        listView.addFooterView(footer);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
//                                try {
//                                    Thread.sleep(3000);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
                                List<SpeedData> result = findByPage(nextPage);
                                handler.sendMessage(handler.obtainMessage(100, result));
                            }
                        }).start();
                    }
                }
            }
        }

        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                speedDataList.addAll((Collection<? extends SpeedData>) msg.obj);
                adapter.notifyDataSetChanged();
                if(listView.getFooterViewsCount()>0){
                    listView.removeFooterView(footer);
                }
                loadFinish = true;

            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
