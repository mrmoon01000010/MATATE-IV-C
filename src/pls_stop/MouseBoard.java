package pls_stop;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MouseBoard implements MouseListener, KeyListener, MouseWheelListener {
	int Left = 0;
	int Middle = 0;
	int Right = 0;
	private HashMap<Integer, Integer> pressed_keys = new HashMap<>();
	private HashSet<Integer> clicked_keys = new HashSet<>();
	double Ratio = 1;
	char character = '\0';
	boolean typed = false;
	
	public void keyPressed(KeyEvent e) {
		if(!pressed_keys.containsKey(e.getExtendedKeyCode()))
			pressed_keys.put(e.getExtendedKeyCode(), 1);
	}
	public void keyReleased(KeyEvent e) {
		pressed_keys.remove(e.getExtendedKeyCode());
		clicked_keys.add(e.getExtendedKeyCode());
	}
	public void keyTyped(KeyEvent e) {
		character = e.getKeyChar();
		typed = true;
	}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {
		switch(e.getButton()) {
		case 1:
			Left = 1;
			break;
		case 2:
			Middle = 1;
			break;
		case 3:
			Right = 1;
			break;
		}
	}
	public void mouseReleased(MouseEvent e) {
		switch(e.getButton()) {
		case 1:
			Left = 0;
			break;
		case 2:
			Middle = 0;
			break;
		case 3:
			Right = 0;
			break;
		}
	}
	public void mouseWheelMoved(MouseWheelEvent e) {
		Ratio = Math.pow(2.0, -e.getPreciseWheelRotation());
	}

	public int get_key(int ExtendedKeyCode) {
		 return pressed_keys.getOrDefault(ExtendedKeyCode, 0);
	}
	public boolean get_last_key(int ExtendedKeyCode) {
		 return clicked_keys.contains(ExtendedKeyCode);
	}
	public void update() {
		for(Entry<Integer, Integer> entry : pressed_keys.entrySet()) {
			pressed_keys.put(entry.getKey(), entry.getValue()+1);
		}
		typed = false;
		clicked_keys.clear();
		if(Left > 0) Left++;
		if(Middle > 0) Middle++;
		if(Right > 0) Right++;
	}
	public boolean typed() {
		return typed;
	}
	public char get_char() {
		return character;
	}
}
 