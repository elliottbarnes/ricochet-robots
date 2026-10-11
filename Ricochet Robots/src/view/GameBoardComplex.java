package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class GameBoardComplex implements Runnable {

	private GameGUI gameGui;
	private Thread thread;
	private boolean running = false;
	private BufferStrategy bs;
	private Graphics g;
	private Handler handler;
	
	public static final int WIDTH=600, HEIGHT=600; 
	
	public GameBoardComplex() { 
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
		handler.addObject(new Barrier(218, 0, 6, 37, ID.Barrier));
		handler.addObject(new Barrier(330, 0, 6, 37, ID.Barrier));
		handler.addObject(new Barrier(367, 36, 6, 37, ID.Barrier));
		handler.addObject(new Barrier(369, 34, 37, 6, ID.Barrier));
		handler.addObject(new Barrier(555, 108, 37, 6, ID.Barrier));
		handler.addObject(new Barrier(330, 147, 6, 37, ID.Barrier));
		handler.addObject(new Barrier(295, 182, 37, 6, ID.Barrier));
		handler.addObject(new Barrier(256, 147, 6, 37, ID.Barrier));
		handler.addObject(new Barrier(221, 182, 37, 6, ID.Barrier));
		handler.addObject(new Barrier(34, 111, 6, 37, ID.Barrier));
		handler.addObject(new Barrier(36, 108, 37, 6, ID.Barrier));
		handler.addObject(new Barrier(0, 218, 37, 6, ID.Barrier));
		handler.addObject(new Barrier(74, 218, 37, 6, ID.Barrier));
		handler.addObject(new Barrier(108, 221, 6, 37, ID.Barrier));
		handler.addObject(new Barrier(110, 255, 37, 6, ID.Barrier));
		handler.addObject(new Barrier(478, 184, 6, 37, ID.Barrier));
		handler.addObject(new Barrier(480, 218, 37, 6, ID.Barrier));
		handler.addObject(new Barrier(515, 221, 6, 37, ID.Barrier));
		handler.addObject(new Barrier(0, 404, 37, 6, ID.Barrier));
		handler.addObject(new Barrier(70, 332, 6, 37, ID.Barrier));
		handler.addObject(new Barrier(73, 366, 37, 6, ID.Barrier));
		handler.addObject(new Barrier(258, 366, 37, 6, ID.Barrier));
		handler.addObject(new Barrier(292, 369, 6, 37, ID.Barrier));
		handler.addObject(new Barrier(256, 554, 6, 37, ID.Barrier));
		handler.addObject(new Barrier(145, 443, 6, 37, ID.Barrier));
		handler.addObject(new Barrier(110, 478, 37, 6, ID.Barrier));
		handler.addObject(new Barrier(108, 480, 6, 37, ID.Barrier));
		handler.addObject(new Barrier(184, 515, 37, 6, ID.Barrier));
		handler.addObject(new Barrier(218, 517, 6, 37, ID.Barrier));
		handler.addObject(new Barrier(554, 478, 37, 6, ID.Barrier));
		handler.addObject(new Barrier(404, 554, 6, 37, ID.Barrier));
		handler.addObject(new Barrier(332, 478, 37, 6, ID.Barrier));
		handler.addObject(new Barrier(330, 480, 6, 37, ID.Barrier));
		handler.addObject(new Barrier(369, 366, 37, 6, ID.Barrier));
		handler.addObject(new Barrier(404, 333, 6, 37, ID.Barrier));
		handler.addObject(new Barrier(444, 404, 37, 6, ID.Barrier));
		handler.addObject(new Barrier(478, 406, 6, 37, ID.Barrier));
		handler.addObject(new Barrier(480, 440, 37, 6, ID.Barrier));
		
		handler.addObject(new ComplexBarrier(185, 258, 37, 37, ID.ComplexBarrier1, 1));
		handler.addObject(new ComplexBarrier(333, 443, 37, 37, ID.ComplexBarrier2, 1));
		handler.addObject(new ComplexBarrier(406, 517, 37, 37, ID.ComplexBarrier3, 1));
		handler.addObject(new ComplexBarrier(517, 74, 37, 37, ID.ComplexBarrier4, 1));
		handler.addObject(new ComplexBarrier(148, 37, 37, 37, ID.ComplexBarrier5, 2));
		handler.addObject(new ComplexBarrier(406, 258, 37, 37, ID.ComplexBarrier6, 2));
		handler.addObject(new ComplexBarrier(222, 444, 37, 37, ID.ComplexBarrier7, 2));
		handler.addObject(new ComplexBarrier(37, 480, 37, 37, ID.ComplexBarrier8, 2));
		
		//Tokens
		handler.addObject(new Token(297, 150, 28, 28, ID.Token1, 1));
		handler.addObject(new Token(75, 225, 28, 28, ID.Token2, 2));
		handler.addObject(new Token(446, 412, 28, 28, ID.Token3, 3));
		handler.addObject(new Token(77, 334, 28, 28, ID.Token4, 4));
		handler.addObject(new Token(115, 485, 28, 28, ID.Token5, 5));
		handler.addObject(new Token(486, 410, 28, 28, ID.Token6, 6)); 
		handler.addObject(new Token(40, 114, 28, 28, ID.Token7, 7));
		handler.addObject(new Token(483, 226, 28, 28, ID.Token8, 8));
		handler.addObject(new Token(115, 225, 28, 28, ID.Token9, 9));
		handler.addObject(new Token(113, 448, 28, 28, ID.Token10, 10));
		handler.addObject(new Token(375, 42, 28, 28, ID.Token11, 11));
		handler.addObject(new Token(373, 336, 28, 28, ID.Token12, 12));
		handler.addObject(new Token(337, 487, 28, 28, ID.Token13, 13));
		handler.addObject(new Token(484, 188, 28, 28, ID.Token14, 14));
		handler.addObject(new Token(188, 521, 28, 28, ID.Token15, 15));
		handler.addObject(new Token(225, 150, 28, 28, ID.Token16, 16));
		handler.addObject(new Token(260, 375, 28, 28, ID.Token17, 17));
		
		//Robots
		handler.addObject(new Robot(10, 10, 22, 22, ID.Robot, handler));
		handler.addObject(new Robot(42, 10, 22, 22, ID.Robot2, handler));
		handler.addObject(new Robot(82, 10, 22, 22, ID.Robot3, handler));
		handler.addObject(new Robot(120, 10, 22, 22, ID.Robot4, handler));
	}

    private javax.swing.Timer renderTimer;
    private controller.SaveData loaded;
    public GameBoardComplex(controller.SaveData saved) {
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
