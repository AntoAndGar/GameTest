package com.mygdx.game.randomgames;

import java.util.Hashtable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

/**
 * Provides helper methods for managing the different maps and map layers.
 * This class has helper methods for loading the TiledMap maps, as well as methods
 * for accessing the different MapLayer, and MapObject objects in the layers.
 * @author Anto
 *
 */
public class MapManager {
	private static final String TAG = MapManager.class.getSimpleName();
	
	//All maps for the game
	private Hashtable<String, String> _mapTable;
	private Hashtable<String, Vector2> _playerStartLocationTable;
	
	//maps
	private static final String TOP_WORLD = "TOP_WORLD";
	private static final String TOWN = "TOWN";
	private static final String CASTLE_OF_DOOM = "CASTLE_OF_DOOM";
	
	//Map layers
	private static final String MAP_COLLISION_LAYER = "MAP_COLLISION_LAYER";
	private static final String MAP_SPAWN_LAYER = "MAP_SPAWN_LAYER";
	private static final String MAP_PORTAL_LAYER = "MAP_PORTAL_LAYER";
	
	private static final String PLAYER_START = "PLAYER_START";
	
	private Vector2 _playerStartPositionRect;
	private Vector2 _closestPlayerStartPosition;
	private Vector2 _convertedUnits;
	
	private Vector2 _playerStart;
	private TiledMap _currentMap = null;
	private String _currentMapName;
	private MapLayer _collisionLayer = null;
	private MapLayer _portalLayer = null;
	private MapLayer _spawnsLayer = null;

	public static final float UNIT_SCALE = 1/16f;
	
	public MapManager() {
		_playerStart = new Vector2(0,0);
		_mapTable = new Hashtable<String, String>();
		
		_mapTable.put(TOP_WORLD, "maps/topworld.tmx");
		_mapTable.put(TOWN, "maps/town.tmx");
		_mapTable.put(CASTLE_OF_DOOM, "maps/castle_of_doom.tmx");
		
		_playerStartLocationTable = new Hashtable<String, Vector2>();
		_playerStartLocationTable.put(TOP_WORLD, _playerStart.cpy());
		_playerStartLocationTable.put(TOWN, _playerStart.cpy());
		_playerStartLocationTable.put(CASTLE_OF_DOOM, _playerStart.cpy());
		
		_playerStartPositionRect = new Vector2(0,0);
		_closestPlayerStartPosition = new Vector2(0,0);
		_convertedUnits = new Vector2(0,0);
	}
	
	/**
	 * This method is a straightforward helper method that verifies that the
	 * string passed in is a valid path and checks to see whether the asset exists; if it does,
	 * it loads it. At this point, we copy the object references of the different layers for fast
	 * access later, such as the collision layer, portal layer, and spawn layer. One item of
	 * note is near the end, we check to see whether the starting location is set to (0, 0). If it
	 * is, we know that we have not cached a player location yet, meaning that this is the
	 * first time we have loaded this map. At this point, we will cache a location closest to
	 * this starting position
	 * @param mapName
	 */
	public void loadMap(String mapName) {
		_playerStart.set(0,0);
		
		String mapFullPath = _mapTable.get(mapName);
		
		if( mapFullPath == null || mapFullPath.isEmpty() ) {
			Gdx.app.debug(TAG, "Map is invalid");
			return;
		}
		
		if( _currentMap != null ) {
			_currentMap.dispose();
		}
		
		Utility.loadMapAsset(mapFullPath);
		if( Utility.isAssetLoaded(mapFullPath) ){
			_currentMap = Utility.getMapAsset(mapFullPath);
			_currentMapName = mapName;
		} else {
			Gdx.app.debug(TAG, "Map not loaded");
			return;
		}
		
		_collisionLayer = _currentMap.getLayers().get(MAP_COLLISION_LAYER);
		if( _collisionLayer == null ) {
			Gdx.app.debug(TAG, "No collision Layer");
		}
		
		_portalLayer = _currentMap.getLayers().get(MAP_PORTAL_LAYER);
		if( _portalLayer == null ) {
			Gdx.app.debug(TAG, "No portal layer");
		}
		
		_spawnsLayer = _currentMap.getLayers().get(MAP_SPAWN_LAYER);
		if( _spawnsLayer == null ) {
			Gdx.app.debug(TAG, "No spawn layer");
		} else {
			Vector2 start = _playerStartLocationTable.get(_currentMapName);
			if( start.isZero() ) {
				setClosestStartPosition(_playerStart);
				start = _playerStartLocationTable.get(_currentMapName);
			}
			_playerStart.set(start.x, start.y);
		}
		
		Gdx.app.debug( TAG, new StringBuilder().append("Player Start: (")
				.append(_playerStart.x)
				.append(",")
				.append(_playerStart.y)
				.append(")").toString() );
	}
	
