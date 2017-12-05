package com.pixeldot.ld40.Tiles;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pixeldot.ld40.Entities.Tiles.TileType;
import com.pixeldot.ld40.Util.ContentManager;

public class Well extends BaseTile {

    private Sprite tile;

    public Well(int x, int y) {
        super(x, y);

        tile = new Sprite(ContentManager.Instance.GetTexture("Tile_Well"));
        tile.setPosition(isoPosition.x, isoPosition.y);
        tile.flip(false,true);

        float z = -(tile.getHeight() - Size) + ((tile.getWidth() - Size) / 4f);
        tile.translate(0, z);
    }

    @Override
    public void render(SpriteBatch batch) {
        tile.draw(batch);
    }


    public TileType getType() {
        return TileType.Water;
    }
}
