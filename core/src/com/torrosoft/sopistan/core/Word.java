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
	private int conflict = -1;

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
	
	public int getCharIndex(final Position pos) {
		int result = -1;
		
		
		
		return result;
	}
	
	public void setConflict(final int index) {
		this.conflict = index;
	}
	
	public int getConflict() {
		return this.conflict;
	}
}
