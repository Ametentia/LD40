package com.pixeldot.ld40.MiniGames;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.pixeldot.ld40.Util.GameStateManager;
import com.pixeldot.ld40.Util.State;

/**
 * @author Matthew Threlfall
 */
public class HospitalHelp extends MiniGame{

    public HospitalHelp(GameStateManager gsm, State previous) {
        super(gsm, previous);
    }

    public void render() {
        batch.setProjectionMatrix(camera.combined);
        renderer.setProjectionMatrix(camera.combined);
        super.render();
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(181/255,179/255,220/255,1);
        renderer.rect(xStart, yStart, drawEndX-xStart, drawEndY-yStart);
        renderer.end();
    }
    public void update(float dt) {
        super.update(dt);
    }
}

class Block {

    private int x;
    private int y;
    private int angle;
    private BLOCK_TYPE type;

    enum BLOCK_TYPE {
        LINE,
        BOX,
        PYRAMID,
        RIGHT_DOG,
        LEFT_DOG,
        LEFT_GUN,
        RIGHT_GUN
    }

    public Block(BLOCK_TYPE type, int x, int y) {
        angle = 0;
        this.x = x;
        this.y = y;
        this.type = type;
    }
    public void rotate() {
        angle = (angle+1)%4;
    }
    public int[][] getPiece() {
        int[][] piece;
        switch (type) {
            case BOX:
                piece = new int[][]{{0,1,1,0}, {0,1,1,0}};
                break;
            case LINE:
                piece = new int[][]{{0,1,1,0}, {0,1,1,0}};
                break;
            case PYRAMID:
                piece = new int[][]{{0,1,0,0},{1,1,1,0},};
                break;
            case LEFT_DOG:
                piece = new int[][] {{1,1,0,0},{0,1,1,0}};
                break;
            case RIGHT_DOG:
                piece = new int[][] {{0,0,1,1}, {1,1,0,0}};
                break;
            case LEFT_GUN:
                piece = new int[][]{{0,0,0,1}, {1,1,1,1}};
                break;
            case RIGHT_GUN:
                piece = new int[][]{{0,0,0,1}, {1,1,1,1}};
                break;
            default:
                piece = new int[][]{{0,1,1,0}, {0,1,1,0}};
                break;
        }

        int[][] rotationMatrix;
        int[][] resultMatrix;
        switch (angle) {
            case 0:
                rotationMatrix = new int[][] {{1,0}, {0,1}};
                resultMatrix = new int[4][2];
                break;
            case 1:
                rotationMatrix = new int[][]{{0,-1}, {1,0}};
                resultMatrix = new int[2][4];
                break;
            case 2:
                rotationMatrix = new int[][]{{-1,0}, {0,-1}};
                resultMatrix = new int[4][2];
                break;
            case 3:
                rotationMatrix = new int[][]{{0,1}, {-1,0}};
                resultMatrix = new int[2][4];
                break;
        }

        return piece;

    }


}
