package view;

import java.awt.*;


import javax.swing.*;

import controller.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class GameGUI extends JFrame implements MouseListener, WindowListener {
	
	private JFrame frmTakeTurn;
	private Canvas canvas;
	private JButton btnP1, btnP2, btnP3, btnP4;
	private GameTimer gameTmr;
	private JTextField textField;
	private JLabel lblP1, lblP2, lblP3, lblP4,lblp1bid, lblp2bid, lblp3bid, lblp4bid, lblp1score, lblp2score, lblp3score, lblp4score;
	private JRadioButton rdbtnRobot1, rdbtnRobot2, rdbtnRobot3, rdbtnRobot4;
	private JPanel leftPanel, rightPanel, bottomPanel;
	private boolean flag;
	private BidController bidController;
	private int player, p1s, p2s, p3s, p4s;
	private SettingsController  sc;
	private Handler handler;
	private Move move;
	private ChangeToken next;
	private int moves;
    private JButton btnUp, btnDown, btnLeft, btnRight, btnToken;
    private JLabel lblMoveCount;
    private JRadioButton rdbtnP1, rdbtnP2, rdbtnP3, rdbtnP4;
	
    public GameGUI() { this(new Handler(), null); }
    public GameGUI(Handler board, SaveData saved) {
        handler = board;
        Display();
        if (saved != null) restoreSession(saved);
    }
    public boolean isGameOpen() { return frmTakeTurn.isDisplayable(); }
	
	public void Display() {
		
		//All classes needed in this class
		sc = (SettingsController) Settings.object();
		bidController = new BidController();
		next = new ChangeToken();
		flag = false;
		
		//Creating all frames and dividing the screen
		frmTakeTurn = new JFrame();
		frmTakeTurn.setFont(new Font("Chalkduster", Font.BOLD, 12));
		makeMenuBar(frmTakeTurn);
		frmTakeTurn.setResizable(false);
		frmTakeTurn.setLocationRelativeTo(null);

		frmTakeTurn.setTitle("Ricochet Robots");
		frmTakeTurn.setBounds(100, 100, 800, 700);
		//frmTakeTurn.setDefaultCloseOperation(new ExitGamePanel());
		frmTakeTurn.getContentPane().setLayout(null);
		
		// Exit confirmation dialogue asking user to play a new game
		
        frmTakeTurn.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frmTakeTurn.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { returnToMenu(); }
        });


		//Left Panel
		
		leftPanel = new JPanel();
		leftPanel.setLocation(0, 0);
		frmTakeTurn.getContentPane().add(leftPanel);
		leftPanel.setSize(600, 600);
		
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(600,600));
		canvas.setMaximumSize(new Dimension(600,600));
		canvas.setMinimumSize(new Dimension(600,600));
		leftPanel.add(canvas);
		
		
		
		//Right Panel
		
		rightPanel = new JPanel();
		rightPanel.setForeground(new Color(51, 204, 255));
		rightPanel.setLocation(600, 0);
		frmTakeTurn.getContentPane().add(rightPanel);
		rightPanel.setSize(200, 600);
		rightPanel.setLayout(null);
		
		
		JButton btnBid = new JButton("BID");
		btnBid.setForeground(new Color(75,0,146));
		btnBid.setFont(new Font("Chalkduster", Font.BOLD, 15));
		btnBid.setBounds(50, 249, 81, 50);
		rightPanel.add(btnBid);
		
		btnP1 = new JButton("P1");
		btnP1.setForeground(new Color(255,51,51));
		btnP1.setFont(new Font("Chalkduster", Font.BOLD, 13));
		btnP1.setBounds(32, 137, 55, 23);
		rightPanel.add(btnP1);
		
		btnP2 = new JButton("P2");
		btnP2.setForeground(new Color(93,58,155));
		btnP2.setFont(new Font("Chalkduster", Font.BOLD, 13));
		btnP2.setBounds(32, 171, 55, 23);
		rightPanel.add(btnP2);
		
		btnP3 = new JButton("P3");
		btnP3.setForeground(new Color(26,133,255));
		btnP3.setFont(new Font("Chalkduster", Font.BOLD, 13));
		btnP3.setBounds(102, 137, 55, 23);
		rightPanel.add(btnP3);
		
		btnP4 = new JButton("P4");
		btnP4.setForeground(new Color(212,17,89));
		btnP4.setFont(new Font("Chalkduster", Font.BOLD, 13));
		btnP4.setBounds(102, 171, 55, 23);
		rightPanel.add(btnP4);

		btnP1.setEnabled(false);
		btnP2.setEnabled(false);
		btnP3.setEnabled(false);
		btnP4.setEnabled(false);
		
		textField = new JTextField();
		textField.setFont(new Font("Chalkduster", Font.BOLD, 13));
		textField.setEnabled(false);
		textField.setVisible(false);
		textField.setBounds(50, 217, 81, 20);
		
		lblP1 = new JLabel("P1");
		lblP1.setForeground(new Color(255,51,51));
		lblP1.setFont(new Font("Chalkduster", Font.BOLD, 15));
		lblP1.setBounds(10, 386, 25, 25);
		rightPanel.add(lblP1);
		
		lblP2 = new JLabel("P2");
		lblP2.setForeground(new Color(93,58,155));
		lblP2.setFont(new Font("Chalkduster", Font.BOLD, 15));
		lblP2.setBounds(10, 436, 25, 25);
		rightPanel.add(lblP2);
		
		lblP3 = new JLabel("P3");
		lblP3.setForeground(new Color(26,133,255));
		lblP3.setFont(new Font("Chalkduster", Font.BOLD, 15));
		lblP3.setBounds(10, 486, 25, 25);
		rightPanel.add(lblP3);
		
		lblP4 = new JLabel("P4");
		lblP4.setForeground(new Color(212,17,89));
		lblP4.setFont(new Font("Chalkduster", Font.BOLD, 15));
		lblP4.setBounds(10, 536, 25, 25);
		rightPanel.add(lblP4);
		
		lblp1bid = new JLabel("0");
		lblp1bid.setHorizontalAlignment(SwingConstants.CENTER);
		lblp1bid.setForeground(new Color(255,51,51));
		lblp1bid.setFont(new Font("Chalkduster", Font.BOLD, 15));
		lblp1bid.setBounds(55, 386, 20, 25);
		rightPanel.add(lblp1bid);
		
		lblp2bid = new JLabel("0");
		lblp2bid.setHorizontalAlignment(SwingConstants.CENTER);
		lblp2bid.setForeground(new Color(93,58,155));
		lblp2bid.setFont(new Font("Chalkduster", Font.BOLD, 15));
		lblp2bid.setBounds(55, 436, 20, 25);
		rightPanel.add(lblp2bid);
		
		lblp3bid = new JLabel("0");
		lblp3bid.setHorizontalAlignment(SwingConstants.CENTER);
		lblp3bid.setForeground(new Color(26,133,255));
		lblp3bid.setFont(new Font("Chalkduster", Font.BOLD, 15));
		lblp3bid.setBounds(55, 486, 20, 25);
		rightPanel.add(lblp3bid);
		
		lblp4bid = new JLabel("0");
		lblp4bid.setHorizontalAlignment(SwingConstants.CENTER);
		lblp4bid.setForeground(new Color(212,17,89));
		lblp4bid.setFont(new Font("Chalkduster", Font.BOLD, 15));
		lblp4bid.setBounds(55, 536, 20, 25);
		rightPanel.add(lblp4bid);
		
		rightPanel.add(textField);
		textField.setColumns(10);
		
		JLabel lbl_BIDS = new JLabel("BIDS");
		lbl_BIDS.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_BIDS.setForeground(new Color(220,50,32));
		lbl_BIDS.setFont(new Font("Chalkduster", Font.BOLD, 15));
		lbl_BIDS.setBounds(35, 339, 60, 35);
		rightPanel.add(lbl_BIDS);
		
		JLabel lblMoves = new JLabel("Moves:");
		lblMoves.setFont(new Font("Chalkduster", Font.BOLD, 13));
		lblMoves.setBounds(32, 60, 50, 20);
		rightPanel.add(lblMoves);
		
        lblMoveCount = new JLabel("0");
		lblMoveCount.setFont(new Font("Chalkduster", Font.BOLD, 13));
		lblMoveCount.setBounds(102, 60, 46, 20);
		rightPanel.add(lblMoveCount);
		
        btnToken = new JButton("Token");
		btnToken.setFont(new Font("Chalkduster", Font.BOLD, 13));
		btnToken.setBounds(50, 100, 89, 23);
		rightPanel.add(btnToken);
		
		JLabel lblNewLabel = new JLabel("|");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(90, 434, 15, 20);
		rightPanel.add(lblNewLabel);
		
		JLabel label = new JLabel("|");
		label.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(90, 448, 15, 20);
		rightPanel.add(label);
		
		JLabel label_1 = new JLabel("|");
		label_1.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setBounds(90, 462, 15, 20);
		rightPanel.add(label_1);
		
		JLabel label_2 = new JLabel("|");
		label_2.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setBounds(90, 476, 15, 20);
		rightPanel.add(label_2);
		
		JLabel label_3 = new JLabel("|");
		label_3.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		label_3.setHorizontalAlignment(SwingConstants.CENTER);
		label_3.setBounds(90, 490, 15, 20);
		rightPanel.add(label_3);
		
		JLabel label_4 = new JLabel("|");
		label_4.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		label_4.setHorizontalAlignment(SwingConstants.CENTER);
		label_4.setBounds(90, 504, 15, 20);
		rightPanel.add(label_4);
		
		JLabel label_5 = new JLabel("|");
		label_5.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		label_5.setHorizontalAlignment(SwingConstants.CENTER);
		label_5.setBounds(90, 518, 15, 20);
		rightPanel.add(label_5);
		
		JLabel label_6 = new JLabel("|");
		label_6.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		label_6.setHorizontalAlignment(SwingConstants.CENTER);
		label_6.setBounds(90, 532, 15, 20);
		rightPanel.add(label_6);
		
		JLabel label_7 = new JLabel("|");
		label_7.setHorizontalAlignment(SwingConstants.CENTER);
		label_7.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		label_7.setBounds(90, 541, 15, 20);
		rightPanel.add(label_7);
		
		JLabel label_8 = new JLabel("|");
		label_8.setHorizontalAlignment(SwingConstants.CENTER);
		label_8.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		label_8.setBounds(90, 555, 15, 20);
		rightPanel.add(label_8);
		
		JLabel label_9 = new JLabel("|");
		label_9.setHorizontalAlignment(SwingConstants.CENTER);
		label_9.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		label_9.setBounds(90, 434, 15, 20);
		rightPanel.add(label_9);
		
		JLabel label_10 = new JLabel("|");
		label_10.setHorizontalAlignment(SwingConstants.CENTER);
		label_10.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		label_10.setBounds(90, 420, 15, 20);
		rightPanel.add(label_10);
		
		JLabel label_11 = new JLabel("|");
		label_11.setHorizontalAlignment(SwingConstants.CENTER);
		label_11.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		label_11.setBounds(90, 406, 15, 20);
		rightPanel.add(label_11);
		
		JLabel label_12 = new JLabel("|");
		label_12.setHorizontalAlignment(SwingConstants.CENTER);
		label_12.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		label_12.setBounds(90, 392, 15, 20);
		rightPanel.add(label_12);
		
		JLabel label_13 = new JLabel("|");
		label_13.setHorizontalAlignment(SwingConstants.CENTER);
		label_13.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		label_13.setBounds(90, 386, 15, 20);
		rightPanel.add(label_13);
		
		JLabel lbl_SCORE = new JLabel("SCORES");
		lbl_SCORE.setHorizontalAlignment(SwingConstants.CENTER);
		lbl_SCORE.setForeground(new Color(0, 0, 205));
		lbl_SCORE.setFont(new Font("Chalkduster", Font.BOLD, 15));
		lbl_SCORE.setBounds(105, 339, 65, 35);
		rightPanel.add(lbl_SCORE);
		
		lblp1score = new JLabel("0");
		lblp1score.setHorizontalAlignment(SwingConstants.CENTER);
		lblp1score.setForeground(new Color(255,51,51));
		lblp1score.setFont(new Font("Chalkduster", Font.BOLD, 15));
		lblp1score.setBounds(125, 386, 20, 25);
		rightPanel.add(lblp1score);
		
		lblp2score = new JLabel("0");
		lblp2score.setHorizontalAlignment(SwingConstants.CENTER);
		lblp2score.setForeground(new Color(93, 58, 155));
		lblp2score.setFont(new Font("Chalkduster", Font.BOLD, 15));
		lblp2score.setBounds(125, 436, 20, 25);
		rightPanel.add(lblp2score);
		
		lblp3score = new JLabel("0");
		lblp3score.setHorizontalAlignment(SwingConstants.CENTER);
		lblp3score.setForeground(new Color(26, 133, 255));
		lblp3score.setFont(new Font("Chalkduster", Font.BOLD, 15));
		lblp3score.setBounds(125, 486, 20, 25);
		rightPanel.add(lblp3score);
		
		lblp4score = new JLabel("0");
		lblp4score.setHorizontalAlignment(SwingConstants.CENTER);
		lblp4score.setForeground(new Color(212, 17, 89));
		lblp4score.setFont(new Font("Chalkduster", Font.BOLD, 15));
		lblp4score.setBounds(125, 536, 20, 25);
		rightPanel.add(lblp4score);
		
		JLabel label_14 = new JLabel("|");
		label_14.setHorizontalAlignment(SwingConstants.CENTER);
		label_14.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		label_14.setBounds(90, 373, 15, 20);
		rightPanel.add(label_14);
		
		JLabel label_15 = new JLabel("|");
		label_15.setHorizontalAlignment(SwingConstants.CENTER);
		label_15.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		label_15.setBounds(90, 361, 15, 20);
		rightPanel.add(label_15);
		
		JLabel lblNewLabel_1 = new JLabel("_____________________");
		lblNewLabel_1.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		lblNewLabel_1.setBounds(28, 360, 170, 15);
		rightPanel.add(lblNewLabel_1);
		
		
		
		//Bottom Panel
		
		bottomPanel = new JPanel();
		
		bottomPanel.setLocation(0, 600);
		frmTakeTurn.getContentPane().add(bottomPanel);
		bottomPanel.setSize(800, 200);
		bottomPanel.setLayout(null);
		
        btnUp = new JButton("UP");
		btnUp.setFont(new Font("Chalkduster", Font.BOLD, 11));
		btnUp.setBounds(10, 7, 75, 30);
		bottomPanel.add(btnUp);
		
        btnDown = new JButton("Down");
		btnDown.setFont(new Font("Chalkduster", Font.BOLD, 11));
		btnDown.setBounds(90, 7, 75, 30);
		bottomPanel.add(btnDown);
		
        btnRight = new JButton("Right");
		btnRight.setFont(new Font("Chalkduster", Font.BOLD, 11));
		btnRight.setBounds(250, 7, 75, 30);
		bottomPanel.add(btnRight);
		
        btnLeft = new JButton("Left");
		btnLeft.setFont(new Font("Chalkduster", Font.BOLD, 11));
		btnLeft.setBounds(170, 7, 75, 30);
		bottomPanel.add(btnLeft);
		
		rdbtnRobot1 = new JRadioButton("R1");
		rdbtnRobot1.setFont(new Font("Chalkduster", Font.BOLD, 11));
		rdbtnRobot1.setBounds(350, 7, 49, 30);
		bottomPanel.add(rdbtnRobot1);
		
		rdbtnRobot2 = new JRadioButton("R2");
		rdbtnRobot2.setFont(new Font("Chalkduster", Font.BOLD, 11));
		rdbtnRobot2.setBounds(400, 7, 49, 30);
		bottomPanel.add(rdbtnRobot2);
		
		rdbtnRobot3 = new JRadioButton("R3");
		rdbtnRobot3.setFont(new Font("Chalkduster", Font.BOLD, 11));
		rdbtnRobot3.setBounds(450, 7, 49, 30);
		bottomPanel.add(rdbtnRobot3);
		
		rdbtnRobot4 = new JRadioButton("R4");
		rdbtnRobot4.setFont(new Font("Chalkduster", Font.BOLD, 10));
		rdbtnRobot4.setBounds(500, 7, 49, 30);
		bottomPanel.add(rdbtnRobot4);
		
		
        rdbtnP1 = new JRadioButton("P1");
		rdbtnP1.setForeground(sc.normal5);
		rdbtnP1.setFont(new Font("Chalkduster", Font.BOLD, 10));
		rdbtnP1.setBounds(575, 7, 52, 26);
		bottomPanel.add(rdbtnP1);
		
        rdbtnP2 = new JRadioButton("P2");
		rdbtnP2.setForeground(new Color(93,58,155));
		rdbtnP2.setFont(new Font("Chalkduster", Font.BOLD, 10));
		rdbtnP2.setBounds(625, 7, 52, 26);
		bottomPanel.add(rdbtnP2);
		
        rdbtnP3 = new JRadioButton("P3");
		rdbtnP3.setForeground(new Color(26,133,255));
		rdbtnP3.setFont(new Font("Chalkduster", Font.BOLD, 11));
		rdbtnP3.setBounds(675, 7, 52, 26);
		bottomPanel.add(rdbtnP3);
		
        rdbtnP4 = new JRadioButton("P4");
		rdbtnP4.setForeground(new Color(212,17,89));
		rdbtnP4.setFont(new Font("Chalkduster", Font.BOLD, 11));
		rdbtnP4.setBounds(721, 7, 52, 26);
		bottomPanel.add(rdbtnP4);
		
		if(sc.isPlayer1()) {rdbtnP1.setEnabled(true);} else {rdbtnP1.setEnabled(false);}
		if(sc.isPlayer2()) {rdbtnP2.setEnabled(true);} else {rdbtnP2.setEnabled(false);}
		if(sc.isPlayer3()) {rdbtnP3.setEnabled(true);} else {rdbtnP3.setEnabled(false);}
		if(sc.isPlayer4()) {rdbtnP4.setEnabled(true);} else {rdbtnP4.setEnabled(false);}
		
		// reset timer
		
		JButton btnResetTmr = new JButton("RESET");
		btnResetTmr.setFont(new Font("Chalkduster", Font.BOLD, 13));
		btnResetTmr.setBounds(125, 19, 75, 29);
		rightPanel.add(btnResetTmr);
		btnResetTmr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Timer();
			}
		});
		
		//set color of game components 
		
				if(sc.isNormal()) {
					
					rdbtnRobot1.setForeground(sc.normal1);
				
					rdbtnRobot2.setForeground(sc.normal2);
				
					rdbtnRobot3.setForeground(sc.normal3);
				
					rdbtnRobot4.setForeground(sc.normal4);
					
					btnP1.setForeground(sc.normal5);
					lblP1.setForeground(sc.normal5);
					lblp1bid.setForeground(sc.normal5);
					rdbtnP1.setForeground(sc.normal5);
					lblp1score.setForeground(sc.normal5);

					btnP2.setForeground(sc.normal6);
					lblP2.setForeground(sc.normal6);
					lblp2bid.setForeground(sc.normal6);
					rdbtnP2.setForeground(sc.normal6);
					lblp2score.setForeground(sc.normal6);

					
					btnP3.setForeground(sc.normal7);
					lblP3.setForeground(sc.normal7);
					lblp3bid.setForeground(sc.normal7);
					rdbtnP3.setForeground(sc.normal7);
					lblp3score.setForeground(sc.normal7);

					
					btnP4.setForeground(sc.normal8);
					lblP4.setForeground(sc.normal8);
					lblp4bid.setForeground(sc.normal8);
					rdbtnP4.setForeground(sc.normal8);
					lblp4score.setForeground(sc.normal8);
					btnResetTmr.setForeground(sc.normal9);
					btnBid.setForeground(sc.normal15);
					lbl_BIDS.setForeground(sc.normal16);
					lbl_SCORE.setForeground(sc.normal17);
					btnToken.setForeground(sc.normal18);
					
				}
				if(sc.isDeuteranope()) {
				
					rdbtnRobot1.setForeground(sc.deut1);
				
					rdbtnRobot2.setForeground(sc.deut2);
				
					rdbtnRobot3.setForeground(sc.deut3);
				
					rdbtnRobot4.setForeground(sc.deut4);
					
					btnP1.setForeground(sc.deut5);
					lblP1.setForeground(sc.deut5);
					lblp1bid.setForeground(sc.deut5);
					rdbtnP1.setForeground(sc.deut5);
					lblp1score.setForeground(sc.deut5);

					btnP2.setForeground(sc.deut6);
					lblP2.setForeground(sc.deut6);
					lblp2bid.setForeground(sc.deut6);
					rdbtnP2.setForeground(sc.deut6);
					lblp2score.setForeground(sc.deut6);

					
					btnP3.setForeground(sc.deut7);
					lblP3.setForeground(sc.deut7);
					lblp3bid.setForeground(sc.deut7);
					rdbtnP3.setForeground(sc.deut7);
					lblp3score.setForeground(sc.deut7);

					
					btnP4.setForeground(sc.deut8);
					lblP4.setForeground(sc.deut8);
					lblp4bid.setForeground(sc.deut8);
					rdbtnP4.setForeground(sc.deut8);
					lblp4score.setForeground(sc.deut8);
					btnResetTmr.setForeground(sc.deut9);
					btnBid.setForeground(sc.deut15);
					lbl_BIDS.setForeground(sc.deut16);
					lbl_SCORE.setForeground(sc.deut17);
					btnToken.setForeground(sc.deut18);
					
				
				}
				if(sc.isProtanope()) {
					
					rdbtnRobot1.setForeground(sc.prot1);
				
					rdbtnRobot2.setForeground(sc.prot2);
				
					rdbtnRobot3.setForeground(sc.prot3);
				
					rdbtnRobot4.setForeground(sc.prot4);
					
					btnP1.setForeground(sc.prot5);
					lblP1.setForeground(sc.prot5);
					lblp1bid.setForeground(sc.prot5);
					rdbtnP1.setForeground(sc.prot5);
					lblp1score.setForeground(sc.prot5);

					btnP2.setForeground(sc.prot6);
					lblP2.setForeground(sc.prot6);
					lblp2bid.setForeground(sc.prot6);
					rdbtnP2.setForeground(sc.prot6);
					lblp2score.setForeground(sc.prot6);

					
					btnP3.setForeground(sc.prot7);
					lblP3.setForeground(sc.prot7);
					lblp3bid.setForeground(sc.prot7);
					rdbtnP3.setForeground(sc.prot7);
					lblp3score.setForeground(sc.prot7);

					
					btnP4.setForeground(sc.prot8);
					lblP4.setForeground(sc.prot8);
					lblp4bid.setForeground(sc.prot8);
					rdbtnP4.setForeground(sc.prot8);
					lblp4score.setForeground(sc.prot8);
					btnResetTmr.setForeground(sc.prot9);
					btnBid.setForeground(sc.prot15);
					lbl_BIDS.setForeground(sc.prot16);
					lbl_SCORE.setForeground(sc.prot17);
					btnToken.setForeground(sc.prot18);
				
				}
				if(sc.isTritanope()) {

					rdbtnRobot1.setForeground(sc.trit1);

					rdbtnRobot2.setForeground(sc.trit2);

					rdbtnRobot3.setForeground(sc.trit3);

					rdbtnRobot4.setForeground(sc.trit4);
					
					btnP1.setForeground(sc.trit5);
					lblP1.setForeground(sc.trit5);
					lblp1bid.setForeground(sc.trit5);
					rdbtnP1.setForeground(sc.trit5);
					lblp1score.setForeground(sc.trit5);

					btnP2.setForeground(sc.trit6);
					lblP2.setForeground(sc.trit6);
					lblp2bid.setForeground(sc.trit6);
					rdbtnP2.setForeground(sc.trit6);
					lblp2score.setForeground(sc.trit6);

					
					btnP3.setForeground(sc.trit7);
					lblP3.setForeground(sc.trit7);
					lblp3bid.setForeground(sc.trit7);
					rdbtnP3.setForeground(sc.trit7);
					lblp3score.setForeground(sc.trit7);

					
					btnP4.setForeground(sc.trit8);
					lblP4.setForeground(sc.trit8);
					lblp4bid.setForeground(sc.trit8);
					rdbtnP4.setForeground(sc.trit8);
					lblp4score.setForeground(sc.trit8);
					btnResetTmr.setForeground(sc.trit9);
					btnBid.setForeground(sc.trit15);
					lbl_BIDS.setForeground(sc.trit16);
					lbl_SCORE.setForeground(sc.trit17);
					btnToken.setForeground(sc.trit18);
					

				}
		
		//End of frame
		
		frmTakeTurn.setVisible(true);
		
		//All buttons' action listeners
		
		btnBid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				textField.setVisible(false);
				textField.setEnabled(false);
				if(getPlayer()==1) {
					setBid("Player1", lblp1bid);
				}
				else if(getPlayer()==2) {
					setBid("Player2", lblp2bid);
				}
				else if(getPlayer()==3) {
					setBid("Player3", lblp3bid);
				}
				else {
					setBid("Player4", lblp4bid);
				}
			}
		});
		
		btnP1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setPlayer(1);
				textField.setVisible(true);
				textField.setEnabled(true);
			}
		});
		
		btnP2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setPlayer(2);
				textField.setVisible(true);
				textField.setEnabled(true);
			}
		});
		
		btnP3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setPlayer(3);
				textField.setVisible(true);
				textField.setEnabled(true);
			}
		});
		
		btnP4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setPlayer(4);
				textField.setVisible(true);
				textField.setEnabled(true);
			}
		});
		
		btnToken.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(sc.isPlayer1()) {
                    btnP1.setEnabled(true);
                }
                if(sc.isPlayer2()) {
                    btnP2.setEnabled(true);
                }
                if(sc.isPlayer3()) {
                    btnP3.setEnabled(true);
                }
                if(sc.isPlayer4()) {
                    btnP4.setEnabled(true);
                }
				next.next(handler, frmTakeTurn);
                resetRound();
				if(next.isFlag()) finalScore();
			}
		});
		
		//BEGIN - Move a robot
		
		btnUp.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
                if(!next.isFlag() && (rdbtnRobot1.isSelected() || rdbtnRobot2.isSelected() || rdbtnRobot3.isSelected() || rdbtnRobot4.isSelected()) && (rdbtnP1.isSelected() || rdbtnP2.isSelected() || rdbtnP3.isSelected() || rdbtnP4.isSelected())) {
					move = new Move(handler, frmTakeTurn, next);
					if(rdbtnP1.isSelected()==true && bidController.compareMoves("Player1", moves)==false) {
						moves +=1;
						lblMoveCount.setText(""+moves+"");
						moveRobotUp(1);
					}else if(rdbtnP2.isSelected()==true && bidController.compareMoves("Player2", moves)==false) {
						moves +=1;
						lblMoveCount.setText(""+moves+"");
						moveRobotUp(2);
					}else if(rdbtnP3.isSelected()==true && bidController.compareMoves("Player3", moves)==false) {
						moves +=1;
						lblMoveCount.setText(""+moves+"");
						moveRobotUp(3);
					}else if(rdbtnP4.isSelected()==true && bidController.compareMoves("Player4", moves)==false) {
						moves +=1;
						lblMoveCount.setText(""+moves+"");
						moveRobotUp(4);
					}else {
						JOptionPane.showMessageDialog(frmTakeTurn, "This Player does not have any more moves");
					}
				}
				else {
					JOptionPane.showMessageDialog(frmTakeTurn, "A Robot and Player needs to be selected");
				}
			}
		});
		
		btnDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                if(!next.isFlag() && (rdbtnRobot1.isSelected() || rdbtnRobot2.isSelected() || rdbtnRobot3.isSelected() || rdbtnRobot4.isSelected()) && (rdbtnP1.isSelected() || rdbtnP2.isSelected() || rdbtnP3.isSelected() || rdbtnP4.isSelected())) {
					move = new Move(handler, frmTakeTurn, next);
					if(rdbtnP1.isSelected()==true && bidController.compareMoves("Player1", moves)==false) {
						moves +=1;
						lblMoveCount.setText(""+moves+"");
						moveRobotDown(1);
					}else if(rdbtnP2.isSelected()==true && bidController.compareMoves("Player2", moves)==false) {
						moves +=1;
						lblMoveCount.setText(""+moves+"");
						moveRobotDown(2);
					}else if(rdbtnP3.isSelected()==true && bidController.compareMoves("Player3", moves)==false) {
						moves +=1;
						lblMoveCount.setText(""+moves+"");
						moveRobotDown(3);
					}else if(rdbtnP4.isSelected()==true && bidController.compareMoves("Player4", moves)==false) {
						moves +=1;
						lblMoveCount.setText(""+moves+"");
						moveRobotDown(4);
					}else {
						JOptionPane.showMessageDialog(frmTakeTurn, "This Player does not have any more moves");
					}
				}else {
					JOptionPane.showMessageDialog(frmTakeTurn, "A Robot and Player needs to be selected");
				}
			}
		});
		
		btnRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                if(!next.isFlag() && (rdbtnRobot1.isSelected() || rdbtnRobot2.isSelected() || rdbtnRobot3.isSelected() || rdbtnRobot4.isSelected()) && (rdbtnP1.isSelected() || rdbtnP2.isSelected() || rdbtnP3.isSelected() || rdbtnP4.isSelected())) {
					move = new Move(handler, frmTakeTurn, next);
					if(rdbtnP1.isSelected()==true && bidController.compareMoves("Player1", moves)==false) {
						moves +=1;
						lblMoveCount.setText(""+moves+"");
						moveRobotRight(1);
					}else if(rdbtnP2.isSelected()==true && bidController.compareMoves("Player2", moves)==false) {
						moves +=1;
						lblMoveCount.setText(""+moves+"");
						moveRobotRight(2);
					}else if(rdbtnP3.isSelected()==true && bidController.compareMoves("Player3", moves)==false) {
						moves +=1;
						lblMoveCount.setText(""+moves+"");
						moveRobotRight(3);
					}else if(rdbtnP4.isSelected()==true && bidController.compareMoves("Player4", moves)==false) {
						moves +=1;
						lblMoveCount.setText(""+moves+"");
						moveRobotRight(4);
					}else {
						JOptionPane.showMessageDialog(frmTakeTurn, "This Player does not have any more moves");
					}
					
				}else {
					JOptionPane.showMessageDialog(frmTakeTurn, "A Robot and Player needs to be selected");
				}
			}
		});
		
		btnLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                if(!next.isFlag() && (rdbtnRobot1.isSelected() || rdbtnRobot2.isSelected() || rdbtnRobot3.isSelected() || rdbtnRobot4.isSelected()) && (rdbtnP1.isSelected() || rdbtnP2.isSelected() || rdbtnP3.isSelected() || rdbtnP4.isSelected())) {
					move = new Move(handler, frmTakeTurn, next);
					if(rdbtnP1.isSelected()==true && bidController.compareMoves("Player1", moves)==false) {
						moves +=1;
						lblMoveCount.setText(""+moves+"");
						moveRobotLeft(1);
					}else if(rdbtnP2.isSelected()==true && bidController.compareMoves("Player2", moves)==false) {
						moves +=1;
						lblMoveCount.setText(""+moves+"");
						moveRobotLeft(2);
					}else if(rdbtnP3.isSelected()==true && bidController.compareMoves("Player3", moves)==false) {
						moves +=1;
						lblMoveCount.setText(""+moves+"");
						moveRobotLeft(3);
					}else if(rdbtnP4.isSelected()==true && bidController.compareMoves("Player4", moves)==false) {
						moves +=1;
						lblMoveCount.setText(""+moves+"");
						moveRobotLeft(4);
					}else {
						JOptionPane.showMessageDialog(frmTakeTurn, "This Player does not have any more moves");
					}
				}else {
					JOptionPane.showMessageDialog(frmTakeTurn, "A Robot and Player needs to be selected");
				}
			}
		});
		
		rdbtnRobot1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnRobot1.isSelected()) {
					rdbtnRobot2.setSelected(false);
					rdbtnRobot3.setSelected(false);
					rdbtnRobot4.setSelected(false);
				}else {
					rdbtnRobot1.setSelected(true);
				}
			}
		});
		
		rdbtnRobot2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnRobot2.isSelected()) {
					rdbtnRobot1.setSelected(false);
					rdbtnRobot3.setSelected(false);
					rdbtnRobot4.setSelected(false);
				}else {
					rdbtnRobot2.setSelected(true);
				}
			}
		});
		
		rdbtnRobot3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnRobot3.isSelected()) {
					rdbtnRobot2.setSelected(false);
					rdbtnRobot1.setSelected(false);
					rdbtnRobot4.setSelected(false);
				}else {
					rdbtnRobot3.setSelected(true);
				}
			}
		});
		
		rdbtnRobot4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnRobot4.isSelected()) {
					rdbtnRobot2.setSelected(false);
					rdbtnRobot3.setSelected(false);
					rdbtnRobot1.setSelected(false);
				}else {
					rdbtnRobot4.setSelected(true);
				}
			}
		});
		
		rdbtnP1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnP1.isSelected()) {
					lblMoveCount.setText("0");
					moves = 0;
					rdbtnP2.setSelected(false);
					rdbtnP3.setSelected(false);
					rdbtnP4.setSelected(false);
				}else {
					rdbtnP1.setSelected(true);
				}
			}
		});
		
		rdbtnP2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnP2.isSelected()) {
					lblMoveCount.setText("0");
					moves = 0;
					rdbtnP1.setSelected(false);
					rdbtnP3.setSelected(false);
					rdbtnP4.setSelected(false);
				}else {
					rdbtnP2.setSelected(true);
				}
			}
		});
		
		rdbtnP3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnP3.isSelected()) {
					lblMoveCount.setText("0");
					moves = 0;
					rdbtnP2.setSelected(false);
					rdbtnP1.setSelected(false);
					rdbtnP4.setSelected(false);
				}else {
					rdbtnP3.setSelected(true);
				}
			}
		});
		
		rdbtnP4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnP4.isSelected()) {
					lblMoveCount.setText("0");
					moves = 0;
					rdbtnP2.setSelected(false);
					rdbtnP3.setSelected(false);
					rdbtnP1.setSelected(false);
				}else {
					rdbtnP4.setSelected(true);
				}
			}
		});
		
		//END - Move a robot
		
		
	
		
	}
	
    private JRadioButton[] robotButtons() { return new JRadioButton[]{rdbtnRobot1,rdbtnRobot2,rdbtnRobot3,rdbtnRobot4}; }
    private JRadioButton[] playerButtons() { return new JRadioButton[]{rdbtnP1,rdbtnP2,rdbtnP3,rdbtnP4}; }
    private int selected(JRadioButton[] buttons) {
        for(int i=0;i<buttons.length;i++) if(buttons[i].isSelected()) return i+1;
        return 0;
    }
    private SaveData snapshot() {
        int[][] positions = SessionState.positions(handler);
        int palette = sc.isDeuteranope()?1:sc.isProtanope()?2:sc.isTritanope()?3:0;
        int players = (sc.isPlayer1()?1:0)|(sc.isPlayer2()?2:0)|(sc.isPlayer3()?4:0)|(sc.isPlayer4()?8:0);
        int[] bids={Integer.parseInt(lblp1bid.getText()),Integer.parseInt(lblp2bid.getText()),Integer.parseInt(lblp3bid.getText()),Integer.parseInt(lblp4bid.getText())};
        return new SaveData(sc.isComplex(),sc.isHintsEnabled(),palette,players,positions[0],positions[1],next.getCurrentToken(),next.isFlag(),
            new int[]{p1s,p2s,p3s,p4s},bids,moves,selected(robotButtons()),selected(playerButtons()),gameTmr==null?-1:gameTmr.remainingSeconds());
    }
    private void saveSession() {
        JFileChooser picker = new JFileChooser(new java.io.File("."));
        picker.setDialogTitle("Save Ricochet game");
        picker.setSelectedFile(new java.io.File("ricochet.rrsave"));
        picker.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Ricochet saves (*.rrsave)","rrsave"));
        if(picker.showSaveDialog(frmTakeTurn)!=JFileChooser.APPROVE_OPTION) return;
        java.io.File file=picker.getSelectedFile();
        if(!file.getName().endsWith(".rrsave")) file=new java.io.File(file.getPath()+".rrsave");
        if(file.exists() && JOptionPane.showConfirmDialog(frmTakeTurn,"Replace this saved game?","Save game",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION) return;
        try {
            ResourceManager.save(snapshot(),file.getPath());
            JOptionPane.showMessageDialog(frmTakeTurn,"Game saved to " + file.getName(),"Save game",JOptionPane.INFORMATION_MESSAGE);
        } catch(Exception ex) {
            JOptionPane.showMessageDialog(frmTakeTurn,"Unable to save: "+ex.getMessage(),"Save game",JOptionPane.ERROR_MESSAGE);
        }
    }
    private void restoreSession(SaveData saved) {
        next.restore(saved.token(),saved.complete(),handler);
        int[] scores=saved.scores(), bids=saved.bids();
        p1s=scores[0];p2s=scores[1];p3s=scores[2];p4s=scores[3];
        JLabel[] scoreLabels={lblp1score,lblp2score,lblp3score,lblp4score};
        JLabel[] bidLabels={lblp1bid,lblp2bid,lblp3bid,lblp4bid};
        JButton[] bidButtons={btnP1,btnP2,btnP3,btnP4};
        for(int i=0;i<4;i++) {
            scoreLabels[i].setText(String.valueOf(scores[i]));
            bidLabels[i].setText(String.valueOf(bids[i]));
            if(bids[i]>0) bidController.newBid("Player"+(i+1),bids[i]);
            bidButtons[i].setEnabled(saved.token()>0 && !saved.complete() && (saved.players()&(1<<i))!=0);
            robotButtons()[i].setSelected(saved.selectedRobot()==i+1);
            playerButtons()[i].setSelected(saved.selectedPlayer()==i+1);
        }
        moves=saved.moves();lblMoveCount.setText(String.valueOf(moves));
        if(saved.timerSeconds()>=0) { startTimer(saved.timerSeconds());flag=true; }
        updateRoundControls();
        frmTakeTurn.setTitle("Ricochet Robots — loaded game");
    }
    private void resetRound() {
        bidController = new BidController();
        moves = 0; lblMoveCount.setText("0"); flag = false;
        for(JLabel label:new JLabel[]{lblp1bid,lblp2bid,lblp3bid,lblp4bid}) label.setText("0");
        if(gameTmr != null) { gameTmr.getTimer().stop();rightPanel.remove(gameTmr);gameTmr=null; }
        rightPanel.revalidate();rightPanel.repaint();
        updateRoundControls();
    }
    private void updateRoundControls() {
        for(JButton button:new JButton[]{btnUp,btnDown,btnLeft,btnRight,btnToken}) button.setEnabled(!next.isFlag());
        boolean[] active={sc.isPlayer1(),sc.isPlayer2(),sc.isPlayer3(),sc.isPlayer4()};
        JButton[] buttons={btnP1,btnP2,btnP3,btnP4};
        for(int i=0;i<4;i++) buttons[i].setEnabled(active[i] && next.getCurrentToken()>0 && !next.isFlag());
    }
    private void returnToMenu() {
        if(JOptionPane.showConfirmDialog(frmTakeTurn,"Return to the main menu? Save first to keep your progress.","Return to menu",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION) return;
        if(gameTmr!=null) gameTmr.getTimer().stop();
        frmTakeTurn.dispose();
        new MainMenu();
    }
    private void showHint(JMenuItem item) {
        if(next.isFlag()) { JOptionPane.showMessageDialog(frmTakeTurn,"This game is complete.");return; }
        final int target=next.getCurrentToken();
        if(target==0) { JOptionPane.showMessageDialog(frmTakeTurn,"Choose a token before asking for a hint.");return; }
        final int[][] before=SessionState.positions(handler);
        final Handler snapshot=HintSolver.snapshot(handler);
        item.setEnabled(false);
        new SwingWorker<String,Void>() {
            @Override protected String doInBackground() { return HintSolver.solve(snapshot,target); }
            @Override protected void done() {
                item.setEnabled(true);
                if(!isGameOpen()) return;
                try {
                    String message=target==next.getCurrentToken() && java.util.Arrays.deepEquals(before,SessionState.positions(handler))
                        ? get() : "The board changed while searching. Request a new hint.";
                    JOptionPane.showMessageDialog(frmTakeTurn,message,"Hint",JOptionPane.INFORMATION_MESSAGE);
                } catch(Exception ex) { JOptionPane.showMessageDialog(frmTakeTurn,"Unable to calculate this hint. Try again.","Hint",JOptionPane.ERROR_MESSAGE); }
            }
        }.execute();
    }

	public void makeMenuBar(JFrame frame){
				//menu bar
				JMenuBar menubar = new JMenuBar();
				menubar.setFont(new Font("Chalkduster", Font.BOLD, 14));
				menubar.setForeground(new Color(20,12,28));
				
				
				JMenu fileMenu = new JMenu("Game Menu");
				fileMenu.setHorizontalAlignment(SwingConstants.CENTER);
				fileMenu.setFont(new Font("Chalkduster", Font.BOLD, 14));
				fileMenu.setForeground(new Color(68,36,52));
				menubar.add(fileMenu);
				
				JMenuItem saveItem = new  JMenuItem("Save Game");
				saveItem.setHorizontalAlignment(SwingConstants.CENTER);
				saveItem.setFont(new Font("Chalkduster", Font.BOLD, 14));
				saveItem.setForeground(new Color(48,52,109));
				fileMenu.add(saveItem);
				
                saveItem.addActionListener(e -> saveSession());
                JMenuItem returnItem = new JMenuItem("Return to Main Menu");
                returnItem.addActionListener(e -> returnToMenu());
                fileMenu.add(returnItem);


				JMenuItem manItem = new JMenuItem("Manual");
				manItem.setHorizontalAlignment(SwingConstants.CENTER);
				manItem.setFont(new Font("Chalkduster", Font.BOLD, 14));
				manItem.setForeground(new Color(78,74,78));
				fileMenu.add(manItem);
				manItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						new InGameHelp();
					}
				});
				
				JMenuItem hintItem = new JMenuItem("Hint");
				hintItem.setHorizontalAlignment(SwingConstants.CENTER);
				hintItem.setFont(new Font("Chalkduster", Font.BOLD, 14));
                hintItem.addActionListener(e -> showHint(hintItem));
				if(sc.isHintsEnabled()) {
					fileMenu.add(hintItem);
					
				}
				
				if(sc.isNormal()) {
					fileMenu.setForeground(sc.normal11);
					saveItem.setForeground(sc.normal12);
					manItem.setForeground(sc.normal13);
					hintItem.setForeground(sc.normal14);
				}
				if(sc.isDeuteranope()) {
					
					fileMenu.setForeground(sc.deut11);
					saveItem.setForeground(sc.deut12);
					manItem.setForeground(sc.deut13);
					hintItem.setForeground(sc.deut14);
					
				}
				if(sc.isProtanope()) {
					fileMenu.setForeground(sc.prot11);
					saveItem.setForeground(sc.prot12);
					manItem.setForeground(sc.prot13);
					hintItem.setForeground(sc.prot14);
				}
				
				if(sc.isTritanope()) {
					fileMenu.setForeground(sc.trit11);
					saveItem.setForeground(sc.trit12);
					manItem.setForeground(sc.trit13);
					hintItem.setForeground(sc.trit14);
				}
				
				frame.setJMenuBar(menubar);
	}
	
	private void setBid(String player, JLabel lbl) {
		String bidValue = textField.getText();
		try {
			int playersBid = Integer.parseInt(bidValue);
			textField.setText("");
			BidConfirmationPanel.bidConfirmation(bidValue);
			if(BidConfirmationPanel.isConfirmedBid()) {
				boolean valid = bidController.newBid(player, playersBid);
				if(valid) {
					lbl.setText(bidValue);
					if(flag == false) {
						
						Timer();
						flag=true;
					}
				}
				else {
                    JOptionPane.showMessageDialog(frmTakeTurn, "A bid must be positive and lower than your previous bid. Please try again.");
				}
			}
			
		}
		catch (NumberFormatException e) {
		     //Not an integer
			JOptionPane.showMessageDialog(frmTakeTurn, "A player must be selected before a bid can be placed (bids must be positive integer values)");
		}
		
	}

	public int getPlayer() {
		return player ;
	}

	public void setPlayer(int player) {
		this.player = player;
	}
    public void Timer() { startTimer(60); }
    private void startTimer(int seconds) {
        if(gameTmr!=null) { gameTmr.getTimer().stop();rightPanel.remove(gameTmr); }
        gameTmr = new GameTimer(seconds);
		gameTmr.setBounds(67, 15, 58, 100);
		frmTakeTurn.getContentPane().add(gameTmr);		
		rightPanel.add(gameTmr);
	}
	
	public void moveRobotUp(int i) {
		move.setTokenNum(next.getCurrentToken());
		move.setPlayer(i);
		if(rdbtnRobot1.isSelected()) {
			move.up(1);
		}
		if(rdbtnRobot2.isSelected()) {
			move.up(2);
		}
		if(rdbtnRobot3.isSelected()) {
			move.up(3);
		}
		if(rdbtnRobot4.isSelected()) {
			move.up(4);
		}
        if(move.isFlag()) {
            setScore(move.getPlayer());
            resetRound();
            if(next.isFlag()) finalScore();
        }
        move.setFlag(false);
	}
	
	public void moveRobotDown(int i) {
		move.setTokenNum(next.getCurrentToken());
		move.setPlayer(i);
		if(rdbtnRobot1.isSelected()) {
			move.down(1);
		}
		if(rdbtnRobot2.isSelected()) {
			move.down(2);
		}
		if(rdbtnRobot3.isSelected()) {
			move.down(3);
		}
		if(rdbtnRobot4.isSelected()) {
			move.down(4);
		}
        if(move.isFlag()) {
            setScore(move.getPlayer());
            resetRound();
            if(next.isFlag()) finalScore();
        }
        move.setFlag(false);
	}
	public void moveRobotRight(int i) {
		move.setTokenNum(next.getCurrentToken());
		move.setPlayer(i);
		if(rdbtnRobot1.isSelected()) {
			move.right(1);
		}
		if(rdbtnRobot2.isSelected()) {
			move.right(2);
		}
		if(rdbtnRobot3.isSelected()) {
			move.right(3);
		}
		if(rdbtnRobot4.isSelected()) {
			move.right(4);
		}
        if(move.isFlag()) {
            setScore(move.getPlayer());
            resetRound();
            if(next.isFlag()) finalScore();
        }
        move.setFlag(false);
	}
	public void moveRobotLeft(int i) {
		move.setTokenNum(next.getCurrentToken());
		move.setPlayer(i);
		if(rdbtnRobot1.isSelected()) {
			move.left(1);
		}
		if(rdbtnRobot2.isSelected()) {
			move.left(2);
		}
		if(rdbtnRobot3.isSelected()) {
			move.left(3);
		}
		if(rdbtnRobot4.isSelected()) {
			move.left(4);
		}
        if(move.isFlag()) {
            setScore(move.getPlayer());
            resetRound();
            if(next.isFlag()) finalScore();
        }
        move.setFlag(false);
	}
	
	public void setScore(int p) {
		if(p==1) {p1s+=1; lblp1score.setText(""+p1s+"");}
		if(p==2) {p2s+=1; lblp2score.setText(""+p2s+"");}
		if(p==3) {p3s+=1; lblp3score.setText(""+p3s+"");}
		if(p==4) {p4s+=1; lblp4score.setText(""+p4s+"");}
	}
	
    public void finalScore() {
        int[] scores={p1s,p2s,p3s,p4s};
        boolean[] active={sc.isPlayer1(),sc.isPlayer2(),sc.isPlayer3(),sc.isPlayer4()};
        int best=-1;
        for(int i=0;i<4;i++) if(active[i]) best=Math.max(best,scores[i]);
        java.util.List<String> winners=new java.util.ArrayList<>();
        for(int i=0;i<4;i++) if(active[i] && scores[i]==best) winners.add("Player "+(i+1));
        JOptionPane.showMessageDialog(frmTakeTurn,String.join(" and ",winners)+(winners.size()==1?" wins":" tie")+" with "+best+" tokens. Game over.");
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mousePressed(MouseEvent e) {
		

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	public void setHandler(Handler handler) {
		this.handler = handler;
	}
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}
