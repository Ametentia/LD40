package com.pixeldot.ld40.State;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.pixeldot.ld40.Metro;
import com.pixeldot.ld40.Util.ContentManager;
import com.pixeldot.ld40.Util.GameStateManager;
import com.pixeldot.ld40.Util.State;
import com.pixeldot.ld40.Util.StateType;

/**
 * @author Matthew Threlfall
 */
public class PixeldotSplash extends State{
    private float logoTime;
    private float duration;
    private float angle;
    private int textAlpha;
    private StateType next;
    private BitmapFont f;
    private boolean noNoise;

    public PixeldotSplash(GameStateManager gsm) {
        super(gsm);
        logoTime = 0.05f;
        textAlpha = 0;
        noNoise = true;
        duration = 1f;
        angle = 0;
        ContentManager.Instance.LoadFont("BigNoodle","fonts/big_noodle_titling.ttf", 90);
        ContentManager.Instance.LoadSound("LogoSound", "sounds/LogoNoise.ogg");
        f = ContentManager.Instance.GetFont("BigNoodle");
        next = StateType.Play;
    }

    @Override
    public void update(float dt) {
        if(logoTime > 0.2f && noNoise) {
            ContentManager.Instance.GetSound("LogoSound").play(0.4f);
            noNoise=false;
        }

        if (logoTime < duration) {
            angle = -180*logoTime/duration;
            logoTime += dt* MathUtils.sin(180*MathUtils.degRad*logoTime/duration);
        }
        if (logoTime > duration*0.8f){
            textAlpha += 255*dt;
            textAlpha = Math.min(textAlpha, 255);
        }
        if (textAlpha == 255) {
            gsm.SetState(next);
        }
        if(Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            textAlpha = 255;
            logoTime = duration;
            angle = -180;
        }
    }


    @Override
    public void render() {

        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl20.glEnable(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl20.glBlendEquation(GL20.GL_FUNC_ADD);
        Gdx.gl20.glBlendFunc(GL20.GL_ONE, GL20.GL_ONE);
        renderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);

        renderer.begin(ShapeRenderer.ShapeType.Filled);

        renderer.setColor(new Color(255/255,0,0, 150f/255f));
        renderer.rect(Metro.W_WIDTH/2 - 150, Metro.W_HEIGHT/2 - 150, 150, 150, 300,300, 1,1,angle);

        renderer.setColor(new Color(0,0,1, 150f/255f));
        renderer.rect(Metro.W_WIDTH/2 - 150, Metro.W_HEIGHT/2 - 150, 150, 150, 300,300, 1,1,angle- (90*(logoTime/duration)/3));


        renderer.setColor(new Color(0,255/255,0, 150f/255f));
        renderer.rect(Metro.W_WIDTH/2 - 150, Metro.W_HEIGHT/2 - 150, 150, 150, 300,300, 1,1,angle - (90*(logoTime/duration)/3*2) );

        renderer.end();
        batch.begin();

        f.setColor(new Color(0,0,0,textAlpha/255f));
        GlyphLayout glo = new GlyphLayout(f, "Pixeldot");
        f.draw(batch, "PIxeldot", Metro.W_WIDTH/2 - glo.width/2-10 + 10, Metro.W_HEIGHT/2 - glo.height*1.5f + 15);

        glo = new GlyphLayout(f, "Studios");
        f.setColor(new Color(0,0,0,textAlpha/255f));
        f.draw(batch, "StudIos", Metro.W_WIDTH/2 - glo.width/2-10 + 10, Metro.W_HEIGHT/2 + 15);
        batch.end();
        Gdx.gl20.glDisable(GL20.GL_BLEND);
    }

    @Override
    public void dispose() {

    }
}
