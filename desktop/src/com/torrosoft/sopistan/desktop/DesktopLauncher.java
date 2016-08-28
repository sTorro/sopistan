/*
 * File name: DesktopLauncher.java
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

package com.torrosoft.sopistan.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.torrosoft.sopistan.SopistanMain;
import com.torrosoft.sopistan.core.Sopistan;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 800;
        config.height = 600;

		new LwjglApplication(new SopistanMain(), config);

		// quick test
		for (int i = 0; i < 1; i++) {
			Sopistan sopa = new Sopistan(i % 2 == 0);
			sopa.create();
		}
	}
}
