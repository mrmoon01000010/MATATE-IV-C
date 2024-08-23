package pls_stop;

public class ToDo {
	private String name;
	private long dueTime;
	private short dueDay;
	
	public ToDo(String name, long dueTime, short dueDay) {
		this.name = name;
		this.dueTime = dueTime;
		this.dueDay = dueDay;
	}
	
	public String getName() {
		return name;
	}
	public long getDueTime() {
		return dueTime;
	}
	public short getDueDay() {
		return dueDay;
	}
	public String toString() {
		return dueDay + ":" + dueTime + ":" + name;
	}
}
