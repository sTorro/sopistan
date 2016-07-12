/*
 * File name: Word.java
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

package com.torrosoft.sopistan.core;

import com.torrosoft.sopistan.core.wrappers.Direction;
import com.torrosoft.sopistan.core.wrappers.Position;

/**
 * 
 * @author storro
 */
public final class Word {
	private static final short MAX_CHARS_PER_WORD = 10;

	private String value;
	private Direction direction = null;
	private Position position = null;
	private boolean isSetted = false;

	public Word(final String input) throws Exception {
		if (input.length() > MAX_CHARS_PER_WORD) throw new Exception() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getMessage() {
				return "The given string (" + input + ") is larger than MAX_CHARS_PER_WORD (" + MAX_CHARS_PER_WORD + ")";
			}
		};

		this.value = input;
	}

	public void setDirection(final Direction dir) {
		this.direction = dir;
	}

	public Direction getDirection() {
		return this.direction;
	}

	public void setPosition(final Position pos) {
		this.position = pos;
	}

	public Position getPosition() {
		return this.position;
	}

	public char[] getChars() {
		return this.value.toCharArray();
	}

	public boolean hasValidVector() {
		return this.direction != null && this.position != null;
	}

	public boolean isSetted() {
		return hasValidVector() && isSetted;
	}

	public void set(final boolean val) {
		this.isSetted = val;
	}

	@Override
	public String toString() {
		return this.value;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;

		final Word input = (Word) obj;
		if (input != null) {
			if (input.toString().equals(this.value)) return true;
		}

		return result;
	}

	public int size() {
		return value.length();
	}

	public boolean isSameCharAt(final char ch, final int index) {
		if (index > this.size()) return false;
		return this.getChars()[index] == ch;
	}

	/**
	 * TODO doc collides
	 * 
	 * @param input
	 * @return The position where collides, null if no collision
	 */
	public Position collides(final Word input) {
		final Position inPos = input.getPosition();
		final Direction inDir = input.getDirection();
		Position collision = null; // the result

		if (inPos != null && inDir != null) {
			if (inPos.equals(this.position)) {
				if (inDir.equals(this.direction)) collision = inPos; // same position and direction

				// Same initial position, check first char and direction
				// if (isSameCharAt(input.getChars()[0], 0)) return false;
				//
				// if (input.getChars()[0] == this.getChars()[0]) {
				// // no collision
				// return false;
				// }

			} else { // Initial positions are different, do the magic
				final int inX = inPos.getX();
				final int inY = inPos.getY();
				final int size = input.size();

				switch (inDir) {
					case Up:
						for (int y = 0; y < size; y++) {
							Position temp = new Position(inX, inY + y);
							if (temp.equals(this.position)) collision = temp; // it collides
						}
						break;

					case Down:
						for (int y = 0; y < size; y++) {
							Position temp = new Position(inX, inY - y);
							if (temp.equals(this.position)) collision = temp;
						}
						break;

					case Left:
						for (int x = 0; x < size; x++) {
							Position temp = new Position(inX - x, inY);
							if (temp.equals(this.position)) collision = temp;
						}
						break;

					case Right:
						for (int x = 0; x < size; x++) {
							Position temp = new Position(inX + x, inY);
							if (temp.equals(this.position)) collision = temp;
						}
						break;

					case UpLeft:
						for (int x = 0, y = 0; x < size && y < size; x++, y++) {
							Position temp = new Position(inX - x, inY + y);
							if (temp.equals(this.position)) collision = temp;
						}
						break;

					case UpRight:
						for (int x = 0, y = 0; x < size && y < size; x++, y++) {
							Position temp = new Position(inX + x, inY + y);
							if (temp.equals(this.position)) collision = temp;
						}
						break;

					case DownLeft:
						for (int x = 0, y = 0; x < size && y < size; x++, y++) {
							Position temp = new Position(inX - x, inY - y);
							if (temp.equals(this.position)) collision = temp;
						}
						break;

					case DownRight:
						for (int x = 0, y = 0; x < size && y < size; x++, y++) {
							Position temp = new Position(inX + x, inY - y);
							if (temp.equals(this.position)) collision = temp;
						}
						break;

					default: // not possible !
						return new Position(0, 0);
				}
			}
		}

		return collision; // no collision
	}
}
