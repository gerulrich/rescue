package net.cloudengine.web;

public class LoginForm {
	
	private String j_username;
	private String j_password;
	private boolean remember_me;
	
	public String getJ_username() {
		return j_username;
	}
	public void setJ_username(String j_username) {
		this.j_username = j_username;
	}
	public String getJ_password() {
		return j_password;
	}
	public void setJ_password(String j_password) {
		this.j_password = j_password;
	}
	public boolean isRemember_me() {
		return remember_me;
	}
	public void setRemember_me(boolean remember_me) {
		this.remember_me = remember_me;
	}
}
