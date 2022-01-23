package engineer;

import java.io.IOException;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.*;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;

public class Main implements NativeKeyListener {
	
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
		
		GlobalScreen.addNativeKeyListener(new Main());
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
		
		if (lastFileWrite + 1 < unixTime) { // Last file write was 5 minutes ago.
			
			// Record the current time as lastFileWrite.
			lastFileWrite = unixTime;
			
			// Write the current logged data to file and reset the counters.
			writeResultsToFileAndReset();
		}
	}
	
	private void writeResultsToFileAndReset() {		
		try (OutputStream os = Files.newOutputStream(file, StandardOpenOption.CREATE, StandardOpenOption.WRITE,
				StandardOpenOption.APPEND); PrintWriter writer = new PrintWriter(os)) {
			
				writer.println("[" + java.time.LocalDate.now() + "] " + keyboardStrokesLogged + " keystrokes, " + mouseMovesLogged + " mousemoves.");
				
				keyboardStrokesLogged = 0;
				mouseMovesLogged = 0;
				
		} catch (IOException ex) {
			//
		}
	}
}
