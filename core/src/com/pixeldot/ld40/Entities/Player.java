package com.pixeldot.ld40.Entities;

public class Player {

    private static final float PowerWeight = 2.4f;
    private static final float WaterWeight = 3.2f;
    private static final float PollutionWeight = 6.4f;

    private float money;
    private float income;
    private float outcome;

    // Good parameters (max these)
    private float excessPower;
    private float excessWater;

    // Bad parameters (min these)
    private float pollution;
    private int population;

    // Between 0 and 1
    private float satisfaction;

    private float accumulator;

    public Player() {
        money = 10000;
        outcome = income = 0;

        excessPower = 0;
        excessWater = 0;

        satisfaction = 1;

        population = 0;

        accumulator = 0;
    }

    public void update(float dt) {
        accumulator += dt;
        if(accumulator < 1) return;

        money += income;
        money -= outcome;

        satisfaction = (money + (PowerWeight * excessPower) + (WaterWeight * excessWater))
                / (population + (PollutionWeight * pollution) + 1);
        satisfaction = 1 - (float) Math.exp(-satisfaction);


        accumulator = 0;
    }

    public void setPollution(float pollution) {
        this.pollution = pollution;
    }

    public void setOutcome(float outcome) {
        this.outcome = outcome;
    }

    public void setIncome(float income) {
        this.income = income;
    }

    public void setExcessPower(float excessPower) {
        this.excessPower = excessPower;
    }

    public void setExcessWater(float excessWater) {
        this.excessWater = excessWater;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public String toString() {
        return "City Stats: [\n" +
                "Money: " + money + "\n" +
                "Power: " + excessPower + "\n" +
                "Water: " + excessWater + "\n" +
                "Satisfaction: " + satisfaction + "\n" +
                "Income: " + income + "\n" +
                "Outcome: " + outcome + "\n" +
                "\n]";
    }
}
