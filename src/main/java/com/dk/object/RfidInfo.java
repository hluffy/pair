package com.dk.object;

import java.io.Serializable;
import java.sql.Timestamp;

public class RfidInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3316864717248167299L;
	private long id;
	private String name;
	private String epc;
	private Timestamp createTime;
	private String pc;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEpc() {
		return epc;
	}
	public void setEpc(String epc) {
		this.epc = epc;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public String getPc() {
		return pc;
	}
	public void setPc(String pc) {
		this.pc = pc;
	}
	

}
