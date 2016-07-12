/*
 * File name: FingerHandler.java
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

package com.torrosoft.sopistan.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.torrosoft.sopistan.core.wrappers.FixedList;
import com.torrosoft.sopistan.gui.graphics.DrawResolver;

/**
 * 
 * @author storro
 */
public class FingerHandler extends InputAdapter {
	private FixedList<Vector2> inputPoints;
	private int inputPointer = 0; // The pointer associated with this swipe event
	private int initialDistance = 10;
	private int minDistance = 20;
	private Vector2 lastPoint = new Vector2();
	private boolean isDrawing = false;
	private DrawResolver simplifier = new DrawResolver();
	private Array<Vector2> simplified;

	/**
	 * 
	 * @param maxInputPoints
	 */
	public FingerHandler(int maxInputPoints) {
		this.inputPoints = new FixedList<Vector2>(maxInputPoints, Vector2.class);
		simplified = new Array<Vector2>(true, maxInputPoints, Vector2.class);
		resolve(); // copy initial empty list
	}

	/**
	 * Returns the fixed list of input points (not simplified).
	 * 
	 * @return the list of input points
	 */
	public Array<Vector2> getInput() {
		return this.inputPoints;
	}

	/**
	 * Returns the simplified list of points representing this swipe.
	 * 
	 * @return
	 */
	public Array<Vector2> getPath() {
		return simplified;
	}

	/**
	 * If the points are dirty, the line is simplified.
	 */
	public void resolve() {
		simplifier.resolve(inputPoints, simplified);
	}

	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (pointer != inputPointer) return false;
		isDrawing = true;

		// clear points
		inputPoints.clear();

		// starting point
		lastPoint = new Vector2(screenX, Gdx.graphics.getHeight() - screenY);
		inputPoints.insert(lastPoint);

		resolve();
		return true;
	}

	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// on release, the line is simplified
		resolve();
		isDrawing = false;
		return false;
	}

	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (pointer != inputPointer) return false;
		isDrawing = true;

		Vector2 v = new Vector2(screenX, Gdx.graphics.getHeight() - screenY);

		// calc length
		float dx = v.x - lastPoint.x;
		float dy = v.y - lastPoint.y;
		float len = (float) Math.sqrt(dx * dx + dy * dy);
		// TODO: use minDistanceSq

		// if we are under required distance
		if (len < minDistance && (inputPoints.size > 1 || len < initialDistance)) return false;

		// add new point
		inputPoints.insert(v);
		lastPoint = v;

		resolve(); // simplify our new line

		return true;
	}

	public boolean isDrawing() {
		return this.isDrawing;
	}

	/**
	 * The minimum distance between the first and second point in a drawn line.
	 * 
	 * @param initialDistance
	 */
	public void setInitialDistance(final int initialDistance) {
		this.initialDistance = initialDistance;
	}

	/**
	 * The minimum distance between two points in a drawn line (starting at the second point).
	 * 
	 * @param minDistance
	 */
	public void setMinDistance(final int minDistance) {
		this.minDistance = minDistance;
	}
}
