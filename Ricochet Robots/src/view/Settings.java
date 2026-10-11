package view;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;

import javax.swing.*;

import controller.*;

import java.util.*;

public class Settings extends JFrame implements ActionListener {

	JFrame frmSettings;
	JRadioButton rdbtnEasy, rdbtnDifficult, rdbtnSimple, rdbtnComplex, rdbtnEnable, rdbtnDisable;
	JCheckBox chckbxPlayer1, chckbxPlayer2, chckboxPlayer3, chckbxPlayer4;
	JButton btnBack;
	static SettingsController sc;
	
	public Settings() {
		//All Classes used in this class
		GameBoard gameSimple = new GameBoard();
		GameBoardComplex gameComplex = new GameBoardComplex();
		sc = new SettingsController();
		
		frmSettings = new JFrame();
		frmSettings.setTitle("Settings");
		frmSettings.setBounds(100, 100, 600, 600);
		frmSettings.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Back Button
		
		ImageIcon img=new ImageIcon(this.getClass().getResource("/Border.jpg"));
		btnBack = new JButton("Cancel");
		btnBack.setBackground(new Color(249,112,104));
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new MainMenu();
				frmSettings.setVisible(false);
			}
		});
		
		btnBack.setForeground(Color.BLACK);
		btnBack.setBorderPainted(false);
		btnBack.setIcon(new ImageIcon(this.getClass().getResource("/back.png")));
		btnBack.setBounds(246,510,93,28);
		frmSettings.getContentPane().add(btnBack);
		
		
		
		//Color Buttons
		
		JRadioButton rdbtnNormal = new JRadioButton("Normal");
		rdbtnNormal.setForeground(sc.normal1);
		rdbtnNormal.setFont(new Font("Chalkduster", Font.BOLD, 15));
		rdbtnNormal.setBounds(116, 311, 125, 23);
		frmSettings.getContentPane().add(rdbtnNormal);
		
		JRadioButton rdbtnDeut = new JRadioButton("Deuteranope");
		rdbtnDeut.setForeground(sc.deut1);
		rdbtnDeut.setFont(new Font("Chalkduster", Font.BOLD, 15));
		rdbtnDeut.setForeground(sc.deut4);
		rdbtnDeut.setBounds(246, 311, 150, 23);
		frmSettings.getContentPane().add(rdbtnDeut);
		
		JRadioButton rdbtnProt = new JRadioButton("Protanope");
		rdbtnProt.setForeground(sc.prot1);
		rdbtnProt.setFont(new Font("Chalkduster", Font.BOLD, 15));
		rdbtnProt.setBounds(116, 342, 125, 23);
		frmSettings.getContentPane().add(rdbtnProt);
		
		JRadioButton rdbtnTrit = new JRadioButton("Tritanope ");
		rdbtnTrit.setFont(new Font("Chalkduster", Font.BOLD, 15));
		rdbtnTrit.setForeground(sc.trit1);
		rdbtnTrit.setBounds(246, 342, 150, 23);
		frmSettings.getContentPane().add(rdbtnTrit);
		
		// Board Buttons
		
		JRadioButton rdbtnSimple = new JRadioButton("SIMPLE");
		rdbtnSimple.setFont(new Font("Chalkduster", Font.BOLD, 15));
		rdbtnSimple.setHorizontalAlignment(SwingConstants.LEFT);
		rdbtnSimple.setBounds(116, 261, 109, 23);
		rdbtnSimple.setForeground(new Color(169,72,152));
		frmSettings.getContentPane().add(rdbtnSimple);
		
		JRadioButton rdbtnComplex = new JRadioButton("COMPLEX");
		rdbtnComplex.setFont(new Font("Chalkduster", Font.BOLD, 15));
		rdbtnComplex.setHorizontalAlignment(SwingConstants.LEFT);
		rdbtnComplex.setBounds(246, 261, 109, 23);
		rdbtnComplex.setForeground(new Color(73,170,153));
		frmSettings.getContentPane().add(rdbtnComplex);
		
		// labels
		JLabel lblNewLabel = new JLabel("SETTINGS");
		lblNewLabel.setForeground(new Color(20,12,28));
		lblNewLabel.setFont(new Font("Noteworthy", Font.BOLD, 18));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(226, 69, 129, 24);
		frmSettings.getContentPane().add(lblNewLabel);
		
		JLabel lblBoard = new JLabel("Board:");
		lblBoard.setForeground(new Color(20,12,28));
		lblBoard.setFont(new Font("Noteworthy", Font.BOLD, 15));
		lblBoard.setBounds(48, 225, 46, 25);
		frmSettings.getContentPane().add(lblBoard);
		
		JLabel lblColor = new JLabel("Color:");
		lblColor.setFont(new Font("Noteworthy", Font.BOLD, 15));
		lblColor.setForeground(new Color(20,12,28));
		lblColor.setBounds(48, 301, 46, 25);
		frmSettings.getContentPane().add(lblColor);
		
		JLabel lblHint = new JLabel("Hint:");
		lblHint.setFont(new Font("Noteworthy", Font.BOLD, 15));
		lblHint.setBounds(48, 364, 46, 25);
		lblHint.setForeground(new Color(20,12,28));
		frmSettings.getContentPane().add(lblHint);
		
		// Hints enabled button
		
		JRadioButton rdbtnEnable = new JRadioButton("ENABLE");
		rdbtnEnable.setFont(new Font("Chalkduster", Font.BOLD, 15));
		rdbtnEnable.setBounds(116, 397, 109, 25);
		rdbtnEnable.setForeground(new Color(25,118,54));
		frmSettings.getContentPane().add(rdbtnEnable);
		
		JRadioButton rdbtnDisable = new JRadioButton("DISABLE");
		rdbtnDisable.setFont(new Font("Chalkduster", Font.BOLD, 15));
		rdbtnDisable.setBounds(246, 397, 109, 25);
		rdbtnDisable.setForeground(new Color(135,37,85));
		frmSettings.getContentPane().add(rdbtnDisable);
		
		// Start Button
		
		RoundButton btnStart = new RoundButton("PLAY");
		btnStart.setFont(new Font("Chalkduster", Font.BOLD, 20));
		btnStart.setForeground(new Color(255,255,255));
		btnStart.setBackground(new Color(139,205,237));
		//btnStart.setOpaque(true);
		btnStart.setBorderPainted(false);
		btnStart.setBounds(197, 448, 192, 51);
		frmSettings.getContentPane().add(btnStart);
		
		// Player Checkboxes
		
		JCheckBox chckbxPlayer4 = new JCheckBox("PLAYER 4");
		chckbxPlayer4.setForeground(sc.normal8);
		chckbxPlayer4.setFont(new Font("Chalkduster", Font.BOLD, 13));
		chckbxPlayer4.setBounds(425, 108, 110, 25);
		frmSettings.getContentPane().add(chckbxPlayer4);
		
		JCheckBox chckbxPlayer3 = new JCheckBox("PLAYER 3");
		chckbxPlayer3.setForeground(sc.normal7);
		chckbxPlayer3.setFont(new Font("Chalkduster", Font.BOLD, 13));
		chckbxPlayer3.setBounds(300, 108, 110, 25);
		frmSettings.getContentPane().add(chckbxPlayer3);
		
		JCheckBox chckbxPlayer2 = new JCheckBox("PLAYER 2");
		chckbxPlayer2.setForeground(sc.normal6);
		chckbxPlayer2.setFont(new Font("Chalkduster", Font.BOLD, 13));
		chckbxPlayer2.setBounds(175, 108, 110, 25);
		frmSettings.getContentPane().add(chckbxPlayer2);
		
		JCheckBox chckbxPlayer1 = new JCheckBox("PLAYER 1");
		chckbxPlayer1.setForeground(sc.normal5);
		chckbxPlayer1.setFont(new Font("Chalkduster", Font.BOLD, 13));
		chckbxPlayer1.setBounds(50, 108, 110, 25);
		frmSettings.getContentPane().add(chckbxPlayer1);
		
		
		/*
		 * This method closes the settings frame & creates the Game Gui
		 */
		
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
                if((chckbxPlayer1.isSelected() || chckbxPlayer2.isSelected() || chckbxPlayer3.isSelected() || chckbxPlayer4.isSelected())&&(rdbtnSimple.isSelected() || rdbtnComplex.isSelected())&&(rdbtnEnable.isSelected() || rdbtnDisable.isSelected())&&(rdbtnNormal.isSelected() || rdbtnDeut.isSelected() || rdbtnProt.isSelected() || rdbtnTrit.isSelected())){
					
					if(chckbxPlayer1.isSelected()) {
						sc.setPlayer1(true);
					}
					if(chckbxPlayer2.isSelected()) {
						sc.setPlayer2(true);
					}
					if(chckbxPlayer3.isSelected()) {
						sc.setPlayer3(true);
					}
					if(chckbxPlayer4.isSelected()) {
						sc.setPlayer4(true);
					}
					
					if(rdbtnEnable.isSelected()) {
						sc.setHintsEnabled(true);
					}
					if(rdbtnDisable.isSelected()) {
						sc.setHintsEnabled(false);
					}
					if(rdbtnNormal.isSelected()) {sc.setNormal(true);}
					if(rdbtnDeut.isSelected()) {sc.setDeuteranope(true);;}
					if(rdbtnProt.isSelected()) {sc.setProtanope(true);;}
					if(rdbtnTrit.isSelected()) {sc.setTritanope(true);;}
                    sc.setComplex(rdbtnComplex.isSelected());
                    if(rdbtnSimple.isSelected()) {
                        frmSettings.setVisible(false);
                        gameSimple.start();
                    }
                    if(rdbtnComplex.isSelected()) {
                        frmSettings.setVisible(false);
                        gameComplex.start();
                    }

				}
				else {
					JOptionPane.showMessageDialog(frmSettings, "Each setting must be selected");
				}
				
			}
		});
		
		
		//Background has to be the last, if not the other components won't show
		JLabel background; 
		frmSettings.getContentPane().setLayout(null);
		background = new JLabel("",img,JLabel.CENTER);
		background.setBackground(Color.WHITE);
		background.setFont(new Font("Chalkduster", Font.BOLD, 13));
		background.setBounds(0, 0, 584, 561);
		frmSettings.getContentPane().add(background);
		
		frmSettings.setVisible(true);
		
		
		//Action Listeners for each component
		
		rdbtnSimple.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(rdbtnSimple.isSelected()) {
					
					rdbtnComplex.setSelected(false);
					
				}
				else {
					rdbtnComplex.setSelected(true);
				}
				
			}
		});
		
		rdbtnComplex.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(rdbtnComplex.isSelected()) {
					
					rdbtnSimple.setSelected(false);
					
				}
				else {
					rdbtnSimple.setSelected(true);
				}
				
			}
		});
		
		rdbtnEnable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(rdbtnEnable.isSelected()) {
					rdbtnDisable.setSelected(false);
				}
				else {
					rdbtnDisable.setSelected(true);
				}
				
			}
		});
		rdbtnDisable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnDisable.isSelected()) {
					rdbtnEnable.setSelected(false);
					
				}
				else {
					rdbtnEnable.setSelected(true);
				}
			}
		});
		
		rdbtnNormal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnNormal.isSelected()) {
					rdbtnDeut.setSelected(false);
					rdbtnProt.setSelected(false);
					rdbtnTrit.setSelected(false);
				}else {
					rdbtnNormal.setSelected(true);
				}
			}
		});
		rdbtnDeut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnDeut.isSelected()) {
					rdbtnNormal.setSelected(false);
					rdbtnProt.setSelected(false);
					rdbtnTrit.setSelected(false);
				}else {
					rdbtnDeut.setSelected(true);
				}
			}
		});
		rdbtnProt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnProt.isSelected()) {
					rdbtnNormal.setSelected(false);
					rdbtnDeut.setSelected(false);
					rdbtnTrit.setSelected(false);
				}else {
					rdbtnProt.setSelected(true);
				}
			}
		});
		rdbtnTrit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnTrit.isSelected()) {
					rdbtnNormal.setSelected(false);
					rdbtnDeut.setSelected(false);
					rdbtnProt.setSelected(false);
				}else {
					rdbtnTrit.setSelected(true);
				}
			}
		});

	}

	@Override
	public void actionPerformed(ActionEvent aevt) {
		// This is a required method
		
		
	}
	
    public static void use(SettingsController settings) { sc = settings; }

	public static Object object() {
		return sc;
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
