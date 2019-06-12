package ljy.mrg;

import android.support.v4.util.Preconditions;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.List;

import ljy.mapping.SpeedData;
import ljy.utils.MyLog;

/**
 * 单例模式
 */
public class SqliteMrg {
    private static final String TAG = SqliteMrg.class.getSimpleName();
    private static class Holder{
        private static SqliteMrg sqliteMrg = new SqliteMrg();
    }
    public static SqliteMrg getInstance(){
        return Holder.sqliteMrg;
    }

    private SqliteMrg(){
        SpeedData speedData = findLast(SpeedData.class);
        if(null == speedData){
            SpeedData.setNextId(1);
        }else{
            SpeedData.setNextId(speedData.getOrder()+1);
        }
    }

    public <T extends LitePalSupport> boolean save(T bean){
        boolean isSuccess = false;
        try{
            isSuccess = bean.save();
            if(isSuccess){
                if(bean instanceof SpeedData){
                    SpeedData speedData = (SpeedData) bean;
                    if(speedData.getOrder()>=SpeedData.getNextId()){
                        SpeedData.setNextId(speedData.getOrder()+1);
                    }
                }
            }
        }catch (Exception e){
            isSuccess = false;
            MyLog.e(TAG, e.getMessage());
        }

        return isSuccess;
    }

    public <T extends LitePalSupport> List<T> findAll(Class<T> clazz){
        return LitePal.findAll(clazz);
    }

    public <T extends LitePalSupport> T findLast(Class<T> clazz){
        return LitePal.findLast(clazz);
    }

    public <T extends LitePalSupport> List<T> findByPage(Class<T> clazz, int page, int sizePerPage){
        assert page>0 && sizePerPage>0;
        return LitePal.limit(sizePerPage).offset((page-1)*sizePerPage).find(clazz);
    }

    public <T extends LitePalSupport> int count(Class<T> clazz){
        return LitePal.count(clazz);
    }
    public <T extends LitePalSupport> int delete(Class<T> clazz, long id){
        return LitePal.delete(clazz, id);
    }
}
