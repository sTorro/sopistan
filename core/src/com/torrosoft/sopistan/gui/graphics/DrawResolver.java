/*
 * File name: DrawResolver.java
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

package com.torrosoft.sopistan.gui.graphics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * TODO doc DrawResolver
 * 
 * @author storro
 */
public class DrawResolver {

	private Array<Vector2> tmp = new Array<Vector2>(Vector2.class);
	private int iterations = 2;
	private float simplifyTolerance = 35F;

	/**
	 * TODO doc resolve
	 * 
	 * @param input
	 * @param output
	 */
	public void resolve(Array<Vector2> input, Array<Vector2> output) {
		output.clear();

		if (input.size <= 2) { // simple copy
			output.addAll(input);
			return;
		}

		// simplify with squared tolerance
		if (simplifyTolerance > 0 && input.size > 3) {
			simplify(input, simplifyTolerance * simplifyTolerance, tmp);
			input = tmp;
		}

		// perform smooth operations
		if (iterations <= 0) { // no smooth, just copy input to output
			output.addAll(input);
		} else if (iterations == 1) { // 1 iteration, smooth to output
			smooth(input, output);
		} else { // multiple iterations.. ping-pong between arrays
			int iters = iterations;
			do {
				smooth(input, output);
				tmp.clear();
				tmp.addAll(output);
				Array<Vector2> old = output;
				input = tmp;
				output = old;
			} while (--iters > 0);
		}
	}

	/**
	 * TODO doc smooth
	 * 
	 * @param input
	 * @param output
	 */
	private static void smooth(Array<Vector2> input, Array<Vector2> output) {
		// expected size
		output.clear();
		output.ensureCapacity(input.size * 2);

		// first element
		output.add(input.get(0));

		// average elements
		for (int i = 0; i < input.size - 1; i++) {
			Vector2 p0 = input.get(i);
			Vector2 p1 = input.get(i + 1);

			Vector2 Q = new Vector2(0.75f * p0.x + 0.25f * p1.x, 0.75f * p0.y + 0.25f * p1.y);
			Vector2 R = new Vector2(0.25f * p0.x + 0.75f * p1.x, 0.25f * p0.y + 0.75f * p1.y);
			output.add(Q);
			output.add(R);
		}

		// last element
		output.add(input.get(input.size - 1));
	}

	/**
	 * Simple distance-based simplification. TODO doc simplify
	 * 
	 * @param points
	 * @param sqTolerance
	 * @param out
	 */
	private static void simplify(Array<Vector2> points, float sqTolerance, Array<Vector2> out) {
		int len = points.size;
		Vector2 point = new Vector2();
		Vector2 prevPoint = points.get(0);

		out.clear();
		out.add(prevPoint);

		for (int i = 1; i < len; i++) {
			point = points.get(i);

			if (distSq(point, prevPoint) > sqTolerance) {
				out.add(point);
				prevPoint = point;
			}
		}

		if (!prevPoint.equals(point)) out.add(point);
	}

	/**
	 * TODO doc distSq
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	private static float distSq(Vector2 p1, Vector2 p2) {
		float dx = p1.x - p2.x;
		float dy = p1.y - p2.y;
		return dx * dx + dy * dy;
	}

	//////////////////////////////////////////////////////////////////////////////////
	// Setters
	public void setIterations(final int it) {
		this.iterations = it;
	}

	public void setTolerance(final float simplifyTolerance) {
		this.simplifyTolerance = simplifyTolerance;
	}
	//////////////////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////////////////
	// Getters
	public int getIterations() {
		return this.iterations;
	}

	public float getTolerance() {
		return this.simplifyTolerance;
	}
	//////////////////////////////////////////////////////////////////////////////////
}
