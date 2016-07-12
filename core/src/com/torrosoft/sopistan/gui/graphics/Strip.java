/*
 * File name: Strip.java
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

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Strip {

	private Array<Vector2> texcoord = new Array<Vector2>();
	private Array<Vector2> tristrip = new Array<Vector2>();
	private int batchSize;
	private Vector2 perp = new Vector2();
	private float thickness = 50f;
	private float endCap = 100F;// 8.5f;
	private Color color = new Color(Color.RED);
	private ImmediateModeRenderer20 gl20;

	public Strip() {
		gl20 = new ImmediateModeRenderer20(false, true, 1);
	}

	public void draw(Camera cam) {
		if (tristrip.size <= 0) return;

		gl20.begin(cam.combined, GL20.GL_TRIANGLE_STRIP);

		for (int i = 0; i < tristrip.size; i++) {
			if (i == batchSize) {
				gl20.end();
				gl20.begin(cam.combined, GL20.GL_TRIANGLE_STRIP);
			}

			Vector2 point = tristrip.get(i);
			Vector2 tc = texcoord.get(i);
			gl20.color(color.r, color.g, color.b, color.a);
			gl20.texCoord(tc.x, 0f);
			gl20.vertex(point.x, point.y, 0f);
		}

		gl20.end();
	}

	private int generate(Array<Vector2> input, int mult) {
		int c = tristrip.size;

		if (endCap <= 0) {
			tristrip.add(input.get(0));
		} else {
			Vector2 p = input.get(0);
			Vector2 p2 = input.get(1);
			// perp.set(p).sub(p2).mul(endcap);
			perp.set(p).sub(p2).scl(endCap);
			tristrip.add(new Vector2(p.x + perp.x, p.y + perp.y));
		}
		texcoord.add(new Vector2(0f, 0f));

		for (int i = 1; i < input.size - 1; i++) {
			Vector2 p = input.get(i);
			Vector2 p2 = input.get(i + 1);

			// get direction and normalize it
			perp.set(p).sub(p2).nor();

			// get perpendicular
			perp.set(-perp.y, perp.x);

			float thick = thickness * (1f - ((i) / (float) (input.size)));

			// move outward by thickness
			perp.scl(thick / 2f);

			// decide on which side we are using
			perp.scl(mult);

			// add the tip of perpendicular
			tristrip.add(new Vector2(p.x + perp.x, p.y + perp.y));
			// 0.0 -> end, transparent
			texcoord.add(new Vector2(0f, 0f));

			// add the center point
			tristrip.add(new Vector2(p.x, p.y));

			// 1.0 -> center, opaque
			texcoord.add(new Vector2(1f, 0f));
		}

		// final point
		if (endCap <= 0) {
			tristrip.add(input.get(input.size - 1));
		} else {
			Vector2 p = input.get(input.size - 2);
			Vector2 p2 = input.get(input.size - 1);
			perp.set(p2).sub(p).scl(endCap);
			tristrip.add(new Vector2(p2.x + perp.x, p2.y + perp.y));
		}

		// end cap is transparent
		texcoord.add(new Vector2(0f, 0f));

		return tristrip.size - c;
	}

	public void update(Array<Vector2> input) {
		tristrip.clear();
		texcoord.clear();

		if (input.size < 2) return;
		batchSize = generate(input, 2);
		// generate(input, -1);
	}

	//////////////////////////////////////////////////////////////////////////////////
	// Setters
	public void setThickness(final float thickness) {
		this.thickness = thickness;
	}

	public void setEndCap(final float endcap) {
		this.endCap = endcap;
	}

	public void setColor(final Color color) {
		this.color = color;
	}

	public void setColor(final int r, final int g, final int b) {
		setColor(r, g, b, 1);
	}

	public void setColor(final int r, final int g, final int b, final int a) {
		this.color = new Color(r, g, b, a);
	}
	//////////////////////////////////////////////////////////////////////////////////

	//////////////////////////////////////////////////////////////////////////////////
	// Getters
	//////////////////////////////////////////////////////////////////////////////////
}
