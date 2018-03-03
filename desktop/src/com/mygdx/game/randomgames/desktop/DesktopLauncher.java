package com.mygdx.game.randomgames.desktop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.randomgames.RandomGame;

/**
 * The main entry point for our game (and the only platform-specific class).
 * Will instantiate BludBourne and add it along with some configuration
 * information to the LibGDX application lifecycle.
 * 
 * The Application class is responsible for setting up a window, handling resize 
 * events, rendering to the surfaces, and managing the application during its lifetime.
 * Specifically, Application will provide the modules for dealing with graphics, audio,
 * input and file I/O handling, logging facilities, memory footprint information, and
 * hooks for extension libraries.
 * @author Anto
 *
 */
public class DesktopLauncher {
	public static void main (String[] arg) 
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "RandomGame";
		config.useGL30 = false;
		config.width = 1920;
		config.height = 1080;
		
		Application app = new LwjglApplication(new RandomGame(), config);
		
		Gdx.app = app;
		//Gdx.app.setLogLevel(Application.LOG_INFO);
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		//Gdx.app.setLogLevel(Application.LOG_ERROR);
		//Gdx.app.setLogLevel(Application.LOG_NONE);
	}
}
