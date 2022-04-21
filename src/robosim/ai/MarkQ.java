package robosim.ai;

import core.Duple;
import robosim.core.*;
import robosim.core.Robot;
import robosim.reinforcement.QTable;

import java.awt.*;

public class MarkQ implements Controller {
    private QTable qValues = new QTable(MarkQState.values().length, Action.values().length,
            0, 8, 200, 0.75);
    private int prevDirt = 0;
    private boolean gotDirt = false;

    @Override
    public void control(Simulator sim) {
        MarkQState state = getState(sim);
        if (prevDirt > sim.getDirt()) {
            gotDirt = true;
        } else {
            gotDirt = false;
        }
        prevDirt = sim.getDirt();
        int chosenAction = qValues.senseActLearn(state.getIndex(), reward(sim));
        Action.values()[chosenAction].applyTo(sim);
    }

    public MarkQState getState(Simulator sim) {
        if (sim.findClosestProblem() < 20) {
            return MarkQState.OBSCLOSE;
        }
        for (Duple<SimObject, Polar> obj: sim.allVisibleObjects()) {
            if (obj.getFirst().isVacuumable()) {
                return MarkQState.DIRT;
            }
        }
        return MarkQState.OBSFAR;
    }

    public double reward(Simulator sim) {
        if (sim.wasHit()) {
            return -10.0;
        } else if (gotDirt) {
            return 50.0;
        }
        else if (Action.values()[qValues.getLastAction()].equals(Action.FORWARD)) {
            if (getState(sim) == MarkQState.DIRT) {
                return 5.0;
            }
            return 1.0;
        }
        return 0;
    }
}
