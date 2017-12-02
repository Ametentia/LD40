package com.pixeldot.ld40.Util;

import com.pixeldot.ld40.Metro;
import com.pixeldot.ld40.State.Play;

import java.util.Stack;

public class GameStateManager {

    public final Metro metro;
    private Stack<State> states;

    public GameStateManager(Metro metro) {
        this.metro = metro;

        states = new Stack<State>();
    }

    private State GetState(StateType type) {
        switch (type) {
            case Play:
                return new Play(this);

            // TODO(Pixel): Implement
            case Menu:
            case EndScreen:
            default:
                throw new IllegalArgumentException("Error: This is implemented yet!");
        }
    }

    public void AddState(StateType type) {
        states.push(GetState(type));
    }

    public void RemoveState() {
        State s = states.pop();
        if(s != null) s.Dispose();
    }

    public void SetState(StateType type) {
        RemoveState();
        AddState(type);
    }

    public void Update(float dt) { states.peek().Update(dt); }
    public void Render() { states.peek().Render(); }
}
