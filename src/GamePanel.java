import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class GamePanel extends Canvas implements Runnable{

	GameWindow window;
	
	Graphics graphics;
	Graphics2D g2d ;
	BufferStrategy buffer;
	BufferedImage bi;
	int panelwidth,panelheight; 
	Level level;
	int mapHeight, mapWidth;
	ArrayList<BufferedImage> tileset;
	int[][][]map;
	
	Tileset set;
	
	Player player;
	Enemy1 enemy1;
	int gamespeed =20;
	//BulletHandler bullethandler;
	
	ArrayList<Bullet> bulletsInRoom;
	Collection  bullets;
	
	public GamePanel(GameWindow w){
		
		window = w;
		level = window.level;
		mapWidth = level.mapPic.getWidth()*64;
		mapHeight = level.mapPic.getHeight()*128;
		
		set = new Tileset();
		tileset = set.tileset;
		map = level.map;
		player = window.player;
		enemy1 = window.enemy1;
		this.setIgnoreRepaint(true);
		
		
		
		graphics =null;
		g2d = null;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		
		bi = gc.createCompatibleImage(1920,1080);
		panelwidth = gc.getBounds().width;
		panelheight = gc.getBounds().height;
		bulletsInRoom = new ArrayList<Bullet>();
		bullets = Collections.synchronizedList(bulletsInRoom);
	}
	
	public Dimension getPreferredSize(){
		
		
		
		return new Dimension(panelwidth,panelheight);		//?
	}
	
	public void drawLevel(Graphics g){
		
		for(int x = 0; x < mapWidth/64;x++){
			for(int y = 0; y < mapHeight/128;y++){
				BufferedImage i = tileset.get(level.map[x][y][0]);
				if(y%2 == 0){
					g.drawImage(i, x*64,y*16,null);//g.drawImage(i, x*64,y*16-16,null);
				}else{
					g.drawImage(i, x*64 +32, y*16,null);//g.drawImage(i, x*64 +32, y*16-16,null);
				}
				//i=null;
			}
		}
	}
	
	public void drawPlayer(Graphics g){
		g.drawImage(player.getImage(),player.getX(),player.getY()-32,64,96,null);
	}
	public void drawEnemy1(Graphics g){
		g.drawImage(enemy1.getImage(),enemy1.getX(),enemy1.getY()-32,64,96,null);
	}
	
	public void drawBullets (Graphics g){
		
//		Collections.synchronizedList(
//			
//		}
		for(int index =0; index < bulletsInRoom.size();index++){
			Bullet b = bulletsInRoom.get(index);
			if(b != null){
				g.drawImage(b.image,b.posX,b.posY,null);
				
			}
		}
		
	}
	
	
	@Override
	public synchronized void run() {
		
		this.createBufferStrategy(2);	//Bufferstrategie setzen, 2= double buffer, 3=triple buffer
		buffer = this.getBufferStrategy();
		
		while(true){
			float onStart = System.currentTimeMillis();
			try{
				g2d = bi.createGraphics();
				g2d.setColor(Color.BLACK);
				g2d.fillRect(0,0,1920,1080);
				
				//hier dinge zeichnen
				drawLevel(g2d);
				drawPlayer(g2d);
				drawBullets(g2d);
				drawEnemy1(g2d);
				
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
			
			float onEnd = System.currentTimeMillis()- onStart;
			if(gamespeed > onEnd){
				try {
					Thread.sleep(gamespeed -(int)onEnd);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
	

}
