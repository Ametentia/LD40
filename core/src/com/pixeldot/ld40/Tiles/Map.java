package com.pixeldot.ld40.Tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pixeldot.ld40.Entities.Player;
import com.pixeldot.ld40.Entities.Tiles.TileParam;
import com.pixeldot.ld40.Entities.Tiles.TileType;
import com.pixeldot.ld40.Util.Vars;

public class Map {

    private BaseTile[][] grid;

    private int xGrid, yGrid;

    private BaseTile selected;
    private TileType current;

    private int houseCount;
    private int wellCount;
    private int powerCount;
    private int townHallCount;

    public Map(int xGrid, int yGrid) {
        grid = new BaseTile[xGrid][yGrid];
        for(int i = 0; i < xGrid; i++) {
            for(int j = 0; j < yGrid; j++) {
                grid[i][j] = new Grass(i, j, Vars.Random.nextInt(2));
            }
        }

        this.xGrid = xGrid;
        this.yGrid = yGrid;

        current = TileType.Grass;
    }

    public void update(float dt) {}

    public void render(SpriteBatch batch, int x, int y) {
        for(int i = 0; i < xGrid; i++) {
            for(int j = 0; j < yGrid; j++) {
                if(i == x && j == y) {
                    switch (current) {
                        case Grass:
                            new Grass(x, y, 0).render(batch);
                            break;
                        case Reactor:
                            drawReactor(batch, x, y);
                            break;
                        case Housing:
                            new House(x, y).render(batch);
                            break;
                        case Road:
                            new Road(x, y).render(batch);
                            break;
                        case TownHall:
                            drawTownHall(batch, x, y);
                            break;
                        case School:
                            drawSchool(batch, x, y);
                            break;
                        case Water:
                            new Well(x, y).render(batch);
                            break;
                        case Hospital:
                            drawHospital(batch, x, y);
                            break;
                    }
                    continue;
                }

                if(grid[i][j] != null)
                    grid[i][j].render(batch);
            }
        }
    }


    public boolean placeTile(Player player, TileType type, int x, int y) {
        if(x < 0 || x >= xGrid) return false;
        else if(y < 0 || y >= yGrid) return false;

        BaseTile tile = grid[x][y];
        if(tile == null) return false;
        else if(tile.getType() != TileType.Grass) {

            selected = tile;
            player.setMinigame(tile.getType());
            return false;
        }

        switch (type) {
            case Road: {
                tile = new Road(x, y);
                if(!player.takeMoney(25)) return false;
                break;
            }
            case Housing: {
                tile = new House(x, y);
                if(!player.takeMoney(300)) return false;
                break;
            }
            case Reactor: {
                if (x + 1 >= xGrid) return false;
                else if (y - 1 < 0) return false;
                BaseTile[] surrounding = new BaseTile[4];
                surrounding[0] = grid[x][y];
                surrounding[1] = grid[x + 1][y - 1];
                surrounding[2] = grid[x][y - 1];
                surrounding[3] = grid[x + 1][y];
                int val = 0;
                for (int i = 0; i < 4; i++) {
                    if (surrounding[i] == null) return false;
                    else
                        val |= surrounding[i].getType().ordinal();
                }

                if (val != 0) return false;
                else if(!player.takeMoney(1500)) return false;

                grid[x][y] = new Reactor(x, y, 0);
                grid[x + 1][y - 1] = new Reactor(x + 1, y - 1, 1);

                grid[x][y - 1] = null;
                grid[x + 1][y] = null;
                powerCount++;
                return true;
            }
            case TownHall: {
                if(townHallCount >= 1) return false;

                BaseTile end = grid[x + 1][y];
                if (end == null || end.getType() != TileType.Grass) return false;
                else if (grid[x][y] == null || grid[x][y].getType() != TileType.Grass) return false;
                else if(!player.takeMoney(650)) return false;

                grid[x][y] = new TownHall(x, y, 0);
                grid[x + 1][y] = new TownHall(x + 1, y, 1);
                townHallCount++;
                return true;
            }
            case School: {
                BaseTile end = grid[x][y + 1];
                if(end == null || end.getType() != TileType.Grass) return false;
                else if(grid[x][y] == null || grid[x][y].getType() != TileType.Grass) return false;
                else if(!player.takeMoney(500)) return false;

                grid[x][y] = new School(x, y, 1);
                grid[x][y + 1] = new School(x, y + 1, 0);
                return true;
            }
            case Water: {
                if(grid[x][y] == null || grid[x][y].getType() != TileType.Grass) return false;
                else if(!player.takeMoney(450)) return false;
                grid[x][y] = new Well(x, y);

                wellCount++;
                return true;
            }
            case Hospital: {

                if(x + 1 >= xGrid || x + 2 >= xGrid) return false;
                else if(y - 1 < 0) return false;
                else if(!player.takeMoney(1500)) return false;

                BaseTile[] area = new BaseTile[5];
                area[0] = grid[x][y];
                area[1] = grid[x + 1][y];
                area[2] = grid[x + 1][y - 1];
                area[3] = grid[x + 2][y];
                area[4] = grid[x + 2][y - 1];

                int val = 0;
                for(int i = 0; i < 5; i++) {
                    if(area[i] == null) return false;
                    else val |= area[i].getType().ordinal();
                }

                if(val != 0) return false;

                grid[x][y] = new Hospital(x, y, 0);
                grid[x + 1][y - 1] = new Hospital(x + 1, y - 1, 1);
                grid[x + 2][y - 1] = new Hospital(x + 2, y - 1, 2);

                grid[x + 1][y] = null;
                grid[x + 2][y] = null;
                return true;
            }
        }

        grid[x][y] = tile;
        return true;
    }

