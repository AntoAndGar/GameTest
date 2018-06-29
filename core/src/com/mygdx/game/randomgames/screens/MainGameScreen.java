package com.mygdx.game.randomgames.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.randomgames.Entity;
import com.mygdx.game.randomgames.MapManager;
import com.mygdx.game.randomgames.PlayerController;
import com.mygdx.game.randomgames.customexceptions.AnimationException;

/**
 * Will be the primary gameplay screen that displays 
 * the different maps and player character moving around in them.
 * Will also create MapManager, Entity, and PlayerController.
 * @author Anto
 *
 */
public class MainGameScreen extends GameScreen implements Screen
{
	private static final String TAG = MainGameScreen.class.getSimpleName();

	private PlayerController _controller;
	private TextureRegion _currentPlayerFrame;
	private Sprite _currentPlayerSprite;
	
	private OrthogonalTiledMapRenderer _mapRenderer = null;
	private OrthographicCamera _camera = null;
	private static MapManager _mapMgr;
	
	private static Entity _player;
	
	private static class VIEWPORT {
		static float viewportWidth;
		static float viewportHeight;
		static float virtualWidth;
		static float virtualHeight;
		static float physicalWidth;
		static float physicalHeight;
		static float aspectRatio;
	}
	
	
	public MainGameScreen(){
		MainGameScreen._mapMgr = new MapManager();
	}
	
	/**
	 * show the new screen
	 */
	@Override
	public void show() {
		//camera setup
		setupViewport(10, 10);
		
		//get the current size
		_camera = new OrthographicCamera();
		//false = positive y facing up
		_camera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);
		
		_mapRenderer = new OrthogonalTiledMapRenderer(_mapMgr.getCurrentMap(), MapManager.UNIT_SCALE);
		_mapRenderer.setView(_camera);
		
		Gdx.app.debug(TAG, "UnitScale value is: " + _mapRenderer.getUnitScale());
		
