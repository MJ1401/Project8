package robosim.ai;

public enum MarkQState {
    OBSCLOSE, OBSFAR, DIRT;
    public int getIndex() {
        for (int i = 0; i < MarkQState.values().length; i++) {
            if (MarkQState.values()[i].equals(this)) {
                return i;
            }
        }
        throw new IllegalStateException("This should never happen :)");
    }
}
