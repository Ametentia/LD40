package com.pixeldot.ld40.MiniGames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.pixeldot.ld40.Metro;
import com.pixeldot.ld40.Util.ContentManager;
import com.pixeldot.ld40.Util.GameStateManager;
import com.pixeldot.ld40.Util.State;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

/**
 * @author Matthew Threlfall
 */
public class PoliceChase extends MiniGame{

    private int lane;
    private Car[] cars;
    private float carTimer;
    private int carCount;
    private int maxCars;
    private int carsClear;
    private Sprite t;
    private float carSpeed;
    private Texture background;
    private int lastLane;

    public PoliceChase(GameStateManager gsm, State previous) {
        super(gsm, previous);
        lane = 1;
        carCount = 0;
        maxCars = 16;
        carTimer = 2;
        carsClear = 0;
        carSpeed = 195;
        cars = new Car[maxCars];
        t = new Sprite( ContentManager.Instance.GetTexture("PoliceCar"));
        background = ContentManager.Instance.GetTexture("roadMinigame");
        t.flip(false, true);
    }
    public void render() {
        super.render();
        batch.setProjectionMatrix(camera.combined);
        renderer.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, xStart, yStart);
        batch.end();

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        int laneWidth = (drawEndX - xStart) /3;
        renderer.setColor(1,1,0,1);
        float dir = 1.75f;
        for(int i =0; i < 4; i++) {
            dir -= 0.3f;
            renderer.rectLine(dir * xStart+(laneWidth*i) , yStart, xStart+laneWidth*i, (drawEndY), 6);
        }
        renderer.setColor(0,0,0,1);
        renderer.triangle(xStart,yStart,xStart,drawEndY,xStart*1.45f,yStart);
        renderer.triangle(drawEndX,yStart,drawEndX,drawEndY,Metro.W_WIDTH-xStart*1.45f,yStart);

        renderer.end();
        batch.begin();

        for(int i =0; i < carCount; i ++) {
            if(cars[i].isAlive()) {
                Car t = cars[i];
                float x = xStart + laneWidth * t.getLane();
                float y = t.getHeight();
                float x2 = xStart + (drawEndX-xStart)/2 + laneWidth*(t.getLane()-1)/1.25f;
                x2 += -(t.getLane()-1) * (1-(y - yStart)/(drawEndY-yStart)) * 30;
                batch.draw(t.getSprite(), x2 - 25, y);
            }
        }
        batch.draw(t, xStart+laneWidth/2 - 40 + laneWidth*lane + 25, drawEndY - 100);
        batch.end();
    }
    public void update(float dt) {
        super.update(dt);
        carTimer += dt;
        carSpeed += 15*dt;
        carSpeed = Math.min(400, carSpeed);
        if(carTimer > 0.7f && carCount < maxCars) {
            cars[carCount] = new Car(yStart, drawEndY-200, carSpeed, lastLane);
            lastLane = (int)cars[carCount].getLane();
            carCount ++;
            carTimer = 0;
        }
        int laneWidth = (drawEndX - xStart) / 3;
        if (Gdx.input.getX() > xStart + laneWidth && Gdx.input.getX() < xStart+laneWidth*2) {
            lane = 1;
        }
        else if(Gdx.input.getX() < xStart + laneWidth)
            lane = 0;
        else if(Gdx.input.getX() > xStart + laneWidth*2)
            lane = 2;
        for(int i = 0; i < carCount; i++ ){
            cars[i].update(dt);
            if(!cars[i].isAlive()) {
                if(cars[i].getLane() == lane && !cars[i].getCheckCol()) {
                    cars[i] = new Car(yStart, drawEndY-200, carSpeed, lastLane);
                    lastLane = (int)cars[i].getLane();
                }
                else if(!cars[i].getCheckCol()) {
                    carsClear++;
                    cars[i].setCheckCol();
                }
            }
        }
        if(carsClear == maxCars) {
            gsm.RemoveState();
        }
    }
}
class Car {
    private float x, y;
    private int cutoff;
    private boolean alive;
    private Sprite c;
    private boolean checkCol;
    private float carSpeed;

    public float getLane() { return x;}
    public float getHeight() {return y;}
    public boolean isAlive() {return alive;}

    public Sprite getSprite() { return c; }
    public boolean getCheckCol() {return checkCol;}
    public void setCheckCol() {
        checkCol = true;
    }

    public void update(float dt) {
        y += carSpeed*dt;
        if(y > cutoff) {
            alive = false;
        }
    }

    public Car(int start, int cutoff, float carSpeed, int lastLane) {
        do {
            x = MathUtils.random(0, 2);
        } while(x==lastLane);
        y = start;
        this.carSpeed = carSpeed;
        this.cutoff = cutoff;
        alive = true;
        c = new Sprite(ContentManager.Instance.GetTexture("Car"));
        c.setFlip(false, true
        );
        checkCol = false;
    }
}
