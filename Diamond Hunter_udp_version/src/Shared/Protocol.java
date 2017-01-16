package Shared;

import java.io.Serializable;
import java.util.Vector;

public class Protocol implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4730536625245540934L;
	/**
	 * 
	 */
	String id;
	String type;
	String msg;
	int x, y;
	int uCount;
	int cmdCount;
	String cmd;
	

	public int getCmdCount() {
		return cmdCount;
	}

	public void setCmdCount(int cmdCount) {
		this.cmdCount = cmdCount;
	}
	
	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public int getuCount() {
		return uCount;
	}

	public void setuCount(int uCount) {
		this.uCount = uCount;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}