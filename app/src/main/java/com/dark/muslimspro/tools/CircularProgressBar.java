package com.dark.muslimspro.tools;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class CircularProgressBar extends View {
    private int maxProgress = 100;
    private int currentProgress = 0;
    private int circleStrokeWidth = 20; // Adjust as needed
    private int progressColor = 0xFF00FF00; // Green color
    private int backgroundColor = 0xFFCCCCCC; // Gray color

    private Paint paint;
    private ValueAnimator animator;
    private boolean animationInProgress = false;

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
        setupAnimator();
    }

    private void setupAnimator() {
        animator = ValueAnimator.ofFloat(0, 1);
        animator.setDuration(1000); // Animation duration in milliseconds
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedFraction = animation.getAnimatedFraction();
                int targetProgress = (int) (animatedFraction * maxProgress);
                setProgress(targetProgress);

                if (animatedFraction >= 1.0f) {
                    animationInProgress = false;
                    animator.cancel();
                }
            }
        });
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

        // Add gradient glow
        if (animationInProgress) {
            int[] colors = {progressColor, Color.TRANSPARENT};
            float[] stops = {0.8f, 1.0f};
            RadialGradient gradient = new RadialGradient(width / 2, height / 2, diameter / 2, colors, stops, Shader.TileMode.CLAMP);
            paint.setShader(gradient);
            canvas.drawCircle(width / 2, height / 2, diameter / 2 - circleStrokeWidth, paint);
        }
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public void setProgress(int progress) {
        if (!animationInProgress && progress >= 0 && progress <= maxProgress) {
            currentProgress = progress;
            invalidate(); // Redraw the view
        }
    }

    // Start the progress animation
    public void startProgressAnimation() {
        if (!animationInProgress) {
            animationInProgress = true;
            animator.start(); // Start the animation
        }
    }

    // Reset the progress to 0
    public void resetProgress() {
        currentProgress = 0;
        invalidate(); // Redraw the view
    }
}
