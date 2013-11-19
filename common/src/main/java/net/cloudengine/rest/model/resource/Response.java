package net.cloudengine.rest.model.resource;

public class Response<T> {
	
	public static final int OK_RESPONSE = 0;
	public static final int ERROR_RESPONSE = 1;
	public static final int UNAUTHRORIZED = 2;
	public static final String SUCCESSFUL = "successful";
	public static final String INVALID_CREDENTIALS = "invalid credentials";
	
	private int status;
	private T data;
	private String msg;
	
	public Response(int status, T data, String msg) {
		super();
		this.status = status;
		this.msg = msg;
		this.data = data;
	}
	
	public Response(int status) {
		this(status, null, null);
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
