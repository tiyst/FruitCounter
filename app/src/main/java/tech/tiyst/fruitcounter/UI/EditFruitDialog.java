package tech.tiyst.fruitcounter.UI;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.res.ResourcesCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import tech.tiyst.fruitcounter.Database.Fruit;
import tech.tiyst.fruitcounter.FCRuntimeException;
import tech.tiyst.fruitcounter.R;

public class EditFruitDialog extends AppCompatDialogFragment {

	private static final String TAG = "EditFruitDialog";
	private static final String ARG_FRUIT = "ARG_FRUIT";

	private final Calendar cal = Calendar.getInstance();

	private TextView nameText;
	private EditText dateText;
	private EditText countText;
	private ImageButton calendarButton;
	private EditDialogListener listener;
	private ImageView fruitImage;

	private boolean isEditingFruit;
	private Fruit fruit;

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.edit_dialog, null);
		builder.setView(view)
				.setTitle("Bananas?")
				.setNegativeButton("Cancel", (dialogInterface, i) -> {})
				.setPositiveButton("Submit", (dialogInterface, i) -> {
					if (fruit.getCount() == 0) {
						Log.d(TAG, "onCreateDialog: Empty count?");
						return;
					}
					if (isEditingFruit) {
						listener.editFruitFromDialog(fruit);
					} else {
						listener.addFruitFromDialog(fruit);
					}
				});

		pullData();
		initViews(view);
		return builder.create();
	}

	private Fruit getDefaultFruit() {
		return new Fruit("Banana", 0, new Date());
	}

	private void initViews(View view) {
		this.nameText = view.findViewById(R.id.editDialogFruitNameText);

		initCountView(view);
		initDateView(view);
		updateDateText();
//		updateCountText();
		updateFruitText();
	}

	private void initCountView(View view) {
		this.countText = view.findViewById(R.id.editDialogCountText);
		this.countText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

			@Override
			public void afterTextChanged(Editable editable) {
				try {
					String s = editable.toString();
					if (!s.equals("")) {
						int value = Integer.parseInt(s);
						fruit.setCount(value);
						Log.d(TAG, "CountTextValue: " + value);
					}
				} catch (NumberFormatException ex) {
					Toast.makeText(view.getContext(), "Dud, only numbers yes?", Toast.LENGTH_SHORT).show();
					countText.setText("0");
					fruit.setCount(0);
				}
			}
		});
	}

	private void initDateView(View view) {
		this.dateText = view.findViewById(R.id.editDialogFruitDateText);
		this.calendarButton = view.findViewById(R.id.editDialogCalendarButton);
		this.fruitImage = view.findViewById(R.id.editDialogFruitImage);

		DatePickerDialog.OnDateSetListener date = (v, year, monthOfYear, dayOfMonth) -> {
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, monthOfYear);
			cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			this.fruit.setDate(cal.getTime());
			updateDateText();
		};

		this.calendarButton.setOnClickListener(v ->
				new DatePickerDialog(getContext(), date, cal
						.get(Calendar.YEAR), cal.get(Calendar.MONTH),
						cal.get(Calendar.DAY_OF_MONTH)).show());
	}

	private void pullData() throws FCRuntimeException {
		if (getArguments() != null) { //add doesn't provide arguments
			this.fruit = (Fruit) getArguments().getSerializable(ARG_FRUIT);
			this.isEditingFruit = true;
		} else {
			this.fruit = getDefaultFruit();
		}
		cal.setTime(this.fruit.getDate());
	}

	private void updateDateText() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
		this.dateText.setText(dateFormat.format(this.cal.getTime()));
	}

	private void updateCountText() {
		this.countText.setText(String.valueOf(this.fruit.getCount()));
	}
	
	private void updateFruitText() {
		// TODO: 10/23/2020 fruit image
		this.nameText.setText(this.fruit.getFruitName());
		this.fruitImage.setImageResource(R.drawable.banana);
	}

	@Override
	public void onAttach(@NonNull Context context) {
		super.onAttach(context);
		try {
			listener = (EditDialogListener) context;
		} catch (ClassCastException e) {
			throw new ClassCastException(context.toString() +
					"must implement EditDialogListener");
		}
	}

	public interface EditDialogListener {
		void addFruitFromDialog(Fruit fruit);
		void editFruitFromDialog(Fruit fruit);
	}
}

