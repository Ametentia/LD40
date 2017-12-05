package com.pixeldot.ld40.Tiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pixeldot.ld40.Entities.Tiles.TileType;
import com.pixeldot.ld40.Util.ContentManager;

public class TownHall extends BaseTile {

    private Sprite tile;

    public TownHall(int x, int y, int lr) {
        super(x, y);

        tile = new Sprite(ContentManager.Instance.GetTexture("Tile_TownHall" + lr));
        tile.setFlip(false, true);
        tile.setOrigin(0, tile.getHeight());
        tile.setPosition(isoPosition.x, isoPosition.y);

        float z = -(tile.getHeight() - Size) + ((tile.getWidth() - Size) / 4f) - ((lr - 1) * 40f);
        tile.translate(0 , z);
    }


    public void render(SpriteBatch batch) {
        if(selected) tile.setColor(Color.GRAY);
        else tile.setColor(Color.WHITE);
        tile.draw(batch);

        super.render(batch);
    }

    public TileType getType() {
        return TileType.TownHall;
    }
}
