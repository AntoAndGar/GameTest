package com.mygdx.game.randomgames.screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class CircularMotion extends ApplicationAdapter implements Screen {
    public static final String TAG = CircularMotion.class.getName();
    private static final float WORLD_SIZE = 480.0f;
    private static final float CIRCLE_RADIUS = WORLD_SIZE / 20;
    private static final float MOVEMENT_RADIUS = WORLD_SIZE / 4;

    // How many seconds until the circular motion repeats
    private static final float PERIOD = 1.0f;

    private ShapeRenderer renderer;
    private FitViewport viewport;

    // We set up a variable to hold the nanoTime at which the application was created.
    private long initialTime;

//    @Override
//    public void create() {
//        renderer = new ShapeRenderer();
//        viewport = new FitViewport(WORLD_SIZE, WORLD_SIZE);
//
//        // Set the initialTime
//        initialTime = TimeUtils.nanoTime();
//    }

    @Override
    public void dispose() {
    	Gdx.app.log(TAG, "method dispose called");
        renderer.dispose();
    }

    @Override
    public void resize(int width, int height) {
    	Gdx.app.log(TAG, "method resize called");
        viewport.update(width, height, true);
    }

//    @Override
//    public void render() {
//    	Gdx.app.log(TAG, "method render() called");
//        viewport.apply();
//
//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        renderer.setProjectionMatrix(viewport.getCamera().combined);
//        renderer.begin(ShapeType.Filled);
//
//        float elapsedNanoseconds = TimeUtils.nanoTime() - initialTime;
//        float elapsedSeconds = MathUtils.nanoToSec * elapsedNanoseconds;
//        float elapsedPeriods = elapsedSeconds / PERIOD;
//        float cyclePosition = elapsedPeriods % 1;
//
//        float x = WORLD_SIZE / 2 + MOVEMENT_RADIUS * MathUtils.cos(MathUtils.PI2 * cyclePosition);
//        float y = WORLD_SIZE / 2 + MOVEMENT_RADIUS * MathUtils.sin(MathUtils.PI2 * cyclePosition);
//
////        renderer.circle(x, y, CIRCLE_RADIUS);
//
//        drawFancyCircles(renderer, elapsedPeriods, 20);
//        renderer.end();
//    }

    private void drawFancyCircles(ShapeRenderer renderer, float elapsedPeriods, int circleCount) {
        for (int i = 1; i <= circleCount; i++) {
            float centerX = WORLD_SIZE / 2 + WORLD_SIZE / 4 * MathUtils.cos(MathUtils.PI2 * i / circleCount);
            float centerY = WORLD_SIZE / 2 + WORLD_SIZE / 4 * MathUtils.sin(MathUtils.PI2 * i / circleCount);

            float x = centerX + WORLD_SIZE / 5 * MathUtils.cos(MathUtils.PI2 * (elapsedPeriods * i / circleCount));
            float y = centerY + WORLD_SIZE / 5 * MathUtils.sin(MathUtils.PI2 * (elapsedPeriods * i / circleCount));

            renderer.circle(x, y, 10);
        }
    }

	@Override
	public void show() {
		Gdx.app.log(TAG, "method show called");
		renderer = new ShapeRenderer();
        viewport = new FitViewport(WORLD_SIZE, WORLD_SIZE);

        // Set the initialTime
        initialTime = TimeUtils.nanoTime();
		
	}

	@Override
	public void render(float delta) {
		Gdx.app.log(TAG, "method render(float delta) called");
		viewport.apply();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.setProjectionMatrix(viewport.getCamera().combined);
        renderer.begin(ShapeType.Filled);

        float elapsedNanoseconds = TimeUtils.nanoTime() - initialTime;
        float elapsedSeconds = MathUtils.nanoToSec * elapsedNanoseconds;
        float elapsedPeriods = elapsedSeconds / PERIOD;
        float cyclePosition = elapsedPeriods % 1;

        float x = WORLD_SIZE / 2 + MOVEMENT_RADIUS * MathUtils.cos(MathUtils.PI2 * cyclePosition);
        float y = WORLD_SIZE / 2 + MOVEMENT_RADIUS * MathUtils.sin(MathUtils.PI2 * cyclePosition);

//        renderer.circle(x, y, CIRCLE_RADIUS);

        drawFancyCircles(renderer, elapsedPeriods, 20);
        renderer.end();
		
	}

	@Override
	public void hide() {
		Gdx.app.log(TAG, "method hide called");
		renderer.dispose();
		// TODO Auto-generated method stub
		
	}
}
