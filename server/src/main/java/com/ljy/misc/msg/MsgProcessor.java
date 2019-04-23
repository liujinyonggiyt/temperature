package com.ljy.misc.msg;

import com.heishi.commons.GamePlayer;
import com.heishi.commons.logs.LoggerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ����������Ϣ���࣬������һ��ҵ���߼������㡣<br/>
 * ��������дproccesse������ʵ��ҵ���߼���
 * 
 * @author wutao
 * 
 */
public abstract class MsgProcessor {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	//�������Ƿ����  ������ʱ����Ϣ
	private boolean enable=true;
	/**
	 * ����������Ϣ���߼����㡣
	 * 
	 * @param session
	 *            �����߼�����ĻỰ��ӳ��һ���ͻ���
	 * @param request
	 *            ������Ϣ
	 * @return ������Ϻ����ɵ���Ӧ��Ϣ
	 */
	public abstract void proccesse(GamePlayer session, RequestMsg request)throws Exception;
	public boolean isEnable() {
		return enable;
	}
	public void setEnable(boolean enable) {
		this.enable = enable;
	}	
}
