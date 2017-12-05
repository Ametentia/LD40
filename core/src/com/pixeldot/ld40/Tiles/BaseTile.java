package com.pixeldot.ld40.Tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.pixeldot.ld40.Entities.Tiles.TileParam;
import com.pixeldot.ld40.Entities.Tiles.TileType;
import com.pixeldot.ld40.Util.Vars;

public abstract class BaseTile {

    public static final float Size = 80;

    protected int x, y;
    protected Vector2 isoPosition;

    protected boolean selected;

    // TODO(James): Implement!
    protected TileParam params;

    public BaseTile(int x, int y) {
        this.x = x;
        this.y = y;

        isoPosition = Vars.PosToIso(x * Size, y * Size);
        selected = false;

        params = new TileParam(getType());
    }

    public void update(float dt) {}

    public abstract void render(SpriteBatch batch);

    public abstract TileType getType();

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
