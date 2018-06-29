package com.mygdx.game.randomgames;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.mygdx.game.randomgames.screens.CircularMotion;
import com.mygdx.game.randomgames.screens.LogoScreen;
import com.mygdx.game.randomgames.screens.MainGameScreen;
import com.mygdx.game.randomgames.screens.ScreenSwitcher;

/**
 * Will maintain all the screens for the game.
 * @author Anto
 *
 */
public class RandomGame extends Game//extends ApplicationAdapter
{
	public static final String TAG = RandomGame.class.getName();
	
	LogoScreen logoScreen;
	CircularMotion circularMotion;
	
	/**
	 * The primary gameplay screen that the player will see as 
	 * they move their hero around in the game world.
	 */
	public static final MainGameScreen _mainGameScreen = new MainGameScreen();
	
		@Override
		public void create()
		{
			Gdx.app.log(TAG, "method create called");
			circularMotion = new CircularMotion();
			logoScreen = new LogoScreen();
			this.setScreen(circularMotion);
			
			Gdx.input.setInputProcessor(new ScreenSwitcher(this, circularMotion, logoScreen));
		}

}
