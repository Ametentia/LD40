package com.pixeldot.ld40.Entities.Tiles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.pixeldot.ld40.Util.ContentManager;
import com.pixeldot.ld40.Util.Vars;

public class Tile {

    public static final int Size = 80;

    private int x, y;
    private TileParam params;

    protected Sprite sprite;
    private float depth;

    private TileType type;

    private int num;

    public Tile(int x, int y, int num) {
        if(num == 1) {
            x -= 2;
            y -= 3;
        }
        else if(num == 0) {
            x -= 2;
            y -= 1;
        }


        this.x = x;
        this.y = y;

        String name = "Tile_Reactor" + num;

        this.type = TileType.Reactor;

        sprite = new Sprite(ContentManager.Instance.GetTexture(name));
        sprite.setFlip(false, true);

        Vector2 pos = Vars.PosToIso(x * Size, y * Size);
        depth = -(sprite.getHeight() - Size) + ((sprite.getWidth() - Size) / 4f) + 40;
        sprite.setPosition(pos.x, pos.y + depth);

        params = null; // TODO
        this.num = num;
    }

    public Tile(TileType type, int x, int y) {
        this.x = x;
        this.y = y;

        String name = "Tile_" + type.name();
        if(type == TileType.Grass) {
            name += (Vars.Random.nextInt(1) + 1);
        }

        this.type = type;


        sprite = new Sprite(ContentManager.Instance.GetTexture(name));
        sprite.setFlip(false, true);

        Vector2 pos = Vars.PosToIso(x * Size, y * Size);
        depth = -(sprite.getHeight() - Size) + ((sprite.getWidth() - Size) / 4f);
        sprite.setPosition(pos.x, pos.y + depth);

        params = null; // TODO
    }

    /*public Tile(TileType type, int x, int y) {
        String name = "Tile_" + type.name();
        if(type == TileType.Grass) {
            name = "Tile_Grass" + ((int)(Math.random() + 0.5) + 1);
        }

        if(type != TileType.Blank) {



            sprite = new Sprite(ContentManager.Instance.GetTexture(name));
            sprite.setOrigin(0, sprite.getHeight());
            Vector2 pos = PosToIso(x * Size, y * Size);
            sprite.setPosition(pos.x, pos.y);

            int mul = (sprite.getTexture().getHeight() / 80) - 1;

            System.out.println(mul);
            sprite.setPosition(-(Size / 2f) + pos.x, pos.y);

            sprite.flip(false, true);
        }

        this.x = x;
        this.y = y;

        params = new TileParam(type);
        this.type = type;
    }*/

    public void update(float dt) {}

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void render(ShapeRenderer renderer) {
        switch (num) {
            case 0:
                renderer.setColor(Color.ORANGE);
                break;
            case 1:
                renderer.setColor(Color.BLUE);
                break;
            case 2:
                renderer.setColor(Color.MAGENTA);
                break;
            case 3:
                renderer.setColor(Color.GREEN);
                break;
        }
        Rectangle r = sprite.getBoundingRectangle();
        renderer.rect(r.x, r.y, r.width, r.height);

        renderer.setColor(Color.WHITE);
    }

    public float getDepth() { return depth + sprite.getX() + sprite.getY(); }

    public TileParam getParams() { return params; }

    public TileType getType() { return type; }
}
