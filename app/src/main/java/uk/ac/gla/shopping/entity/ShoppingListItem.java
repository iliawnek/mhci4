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

    @ColumnInfo(name = "translation")
    private String translation;

    @ColumnInfo(name = "isDone")
    private boolean isDone;

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
        this.isDone = false;
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

    public String getTranslation() {
        return this.translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public boolean isDone() {
        return this.isDone;
    }

    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }
}
