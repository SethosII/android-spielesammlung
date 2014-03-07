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
import de.sethosii.android_spielesammlung.persistence.SpacesliderPersistentSnapshot;

public class SpacesliderView extends View {

	/**
	 * Interface definition for a callback to be invoked when the value in Spaceslider has changed.
	 */
	public interface OnSpacesliderChangeListener {
		/**
		 * Called when the live count of a ship has changed.
		 * 
		 * @param v
		 *            The view whose state has changed.
		 * @param liveCount
		 *            The new number of lives.
		 */
		void onLiveChange(View v, int liveCount);

		/**
		 * Called when the difficulty of the game has changed.
		 * 
		 * @param v
		 *            The view whose state has changed.
		 * @param difficulty
		 *            The difficulty value.
		 */
		void onDifficultyChange(View v, float difficulty);
	}

	private OnSpacesliderChangeListener mOnSpacesliderChangeListener;

	/**
	 * Returns the Spaceslider-change callback registered for this view.
	 * 
	 * @return The callback, or null if one is not registered.
	 */
	public OnSpacesliderChangeListener getOnSpacesliderChangeListener() {
		return mOnSpacesliderChangeListener;
	}

	/**
	 * Register a callback to be invoked when a value in Spaceslider has changed.
	 * 
	 * @param l
	 *            The callback that will run.
	 */
	public void setOnLiveChangeListener(OnSpacesliderChangeListener l) {
		mOnSpacesliderChangeListener = l;
	}

	private class StarMap {

		private abstract class SpaceEntity {
			int x = 0, y = 0;
			float speed = 0, size = 0;

			protected SpaceEntity() {
				speed = (float) Math.random() * 12.0f + 1.0f;

				reset();
			}

			public void step() {
				y = Math.round(y + (speed * difficulty));
				if (y > yMax) {
					reset();
				}
			}

			public void reset() {
				x = Math.round((float) Math.random() * (float) xMax);
				y = 0;
			}
		}

		private class Star extends SpaceEntity {
			public Star() {
				super();

				y = Math.round((float) Math.random() * (float) yMax); // starts not on the top edge
				// star size between 0.5 and 3.5 virtual pixels
				size = (float) Math.random() * 3.0f + 0.5f;
			}
		}

		private class Asteroid extends SpaceEntity {
			int color;

			public Asteroid() {
				super();

				// asteroid size between 13 and 28 virtual pixels
				size = (float) Math.random() * 15.0f + 13.0f;

				switch ((int) (Math.random() * 3.0f)) {
				case 0:
					color = Color.argb(255, 255, 255, 63); // yellow
					break;
				case 1:
					color = Color.argb(255, 127, 127, 255); // blue
					break;
				default:
					color = Color.argb(255, 255, 127, 127); // red
				}
			}

			public boolean testCollision(Region regionShip, Region regionClip) {
				Path pathThis = new Path();
				pathThis.addCircle(x, y, size, Path.Direction.CW);

				Region regionThis = new Region();
				regionThis.setPath(pathThis, regionClip);

				// intersection test, first a quick one based on rectangle
				return ((!regionThis.quickReject(regionShip)) && regionThis.op(regionShip,
						Region.Op.INTERSECT));
			}
		}

		public List<Star> stars = new ArrayList<Star>();
		public List<Asteroid> asteroids = new ArrayList<Asteroid>();

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

		private void paintTo(Canvas canvas, Paint paint) {
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
			for (Asteroid asteroid : asteroids) {
				asteroid.step();
			}
		}

		private Asteroid testCollisionAll() {
			Region regionClip = new Region(xMin, yMin, xMax, yMax);
			Region regionShip = new Region();
			regionShip.setPath(pathShip, regionClip);

			for (Asteroid asteroid : asteroids) {
				if (asteroid.testCollision(regionShip, regionClip)) {
					return asteroid;
				}
			}
			return null;
		}

	}

