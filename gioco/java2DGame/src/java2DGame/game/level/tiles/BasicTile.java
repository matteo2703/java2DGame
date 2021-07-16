package java2DGame.game.level.tiles;

import java2DGame.game.gfx.Screen;
import java2DGame.game.level.Level;

public class BasicTile extends Tile{
	
	protected int tileId;
	protected int tileColor;

	public BasicTile(int id,int x, int y, int tileColour) {
		super(id, false, false);
		this.tileId=x+y;
		this.tileColor=tileColour;
	}

	public void render(Screen screen, Level level, int x, int y) {
		screen.render(x, y, tileId,tileColor,0x00,1);
	}

}