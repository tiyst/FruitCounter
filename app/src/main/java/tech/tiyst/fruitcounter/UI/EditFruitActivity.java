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

import tech.tiyst.fruitcounter.Database.Fruit;
import tech.tiyst.fruitcounter.FCRuntimeException;
import tech.tiyst.fruitcounter.R;

public class EditFruitActivity extends AppCompatActivity {

    private static final String TAG = EditFruitActivity.class.getName();

    public static final String ARG_FRUIT = "ARG_FRUIT";
    public static final String ARG_FRUIT_ID = "ARG_FRUIT_ID";
    public static final String ARG_FRUIT_NAME = "ARG_FRUIT_NAME";
    public static final String ARG_COUNT = "ARG_COUNT";
    public static final String ARG_DATE = "ARG_DATE";

    private Fruit fruit;

    //Is a new fruit being added or one being edited, necessary for return code
    private boolean isEditingFruit;

    private TextView nameText;
    private EditText dateText;
    private EditText countText;
    private ImageView imageView;

    private final Calendar cal = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fruit);

        this.isEditingFruit = false;

        initViews();
        pullData();
        updateLabels();
    }

    private Fruit getDefaultFruit() {
        return new Fruit("Banana", 0, new Date());
    }

    private void initViews() {
        this.dateText = findViewById(R.id.editTextDate);
        this.countText = findViewById(R.id.editTextCount);
        this.nameText = findViewById(R.id.fruitNameTextView);
        this.imageView = findViewById(R.id.editFruitImageView);

        //Init DatePicker
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, monthOfYear);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabels();
        };

        this.dateText.setOnClickListener(v -> {
            new DatePickerDialog(EditFruitActivity.this, date, cal
                    .get(Calendar.YEAR), cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void pullData() throws FCRuntimeException {
        Bundle data = getIntent().getExtras();
        if (data != null) { //Editing existing fruit
            this.isEditingFruit = true;
            this.fruit = (Fruit) data.getSerializable(ARG_FRUIT);
        } else {
            this.fruit = getDefaultFruit();
        }
        cal.setTime(this.fruit.getDate());

    }

    public void onSubmitClicked(View view) {
        Intent intent = new Intent();
//        intent.putExtra(ARG_FRUIT_ID, this.fruitFragmentID);
//        intent.putExtra(ARG_FRUIT_NAME, nameText.getText());
//        intent.putExtra(ARG_COUNT, Integer.parseInt(String.valueOf(countText.getText()))); //Disgusting
//        intent.putExtra(ARG_DATE, cal.getTime());

        intent.putExtra(ARG_FRUIT, this.fruit);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void updateLabels() {
        this.nameText.setText(this.fruit.getFruitName());
        this.countText.setText(String.valueOf(this.fruit.getCount()));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        this.dateText.setText(sdf.format(this.cal.getTime())); // FIXME: 10/20/2020 calendar or fruit date?
    }
}