	private class SpaceShip {
		private static final int INITIAL_LIVES = 5;

		private class Flame {
			private byte flameColor;
			private int flameLength;
			private int flameWidth;
			private Path pathFlame = new Path();

			public Flame(int Length, int Width) {
				flameLength = Length;
				flameWidth = Width;

				pathFlame.reset();
				pathFlame.setFillType(Path.FillType.WINDING);
				pathFlame.moveTo(-flameWidth / 4, 0);
				pathFlame.lineTo(-flameWidth / 2, flameLength / 3);
				pathFlame.lineTo(0, flameLength);
				pathFlame.lineTo(flameWidth / 2, flameLength / 3);
				pathFlame.lineTo(flameWidth / 4, 0);
				pathFlame.close();
			}

			public int nextColor() {
				int color;
				flameColor = (byte) ((flameColor + 1) % 5);
				switch (flameColor) {
				case 0:
					color = Color.argb(255, 255, 255, 255);
					break;
				case 1:
					color = Color.argb(255, 255, 191, 159);
					break;
				case 2:
					color = Color.argb(255, 255, 127, 63);
					break;
				case 3:
					color = Color.argb(255, 255, 191, 95);
					break;
				default:
					color = Color.argb(255, 255, 223, 127);
				}

				return color;
			}

			public void paintTo(Canvas canvas, float x, float y, Paint paint) {
				paint.setColor(nextColor());
				pathPaint.reset();
				pathPaint.addPath(pathFlame, x, y);
				canvas.drawPath(pathPaint, paint);
			}
		}

		private Flame flame = new Flame(30, 20);

		private float shipSpan = 160; // ship's wing span
		private float shipLength = 140; // ship's body length
		private float shipX = 0; // ship's center (x,y)
		private float shipY = 0;
		private float shipSpeedX = 5; // ship's horizontal speed
		private RectF shipBounds; // Needed for Canvas.drawOval

		private Path pathWings = new Path();
		private Path pathBody = new Path();

		public int lives;

		public SpaceShip() {
			shipBounds = new RectF();

			//
			pathWings.reset();
			pathWings.setFillType(Path.FillType.WINDING);
			pathWings.moveTo(0, -shipLength / 5);
			pathWings.lineTo(-shipSpan / 2, shipLength * 2 / 5);
			pathWings.lineTo(shipSpan / 2, shipLength * 2 / 5);
			pathWings.close();

			//
			int bodyRadius = 10;
			int noseLen = 10;
			pathBody.reset();
			pathBody.setFillType(Path.FillType.WINDING);
			pathBody.moveTo(-bodyRadius, shipLength / 2);
			pathBody.lineTo(bodyRadius, shipLength / 2);
			pathBody.lineTo(bodyRadius, -shipLength / 2 + noseLen);
			pathBody.lineTo(bodyRadius / 2, -shipLength / 2);
			pathBody.lineTo(-bodyRadius / 2, -shipLength / 2);
			pathBody.lineTo(-bodyRadius, -shipLength / 2 + noseLen);
			pathBody.close();

			reset();
		}

		public void reset() {
			setLives(INITIAL_LIVES);

			shipX = xMax / 2;
			shipY = yMax - shipLength;
		}

		public void setLives(int lives) {
			this.lives = lives;
			if (mOnSpacesliderChangeListener != null) {
				mOnSpacesliderChangeListener.onLiveChange(SpacesliderView.this, ship.lives);
			}
		}

		public void paintTo(Canvas canvas, Paint paint) {
			paint.setColor(android.graphics.Color.LTGRAY);

			pathShip.reset();
			pathShip.addPath(pathWings, shipX, shipY);
			pathShip.addPath(pathBody, shipX, shipY);
			canvas.drawPath(pathShip, paint);

			// canvas.drawPath(pathWings, paint);
			// canvas.drawPath(pathBody, paint);

			flame.paintTo(canvas, shipX, shipY + shipLength / 2, paint);
		}

