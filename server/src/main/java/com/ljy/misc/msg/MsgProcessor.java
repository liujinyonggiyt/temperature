package com.ljy.misc.msg;

import com.heishi.commons.GamePlayer;
import com.heishi.commons.logs.LoggerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 处理请求消息的类，代表了一个业务逻辑的运算。<br/>
 * 子类请重写proccesse函数，实现业务逻辑。
 * 
 * @author wutao
 * 
 */
public abstract class MsgProcessor {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	//处理器是否可用  用于临时封消息
	private boolean enable=true;
	/**
	 * 处理请求消息，逻辑运算。
	 * 
	 * @param session
	 *            请求逻辑运算的会话，映射一个客户端
	 * @param request
	 *            请求消息
	 * @return 计算完毕后生成的响应消息
	 */
	public abstract void proccesse(GamePlayer session, RequestMsg request)throws Exception;
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}	
}
