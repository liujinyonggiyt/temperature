package com.ljy.misc.msg;


/**
 * �˽ӿ���������Ӧ��Ϣ���뺯���������������ÿ����Ӧ��Ϣ������ʵ�ִ˽ӿڡ�<br>
 * ���������ĺ�����protocolEncoder�лᱻ�Զ����ã������byte stream���͵��ͻ��ˡ�
 * 
 * @author liuhengli
 * 
 */
public interface ResponseMsg {
	/**
	 * �����Ϣ�� 
	 * @return
	 */
	public int getMsgCode();
	
	public int getTotalBytes();
	/**
	 * ������Ϣ��
	 * @param code
	 */
	public void setMsgCode(int code); 
	/**
	 * ������Ϣ��������
	 * @return
	 */
	public IoBuffer entireMsg(); 
}
