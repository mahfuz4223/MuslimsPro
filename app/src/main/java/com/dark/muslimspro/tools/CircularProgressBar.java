package com.dark.muslimspro.tools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CircularProgressBar extends View {
    private int maxProgress = 100;
    private int currentProgress = 0;
    private int circleStrokeWidth = 20; // Adjust as needed
    private int progressColor = 0xFF00FF00; // Green color
    private int backgroundColor = 0xFFCCCCCC; // Gray color

    private Paint paint;

    public CircularProgressBar(Context context) {
        super(context);
        init();
    }

    public CircularProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircularProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int diameter = Math.min(width, height);

        // Draw background circle
        paint.setColor(backgroundColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(circleStrokeWidth);
        canvas.drawCircle(width / 2, height / 2, diameter / 2 - circleStrokeWidth, paint);

        // Draw progress arc
        paint.setColor(progressColor);
        paint.setStrokeCap(Paint.Cap.ROUND);
        float sweepAngle = 360f * currentProgress / maxProgress;
        canvas.drawArc(circleStrokeWidth, circleStrokeWidth, diameter - circleStrokeWidth, diameter - circleStrokeWidth, -90, sweepAngle, false, paint);
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
        invalidate(); // Redraw the view
    }

    // Add this method to set the progress
    public void setProgress(int progress) {
        if (progress >= 0 && progress <= maxProgress) {
            this.currentProgress = progress;
            invalidate(); // Redraw the view
        }
    }
}
