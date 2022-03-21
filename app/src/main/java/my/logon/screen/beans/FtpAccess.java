package my.logon.screen.beans;

public class FtpAccess {

	private String ip;
	private String user;
	private String pass;
	private String verFile;
	private String apkFile;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getVerFile() {
		return verFile;
	}

	public void setVerFile(String verFile) {
		this.verFile = verFile;
	}

	public String getApkFile() {
		return apkFile;
	}

	public void setApkFile(String apkFile) {
		this.apkFile = apkFile;
	}

	@Override
	public String toString() {
		return "FtpAccess [ip=" + ip + ", user=" + user + ", pass=" + pass + ", verFile=" + verFile + ", apkFile=" + apkFile + "]";
	}

}
