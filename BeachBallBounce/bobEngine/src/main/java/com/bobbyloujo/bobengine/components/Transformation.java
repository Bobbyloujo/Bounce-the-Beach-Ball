/**
 * BobEngine - 2D game engine for Android
 *
 * Copyright (C) 2014, 2015, 2016 Benjamin Blaszczak
 *
 * BobEngine is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser Public License
 * version 2.1 as published by the free software foundation.
 *
 * BobEngine is provided without warranty; without even the implied
 * warranty of merchantability or fitness for a particular
 * purpose. See the GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with BobEngine; if not, write to the
 * Free Software Foundation, Inc., 51 Franklin Street, Fifth
 * Floor, Boston, MA 02110-1301 USA
 *
 */
package com.bobbyloujo.bobengine.components;

/**
 * A Component of an Entity that contains transform data such as
 * x and y coordinates, dimensions, angle, etc.
 *
 * Created by Benjamin on 11/14/2015.
 */
public interface Transformation extends Component {
	Transformation getParent();
	double getX();
	double getY();
	double getAngle();
	double getWidth();
	double getHeight();
	double getScale();
	int getLayer();
	boolean getVisibility();
	boolean shouldFollowCamera();
}
