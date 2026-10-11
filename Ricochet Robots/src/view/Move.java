package view;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Move {
    Handler handler;
    int tokenNum;
    JFrame frame;
    ChangeToken next;
    int player, p1score, p2score, p3score, p4score;
    public boolean flag;
    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public Move(Handler handler, JFrame frame, ChangeToken next) {
        this.handler = handler;
        this.frame = frame;
        this.next = next;
    }

    public int getTokenNum() {
        return tokenNum;
    }

    public void setTokenNum(int tokenNum) {
        this.tokenNum = tokenNum;
    }

    private SlideRules.Board slideBoard;

    public void up(int num) { slide(num, 0); }
    public void down(int num) { slide(num, 1); }
    public void left(int num) { slide(num, 2); }
    public void right(int num) { slide(num, 3); }

    private void slide(int num, int direction) {
        if (num < 1 || num > 4 || (next != null && next.isFlag())) return;
        GameObject[] robots = robots();
        GameObject robot = robots[num - 1];
        if (robot == null) return;
        int[][] position = positions(robots);
        if (slideBoard == null) slideBoard = new SlideRules.Board(handler);
        int cell = slideBoard.slide(position[0], position[1], num - 1, direction);
        if (cell < 0) return;
        robot.setX(SlideRules.x(cell));
        robot.setY(SlideRules.y(cell));
        if (tokenTaken(robot, getIDToken(getTokenNum()))) message();
    }

    /** Keep the original snapping entry point, but never snap through an obstacle. */
    public void setNewPosition(GameObject robot) {
        int index = SlideRules.robotIndex(robot.getId());
        if (index < 0) return;
        int[][] position = positions(robots());
        if (slideBoard == null) slideBoard = new SlideRules.Board(handler);
        int cell = slideBoard.snap(position[0], position[1], index);
        if (cell >= 0) {
            robot.setX(SlideRules.x(cell));
            robot.setY(SlideRules.y(cell));
        }
    }

    private GameObject[] robots() {
        GameObject[] result = new GameObject[4];
        for (GameObject object : handler.object) {
            int index = SlideRules.robotIndex(object.getId());
            if (index >= 0) result[index] = object;
        }
        return result;
    }

    private static int[][] positions(GameObject[] robots) {
        int[][] result = new int[2][4];
        for (int i = 0; i < 4; i++) {
            result[0][i] = robots[i] == null ? -1000 : robots[i].getX();
            result[1][i] = robots[i] == null ? -1000 : robots[i].getY();
        }
        return result;
    }

    public boolean collision2(GameObject robot, ID identity) {
        for (GameObject object : handler.object) {
            if ((object.getId() == ID.Barrier
                    || (SlideRules.robotIndex(object.getId()) >= 0 && object.getId() != identity))
                    && robot.getBounds().intersects(object.getBounds())) return true;
        }
        return false;
    }

    public boolean tokenTaken(GameObject gm, ID id) {
        for(int i=0; i<handler.object.size(); i++) {
            GameObject go = handler.object.get(i);
            if(go.getId() == id && go.getWidth() == 28 && go.getHeight() == 28) {
                if(gm.getBounds().intersects(go.getBounds())) {
                    if(gm.getId() == ID.Robot &&(tokenNum==1 || tokenNum==5 || tokenNum==9 || tokenNum==13)) return true;
                    if(gm.getId() == ID.Robot2 &&(tokenNum==2 || tokenNum==6 || tokenNum==10 || tokenNum==14)) return true;
                    if(gm.getId() == ID.Robot3 &&(tokenNum==3 || tokenNum==7 || tokenNum==11 || tokenNum==15)) return true;
                    if(gm.getId() == ID.Robot4 &&(tokenNum==4 || tokenNum==8 || tokenNum==12 || tokenNum==16)) return true;
                }
            }
        }
        return false;
    }

    public ID getIDToken(int num) {
        switch(num){
            case 1:
                return ID.Token1;
            case 2:
                return ID.Token2;
            case 3:
                return ID.Token3;
            case 4:
                return ID.Token4;
            case 5:
                return ID.Token5;
            case 6:
                return ID.Token6;
            case 7:
                return ID.Token7;
            case 8:
                return ID.Token8;
            case 9:
                return ID.Token9;
            case 10:
                return ID.Token10;
            case 11:
                return ID.Token11;
            case 12:
                return ID.Token12;
            case 13:
                return ID.Token13;
            case 14:
                return ID.Token14;
            case 15:
                return ID.Token15;
            case 16:
                return ID.Token16;
            default:
                return null;
        }
    }

    public void message() {
        String ms = "Token Taken by Player "+getPlayer();
        int confirmed = JOptionPane.showConfirmDialog(frame,
                "Next Token", ms, JOptionPane.OK_OPTION);

            if (confirmed == JOptionPane.OK_OPTION) {
                next.next(handler, frame);
                setFlag(true);
            }else {
                //nothing
            }
    }

    public int getP1score() {
        return p1score;
    }

    public void setP1score(int p1score) {
        this.p1score = p1score;
    }

    public int getP2score() {
        return p2score;
    }

    public void setP2score(int p2score) {
        this.p2score = p2score;
    }

    public int getP3score() {
        return p3score;
    }

    public void setP3score(int p3score) {
        this.p3score = p3score;
    }

    public int getP4score() {
        return p4score;
    }

    public void setP4score(int p4score) {
        this.p4score = p4score;
    }
    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
