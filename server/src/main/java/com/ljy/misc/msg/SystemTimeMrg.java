package com.ljy.misc.msg;

import com.google.inject.Inject;
import com.ljy.misc.trigger.TriggerSystem;

/**
 * 
 * @author liujinyong
 *
 */
public class SystemTimeMrg
{

	/**
	 * 1秒多少帧
	 */
	private static final int TICK_TIMES_PER_SECOND = 25;

	/**
	 * 每一帧的毫秒时间
	 */
	private final long PER_TICK_MILL_TIME;

	/**
	 * 系统的毫秒时间戳
	 */
	private long sysMillTime;

	/**
	 * 系统的秒数时间戳
	 */
	private int sysSecTime;
	/**
	 * 帧率间隔
	 */
	private int deltaMilltime;

	@Inject
	public SystemTimeMrg()
	{

		PER_TICK_MILL_TIME = 1000 / TICK_TIMES_PER_SECOND;

		setTime(System.currentTimeMillis());
	}

	public long getSysMillTime()
	{
		return sysMillTime;
	}

	public int getSysSecTime()
	{
		return sysSecTime;
	}

	/**
	 * 更新游戏时间戳
	 * @return 返回两帧时间间隔多少毫秒，如果还未到每一帧的毫秒时间则返回0
	 */
	public int update()
	{
		long cur = System.currentTimeMillis();
		//当前时间戳减去咱游戏的时间戳，得到从上次更新游戏时间戳已经过了多久
		long escapedMillTime = cur - sysMillTime;
		//已流逝的时间小于刷帧的时间则return 0跳过，否则更新游戏时间戳、delta（两帧时间差）
        //比如1秒更新25帧，即每40毫秒刷一帧，还没过40毫秒则返回0不刷新
		if(escapedMillTime < PER_TICK_MILL_TIME)
		{
			return 0;
		}

		setTime(cur);
		deltaMilltime=(int)escapedMillTime;
		return deltaMilltime;
	}

	private void setTime(long curSysMillTime)
	{

		sysMillTime = curSysMillTime;

		sysSecTime = (int)(curSysMillTime / 1000);
		
		TriggerSystem.setCurMilltime(curSysMillTime);

	}

	public int getDeltaMilltime() {
		return deltaMilltime;
	}

}
