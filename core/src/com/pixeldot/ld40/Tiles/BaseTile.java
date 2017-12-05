package com.pixeldot.ld40.Tiles;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.pixeldot.ld40.Entities.Tiles.TileParam;
import com.pixeldot.ld40.Entities.Tiles.TileType;
import com.pixeldot.ld40.Util.ContentManager;
import com.pixeldot.ld40.Util.Vars;

public abstract class BaseTile {

    public static final float Size = 80;

    protected int x, y;
    protected Vector2 isoPosition;

    protected boolean selected;

    protected float timeSince;
    protected float limit = 10;
    protected boolean minigame;

    // TODO(James): Implement!
    protected TileParam params;

    public BaseTile(int x, int y) {
        this.x = x;
        this.y = y;

        isoPosition = Vars.PosToIso(x * Size, y * Size);
        selected = false;

        params = new TileParam(getType());
    }

    public void update(float dt) {
        timeSince += dt;
        if(timeSince >= limit) {
            timeSince = 0;
            minigame = true;
        }
    }

    public void render(SpriteBatch batch) {
        if(minigame) {
            Sprite warn = new Sprite(ContentManager.Instance.GetTexture("UI_Warning"));
            warn.setPosition(isoPosition.x, isoPosition.y);
            warn.setFlip(false, true);
            warn.draw(batch);
        }
    }

    public abstract TileType getType();

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
