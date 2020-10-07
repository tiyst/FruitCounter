package tech.tiyst.fruitcounter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tech.tiyst.fruitcounter.Database.Fruit;
import tech.tiyst.fruitcounter.Database.FruitDatabase;
import tech.tiyst.fruitcounter.UI.FruitFragment;

public class MainActivity extends AppCompatActivity
        implements FruitFragment.OnFruitSelectedListener {

    private static final String TAG = "MainActivity";

    private FruitDatabase DATABASE;
    private ExecutorService executorService;
    private FragmentManager fManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.fManager = getSupportFragmentManager();
        this.DATABASE = FruitDatabase.getDatabaseInstance(this);
        this.executorService = Executors.newFixedThreadPool(8);

        populateFruitList();
    }

    private void populateFruitList() {
        FragmentTransaction fragmentTransaction = fManager.beginTransaction();
        for (Fruit fruit : this.DATABASE.fruitDao().getFruits()) {
            fragmentTransaction.add(R.id.fruitListLayout, FruitFragment.newInstance(fruit), String.valueOf(fruit.getEntryID()));
        }

        fragmentTransaction.commit();
    }

    public void addButton(View v) {
        Log.d(TAG, "addButton: ");
        addFruit();
    }
    
    public void showButton(View v) {
        Log.d(TAG, "showButton: \n" + DATABASE.toString());
    }

    public void removeButton(View v) {
        Log.d(TAG, "removeButton: removing all");
        removeAllFruits();
    }

    private void addFruit() {
        Random rng = new Random();
        Fruit fruit = new Fruit("banana", rng.nextInt(10), new Date());
        DATABASE.fruitDao().insertFruit(fruit);
        FragmentTransaction ft = fManager.beginTransaction();
        ft.add(R.id.fruitListLayout, FruitFragment.newInstance(fruit), String.valueOf(fruit.getEntryID()));
        ft.commit();
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
    }

    @Override
    public void deleteFruit(long id) {
        Fragment f = fManager.findFragmentByTag(String.valueOf(id));
        if (f != null) {
            FragmentTransaction ft = fManager.beginTransaction()
                    .setCustomAnimations(
                    R.anim.slide_in,  // enter
                    R.anim.fade_out,  // exit
                    R.anim.fade_in,   // popEnter
                    R.anim.slide_out  // popExit
            );
            ft.hide(f);
            ft.commit();
            DATABASE.fruitDao().deleteFruit(id);
        }
    }
}