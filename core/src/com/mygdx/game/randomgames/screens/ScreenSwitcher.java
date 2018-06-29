package com.mygdx.game.randomgames.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;

/**
 * Mhe class it can just be more encapsulable and polymorphic... 
 * Done with Eddo in spare time
 * TODO: adjust everything
 * @author Anto
 *
 */
public class ScreenSwitcher extends InputAdapter {
    Game game;
    Screen screen1;
    Screen screen2;
    int currentScreen;

    public ScreenSwitcher(Game game, Screen screen1, Screen screen2) {
        this.game = game;
        this.screen1 = screen1;
        this.screen2 = screen2;
        currentScreen = 1;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Keys.SPACE) {
            if (currentScreen == 1) {
                currentScreen = 2;
                game.setScreen(screen2);
            } else {
                currentScreen = 1;
                game.setScreen(screen1);
            }
        }
        return true;
    }

}
