package com.pixeldot.ld40.MiniGames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.pixeldot.ld40.Metro;
import com.pixeldot.ld40.Util.ContentManager;
import com.pixeldot.ld40.Util.GameStateManager;
import com.pixeldot.ld40.Util.State;

/**
 * @author Matthew Threlfall
 */
public class ConnectTheDots extends MiniGame {
    private Dot[] dots;
    private int dotCount;
    private int successCount;
    private int dotsHit;
    private float dotCounter;
    private GlyphLayout layout;
    private Sound sounds;

    BitmapFont font;
    public ConnectTheDots(GameStateManager gsm, State previousState) {

        super(gsm, previousState);
        dots = new Dot[6];
        sounds = ContentManager.Instance.GetSound("getDot1");
        successCount = 150;
        dotCount = 0;
        dotsHit = 0;
        dotCounter = 0;
        font = ContentManager.Instance.GetFont("ubuntu24");
        for(int i = 0; i < 6; i ++) {
            dots[i] = new Dot(new Vector2(), 0, -1);
            dots[i].setAlive(false);
        }
        addDotAt(0);
        layout = new GlyphLayout(font, "10");
    }

    public boolean addDotAt(int dotPos) {
        dotPos%=6;
        if(!dots[dotPos].getAlive() && successCount-dotCount>-1) {
            Vector2 pos = new Vector2();
            float radius = 30;
            boolean noOverlap;
            do {
                noOverlap = true;
                pos.x = MathUtils.random(xStart + radius * 2, drawEndX - radius * 2);
                pos.y = MathUtils.random(yStart + radius * 2, drawEndY - radius * 2);
                for(int i = 0; i < 6; i++) {
                    if (dots[i].getPosition().dst(pos) < radius * 3) {
                        noOverlap = false;
                    }
                }
            } while(!noOverlap);
            dots[dotPos] = new Dot(pos, radius, dotCount);
            dotCount++;
            return true;
        }
        return false;
    }
    public void update(float dt) {
        super.update(dt);
        dotCounter += dt;
        if(dotCounter > 0.2f) {
            for(int i = 0; i < 6; i++) {
                if(addDotAt(dotCount))
                    i = 100;
            }
            dotCounter = 0;
        }
        if(Gdx.input.isTouched()) {
            for(int i = 0; i < 6; i++) {
                if(dots[i].checkHit(dotsHit, new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0)) && dots[i].getAlive()) {
                    dots[i].setAlive(false);
                    dotsHit++;
                    sounds.play(0.4f);
                }
            }
        }
        for(int i = 0; i < 6; i ++) {
            if(dots[i].getAlive())
                dots[i].update(dt);
        }
        if(successCount < dotsHit) {
            gsm.RemoveState();
        }
    }
    public void render() {
        batch.setProjectionMatrix(camera.combined);
        renderer.setProjectionMatrix(camera.combined);
        super.render();
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        int count = 0;
        int lowest = Integer.MAX_VALUE;
        int lowIndex = -2;
        for(int i = 0; count < 6; i ++) {
            if(dots[i%6].getNumber() <= lowest && dots[i%6].getAlive()) {
                lowest = dots[i%6].getNumber();
                lowIndex = i % 6;
            }
            if(dots[i%6].getNumber() == dots[(i+1)%6].getNumber()-1) {
                renderer.setColor(dots[i%6].getColour());
                if(dots[i%6].getAlive())
                    renderer.rectLine(dots[i%6].getPosition(), dots[(i+1)%6].getPosition(), 4);
                count++;
            }
        }

        for(int i = 0; i < 6; i++) {
            if(dots[i].getAlive()) {
                Dot d = dots[i];
                renderer.setColor(d.getColour());
                renderer.circle(d.getPosition().x, d.getPosition().y, d.getRadius());
            }
        }
        renderer.end();
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(dots[lowIndex].getColour());
        renderer.circle(dots[lowIndex].getPosition().x,dots[lowIndex].getPosition().y, dots[lowIndex].getRadius()+(20*(dotCounter+0.7f)));
        renderer.end();
        batch.begin();
        for(int i = 0; i < 6; i ++) {
            Dot d = dots[i];
            layout = new GlyphLayout(font, d.getLabel());
            if(d.getAlive())
                font.draw(batch, d.getLabel(), d.getPosition().x - layout.width/2, d.getPosition().y - layout.height/2);
        }
        batch.end();
    }
}
class Dot {
    private float radius;
    private Vector2 position;
    private int label;
    private float sizePercent;
    private boolean alive;
    private Color colour;
    private float timeAlive;

    public void setAlive(boolean x) {
        alive = x;
    }
    public Vector2 getPosition() { return position; }
    public int getNumber() {return label; }

    public Color getColour() {
        return colour;
    }
    public String getLabel() {
        return "" + (label+1);
    }
    public void update(float dt) {
        if(timeAlive < 10) {
            timeAlive+=0.1f*dt;
        }
        if(sizePercent < 1) {
            sizePercent+=2.5*dt;
        }
        this.colour.lerp(new Color(230/255f,52/255f, 64/255f, 1), timeAlive/10f);
    }

    public boolean checkHit(int target, Vector3 mouse) {
        if(target!=label)
            return false;
        return square(mouse.x - position.x) + square(mouse.y - position.y) <= square(radius*sizePercent);
    }
    private float square(float x) {
        return x*x;
    }
    public boolean getAlive() {
        return alive;
    }
    public float getRadius() {
        return radius * sizePercent;
    }
    public Dot(Vector2 pos, float radius, int label) {
        sizePercent = 0.01f;
        this.label = label;
        this.position = pos;
        this.radius = radius;
        alive = true;
        colour = new Color( 100/255f,149/255f, 237/255f, 1);
        timeAlive = 0;
    }
}
