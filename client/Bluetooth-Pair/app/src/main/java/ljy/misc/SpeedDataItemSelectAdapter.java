package ljy.misc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import ljy.base.bean.SpeedDataBus;
import ljy.base.constant.SpeedDataContant;
import ljy.bluetooth.R;
import ljy.mapping.SpeedData;

public class SpeedDataItemSelectAdapter extends ArrayAdapter<SpeedData> {
    private int resourceId;
    public SpeedDataItemSelectAdapter(Context context, int resource, List<SpeedData> datas) {
        super(context, resource, datas);
        resourceId = resource;
    }

    /**
     * 每个子项滚动到屏幕内的时候都会被调用
     * @param position
     * @param convertView 用于将之前加载好的布局进行缓存，以便之后可以进行重用
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.order = view.findViewById(R.id.speed_list_item_order);
            viewHolder.speed = view.findViewById(R.id.speed_list_item_speed);
            viewHolder.time = view.findViewById(R.id.speed_list_item_time);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        SpeedData speedData = getItem(position);
        viewHolder.order.setText("序号："+(position+1)+"， ");
        viewHolder.speed.setText("速度："+speedData.getSpeed()+"M/s， ");
        viewHolder.time.setText("时间："+speedData.getTime());
        return view;
    }

    class ViewHolder{
        TextView order;
        TextView speed;
        TextView time;

    }
}
