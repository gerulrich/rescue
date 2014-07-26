package net.cloudengine.service.impl;

import com.jcraft.jsch.SftpProgressMonitor;

public class ProgressMonitor implements SftpProgressMonitor {
	
	private long count = 0L;
	private long max = 0L;
	private long percent = -1L;
	private boolean end = false;

	public void init(int op, String src, String dest, long max) {
		this.max = max;
		this.count = 0L;
		this.percent = -1L;
	}

	public boolean count(long count) {
		this.count += count;
		if (this.percent >= this.count * 100L / this.max)
			return true;
		this.percent = (this.count * 100L / this.max);
		return true;
	}

	public void end() {
		end = true;
	}

	public boolean isEnd() {
		return end;
	}
	
}