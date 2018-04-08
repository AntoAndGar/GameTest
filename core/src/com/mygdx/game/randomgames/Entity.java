package com.mygdx.game.randomgames;

import java.util.UUID;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Will represent the primary class for our player character in the game.
 * Represents the primary game object, including the player character
 * and non-playable characters (NPCs), which can move around in the world and
 * interact with their environment.
 * @author Anto
 *
 */
public class Entity {
	private static final String TAG = Entity.class.getSimpleName();
	private static final String _defaultSpritePath = "sprites/characters/Warrior.png";
	
	private Vector2 _velocity;
	private String _entityID;
	
	private Direction _currentDirection = Direction.LEFT;
	private Direction _previousDirection = Direction.UP;
	
	private Animation _walkLeftAnimation;
	private Animation _walkRightAnimation;
	private Animation _walkUpAnimation;
	private Animation _walkDownAnimation;
	
	private Array<TextureRegion> _walkLeftFrames;
	private Array<TextureRegion> _walkRightFrames;
	private Array<TextureRegion> _walkUpFrames;
	private Array<TextureRegion> _walkDownFrames;
	
	protected Vector2 _nextPlayerPosition;
	protected Vector2 _currentPlayerPosition;
	protected State _state = State.IDLE;
	protected float _frameTime = 0f;
	protected Sprite _frameSprite = null;
	protected TextureRegion _currentFrame = null;
	
	public final int FRAME_WIDTH = 16;
	public final int FRAME_HEIGHT = 16;
	public static Rectangle boundingBox;
	
	public enum State {
		IDLE, WALKING
	}
	
	public enum Direction {
		UP,RIGHT,DOWN,LEFT;
	}
	
	public Entity(){
		initEntity();
	}
	
	public void initEntity(){
		this._entityID = UUID.randomUUID().toString();
		this._nextPlayerPosition = new Vector2();
		this._currentPlayerPosition = new Vector2();
		this.boundingBox = new Rectangle();
		this._velocity = new Vector2(2f,2f);
		Utility.loadTextureAsset(_defaultSpritePath);
		loadDefaultSprite();
		loadAllAnimations();
	}
	
	/**
	 * This method will be called on any game object entity before it is rendered.
	 * One of the states we need to maintain for smooth animation cycles is frameTime,
	 * which is simply the accumulation of the deltas between frame updates. This allows
	 * the animation to account for changes in the frame rate of the game. One quick
	 * note is that depending on how long the game is playing, we don’t want to have a
	 * value increasing for the entire lifetime of the game since there is the potential for an
	 * overflow. One simple solution is to mod the value to 5, essentially resetting the
	 * value every five seconds
	 * @param delta
	 */
	public void update(float delta){
		_frameTime = (_frameTime + delta)%5; //Want to avoid overflow
		//We want the hitbox to be at the feet for a better feel
		setBoundingBoxSize(0f, 0.5f);
		}

	public void init(float startX, float startY) {
		this._currentPlayerPosition.x = startX;
		this._currentPlayerPosition.y = startY;
		this._nextPlayerPosition.x = startX;
		this._nextPlayerPosition.y = startY;
	}
	
	/**
	 * This method allows us to customize the hitbox for the
	 * different entities. In our case, we currently only use this for our player character in
	 * the game. Based on the tileset graphics, the default area of the hitbox for the player
	 * is the width and height of the sprite. This could cause issues when trying to traverse
	 * through forested areas with collision rectangles spread about, as well as when
	 * blocking the player from moving along the bottom of mountain ranges or on the top
	 * of lakes in the game. One solution is to reduce the height of the hitbox to half, which
	 * gives us a hitbox of a rectangle from the waist to the bottom of the character. This
	 * allows a better feeling movement of the player and also looks much better when
	 * moving through obstacles
	 * @param percentageWidthReduced
	 * @param percentageHeightReduced
	 */
	public void setBoundingBoxSize(float percentageWidthReduced, float percentageHeightReduced){
		//Update the current bounding box
		float width;
		float height;
		
		float widthReductionAmount = 1.0f - percentageWidthReduced;
		//.8f for 20% (1 - .20)
		float heightReductionAmount = 1.0f - percentageHeightReduced;
		//.8f for 20% (1 - .20)
		
		if( widthReductionAmount > 0 && widthReductionAmount < 1){
			width = FRAME_WIDTH * widthReductionAmount;
		} else {
			width = FRAME_WIDTH;
		}
		
		if( heightReductionAmount > 0 && heightReductionAmount < 1){
			height = FRAME_HEIGHT * heightReductionAmount;
		} else {
			height = FRAME_HEIGHT;
		}
		
		if( width == 0 || height == 0){
			Gdx.app.debug(TAG, "Width and Height are 0!! " + width + ":" + height);
		}
		
		//Need to account for the unitscale, since the map coordinates will be in pixels
		float minX;
		float minY;
		if( MapManager.UNIT_SCALE > 0 ) {
			minX = _nextPlayerPosition.x / MapManager.UNIT_SCALE;
			minY = _nextPlayerPosition.y / MapManager.UNIT_SCALE;
		} else {
			minX = _nextPlayerPosition.x;
			minY = _nextPlayerPosition.y;
		}
		
		boundingBox.set(minX, minY, width, height);
		}
	
	private void loadDefaultSprite() {
		Texture texture = Utility.getTextureAsset(_defaultSpritePath);
		TextureRegion[][] textureFrames = TextureRegion.split(texture,
				FRAME_WIDTH, FRAME_HEIGHT);
		_frameSprite = new Sprite(textureFrames[0][0].getTexture(),
				0,0,FRAME_WIDTH, FRAME_HEIGHT);
		_currentFrame = textureFrames[0][0];
	}
	
