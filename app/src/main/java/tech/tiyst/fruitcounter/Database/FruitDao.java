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

    @Query("SELECT * FROM fruits WHERE entryID=:id")
    Fruit getFruit(long id);

    @Query("SELECT * FROM fruits")
    List<Fruit> getFruits();

    @Query("SELECT * FROM fruits WHERE date BETWEEN :from AND :to")
    List<Fruit> getFruits(Date from, Date to);


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertFruit(Fruit fruit);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertFruits(List<Fruit> fruits);

    @Update
    int updateFruit(Fruit fruit);

    // === returns lines affected
    @Query("DELETE FROM fruits WHERE entryID IS :ID")
    int deleteFruit(Long ID);

    @Query("DELETE FROM fruits WHERE date BETWEEN :from AND :to")
    int deleteFruits(Date from, Date to);

    @Query("DELETE FROM fruits")
    int deleteAllFruits();

}
