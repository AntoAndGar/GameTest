package com.mygdx.game.randomgames;

import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

/**
 * Will be the class that controls the player's input and controls on the screen.
 * @author Anto
 *
 */
public class PlayerController implements InputProcessor {

	public static final String TAG = PlayerController.class.getSimpleName();
	
	enum Keys {
		LEFT, RIGHT, UP, DOWN, QUIT
	}
	
	enum Mouse {
		SELECT, DOACTION
	}
	
	private static Map<Keys, Boolean> keys = new HashMap
			<PlayerController.Keys, Boolean>();
	private static Map<Mouse, Boolean> mouseButtons = new HashMap
			<PlayerController.Mouse, Boolean>();
	private Vector3 lastMouseCoordinates;
	
	//initialize the hashmap for inputs
	static {
		keys.put(Keys.LEFT, false);
		keys.put(Keys.RIGHT, false);
		keys.put(Keys.UP, false);
		keys.put(Keys.DOWN, false);
		keys.put(Keys.QUIT, false);
	}
	
	static {
		mouseButtons.put(Mouse.SELECT, false);
		mouseButtons.put(Mouse.DOACTION, false);
	}
	
	private Entity _player;
	
	public PlayerController(Entity _player) {
		this.lastMouseCoordinates = new Vector3();
		this._player = _player;
	}

	/**
	 * The keyDown() and keyUp() pair of methods will process specific key presses and
	 * releases, respectively, by caching them in a Hashtable object. This allows us to
	 * process the input later, without losing keyboard key press or release events and
	 * appropriately removing redundant key events from the queue
	 */
	@Override
	public boolean keyDown(int keycode) {
		if( keycode == Input.Keys.LEFT || keycode == Input.Keys.A){
			this.leftPressed();
		}
		if( keycode == Input.Keys.RIGHT || keycode == Input.Keys.D){
			this.rightPressed();
		}
		if( keycode == Input.Keys.UP || keycode == Input.Keys.W){
			this.upPressed();
		}
		if( keycode == Input.Keys.DOWN || keycode == Input.Keys.S){
			this.downPressed();
		}
		if( keycode == Input.Keys.ESCAPE){
			this.quitPressed();
		}
		return true;
	}

