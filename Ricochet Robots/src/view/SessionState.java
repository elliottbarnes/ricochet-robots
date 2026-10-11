package view;

import controller.SaveData;
import java.awt.Rectangle;

/** Validates and restores robot positions before opening a loaded game window. */
public final class SessionState {
    private SessionState() {}
    static final ID[] ROBOTS = {ID.Robot, ID.Robot2, ID.Robot3, ID.Robot4};
    public static int[][] positions(Handler handler) {
        int[][] values = new int[2][4];
        for(int i=0;i<4;i++) {
            GameObject robot=robot(handler,i);
            values[0][i]=robot.getX(); values[1][i]=robot.getY();
        }
        return values;
    }
    private static GameObject robot(Handler handler,int index) {
        return handler.object.stream().filter(o->o.getId()==ROBOTS[index]).findFirst()
            .orElseThrow(()->new IllegalArgumentException("Board is missing a robot"));
    }
    public static void restore(Handler handler, SaveData data) {
        int[] x=data.x(), y=data.y();
        for(int i=0;i<4;i++) {
            Rectangle bounds=new Rectangle(x[i],y[i],22,22);
            for(GameObject o:handler.object)
                if(o.getId()==ID.Barrier && bounds.intersects(o.getBounds()))
                    throw new IllegalArgumentException("Saved robot overlaps a wall");
            for(int j=0;j<i;j++) if(bounds.intersects(new Rectangle(x[j],y[j],22,22)))
                throw new IllegalArgumentException("Saved robots overlap");
            robot(handler,i); // Validate all objects before any position is changed.
        }
        for(int i=0;i<4;i++) {robot(handler,i).setX(x[i]);robot(handler,i).setY(y[i]);}
    }
}
