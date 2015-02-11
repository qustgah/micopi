/*
 * Copyright (C) 2014 Easy Target
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ijiaban.yinxiang.engine;

import android.graphics.Canvas;
import android.util.Log;

import com.ijiaban.yinxiang.Contact;

/**
 * Created by michel on 12/11/14.
 *
 */
public class WanderingShapesGenerator {

    /**
     * Fills a canvas with a lot of colourful circles or polygon approximations of circles
     * Uses Painter
     *
     * @param canvas Canvas to draw on
     * @param contact Data from this Contact object will be used to generate the shapes
     */
    public static void generate(Canvas canvas, Contact contact) {
        // If the first name has at least 3 (triangle) and no more than 6 (hexagon) letters,
        // there is a 2/3 chance that polygons will be painted instead of circles.
        final int numOfEdges = contact.getNameWord(0).length();
        final String md5String = contact.getMD5EncryptedString();

        // Some pictures have polygon approximations instead of actual circles.
        boolean paintPolygon = false;
        if (md5String.charAt(15) % 3 != 0 && numOfEdges > 2 && numOfEdges < 7) paintPolygon = true;

        // These characters will be used for color generating:
        final char colorChar1     = contact.getFullName().charAt(0);
        final int lastNamePart    = contact.getNumberOfNameWords() - 1;
        final char colorChar2     = contact.getNameWord(lastNamePart).charAt(0);

        // Determine if the shapes will be painted filled or stroked.
        boolean paintFilled = false;
        if (md5String.charAt(0) % 2 == 0) paintFilled = true;

        // Determine the alpha value to paint with.
        int alpha  = md5String.charAt(6) * 2;
        // Filled shapes have a smaller alpha value.
        if (paintFilled) alpha /= 2;
        //Log.i("Circle Scape", "Alpha: " + alpha + " paintFilled: " + paintFilled);
        // This was 0.1
        float shapeWidth = (float) md5String.charAt(7) * 2f;

        // Determine if to paint occasional arcs or not.
        boolean paintArc = true;
        final float endAngle = md5String.charAt(8) * 2;
        if (md5String.charAt(1) % 2 == 0) paintArc = false;

        // Draw all the shapes.
        final int md5Length  = md5String.length();
        int md5Pos = 0;
        float x = canvas.getWidth() * 0.5f;
        float y = x;

        // The amount of double shapes that will be painted; at least 10, no more than 25.
        int numberOfShapes = contact.getNumberOfLetters() * 4;
        numberOfShapes = Math.min(numberOfShapes, 25);
        while (numberOfShapes < 10) numberOfShapes *= 2;
        Log.d("Number of Circle Scape shapes", contact.getFullName() + " " + numberOfShapes);

        for (int i = 0; i < numberOfShapes; i++) {
            // Get the next character from the MD5 String.
            md5Pos++;
            if (md5Pos >= md5Length) md5Pos = 0;

            // Move the coordinates around.
            final int md5Int = md5String.charAt(md5Pos) + i;
            switch (md5Int % 6) {
                case 0:
                    x += md5Int;
                    y += md5Int;
                    break;
                case 1:
                    x -= md5Int;
                    y -= md5Int;
                    break;
                case 2:
                    x += md5Int * 2;
                    break;
                case 3:
                    y += md5Int * 2;
                    break;
                case 4:
                    x -= md5Int * 2;
                    y -= md5Int;
                    break;
                default:
                    x -= md5Int;
                    y -= md5Int * 2;
                    break;
            }

            int paintMode = Painter.MODE_CIRCLE;
            if (paintArc && md5Int % 2 == 0) paintMode = Painter.MODE_ARC;
            else if (paintPolygon) paintMode = Painter.MODE_POLYGON;
            //if (paintFilled) paintMode += Painter.MODE_CIRCLE_FILLED;

            // The new coordinates have been generated. Paint something.
            Painter.paintDoubleShape(
                    canvas,
                    paintMode,
                    ColorCollection.generateColor(colorChar1, colorChar2, md5Int, i + 1),
                    alpha,
                    shapeWidth,
                    numOfEdges,
                    md5Int * 2,            // Start Angle of Arc
                    endAngle,
                    x,
                    y,
                    i * md5String.charAt(2) // Radius
            );
            shapeWidth += .05f;
        }
    }
}
