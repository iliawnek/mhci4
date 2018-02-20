package uk.ac.gla.shopping.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import uk.ac.gla.shopping.dao.ShoppingListItemDAO;
import uk.ac.gla.shopping.entity.ShoppingListItem;

@Database(entities = {ShoppingListItem.class}, version = 1, exportSchema = false)
public abstract class ShoppingListItemDatabase extends RoomDatabase {
    private static ShoppingListItemDatabase INSTANCE;

    public static ShoppingListItemDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    ShoppingListItemDatabase.class,
                    "ShoppingListItemDatabase")
                    .build();
        }

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public abstract ShoppingListItemDAO shoppingListItemDao();
}