	/**
	 * method should only be called when first instantiating the entity objects.
	 * The Texture will include the entire image consisting of 16 different sprites,
	 * each row representing a different direction animation of four frames, 
	 * including walking down, walking to the left,walking to the right, and walking up.
	 * When we want to use the individual sprites, we can call the split() static method 
	 * in the TextureRegion class by passing in the Texture and the width and height 
	 * dimensions representing one sprite. We will then get back an array of TextureRegion
	 * objects that represent the individual keyframes for the animation. 
	 * We can render these objects since each TextureRegion references
	 * a specific subregion of the Texture. We can then take these arrays of TextureRegion
	 * objects and create four Animation objects, for each of the four cardinal directions
	 */
	private void loadAllAnimations(){
		//Walking animation
		Texture texture = Utility.getTextureAsset(_defaultSpritePath);
		TextureRegion[][] textureFrames = TextureRegion.split(texture, FRAME_WIDTH, FRAME_HEIGHT);
		_walkDownFrames = new Array<TextureRegion>(4);
		_walkLeftFrames = new Array<TextureRegion>(4);
		_walkRightFrames = new Array<TextureRegion>(4);
		_walkUpFrames = new Array<TextureRegion>(4);
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				TextureRegion region = textureFrames[i][j];
				if( region == null ){
					Gdx.app.debug(TAG, "Got null animation frame " + i + "," + j);
				}
				switch(i)
				{
				case 0:
					_walkDownFrames.insert(j, region);
					break;
				case 1:
					_walkLeftFrames.insert(j, region);
					break;
				case 2:
					_walkRightFrames.insert(j, region);
					break;
				case 3:
					_walkUpFrames.insert(j, region);
					break;
				}
			}
		}
		_walkDownAnimation = new Animation(0.25f, _walkDownFrames, Animation.PlayMode.LOOP);
		_walkLeftAnimation = new Animation(0.25f, _walkLeftFrames, Animation.PlayMode.LOOP);
		_walkRightAnimation = new Animation(0.25f, _walkRightFrames, Animation.PlayMode.LOOP);
		_walkUpAnimation = new Animation(0.25f, _walkUpFrames, Animation.PlayMode.LOOP);
	}
	
	public void dispose(){
		Utility.unloadAsset(_defaultSpritePath);
	}
	
	public void setState(State state){
		this._state = state;
	}
	
	public Sprite getFrameSprite(){
		return _frameSprite;
	}
	
	public TextureRegion getFrame(){
		return _currentFrame;
	}
	
	public Vector2 getCurrentPosition(){
		return _currentPlayerPosition;
	}
	
	public void setCurrentPosition(float currentPositionX, float currentPositionY){
		_frameSprite.setX(currentPositionX);
		_frameSprite.setY(currentPositionY);
		this._currentPlayerPosition.x = currentPositionX;
		this._currentPlayerPosition.y = currentPositionY;
	}
	
	/**
	 * This method deals with updating the animation keyframes based 
	 * on our current cardinal direction. This method will be called every time we process
	 * input from the event queue. During every frame of the render loop, the current
	 * TextureRegion frame that represents the player character will be retrieved and
	 * rendered. Based on the current facing direction, this method will guarantee that 
	 * the proper frame is set at that time
	 * @param direction
	 * @param deltaTime
	 */
	public void setDirection(Direction direction, float deltaTime){
		this._previousDirection = this._currentDirection;
		this._currentDirection = direction;
		
		//Look into the appropriate variable when changing position
		switch (_currentDirection) {
			case DOWN :
				_currentFrame = (TextureRegion) _walkDownAnimation.getKeyFrame(_frameTime);
				break;
			case LEFT :
				_currentFrame = (TextureRegion) _walkLeftAnimation.getKeyFrame(_frameTime);
				break;
			case UP :
				_currentFrame = (TextureRegion) _walkUpAnimation.getKeyFrame(_frameTime);
				break;
			case RIGHT :
				_currentFrame = (TextureRegion) _walkRightAnimation.getKeyFrame(_frameTime);
				break;
			default:
				break;
		}
	}
	
	public void setNextPositionToCurrent(){
		setCurrentPosition(_nextPlayerPosition.x, _nextPlayerPosition.y);
	}
	
	/**
	 * This method is called every time that player input is
	 * detected. Sometimes, collisions are not detected during a frame update because the
	 * velocity value is too fast to be calculated in the current frame. By the time the next
	 * frame checks the collision, the game objects have already passed through each other.
	 * Basically, this method represents one technique to deal with collisions between two
	 * moving objects in the game world. We are going to “look ahead” and predict what
	 * the next position value will be by using our current velocity and the time to render
	 * the last frame. By multiplying the current velocity vector, _velocity, and by the
	 * deltaTime scalar quantity using the scl() method, we get a value that represents
	 * the distance we would travel (displacement). We add or subtract this distance to our
	 * next position based upon our direction. If this new position collides with an object,
	 * then it is not set to our current position. Otherwise, we will use that value as our
	 * current position
	 * @param currentDirection
	 * @param deltaTime
	 */
	public void calculateNextPosition(Direction currentDirection, float deltaTime){
		float testX = _currentPlayerPosition.x;
		float testY = _currentPlayerPosition.y;
		
		_velocity.scl(deltaTime);
		
		switch (currentDirection) {
			case LEFT :
				testX -= _velocity.x;
				break;
			case RIGHT :
				testX += _velocity.x;
				break;
			case UP :
				testY += _velocity.y;
				break;
			case DOWN :
				testY -= _velocity.y;
				break;
			default:
				break;
		}
		
		_nextPlayerPosition.x = testX;
		_nextPlayerPosition.y = testY;
		
		//velocity
		_velocity.scl(1 / deltaTime);
	}

}
