package engineer;

import java.io.IOException;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.*;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseMotionListener;

public class Main implements NativeKeyListener, NativeMouseMotionListener {
	
	private Integer keyboardStrokesLogged = 0;
	private Integer mouseMovesLogged = 0;
	private Integer lastFileWrite = 0;
	
	private Path file = Paths.get("activity.txt");

	public static void main(String[] args) {

		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException e) {
			//
		}
		
		Main ourMain = new Main();
		
		GlobalScreen.addNativeKeyListener(ourMain);
		GlobalScreen.addNativeMouseMotionListener(ourMain);
	}

	public void nativeKeyPressed(NativeKeyEvent e) {
		keyboardStrokesLogged++;
		
		checkIfFileWriteDue();
	}
	
	public void nativeMouseMoved(NativeMouseEvent e) {
		mouseMovesLogged++;
		
		checkIfFileWriteDue();
	}
	
	private void checkIfFileWriteDue() {
		
		Integer unixTime = (int) (System.currentTimeMillis() / 1000);
		
		if (lastFileWrite + 60*5 < unixTime) { // Last file write was 5 minutes ago.
			
			// Record the current time as lastFileWrite.
			lastFileWrite = unixTime;
			
			// Write the current logged data to file and reset the counters.
			writeResultsToFileAndReset();
		}
	}
	
	private void writeResultsToFileAndReset() {
		try (OutputStream os = Files.newOutputStream(file, StandardOpenOption.CREATE, StandardOpenOption.WRITE,
				StandardOpenOption.APPEND); PrintWriter writer = new PrintWriter(os)) {
			
			
				String pattern = "dd-MM-yyyy HH:mm:ss";
	
				// Create an instance of SimpleDateFormat used for formatting 
				// the string representation of date according to the chosen pattern
				DateFormat df = new SimpleDateFormat(pattern);
	
				// Get the today date using Calendar object.
				java.util.Date today = Calendar.getInstance().getTime();        
				// Using DateFormat format method we can create a string 
				// representation of a date with the defined format.
				String todayAsString = df.format(today);
			
			
			
				writer.println("[" + todayAsString + "] " + keyboardStrokesLogged + " keystrokes, " + mouseMovesLogged + " mousemoves.");
				
				keyboardStrokesLogged = 0;
				mouseMovesLogged = 0;
				
		} catch (IOException ex) {
			//
		}
	}
}
