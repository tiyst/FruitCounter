package tech.tiyst.fruitcounter.UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import tech.tiyst.fruitcounter.R;

public class EditFruitActivity extends AppCompatActivity {

    private static final String TAG = EditFruitActivity.class.getName();

    public static final String ARG_NAME = "ARG_NAME";
    public static final String ARG_COUNT = "ARG_COUNT";
    public static final String ARG_DATE = "ARG_DATE";

    private TextView nameText;
    private EditText dateText;
    private EditText countText;

    private final Calendar cal = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fruit);
        this.dateText = findViewById(R.id.editTextDate);
        this.countText = findViewById(R.id.editTextCount);
        this.nameText = findViewById(R.id.fruitNameTextView);

        initDatePicker();
    }

    public void onSubmitClicked(View view) {
        Intent intent = new Intent();
        intent.putExtra(ARG_NAME, nameText.getText());
        intent.putExtra(ARG_COUNT, Integer.parseInt(String.valueOf(countText.getText()))); //Disgusting
        intent.putExtra(ARG_DATE, cal.getTime());
        setResult(RESULT_OK, intent);
        finish();
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, monthOfYear);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateLabel();
        };

        this.dateText.setOnClickListener(v -> {
            new DatePickerDialog(EditFruitActivity.this, date, cal
                    .get(Calendar.YEAR), cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void updateDateLabel() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        this.dateText.setText(sdf.format(this.cal.getTime()));
    }
}