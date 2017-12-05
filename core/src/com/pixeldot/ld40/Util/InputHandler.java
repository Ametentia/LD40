package com.pixeldot.ld40.Util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.pixeldot.ld40.Entities.Tiles.TileType;

public class InputHandler extends InputAdapter {

    private int scrollValue;
    private TileType current;

    public InputHandler() {
        scrollValue = 0;
        current = TileType.Grass;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.NUM_1:
                current = TileType.Road;
                break;
            case Input.Keys.NUM_2:
                current = TileType.Housing;
                break;
            case Input.Keys.NUM_3:
                current = TileType.TownHall;
                break;
            case Input.Keys.NUM_4:
                current = TileType.Reactor;
                break;
            case Input.Keys.NUM_5:
                current = TileType.School;
                break;
            case Input.Keys.NUM_6:
                current = TileType.Water;
                break;
            case Input.Keys.NUM_7:
                current = TileType.Hospital;
                break;
            default:
                return false;
        }

        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        scrollValue += amount;
        return true;
    }

    public void resetScroll() { scrollValue = 0; }

    public int getScrollValue() {
        return scrollValue;
    }

    public TileType getCurrent() {
        return current;
    }
}
