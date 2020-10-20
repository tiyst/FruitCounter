package tech.tiyst.fruitcounter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import tech.tiyst.fruitcounter.UI.EditFruitActivity;
import tech.tiyst.fruitcounter.UI.FruitFragment;

public class MainActivity extends AppCompatActivity
        implements FruitFragment.OnFruitSelectedListener {

    private static final String TAG = "MainActivity";
    private static final int RESULT_CODE_ADD = 2;
    private static final int RESULT_CODE_EDIT = 3;

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
        for (Fruit fruit : this.DATABASE.fruitDao().getFruits()) {
            fragmentTransaction.add(R.id.fruitListLayout, FruitFragment.newInstance(fruit), String.valueOf(fruit.getEntryID()));
        }

        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_CODE_ADD) {
                String fruitName = data.getStringExtra(EditFruitActivity.ARG_FRUIT_NAME);
                int fruitCount = data.getIntExtra(EditFruitActivity.ARG_COUNT,-1);
                Date fruitDate = (Date) data.getSerializableExtra(EditFruitActivity.ARG_DATE);
                addFruit(fruitName, fruitCount, fruitDate);
            }
            if (requestCode == RESULT_CODE_EDIT) {
                // TODO: 10/19/2020 do this 
                Log.e(TAG, "onActivityResult: editing fruit finished" );
                int fruitName = data.getIntExtra(EditFruitActivity.ARG_FRUIT_ID,-1);
                getFragmentManager().findFragmentByTag("ytes");


            }
        }
    }

    public void addButton(View v) {
        Log.d(TAG, "addButton: ");
        Intent intent = new Intent(this, EditFruitActivity.class);
        startActivityForResult(intent, RESULT_CODE_ADD);
    }

    public void addRandomFruit(View v) {
        addFruitTesting();
    }

    public void showButton(View v) {
        Log.d(TAG, "showButton: \n" + DATABASE.toString());
    }

    public void removeButton(View v) {
        Log.d(TAG, "removeButton: removing all");
        removeAllFruits();
    }

    private void addFruit(String name, int count, Date date) {
        Fruit fruit = new Fruit(name, count, date);
        fruit.setEntryID(DATABASE.fruitDao().insertFruit(fruit)); //Database insert doesn't touch local fruit object
        FragmentTransaction ft = fManager.beginTransaction();
        FruitFragment fruitFragment = FruitFragment.newInstance(fruit);
        ft.add(R.id.fruitListLayout, fruitFragment, String.valueOf(fruit.getEntryID()));
        ft.commit();
    }

    private void addFruitTesting() {
        Random rng = new Random();
        addFruit("banana", rng.nextInt(10), new Date());
    }

    private void removeAllFruits() {
        DATABASE.fruitDao().deleteAllFruits();
        // TODO: 10/5/2020 remove all fragments
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
        Intent intent = new Intent(this, EditFruitActivity.class);
        Fruit fruit = DATABASE.fruitDao().getFruit(id);
        intent.putExtra(EditFruitActivity.ARG_FRUIT_NAME, fruit.getFruitName());
        intent.putExtra(EditFruitActivity.ARG_COUNT, fruit.getCount());
        intent.putExtra(EditFruitActivity.ARG_DATE, fruit.getDate());
        startActivityForResult(intent, RESULT_CODE_EDIT);
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
    //            ft.hide(f); //What does hide do?
                ft.remove(f);
                ft.commit();
            }
        }
    }
}