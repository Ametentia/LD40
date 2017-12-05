package com.pixeldot.ld40.MiniGames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.pixeldot.ld40.State.Play;
import com.pixeldot.ld40.Util.ContentManager;
import com.pixeldot.ld40.Util.GameStateManager;
import com.pixeldot.ld40.Util.State;


/**
 * @author Matthew Threlfall
 */
public class PipeGame extends MiniGame{
    private Pipe[][] pipes;
    private boolean mouseDown;
    private Texture pipeArrow;
    private Sprite rotateArrow;
    private float timer;
    private Sound[] pipe;
    public PipeGame(GameStateManager gsm, State previous) {
        super(gsm, previous);
        mouseDown = false;
        pipes = new Pipe[10][10];
        generateRoute();
        pipeArrow = ContentManager.Instance.GetTexture("pipeArrow");
        rotateArrow = new Sprite(pipeArrow);
        //rotateArrow.rotate(180);
        rotateArrow.setPosition(xStart+109 + 400, yStart+420);
        pipe = new Sound[] {ContentManager.Instance.GetSound("pipe1"), ContentManager.Instance.GetSound("pipe2")};
        timer = 0;
    }


    public void render() {
        super.render();
        batch.setProjectionMatrix(camera.combined);
        renderer.setProjectionMatrix(camera.combined);
        batch.begin();
        for(int i = 0; i < 10; i ++) {
            for(int j = 0; j < 10; j++) {
                Pipe p = pipes[j][i];
                if(p.getType()!=Pipe.pipeType.EMPTY)
                p.getT().draw(batch);
            }
        }

        batch.draw(pipeArrow, xStart+98 - 40, yStart + 104 - 45);
        rotateArrow.draw(batch);

        batch.end();
    }

    public void update(float dt) {
        super.update(dt);
        timer += dt;
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            generateRoute();
        }
        if(Gdx.input.isTouched() && !mouseDown) {
            int startBoardX = xStart + (604-400)/2;
            int startBoardY = yStart + (514-400)/2;
            int x = Gdx.input.getX();
            int y = Gdx.input.getY();
            int indexX = (x-startBoardX)/40;
            int indexY = (y-startBoardY)/40;
            if(indexX < 10 && indexY < 10 && indexX > -1 && indexY > -1) {
                pipes[indexY][indexX].rotate();
                clearPipes();
                pipe[MathUtils.random(0,1)].play(0.4f);
            }
            mouseDown = true;
        }
        else if(!Gdx.input.isTouched()) {
            mouseDown=false;
        }
        if(pipes[9][9].isFull()) {
            gsm.RemoveState();
            if(background instanceof Play) ((Play)background).setFocus();
        }
    }
    private void clearPipes() {
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j<10; j ++) {
                pipes[j][i].empty();
            }
        }
        pipes[0][0].setFull(true);
    }
    private void generateRoute() {
        int x = 0;
        int y = 0;
        boolean repeat;
        do {
            repeat = false;
            x = 0;
            y = 0;
            boolean[][] visited = new boolean[10][10];
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    visited[j][i] = false;
                }
            }
            boolean lastVertical = true;
            while (x != 9 || y != 9) {
                int moveX = 0;
                int moveY = 0;
                visited[0][0] = true;
                int tries = 0;
                do {
                    tries++;
                    if (tries > 4) {
                        x = 9;
                        y = 9;
                        repeat = true;
                        break;
                    }
                    moveX = MathUtils.random(0, 1);
                    moveY = MathUtils.random(-1, 1);
                    boolean noDiag = Math.abs(moveX) + Math.abs(moveY) != 1;
                    if (noDiag || moveX + x < 0 || moveX + x > 9 || moveY + y > 9 || moveY + y < 0 || visited[moveY + y][moveX + x])
                        moveX = moveY = 0;
                } while (moveX + moveY == 0);

                x += moveX;
                y += moveY;
                pipes[y][x] = new Pipe(x, y, Pipe.pipeType.STRAIGHT, pipes, true, MathUtils.random(0, 4), new Vector2(xStart + x * 40 + (604 - 400) / 2, yStart + y * 40 + (514 - 400) / 2));
                if((lastVertical && moveY==0) || (!lastVertical&&moveY!=0)) {
                    pipes[y-moveY][x-moveX] = new Pipe(x-moveX, y-moveY, Pipe.pipeType.TURN, pipes, true, MathUtils.random(0, 4), new Vector2(xStart + (x-moveX) * 40 + (604 - 400) / 2, yStart + (y-moveY) * 40 + (514 - 400) / 2));
                }
                lastVertical = moveY!=0;

                visited[y][x] = true;
            }
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (!visited[i][j]) {
                        Pipe.pipeType picked = Pipe.pipeType.EMPTY;
                        int rand = MathUtils.random(0, 3);
                        if (rand == 1)
                            picked = Pipe.pipeType.EMPTY;
                        else if (rand == 2)
                            picked = Pipe.pipeType.EMPTY;
                        else if (rand == 3)
                            picked = Pipe.pipeType.EMPTY;
                        pipes[i][j] = new Pipe(j, i, picked, pipes, true, MathUtils.random(0, 4), new Vector2(xStart + j * 40 + (604 - 400) / 2, yStart + i * 40 + (514 - 400) / 2));
                    }
                }
            }

            pipes[0][0] = new Pipe(0, 0, Pipe.pipeType.CROSS, pipes, true, MathUtils.random(0, 4), new Vector2(xStart + 0 * 40 + (604 - 400) / 2, yStart + (0) * 40 + (514 - 400) / 2));
            pipes[0][0].setTurnAble(false);
            pipes[0][0].setFull(true);
            pipes[9][9] = new Pipe(9, 9, Pipe.pipeType.CROSS, pipes, true, MathUtils.random(0, 4), new Vector2(xStart + 9 * 40 + (604 - 400) / 2, yStart + (9) * 40 + (514 - 400) / 2));
            pipes[9][9].setTurnAble(false);

            for(int i = 0; i < 10; i++) {
                for(int j = 0; j < 10; j++) {
                    if(!visited[j][i]) {
                        Pipe.pipeType picked = Pipe.pipeType.STRAIGHT;
                        int rand = MathUtils.random(0,3);
                        if(rand ==1)
                            picked = Pipe.pipeType.CROSS;
                        else if(rand == 2)
                            picked = Pipe.pipeType.TURN;
                        else if(rand == 3)
                            picked = Pipe.pipeType.EMPTY;
                        pipes[j][i] = new Pipe(i,j, picked, pipes, true, MathUtils.random(0,4), new Vector2(xStart+i*40 + (604-400)/2, yStart+j*40 +(514-400)/2));
                    }
                }
            }

        } while(repeat);
    }

}
class Pipe {
    private boolean turnAble;
    private Pipe[][] pipes;
    private boolean full;
    private int x;
    private int y;
    private boolean[] openSides;
    private pipeType type;
    private Sprite t;
    private Sprite tO;
    private Vector2 drawStart;

