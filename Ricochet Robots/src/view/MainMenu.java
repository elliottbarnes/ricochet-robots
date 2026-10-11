package view;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import controller.*;
import javax.swing.border.Border;
import java.awt.geom.*;

public class MainMenu extends JFrame implements ActionListener {

	private JFrame frmRicochetRobots;
	public MainMenu() {
		
		ImageIcon img=new ImageIcon(this.getClass().getResource("/Background.jpg"));
	    
		frmRicochetRobots = new JFrame();
		frmRicochetRobots.setTitle("Ricochet Robots");
		frmRicochetRobots.getContentPane().setBackground(Color.GRAY);
		frmRicochetRobots.setBackground(Color.BLACK);
		frmRicochetRobots.setBounds(100, 100, 490, 450);
		frmRicochetRobots.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmRicochetRobots.getContentPane().setLayout(null);
		
		//Button Help
		RoundButton btnHelp = new RoundButton("HELP");
		btnHelp.setFont(new Font("Chalkduster", Font.BOLD, 15));
		btnHelp.setBackground(new Color(208,70,72));
		btnHelp.setForeground(new Color(218,212,94));
		btnHelp.setBounds(149, 309, 200, 50);
		//btnHelp.setOpaque(true);
		btnHelp.setBorderPainted(false);
		frmRicochetRobots.getContentPane().add(btnHelp);
		btnHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Help();
				frmRicochetRobots.setVisible(false);
			}
		});
		
		//Button Load Game
		RoundButton btnLoadGame = new RoundButton("LOAD GAME");
		//btnLoadGame.setOpaque(true);
		btnLoadGame.setBorderPainted(false);
		btnLoadGame.setFont(new Font("Chalkduster", Font.BOLD, 15));
		btnLoadGame.setBackground(new Color(208,70,72));
		btnLoadGame.setForeground(new Color(218,212,94));
		btnLoadGame.setBounds(149, 248, 200, 50);
		frmRicochetRobots.getContentPane().add(btnLoadGame);
		btnLoadGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
                JFileChooser picker = new JFileChooser(new java.io.File("."));
                picker.setDialogTitle("Load Ricochet game");
                picker.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Ricochet saves (*.rrsave)", "rrsave"));
                if (picker.showOpenDialog(frmRicochetRobots) != JFileChooser.APPROVE_OPTION) return;
                try {
                    SaveData data = ResourceManager.load(picker.getSelectedFile().getPath());
                    // Construct and validate the board before changing global settings or closing the menu.
                    Runnable start;
                    if (data.complex()) {
                        GameBoardComplex board = new GameBoardComplex(data); start = board::start;
                    } else {
                        GameBoard board = new GameBoard(data); start = board::start;
                    }
                    Settings.use(data.settings());
                    start.run();
                    frmRicochetRobots.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frmRicochetRobots, "Unable to load: " + ex.getMessage(), "Load game", JOptionPane.ERROR_MESSAGE);
                }
				
			}
		});
		
		//Button New Game
		RoundButton btnNewGame = new RoundButton("NEW GAME");
		btnNewGame.setFont(new Font("Chalkduster", Font.BOLD, 15));
		btnNewGame.setForeground(new Color(218,212,94));
		btnNewGame.setBackground(new Color(208,70,72));
		//btnNewGame.setOpaque(true);
		btnNewGame.setBorderPainted(false);
		btnNewGame.setBounds(149, 187, 200, 50);
		frmRicochetRobots.getContentPane().add(btnNewGame);
		btnNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Settings();
				frmRicochetRobots.setVisible(false);
			}
		});
		
		JLabel background; 
		background = new JLabel("",img,JLabel.CENTER);
		background.setBounds(0, 0, 490, 450);
		frmRicochetRobots.getContentPane().add(background);
	
		frmRicochetRobots.setVisible(true);	

	}
	@Override
	public void actionPerformed(ActionEvent e) {
		//This is a required method
	}
	class RoundButton extends JButton
	{
	   // for mouse detection purposes
	   Shape shape;
	 
	   public RoundButton(String label) {
	      super(label);
	      // allows us to paint a round background
	      // if true, it would be rectangular
	      setContentAreaFilled(false);
	   }
	  
	   protected void paintComponent(Graphics g) {
	      // if the button is pressed and ready to be released
	      if (getModel().isArmed()) {
	         g.setColor(Color.lightGray);
	      } else {
	         g.setColor(getBackground());
	      }
	  
	      g.fillOval(0, 0, getSize().width-1, getSize().height-1);
	  
	      super.paintComponent(g);
	   }
	  
	   // paint a round border as opposed to a rectangular one
	   protected void paintBorder(Graphics g) {
	      g.setColor(getForeground());
	      g.drawOval(0, 0, getSize().width-1, getSize().height-1);
	   }
	  
	   // only clicks within the round shape should be accepted
	   public boolean contains(int x, int y) {
	      if (shape == null || !shape.getBounds().equals(getBounds())) {
	         shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
	      }
	  
	      return shape.contains(x, y);
	   }
	}
	
	

}
