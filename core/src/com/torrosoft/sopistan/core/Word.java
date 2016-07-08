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

	/**
	 * TODO if there are a collision check for equals characters !
	 * 
	 * check this method dude
	 * 
	 * @param input
	 * @return
	 */
	public boolean collides(final Word input) {
		final Position inPos = input.getPosition();
		final Direction inDir = input.getDirection();

		if (inPos != null && inDir != null) {
			if (inPos.equals(this.position)) {
				// same initial position, check first char and direction
				if (input.getChars()[0] == this.getChars()[0] && !inDir.equals(this.direction)) {
					// no collision
					return false;
				}
			} else {
				// Initial positions are different
				int inX = inPos.getX();
				int inY = inPos.getY();

				switch (inDir) {
					case Up:
						for (int y = 0; y < Position.MAX_Y; y++) {
							Position temp = new Position(inX, inY + y);
							if (temp.equals(this.position)) return true;
						}
						break;

					case Down:
						for (int y = 0; y < Position.MAX_Y; y++) {
							Position temp = new Position(inX, inY - y);
							if (temp.equals(this.position)) return true;
						}
						break;

					case Left:
						for (int x = 0; x < Position.MAX_X; x++) {
							Position temp = new Position(inX - x, inY);
							if (temp.equals(this.position)) return true; // it collides
						}
						break;

					case Right:
						for (int x = 0; x < Position.MAX_X; x++) {
							Position temp = new Position(inX + x, inY);
							if (temp.equals(this.position)) return true; // it collides
						}
						break;

					case UpLeft:
						for (int x = 0, y = 0; x < Position.MAX_X && y < Position.MAX_Y; x++, y++) {
							Position temp = new Position(inX - x, inY + y);
							if (temp.equals(this.position)) return true; // it collides
						}
						break;

					case UpRight:
						for (int x = 0, y = 0; x < Position.MAX_X && y < Position.MAX_Y; x++, y++) {
							Position temp = new Position(inX + x, inY + y);
							if (temp.equals(this.position)) return true; // it collides
						}
						break;

					case DownLeft:
						for (int x = 0, y = 0; x < Position.MAX_X && y < Position.MAX_Y; x++, y++) {
							Position temp = new Position(inX - x, inY - y);
							if (temp.equals(this.position)) return true; // it collides
						}
						break;

					case DownRight:
						for (int x = 0, y = 0; x < Position.MAX_X && y < Position.MAX_Y; x++, y++) {
							Position temp = new Position(inX + x, inY - y);
							if (temp.equals(this.position)) return true; // it collides
						}
						break;

					default: // not possible !
						return true;
				}

			}
		}

		return false;
	}
}
