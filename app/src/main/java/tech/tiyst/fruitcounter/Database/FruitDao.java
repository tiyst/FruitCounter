package tech.tiyst.fruitcounter.Database;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

@Dao
public interface FruitDao {

    @Query("SELECT * FROM fruits")
    List<Fruit> getFruits();

    @Query("SELECT * FROM fruits WHERE date BETWEEN :from AND :to")
    List<Fruit> getFruits(Date from, Date to);


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertFruit(Fruit fruit);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertFruits(List<Fruit> fruits);

    @Update
    void updateFruid(Fruit fruit);


    @Query("DELETE FROM fruits WHERE entryID IS :ID")
    void deleteFruit(Long ID);

    @Query("DELETE FROM fruits WHERE date BETWEEN :from AND :to")
    void deleteFruits(Date from, Date to);

    @Query("DELETE FROM fruits")
    void deleteAllFruits();

}
