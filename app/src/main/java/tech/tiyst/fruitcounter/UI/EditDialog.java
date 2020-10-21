package tech.tiyst.fruitcounter.UI;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import tech.tiyst.fruitcounter.Database.Fruit;
import tech.tiyst.fruitcounter.FCRuntimeException;
import tech.tiyst.fruitcounter.R;

import static tech.tiyst.fruitcounter.UI.EditFruitActivity.ARG_FRUIT;

public class EditDialog extends AppCompatDialogFragment {
	private TextView nameText;
	private EditText dateText;
	private EditText countText;
	private ImageButton calendarButton;
	private EditDialogListener listener;

	private boolean isEditingFruit;
	private Fruit fruit;


	private final Calendar cal = Calendar.getInstance();

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.edit_dialog, null);
		builder.setView(view)
				.setTitle("IT FUCKING WORKS!!!")
				.setNegativeButton("Cancel", (dialogInterface, i) -> {})
				.setPositiveButton("Submit", (dialogInterface, i) ->
						listener.applyFruit(this.fruit));
		initViews(view);
		pullData();
		return builder.create();
	}

	private Fruit getDefaultFruit() {
		return new Fruit("Banana", 0, new Date());
	}

	private void initViews(View view) {
		this.nameText = view.findViewById(R.id.editDialogFruitNameText);
		this.dateText = view.findViewById(R.id.editDialogFruitDateText);
		this.countText = view.findViewById(R.id.editDialogCountText);
		this.calendarButton = view.findViewById(R.id.editDialogCalendarButton);

		DatePickerDialog.OnDateSetListener date = (v, year, monthOfYear, dayOfMonth) -> {
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, monthOfYear);
			cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			this.fruit.setDate(cal.getTime());
			updateDateText();
		};

		this.calendarButton.setOnClickListener(v -> {
			new DatePickerDialog(getContext() /*view.getContext()*/, date, cal
					.get(Calendar.YEAR), cal.get(Calendar.MONTH),
					cal.get(Calendar.DAY_OF_MONTH)).show();
		});
	}

	private void pullData() throws FCRuntimeException {
		this.fruit = (Fruit) getArguments().getSerializable(ARG_FRUIT);
		if (this.fruit != null) { //Editing existing fruit
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
		void applyFruit(Fruit fruit);
	}
}