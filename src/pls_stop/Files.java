package pls_stop;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class Files {
	public static HashMap<Short, ArrayList<ToDo>> readSavedToDo() {
		BufferedReader file;
		int current_line = 0;
		HashMap<Short, ArrayList<ToDo>> product = new HashMap<>();
		
		try {
			file = new BufferedReader(new FileReader("res/todo.txt"));
			String line;
			while((line = file.readLine())!=null) {
				current_line++;
				ToDo tmp = read_todo_line(line, current_line);
				if(!product.containsKey(tmp.getDueDay()))
					product.put(tmp.getDueDay(), new ArrayList<>());
				ArrayList<ToDo> current_todo = product.get(tmp.getDueDay());
				current_todo.add(tmp);
				product.put(tmp.getDueDay(), current_todo);
			}
			
			file.close();
		} catch(Exception e) {
			System.err.println("could not read file!");
			e.printStackTrace();
			System.exit(1);
		}
		
		return product;
	}
	private static ToDo read_todo_line(String line, int current_line) {
		String[] split_lines;
		split_lines = line.split(":");
		short day;
		long time;
		String name;
		
		try {
			day = Short.parseShort(split_lines[0]);
			time = Long.parseLong(split_lines[1]);
			name = split_lines[2];
		} catch(Exception e) {
			System.err.printf("line %1d does not adhere to format \"DAY_OF_YEAR:TIME_IN_MINUTES:NAME_OF_PROYECT\"; ignoring.", current_line);
			e.printStackTrace();
			return new ToDo("fail", (long) 0, (short) 0);
		}
		
		return new ToDo(name, time, day);
	}
	public static boolean SaveToDo(HashMap<Short, ArrayList<ToDo>> todo) {
		BufferedWriter file;
		
		try {
			file = new BufferedWriter(new FileWriter("res/todo.txt"));
			
			for(Entry<Short, ArrayList<ToDo>> entry : todo.entrySet()) {
				ArrayList<ToDo> day_todo = entry.getValue();
				for(ToDo actual_todo : day_todo) {
					file.write(actual_todo.toString());
					file.newLine();
				}
			}
			
			file.close();
		} catch (IOException e) {
			System.err.println("could not write file!");
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	public static String read_file(String fileDir) {
		StringBuilder text = new StringBuilder();
		BufferedReader file;
		
		try {
			file = new BufferedReader(new FileReader(fileDir));
			String line;
			while((line = file.readLine())!=null) {
				text.append(line).append("\n");
			}
			
			file.close();
		} catch(Exception e) {
			System.err.println("could not read file!");
			e.printStackTrace();
			System.exit(1);
		}
		
		return text.toString();
	}
}
