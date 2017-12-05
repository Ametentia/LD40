package com.pixeldot.ld40.MiniGames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.pixeldot.ld40.State.Play;
import com.pixeldot.ld40.Util.Animation;
import com.pixeldot.ld40.Util.ContentManager;
import com.pixeldot.ld40.Util.GameStateManager;
import com.pixeldot.ld40.Util.State;

/**
 * @author Matthew Threlfall
 */
public class Sheep extends MiniGame{
    private int successCount;
    private int sheepCount;
    private Sheepie[] sheep;
    private float sheepSpeed;
    private Texture fence;
    private final Vector2 fencePos = new Vector2(xStart + (drawEndX-xStart)/2, drawEndY-yStart - 24);
    private Vector2[] zeds;
    private int zCount;
    private BitmapFont font;
    private BitmapFont bigFont;
    private Sound DIE;


    public Sheep(GameStateManager gsm, State previous) {
        super(gsm, previous);
        successCount = 10;
        sheepSpeed = 240;

        DIE = ContentManager.Instance.GetSound("DIE_SHEEP");
        sheepCount = 0;
        sheep = new Sheepie[successCount];
        fence = ContentManager.Instance.GetTexture("SheepFence");
        font = ContentManager.Instance.GetFont("ubuntu84");
        bigFont = ContentManager.Instance.GetFont("ubuntu130");
        addSheep();
        zeds = new Vector2[60];
        zCount = 0;
    }
    private void addSheep() {
        sheep[sheepCount] = new Sheepie(new Vector2(xStart, drawEndY-yStart), sheepSpeed, ((drawEndX - xStart)/10) * 4, (drawEndX - xStart)/5, drawEndX - 128);
        sheepCount++;
    }

    public void render() {
        super.render();
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        Color c1 = new Color(232/255f, 134/255f,16/255f,1);
        Color c2 = new Color(229/255f, 58/255f,19/255f,1);
        renderer.rect(xStart, yStart, drawEndX-xStart, drawEndY-yStart, c1, c1, c2, c2);
        renderer.end();
        batch.begin();
        for(int i =0 ; i < zCount; i ++) {
            float progress = (drawEndY - zeds[i].y) / (drawEndY - yStart);
            Color white = new Color(1,1,1,1);
            font.setColor(white.lerp(new Color(1,1,1,0), progress));
            font.draw(batch, "z", zeds[i].x, zeds[i].y);
        }
        GlyphLayout glo = new GlyphLayout(bigFont, sheepCount+"");
        bigFont.setColor(new Color(1,1,1,0.7f));
        bigFont.draw(batch, sheepCount+"", xStart+(drawEndX-xStart)/2 - glo.width/2, yStart + glo.height);

        sheep[sheepCount-1].render(batch);

        batch.draw(fence, xStart + (drawEndX-xStart)/2, drawEndY-yStart-24);

        Texture t = ContentManager.Instance.GetTexture("PixelGrass");
        Sprite s = new Sprite(t);
        s.setFlip(false, true);
        for(int i = 0; i < 4; i ++) {
            batch.draw(s, xStart+ i * 128, drawEndY-yStart);
        }
        s = new Sprite(ContentManager.Instance.GetTexture("PixelGrassEnd"));
        s.setFlip(false, true);
        batch.draw(s, xStart + 4*128, drawEndY-yStart);
        batch.end();

    }
    public void update(float dt) {
        super.update(dt);
        sheepSpeed = Math.min(sheepSpeed+5*dt, 310);
        if(!sheep[sheepCount-1].isAlive()) {
            addSheep();
            for(int i = 0; i < 4; i ++) {
                if(zCount+i < 60)
                    zeds[zCount + i] = new Vector2(MathUtils.random(xStart + 50, drawEndX - 50), drawEndY - 84 + MathUtils.random(0, 25));
            }
            zCount += 4;
            zCount = Math.min(59, zCount);
        }
        sheep[sheepCount-1].update(dt);
        Vector2 pos = sheep[sheepCount-1].getPosition();
        if(pos.x < fencePos.x + 48 && pos.x + 104 > fencePos.x) {
            if(pos.y < fencePos.y + 96 && pos.y + 73 > fencePos.y) {
                DIE.play(0.4f);
                sheep[sheepCount-1] = new Sheepie(new Vector2(xStart, drawEndY-yStart), sheepSpeed, ((drawEndX - xStart)/10) * 4, (drawEndX - xStart)/5, drawEndX - 128);
            }

        }
        if(Gdx.input.isTouched()) {
            sheep[sheepCount-1].jump();
        }
        for(int i = 0; i < zCount; i ++) {
            zeds[i].y -= MathUtils.random(46,86) * dt;
            zeds[i].x += MathUtils.sin((yStart-zeds[i].y)/10);
            if(zeds[i].y - 24 < yStart) {
                zeds[i].y += drawEndY-yStart - 84 + 24;
            }
        }
        if(sheepCount == successCount) {
            gsm.RemoveState();
            if(background instanceof Play) ((Play)background).setFocus();
        }
    }
}

class Sheepie {
    private float x;
    private float y;
    private float speed;
    private float deathX;
    private boolean jumping;
    private boolean jumpDone;
    private boolean alive;
    private Animation animation;
    private Texture t;
    private float jumpOffset;
    private float ySpeed;
    private Sound s;
    private float initialY;
    private boolean isDone;

    public boolean isDone() { return isDone; }
    public void setAlive(boolean alive) { this.alive = alive;}

    public Vector2 getPosition() {
        return new Vector2(x, y);
    }

    public boolean isAlive() {return alive; }
    public void rotate() {

    }

    public void update(float dt) {
        animation.update(dt);
        x += speed * dt;
        y += ySpeed * dt;
        if(y < initialY) {
            ySpeed += dt * 2800f;
        }
        else {
            ySpeed = 0;
            y = initialY;
        }
        if(x > deathX) {
            alive = false;
        }
    }
    public void jump() {
        if(!jumping && !jumpDone) {
            jumping = true;
            ySpeed = -1180;
            s.play(0.4f);
        }
    }

    public Sheepie(Vector2 spawn, float speed, float xJump, float jmpDis, float deathX) {
        this.x = spawn.x;
        this.deathX = deathX;
        this.y = spawn.y;
        this.initialY = spawn.y;
        t = ContentManager.Instance.GetTexture("SheepPixel");
        if(MathUtils.random(0,100)==0) {
            t = ContentManager.Instance.GetTexture("SheepPixelRain");
        }
        this.jumpOffset = 0;
        this.speed = speed;
        jumpDone = false;
        jumping = false;
        isDone = false;
        this.alive = true;
        animation = new Animation(t, 1, 4, 0.1f);
        ySpeed = 0;
        s  = ContentManager.Instance.GetSound("sheepJump");
    }
    public void render(SpriteBatch batch) {
        if(alive) {
            animation.render(batch, new Vector2(x, y + jumpOffset));
        }
    }
}
