/*
 * File name: Direction.java
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

import com.torrosoft.sopistan.utils.Utils;

/**
 * 
 * 
 * @author storro
 */
public enum Direction {
	/** +y */
	Up,

	/** -y */
	Down,

	/** -x */
	Left,

	/** +x */
	Right,

	/** +y +x */
	UpRight,

	/** +y -x */
	UpLeft,

	/** -y +x */
	DownRight,

	/** -y -x */
	DownLeft;

	public static Direction getRandomDirection() {
		int rnd = Utils.randomInt(0, 7);
		if (rnd == 0) return Direction.Down;
		else if (rnd == 1) return Direction.Left;
		else if (rnd == 2) return Direction.Right;
		else if (rnd == 3) return Direction.UpRight;
		else if (rnd == 4) return Direction.UpLeft;
		else if (rnd == 5) return Direction.DownRight;
		else if (rnd == 6) return Direction.DownLeft;
		else return Direction.Up; // 7
	}
}
