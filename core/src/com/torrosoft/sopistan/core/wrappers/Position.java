/*
 * File name: Position.java
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

package com.torrosoft.sopistan.core.wrappers;

import java.util.ArrayList;

import com.torrosoft.sopistan.core.Word;
import com.torrosoft.sopistan.utils.Utils;

/**
 * TODO doc
 * 
 * @author storro
 */
public final class Position {

	public static final short MIN_X = 0;
	public static final short MIN_Y = 0;
	public static final short MAX_X = 15;
	public static final short MAX_Y = 15;

	private int x;
	private int y;
	private boolean isValid;

	public Position(final int x, final int y) {
		setX(x);
		setY(y);
		setValid(true);

		if (x < MIN_X || x > MAX_X) setValid(false);
		if (y < MIN_Y || y > MAX_Y) setValid(false);
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public void setX(final int x) {
		this.x = x;
	}

	public void setY(final int y) {
		this.y = y;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;

		final Position input = (Position) obj;
		if (input != null) {
			if (input.getX() == this.x && input.getY() == this.y) result = true;
		}

		return result;
	}

	public static Position getRandomPosition() {
		return new Position(Utils.randomInt(0, MAX_X), Utils.randomInt(0, MAX_Y));
	}

	// static ?
	public static boolean isInsideBoundaries(final int x, final int y) {
		return isXInsideBoundaries(x) && isYInsideBoundaries(y);
	}

	public static boolean isXInsideBoundaries(final int x) {
		return x >= MIN_X && x <= MAX_X;
	}

	public static boolean isYInsideBoundaries(final int y) {
		return y >= MIN_Y && y <= MAX_Y;
	}

	@Override
	public String toString() {
		return "X = " + this.getX() + " -- Y = " + this.getY();
	}

	public boolean isValid() {
		return this.isValid;
	}

	public void setValid(final boolean valid) {
		this.isValid = valid;
	}
	
	/**
	 * 
	 * @param word
	 * @return
	 */
	public static ArrayList<Position> getPositions(final Word word) {
		return getPositions(word.getPosition(), word.getDirection(), word.size());
	}
	
	/**
	 * 
	 * @param initPos
	 * @param dir
	 * @param size
	 * @return
	 */
	public static ArrayList<Position> getPositions(final Position initPos, final Direction dir, final int size) {
		ArrayList<Position> result = new ArrayList<Position>();
		final int inX = initPos.getX();
		final int inY = initPos.getY();
		
		switch (dir) {
			case Up:
				for (int y = 0; y < size; y++) {
					Position temp = new Position(inX, inY + y);
					result.add(temp);
				}
				break;

			case Down:
				for (int y = 0; y < size; y++) {
					Position temp = new Position(inX, inY - y);
					result.add(temp);
				}
				break;

			case Left:
				for (int x = 0; x < size; x++) {
					Position temp = new Position(inX - x, inY);
					result.add(temp);
				}
				break;

			case Right:
				for (int x = 0; x < size; x++) {
					Position temp = new Position(inX + x, inY);
					result.add(temp);
				}
				break;

			case UpLeft:
				for (int x = 0, y = 0; x < size && y < size; x++, y++) {
					Position temp = new Position(inX - x, inY + y);
					result.add(temp);
				}
				break;

			case UpRight:
				for (int x = 0, y = 0; x < size && y < size; x++, y++) {
					Position temp = new Position(inX + x, inY + y);
					result.add(temp);
				}
				break;

			case DownLeft:
				for (int x = 0, y = 0; x < size && y < size; x++, y++) {
					Position temp = new Position(inX - x, inY - y);
					result.add(temp);
				}
				break;

			case DownRight:
				for (int x = 0, y = 0; x < size && y < size; x++, y++) {
					Position temp = new Position(inX + x, inY - y);
					result.add(temp);
				}
				break;
				
			default: break;
		}
		
		return result;
	}
}
