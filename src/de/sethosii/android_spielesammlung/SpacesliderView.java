package de.sethosii.android_spielesammlung;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

public class SpacesliderView extends View {

	private class StarMap {
		
		private class Star {
			int x = 0, y = 0;
			float speed = 0, size = 0;
			public Star() {
				x = Math.round((float)Math.random()*(float)xMax);
				y = Math.round((float)Math.random()*(float)yMax);
				speed = (float)Math.random()*20.0f;
				size = (float)Math.random()*2.0f + 1.0f;
			}
			public void step() {
				y = Math.round(y + speed);
				if (y > yMax) {
					y %= yMax;
					x = Math.round((float)Math.random()*(float)xMax);
				}
			}
		}
		private class AstObj extends Star {
			int color;
			public AstObj() {
				super();
				y = 0;
				size = (float)Math.random()*10.0f + 10.0f;
				if (Math.random() > 0.5f) {
					color = Color.argb(255, 255, 255, 192);
				} else {
					color = Color.argb(255, 192, 192, 255);
				}
			}
		}

		List<Star> stars = new ArrayList<Star>();
		List<AstObj> objects = new ArrayList<AstObj>();

		private void setStarCount(int count) {
			while (stars.size() > count) {
				stars.remove(0);
			}
			while (stars.size() < count) {
				stars.add(new Star());
			}
		}

		private void setObjectCount(int count) {
			while (objects.size() > count) {
				objects.remove(0);
			}
			while (objects.size() < count) {
				objects.add(new AstObj());
			}
		}

		private void paintTo(Canvas canvas) {
		    paint.setColor(Color.WHITE);
			for (Star star : stars) {
				canvas.drawCircle(star.x, star.y, star.size, paint);
			}
			for (AstObj astObj : objects) {
			    paint.setColor(astObj.color);
				canvas.drawCircle(astObj.x, astObj.y, astObj.size, paint);
			}
		}
		
		private void stepStars() {
			for (Star star : stars) {
				star.step();
			}
			for (AstObj astObj : objects) {
				astObj.step();
			}
		}

	}
	
	   private int xMin = 0;          // This view's bounds
	   private int xMax;
	   private int yMin = 0;
	   private int yMax;
	   private float ballRadius = 80; // Ball's radius
	   private float ballX = ballRadius + 20;  // Ball's center (x,y)
	   private float ballY = ballRadius + 500; //TODO
	   private float ballSpeedX = 5;  // Ball's speed (x,y)
	   private float ballSpeedY = 3;
	   private RectF ballBounds;      // Needed for Canvas.drawOval
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
		      
		      ballBounds = new RectF();
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
	  
	   
       Path pathWings = new Path();
       Path pathBody = new Path();

       // Called back to draw the view. Also called after invalidate().
	   @Override
	   protected void onDraw(Canvas canvas) {
		  // Draw Stars
		  starMap.paintTo(canvas);
		  
	      // Draw the ball
	      //ballBounds.set(ballX-ballRadius, ballY-ballRadius, ballX+ballRadius, ballY+ballRadius);
	      //paint.setColor(Color.BLUE);
	      //canvas.drawOval(ballBounds, paint);

	      // Draw Ship
	      paint.setStrokeWidth(2);
	      paint.setColor(android.graphics.Color.LTGRAY);
	      paint.setStyle(Paint.Style.FILL_AND_STROKE);
	      paint.setAntiAlias(true);

	      //Point point1_draw = new Point();        
	      //Point point2_draw = new Point();    
	      //Point point3_draw = new Point();

	      //mapView.getProjection().toPixels(point1, point1_draw);
	      //mapView.getProjection().toPixels(point2, point2_draw);
	      //mapView.getProjection().toPixels(point3, point3_draw);

	      pathWings.reset();
	      pathWings.setFillType(Path.FillType.EVEN_ODD);
	      pathWings.moveTo(ballX-ballRadius, ballY+ballRadius);
	      pathWings.lineTo(ballX+ballRadius, ballY+ballRadius);
	      pathWings.lineTo(ballX, ballY);
	      pathWings.close();
	      canvas.drawPath(pathWings, paint);

	      int bodyRadius = 10;
	      int noseLen = 10;
	      pathBody.reset();
	      pathBody.setFillType(Path.FillType.EVEN_ODD);
	      pathBody.moveTo(ballX-bodyRadius, ballY+ballRadius);
	      pathBody.lineTo(ballX+bodyRadius, ballY+ballRadius);
	      pathBody.lineTo(ballX+bodyRadius, ballY-ballRadius+noseLen);
	      pathBody.lineTo(ballX+bodyRadius/2, ballY-ballRadius);
	      pathBody.lineTo(ballX-bodyRadius/2, ballY-ballRadius);
	      pathBody.lineTo(ballX-bodyRadius, ballY-ballRadius+noseLen);
	      pathBody.close();
	      canvas.drawPath(pathBody, paint);





	      // Draw the status message
	      paint.setColor(Color.DKGRAY);
	      canvas.drawText(statusMsg.toString(), 10, 30, paint);
	      
	      // Update the position of the ball, including collision detection and reaction.
	      update();
	  
	      // Delay
	      try {  
	         Thread.sleep(30);  
	      } catch (InterruptedException e) { }
	      
	      invalidate();  // Force a re-draw
	   }
	   
