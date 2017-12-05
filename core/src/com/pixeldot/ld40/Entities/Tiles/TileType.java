package com.pixeldot.ld40.Entities.Tiles;

public enum TileType {
    Grass(0, 0, 0, 0, 0, 0, 0),

    Road(0, 0, 0, 0, 0, 0, 0),

    TownHall(60, 0, 60, 0, 50, 700, 100),

    Housing(75, 0, 75, 0, 50, 0, 75),
    Reactor(0, 250, 100, 0, 250, 300, 0),
    School(50, 0, 50, 0, 5, 500, 0),
    Hospital(100, 0, 100, 0, 100, 300, 0),
    Water(0, 0, 0, 250, 0, 50, 0);

    public final float powerIntake;
    public final float powerOutput;

    public final float waterIntake;
    public final float waterOutput;

    public final float pollution;

    public final float moneyIntake;
    public final float moneyOutput;

    TileType(float pI, float pO, float wI, float wO, float p, float mI, float mO) {
        powerIntake = pI;
        powerOutput = pO;

        waterIntake = wI;
        waterOutput = wO;

        pollution = p;

        moneyIntake = mI;
        moneyOutput = mO;
    }
}
