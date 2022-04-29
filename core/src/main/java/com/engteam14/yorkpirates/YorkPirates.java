package com.engteam14.yorkpirates;

import javax.lang.model.util.ElementScanner6;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class YorkPirates extends Game {

	// Global values
	public BitmapFont font;
	public SpriteBatch batch;
	public OrthographicCamera camera;
	public Array<Array<Boolean>> edges;
	

	// Animations
	public Animation<TextureRegion> logo;
	public Animation<TextureRegion> mouse;
	public Animation<TextureRegion> keyboard;
	public Animation<TextureRegion> enter;

	// Constants
	public static final boolean DEBUG_ON = false; // Determines if the game runs in DEBUG mode.
	private static final int SCREEN_TO_PIXEL_RATIO = 16; // Determines the pixel ratio of the game.

	/**
	 * Initialises base game class.
	 */
	@Override
	public void create () {
		// Graphics settings
		Gdx.graphics.setForegroundFPS(30);
		Gdx.graphics.setVSync(true);

		// Initialise objects
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 16* SCREEN_TO_PIXEL_RATIO, 9* SCREEN_TO_PIXEL_RATIO);
		batch = new SpriteBatch();

		// Get font from skin
		TextureAtlas atlas;
		atlas = new TextureAtlas(Gdx.files.internal("Skin/YorkPiratesSkin.atlas"));
		Skin skin = new Skin(Gdx.files.internal("Skin/YorkPiratesSkin.json"), new TextureAtlas(Gdx.files.internal("Skin/YorkPiratesSkin.atlas")));
		skin.addRegions(atlas);
		font = skin.getFont("Raleway-Bold");

		// Long animations loaded in at start of the game for better performance
		logo = getAnimator("logo.png", 20, 10,8);
		mouse = getAnimator("mouse.png", 20,5, 5);
		keyboard = getAnimator("keyboard.png", 20,1, 16);
		enter = getAnimator("enter.png", 20,1, 10);

		// Calculates collision array from edges csv
		edges = new Array<>();
		String data = Gdx.files.internal("FINAL_MAP_Terrain.csv").readString();
		for(String row: data.split("\n")){
			Array<Boolean> newRow = new Array<>();

			for(String num: row.split(","))
			{
				if(num.equals("-1")) 
				{				
					newRow.add(true);

				}
				else if(num.equals("2"))
				{
					newRow.add(true);
				}
				else 
				{
					newRow.add(false);
				} 	
			}
			edges.insert(0, newRow);
		}

		// Sets the screen to the title screen
		setScreen(new TitleScreen(this));
	}

	/**
	 * Splits a sheet of frames into an animator.
	 * @param path		The file to split.
	 * @param fps		The framerate of the animation.
	 * @param rows		The number of rows.
	 * @param columns	The number of columns.
	 * @return			The animation.
	 */
	private Animation<TextureRegion> getAnimator(String path, float fps, int rows, int columns) {
		Texture logosheet = new Texture(Gdx.files.internal(path));
		TextureRegion[][] split = TextureRegion.split(logosheet, logosheet.getWidth() / columns, logosheet.getHeight() / rows);
		Array<TextureRegion> frames = new Array<>();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				frames.add(split[i][j]);
			}
		}
		return new Animation<>(fps==0?0:(1f/fps), frames);
	}

	/**
	 * Disposes of data when game finishes execution.
	 */
	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
	}

	/**
	 * Closes the application
	 */
	public void quit(){
		Gdx.app.exit();
	}
}