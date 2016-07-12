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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.torrosoft.sopistan.gui.graphics.Strip;
import com.torrosoft.sopistan.input.FingerHandler;

/**
 * 
 * @author storro
 */
public class SopistanMain extends ApplicationAdapter {
	OrthographicCamera cam;
	SpriteBatch batch;
	FingerHandler swipe;
	Texture tex;
	ShapeRenderer shapes;
	Strip tris;

	@Override
	public void create() {
		batch = new SpriteBatch();

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

		// uncomment to see debug lines
		// drawDebug();
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
}
