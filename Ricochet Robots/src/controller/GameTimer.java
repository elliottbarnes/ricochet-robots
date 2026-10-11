package controller;
import java.awt.*;

import javax.swing.*;

import view.Settings;

import java.awt.event.*;
public class GameTimer extends JPanel {
	
	private int count = 61;
	private JLabel tmrDisplay;
	private Timer timer;
	private SettingsController sc;
	
    public GameTimer() { this(60); }
    public int remainingSeconds() { return Math.max(0, Math.min(60, count)); }
    public GameTimer(int seconds) {
        count = seconds + 1;
	
		
		sc = (SettingsController) Settings.object();
		
		tmrDisplay = new JLabel("...");
		
		if(sc.isNormal()) {
			tmrDisplay.setForeground(sc.normal10);
		}
		if(sc.isDeuteranope()) {
			tmrDisplay.setForeground(sc.deut10);
		}
		if(sc.isProtanope()) {
			tmrDisplay.setForeground(sc.prot10);
		}
		if(sc.isTritanope()) {
			tmrDisplay.setForeground(sc.trit10);
		}
		tmrDisplay.setFont(new Font("Chalkduster", Font.BOLD, 17));
		add(tmrDisplay);
		setTimer(new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				count--;
				if (count < 10 && count >= 0) {
					tmrDisplay.setText("0:0"+Integer.toString(count));
				}else if(count < 60 && count >= 0) {
					tmrDisplay.setText("0:"+Integer.toString(count));
				}else if(count == 60) {
					//tmrDisplay.setForeground(Color.RED);
					tmrDisplay.setText("1:00");
				}else {
					((Timer) (e.getSource())).stop();
				}
			}
		}));
		getTimer().setInitialDelay(0);
		getTimer().start();
	}
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(200,200);
	}
	public Timer getTimer() {
		return timer;
	}
	public void setTimer(Timer timer) {
		this.timer = timer;
	}
		

}
