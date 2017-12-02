package com.pixeldot.ld40.Util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.pixeldot.ld40.Metro;

public abstract class State {

    protected Metro metro;
    protected GameStateManager gsm;

    protected SpriteBatch batch;
    protected ShapeRenderer renderer;

    protected OrthographicCamera camera;

    protected final Vector3 mouse;

    public State(GameStateManager gsm) {
        this.gsm = gsm;
        metro = gsm.metro;

        batch = metro.getBatch();
        renderer = metro.getRenderer();

        camera = metro.getCamera();

        mouse = new Vector3(0, 0, 0);
    }

    public abstract void Update(float dt);
    public abstract void Render();
    public void Dispose() {}
}