	   // Detect collision and update the position of the ball.
	   private void update() {
		  starMap.stepStars();
		  
		  float[] vals = new float[3];
		  tc.getTilt(vals);
			  
	      // Get new (x,y) position
	      ballX -= Math.round(50.0f * vals[1]);
	      //ballX += ballSpeedX;
	      //ballY += ballSpeedY;
	      // Detect collision and react
	      if (ballX + ballRadius > xMax) {
	         ballSpeedX = -ballSpeedX;
	         ballX = xMax-ballRadius;
	      } else if (ballX - ballRadius < xMin) {
	         ballSpeedX = -ballSpeedX;
	         ballX = xMin+ballRadius;
	      }
	      if (ballY + ballRadius > yMax) {
	         ballSpeedY = -ballSpeedY;
	         ballY = yMax - ballRadius;
	      } else if (ballY - ballRadius < yMin) {
	         ballSpeedY = -ballSpeedY;
	         ballY = yMin + ballRadius;
	      }

	      // Build status message
	      statusMsg.delete(0, statusMsg.length());   // Empty buffer
	      formatter.format("Ball@(%3.0f,%3.0f), Speed=(%2.0f,%2.0f), Neig(%f)",
	    		  ballX, ballY, ballSpeedX, ballSpeedY, vals[1]);
	   }
	   
	   // Called back when the view is first created or its size changes.
	   @Override
	   public void onSizeChanged(int w, int h, int oldW, int oldH) {
	      // Set the movement bounds for the ball
	      xMax = w-1;
	      yMax = h-1;

		  starMap.setStarCount(50);
		  starMap.setObjectCount(8);
	   }

	   // Key-up event handler
	   @Override
	   public boolean onKeyUp(int keyCode, KeyEvent event) {
	      switch (keyCode) {
	         case KeyEvent.KEYCODE_DPAD_RIGHT: // Increase rightward speed
	            ballSpeedX++;
	            break;
	         case KeyEvent.KEYCODE_DPAD_LEFT:  // Increase leftward speed
	            ballSpeedX--;
	            break;
	         case KeyEvent.KEYCODE_DPAD_UP:    // Increase upward speed
	            ballSpeedY--;
	            break;
	         case KeyEvent.KEYCODE_DPAD_DOWN:  // Increase downward speed
	            ballSpeedY++;
	            break;
	         case KeyEvent.KEYCODE_DPAD_CENTER: // Stop
	            ballSpeedX = 0;
	            ballSpeedY = 0;
	            break;
	         case KeyEvent.KEYCODE_A:    // Zoom in
	            // Max radius is about 90% of half of the smaller dimension
	            float maxRadius = (xMax > yMax) ? yMax / 2 * 0.9f  : xMax / 2 * 0.9f;
	            if (ballRadius < maxRadius) {
	               ballRadius *= 1.05;   // Increase radius by 5%
	            }
	            break;
	         case KeyEvent.KEYCODE_Z:    // Zoom out
	            if (ballRadius > 20) {   // Minimum radius
	               ballRadius *= 0.95;   // Decrease radius by 5%
	            }
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
	            ballSpeedX += deltaX * scalingFactor;
	            ballSpeedY += deltaY * scalingFactor;
	      }
	      // Save current x, y
	      previousX = currentX;
	      previousY = currentY;
	      return true;  // Event handled
	   }

}
