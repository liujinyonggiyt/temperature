package com.ljy.misc.trigger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.PriorityQueue;

/**
 * 触发器系统，把所有的计时器放到排序队列里，然后刷帧检测第1个计时器是否到时间，触发回调。
 * 想要取消某个计时器是调用Timer类的closeTimer函数
 * Created by lishile on 2016/3/8.
 */
public class TriggerSystem implements TriggerInterface {

    private static final Logger logger = LoggerFactory.getLogger(TriggerSystem.class);
    /**
     * 该字段加volatile是由于在AbsRedisReadWriteThreadMrg类里redis线程读取该字段，而set是在游戏主线程中刷帧更新的
     */
    private static volatile long curMilltime;

    public TriggerSystem(){
    }

    /**
     * 所有计时器队列，按触发时间从小到大排列
     */
    private final PriorityQueue<Timer> timerQueue = new PriorityQueue<>((Timer o1, Timer o2) -> {
        long triggerTime1 = o1.getTriggerTime();
        long triggerTime2 = o2.getTriggerTime();
        if (triggerTime1 > triggerTime2) {
            return 1;
        } else if (triggerTime1 == triggerTime2) {
            return 0;
        } else {
            return -1;
        }
    });

    @Override
    public void addTimer(Timer timer) {
        timer.setStartTime(curMilltime);
        timerQueue.offer(timer);
    }

    @Override
    public void tickTrigger() {
        Timer tempTimer;

        for (;;) {
            //获取但不移除此队列的头
            tempTimer = timerQueue.peek();

            if (null == tempTimer) {
                return;
            }
            //时候未到
            if (curMilltime <= tempTimer.getTriggerTime()) {
                return;
            }
            //移除队列的第一个计时器
            timerQueue.poll();
            
            if (tempTimer.getExecuteNum() <= 0) {
                //该计时器所有次数都执行完了
                continue;
            }

            try {
                tempTimer.setExecuteNum(tempTimer.getExecuteNum() - 1);
                //触发计时器到时间的回调
                tempTimer.trigger();
            } catch (Exception e) {
                logger.error("", e);
            }

            if (tempTimer.getExecuteNum() <= 0) {
                continue;
            }
            //次数还没执行完，则更新起始时间戳并加入队列
            tempTimer.setStartTime(curMilltime);
            timerQueue.offer(tempTimer);
        }
    }

	public static long getCurMilltime() {
		return curMilltime;
	}

	public static void setCurMilltime(long curMilltime) {
		TriggerSystem.curMilltime = curMilltime;
	}
}
