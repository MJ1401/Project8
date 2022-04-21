package robosim.ai;
public enum QInClassState {
    CLOSE, FAR;
    public int getIndex() {
        for (int i = 0; i < QInClassState.values().length; i++) {
            if (QInClassState.values()[i].equals(this)) {
                return i;
            }
        }
        throw new IllegalStateException("This should never happen");
    }
}
