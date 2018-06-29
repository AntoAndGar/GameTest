package com.mygdx.game.randomgames.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.mygdx.game.randomgames.customexceptions.DragonGeneratorException;

/**
 * Created by Anto on 21/04/2018.
 */

public class DragonScreen extends Game implements Screen{
	private float[] dragonCurve;
	// Any more than 10 and we'll need to break up the polyline into multiple lines
	private static final int RECURSIONS = 10;

	private ShapeRenderer shapeRenderer;

	@Override
	public void create () {
		try {
			dragonCurve = DragonCurveGenerator.generateDragonCurve(
					Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), RECURSIONS);
			shapeRenderer = new ShapeRenderer();
		} catch (DragonGeneratorException e) {
			e.printStackTrace();
		}
	}
//
//	@Override
//	public void render () {
//		Gdx.gl.glClearColor(0, 0, 0, 1);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		shapeRenderer.begin(ShapeType.Line);
//		shapeRenderer.polyline(dragonCurve);
//		shapeRenderer.end();
//	}

	@Override
	public void show() {
		try {
			dragonCurve = DragonCurveGenerator.generateDragonCurve(
					Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), RECURSIONS);
			shapeRenderer = new ShapeRenderer();
		} catch (DragonGeneratorException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.polyline(dragonCurve);
		shapeRenderer.end();
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

}