		try {
			_player = new Entity();
			_player.init(_mapMgr.getPlayerStartUnitScaled().x, _mapMgr.getPlayerStartUnitScaled().y);
			
			_currentPlayerSprite =_player.getFrameSprite();
			
			_controller = new PlayerController(_player);
			Gdx.input.setInputProcessor(_controller);
			
		} catch (AnimationException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Will be called every frame, and is the primary location for
	 * rendering, updating, and checking for collisions in the game lifecycle. First, we will
	 * make sure that we lock the viewport (camera position) to the current position of
	 * our player character so that the player is always in the middle of the screen. We will
	 * then check whether the player has activated a portal.
	 * We will also check for collisions with
	 * the collision layer of the map, and if there are collisions, then we will not update
	 * the player's position. We make sure that we update the camera information in the
	 * OrthogonalTiledMapRenderer object and then render the TiledMap object first
	 * because, as mentioned previously, the order in which you draw your objects matter.
	 * Finally, we will draw the character to the screen, making sure to use the getBatch()
	 * call for when we have numerous objects to update. By drawing in a batch update, the
	 * overhead of updating the textures will be minimal since the GPU will consume the
	 * texture updates at one time instead of constantly throttling between updating and
	 * rendering separate textures.
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//Preferable to lock and center the _camera to the player's position
		_camera.position.set(_currentPlayerSprite.getX(), _currentPlayerSprite.getY(), 0f);
		_camera.update();
		
		_player.update(delta);
		_currentPlayerFrame = _player.getFrame();
		
		updatePortalLayerActivation(Entity.boundingBox);
		
		if( !isCollisionWithMapLayer(Entity.boundingBox) ) 
			_player.setNextPositionToCurrent();
		_controller.update(delta);
		
		_mapRenderer.setView(_camera);
		_mapRenderer.render();
		
		_mapRenderer.getBatch().begin();
		_mapRenderer.getBatch().draw(_currentPlayerFrame, _currentPlayerSprite.getX(), _currentPlayerSprite.getY(), 1, 1);
		_mapRenderer.getBatch().end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * hide the actual screen
	 */
	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void dispose() 
	{
		_player.dispose();
		_controller.dispose();
		Gdx.input.setInputProcessor(null);
		_mapRenderer.dispose();
	}
	
	/**
	 * Helps with the bookkeeping of our inner class VIEWPORT. 
	 * This is simply a convenience class for maintaining all the parameters
	 * that compose our viewport for the camera. This class will also account for the
	 * skewing that can occur depending on the width to height ratio and will update
	 * the values accordingly
	 * @param width
	 * @param height
	 */
	private void setupViewport(int width, int height) {
		//Make the viewport a percentage of the total display area
		VIEWPORT.virtualWidth = width;
		VIEWPORT.virtualHeight = height;
		
		//Current viewport dimensions
		VIEWPORT.viewportWidth = VIEWPORT.virtualWidth;
		VIEWPORT.viewportHeight = VIEWPORT.virtualHeight;
		
		//pixel dimensions of display
		VIEWPORT.physicalWidth = Gdx.graphics.getWidth();
		VIEWPORT.physicalHeight = Gdx.graphics.getHeight();
		
		//aspect ratio for current viewport
		VIEWPORT.aspectRatio = (VIEWPORT.virtualWidth / VIEWPORT.viewportHeight);
		
		//update viewport if there could be skewing
		if( VIEWPORT.physicalWidth / VIEWPORT.physicalHeight >= VIEWPORT.aspectRatio ) {
			//Letterbox left and right
			VIEWPORT.viewportWidth = VIEWPORT.viewportHeight * (VIEWPORT.physicalWidth/VIEWPORT.physicalHeight);
			VIEWPORT.viewportHeight = VIEWPORT.virtualHeight;
		} 
		else {
		//letterbox above and below
			VIEWPORT.viewportWidth = VIEWPORT.virtualWidth;
			VIEWPORT.viewportHeight = VIEWPORT.viewportWidth * (VIEWPORT.physicalHeight/VIEWPORT.physicalWidth);
		}
		
		Gdx.app.debug(TAG, "WorldRenderer: virtual: (" + VIEWPORT.virtualWidth + "," + VIEWPORT.virtualHeight + ")" );
		Gdx.app.debug(TAG, "WorldRenderer: viewport: (" + VIEWPORT.viewportWidth + "," + VIEWPORT.viewportHeight + ")" );
		Gdx.app.debug(TAG, "WorldRenderer: physical: (" + VIEWPORT.physicalWidth + "," + VIEWPORT.physicalHeight + ")" );
	}
	
	/**
	 * This method is called for every frame in the render()
	 * method with the player character's bounding box passed in. This is essentially the
	 * rectangle that defines the hitbox of the player. We test the player's hitbox against
	 * all rectangle objects on the collision layer of the TiledMap map, and if any of the
	 * rectangles overlap, then we know we have a collision and will return true
	 * @param boundingBox
	 * @return bool
	 */
	private boolean isCollisionWithMapLayer(Rectangle boundingBox) {
		MapLayer mapCollisionLayer = _mapMgr.getCollisionLayer();
		if( mapCollisionLayer == null ){
			return false;
		}
		
		Rectangle rectangle = null;
		
		for( MapObject object : mapCollisionLayer.getObjects() ) {
			if(object instanceof RectangleMapObject) {
				rectangle = ((RectangleMapObject)object).getRectangle();
				if( boundingBox.overlaps(rectangle) ){
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * The method is similar to the isCollisionWithMapLayer(),
	 *  in that we will walk through every rectangle on the
	 *  portal layer checking for collisions with the player's hitbox. The primary difference
	 *  is that if a player walks over these special areas on the map, then an event will be
	 *  triggered letting us know that the player has activated the portal. When portal
	 *  activation occurs, we will first cache the closest player spawn in the MapManager
	 *  class. This will help when we transition out from the new location, back to the current
	 *  location. Then, we will load the new map designated by the portal activation name,
	 *  reset the player position, and set the new map to be rendered in the next frame.
	 * @param boundingBox
	 * @return
	 */
	private boolean updatePortalLayerActivation( Rectangle boundingBox) {
		MapLayer mapPortalLayer = _mapMgr.getPortalLayer();
		
		if( mapPortalLayer == null ) {
			return false;
		}
		
		Rectangle rectangle = null;
		
		for( MapObject object : mapPortalLayer.getObjects() ) {
			if(object instanceof RectangleMapObject ) {
				rectangle = ((RectangleMapObject)object).getRectangle();
				if( boundingBox.overlaps(rectangle) ) {
					String mapName = object.getName();
					if( mapName == null ) {
						return false;
					}
					_mapMgr.setClosestStartPositionFromScaledUnits(_player.getCurrentPosition());
					_mapMgr.loadMap(mapName);
					_player.init(_mapMgr.getPlayerStartUnitScaled().x,
							_mapMgr.getPlayerStartUnitScaled().y);
					_mapRenderer.setMap(_mapMgr.getCurrentMap());
					Gdx.app.debug(TAG, "Portal Activated");
					return true;
				}
			}
		}
		return false;
	}
	
	
	
}