		public void step(float value) {
			// Get new (x,y) position
			shipX -= Math.round(60.0f * value);
			// shipX += shipSpeedX;
			// shipY += shipSpeedY;
			// Detect collision and react
			if (shipX + shipSpan / 2 > xMax) {
				shipSpeedX = -shipSpeedX;
				shipX = xMax - shipSpan / 2;
			} else if (shipX - shipSpan / 2 < xMin) {
				shipSpeedX = -shipSpeedX;
				shipX = xMin + shipSpan / 2;
			}

			// Build status message
			statusMsg.delete(0, statusMsg.length()); // Empty buffer
			formatter.format("Ball@(%3.0f,%3.0f), Speed=(%2.0f), Neig(%f)", shipX, shipY,
					shipSpeedX, value);
		}
	}

	/** number of asteroids on start */
	private static final int BASE_ASTEROID_COUNT = 5;
	/** number of stars in space */
	private static final int DEFAULT_STAR_COUNT = 50;

	private boolean running = false;
	private int xMin = 0; // This view's bounds
	private int xMax;
	private int yMin = 0;
	private int yMax;
	private SpaceShip ship = new SpaceShip();
	private float difficulty = 1;
	private Paint paint; // The paint (e.g. style, color) used for drawing

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

		paint = new Paint(Paint.ANTI_ALIAS_FLAG); // TODO new Paint();
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
	Path pathPaint = new Path();

	int mDialHeight = 590/* 255 */; // wanted virtual dimension of view
	int mDialWidth = 1280/* 480 */;
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

		if (widthMode != MeasureSpec.UNSPECIFIED /* && widthSize < mDialWidth */) {
			hScale = (float) widthSize / (float) mDialWidth;
		}

		if (heightMode != MeasureSpec.UNSPECIFIED /* && heightSize < mDialHeight */) {
			vScale = (float) heightSize / (float) mDialHeight;
		}

		// use same scale for x and y
		viewScale = Math.min(hScale, vScale);

		setMeasuredDimension((int) ((float) widthSize / (float) viewScale),
				(int) ((float) heightSize / (float) viewScale));

