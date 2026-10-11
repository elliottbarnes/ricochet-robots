package controller;

/** Plain game state; no Swing components or executable Java serialization. */
public record SaveData(boolean complex, boolean hints, int palette, int players,
        int[] x, int[] y, int token, boolean complete, int[] scores, int[] bids,
        int moves, int selectedRobot, int selectedPlayer, int timerSeconds) {
    public SaveData {
        x = copy(x); y = copy(y); scores = copy(scores); bids = copy(bids);
        range(palette, 0, 3); range(players, 1, 15); range(token, 0, 16);
        range(moves, 0, 1000000); range(selectedRobot, 0, 4);
        range(selectedPlayer, 0, 4); range(timerSeconds, -1, 60);
        if (complete && token != 1) throw new IllegalArgumentException("Invalid completed round");
        if (selectedPlayer > 0 && (players & (1 << (selectedPlayer - 1))) == 0)
            throw new IllegalArgumentException("Selected player is not in this game");
        for (int i = 0; i < 4; i++) {
            range(x[i], 6, 566); range(y[i], 6, 566);
            range(scores[i], 0, 16); range(bids[i], 0, 1000000);
        }
    }
    private static int[] copy(int[] values) {
        if (values == null || values.length != 4) throw new IllegalArgumentException("Expected four values");
        return values.clone();
    }
    private static void range(int n, int min, int max) {
        if (n < min || n > max) throw new IllegalArgumentException("Saved value outside its valid range");
    }
    @Override public int[] x() { return x.clone(); }
    @Override public int[] y() { return y.clone(); }
    @Override public int[] scores() { return scores.clone(); }
    @Override public int[] bids() { return bids.clone(); }

    public SettingsController settings() {
        SettingsController result = new SettingsController();
        result.setComplex(complex); result.setHintsEnabled(hints);
        result.setNormal(palette == 0); result.setDeuteranope(palette == 1);
        result.setProtanope(palette == 2); result.setTritanope(palette == 3);
        result.setPlayer1((players & 1) != 0); result.setPlayer2((players & 2) != 0);
        result.setPlayer3((players & 4) != 0); result.setPlayer4((players & 8) != 0);
        return result;
    }
}
