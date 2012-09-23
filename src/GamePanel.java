import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


public class GamePanel extends Canvas implements Runnable{

	GameWindow window;
	
	Graphics graphics;
	Graphics2D g2d ;
	BufferStrategy buffer;
	BufferedImage bi;
	
	Level level;
	int kartenhoehe, kartenbreite;
	ArrayList<BufferedImage> tileset;
	int[][][]karte;
	public GamePanel(GameWindow w){
		
		window = w;
		level = w.level;
		kartenbreite = level.kartenbild.getWidth();
		kartenhoehe = level.kartenbild.getHeight();
		tileset = level.tileset;
		karte = level.karte;
		this.setIgnoreRepaint(true);
		
		
		
		graphics =null;
		g2d = null;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		
		bi = gc.createCompatibleImage(800,600);
		
	}
	
	public void zeichneLevel(Graphics g){
		
		for(int x = 0; x < kartenbreite;x++){
			for(int y = 0; y < kartenhoehe;y++){
				BufferedImage i = tileset.get(level.karte[x][y][0]);
				if(y%2 == 0){
					g.drawImage(i, x*64,y*16-16,null);
					g.setColor(Color.WHITE);
				}else{
					g.drawImage(i, x*64 +32, y*16-16,null);
				}
				
			}
		}
	}
	
	@Override
	public void run() {
		
		this.createBufferStrategy(2);	//Bufferstrategie setzen, 2= double buffer, 3=triple buffer
		buffer = this.getBufferStrategy();
		
		while(true){
			try{
				g2d = bi.createGraphics();
				g2d.setColor(Color.BLACK);
				g2d.fillRect(0,0,800,600);
				
				//hier dinge zeichnen
				zeichneLevel(g2d);
				
				graphics = buffer.getDrawGraphics();
				graphics.drawImage(bi,0,0,null);
				
				if(!buffer.contentsLost()){
					buffer.show();
				}
				
				Thread.yield();
			}finally{
				if(graphics != null){
					graphics.dispose();
				}
				if(g2d != null){
					g2d.dispose();
				}
			}
			
			
			
		}
	}
	
	

}
