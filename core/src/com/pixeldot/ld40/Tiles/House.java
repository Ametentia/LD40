package com.pixeldot.ld40.Tiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pixeldot.ld40.Entities.Tiles.TileType;
import com.pixeldot.ld40.Util.ContentManager;

public class House extends BaseTile {

    private Sprite tile;

    public House(int x, int y) {
        super(x, y);

        tile = new Sprite(ContentManager.Instance.GetTexture("Tile_House"));
        tile.setPosition(isoPosition.x, isoPosition.y);
        tile.flip(false,true);

        float z = -(tile.getHeight() - Size) + ((tile.getWidth() - Size) / 4f);
        tile.translate(0, z);
    }

    @Override
    public void render(SpriteBatch batch) {
        if(selected) tile.setColor(Color.GRAY);
        else tile.setColor(Color.WHITE);
        tile.draw(batch);
    }

    @Override
    public TileType getType() {
        return TileType.Housing;
    }
}
