package java2DGame.game.level;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import java2DGame.game.entities.Entity;
import java2DGame.game.gfx.Screen;
import java2DGame.game.level.tiles.Tile;

public class Level {

	private byte[] tiles;
	public int width;
	public int height;
	public List<Entity> entities = new ArrayList<Entity>();
	private BufferedImage image;
	
	public Level(String imagePath) {
		
		if(imagePath!=null) {
			this.loadLevelFromFile(imagePath);
		}else {
			tiles=new byte[width*height];
			this.width=64;
			this.height=64;
			this.generateLevel();
		}
	}
	
	private void loadLevelFromFile(String imagePath) {
		File file = new File(imagePath);
		try {
			this.image=ImageIO.read(file);
			this.width=image.getWidth();
			this.height=image.getHeight();
			tiles=new byte[width*height];
			this.loadTiles();
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void saveLevelToFile(String imagePath) {
		try {
			ImageIO.write(image, "png", new File(Level.class.getResource(imagePath).getFile()));
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void alterTile(int x,int y,Tile newTile) {
		this.tiles[x+y*width]=newTile.getID();
		image.setRGB(x, y, newTile.getLevelColour());
	}
	
	private void loadTiles() {
		int[] tileColours=this.image.getRGB(0, 0, width, height, null, 0, width);
		for(int y=0;y<height;y++) {
			for(int x=0;x<width;x++) {
				tileCheck: for(Tile t:Tile.tiles) {
					if(t!=null && t.getLevelColour() == tileColours[x+y*width]) {
						this.tiles[x+y*width]=t.getID();
						break tileCheck;
					}
				}
			}
		}
	}

	public void generateLevel() {
		for(int y=0;y<height;y++) {
			for(int x=0;x<width;x++) {
				if(x*y%10<7) {
					tiles[x+y*width]=Tile.GRASS.getID();
				}else {
					tiles[x+y*width]=Tile.STONE.getID();
				}
			}
		}
	}
	
	public void tick() {
		for(Entity e: entities) {
			e.tick();
		}
		
		for(Tile t:Tile.tiles) {
			if(t==null) {
				break;
			}
			t.tick();
		}
	}
	
	public void renderTile(Screen screen, int xOffset, int yOffset) {
		if(xOffset<0) xOffset=0;
		if(xOffset>((width<<3)-screen.width)) xOffset=((width<<3)-screen.width);
		if(yOffset<0) yOffset=0;
		if(yOffset>((height<<3)-screen.height)) yOffset=((height<<3)-screen.height);
		
		screen.setOffset(xOffset,yOffset);
		
		for(int y=0;y<height;y++) {
			for(int x=0;x<width;x++) {
				getTile(x,y).render(screen,this,x<<3,y<<3);
			}
		}
	}
	
	public void renderEntities(Screen screen) {
		for(Entity e: entities) {
			e.render(screen);
		}
	}

	public Tile getTile(int x, int y) {
		
		if(0>x || x>=width || 0>y || y>=height) return Tile.VOID;
		return Tile.tiles[tiles[x+y*width]];
	}
	
	public void addEntity(Entity entity) {
		this.entities.add(entity);
	}
}
