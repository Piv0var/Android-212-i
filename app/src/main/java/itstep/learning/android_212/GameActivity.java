package itstep.learning.android_212;

import android.annotation.SuppressLint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GameActivity extends AppCompatActivity {
    private TextView tvScore;
    private TextView tvBestScore;
    private long score;
    private long bestScore;
    private final int N = 4;
    private final int[][] tiles = new int[N][N];
    private final TextView[][] tvTiles = new TextView[N][N];

    @SuppressLint({"ClickableViewAccessibility", "DiscouragedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        View mainLayout = findViewById(R.id.game_layout_main);
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tvTiles[i][j] = findViewById(
                        getResources().getIdentifier(
                                "game_tv_tile_" + i + j,
                                "id",
                                getPackageName()
                        )
                );
            }
        }
        tvScore = findViewById( R.id.game_tv_score );
        tvBestScore = findViewById( R.id.game_tv_best );
        LinearLayout gameField = findViewById( R.id.game_layout_field );
        /*
        На етапі onCreate активність ще не "зверстана" - розмітка завантажена, об'єкти
        створені, але реальні розміри ще не розраховані. Для того щоб виконати дії після
        готовності елемента йому передають задачі методом post
         */
        gameField.post( () -> {
            int windowWidth = this.getWindow().getDecorView().getWidth();
            // задаємо відступи (margin - частина розмірів, до вікна не належить)
            int fieldMargins = 20;
            // Замінюємо параметри шаблона (layout) для поля на нові
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    windowWidth - 2 * fieldMargins,
                    windowWidth - 2 * fieldMargins
            );
            params.setMargins( fieldMargins, fieldMargins, fieldMargins, fieldMargins );
            params.gravity = Gravity.CENTER;
            gameField.setLayoutParams( params );
        });
        gameField.setOnTouchListener( new OnSwipeListener( this ) {
            @Override
            public void onSwipeBottom() {
                Toast.makeText(GameActivity.this, "onSwipeBottom", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onSwipeLeft() {
                Toast.makeText(GameActivity.this, "onSwipeLeft", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onSwipeRight() {
                Toast.makeText(GameActivity.this, "onSwipeRight", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onSwipeTop() {
                Toast.makeText(GameActivity.this, "onSwipeTop", Toast.LENGTH_SHORT).show();
            }
        } );
        bestScore = 0L;
        startNewGame();
    }

    private void startNewGame() {
        score = 0L;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = (int) Math.pow(2, i + j + 1);
                if(tiles[i][j] > 64) tiles[i][j] = 0;
            }
        }
        updateField();
    }

    @SuppressLint("DiscouragedApi")
    private void updateField() {
        tvScore.setText( getString( R.string.game_tv_score_tpl, scoreToString( score ) ) );
        tvBestScore.setText( getString( R.string.game_tv_best_tpl, scoreToString( bestScore ) ) );
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tvTiles[i][j].setText( String.valueOf( tiles[i][j] ) );
                tvTiles[i][j].getBackground().setColorFilter(
                        getResources().getColor(
                                getResources().getIdentifier(
                                        "game_tv_tile_bg_" + tiles[i][j],
                                        "color",
                                        getPackageName()
                                ),
                                getTheme()
                        ),
                        PorterDuff.Mode.SRC_ATOP
                );
                tvTiles[i][j].setTextColor(
                        getResources().getColor(
                                getResources().getIdentifier(
                                        "game_tv_tile_bg_" + tiles[i][j],
                                        "color",
                                        getPackageName()
                                ),
                                getTheme()
                        )
                );
            }
        }
    }

    private String scoreToString( long score ) {
        return String.valueOf( score );
    }
}