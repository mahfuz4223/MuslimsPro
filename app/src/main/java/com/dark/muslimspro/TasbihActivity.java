package com.dark.muslimspro;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dark.muslimspro.CircularProgressBar;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TasbihActivity extends AppCompatActivity {

    private TextView tasbihText;
    private CircularProgressBar tasbihProgress;
    private Button incrementButton;
    private TextView tasbihCount;

    private List<String> zikirs = Arrays.asList(
            "Subhanallah (سُبْحَانَ اللَّهِ)",
            "Alhamdulillah (ٱلْحَمْدُ لِلَّٰهِ)",
            "Allahu Akbar (ٱللَّٰهُ أَكْبَرُ)",
            "La ilaha illa Allah (لا إِلَهَ إِلَّا اللَّهُ)",
            "Astaghfirullah (أَسْتَغْفِرُ اللَّهَ)",
            "Hasbunallah wanikmal wakeel (حَسْبُنَا اللَّهُ وَنِعْمَ الْوَكِيلُ)",
            "La hawla wa la quwwata illa billah (لاَ حَوْلَ وَلاَ قُوَّةَ إِلاَّ بِاللَّهِ)"
    );

    private Random random = new Random();
    private int currentZikirIndex = 0;
    private int zikirCount = 0;
    private int maxZikirCount = 100; // Number of counts to trigger vibration and reset

    private Vibrator vibrator;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasbih);

        tasbihText = findViewById(R.id.tasbih_text);
        tasbihProgress = findViewById(R.id.tasbih_progress);
        incrementButton = findViewById(R.id.increment_button);
        tasbihCount = findViewById(R.id.tasbih_count);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Initialize the tasbih text and progress
        updateTasbihTextAndCount();

        // Set up the increment button
        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                incrementZikirCount();
            }
        });

        // Prevent clicks on the whole screen
        View screenLayout = findViewById(R.id.tasbih_progress_sc);
        screenLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Do nothing to prevent unwanted clicks on the screen
                incrementZikirCount();
            }
        });

        // Animate the CircularProgressBar
        animateProgressBar();
    }

    private void animateProgressBar() {
        // Create an ObjectAnimator for CircularProgressBar
        ObjectAnimator animator = ObjectAnimator.ofInt(tasbihProgress, "progress", 0, 100,0);
        animator.setDuration(1000); // Animation duration in milliseconds

        // Listen for animation completion
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Animation completed, you can add any additional logic here
            }
        });

        // Start the animation
        animator.start();
    }





    private void incrementZikirCount() {
        zikirCount++;

        // Check if maxZikirCount is reached
        if (zikirCount >= maxZikirCount) {
            // Trigger vibration
            vibrator.vibrate(500); // Vibrate for 500 milliseconds

            // Move to the next zikir
            currentZikirIndex = (currentZikirIndex + 1) % zikirs.size();
            zikirCount = 0; // Reset the zikir count
        }

        // Update the progress bar, tasbih text, and tasbih count on the main thread
        handler.post(new Runnable() {
            @Override
            public void run() {
                int progress = (zikirCount * 100) / maxZikirCount;
                tasbihProgress.setProgress(progress);
                updateTasbihTextAndCount();
            }
        });
    }

    private void updateTasbihTextAndCount() {
        String currentZikir = zikirs.get(currentZikirIndex);
        tasbihText.setText(currentZikir);
        tasbihCount.setText(String.valueOf(zikirCount));
    }
}
