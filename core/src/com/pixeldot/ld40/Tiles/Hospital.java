package com.pixeldot.ld40.Tiles;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pixeldot.ld40.Entities.Tiles.TileType;
import com.pixeldot.ld40.Util.ContentManager;

public class Hospital extends BaseTile {

    private Sprite tile;

    public Hospital(int x, int y, int num) {
        super(x, y);

        tile = new Sprite(ContentManager.Instance.GetTexture("Tile_Hospital" + num));
        tile.setFlip(false, true);
        tile.setOrigin(0, tile.getHeight());
        tile.setPosition(isoPosition.x, isoPosition.y);

        float z = -(tile.getHeight() - Size) + ((tile.getWidth() - Size) / 4f) + (num == 2 ? 40 : 80);
        tile.translate(0 , z);

    }

    @Override
    public void render(SpriteBatch batch) {
        tile.draw(batch);
    }

    public TileType getType() {
        return TileType.Hospital;
    }
}
