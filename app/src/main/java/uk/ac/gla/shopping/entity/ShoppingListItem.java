package uk.ac.gla.shopping.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class ShoppingListItem {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    /**
     * Default Constructor
     * <p>
     * Room Database will use this no-arg constructor by default.
     * The others are annotated with @Ignore,
     * so Room will not give a warning about "Multiple Good Constructors".
     */
    public ShoppingListItem() {
    }

    @Ignore
    public ShoppingListItem(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
