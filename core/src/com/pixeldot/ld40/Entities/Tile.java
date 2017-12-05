package com.pixeldot.ld40.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.pixeldot.ld40.Util.ContentManager;

/**
 * @author Matthew Threlfall
 */
public class Tile {
    protected int x;
    protected int y;
    protected Texture texture;
    protected int level;
    protected float updateTimer;
    protected boolean updating;

    public Tile(int x, int y, String textureID) {
        this.x = x;
        this.y = y;
        this.texture = ContentManager.Instance.GetTexture(textureID);
        this.level = 1;
    }

    public float getUpdateTime() {
        return level*15;
    }

    public void update(float dt) {
        if(updating)
            updateTimer += dt;
        if(updateTimer > getUpdateTime()) {
            level++;
            updateTimer = 0;
            updating = false;
        }
    }
    public void render() {

    }
}
