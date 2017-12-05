package com.pixeldot.ld40.MiniGames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.pixeldot.ld40.Metro;
import com.pixeldot.ld40.Util.ContentManager;
import com.pixeldot.ld40.Util.GameStateManager;
import com.pixeldot.ld40.Util.State;

/**
 * @author Matthew Threlfall
 */
public class SchoolQuiz extends MiniGame{

    private int answersNeeded;
    private int answersCorrect;
    private BitmapFont font;
    private BitmapFont fontBig;
    private String currentQuestion;
    private int correct;
    private int offsets[];
    private float buttonsStartX[];
    private float buttonsStartY;
    private float noticeTimer;
    private String noticeLabel;
    private float mouseTimeoout;
    private Sound correctSound;
    private Sound wrongSound;

    public SchoolQuiz(GameStateManager gsm, State lastState) {
        super(gsm, lastState);
        font = ContentManager.Instance.GetFont("OhMaria64");
        fontBig = ContentManager.Instance.GetFont("OhMaria84");
        offsets = new int[3];
        currentQuestion = generateQuestion();
        buttonsStartX = new float[] {xStart + (drawEndX-xStart)/4 *(0+1) - 50, xStart + (drawEndX-xStart)/4 *(1+1) - 50, xStart + (drawEndX-xStart)/4 *(2+1) - 50 };
        buttonsStartY = yStart + (drawEndY-yStart)*0.3f - 100;
        noticeTimer = 0;
        answersNeeded = 10;
        answersCorrect = 0;
        mouseTimeoout = 0;
        noticeLabel = "";
        correctSound = ContentManager.Instance.GetSound("correct");
        wrongSound = ContentManager.Instance.GetSound("wrong");

    }

    public void render() {
        batch.setProjectionMatrix(camera.combined);
        renderer.setProjectionMatrix(camera.combined);
        super.render();
        GlyphLayout glo = new GlyphLayout(fontBig, currentQuestion.split("#")[0]);
        batch.begin();
        fontBig.setColor(1,1,1,1);
        fontBig.draw(batch, currentQuestion.split("#")[0], xStart + (drawEndX-xStart)/2 - glo.width/2, Metro.W_HEIGHT-(yStart + (drawEndY-yStart)*0.8f + glo.height/2));
        fontBig.setColor(1,1,1,noticeTimer/2);
        glo = new GlyphLayout(font, noticeLabel);
        fontBig.draw(batch,noticeLabel, xStart + (drawEndX-xStart)/2 - glo.width/1.2f, Metro.W_HEIGHT-(yStart + (drawEndY-yStart)*0.6f - glo.height/2));
        for(int i = 0; i < 3; i ++) {
            String answer = currentQuestion.split("#")[1];
            if(correct != i) {
                answer = Integer.parseInt(currentQuestion.split("#")[1]) + offsets[i] + "";
            }
            glo = new GlyphLayout(font, answer);
            font.draw(batch, answer, xStart + (drawEndX-xStart)/4 *(i+1) - glo.width/2,  Metro.W_HEIGHT-(yStart + (drawEndY-yStart)*0.3f - glo.height/2));
        }
        batch.end();

        renderer.begin(ShapeRenderer.ShapeType.Line);
        for(int i = 0; i < 3; i ++) {
            String answer = currentQuestion.split("#")[1];
            if(correct != i) {
                answer = Integer.parseInt(currentQuestion.split("#")[1]) + offsets[i] + "";
            }
            glo = new GlyphLayout(font, answer);
            int x = MathUtils.round(xStart + (drawEndX-xStart)/4 *(i+1) - 50);
            int y = Metro.W_HEIGHT - MathUtils.round(yStart + (drawEndY-yStart)*0.3f );
            renderer.setColor(1,1,1,1);
            renderer.rect(x, y, 100, 100);
        }
        renderer.end();
    }
    public void update(float dt)  {
        super.update(dt);
        if(noticeTimer>0) {
            noticeTimer -= dt;
        }
        if(mouseTimeoout>0)
            mouseTimeoout -= dt;
        for(int i = 0; i < 3; i ++ ) {
            int x,y;
            x = Gdx.input.getX();
            y = Metro.W_HEIGHT - Gdx.input.getY();
            if(x >= buttonsStartX[i] && x <= buttonsStartX[i]+100) {
                if(y >= buttonsStartY && y <= buttonsStartY+100) {
                    if(Gdx.input.isTouched() && mouseTimeoout <=0) {
                        if(i == correct){
                            answersCorrect++;
                            noticeLabel = "Correct!";
                            correctSound.play(0.4f);
                        }
                        else {
                            noticeLabel = "Incorrect!";
                            wrongSound.play(0.4f);
                        }
                        currentQuestion = generateQuestion();
                        noticeTimer = 2;
                        mouseTimeoout = 0.5f;
                    }
                }
            }
        }
        if(answersCorrect > answersNeeded) {
            gsm.RemoveState();
        }
    }


    public String generateQuestion() {
        int num1 = MathUtils.random(1, 13);
        int num2 = MathUtils.random(1, 10);
        int symbol = MathUtils.random(1,3);
        String question = ""+num1;
        int answer = -3123123;
        switch (symbol) {
            case 1:
                question += " x ";
                answer = num1*num2;
                break;
            case 2:
                question += " + ";
                answer = num1 + num2;
                break;
            case 3:
                answer = num1-num2;
                question += " - ";
                break;
        }
        correct = MathUtils.random(0,2);
        for(int i = 0 ; i < 3; i ++) {
            int rand;
            do {
                rand = MathUtils.random(-7 ,7);
                for(int j = i - 1; j > -1; j--) {
                    if(offsets[j] == rand) {
                        rand = 0;
                    }
                }
            } while(rand == 0);
            offsets[i] = correct == i ? 0 : rand;
        }
        return question+num2+"=?#" + answer;
    }
}