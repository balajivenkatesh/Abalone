package com.abalone;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

public class GameView extends View implements OnTouchListener {
	private Paint p = new Paint();
	private Paint bP = new Paint(), wP = new Paint(), empP = new Paint(),
			sP = new Paint(), hP = new Paint();
	Path boardHex;

	int countBOut = 0, countWOut = 0; // states = black -1, white 1, blank 0
	private static String TAG = "developer";
	float[][] vertices = new float[6][2];
	Board board;

	List<Point> touchPoints = new ArrayList<Point>();
	int screenHt, screenWd;

	// int N = 3;
	// int[][] A = new int[N][N]; // Stores the grid state
	// int u = 0;
	// int gc = 0;
	// boolean sf = false;
	// boolean manturn = false;

	// int turn = 1;

	int x0 = 0, x1 = 50;
	int y0 = 0, y1 = 50;
	int l = x1 - x0;

	// int step = (x1 - x0) / 5;

	// boolean drawWin = false, multip = false;
	// int Winner = 0;
	// boolean think = false;

	protected void measure(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metrics);
		screenHt = metrics.heightPixels;// - 96;
		screenWd = metrics.widthPixels;// - 96;
		x1 = (screenWd < screenHt) ? screenWd : screenHt;
		x1 = x1 - x0;
		y1 = x1;
		l = x1 - x0;
		// Log.d(TAG,"l = "+l);
		genHexVertices();
		board = new Board(x0, y0, l);
	}

	public GameView(Context context, AttributeSet aSet) {
		super(context, aSet);

		p.setColor(Color.rgb(139, 69, 19));
		p.setAlpha(255);
		p.setStrokeWidth(12);
		p.setTextSize(60);

		empP.setColor(Color.rgb(205, 133, 63));
		empP.setAlpha(255);
		empP.setStyle(Paint.Style.FILL);

		bP.setColor(Color.BLACK);
		bP.setAlpha(255);
		bP.setStyle(Paint.Style.FILL);
		bP.setStrokeWidth(20);

		sP.setColor(Color.rgb(255, 0, 0));
		sP.setStrokeWidth(10);

		hP.setColor(Color.rgb(255, 255, 0));
		hP.setStrokeWidth(10);
		hP.setStyle(Paint.Style.STROKE);

		wP.setColor(Color.rgb(190, 190, 190));
		wP.setColor(Color.WHITE);
		wP.setAlpha(255);
		wP.setStyle(Paint.Style.FILL);

		boardHex = new Path();

		measure(context);

		init();
		setFocusableInTouchMode(true);
		this.setOnTouchListener(this);
	}

	private void genHexVertices() {
		float a = (float) (l / 4);
		vertices[0][0] = x0;
		vertices[0][1] = y0 + l / 2;

		vertices[1][0] = x0 + a;
		vertices[1][1] = y0;

		vertices[2][0] = x1 - a;
		vertices[2][1] = y0;

		vertices[3][0] = x1;
		vertices[3][1] = y0 + l / 2;

		vertices[4][0] = x1 - a;
		vertices[4][1] = y1;

		vertices[5][0] = x0 + a;
		vertices[5][1] = y1;
	}

	public void undo() {
		for (int i = 0; i < board.state.length; i++) {
			board.state[i] = board.prevState[i];
		}
		touchPoints.clear();
		highPoints.clear();
		invalidate();
	}

	void resetall() {
		Log.d(TAG, "reset");
		countBOut = 0;
		countWOut = 0;
		for (int i = 0; i < board.state.length; i++)
			board.state[i] = board.initSt[i];
		touchPoints.clear();
		highPoints.clear();
		invalidate();
	}

	public void init() {
	}

	// public void comp() {
	// new CompThread().execute();
	// invalidate();
	// }

	// public class CompThread extends AsyncTask<String, String, Integer> {
	// public void crunch() {
	// }
	//
	// @Override
	// protected Integer doInBackground(String... arg0) {
	// crunch();
	// return null;
	// }
	//
	// protected void onPostExecute(Integer arg) {
	// invalidate();
	// }
	// }

	int prevPointsSize = 0;

	// Method to handle user touch inputs
	public boolean onTouch(View view, MotionEvent event) {
		// int x = (int) event.getX();
		// int y = (int) event.getY();

		// Log.d(TAG,event.getAction()+"");
		if (event.getAction() == MotionEvent.ACTION_UP) {
			highlight();
			if (highPoints.size() > 0)
				movePcs();
			invalidate();
			return super.onTouchEvent(event);
		}
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			touchPoints.clear();
		}
		Point point = new Point();
		point.x = (int) event.getX();
		point.y = (int) event.getY();
		// Log.d(TAG, "at " + event.getX() + " , " + event.getY());

		touchPoints.add(point);

		return true;
	}

	List<Integer> highPoints = new ArrayList<Integer>();

	void highlight() {
		highPoints.clear();
		float[][] v = board.coords;
		float rad = board.r;
		float dist = 50;
		for (Point point : touchPoints) {
			for (int i = 0; i < 61; i++) {
				dist = (float) Math.pow(point.x - v[i][0], 2.0)
						+ (float) Math.pow(point.y - v[i][1], 2.0);
				dist = (float) Math.sqrt(dist);
				if (dist < rad) {
					if (!highPoints.contains(i))
						highPoints.add(i);
				}
			}
		}
	}

	void movePcs() {
		if (highPoints == null)
			return;
		int size = highPoints.size();

		if (size <= 1 || size > 4) // highPoints.size()>4||
			return;

		// List<Integer> h = null, dia1 = null, dia2 = null;
		List<Integer> line = null;
		boolean found = true;
		int firstPoint = highPoints.get(0);

		for (List<Integer> li : board.horiz) {
			if (li.contains(firstPoint))
				line = li;
			// Log.d(TAG,"horiz : "+li.toString());
		}
		found = true;
		for (int i = 1; i < size; i++) {
			if (!line.contains(highPoints.get(i))) {
				found = false;
				break;
			}
		}

		if (!found) {
			for (List<Integer> li : board.diag1) {
				if (li.contains(firstPoint))
					line = li;
				// Log.d(TAG,"diag1 : "+li.toString());
			}
			found = true;
			for (int i = 1; i < size; i++) {
				if (!line.contains(highPoints.get(i))) {
					found = false;
					break;
				}
			}
		}

		if (!found) {
			for (List<Integer> li : board.diag2) {
				if (li.contains(firstPoint))
					line = li;
				// Log.d(TAG,"diag2 : "+li.toString());
			}
			found = true;
			for (int i = 1; i < size; i++) {
				if (!line.contains(highPoints.get(i))) {
					found = false;
					break;
				}
			}
		}

		if (!found) {
			// Log.d(TAG, "not straight");
			return;
		}
		// if (found)

		// Log.d(TAG, "found line : " + line.toString());

		int firstState = board.state[highPoints.get(0)];
		int dir = -line.indexOf(highPoints.get(0))
				+ line.indexOf(highPoints.get(1));
		int opState = firstState * -1;
		int countFirstState = 1;
		int countOpState = 0;

		if (firstState == 0)
			return;

		boolean allSame = true;
		for (int i = 1; i < size; i++) {
			if (board.state[highPoints.get(i)] != firstState) {
				allSame = false;
				countOpState++;
			} else
				countFirstState++;
		}
		if (allSame) {
			// Log.d(TAG, "all same");
			return;
		}

		if (board.state[highPoints.get(size - 1)] == 0) { // if empty, move
			for (int i = 0; i < board.state.length; i++) {
				board.prevState[i] = board.state[i];
			}

			for (int i = size - 1; i > 0; i--) {
				board.state[highPoints.get(i)] = board.state[highPoints
						.get(i - 1)];
			}
			board.state[firstPoint] = 0;
			return;
		}
		
		boolean emptySumito = false;
		int lastIndexOnLine = line.indexOf(highPoints.get(size - 1));
		// Log.d(TAG,"lastindexonline : "+lastIndexOnLine+"; dir : "+dir);
		try {
			for (int i = 1; i <= 3; i++) {
				// Log.d(TAG,"firstState : "+countFirstState+"; opState : "+countOpState);
				if (board.state[line.get(lastIndexOnLine + dir * i)] == opState)
					countOpState++;
				else {
					emptySumito = true;
					break;
				}
			}
		} catch (Exception e) {
			// emptySumito=true;
		}

		if (countFirstState <= countOpState)
			return;
		if (emptySumito) { // if empty sumito, move
			for (int i = 0; i < board.state.length; i++) {
				board.prevState[i] = board.state[i];
			}

			// Log.d(TAG, "empty Sumito");
			board.state[line.get(lastIndexOnLine + dir * (countOpState))] = opState;
			for (int i = size - 1; i > 0; i--) {
				board.state[highPoints.get(i)] = board.state[highPoints
						.get(i - 1)];
			}
			board.state[firstPoint] = 0;
			return;
		}

		// if(board.state[highPoints.get(size-1)]==opState &&
		// (highPoints.get(size-1)==line.get(line.size()-1) ||
		// highPoints.get(size-1)==line.get(0))) { // if last, move
		if (line.get(lastIndexOnLine + dir * (countOpState - 1)) == line
				.get(line.size() - 1)
				|| line.get(lastIndexOnLine + dir * (countOpState - 1)) == line
						.get(0)) { // if last, move
			for (int i = 0; i < board.state.length; i++) {
				board.prevState[i] = board.state[i];
			}

			// Log.d(TAG, "sumito out");
			for (int i = size - 1; i > 0; i--) {
				board.state[highPoints.get(i)] = board.state[highPoints
						.get(i - 1)];
			}
			board.state[firstPoint] = 0;
			if (opState == 1)
				countWOut++;
			else if (opState == -1)
				countBOut++;
			return;
		}
	}

	@Override
	public void onDraw(Canvas canvas) {
		// // Draw Hex Board
		// for (int i = 0; i < N + 1; i++) {
		// canvas.drawLine(x0, y0 + step * i, x1, y0 + step * i, p);
		// } // Horiz lines
		//
		// for (int i = 0; i < N + 1; i++) {
		// canvas.drawLine(x0 + step * i, y0, x0 + step * i, y1, p);
		// } // Vert lines

		boardHex.reset();
		boardHex.moveTo(vertices[0][0], vertices[0][1]);
		// Log.d(TAG, "x,y = "+vertices[0][0]+","+vertices[0][1]);
		for (int i = 1; i < 6; i++) {
			boardHex.lineTo(vertices[i][0], vertices[i][1]);
			// Log.d(TAG, "x,y = "+vertices[i][0]+","+vertices[i][1]);
		}
		canvas.drawPath(boardHex, p);

		// Draw marks
		float rad = board.r;

		float[][] v = board.coords;
		int[] state = board.state;
		for (int i = 0; i < board.coords.length; i++) {
			if (state[i] == 0)
				canvas.drawCircle(v[i][0], v[i][1], rad, empP);
			else if (state[i] == 1)
				canvas.drawCircle(v[i][0], v[i][1], rad, wP);
			else if (state[i] == -1)
				canvas.drawCircle(v[i][0], v[i][1], rad, bP);
		}

		// for (Point point : touchPoints) {
		// canvas.drawPoint(point.x, point.y, hP);
		// }

		String str = "";
		for (int i : highPoints) {
			canvas.drawCircle(v[i][0], v[i][1], rad, hP);
			str += i + ", ";
		}
		// Log.d(TAG, "Path - " + str);
		if (!touchPoints.isEmpty()) {
			Point prevPoint = touchPoints.get(0);
			for (Point point : touchPoints) {
				canvas.drawLine(prevPoint.x, prevPoint.y, point.x, point.y, sP);
				prevPoint = point;
			}
		}

		// Draw text message
		// if (think & !manturn & !multip)
		canvas.drawText("Scores : Black = " + countWOut + "; White = "
				+ countBOut, x0, y1 + y0 + 50, p);
		// if (manturn & !multip)
		// canvas.drawText("Your turn...", x0, y1 + y0 + 50, p);
		// if (multip & turn == 1 & !drawWin)
		// canvas.drawText("(Multiplayer) X to play...", x0, y1 + y0 + 50, p);
		// if (multip & turn == -1 & !drawWin)
		// canvas.drawText("(Multiplayer) O to play...", x0, y1 + y0 + 50, p);
		// if (drawWin) {
		// String sWin = "Winner is ";
		//
		// canvas.drawText(sWin, x0, y1 + y0 + 50, p);
		// }
	}
}