	public TiledMap getCurrentMap() {
		if(_currentMap == null ) {
			_currentMapName = TOWN;
			loadMap(_currentMapName);
		}
		return _currentMap;
	}
	
	public MapLayer getCollisionLayer() {
		return _collisionLayer;
	}
	
	public MapLayer getPortalLayer() {
		return _portalLayer;
	}
	
	/**
	 *  The location will be in pixel coordinates.
	 *  We need to convert these coordinates to unit coordinates so that when this
	 *  method is called, the character will start in the correct location using the map units.
	 * @return
	 */
	public Vector2 getPlayerStartUnitScaled() {
		Vector2 playerStart = _playerStart.cpy();
		playerStart.set(_playerStart.x * UNIT_SCALE, _playerStart.y * UNIT_SCALE);
		return playerStart;
	}
	
	/**
	 * This method will cache the closest spawn location to
	 * the player on the current map. This is used when the portal activation occurs in order
	 * to start the player in the correct location when transitioning out of the new location,
	 * back to the previous location. For instance, there are two player start locations on the
	 * TOP_WORLD map. One player's start spawn is near the village represented by the TOWN
	 * map, and the other one is outside the enemy's castle, represented by the CASTLE_OF_
	 * DOOM map. In order to resolve the ambiguity of which player start location we should
	 * choose, we call this method when we are transitioning to another location. So, if you
	 * enter the enemy castle and then leave, you will start at the player start spawn outside
	 * the castle because when you first entered the castle, this player start spawn was the
	 * closest to your location at that time
	 * @param position
	 */
	private void setClosestStartPosition(final Vector2 position) {
		//Get last know position on this map
		_playerStartPositionRect.set(0,0);
		_closestPlayerStartPosition.set(0,0);
		float shortestDistance = 0f;
		
		//Go through all player start positions and choose closest to
		//last known position
		for( MapObject object : _spawnsLayer.getObjects()) {
			if( object.getName().equalsIgnoreCase(PLAYER_START) ) {
				((RectangleMapObject)object).getRectangle().getPosition(_playerStartPositionRect);
				float distance = position.dst(_playerStartPositionRect);
				
				if(distance < shortestDistance || shortestDistance == 0) {
					_closestPlayerStartPosition.set(_playerStartPositionRect);
					shortestDistance = distance;
				}
			}
		}
		_playerStartLocationTable.put(_currentMapName, _closestPlayerStartPosition.cpy());
	}
		
	/**
	 * This method is a helper method
	 * that wraps setClosestStartPosition() so that we can map the unit coordinate
	 * location back into pixel coordinate space used in the map.
	 * @param position
	 */
	public void setClosestStartPositionFromScaledUnits(Vector2 position) {
		if (UNIT_SCALE <= 0)
			return;
		_convertedUnits.set(position.x / UNIT_SCALE, position.y / UNIT_SCALE);
		setClosestStartPosition(_convertedUnits);
	}
}
