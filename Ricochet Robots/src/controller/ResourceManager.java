package controller;

import java.io.*;
import java.nio.file.*;
import java.util.Properties;

/** Versioned, bounded plain-text saves; legacy object streams are not loaded. */
public final class ResourceManager {
    private ResourceManager() {}

    public static void save(SaveData data, String fileName) throws IOException {
        Properties p = new Properties();
        p.setProperty("format", "ricochet-robots-1");
        p.setProperty("complex", String.valueOf(data.complex()));
        p.setProperty("hints", String.valueOf(data.hints()));
        p.setProperty("complete", String.valueOf(data.complete()));
        p.setProperty("palette", String.valueOf(data.palette()));
        p.setProperty("players", String.valueOf(data.players()));
        p.setProperty("token", String.valueOf(data.token()));
        p.setProperty("moves", String.valueOf(data.moves()));
        p.setProperty("robot", String.valueOf(data.selectedRobot()));
        p.setProperty("player", String.valueOf(data.selectedPlayer()));
        p.setProperty("timer", String.valueOf(data.timerSeconds()));
        int[] x=data.x(), y=data.y(), scores=data.scores(), bids=data.bids();
        for (int i=0; i<4; i++) {
            p.setProperty("x"+i, String.valueOf(x[i])); p.setProperty("y"+i, String.valueOf(y[i]));
            p.setProperty("score"+i, String.valueOf(scores[i])); p.setProperty("bid"+i, String.valueOf(bids[i]));
        }
        Path destination = Path.of(fileName).toAbsolutePath();
        Path temporary = Files.createTempFile(destination.getParent(), ".ricochet-", ".tmp");
        try {
            try (OutputStream out = Files.newOutputStream(temporary)) { p.store(out, "Ricochet Robots saved game"); }
            try { Files.move(temporary, destination, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE); }
            catch (AtomicMoveNotSupportedException ex) { Files.move(temporary, destination, StandardCopyOption.REPLACE_EXISTING); }
        } finally { Files.deleteIfExists(temporary); }
    }

    public static SaveData load(String fileName) throws IOException {
        Properties p = new Properties();
        try (InputStream in = Files.newInputStream(Path.of(fileName))) {
            byte[] bytes = in.readNBytes(65537);
            if (bytes.length > 65536) throw new IOException("Saved game is too large");
            p.load(new ByteArrayInputStream(bytes));
        } catch (IllegalArgumentException ex) { throw new IOException("Invalid saved game", ex); }
        if (!"ricochet-robots-1".equals(p.getProperty("format")))
            throw new IOException("Unsupported save format. Choose a .rrsave file created by this version.");
        try {
            int[] x=new int[4],y=new int[4],scores=new int[4],bids=new int[4];
            for(int i=0;i<4;i++) { x[i]=number(p,"x"+i);y[i]=number(p,"y"+i);scores[i]=number(p,"score"+i);bids[i]=number(p,"bid"+i); }
            return new SaveData(bool(p,"complex"), bool(p,"hints"), number(p,"palette"), number(p,"players"),
                x,y,number(p,"token"),bool(p,"complete"),scores,bids,number(p,"moves"),number(p,"robot"),number(p,"player"),number(p,"timer"));
        } catch (IllegalArgumentException ex) { throw new IOException("Invalid or incomplete saved game", ex); }
    }
    private static int number(Properties p,String key) { return Integer.parseInt(p.getProperty(key)); }
    private static boolean bool(Properties p,String key) {
        String value=p.getProperty(key);
        if (!"true".equals(value) && !"false".equals(value)) throw new IllegalArgumentException("Invalid boolean");
        return Boolean.parseBoolean(value);
    }
}
