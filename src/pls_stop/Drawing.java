package pls_stop;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Drawing extends JPanel {
	private static final long serialVersionUID = 1L;
	
	int Calendar_Height_Divisions = 5;
	float Calendar_x_offset = 0;
	float Calendar_y_offset = 0.1f;
	float Calendar_height_usage = 0.9f;
	Color[][] Colors;
	private int Calendar_x_start;
	private int Calendar_y_start;
	private int Calendar_width;
	private int Calendar_height;
	private int Day_width;
	private int Day_height;
	private int Day_x;
	private int Day_y;
	private short edit_day;
	private boolean editting = false;
	private boolean deleting = false;
	private boolean adding = false;
	private boolean full_week = false;
	public boolean cursor = false;
	private String new_event = "";
	private int selected;
	private short first_day = 213;
	private short saved_day = 213;
	private short first_year = 2022;
	private short edit_i = 0;
	private short edit_j = 0;
	private Image image;
	private int current_image = 0;
	private Random random;
	private HashMap<Short, ArrayList<ToDo>> todo;
	
	public Drawing() {
		first_day = (short) Date.FirstDayOfWeek();
		first_year = (short) Date.CurrentYear();
		random = new Random(0);
		generate_colors(false);
		recover_todo();
		newImage();
	}
	
	private void generate_colors(boolean edit) {
		if(edit) {
			Colors = new Color[1][1];
			if(first_day == 239) {
				Colors[0][0] = new Color(0xD2, 0x9B, 0xFB);
				return;
			}
			if(first_day == 243) {
				Colors[0][0] = new Color(0, 255, 255);
				return;
			}
			if(first_day == 360) {
				Colors[0][0] = new Color(0x93, 0x13, 0x23);
				return;
			}
			random.setSeed(first_day);
			random.nextDouble();
			random.nextDouble();
			random.nextDouble();
			
			int R = 175;
			int G = 250;
			int B = 250;
			
			double rand = random.nextDouble();
			if(rand < 0.25)
				R = 100;
			else if(rand < 0.5)
				R = 125;
			else if(rand < 0.75)
				R = 150;
			
			rand = random.nextDouble();
			if(rand < 0.25)
				G = 175;
			else if(rand < 0.5)
				G = 200;
			else if(rand < 0.75)
				G = 225;
			
			rand = random.nextDouble();
			if(rand < 0.25)
				B = 175;
			else if(rand < 0.5)
				B = 200;
			else if(rand < 0.75)
				B = 225;
			
			Colors[0][0] = new Color(R, G, B);
			return;
		}
		Colors = new Color[7][Calendar_Height_Divisions];
		for(int i = 0; i<7; i++) {
			for(int j = 0; j<Calendar_Height_Divisions; j++) {
				if(first_day+i+7*j == 239) {
					Colors[i][j] = new Color(0xD2, 0x9B, 0xFB);
					continue;
				}
				if(first_day+i+7*j == 243) {
					Colors[i][j] = new Color(0, 255, 255);
					continue;
				}
				if(first_day+i+7*j == 360) {
					Colors[i][j] = new Color(0x93, 0x13, 0x23);
					continue;
				}
				random.setSeed(first_day+i+7*j);
				random.nextDouble();
				random.nextDouble();
				random.nextDouble();
				
				int R = 175;
				int G = 250;
				int B = 250;
				
				double rand = random.nextDouble();
				if(rand < 0.25)
					R = 100;
				else if(rand < 0.5)
					R = 125;
				else if(rand < 0.75)
					R = 150;
				
				rand = random.nextDouble();
				if(rand < 0.25)
					G = 175;
				else if(rand < 0.5)
					G = 200;
				else if(rand < 0.75)
					G = 225;
				
				rand = random.nextDouble();
				if(rand < 0.25)
					B = 175;
				else if(rand < 0.5)
					B = 200;
				else if(rand < 0.75)
					B = 225;
				
				Colors[i][j] = new Color(R, G, B);
			}
		}
	}
	private void recover_todo() {
		todo = Files.readSavedToDo();
	}

	public void paintComponent(Graphics g) {
		draw_banner(g);
		draw_calendar(g);
		if(!full_week)
			draw_propaganda(g);
	}
	private void draw_banner(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, (int) this.getWidth(), (int) (Calendar_y_offset * this.getHeight()));
		g.setColor(Color.WHITE);
		g.setFont(new Font(g.getFont().getFontName(), Font.CENTER_BASELINE, 20));
		g.drawString(java.time.LocalDateTime.now().format(new java.time.format.DateTimeFormatterBuilder()
				.appendPattern("dd 'de " + Date.MonthNameNow() + " del año' yyyy, 'la hora es:' HH:mm:ss").toFormatter()), 30, 30);
		g.drawString("Mostrando mes de " + 
				Date.MonthName(Date.getDate((short) (first_day+ (editting?0:4) + (Calendar_Height_Divisions-1)/2), (short) (first_year)).month) +
				" del año " + 
				Date.getDate((short) (first_day+ (editting?0:4) + (Calendar_Height_Divisions-1)/2), (short) (first_year)).year, 30, 60);
		if(adding) {
			g.drawString(new_event + (cursor?"_":""), this.getWidth()/2, 30);
		}
	}
	private void draw_calendar(Graphics g) {
		Calendar_x_start = (int) (this.getWidth()*Calendar_x_offset)+1;
		Calendar_y_start = (int) (this.getHeight()*Calendar_y_offset);
		Calendar_width = this.getWidth();
		Calendar_height = (int) (this.getHeight()*Calendar_height_usage);
		Day_width = Calendar_width/(editting?1:7)+1;
		Day_height = Calendar_height/Calendar_Height_Divisions+1;
		for(int i = 0; i<(editting?1:7); i++) {
			for(int j = 0; j<Calendar_Height_Divisions; j++) {
				Day_x = ((Calendar_width)*i)/(editting?1:7);
				Day_y = (Calendar_height*j)/Calendar_Height_Divisions;
				draw_day(i, j, g);
			}
		}
		g.setColor(Color.BLACK);
		for(int i = -1; i<(editting?1:7); i++) {
			int x = ((Calendar_width-2)*(i+1))/(editting?1:7);
			g.fillRect(Calendar_x_start-1 + x, Calendar_y_start-1, 2, Calendar_height+2);
		}
		if(!full_week) {
			int x = ((Calendar_width-2)*(4+1))/7;
			g.fillRect(Calendar_x_start-1 + x, Calendar_y_start-1, 2, Calendar_height+2);
		}
		for(int j = -1; j<Calendar_Height_Divisions; j++) {
			int y = (Calendar_height*(j+1))/Calendar_Height_Divisions;
			g.fillRect(Calendar_x_start-1, Calendar_y_start + y-1, Calendar_width+2, 2);
		}
	}
	private void draw_day(int i, int j, Graphics g) {
		g.setColor(Colors[i][j]);
		short current_day = (short) (first_day+i+7*j);
		boolean deleting_day = false;
		if(current_day == edit_day && editting && deleting) deleting_day = true;
		String current_day_name = Date.getDate(current_day, (short) first_year).getName();
		g.fillRect(Calendar_x_start + Day_x, Calendar_y_start + Day_y, Day_width, Day_height);
		g.setColor(Color.black);
		if(current_day == 360)
			g.setColor(new Color(0xd2, 0xad, 0x32));
		g.setFont(new Font(g.getFont().getFontName(), Font.CENTER_BASELINE, 20));
		g.drawString(current_day_name, Calendar_x_start + Day_x+5, Calendar_y_start + Day_y+29);
		g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 12));
		if(todo.containsKey(current_day)) {
			ArrayList<ToDo> current_todo = todo.get(current_day);
			for(int k = 0; k < current_todo.size(); k++) {
				if(deleting_day && k == selected) {
					g.setColor(new Color(255, 0, 0));
					g.fillRect(Calendar_x_start + Day_x, Calendar_y_start + Day_y+37+k*15, Day_width, 15);
					g.setColor(Color.black);
				}
				g.drawString(current_todo.get(k).getName(), Calendar_x_start + Day_x+5, Calendar_y_start + Day_y+50+k*15);
			}
		}
		if(Date.getDate(current_day, (short) first_year).Day_state() == -1)
			g.drawLine(Calendar_x_start + Day_x, Calendar_y_start + Day_y + Day_height, Calendar_x_start + Day_x + Day_width, Calendar_y_start + Day_y);
	}
	private void draw_propaganda(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect((int) (5.0f/7.0f * this.getWidth()), (int) (Calendar_y_offset * this.getHeight()), (int) ((1.0 - 5.0f/7.0f) * this.getWidth()), (int) ((1 - Calendar_y_offset) * this.getHeight()));
		g.setColor(Color.BLACK);
		g.drawRect((int) (5.0f/7.0f * this.getWidth()), (int) (Calendar_y_offset * this.getHeight()), (int) ((1.0 - 5.0f/7.0f) * this.getWidth()) - 1, (int) ((1 - Calendar_y_offset) * this.getHeight()) + 1);
		g.drawLine(0, this.getHeight() - 2, this.getWidth(), this.getHeight() - 2);
		g.drawImage(image,
				(int) (5.0f/7.0f * this.getWidth())+1, (int) (Calendar_y_offset * this.getHeight())+1,
				this.getWidth()-2, this.getHeight()-2,
				0, 0,
				image.getWidth(this), image.getHeight(this),
				this);
	}
	public void Up() {
		if(editting && !deleting) return;
		if(deleting) {
			selected--;
			if(selected == -1)
				selected = todo.get(edit_day).size()-1;
			return;
		}
		first_day -= 7;
		generate_colors(editting);
	}
	public void Down() {
		if(editting && !deleting) return;
		if(deleting) {
			selected++;
			if(selected == todo.get(edit_day).size())
				selected = 0;
			return;
		}
		first_day += 7;
		generate_colors(editting);
	}
	public void FullCalendar() {
		full_week = true;
	}
	public void WeekdaysCalendar() {
		full_week = false;
	}

	public void clicked() {
		if(banner_clicked())
			clicked_banner();
		else if(calendar_clicked())
			clicked_calendar();
		else if(propaganda_clicked())
			clicked_propaganda();
	}
	private boolean banner_clicked() {
		return java.awt.MouseInfo.getPointerInfo().getLocation().getY() - this.getLocationOnScreen().getY() < Calendar_y_offset * this.getHeight();
	}
	private boolean calendar_clicked() {
		return java.awt.MouseInfo.getPointerInfo().getLocation().getX() - this.getLocationOnScreen().getX() < (full_week?1.0f:(5.0f/7.0f)) * this.getWidth();
	}
	private boolean propaganda_clicked() {
		return true;
	}
	private void clicked_banner() {
		System.out.println("banner");
	}
	private void clicked_calendar() {
		double x = java.awt.MouseInfo.getPointerInfo().getLocation().getX() - this.getLocationOnScreen().getX();
		int i_position = (int) ((x / this.getWidth()) * (editting?1:7));
		double y = java.awt.MouseInfo.getPointerInfo().getLocation().getY() - this.getLocationOnScreen().getY() - Calendar_y_offset * this.getHeight();
		int j_position = (int) ((y / (Calendar_height_usage * this.getHeight())) * Calendar_Height_Divisions);
		edit_day = (short) (first_day+j_position*7+i_position);
		editting = true;
		edit_i = (short) (i_position);
		edit_j = (short) (j_position*7);
		select_day();
	}
	private void clicked_propaganda() {
		newImage();
	}
	private void select_day() {
		saved_day = first_day;
		first_day = edit_day;
		Calendar_Height_Divisions = 1;
		generate_colors(true);
	}
	private void unselect_day() {
		first_day = saved_day;
		generate_colors(false);
	}
	
	public void Cancel() {
		editting = false;
		deleting = false;
		adding = false;
		Calendar_Height_Divisions = 5;
		unselect_day();
	}
	public void add_day() {
		if(!editting || deleting || adding) return;
		adding = true;
		new_event = "+";
	}
	public void delete_day() {
		if(!editting || deleting || adding) return;
		deleting = true;
		selected = 0;
	}
	public void confirm() {
		if(editting && deleting) {
			ArrayList<ToDo> to_remove_from = todo.get(edit_day);
			to_remove_from.remove(selected);
			todo.put(edit_day, to_remove_from);
			deleting = false;
		}
		if(editting && adding) {
			ArrayList<ToDo> to_remove_from = todo.getOrDefault(edit_day, new ArrayList<>());
			to_remove_from.add(new ToDo(new_event.substring(1), 12000, edit_day));
			todo.put(edit_day, to_remove_from);
			adding = false;
		}
	}
	public void typed(char to_add) {
		new_event += to_add;
	}
	public void backspace() {
		if(new_event.length() > 2)
			new_event = new_event.substring(0, new_event.length()-2);
	}
	public void close() {
		System.out.println("closing content");
		Files.SaveToDo(todo);
	}
	public void newImage() {
		File images = new File("res/img/");
		int size = images.listFiles().length - 1;
		int new_image = ((int) Math.floor(size * Math.random()));
		new_image = current_image;
		current_image = new_image<current_image?new_image:(new_image+1);
		current_image = Math.floorMod(current_image, size);
		try {
			image = ImageIO.read(images.listFiles()[current_image]);
		} catch (IOException e) {
			System.err.println("THE HECK DID YOU JUST DO?");
			e.printStackTrace();
		}
	}
}
