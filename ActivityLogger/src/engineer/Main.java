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

public class Main implements NativeKeyListener {
	
	private Integer keyboardStrokesLogged = 0;
	private Integer mouseMovesLogged = 0;
	
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
		
	}
	
	private void writeResultsToFileAndReset() {		
		try (OutputStream os = Files.newOutputStream(file, StandardOpenOption.CREATE, StandardOpenOption.WRITE,
				StandardOpenOption.APPEND); PrintWriter writer = new PrintWriter(os)) {
			
				writer.print("[" + java.time.LocalDate.now() + "]");
			
		} catch (IOException ex) {
			//
		}
	}
}
