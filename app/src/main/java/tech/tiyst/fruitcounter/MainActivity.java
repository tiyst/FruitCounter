package tech.tiyst.fruitcounter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tech.tiyst.fruitcounter.Database.Fruit;
import tech.tiyst.fruitcounter.Database.FruitDatabase;
import tech.tiyst.fruitcounter.UI.EditFruitDialog;
import tech.tiyst.fruitcounter.UI.FruitFragment;

// TODO: 10/17/2020 edit fruit

public class MainActivity extends AppCompatActivity
        implements FruitFragment.OnFruitSelectedListener, EditFruitDialog.EditDialogListener {

    private static final String TAG = "MainActivity";
    private static final String ARG_FRUIT = "ARG_FRUIT";

    private FruitDatabase DATABASE;
    private ExecutorService executorService;
    private FragmentManager fManager;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.fManager = getSupportFragmentManager();
        this.DATABASE = FruitDatabase.getDatabaseInstance(this);
        this.executorService = Executors.newFixedThreadPool(8);

        populateFruitList();
        initFab();
    }

    private void initFab() {
        fab = findViewById(R.id.addFruitFab);
    }

    private void populateFruitList() {
        FragmentTransaction fragmentTransaction = fManager.beginTransaction();
        Log.d(TAG, " getting fruits");
        for (Fruit fruit : this.DATABASE.fruitDao().getFruits()) {
            fragmentTransaction.add(R.id.fruitListLayout, FruitFragment.newInstance(fruit), String.valueOf(fruit.getEntryID()));
        }

        fragmentTransaction.commit();
    }

    public void addButton(View v) {
        Log.d(TAG, "addButton: ");
        EditFruitDialog editFruitDialog = new EditFruitDialog();
        editFruitDialog.show(getSupportFragmentManager(), EditFruitDialog.class.getSimpleName());
    }

    public void addRandomFruit(View v) {
        addFruitTesting();
    }

    public void removeButton(View v) {
        Log.d(TAG, "removeButton: removing all");
        removeAllFruits();
    }

    private void addFruitTesting() {
        Random rng = new Random();
        addFruit("Banana", rng.nextInt(10), new Date());
    }

    private void addFruit(Fruit fruit) {
        this.addFruit(fruit.getFruitName(), fruit.getCount(), fruit.getDate());
    }

    private void addFruit(String name, int count, Date date) {
        Fruit fruit = new Fruit(name, count, date);
        fruit.setEntryID(DATABASE.fruitDao().insertFruit(fruit)); //Database insert doesn't touch local fruit object
        FragmentTransaction ft = fManager.beginTransaction();
        FruitFragment fruitFragment = FruitFragment.newInstance(fruit);
        ft.add(R.id.fruitListLayout, fruitFragment, String.valueOf(fruit.getEntryID()));
        ft.commit();
    }

    private void editFruit(Fruit fruit) {
        EditFruitDialog editFruitDialog = new EditFruitDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_FRUIT, fruit);
        editFruitDialog.setArguments(bundle);
        editFruitDialog.show(getSupportFragmentManager(), EditFruitDialog.class.getSimpleName());
    }

    private void removeFruit(Fruit fruit) {
        DATABASE.fruitDao().deleteFruit(fruit.getEntryID());
    }

    private void removeAllFruits() {
        DATABASE.fruitDao().deleteAllFruits();
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        if (fragment instanceof FruitFragment) {
            FruitFragment fruitFragment = (FruitFragment) fragment;
            fruitFragment.setListener(this);
        }
    }

    @Override
    public void onFruitSelected(long id) {
        Log.d(TAG, "onFruitSelected: " + id);
        Fruit fruit = DATABASE.fruitDao().getFruit(id);
        if (fruit != null) {
            editFruit(fruit);
        }
    }

    @Override
    public void deleteFruit(long id) {
        Fragment f = fManager.findFragmentByTag(String.valueOf(id));
        if (f != null) {
            int linesAffected = DATABASE.fruitDao().deleteFruit(id);
            Log.d(TAG, "deleteFruit: lines affected: " + linesAffected);

            if (linesAffected == 1) {
                FragmentTransaction ft = fManager.beginTransaction()
                    .setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.fade_out,
                        R.anim.fade_in,
                        R.anim.slide_out
                    );
                ft.remove(f);
                ft.commit();
            }
        }
    }

    @Override
    public void addFruitFromDialog(Fruit fruit) {
        Log.e(TAG, "addFruitFromDialog: ");
        addFruit(fruit);
    }

    @Override
    public void editFruitFromDialog(Fruit fruit) {
        Log.e(TAG, "editFruitFromDialog: ");

        Fruit f = DATABASE.fruitDao().getFruit(fruit.getEntryID());
        if (f != null) {
            FruitFragment frag = (FruitFragment)
                    getSupportFragmentManager().findFragmentByTag(String.valueOf(fruit.getEntryID()));
            if (frag != null) {
                int linesAffected = DATABASE.fruitDao().updateFruit(fruit);
                frag.editFruitType(fruit);
                frag.editCountText(fruit.getCount());
                frag.editDateText(fruit.getDate());

                Log.d(TAG, "edit fruit from dialog: update lines affected: " + linesAffected);
            }
        }
    }
}