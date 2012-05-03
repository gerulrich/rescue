package net.cloudengine.pbx;

import java.util.Date;

public class CTIQueueEntry {
	
	private Date date;
	private String number;
	private String queue;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getQueue() {
		return queue;
	}
	public void setQueue(String queue) {
		this.queue = queue;
	}

}
