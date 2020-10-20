package tech.tiyst.fruitcounter.UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import tech.tiyst.fruitcounter.FruitCounterException;
import tech.tiyst.fruitcounter.R;

public class EditFruitActivity extends AppCompatActivity {

    private static final String TAG = EditFruitActivity.class.getName();

    public static final String ARG_FRUIT_ID = "ARG_FRUIT_ID";
    public static final String ARG_FRUIT_NAME = "ARG_FRUIT_NAME";
    public static final String ARG_COUNT = "ARG_COUNT";
    public static final String ARG_DATE = "ARG_DATE";

    private int fruitNameID;
    private int count;
    private Date fruitDate;

    private TextView nameText;
    private EditText dateText;
    private EditText countText;
    private ImageView imageView;

    private final Calendar cal = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fruit);

        initViews();
        fillViews();
        initDatePicker();
        updateDateLabel();
    }

    private void initViews() {
        this.dateText = findViewById(R.id.editTextDate);
        this.countText = findViewById(R.id.editTextCount);
        this.nameText = findViewById(R.id.fruitNameTextView);
        this.imageView = findViewById(R.id.editFruitImageView);
    }

    private void fillViews() throws FruitCounterException {
        Bundle data = getIntent().getExtras();
        String name = "Banana";
        int count = 0;
        Date date;
        if (data != null && data.size() == 3) { //Editing existing fruit
            name = data.getString(ARG_FRUIT_NAME);
            count = data.getInt(ARG_COUNT);
            date = (Date) data.getSerializable(ARG_DATE);
            cal.setTime(date);
        }

        this.nameText.setText(name);
        this.countText.setText(String.valueOf(count));
        updateDateLabel();
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