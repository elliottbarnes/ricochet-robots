package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class GameBoard implements Runnable{

	private GameGUI gameGui;
	private Thread thread;
	private boolean running = false;
	private BufferStrategy bs;
	private Graphics g;
	private Handler handler;
	
	public static final int WIDTH=600, HEIGHT=600; 
	
	public GameBoard() { 
		handler = new Handler();
		
		//Cells
		for(int col=0; col<WIDTH-8; col=col+(WIDTH/16)) {
			for(int row=0; row<HEIGHT-8; row=row+(HEIGHT/16)) {
				handler.addObject(new BoardCell(col, row, ((WIDTH/16)-1), ((HEIGHT/16)-1), ID.Cell));
			}
		}
		//Borders
		handler.addObject(new Barrier(0, 0, 6, 600, ID.Barrier));
		handler.addObject(new Barrier(0, 0, 600, 6, ID.Barrier));
		handler.addObject(new Barrier(588, 0, 6, 600, ID.Barrier));
		handler.addObject(new Barrier(0, 588, 600, 6, ID.Barrier));
		//Middle
		handler.addObject(new Barrier(255, 255, 80, 80, ID.Barrier));
		//Inside
		handler.addObject(new Barrier(70, 0, 6, 36, ID.Barrier));
		handler.addObject(new Barrier(0, 218, 36, 6, ID.Barrier));
		handler.addObject(new Barrier(0, 440, 36, 6, ID.Barrier));
		handler.addObject(new Barrier(256, 554, 6, 36, ID.Barrier));
		handler.addObject(new Barrier(554, 404, 36, 6, ID.Barrier));//
		handler.addObject(new Barrier(554, 144, 36, 6, ID.Barrier));
		handler.addObject(new Barrier(36, 72, 36, 6, ID.Barrier));
		handler.addObject(new Barrier(70, 74, 6, 36, ID.Barrier));
		handler.addObject(new Barrier(110, 256 , 36, 6, ID.Barrier));
		handler.addObject(new Barrier(110, 221, 6, 36, ID.Barrier));
		handler.addObject(new Barrier(148, 34 , 36, 6, ID.Barrier));
		handler.addObject(new Barrier(145, 36, 6, 36, ID.Barrier));
		handler.addObject(new Barrier(254, 110, 6, 36, ID.Barrier));
		handler.addObject(new Barrier(222, 144, 36, 6, ID.Barrier));
		handler.addObject(new Barrier(333, 106, 36, 6, ID.Barrier));
		handler.addObject(new Barrier(365, 72, 6, 36, ID.Barrier));
		handler.addObject(new Barrier(440, 0, 6, 36, ID.Barrier));
		handler.addObject(new Barrier(482, 34, 36, 6, ID.Barrier));
		handler.addObject(new Barrier(478, 36, 6, 36, ID.Barrier));
		handler.addObject(new Barrier(518, 184, 6, 36, ID.Barrier));
		handler.addObject(new Barrier(518, 217, 36, 6, ID.Barrier));
		handler.addObject(new Barrier(406, 221, 36, 6, ID.Barrier));
		handler.addObject(new Barrier(440, 221, 6, 36, ID.Barrier));
		handler.addObject(new Barrier(37, 366, 36, 6, ID.Barrier));
		handler.addObject(new Barrier(70, 332, 6, 36, ID.Barrier));
		handler.addObject(new Barrier(148, 402, 36, 6, ID.Barrier));
		handler.addObject(new Barrier(147, 368, 6, 36, ID.Barrier));	
		handler.addObject(new Barrier(110, 517, 36, 6, ID.Barrier));
		handler.addObject(new Barrier(110, 517, 6, 36, ID.Barrier));
		handler.addObject(new Barrier(185, 480, 36, 6, ID.Barrier));
		handler.addObject(new Barrier(218, 480, 6, 36, ID.Barrier));
		handler.addObject(new Barrier(295, 369, 36, 6, ID.Barrier));
		handler.addObject(new Barrier(328, 369, 6, 36, ID.Barrier));
		handler.addObject(new Barrier(518, 365, 36, 6, ID.Barrier));
		handler.addObject(new Barrier(514, 332, 6, 36, ID.Barrier));
		handler.addObject(new Barrier(480, 440, 36, 6, ID.Barrier));
		handler.addObject(new Barrier(480, 407, 6, 36, ID.Barrier));
		handler.addObject(new Barrier(369, 480, 36, 6, ID.Barrier));
		handler.addObject(new Barrier(369, 481, 6, 36, ID.Barrier));
		handler.addObject(new Barrier(440, 555, 6, 36, ID.Barrier));
		
		//Tokens
		handler.addObject(new Token(334, 74, 28, 28, ID.Token1, 1));
		handler.addObject(new Token(37, 80, 28, 28, ID.Token2, 2));
		handler.addObject(new Token(298, 377, 28, 28, ID.Token3, 3));
		handler.addObject(new Token(118, 526, 28, 28, ID.Token4, 4)); 
		handler.addObject(new Token(39, 335, 28, 28, ID.Token5, 5));
		handler.addObject(new Token(488, 410, 28, 28, ID.Token6, 6)); 
		handler.addObject(new Token(155, 43, 28, 28, ID.Token7, 7));
		handler.addObject(new Token(409, 230, 28, 28, ID.Token8, 8));
		handler.addObject(new Token(118, 226, 28, 28, ID.Token9, 9));
		handler.addObject(new Token(155, 372, 28, 28, ID.Token10, 10));
		handler.addObject(new Token(487, 45, 28, 28, ID.Token11, 11));
		handler.addObject(new Token(522, 335, 28, 28, ID.Token12, 12));
		handler.addObject(new Token(377, 487, 28, 28, ID.Token13, 13));
		handler.addObject(new Token(526, 188, 28, 28, ID.Token14, 14));
		handler.addObject(new Token(188, 488, 28, 28, ID.Token15, 15));
		handler.addObject(new Token(225, 113, 28, 28, ID.Token16, 16));
		handler.addObject(new Token(186, 300, 28, 28, ID.Token17, 17));
		
		//Robots
		handler.addObject(new Robot(10, 10, 22, 22, ID.Robot, handler));
		handler.addObject(new Robot(42, 10, 22, 22, ID.Robot2, handler));
		handler.addObject(new Robot(82, 10, 22, 22, ID.Robot3, handler));
		handler.addObject(new Robot(120, 10, 22, 22, ID.Robot4, handler));
	}

    private javax.swing.Timer renderTimer;
    private controller.SaveData loaded;
    public GameBoard(controller.SaveData saved) {
        this();
        SessionState.restore(handler, saved);
        loaded = saved;
    }
    public Handler getHandler() { return handler; }
    private void render() {
        if (!gameGui.isGameOpen()) { stop(); return; }
        bs = gameGui.getCanvas().getBufferStrategy();
        if (bs == null) { gameGui.getCanvas().createBufferStrategy(2); return; }
        Graphics graphics = bs.getDrawGraphics();
        try {
            graphics.setColor(Color.black);
            graphics.fillRect(0, 0, WIDTH, HEIGHT);
            handler.render(graphics);
        } finally { graphics.dispose(); }
        bs.show();
    }
    public void run() {
        gameGui = new GameGUI(handler, loaded);
        renderTimer = new javax.swing.Timer(33, event -> render());
        renderTimer.start();
    }
    public void start() {
        if (running) return;
        running = true;
        if (javax.swing.SwingUtilities.isEventDispatchThread()) run();
        else javax.swing.SwingUtilities.invokeLater(this);
    }
    public void stop() {
        running = false;
        if (renderTimer != null) renderTimer.stop();
    }
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
}
