package com.mygdx.game.randomgames;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.randomgames.screens.LogoScreen;
import com.mygdx.game.randomgames.screens.MainGameScreen;

/**
 * Will maintain all the screens for the game.
 * @author Anto
 *
 */
public class RandomGame extends Game//extends ApplicationAdapter 
{

	SpriteBatch batch;
	Texture logo;
	BitmapFont font;
	
	/**
	 * The primary gameplay screen that the player will see as 
	 * they move their hero around in the game world.
	 */
	public static final MainGameScreen _mainGameScreen = new MainGameScreen();
	
	@Override
	public void create() 
	{
		this.setScreen(new LogoScreen());
	}
//		setScreen(_mainGameScreen);
//		batch = new SpriteBatch();
//		font = new BitmapFont(Gdx.files.internal("verdana39.fnt"),
//		Gdx.files.internal("verdana39.png"), false);
//		font.setColor(Color.BLUE);
//		logo = new Texture("Truncated_octahedra.jpg");
//	}
//
//	@Override
//	public void render () {
//		Gdx.gl.glClearColor(1, 0, 0, 1);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		batch.begin();
//		batch.draw(logo, 0, 0);
//		font.draw(batch, "~~ Shit ~~", 400, 400);
//		batch.end();
//	}
//	
//	@Override
//	public void dispose() 
//	{
//		_mainGameScreen.dispose();
//		batch.dispose();
//		font.dispose();
//		logo.dispose();
//	}
}
