/*
 * File name: Sopistan.java
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
 * Wordsearch puzzle game generator.
 * 
 * @author storro
 */
public final class Sopistan {

	private static Map map;
	private List<Word> readedWords;

	/**
	 * 
	 * @param upperCase
	 */
	public Sopistan(final boolean upperCase) {
		map = new Map(upperCase);
		readedWords = new ArrayList<Word>();
	}

	/**
	 * 
	 */
	public void create() {
		map.randomize();

		// get words
		List<String> list = Utils.readFile("swords");
		for (String str : list) {
			try {
				str = str.trim();
				readedWords.add(new Word(map.isUpperCase() ? str.toUpperCase() : str.toLowerCase()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Creating map ...
		for (final Word w : readedWords) {
			do {
				w.setDirection(Direction.getRandomDirection());
				w.setPosition(Position.getRandomPosition());
				w.set(map.tryToInsertWord(w));
			} while (!w.isSetted());
		}

		// testing
		map.dumpWords();
		map.print();
	}
}
