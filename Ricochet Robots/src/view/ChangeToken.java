package view;

import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class ChangeToken {
	private Stack<Integer> st;
	private int currentToken;
	private boolean flag;
	public ChangeToken() {
		st = new Stack<Integer>();
		for(int i=1; i<17; i++) {
			st.push(i);
		}
	}
    public void next(Handler handler, JFrame frame) {
        if (st.isEmpty()) {
            flag = true;
            renderIndicator(handler);
            JOptionPane.showMessageDialog(frame, "All tokens have been taken");
        } else {
            currentToken = st.pop();
            renderIndicator(handler);
        }
    }
    public void restore(int token, boolean completed, Handler handler) {
        if (token < 0 || token > 16 || (completed && token != 1))
            throw new IllegalArgumentException("Invalid token state");
        currentToken = token;
        flag = completed;
        st.clear();
        for (int i=1; i <= (token == 0 ? 16 : token-1); i++) st.push(i);
        renderIndicator(handler);
    }
    public int remaining() { return st.size(); }
    private void renderIndicator(Handler handler) {
        handler.object.removeIf(o -> (o instanceof Token && o.getWidth() == 36 && o.getX() == 276)
            || (o.getId() == ID.Barrier && o.getX() == 270 && o.getWidth() == 50));
        if (currentToken > 0 && !flag)
            handler.addObject(new Token(276, 276, 36, 36, getIDToken(currentToken), currentToken));
    }

	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public int getCurrentToken() {
		return currentToken;
	}
	public void setCurrentToken(int currentToken) {
		this.currentToken = currentToken;
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
}
