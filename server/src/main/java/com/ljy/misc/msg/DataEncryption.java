package com.ljy.misc.msg;

/**
 * ���ݼ���
 * @author zss
 *
 */
public class DataEncryption {
/**
 * ��λȡ��
 * @param data 
 * @return
 */
 public  static boolean BitReversion(byte [] data,int begin)
 {
	 for(int i=begin;i<data.length;i++)
		{
			int temp=(int)data[i];
			temp=~temp;
			data[i]=(byte)temp;
		}
	 return true;
 }
}
