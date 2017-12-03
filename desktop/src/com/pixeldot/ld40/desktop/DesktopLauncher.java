package com.pixeldot.ld40.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.pixeldot.ld40.Metro;

public class DesktopLauncher {

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		// Window size
		config.height = Metro.W_HEIGHT;
		config.width = Metro.W_WIDTH;

		// Prevent resizing of the window
		config.resizable = false;

		config.vSyncEnabled = true;

		// Stop error code 255 when closing
		config.forceExit = false;

		new LwjglApplication(new Metro(), config);
	}
}