	/**
	 * The keyDown() and keyUp() pair of methods will process specific key presses and
	 * releases, respectively, by caching them in a Hashtable object. This allows us to
	 * process the input later, without losing keyboard key press or release events and
	 * appropriately removing redundant key events from the queue
	 */
	@Override
	public boolean keyUp(int keycode) {
		if( keycode == Input.Keys.LEFT || keycode == Input.Keys.A){
			this.leftReleased();
		}
		if( keycode == Input.Keys.RIGHT || keycode == Input.Keys.D){
			this.rightReleased();
		}
		if( keycode == Input.Keys.UP || keycode == Input.Keys.W ){
			this.upReleased();
		}
		if( keycode == Input.Keys.DOWN || keycode == Input.Keys.S){
			this.downReleased();
		}
		if( keycode == Input.Keys.ESCAPE){
			this.quitReleased();
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * The touchDown() and touchUp() pair of methods will process specific mouse button
	 * presses and releases, respectively, by caching the position in a Hashtable object. This
	 * allows us to process the input later, without losing mouse button press or release
	 * events and appropriately removing redundant mouse press events from the queue
	 */
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if( button == Input.Buttons.LEFT || button == Input.Buttons.RIGHT ) {
			this.setClickedMouseCoordinates(screenX, screenY);
		}
		//left is selection, right is context menu
		if( button == Input.Buttons.LEFT){
			this.selectMouseButtonPressed(screenX, screenY);
		}
		if( button == Input.Buttons.RIGHT){
			this.doActionMouseButtonPressed(screenX, screenY);
		}
		return true;
	}

	/*
	 * The touchDown() and touchUp() pair of methods will process specific mouse button
	 * presses and releases, respectively, by caching the position in a Hashtable object. This
	 * allows us to process the input later, without losing mouse button press or release
	 * events and appropriately removing redundant mouse press events from the queue
	 */
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		//left is selection, right is context menu
		if( button == Input.Buttons.LEFT){
			this.selectMouseButtonReleased(screenX, screenY);
		}
		if( button == Input.Buttons.RIGHT){
			this.doActionMouseButtonReleased(screenX, screenY);
		}
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	public void dispose() {
		// TODO Auto-generated method stub
	}
	
	//Key presses
	public void leftPressed(){
		keys.put(Keys.LEFT, true);
	}
	
	public void rightPressed(){
		keys.put(Keys.RIGHT, true);
	}
	
	public void upPressed(){
		keys.put(Keys.UP, true);
	}
	
	public void downPressed(){
		keys.put(Keys.DOWN, true);
	}
	
	public void quitPressed(){
		keys.put(Keys.QUIT, true);
	}
	
	public void setClickedMouseCoordinates(int x, int y){
		lastMouseCoordinates.set(x, y, 0);
	}
	
	public void selectMouseButtonPressed(int x, int y){
		mouseButtons.put(Mouse.SELECT, true);
	}
	
	public void doActionMouseButtonPressed(int x, int y){
		mouseButtons.put(Mouse.DOACTION, true);
	}
	
	//Releases
	public void leftReleased(){
		keys.put(Keys.LEFT, false);
	}
	
	public void rightReleased(){
		keys.put(Keys.RIGHT, false);
	}
	
	public void upReleased(){
		keys.put(Keys.UP, false);
	}
	
	public void downReleased(){
		keys.put(Keys.DOWN, false);
	}
	
	public void quitReleased(){
		keys.put(Keys.QUIT, false);
	}
	
	public void selectMouseButtonReleased(int x, int y){
		mouseButtons.put(Mouse.SELECT, false);
	}
	
	public void doActionMouseButtonReleased(int x, int y){
		mouseButtons.put(Mouse.DOACTION, false);
	}
	
	public void update(float delta){
		processInput(delta);
	}
	
	public static void hide(){
		keys.put(Keys.LEFT, false);
		keys.put(Keys.RIGHT, false);
		keys.put(Keys.UP, false);
		keys.put(Keys.DOWN, false);
		keys.put(Keys.QUIT, false);
	}
	
	/**
	 * This method is the primary business logic that drives this class.
	 * During the beginning of every frame in the render loop, processInput() will
	 * be called before rendering any graphics. This will be where the cached values of
	 * the keyboard and mouse input will be processed. We will first calculate the next
	 * position, as explained in the previous section, in order to avoid issues with two
	 * fast-moving game objects colliding and missing a collision check. Then, we set
	 * the state and direction of the player character during this time
	 * @param delta
	 */
	private void processInput(float delta) {
		// Keyboard input
		if (keys.get(Keys.LEFT)) {
			_player.calculateNextPosition(Entity.Direction.LEFT, delta);
			_player.setState(Entity.State.WALKING);
			_player.setDirection(Entity.Direction.LEFT, delta);
		} else if (keys.get(Keys.RIGHT)) {
			_player.calculateNextPosition(Entity.Direction.RIGHT, delta);
			_player.setState(Entity.State.WALKING);
			_player.setDirection(Entity.Direction.RIGHT, delta);
		} else if (keys.get(Keys.UP)) {
			_player.calculateNextPosition(Entity.Direction.UP, delta);
			_player.setState(Entity.State.WALKING);
			_player.setDirection(Entity.Direction.UP, delta);
		} else if (keys.get(Keys.DOWN)) {
			_player.calculateNextPosition(Entity.Direction.DOWN, delta);
			_player.setState(Entity.State.WALKING);
			_player.setDirection(Entity.Direction.DOWN, delta);
		} else if (keys.get(Keys.QUIT)) {
			Gdx.app.exit();
		} else {
			_player.setState(Entity.State.IDLE);
		}
		
		// Mouse input
		if (mouseButtons.get(Mouse.SELECT)) {
			mouseButtons.put(Mouse.SELECT, false);
		}
	}

}
