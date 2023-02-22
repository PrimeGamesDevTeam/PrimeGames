package net.primegames.components.leaderboard;

public abstract class Leaderboard {

//    private final int rows;
//    @Getter
//    private final String name;
//    private Hologram hologram;
//    private Location location;
//
//    public Leaderboard(String name, Location location, int rows) {
//        this.rows = rows;
//        this.name = name;
//    }
//
//    public Leaderboard(String name, Location location) {
//        this.location = location;
//        this.name = name;
//        this.rows = 10;
//    }
//
//    public void updateValues(Map<@NonNull Integer, @NonNull String> values) {
//        updateValues(values, null);
//    }
//
//    public void updateValues(Map<@NonNull Integer, @NonNull String> values, @Nullable String suffix) {
//        if (hologram != null) {
//            hologram.delete();
//            hologram = null;
//        }
//        hologram = HologramsAPI.createHologram(PrimeGames.getInstance(), location);
//        hologram.appendTextLine(name);
//        AtomicInteger maxRows = new AtomicInteger(rows);
//        AtomicInteger ranking = new AtomicInteger(1);
//        Map<Integer, String> shortedValues = shortScores(values);
//        shortedValues.forEach((value, name) -> {
//            if (maxRows.get() <= 0) return;
//            hologram.appendTextLine(getLbText(ranking.get(), name, value) + (suffix == null ? "" : suffix));
//            ranking.getAndIncrement();
//            maxRows.getAndDecrement();
//        });
//    }
//
//    private String getLbText(int ranking, String name, int value) {
//        return "§7" + ranking + ". §e" + name + "§7: §a" + value;
//    }
//
//    protected Map<Integer, String> shortScores(Map<Integer, String> scores) {
//        Map<Integer, String> shortedScores = new TreeMap<>(Collections.reverseOrder());
//        shortedScores.putAll(scores);
//        return shortedScores;
//    }
//
//
//    public abstract void scheduleUpdate();

}
