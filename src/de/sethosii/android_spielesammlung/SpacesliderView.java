package de.sethosii.android_spielesammlung;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class SpacesliderView extends View {

	private class StarMap {
		
		private abstract class SpaceEntity {
			int x = 0, y = 0;
			float speed = 0, size = 0;

			protected SpaceEntity() {
				x = Math.round((float)Math.random()*(float)xMax);
				speed = (float)Math.random()*20.0f + 1.0f;
			}

			public void step() {
				y = Math.round(y + speed);
				if (y > yMax) {
					y %= yMax;
					x = Math.round((float)Math.random()*(float)xMax);
				}
			}
		}

		private class Star extends SpaceEntity {
			public Star() {
				super();

				y = Math.round((float)Math.random()*(float)yMax);
				size = (float)Math.random()*2.0f + 0.5f;
			}
		}

		private class Asteroid extends SpaceEntity {
			int color;

			public Asteroid() {
				super();

				y = 0;
				size = (float)Math.random()*10.0f + 10.0f;
				
				switch ((int)(Math.random() * 3.0f)) {
				case 0:
					color = Color.argb(255, 255, 255, 128);
					break;
				case 1:
					color = Color.argb(255, 192, 192, 255);
					break;
				default:
					color = Color.argb(255, 255, 192, 192);
				}
			}

			public boolean testCollision(Region regionShip, Region regionClip) {
				Path pathThis = new Path();
				pathThis.addCircle(x, y, size, Path.Direction.CW);

				Region regionThis = new Region();
				regionThis.setPath(pathThis, regionClip);

				// intersection test, first a quick one based on rectangle
				return ((!regionThis.quickReject(regionShip)) && regionThis.op(regionShip, Region.Op.INTERSECT));
			}
		}

		List<Star> stars = new ArrayList<Star>();
		List<Asteroid> asteroids = new ArrayList<Asteroid>();

		private void setStarCount(int count) {
			while (stars.size() > count) {
				stars.remove(0);
			}
			while (stars.size() < count) {
				stars.add(new Star());
			}
		}

		private void setAsteroidCount(int count) {
			while (asteroids.size() > count) {
				asteroids.remove(0);
			}
			while (asteroids.size() < count) {
				asteroids.add(new Asteroid());
			}
		}

		private void paintTo(Canvas canvas) {
		    paint.setColor(Color.WHITE);
			for (Star star : stars) {
				canvas.drawCircle(star.x, star.y, star.size, paint);
			}
			for (Asteroid astObj : asteroids) {
			    paint.setColor(astObj.color);
				canvas.drawCircle(astObj.x, astObj.y, astObj.size, paint);
			}
		}
		
		private void stepStars() {
			for (Star star : stars) {
				star.step();
			}
			for (Asteroid astObj : asteroids) {
				astObj.step();
			}
		}

		private boolean testCollisionAll() {
			Region regionClip = new Region(xMin, yMin, xMax, yMax);
			Region regionShip = new Region();
			regionShip.setPath(pathShip, regionClip);

			for (Asteroid astObj : asteroids) {
				if (astObj.testCollision(regionShip, regionClip)) {
					return true;
				}
			}
			return false;
		}

	}

        private class SpaceShip {
            private float shipWidth = 80;  // ship's radius
            private float shipX = shipWidth + 20;  // ship's center (x,y)
            private float shipY = shipWidth + 450; //TODO
            private float shipSpeedX = 5;  // ship's horizontal speed
            private RectF shipBounds;      // Needed for Canvas.drawOval

            private Path pathWings = new Path();
            private Path pathBody = new Path();

            public SpaceShip() {
  		        shipBounds = new RectF();

  		      float posX = 0/*ballX*/;
  			  float posY = 0/*ballY*/;
  			  //
  		      pathWings.reset();
  		      pathWings.setFillType(Path.FillType.WINDING);
  		      pathWings.moveTo(posX-shipWidth, posY+shipWidth);
  		      pathWings.lineTo(posX+shipWidth, posY+shipWidth);
  		      pathWings.lineTo(posX, posY);
  		      pathWings.close();

  		      //
  		      int bodyRadius = 10;
  		      int noseLen = 10;
  		      pathBody.reset();
  		      pathBody.setFillType(Path.FillType.WINDING);
  		      pathBody.moveTo(posX-bodyRadius, posY+shipWidth);
  		      pathBody.lineTo(posX+bodyRadius, posY+shipWidth);
  		      pathBody.lineTo(posX+bodyRadius, posY-shipWidth+noseLen);
  		      pathBody.lineTo(posX+bodyRadius/2, posY-shipWidth);
  		      pathBody.lineTo(posX-bodyRadius/2, posY-shipWidth);
  		      pathBody.lineTo(posX-bodyRadius, posY-shipWidth+noseLen);
  		      pathBody.close();
  		      
            }

            private void paintTo(Canvas canvas) {
      	      paint.setColor(android.graphics.Color.LTGRAY);

    	      pathShip.reset();
    	      pathShip.addPath(pathWings, shipX, shipY);
    	      pathShip.addPath(pathBody, shipX, shipY);
    	      canvas.drawPath(pathShip, paint);

    	      //canvas.drawPath(pathWings, paint);
    	      //canvas.drawPath(pathBody, paint);

    		}

            public void step(float value) {
      	      // Get new (x,y) position
      	      shipX -= Math.round(60.0f * value);
      	      //shipX += shipSpeedX;
      	      //shipY += shipSpeedY;
      	      // Detect collision and react
      	      if (shipX + shipWidth > xMax) {
      	         shipSpeedX = -shipSpeedX;
      	         shipX = xMax-shipWidth;
      	      } else if (shipX - shipWidth < xMin) {
      	         shipSpeedX = -shipSpeedX;
      	         shipX = xMin+shipWidth;
      	      }

    	      // Build status message
    	      statusMsg.delete(0, statusMsg.length());   // Empty buffer
    	      formatter.format("Ball@(%3.0f,%3.0f), Speed=(%2.0f), Neig(%f)",
    	    		  shipX, shipY, shipSpeedX, value);
			}
        }

	   private int xMin = 0;          // This view's bounds
	   private int xMax;
	   private int yMin = 0;
	   private int yMax;
	   private SpaceShip ship = new SpaceShip();
	   private Paint paint;           // The paint (e.g. style, color) used for drawing

	   // For touch inputs - previous touch (x, y)
	   private float previousX;
	   private float previousY;

	   // Status message to show Ball's (x,y) position and speed.
	   private StringBuilder statusMsg = new StringBuilder();
	   private Formatter formatter = new Formatter(statusMsg);
	   
	   private TiltCalc tc;
	   
	   StarMap starMap = new StarMap();

	   // Constructor
	   public SpacesliderView(Context context) {
		      super(context);
		      
		      this.setKeepScreenOn(true);
		      this.setBackgroundColor(Color.BLACK);
		      
		      paint = new Paint(Paint.ANTI_ALIAS_FLAG);  //TODO new Paint();
		      // Set the font face and size of drawing text
		      paint.setTypeface(Typeface.MONOSPACE);
		      paint.setTextSize(16);

		      // To enable keypad
		      this.setFocusable(true);
		      this.requestFocus();

		      // To enable touch mode
		      this.setFocusableInTouchMode(true);
		      
		      //

			  tc = new TiltCalc(context);

       }
	  
	   
       Path pathShip = new Path();
       Path pathDel = new Path();

       int mDialHeight = 590/*255*/;
       int mDialWidth = 1280/*480*/;
       int widthSize;
       int heightSize;
       float viewScale;

       @Override
       protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

           int widthMode = MeasureSpec.getMode(widthMeasureSpec);
           widthSize = MeasureSpec.getSize(widthMeasureSpec);
           int heightMode = MeasureSpec.getMode(heightMeasureSpec);
           heightSize = MeasureSpec.getSize(heightMeasureSpec);

           float hScale = 1.0f;
           float vScale = 1.0f;

           if (widthMode != MeasureSpec.UNSPECIFIED /*&& widthSize < mDialWidth*/) {
               hScale = (float)widthSize / (float)mDialWidth;
           }

           if (heightMode != MeasureSpec.UNSPECIFIED /*&& heightSize < mDialHeight*/) {
               vScale = (float)heightSize / (float)mDialHeight;
           }

           viewScale = Math.min(hScale, vScale);

           int a1, a2, b1, b2;
           a1 = (int)((float)widthSize / (float)viewScale);
           a2 = (int)((float)heightSize / (float)viewScale);
           b1 = resolveSize((int) (mDialWidth * viewScale), widthMeasureSpec);
           b2 = resolveSize((int) (mDialHeight * viewScale), heightMeasureSpec);
 	       //setMeasuredDimension(resolveSize((int) (mDialWidth * viewScale), widthMeasureSpec),
           //        resolveSize((int) (mDialHeight * viewScale), heightMeasureSpec));
           setMeasuredDimension((int)((float)widthSize / (float)viewScale), (int)((float)heightSize / (float)viewScale));

 	       setScaleX(viewScale);
 	       setScaleY(viewScale);
 	       setPivotX(0);
 	       setPivotY(0);
       }

       // Called back to draw the view. Also called after invalidate().
	   @Override
	   protected void onDraw(Canvas canvas) {

		  //paint.setStrokeWidth(1);
		  paint.setStyle(Paint.Style.FILL);
		  paint.setAntiAlias(true);

		  // Draw Stars
		  starMap.paintTo(canvas);
		  
	      // Draw the ball
	      //ballBounds.set(ballX-ballRadius, ballY-ballRadius, ballX+ballRadius, ballY+ballRadius);
	      //paint.setColor(Color.BLUE);
	      //canvas.drawOval(ballBounds, paint);

	      // Draw Ship
		  ship.paintTo(canvas);


	      // Draw the status message
	      paint.setColor(Color.DKGRAY);
	      canvas.drawText(statusMsg.toString(), 10, 30, paint);

	      canvas.drawText(String.format("x:%d,y:%d", Math.round(xMax), Math.round(yMax)), xMax-150, yMax-30, paint);
	      paint.setColor(Color.RED);
	      canvas.drawRect(0, 0, 1, 1, paint);
	      canvas.drawRect(0, yMax-1, 1, yMax, paint);
	      canvas.drawRect(xMax-1, yMax-1, xMax, yMax, paint);
	      canvas.drawRect(xMax-1, 0, xMax, 1, paint);

	      // Update the position of the ball, including collision detection and reaction.
	      update();
	  
	      // Delay
	      try {  
	         Thread.sleep(30);  
	      } catch (InterruptedException e) { }
	      
//canvas.restore();
	      invalidate();  // Force a re-draw
	   }
	   
	   // Detect collision and update the position of the ball.
	   private void update() {
		  if (starMap.testCollisionAll()) {
			  try {
				starMap.setAsteroidCount(0);
				starMap.setAsteroidCount(8);
				setActivated(false);
				Thread.sleep(3000);
				setVisibility(View.GONE);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		  }
		  
		  starMap.stepStars();

		  float[] vals = new float[3];
		  tc.getTilt(vals);
			  
		  ship.step(vals[1]);
	   }
	   
	   // Called back when the view is first created or its size changes.
	   @Override
	   public void onSizeChanged(int w, int h, int oldW, int oldH) {
	      // Set the movement bounds for all objects
		  //w = 1280, h = 590
		  //w = 480, h = 255
	      xMax = w;
	      yMax = h;

		  starMap.setStarCount(50);
		  starMap.setAsteroidCount(8);
	   }

	   // Key-up event handler
	   @Override
	   public boolean onKeyUp(int keyCode, KeyEvent event) {
	      switch (keyCode) {
	         case KeyEvent.KEYCODE_DPAD_RIGHT: // Increase rightward speed
	            //TODO shipSpeedX++;
	            break;
	         case KeyEvent.KEYCODE_DPAD_LEFT:  // Increase leftward speed
	        	//TODO shipSpeedX--;
	            break;
	         case KeyEvent.KEYCODE_DPAD_UP:    // Increase upward speed
	            ;
	            break;
	         case KeyEvent.KEYCODE_DPAD_DOWN:  // Increase downward speed
	            ;
	            break;
	         case KeyEvent.KEYCODE_DPAD_CENTER: // Stop
	            //TODO shipSpeedX = 0;
	            break;
	         default:
	 	        return false;  // Event unhandled
	      }
	      return true;  // Event handled
	   }

	   // Touch-input handler
	   @Override
	   public boolean onTouchEvent(MotionEvent event) {
	      float currentX = event.getX();
	      float currentY = event.getY();
	      float deltaX, deltaY;
	      float scalingFactor = 5.0f / ((xMax > yMax) ? yMax : xMax);
	      switch (event.getAction()) {
	         case MotionEvent.ACTION_MOVE:
	            // Modify rotational angles according to movement
	            deltaX = currentX - previousX;
	            deltaY = currentY - previousY;
	            //TODO shipSpeedX += deltaX * scalingFactor;
	            //TODO shipSpeedY += deltaY * scalingFactor;
	      }
	      // Save current x, y
	      previousX = currentX;
	      previousY = currentY;
	      return true;  // Event handled
	   }

}
