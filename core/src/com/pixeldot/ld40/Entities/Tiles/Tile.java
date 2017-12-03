package com.pixeldot.ld40.Entities.Tiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.pixeldot.ld40.Util.ContentManager;

public class Tile {

    static Vector2 PosToIso(float x, float y) {
        return new Vector2(x - y, (x + y) / 2f);
    }

    public Color debugOverlay = null;

    public static final int Size = 80;

    private int x;
    private int y;
    private TileParam params;

    protected Sprite sprite;

    private TileType type;

    public Tile(TileType type, int x, int y) {
        String name = type.name();
        if(type == TileType.Blank) {
            name = "Blank" + ((int)(Math.random() + 0.5) + 1);
        }

        sprite = new Sprite(ContentManager.Instance.GetTexture(name));
        Vector2 pos = PosToIso(x * Size, y * Size);

        int mul = (sprite.getTexture().getHeight() / 80) - 1;

        sprite.setPosition(-(Size / 2f) + pos.x, -mul * Size + pos.y);

        sprite.flip(false, true);

        this.x = x;
        this.y = y;

        params = new TileParam(type);
        this.type = type;
    }

    public void update(float dt) {}

    public void render(SpriteBatch batch) {
        if(debugOverlay != null) {
            sprite.setColor(debugOverlay);
        }
        else {
            sprite.setColor(Color.WHITE);
        }

        sprite.draw(batch);
    }

    public TileParam getParams() { return params; }

    public void setType(TileType type) {
        this.type = type;
        String name = type.name();
        if(type == TileType.Blank) {
            name = "Blank" + ((int)(Math.random() + 0.5) + 1);
        }

        sprite = new Sprite(ContentManager.Instance.GetTexture(name));
        Vector2 pos = PosToIso(x * Size, y * Size);

        sprite.flip(false, true);

        int mul = (sprite.getTexture().getHeight() / 80) - 1;

        sprite.setPosition(-(Size / 2f) + pos.x, -mul * Size + pos.y);

        params = new TileParam(type);
    }
    public TileType getType() { return type; }
}