    public void removeTile(int x, int y) {
        if(x < 0 || x >= xGrid) return;
        else if(y < 0 || y >= yGrid) return;

        BaseTile tile = grid[x][y];
        if(tile == null) {
            // TODO
        }
        else {
            switch (tile.getType()) {
                case Road:
                case Housing:
                case Water:
                    grid[x][y] = new Grass(x, y, Vars.Random.nextInt(2));
                    break;
            }
        }
    }

    public void getParams(TileParam params) {
        for(int i = 0; i < xGrid; i++) {
            for(int j = 0; j < yGrid; j++) {
                BaseTile tile = grid[i][j];
                if(tile == null) continue;
                TileType type = tile.getType();

                if(type == TileType.Housing) {
                    params.population += 20;
                }
                else if(type == TileType.Hospital) {
                    params.population += 50;
                }

                params.powerIntake += type.powerIntake;
                params.powerOutput += type.powerOutput;

                params.waterIntake += type.waterIntake;
                params.waterOutput += type.waterOutput;

                params.pollution += type.pollution;

                params.moneyIntake += type.moneyIntake;
                params.moneyOutput += type.moneyOutput;
            }
        }
    }

    private void drawHospital(SpriteBatch batch, int x, int y) {
        new Hospital(x, y, 0).render(batch);
        new Hospital(x + 1, y - 1, 1).render(batch);
        new Hospital(x + 2, y - 1, 2).render(batch);
    }

    private void drawSchool(SpriteBatch batch, int x, int y) {
        new School(x, y, 1).render(batch);
        new School(x, y + 1, 0).render(batch);
    }

    private void drawTownHall(SpriteBatch batch, int x, int y) {
        TownHall t = new TownHall(x, y, 0);
        t.render(batch);

        t = new TownHall(x + 1, y, 1);
        t.render(batch);
    }
    private void drawReactor(SpriteBatch batch, int x, int y) {
        Reactor r = new Reactor(x, y, 0);
        r.render(batch);

        r = new Reactor(x + 1, y - 1, 1);
        r.render(batch);
    }

    public void setCurrent(TileType current) {
        this.current = current;
    }

    public void reset() { selected = null; }
    public BaseTile getSelected() {
         return selected;
    }
}
