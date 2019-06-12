package ljy.mrg;

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

    }

    public int getNextSpeedDataId(){
        return count(SpeedData.class)+1;
    }
    public <T extends LitePalSupport> boolean save(int order, SpeedData speedData){
        boolean isSuccess = false;
        try{
            if(order>=getNextSpeedDataId()){//插入
                isSuccess = speedData.save();
            }else{//替换
                SpeedData oldData = findByOffset(SpeedData.class, order-1);
                oldData.copyFrom(speedData);
                isSuccess = oldData.save();
            }
        }catch (Exception e){
            isSuccess = false;
            MyLog.e(TAG, e.getMessage());
        }

        return isSuccess;
    }

    public <T extends LitePalSupport> boolean save(T bean){
        boolean isSuccess = false;
        try{
            isSuccess = bean.save();
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
    public <T extends LitePalSupport> T findByOffset(Class<T> clazz, int offset){
        List<T> list = LitePal.limit(1).offset(offset).find(clazz);
        if(list.isEmpty()){
            return null;
        }else{
            return list.get(0);
        }
    }

    public <T extends LitePalSupport> int count(Class<T> clazz){
        return LitePal.count(clazz);
    }
    public <T extends LitePalSupport> int delete(Class<T> clazz, long id){
        return LitePal.delete(clazz, id);
    }
}
