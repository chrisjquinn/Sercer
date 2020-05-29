package Frontend;

import Backend.SERC;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/*  ---------------     NO LONGER USED      ------------------- */

/*  ResizableCanvas

    made so that a canvas object in JavaFX can be resized when the window is resized.
    Didn't resize, the SceneBuilder does not recognize this object either and will
    throw errors.
 */

public class ResizableCanvas extends Canvas {
        public SERC currentSERC;

        @Override
        public double minHeight(double width)
        {
            return 300;
        }

        @Override
        public double maxHeight(double width)
        {
            return 100000;
        }

        @Override
        public double prefHeight(double width)
        {
            return super.getHeight();
        }

        @Override
        public double minWidth(double height)
        {
            return 300;
        }

        @Override
        public double maxWidth(double height)
        {
            return 100000000;
        }

        @Override
        public double prefWidth(double height)
        {
            return super.getWidth();
        }

        @Override
        public boolean isResizable()
        {
            return true;
        }

        @Override
        public void resize(double width, double height)
        {
            super.setWidth(width);
            super.setHeight(height);
        }

        public void setSERC(SERC serc){
            this.currentSERC = serc;
            this.drawSERC();
        }

        public void drawSERC()
        {
            GraphicsContext gc = this.getGraphicsContext2D();
            for(int i = 0; i  < currentSERC.getWidth(); i++){
                for(int j = 0; j < currentSERC.getLength(); j++){
                    if (j==50 || i ==50){
                        System.out.println("Got j or i a bit too big here");
                    }
                    gc.setFill(this.setColor(currentSERC.grid[i][j].getIntValue()));
                    gc.fillRect(i * 10,j * 10,10,10);       //Doesn't seem to be enlarging the rectangle
                }
            }
        }

         public Color setColor(int i) {
              switch (i) {
                  case 0: return Color.WHITE;     //Empty
                  case 1: return Color.BLACK;     //Wall
                  case 2: return Color.ORANGE;    //Casualty
                  case 3: return Color.GREEN;     //Aids
                  case 4: return Color.RED;       //Danger / Out of Bounds
                  case 5: return Color.BLUE;      //Water
                  default: return Color.BEIGE; //For when something goes wrong
              }
         }



}