package com.ljy.misc.net;

/**
 * 
 * @author lishile
 *
 */
public class ConnectionInfo {

	private String host;

	private int port;

	public ConnectionInfo(String host, int port) {

		this.setHost(host);
		
		this.setPort(port);

	}
	
	@Override
	public String toString(){
		
		return host + ":" + port;
		
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
