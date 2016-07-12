/*
 * File name: Map.java
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

import java.util.ArrayList;
import java.util.List;

import com.torrosoft.sopistan.core.wrappers.Direction;
import com.torrosoft.sopistan.core.wrappers.Position;
import com.torrosoft.sopistan.utils.Utils;

/**
 * TODO
 * 
 * @author storro
 */
public final class Map {
	public static final short MAX_WORDS = 10;

	private List<Word> words; // list of inserted words
	private char[][] map = new char[Position.MAX_X][Position.MAX_Y];
	private boolean upperCase;

	/**
	 * Main and unique ctor.
	 * 
	 * @param upper
	 *            Just upper case words or not.
	 */
	public Map(final boolean upper) {
		this.upperCase = upper;
		words = new ArrayList<Word>(MAX_WORDS);
	}

	/**
	 * TODO doc isSameChar
	 * 
	 * @param ch
	 * @param pos
	 * @return
	 */
	public boolean isSameCharAt(final char ch, final Position pos) {
		return map[pos.getX()][pos.getY()] == ch;
	}

	/**
	 * This method checks if the given word can be inserted (ignores other words, just boundaries!).
	 * The input Word have to have a valid Direction and Position.
	 * 
	 * @see Word
	 * @see Direction
	 * @see Position
	 * 
	 * @param word
	 *            The Word to check in the map.
	 * @return True if the given Word can be inserted in map. False otherwise.
	 */
	private boolean checkMapBoundaries(final Word word) {
		boolean result = false;
		final Direction dir = word.getDirection();
		final Position pos = word.getPosition();
		final int nChars = word.size();

		if (dir != null && pos != null) {
			switch (dir) {
				case Up:
					return Position.isYInsideBoundaries(pos.getY() + nChars);

				case Down:
					return Position.isYInsideBoundaries(pos.getY() - nChars);

				case Left:
					return Position.isXInsideBoundaries(pos.getX() - nChars);

				case Right:
					return Position.isXInsideBoundaries(pos.getX() + nChars);

				case UpLeft:
					return Position.isInsideBoundaries(pos.getX() - nChars, pos.getY() + nChars);

				case UpRight:
					return Position.isInsideBoundaries(pos.getX() + nChars, pos.getY() + nChars);

				case DownLeft:
					return Position.isInsideBoundaries(pos.getX() - nChars, pos.getY() - nChars);

				case DownRight:
					return Position.isInsideBoundaries(pos.getX() + nChars, pos.getY() - nChars);

				default: // not possible !
					break;
			}
		}

		return result;
	}

	/**
	 * It ramdomizes the map char array with ramdom chars (just English).
	 * 
	 * @see Utils
	 */
	public void randomize() {
		for (int x = 0; x < Position.MAX_X; x++)
			for (int y = 0; y < Position.MAX_Y; y++)
				map[x][y] = Utils.getRandomChar(this.upperCase);
	}

	/**
	 * @return True if the words in the map are just upper-case.
	 */
	public boolean isUpperCase() {
		return this.upperCase;
	}

	/**
	 * Testing method. It prints in the console the raw text map.
	 */
	public void print() {
		for (int x = 0; x < Position.MAX_X; x++) {
			for (int y = 0; y < Position.MAX_Y; y++) {
				System.out.print(map[x][y] + "  ");
			}
			System.out.println();
		}
		System.out.println();
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	public boolean tryToInsertWord(final Word input) {
		boolean result = false;

		if (input != null) {
			if (input.hasValidVector()) {
				if (checkMapBoundaries(input)) {
					// Check inserted words
					if (words.size() > 0) {
						for (final Word insertedWord : words) {
							Position collision = insertedWord.collides(input);
							if (collision == null) {
								// no collision !
								insertWord(input);
								result = true;
								break;
							} else {
								// check if the char is the same in the given position
								// if (isSameCharAt(insertedWord.getChars(), collision)) {
								// insertWord(input);
								// result = true;
								// break;
								// }

								System.out.println("COLLIDES !");
							}
						}
					} else {
						insertWord(input);
						result = true;
					}
				}
			}
		}

		return result;
	}

	private boolean insertWord(final Word input) {
		String word = input.toString();
		if (Sopistan.DEBUG_MODE) {
			word = isUpperCase() ? input.toString().toLowerCase() : input.toString().toUpperCase();
		}

		final char[] chars = word.toCharArray();
		final Direction dir = input.getDirection();
		final Position pos = input.getPosition();

		if (chars == null || chars.length <= 0 || dir == null || pos == null) return false;

		try {
			int y = 0;
			int x = 0;
			switch (dir) {
				case Up:
					for (char ch : chars)
						map[pos.getX()][pos.getY() + y++] = ch;
					break;

				case Down:
					for (char ch : chars)
						map[pos.getX()][pos.getY() - y++] = ch;
					break;

				case Left:
					for (char ch : chars)
						map[pos.getX() - x++][pos.getY()] = ch;
					break;

				case Right:
					for (char ch : chars)
						map[pos.getX() + x++][pos.getY()] = ch;
					break;

				case UpLeft:
					for (char ch : chars)
						map[pos.getX() - x++][pos.getY() + y++] = ch;
					break;

				case UpRight:
					for (char ch : chars)
						map[pos.getX() + x++][pos.getY() + y++] = ch;
					break;

				case DownLeft:
					for (char ch : chars)
						map[pos.getX() - x++][pos.getY() - y++] = ch;
					break;

				case DownRight:
					for (char ch : chars)
						map[pos.getX() + x++][pos.getY() - y++] = ch;
					break;

				default: // not possible !
					break;
			}
		} catch (ArrayIndexOutOfBoundsException ex) { // not acceptable
			System.out.println("ArrayIndexOutOfBoundsException!! - " + input.toString());
			System.out.println(" - " + dir.toString());
			System.out.println(" - " + pos.toString());
			return false;
		}

		// input.set(true);
		words.add(input);
		return true;
	}

	public void dumpWords() {
		System.out.println("--------------------------");
		System.out.println(" - dumpWords - ");
		for (final Word w : words) {
			System.out.println(w.toString());
			System.out.println(w.getPosition());
			System.out.println(w.getDirection());
			System.out.println("Setted: " + w.isSetted());
			System.out.println(" ------------- ");
		}
		System.out.println("--------------------------");
	}

	/**
	 * 
	 * @param input
	 * @return The p
	 */
	private Position getCollision(final Word input) {
		Position result = null;

		for (final Word w : words) {
			// check same word ?

			final Position pos = w.getPosition();
			final Direction dir = w.getDirection();
			if (pos == null || dir == null) continue;

			final int size = w.size();
			final int x = pos.getX();
			final int y = pos.getY();

			// TODO getCollision
		}

		return result;
	}
}
