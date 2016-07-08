/*
 * File name: Utils.java
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

package com.torrosoft.sopistan.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

/**
 * 
 * @author storro
 */
public final class Utils {

	/**
	 * Testing purpouses
	 * 
	 * @param fileName
	 * @return
	 */
	public static final List<String> readFile(final String fileName) {
		FileHandle file = Gdx.files.internal(fileName);
		String wordsStr = file.readString();
		StringTokenizer tokenizer = new StringTokenizer(wordsStr, ",");
		List<String> result = new ArrayList<String>();

		while (tokenizer.hasMoreTokens()) {
			String word = tokenizer.nextToken();
			if (word != null && !word.isEmpty()) {
				try {
					result.add(word);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}

	/**
	 * 
	 * @param max
	 * @param min
	 * @return
	 */
	public static final int randomInt(int min, int max) {
		return (int) (Math.random() * max + min);
	}

	/**
	 * 
	 * @param upperCase
	 * @return
	 */
	public static final char getRandomChar(final boolean upperCase) {
		int mod = (int) (Math.random() * 25 + 1);
		return (char) ((upperCase ? 'A' : 'a') + mod);
	}
}
