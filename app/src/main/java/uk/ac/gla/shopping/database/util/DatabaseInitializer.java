package uk.ac.gla.shopping.database.util;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import uk.ac.gla.shopping.database.ShoppingListItemDatabase;
import uk.ac.gla.shopping.entity.ShoppingListItem;

public class DatabaseInitializer {
    public static void populateAsync(final ShoppingListItemDatabase database) {
        new PopulateDbAsync(database).execute();
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final ShoppingListItemDatabase database;

        PopulateDbAsync(ShoppingListItemDatabase database) {
            this.database = database;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // If the Database is empty, add the initial data.
            if (database.shoppingListItemDao().rowCount() == 0) {
                List<ShoppingListItem> shoppingList = new ArrayList<>();

                String items[] = {"Lychee", "Roast duck", "Pepsi chicken crisps"};
                for (String item : items) {
                    shoppingList.add(new ShoppingListItem(item));
                }

                database.shoppingListItemDao()
                        .insertShoppingListItems(
                                shoppingList.toArray(new ShoppingListItem[shoppingList.size()]));
            }

            return null;
        }
    }
}