    enum pipeType {
        STRAIGHT,
        TURN,
        CROSS,
        EMPTY
    }
    enum direction {
        top,
        bottom,
        right,
        left
    }
    public void setTurnAble(boolean turnAble) { this.turnAble = turnAble;}
    public boolean isFull() {
        return full;
    }
    public boolean isOpen(direction inDirection) {
        switch (inDirection) {
            case top:
                return openSides[0];
            case bottom:
                return openSides[1];
            case right:
                return openSides[2];
            case left:
                return openSides[3];
        }
        return false;
    }
    public void empty() {
        full = false;
    }

    public void setFull(boolean set) {
        if(!full)
            full = true;
        if(set) {
            for(int i = -1; i < 2; i++) {
                for(int j = -1; j < 2; j++) {
                    if(((i!= 0 && j==0) || (j!=0 && i==0)) && y+i > -1 && y+i < 10 && x+j >= 0 && x+j < 10  && !pipes[y+i][x+j].isFull()) {
                        direction in = direction.top;
                        boolean out = false;
                        switch (i) {
                            case 1:
                                in = direction.top;
                                out = openSides[1];
                                break;
                            case -1:
                                in = direction.bottom;
                                out = openSides[0];
                                break;
                            case 0:
                                switch (j) {
                                    case 1:
                                        in = direction.left;
                                        out = openSides[2];
                                        break;
                                    case -1:
                                        in = direction.right;
                                        out = openSides[3];
                                        break;
                                }
                                break;
                        }
                        if(pipes[y+i][x+j].isOpen(in) && out)
                        {
                            pipes[y+i][x+j].setFull(true);
                        }
                    }
                }
            }
        }
    }

    public void rotate() {
        if(turnAble) {
            boolean temp = openSides[0];
            openSides[0] = openSides[2];
            boolean left = openSides[3];
            openSides[3] = temp;
            temp = openSides[1];
            openSides[1] = left;
            openSides[2] = temp;
            t.rotate90(true);
            tO.rotate90(true);
        }
    }

    public Pipe(int x, int y,pipeType type, Pipe[][] pipes, boolean turnable, int turnCount, Vector2 screenPos) {
        turnAble = turnable;
        this.pipes = pipes;
        full = false;
        this.x = x;
        this.y = y;
        this.type = type;
        openSides = new boolean[4];
        drawStart = screenPos;
        switch (type) {
            case STRAIGHT:
                openSides = new boolean[]{true, true, false, false};
                t = new Sprite(ContentManager.Instance.GetTexture("pipeStraightEmp"));
                tO = new Sprite(ContentManager.Instance.GetTexture("pipeStraightFull"));
                break;
            case TURN:
                openSides = new boolean[]{false,true,true,false};
                t = new Sprite(ContentManager.Instance.GetTexture("pipeTurnEmp"));
                tO = new Sprite(ContentManager.Instance.GetTexture("pipeTurnFull"));
                break;
            case CROSS:
                openSides = new boolean[]{true, true, true, true};
                t = new Sprite(ContentManager.Instance.GetTexture("pipeCrossEmp"));
                tO = new Sprite(ContentManager.Instance.GetTexture("pipeCrossFull"));
                break;
            case EMPTY:
                openSides = new boolean[]{false, false,false, false};
                this.turnAble = false;
                break;
        }
        if(type!=pipeType.EMPTY) {
            t.setPosition(screenPos.x, screenPos.y);
            tO.setPosition(screenPos.x, screenPos.y);
            for (int i = 0; i < turnCount; i++) {
                boolean temp = openSides[0];
                openSides[0] = openSides[2];
                boolean left = openSides[3];
                openSides[3] = temp;
                temp = openSides[1];
                openSides[1] = left;
                openSides[2] = temp;
                t.rotate90(true);
                tO.rotate90(true);
            }
        }
    }
    public Pipe.pipeType getType() {return type;}
    public int getX() {return x;}
    public Sprite getT() {
        if(full) {
            return tO;
        }
        return t;
    }
    public int getY() {return y;}
}