		setScaleX(viewScale);
		setScaleY(viewScale);
		setPivotX(0);
		setPivotY(0);
	}

	// Called back to draw the view. Also called after invalidate().
	@Override
	protected void onDraw(Canvas canvas) {

		// paint.setStrokeWidth(1);
		paint.setStyle(Paint.Style.FILL);
		paint.setAntiAlias(true);

		// Draw Stars
		starMap.paintTo(canvas, paint);

		// Draw the ball
		// ballBounds.set(ballX-ballRadius, ballY-ballRadius, ballX+ballRadius, ballY+ballRadius);
		// paint.setColor(Color.BLUE);
		// canvas.drawOval(ballBounds, paint);

		// Draw Ship
		ship.paintTo(canvas, paint);

		// Draw the status message
		paint.setColor(Color.DKGRAY);
		canvas.drawText(statusMsg.toString(), 10, 30, paint);

		canvas.drawText(String.format("x:%d,y:%d", Math.round(xMax), Math.round(yMax)), xMax - 150,
				yMax - 30, paint);
		paint.setColor(Color.RED);
		canvas.drawRect(0, 0, 1, 1, paint);
		canvas.drawRect(0, yMax - 1, 1, yMax, paint);
		canvas.drawRect(xMax - 1, yMax - 1, xMax, yMax, paint);
		canvas.drawRect(xMax - 1, 0, xMax, 1, paint);
		canvas.drawRect(ship.shipX - 1, ship.shipY - 1, ship.shipX + 1, ship.shipY + 1, paint);

		if (running) {
			// Updates the positions of ship, stars, etc...
			update();
		}

		// Delay
		try {
			Thread.sleep(30);
		} catch (InterruptedException e) {
		}

		// canvas.restore();
		invalidate(); // Force a re-draw
	}

	// Detect collision and update the position of the ball.
	private void update() {
		StarMap.Asteroid asteroid = starMap.testCollisionAll();
		if (asteroid != null) {
			try {
				ship.setLives(ship.lives - 1);

				if (ship.lives == 0) {
					setRunning(false);
					setActivated(false);
					Thread.sleep(3); // TODO
				} else {
					asteroid.reset();
				}
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
		// w = 1280, h = 590
		// w = 480, h = 255
		xMax = w;
		yMax = h;

		starMap.setStarCount(DEFAULT_STAR_COUNT);

		reset();
	}

	// Key-up event handler
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_RIGHT: // Increase rightward speed
			// TODO shipSpeedX++;
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT: // Increase leftward speed
			// TODO shipSpeedX--;
			break;
		case KeyEvent.KEYCODE_DPAD_UP: // Increase upward speed
			;
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN: // Increase downward speed
			;
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER: // Stop
			// TODO shipSpeedX = 0;
			break;
		default:
			return false; // Event unhandled
		}
		return true; // Event handled
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
			// TODO shipSpeedX += deltaX * scalingFactor;
			// TODO shipSpeedY += deltaY * scalingFactor;
		}
		// Save current x, y
		previousX = currentX;
		previousY = currentY;
		return true; // Event handled
	}

	public void reset() {
		difficulty = 1;
		if (mOnSpacesliderChangeListener != null) {
			mOnSpacesliderChangeListener.onDifficultyChange(this, difficulty);
		}

		starMap.setAsteroidCount(0);
		starMap.setAsteroidCount(BASE_ASTEROID_COUNT);

		ship.reset();

		running = true;
	}

	public void increaseDifficulty() {
		difficulty *= 1.1f;
		starMap.setAsteroidCount(Math.round(BASE_ASTEROID_COUNT * difficulty));
		if (mOnSpacesliderChangeListener != null) {
			mOnSpacesliderChangeListener.onDifficultyChange(this, difficulty);
		}
	}

	public void setRunning(boolean running) {
		this.running = running;
		if (running) {
			invalidate();
		}
	}

	public boolean getRunning() {
		return this.running;
	}

	/**
	 * Transfer current state to a snapshot object.
	 * 
	 * @param s
	 *            snapshot object
	 */
	public void fillSnapshot(SpacesliderPersistentSnapshot s) {
		s.difficulty = difficulty;
		s.liveCount = ship.lives;
		s.shipX = Math.round(ship.shipX);

		s.asteroidCount = starMap.asteroids.size();

		s.asteroidX = new int[s.asteroidCount];
		s.asteroidY = new int[s.asteroidCount];
		s.asteroidSpeed = new int[s.asteroidCount];
		s.asteroidSize = new int[s.asteroidCount];

		for (int i = 0; i < s.asteroidCount; i++) {
			StarMap.Asteroid asteroid = starMap.asteroids.get(i);

			s.asteroidX[i] = asteroid.x;
			s.asteroidY[i] = asteroid.y;
			s.asteroidSpeed[i] = Math.round(asteroid.speed);
			s.asteroidSize[i] = Math.round(asteroid.size);
		}
	}

	/**
	 * Applies the state from a snapshot object.
	 * 
	 * @param s
	 *            snapshot object
	 */
	public void applySnapshot(SpacesliderPersistentSnapshot s) {
		difficulty = s.difficulty;
		ship.setLives(s.liveCount);
		ship.shipX = s.shipX;

		starMap.setAsteroidCount(s.asteroidCount);

		for (int i = 0; i < s.asteroidCount; i++) {
			StarMap.Asteroid asteroid = starMap.asteroids.get(i);

			asteroid.x = s.asteroidX[i];
			asteroid.y = s.asteroidY[i];
			asteroid.speed = s.asteroidSpeed[i];
			asteroid.size = s.asteroidSize[i];
		}
	}
}
