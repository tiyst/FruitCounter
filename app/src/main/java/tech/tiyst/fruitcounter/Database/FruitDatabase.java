package tech.tiyst.fruitcounter.Database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.List;

@Database(entities = Fruit.class, version = 1)
@TypeConverters({DatabaseTypeConverters.class})
public abstract class FruitDatabase extends RoomDatabase {

    private static volatile FruitDatabase DATABASE;

    public abstract FruitDao fruitDao();

    public static FruitDatabase getDatabaseInstance(Context context) {
        if (DATABASE == null) {
            synchronized (FruitDatabase.class) {
                if (DATABASE == null) {
                    DATABASE = Room.databaseBuilder(context.getApplicationContext(),
                            FruitDatabase.class, "Fruits.db") // FIXME: 10/3/2020 Sample.db
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }

        return DATABASE;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        List<Fruit> fruits = fruitDao().getFruits();

        for (Fruit fruit : fruits) {
            sb.append(fruit.toString())
              .append("\n");
        }

        return sb.toString();
    }
}
