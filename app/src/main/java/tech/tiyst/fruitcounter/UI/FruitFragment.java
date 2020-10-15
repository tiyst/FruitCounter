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
import androidx.fragment.app.Fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import tech.tiyst.fruitcounter.Database.Fruit;
import tech.tiyst.fruitcounter.R;

public class FruitFragment extends Fragment {

    private static final String ARG_ID = "argFruitID";
    private static final String ARG_NAME = "argFruitName";
    private static final String ARG_COUNT = "argFruitCount";
    private static final String ARG_DATE = "argFruitDate";

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

    private void initIdAttributes() {

        this.nameText.setText(fruitName);
        DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy hh:mm");
//        DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy hh:mm", Locale.GERMAN);
        this.dateText.setText(dateFormat.format(fruitDate));
        this.countText.setText(getResources().getString(R.string.fruitCount, this.fruitCount));

    }

    public FruitFragment() {
        // Required empty public constructor
    }

    public static FruitFragment newInstance(Fruit fruit) {
        FruitFragment fragment = new FruitFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, fruit.getEntryID());
        args.putString(ARG_NAME, fruit.getFruitName());
        args.putInt(ARG_COUNT, fruit.getCount());
        args.putSerializable(ARG_DATE, fruit.getDate());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fruitID = getArguments().getLong(ARG_ID);
            fruitName = getArguments().getString(ARG_NAME);
            fruitCount = getArguments().getInt(ARG_COUNT);
            fruitDate = (Date)getArguments().getSerializable(ARG_DATE);
        }
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

    public void setListener(OnFruitSelectedListener listener) {
        this.listener = listener;
    }

    private void fragmentClicked() {
        listener.onFruitSelected(this.fruitID);
    }

    public interface OnFruitSelectedListener {
        public void onFruitSelected(long id);
        public void deleteFruit(long id);
    }
}