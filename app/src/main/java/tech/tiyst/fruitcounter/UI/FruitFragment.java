package tech.tiyst.fruitcounter.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import tech.tiyst.fruitcounter.Database.Fruit;
import tech.tiyst.fruitcounter.R;

public class FruitFragment extends Fragment {

    private static final String TAG = "FruitFragment";

    private static final String ARG_FRUIT_ID = "ARG_FRUIT_ID";
    private static final String ARG_FRUIT_NAME = "ARG_FRUIT_NAME";
    private static final String ARG_COUNT = "ARG_COUNT";
    private static final String ARG_DATE = "ARG_DATE";

    private long fruitID;
    private String fruitName;
    private int fruitCount;
    private Date fruitDate;

    private ImageView fruitImage;
    private TextView nameText;
    private TextView dateText;
    private TextView countText;
    private ImageButton deleteButton;

    private OnFruitSelectedListener listener;

    public FruitFragment() {
        // Required empty public constructor
    }

    public static FruitFragment newInstance(Fruit fruit) {
        FruitFragment fragment = new FruitFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_FRUIT_ID, fruit.getEntryID());
        args.putString(ARG_FRUIT_NAME, fruit.getFruitName());
        args.putInt(ARG_COUNT, fruit.getCount());
        args.putSerializable(ARG_DATE, fruit.getDate());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fruitID = getArguments().getLong(ARG_FRUIT_ID);
            fruitName = getArguments().getString(ARG_FRUIT_NAME);
            fruitCount = getArguments().getInt(ARG_COUNT);
            fruitDate = (Date)getArguments().getSerializable(ARG_DATE);
        }
    }

    public void editFruitType(Fruit fruit) { //Will change both name and image
        this.nameText.setText(fruit.getFruitName());
    }

    public void editDateText(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        this.editDateText(dateFormat.format(date));
    }

    public void editDateText(String value) {
        this.dateText.setText(value);
    }

    public void editCountText(int value) {
        this.fruitCount = value;
        String s = getResources().getString(R.string.fruitCount, this.fruitCount);
        this.countText.setText(s);
    }

    public void editCountText(String value) {
        this.editCountText(Integer.parseInt(value));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fruit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.fruitImage = view.findViewById(R.id.FruitImage);
        this.nameText = view.findViewById(R.id.FruitNameText);
        this.dateText = view.findViewById(R.id.FruitDateText);
        this.countText = view.findViewById(R.id.FruitCountText);
        this.deleteButton = view.findViewById(R.id.deleteButton);
        this.deleteButton.setOnClickListener(v -> listener.deleteFruit(fruitID));

        this.initIdAttributes();
        view.setOnClickListener(v -> fragmentClicked());
    }

    private void initIdAttributes() {
        this.fruitImage.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.banana, null));
        this.nameText.setText(fruitName);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        this.dateText.setText(dateFormat.format(fruitDate));
        this.countText.setText(getResources().getString(R.string.fruitCount, this.fruitCount));
    }

    public void setListener(OnFruitSelectedListener listener) {
        this.listener = listener;
    }

    private void fragmentClicked() {
        listener.onFruitSelected(this.fruitID);
    }

    public interface OnFruitSelectedListener {
        void onFruitSelected(long id);
        void deleteFruit(long id);
    }
}