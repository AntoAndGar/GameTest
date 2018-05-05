package com.mygdx.game.randomgames;

import java.awt.Component;

import com.badlogic.gdx.utils.Json;

/**
 * This class provides a convenience for creating the different Entity objects
 * defined by their components. One example of the usefulness for this class is that, if we
 * were demonstrating the game at a kiosk, we could simply have a timer go off at the
 * main menu screen, and instead of loading the PLAYER type, we could get an Entity
 * type DEMO_PLAYER. The only difference between the PLAYER type and the DEMO_PLAYER
 * type is that the DEMO_PLAYER type contains NPCInputComponent for input instead of
 * the PlayerInputComponent. This means that the AI logic that controls the movement
 * of NPCs would be used to move our player around the screen in a "demo" mode
 * instead of waiting for user input. The flexibility with swapping out components and
 * recombining them in order to yield different behaviors is just one of the many benefits
 * using this model
 * @author Anto
 *
 */
public class EntityFactory {
	private static Json _json = new Json();

	public static enum EntityType {
		PLAYER, DEMO_PLAYER, NPC
	}

	public static String PLAYER_CONFIG = "scripts/player.json";

	static public Entity getEntity(EntityType entityType) {
		Entity entity = null;
		switch (entityType) {
		case PLAYER:
			entity = new Entity(new PlayerInputComponent(), new PlayerPhysicsComponent(),
					new PlayerGraphicsComponent());
			entity.setEntityConfig(Entity.getEntityConfig(EntityFactory.PLAYER_CONFIG));
			entity.sendMessage(Component.MESSAGE.LOAD_ANIMATIONS, _json.toJson(entity.getEntityConfig()));
			return entity;
		case DEMO_PLAYER:
			entity = new Entity(new NPCInputComponent(), new PlayerPhysicsComponent(), new PlayerGraphicsComponent());
			return entity;
		case NPC:
			entity = new Entity(new NPCInputComponent(), new NPCPhysicsComponent(), new NPCGraphicsComponent());
			return entity;
		default:
			return null;
		}
	}
}
