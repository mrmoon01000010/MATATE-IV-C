package pls_stop;

public class Date {
	short day;
	char month;
	short year;
	public Date(short day, char month, short year) {
		this.day = day;
		this.month = month;
		this.year = year;
	}
	
	public String getName() {
		return Integer.toString(day) + " de " + MonthName(month);
	}
	public short Day_state() {
		String current_date = java.time.LocalDate.now().toString();
		String[] current_date_split = current_date.split("-");
		if(this.year<Short.parseShort(current_date_split[0]))
			return -1;
		if(this.year>Short.parseShort(current_date_split[0]))
			return 1;
		if(this.month<Short.parseShort(current_date_split[1]))
			return -1;
		if(this.month>Short.parseShort(current_date_split[1]))
			return 1;
		if(this.day<Short.parseShort(current_date_split[2]))
			return -1;
		if(this.day>Short.parseShort(current_date_split[2]))
			return 1;
		return 0;
	}
	
	public static Date getDate(short day_of_year, short current_year) {
		short leap_day = (short) (isLeapYear(current_year)?1:0);
		if(day_of_year < 1)
			return getDate((short) (day_of_year + 365 + (isLeapYear((short) (current_year - 1))?1:0)), (short) (current_year - 1));
		if(day_of_year < 32)
			return new Date(day_of_year, (char) 1, current_year);
		if(day_of_year < 60 + leap_day)
			return new Date((short) (day_of_year - 31), (char) 2, current_year);
		if(day_of_year < 91 + leap_day)
			return new Date((short) (day_of_year - 59 - leap_day), (char) 3, current_year);
		if(day_of_year < 121 + leap_day)
			return new Date((short) (day_of_year - 90 - leap_day), (char) 4, current_year);
		if(day_of_year < 152 + leap_day)
			return new Date((short) (day_of_year - 120 - leap_day), (char) 5, current_year);
		if(day_of_year < 182 + leap_day)
			return new Date((short) (day_of_year - 151 - leap_day), (char) 6, current_year);
		if(day_of_year < 213 + leap_day)
			return new Date((short) (day_of_year - 181 - leap_day), (char) 7, current_year);
		if(day_of_year < 244 + leap_day)
			return new Date((short) (day_of_year - 212 - leap_day), (char) 8, current_year);
		if(day_of_year < 274 + leap_day)
			return new Date((short) (day_of_year - 243 - leap_day), (char) 9, current_year);
		if(day_of_year < 305 + leap_day)
			return new Date((short) (day_of_year - 273 - leap_day), (char) 10, current_year);
		if(day_of_year < 335 + leap_day)
			return new Date((short) (day_of_year - 304 - leap_day), (char) 11, current_year);
		if(day_of_year < 366 + leap_day)
			return new Date((short) (day_of_year - 334 - leap_day), (char) 12, current_year);
		return getDate((short) (day_of_year - 365 - leap_day), (short) (current_year + 1));
	}
	public static boolean isLeapYear(short current_year) {
		if(current_year%4!=0)
			return false;
		if(current_year%400==0)
			return true;
		if(current_year%100==0)
			return false;
		return true;
	}
	public static String MonthName(char month) {
		String product = "Mistakes were made";
		switch(month) {
		case 1:
			product = "Enero";
			break;
		case 2:
			product = "Febrero";
			break;
		case 3:
			product = "Marzo";
			break;
		case 4:
			product = "Abril";
			break;
		case 5:
			product = "Mayo";
			break;
		case 6:
			product = "Junio";
			break;
		case 7:
			product = "Julio";
			break;
		case 8:
			product = "Agosto";
			break;
		case 9:
			product = "Septiembre";
			break;
		case 10:
			product = "Octubre";
			break;
		case 11:
			product = "Noviembre";
			break;
		case 12:
			product = "Diciembre";
			break;
		}
		return product;
	}
	public static String MonthNameNow() {
		return MonthName((char) Integer.parseInt(java.time.LocalDate.now().toString().split("-")[1]));
	}
	public static int CurrentYear() {
		return Integer.parseInt(java.time.LocalDate.now().toString().split("-")[0]);
	}
	public static int FirstDayOfWeek() {
		String[] days_y_w = java.time.LocalDate.now().format(new java.time.format.DateTimeFormatterBuilder().appendPattern("D-e").toFormatter()).split("-");
		System.out.println(days_y_w[0] + "-" + days_y_w[1]);
		return Integer.parseInt(days_y_w[0]) - Integer.parseInt(days_y_w[1]) + 2;
	}
}
