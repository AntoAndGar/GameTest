package com.mygdx.game.randomgames;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.randomgames.screens.MainGameScreen;

public class RandomGame extends ApplicationAdapter 
{ // la madonnna è troia
	SpriteBatch batch;
	Texture img;
	
	public static final MainGameScreen _mainGameScreen = new MainGameScreen();
	
	@Override
	public void create() 
	{
//		setScreen(_mainGameScreen);
		batch = new SpriteBatch();
		img = new Texture("Truncated_octahedra.jpg");
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose() 
	{
//		_mainGameScreen.dispose();
		batch.dispose();
		img.dispose();
	}
}
