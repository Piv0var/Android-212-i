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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private TextView tvScore;
    private TextView tvBestScore;
    private long score;
    private long bestScore;
    private final int N = 4;
    private final int[][] tiles = new int[N][N];
    private final TextView[][] tvTiles = new TextView[N][N];
    private final Random random = new Random();
    private boolean testMode = true; // Переключатель тестового режима

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
        tvScore = findViewById(R.id.game_tv_score);
        tvBestScore = findViewById(R.id.game_tv_best);
        LinearLayout gameField = findViewById(R.id.game_layout_field);

        gameField.post(() -> {
            int windowWidth = this.getWindow().getDecorView().getWidth();
            int fieldMargins = 20;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    windowWidth - 2 * fieldMargins,
                    windowWidth - 2 * fieldMargins
            );
            params.setMargins(fieldMargins, fieldMargins, fieldMargins, fieldMargins);
            params.gravity = Gravity.CENTER;
            gameField.setLayoutParams(params);
        });

        gameField.setOnTouchListener(new OnSwipeListener(this) {
            @Override
            public void onSwipeBottom() {
                Toast.makeText(GameActivity.this, "onSwipeBottom", Toast.LENGTH_SHORT).show();
                if (testMode) {
                    toggleTestMode();
                }
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
        });

        bestScore = 0L;
        startNewGame();
    }

    private void startNewGame() {
        score = 0L;

        if (testMode) {
            // Заполняем поле тестовыми значениями от 0 до 64
            fillWithTestValues();
        } else {
            // Инициализируем все ячейки нулями (пустыми)
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    tiles[i][j] = 0;
                }
            }

            // Добавляем две случайные ячейки
            addRandomTile();
            addRandomTile();
            addRandomTile();
            addRandomTile();
            addRandomTile();
            addRandomTile();
            addRandomTile();
            addRandomTile();
            addRandomTile();
            addRandomTile();
        }

        updateField();
    }

    private void toggleTestMode() {
        testMode = !testMode;
        startNewGame();
        Toast.makeText(this, "Test mode: " + (testMode ? "ON" : "OFF"), Toast.LENGTH_SHORT).show();
    }

    private void fillWithTestValues() {
        int[] values = {0, 2, 4, 8, 16, 32, 64};

//        for (int i = 0; i < N; i++) {
//            for (int j = 0; j < N; j++) {
//                int index = (i * N + j) % values.length;
//                tiles[i][j] = values[index];
//            }
//        }

        // Второй вариант: случайное заполнение всеми значениями
        // Раскомментируйте этот код, если хотите случайное размещение значений

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int randomIndex = random.nextInt(values.length);
                tiles[i][j] = values[randomIndex];
            }
        }

    }

    // Добавляет новую случайную ячейку (2 или 4) в случайное пустое место
    private void addRandomTile() {
        // Находим все пустые ячейки
        List<int[]> emptyCells = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tiles[i][j] == 0) {
                    emptyCells.add(new int[] {i, j});
                }
            }
        }
        if (emptyCells.isEmpty()) {
            return;
        }
        int[] randomCell = emptyCells.get(random.nextInt(emptyCells.size()));
        int row = randomCell[0];
        int col = randomCell[1];
        tiles[row][col] = random.nextInt(10) < 9 ? 2 : 4;
    }

    @SuppressLint("DiscouragedApi")
    private void updateField() {
        tvScore.setText(getString(R.string.game_tv_score_tpl, scoreToString(score)));
        tvBestScore.setText(getString(R.string.game_tv_best_tpl, scoreToString(bestScore)));
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                // Для пустых ячеек (со значением 0) устанавливаем пустой текст
                if (tiles[i][j] == 0) {
                    tvTiles[i][j].setText("");
                } else {
                    tvTiles[i][j].setText(String.valueOf(tiles[i][j]));
                }

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
                                        "game_tv_tile_fg_" + tiles[i][j],
                                        "color",
                                        getPackageName()
                                ),
                                getTheme()
                        )
                );
            }
        }
    }

    private String scoreToString(long score) {
        return String.valueOf(score);
    }
}