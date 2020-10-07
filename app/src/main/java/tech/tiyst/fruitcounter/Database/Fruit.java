package tech.tiyst.fruitcounter.Database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "fruits")
public class Fruit {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "entryID")
    private long entryID = 0;

    @NonNull
    @ColumnInfo(name = "fruitName")
    private String fruitName;

    @ColumnInfo(name = "count")
    private int count;

    @ColumnInfo(name = "date")
    private Date date;

    public Fruit(@NonNull String fruitName, int count, Date date) {
        this.fruitName = fruitName;
        this.count = count;
        this.date = date;
    }

    @Ignore
    public Fruit(@NonNull String fruitName) {
        this.fruitName = fruitName;
    }

    public void setEntryID(@NonNull long entryID) {
        this.entryID = entryID;
    }

    public void setFruitName(@NonNull String fruitName) {
        this.fruitName = fruitName;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @NonNull
    public long getEntryID() {
        return entryID;
    }

    @NonNull
    public String getFruitName() {
        return fruitName;
    }

    public int getCount() {
        return count;
    }

    public Date getDate() {
        return date;
    }

    @NonNull
    @Override
    public String toString() {
        return "FruitID: " + this.entryID + " fruitName: " + this.fruitName + " count: " + this.count + " date: " + this.date.toString();
    }
}
