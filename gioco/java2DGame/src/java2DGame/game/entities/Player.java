package java2DGame.game.entities;

import java2DGame.game.InputHandler;
import java2DGame.game.gfx.Colours;
import java2DGame.game.gfx.Screen;
import java2DGame.game.level.Level;

public class Player extends Mob{
	
	private InputHandler input;
	private int colour = Colours.get(-1,111,145,543);
	private int scale=1;
	protected boolean isSwimming=false;	

	public Player(Level level, int x, int y, InputHandler input) {
		super(level, "Player", x, y, 1);
		this.input=input;
	}


	public void tick() {
		int xa = 0;
		int ya = 0;
		
		if(input.up.isPressed())ya--;
		if(input.down.isPressed())ya++;
		if(input.left.isPressed())xa--;
		if(input.right.isPressed())xa++;
		
		if(xa!=0 || ya!=0) {
			move(xa,ya);
			isMoving=true;
		}
		else {
			isMoving=false;
		}
		
		if(level.getTile(this.x>>3, this.y>>3).getID()==3){
			isSwimming=true;
		}
		if(isSwimming && level.getTile(this.x>>3, this.y>>3).getID()!=3) {
			isSwimming=false;
		}
	}

	public void render(Screen screen) {
		int xTile = 0;
		int yTile = 28;
		int walkingSpeed = 4;
		int flipTop=(numSteps>>walkingSpeed)&1;
		int flipBottom=(numSteps>>walkingSpeed)&1;
		
		if(movingDir==1) {
			xTile+=2;
		}else if(movingDir>1) {
			xTile+=4+((numSteps>>walkingSpeed)&1)*2;
			flipTop=(movingDir-1)%2;
		}
		
		int modifier = 8*scale;
		int xOffset=x-modifier/2;
		int yOffset=y-modifier/2 -4;
		
		screen.render(xOffset + (modifier*flipTop), yOffset, xTile+yTile*32, colour,flipTop,scale);
		screen.render(xOffset+modifier-(modifier*flipTop), yOffset, (xTile+1)+yTile*32, colour,flipTop,scale);
		if(!isSwimming) {
			screen.render(xOffset+ (modifier*flipBottom), yOffset+modifier, xTile+(yTile+1)*32, colour,flipBottom,scale);
			screen.render(xOffset+modifier-(modifier*flipBottom), yOffset+modifier, (xTile+1)+(yTile+1)*32, colour,flipBottom,scale);
		}
	}
	

	public boolean hasCollided(int xa, int ya) {
		int xMin=0;
		int xMax=7;
		int yMin=3;
		int yMax=7;
		
		for(int x=xMin;x<xMax;x++) {
			if(isSolidTile(xa,ya,x,yMin)) {
				return true;
			}
		}
		for(int x=xMin;x<xMax;x++) {
			if(isSolidTile(xa,ya,x,yMax)) {
				return true;
			}
		}
		for(int y=yMin;y<yMax;y++) {
			if(isSolidTile(xa,ya,xMin,y)) {
				return true;
			}
		}
		for(int y=yMin;y<yMax;y++) {
			if(isSolidTile(xa,ya,xMax,y)) {
				return true;
			}
		}
		return false;
	}

}
