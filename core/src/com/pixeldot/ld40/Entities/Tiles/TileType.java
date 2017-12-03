package com.pixeldot.ld40.Entities.Tiles;

public enum TileType {
    Housing(20, 0, 20, 0, 4, 30, 60),
   // Power(10, 100, 40, 0, 50, 100, 0),
   // Water(20, 0, 100, 0, 40, 100, 0),
    Road(5, 0, 0, 0, 5, 20, 0),
    Blank(0, 0, 0, 0, 0, 0, 0);

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
