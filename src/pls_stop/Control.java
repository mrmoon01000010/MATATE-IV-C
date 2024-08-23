package pls_stop;

import java.awt.Color;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

public class Control {
	public JFrame Window;
	public Drawing Content;
	public MouseBoard Input;
	private long frame_duration;
	private int cursor_on = 30;
	private int cursor_off = 30;
	private double frame_count = 0;
	
	public Control() {
		Content = new Drawing();
		Content.setBackground(new Color(0, 0, 0));
		Input = new MouseBoard();
		Window = new JFrame();
	}
	
	public void run() {
		while(Window.isVisible()) {
			start_frame();
			
			//input
			if(Input.get_key(KeyEvent.VK_UP) == 1)
				Content.Up();
			if(Input.get_key(KeyEvent.VK_DOWN) == 1)
				Content.Down();
			if(Input.get_key(KeyEvent.VK_RIGHT) == 1)
				Content.FullCalendar();
			if(Input.get_key(KeyEvent.VK_LEFT) == 1)
				Content.WeekdaysCalendar();
			if(Input.get_key(KeyEvent.VK_UP) > 20)
				Content.Up();
			if(Input.get_key(KeyEvent.VK_DOWN) > 20)
				Content.Down();
			if(Input.Left == 1) {
				Content.clicked();
			}
			if(Input.get_key(KeyEvent.VK_ESCAPE) == 1) {
				Content.Cancel();
			}
			if(Input.typed()) {
				Content.typed(Input.get_char());
			}
			if(Input.get_key(KeyEvent.VK_PLUS) == 1) {
				Content.add_day();
			}
			if(Input.get_key(KeyEvent.VK_MINUS) == 1) {
				Content.delete_day();
			}
			if(Input.get_key(KeyEvent.VK_ENTER) == 1) {
				Content.confirm();
			}
			if(Input.get_key(KeyEvent.VK_BACK_SPACE) == 1) {
				Content.backspace();
			}
			//update
			Content.repaint();
			Input.update();
			//reload
			
			end_frame(60.0f);
		}
		close();
	}
	private void start_frame() {
		frame_duration = System.nanoTime();
		if(frame_count%(cursor_on+cursor_off)>cursor_on) Content.cursor = false;
		else Content.cursor = true;
	}
	private boolean end_frame(float frame_rate) {
		frame_count++;
		frame_duration = System.nanoTime() - frame_duration;
		long ideal_frame_duration = (long) (1000000000/frame_rate);
		if(frame_duration < ideal_frame_duration) {
			long frame_extra = ideal_frame_duration - frame_duration;
			try {
//				System.out.printf("frame took %1$d nanoseconds\n", frame_duration);
				Thread.sleep(frame_extra/1000000, (int) frame_extra%1000000);
			} catch (InterruptedException e) {
				System.err.println("failed to limit frame rate, at:");
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	private void close() {
		Window.dispose();
		Content.close();
	}
}
