/*
 * Copyright 2004 Andreas Siebert (j2biz community)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.j2biz.pencil.examples.simpleClient;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.j2biz.log.LOG;

/**
 * @author Andreas Siebert
 * 
 * (c) 2004 by Andreas Siebert / j2biz.com
 */
public class PlasmaAnimation extends Canvas {

       
       static {
              String className = PlasmaAnimation.class.getName();
              LOG.trace( "initialize plazma-class: ${className}" );
       }
       
       private static final int MAXX     = 199;

       private static final int MAXY     = 199;

       /**
        * die x0 koeffizienten ...
        */
       private static double    C        = 100;

       private static double    F        = 67;

       private static double    I        = 32;

       private static double    L        = 8;

       private double           time1    = C;

       private double           time2    = F;
 
       private double           time3    = I;

       private double           time4    = L;

       private double           angle3   = time3;

       private double           angle4   = time4;

       private double           angle1   = time1;

       private double           angle2   = time2;

       Thread                   t;

       private double[]         cosTable = new double[256];

       {      
              LOG.debug( "prepare color-table" );
       
              for (int i = 0; i < cosTable.length; i++) {
                     double colorValue = 32 + 32 * Math.cos(i * 2 * Math.PI / 256);
                     cosTable[i] = colorValue;
                     LOG.trace( "index[${i}] == ${colorValue}" );
              }
       }

       public void start() {
              t = new Thread() {

                     /**
                      * @see java.lang.Thread#run()
                      */
                     public void run() {
                            try {
                                   while (true) {
                                          PlasmaAnimation.this.repaint();
                                          Thread.sleep(100);
                                   }
                            } catch (InterruptedException e) {
                                   e.printStackTrace();
                                   System.exit(-1);
                            }
                     }
              };

              t.start();
       }

       public static void main(String args[]) {
              LOG.trace("");

              int mod = 4 % 2;

//              System.out.println(" modulo goes on: " + (128 & 127));

              Frame frame = new Frame("plasme, baby!");
              frame.setSize(MAXX + 1, MAXY + 1);

              PlasmaAnimation anim = new PlasmaAnimation();
              frame.add(anim);
              frame.setVisible(true);

              anim.start();
       }
       
       BufferedImage img = new BufferedImage( MAXX+1, MAXY+1,BufferedImage.TYPE_INT_RGB ) ;

       /**
        * 
        */
       private void paintImg(Graphics g) {
              angle3 = time3;
              angle4 = time4;

              for (int y = 0; y <= MAXY; y++) {
                     angle1 = time1;
                     angle2 = time2;
                     for (int x = 0; x <= MAXX; x++) {
                            double color = this.cosTable[(int) Math.round(angle1) & 255] + this.cosTable[(int) Math.round(angle2) & 255]
                                          + cosTable[(int) Math.round(angle3) & 255] + cosTable[(int) Math.round(angle4) & 255];
                            
                            g.setColor( new Color((int) color) );
                            g.drawLine( x, y, x, y);
                            angle1 += A;
                            angle2 += D;
                     }
                     
                     angle3 += G;
                     angle4 += J;
              }

              time1 += B;
              time2 += E;
              time3 += H;
              time4 += K;
       }

       /**
        * @see java.awt.Canvas#paint(java.awt.Graphics)
        */
       public void paint(Graphics g) {
//              paintImg( img.getGraphics() );
//              g.drawImage(img, 0, 0, null);
                paintImg( g );
       }

       private static double A = 12;
       private static double D = 4;
       private static double J = 46;
       private static double B = 123;
       private static double E = 67;
       private static double H = 76;
       private static double K = 12;
       private static double G = 3;
       
}