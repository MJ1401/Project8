package robosim.ai;
import robosim.core.Action;
import robosim.core.Controller;
import robosim.core.Simulator;
import robosim.reinforcement.QTable;
public class QInClass implements Controller {
    private QTable qValues = new QTable(QInClassState.values().length, Action.values().length,
            0, 8, 200, .9);
    @Override
    public void control(Simulator sim) {
        QInClassState state = getState(sim);
        int chosenAction = qValues.senseActLearn(state.getIndex(), reward(sim));
        Action.values()[chosenAction].applyTo(sim);
    }
    public QInClassState getState(Simulator sim) {
        if (sim.findClosestProblem() < 30) {
            return QInClassState.CLOSE;
        } else {
            return QInClassState.FAR;
        }
    }
    public double reward(Simulator sim) {
        if (sim.wasHit()) {
            return -10.0;
        } else if (Action.values()[qValues.getLastAction()].equals(Action.FORWARD)) {
            return 1.0;
        } else {
            return 0.0;
        }
    }
}
