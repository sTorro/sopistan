/*
 * File name: SopistanMain.java
 * This file is part of Sopistan project.
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 3 of the License, or (at your
 * option) any later version. This program is distributed in the hope that
 * it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with triviazo-project.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2016 Sergio Torró.
 */

package com.torrosoft.sopistan;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.torrosoft.sopistan.core.Sopistan;
import com.torrosoft.sopistan.core.wrappers.Position;
import com.torrosoft.sopistan.gui.graphics.Strip;
import com.torrosoft.sopistan.input.FingerHandler;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import static com.torrosoft.sopistan.utils.IterUtil.iter;

/**
 * Main entry.
 * @author storro
 */
public class SopistanMain extends ApplicationAdapter {
	OrthographicCamera cam;
	SpriteBatch batch;
	FingerHandler swipe;
	Texture tex;
    Sprite sprite, sprite2, sprite3;
	ShapeRenderer shapes;
	Strip tris;
    List<Sprite> group_sprites;
    String strAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    Stage stage;

    int HEIGHT = 600;
    int WIDTH = 800;
    int ITEM_PER_ROW = 15;

    Texture abcBuble;
    Sprite spriteX ;
    Map<String, Sprite> spriteAlphabet;
    int istate = 0;

    @Override
	public void create() {

		batch = new SpriteBatch();

        spriteAlphabet = new HashMap<String, Sprite>();
        initAlphabets();

		// the triangle strip renderer
		tris = new Strip();

		// a swipe handler with max # of input points to be kept alive
		swipe = new FingerHandler(10);

		// minimum distance between two points
		swipe.setMinDistance(10);

		// minimum distance between first and second point
		swipe.setInitialDistance(10);

		// we will use a texture for the smooth edge, and also for stroke effects
		tex = new Texture("gradient2.png");
		tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);

        Texture ttr_mat_a = new Texture("mat-A.png");
        //Texture ttr_mat_b = new Texture("data/mat-A.png");

        group_sprites = new ArrayList<Sprite>();
        int offset = 10;
        int SPRITE_WIDTH = 40;
        int SPRITE_HEIGHT = 40;

        int len = strAlphabet.length();
        float posx = 5;
        float posy = 5;
        float offsetX = 30;
        float offsetY = 30;

        for (int x = 0; x < Position.MAX_X; x++) {
            float newPosX = posx  + (posx * x) + (x * offsetX);
            for (int y = 0; y < Position.MAX_Y; y++) {
                float newPosY = posx  + (posy * y) + (y * offsetY);
                Sprite spriteTmp = new Sprite(spriteAlphabet.get(
                                        String.valueOf(
                                            com.torrosoft.sopistan.core.Map.map[x][y]
                                        )
                )) ;
                spriteTmp.setPosition(newPosX, newPosY);
                group_sprites.add( spriteTmp );
			}
		}

        for( int idx = 0; idx < len; idx++){
            group_sprites.add( ((Sprite)spriteAlphabet.get( String.valueOf(strAlphabet.charAt(idx)))) );
            System.out.println(strAlphabet.charAt(idx));
        }

		shapes = new ShapeRenderer();
		batch = new SpriteBatch();

