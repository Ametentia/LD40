package com.pixeldot.ld40.Tiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pixeldot.ld40.Entities.Tiles.TileType;
import com.pixeldot.ld40.Util.ContentManager;

public class Road extends BaseTile {

    private Sprite tile;

    public Road(int x, int y) {
        super(x, y);
        tile = new Sprite(ContentManager.Instance.GetTexture("Tile_Road"));
        tile.setPosition(isoPosition.x, isoPosition.y);
        tile.flip(false, true);

        float z = -(tile.getHeight() - Size) + ((tile.getWidth() - Size) / 4f);
        tile.translate(0, z);
    }

    public void render(SpriteBatch batch) {
        if(selected) tile.setColor(Color.GRAY);
        else tile.setColor(Color.WHITE);

        tile.draw(batch);
    }

    // Road Tile type
    public TileType getType() {
        return TileType.Road;
    }
}
