package com.mygdx.game.randomgames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.assets.AssetManager;

/**
 * Some asset manager helper methods.
 * Represents a placeholder for various methods including dealing with 
 * the loading and unloading of game assets.
 * @author Anto
 *
 */
public final class Utility {
	private static final String TAG = Utility.class.getSimpleName();
	private static InternalFileHandleResolver _filePathResolver = new InternalFileHandleResolver();
	public static final AssetManager _assetManager = new AssetManager();
	
	/**
	 * Helper method that takes advantage of the fact that
	 * there is one static instance of AssetManager for all game assets. This method will
	 * check to see whether the asset is loaded, and if it is, then it will unload the 
	 * asset from memory
	 * @param assetFilenamePath
	 */
	public static void unloadAsset(String assetFilenamePath) {
		if(_assetManager.isLoaded(assetFilenamePath)) {
			_assetManager.unload(assetFilenamePath);
		} 
		else {
			Gdx.app.debug(TAG, "Asset is not loaded; Nothing to unload: " + assetFilenamePath);
		}
	}
	
	/**
	 * Wraps the progress of AssetManager as a percentage
	 * of completion. This can be used to update progress meter 
	 * values when loading asynchronously.
	 * @return percentage of completion
	 */
	public static float loadCompleted() {
		return _assetManager.getProgress();
	}
	
	/**
	 * Wraps the number of assets
	 * left to load from the AssetManager queue.
	 * @return number of assets left to load
	 */
	public static int numberAssetsQueued() {
		return _assetManager.getQueuedAssets();
	}
	
	/**
	 * Wraps the update call in AssetManager and can be called in a render() loop,
	 * if loading assets asynchronously in order to process the preload queue.
	 * @return update call
	 */
	public static boolean updateAssetLoading() {
		return _assetManager.update();
	}

	/**
	 * Wraps the AssetManager method isLoaded() and will return a simple
	 * Boolean value on whether the asset is currently loaded or not
	 * @param fileName
	 * @return the asset is currently loaded or not
	 */
	public static boolean isAssetLoaded(String fileName) {
		return _assetManager.isLoaded(fileName);
		
	}
	
	/**
	 * Will take a TMX filename path relative to the working
	 * directory and load the TMX file as a TiledMap asset, blocking until finished. We can
	 * load these assets later asynchronously once we create a screen with a progress bar,
	 * instead of blocking on the render thread.
	 * @param mapFilenamePath
	 */
	public static void loadMapAsset(String mapFilenamePath) {
		if( mapFilenamePath == null || mapFilenamePath.isEmpty() )
			return;
		
		//load asset
		if( _filePathResolver.resolve(mapFilenamePath).exists() ) {
			_assetManager.setLoader(TiledMap.class, new TmxMapLoader(_filePathResolver) );
			_assetManager.load(mapFilenamePath, TiledMap.class);
			
			//Until we add loading screen,
			//just block until we load the map
			_assetManager.finishLoadingAsset(mapFilenamePath);
			Gdx.app.debug(TAG, "Map loaded!: " + mapFilenamePath);
		}
		else {
			Gdx.app.debug(TAG, "Map doesn't exist!: " + mapFilenamePath);
		}
	}
	
	/**
	 * Can retrieve the asset for use
	 * @param mapFilenamePath
	 * @return the asset
	 */
	public static TiledMap getMapAsset(String mapFilenamePath) {
		TiledMap map = null;
		
		if(_assetManager.isLoaded(mapFilenamePath) ) 
			map = _assetManager.get(mapFilenamePath, TiledMap.class);
		else
			Gdx.app.debug(TAG, "Map is not loaded: " + mapFilenamePath);
		
		return map;
	}
	
	/**
	 * Will take an image filename path relative to the
	 * working directory and load the image file as a Texture asset, blocking until finished.
	 * @param textureFilenamePath
	 */
	public void loadTextureAsset(String textureFilenamePath) {
		if( textureFilenamePath == null || textureFilenamePath.isEmpty() )
			return;
		
		//load asset
		if( _filePathResolver.resolve(textureFilenamePath).exists() ) {
			_assetManager.setLoader(Texture.class, new TextureLoader(_filePathResolver));
			_assetManager.load(textureFilenamePath, Texture.class);
			//Until we add loading screen,
			//just block until we load the map
			_assetManager.finishLoadingAsset(textureFilenamePath);
		}
		else {
			Gdx.app.debug(TAG, "Texture doesn't exist!: " + textureFilenamePath);
		}
	}
	
	/**
	 * Can retrieve the asset for use
	 * @param textureFilenamePath
	 * @return the asset
	 */
	public Texture getTextureAsset(String textureFilenamePath) {
		Texture texture = null;
		
		if( _assetManager.isLoaded(textureFilenamePath) ) 
			texture = _assetManager.get(textureFilenamePath, Texture.class);
		else 
			Gdx.app.debug(TAG, "Texture is not loaded: " + textureFilenamePath);
		
		return texture;
	}
	
}