		cam = new OrthographicCamera();
		cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		// handle swipe input
		Gdx.input.setInputProcessor(swipe);
	}

	@Override
	public void resize(int width, int height) {
		cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void render() {

        if ( istate == 5)
            return;

        batch.begin();

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		cam.update();
		batch.setProjectionMatrix(cam.combined);

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		tex.bind();

		// the end cap scale
		tris.setEndCap(5F);

		// the thickness of the line
		tris.setThickness(30F);

		// generate the triangle strip from our path
		tris.update(swipe.getPath());

		// the vertex color for tinting, i.e. for opacity
		tris.setColor(Color.RED);

		// render the triangles to the screen
		tris.draw(cam);

        int idx = 0;
        for (int y = 0; y < Position.MAX_Y; y++) {
            for (int x = 0; x < Position.MAX_X; x++) {
                ((Sprite) group_sprites.get(idx)).draw(batch);
                idx++;
			}
		}

		// uncomment to see debug lines
		//drawDebug();
        batch.end();

        istate = 1;
	}

	// optional debug drawing..
	void drawDebug() {
		Array<Vector2> input = swipe.getInput();

		// draw the raw input
		shapes.begin(ShapeType.Line);
		shapes.setColor(Color.GRAY);
		for (int i = 0; i < input.size - 1; i++) {
			Vector2 p = input.get(i);
			Vector2 p2 = input.get(i + 1);
			shapes.line(p.x, p.y, p2.x, p2.y);
		}
		shapes.end();

		// draw the smoothed and simplified path
		shapes.begin(ShapeType.Line);
		shapes.setColor(Color.RED);

		Array<Vector2> out = swipe.getPath();
		for (int i = 0; i < out.size - 1; i++) {
			Vector2 p = out.get(i);
			Vector2 p2 = out.get(i + 1);
			shapes.line(p.x, p.y, p2.x, p2.y);
		}

		shapes.end();

		// render our perpendiculars
		shapes.begin(ShapeType.Line);
		Vector2 perp = new Vector2();

		for (int i = 1; i < input.size - 1; i++) {
			Vector2 p = input.get(i);
			Vector2 p2 = input.get(i + 1);

			shapes.setColor(Color.LIGHT_GRAY);
			perp.set(p).sub(p2).nor();
			perp.set(perp.y, -perp.x);
			perp.scl(10f);
			shapes.line(p.x, p.y, p.x + perp.x, p.y + perp.y);
			perp.scl(-1f);
			shapes.setColor(Color.BLUE);
			shapes.line(p.x, p.y, p.x + perp.x, p.y + perp.y);
		}

		shapes.end();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		batch.dispose();
		shapes.dispose();
		tex.dispose();
	}

    public void initAlphabets()
    {
        float sizeX = 30;
        float sizeY = 30;
        abcBuble = new Texture("abc-buble.jpg");
        spriteX = new Sprite(abcBuble, 12, 29, 115, 79);
        spriteX.setPosition(10,30);
        spriteX.setSize(sizeX,sizeY);
        spriteAlphabet.put("A", spriteX);

        spriteX = new Sprite(abcBuble, 149, 29, 115, 79);
        spriteX.setPosition(60,30);
        spriteX.setSize(sizeX,sizeY);
        spriteAlphabet.put("B", spriteX);

        spriteX = new Sprite(abcBuble, 288, 29, 115, 79);
        spriteX.setPosition(110,30);
        spriteX.setSize(sizeX,sizeY);
        spriteAlphabet.put("C", spriteX);

        spriteX = new Sprite(abcBuble, 423, 29, 115, 79);
        spriteX.setPosition(160,30);
        spriteX.setSize(sizeX,sizeY);
        spriteAlphabet.put("D", spriteX);

        spriteX = new Sprite(abcBuble, 14, 131, 115, 79);
        spriteX.setPosition(210,30);
        spriteX.setSize(sizeX,sizeY);
        spriteAlphabet.put("E", spriteX);

        spriteX = new Sprite(abcBuble, 151, 131, 115, 79);
        spriteX.setPosition(260,30);
        spriteX.setSize(sizeX,sizeY);
        spriteAlphabet.put("F", spriteX);

        spriteX = new Sprite(abcBuble, 288, 131, 115, 79);
        spriteX.setPosition(310,30);
        spriteX.setSize(sizeX,sizeY);
        spriteAlphabet.put("G", spriteX);

        spriteX = new Sprite(abcBuble, 426, 131, 115, 79);
        spriteX.setPosition(310,30);
        spriteX.setSize(sizeX,sizeY);
        spriteAlphabet.put("H", spriteX);

        spriteX = new Sprite(abcBuble, 13, 233, 115, 79);
        spriteX.setPosition(360,30);
        spriteX.setSize(sizeX,sizeY);
        spriteAlphabet.put("I", spriteX);

        spriteX = new Sprite(abcBuble, 152, 233, 115, 79);
        spriteX.setPosition(410,30);
        spriteX.setSize(sizeX,sizeY);
        spriteAlphabet.put("J", spriteX);

        spriteX = new Sprite(abcBuble, 288, 233, 115, 79);
        spriteX.setPosition(460,30);
        spriteX.setSize(sizeX,sizeY);
        spriteAlphabet.put("K", spriteX);

        spriteX = new Sprite(abcBuble, 427, 233, 115, 79);
        spriteX.setPosition(510,30);
        spriteX.setSize(sizeX,sizeY);
        spriteAlphabet.put("L", spriteX);

        spriteX = new Sprite(abcBuble, 13, 335, 115, 79);
        spriteX.setPosition(560,30);
        spriteX.setSize(sizeX,sizeY);
        spriteAlphabet.put("M", spriteX);

        spriteX = new Sprite(abcBuble, 151, 335, 115, 79);
        spriteX.setPosition(610,30);
        spriteX.setSize(sizeX,sizeY);
        spriteAlphabet.put("N", spriteX);

        spriteX = new Sprite(abcBuble, 288, 335, 115, 79);
        spriteX.setPosition(660,30);
        spriteX.setSize(sizeX,sizeY);
        spriteAlphabet.put("O", spriteX);

        spriteX = new Sprite(abcBuble, 424, 335, 115, 79);
        spriteX.setPosition(10,80);
        spriteX.setSize(sizeX,sizeY);
        spriteAlphabet.put("P", spriteX);

        spriteX = new Sprite(abcBuble, 13, 437, 115, 79);
        spriteX.setPosition(60,80);
        spriteX.setSize(sizeX,sizeY);
        spriteAlphabet.put("Q", spriteX);

        spriteX = new Sprite(abcBuble, 149, 437, 115, 79);
        spriteX.setPosition(110,80);
        spriteX.setSize(sizeX,sizeY);
        spriteAlphabet.put("R", spriteX);

        spriteX = new Sprite(abcBuble, 288, 437, 115, 79);
        spriteX.setPosition(160,80);
        spriteX.setSize(sizeX,sizeY);
        spriteAlphabet.put("S", spriteX);

        spriteX = new Sprite(abcBuble, 424, 437, 115, 79);
        spriteX.setPosition(210,80);
        spriteX.setSize(sizeX,sizeY);
        spriteAlphabet.put("T", spriteX);

        spriteX = new Sprite(abcBuble, 13, 540, 115, 79);
        spriteX.setPosition(260,80);
        spriteX.setSize(sizeX,sizeY);
        spriteAlphabet.put("U", spriteX);

        spriteX = new Sprite(abcBuble, 151, 540, 115, 79);
        spriteX.setPosition(310,80);
        spriteX.setSize(sizeX,sizeY);
        spriteAlphabet.put("V", spriteX);

        spriteX = new Sprite(abcBuble, 288, 540, 115, 79);
        spriteX.setPosition(360,80);
        spriteX.setSize(sizeX,sizeY);
        spriteAlphabet.put("W", spriteX);

        spriteX = new Sprite(abcBuble, 424, 540, 115, 79);
        spriteX.setPosition(410,80);
        spriteX.setSize(sizeX,sizeY);
        spriteAlphabet.put("X", spriteX);

        spriteX = new Sprite(abcBuble, 151, 642, 115, 79);
        spriteX.setPosition(460,80);
        spriteX.setSize(sizeX,sizeY);
        spriteAlphabet.put("Y", spriteX);

        spriteX = new Sprite(abcBuble, 288, 642, 115, 79);
        spriteX.setPosition(510,80);
        spriteX.setSize(sizeX,sizeY);
        spriteAlphabet.put("Z", spriteX);
    }
}
