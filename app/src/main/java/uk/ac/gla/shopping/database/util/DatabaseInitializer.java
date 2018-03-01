package uk.ac.gla.shopping.database.util;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import uk.ac.gla.shopping.database.PhraseDatabase;
import uk.ac.gla.shopping.database.ShoppingListItemDatabase;
import uk.ac.gla.shopping.entity.Phrase;
import uk.ac.gla.shopping.entity.ShoppingListItem;

public class DatabaseInitializer {
    public static void populateAsync(final ShoppingListItemDatabase shoppingListItemDatabase, final PhraseDatabase phraseDatabase) {
        new PopulateDbAsync(shoppingListItemDatabase, phraseDatabase).execute();
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final ShoppingListItemDatabase shoppingListItemDatabase;
        private final PhraseDatabase phraseDatabase;

        PopulateDbAsync(ShoppingListItemDatabase shoppingListItemDatabase, PhraseDatabase phraseDatabase) {
            this.shoppingListItemDatabase = shoppingListItemDatabase;
            this.phraseDatabase = phraseDatabase;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // If the shopping list item database is empty, add the initial data.
            if (shoppingListItemDatabase.shoppingListItemDao().rowCount() == 0) {
                List<ShoppingListItem> shoppingList = new ArrayList<>();

                String items[] = {"Lychee", "Roast duck", "Pepsi chicken crisps"};
                for (String item : items) {
                    shoppingList.add(new ShoppingListItem(item));
                }

                shoppingListItemDatabase.shoppingListItemDao()
                        .insertShoppingListItems(
                                shoppingList.toArray(new ShoppingListItem[shoppingList.size()]));
            }

            // If the phrase database is empty, add the initial data.
            if (phraseDatabase.phraseDao().rowCount() == 0) {
                List<Phrase> phrasebook = new ArrayList<>();

                String phrases[] = {
                        "Where is the checkout?",
                        "Do you accept cash?",
                        "Do you accept credit cards?",
                        "Where is the toilet?",
                        "Do you have baby-changing facilities?",
                        "Do you have disabled toilets?",
                        "Where is the entrance?",
                        "Where is the exit?",
                        "What are the opening times?"
                };
                for (String phrase : phrases) {
                    phrasebook.add(new Phrase(phrase));
                }

                phraseDatabase.phraseDao().insertPhrases(phrasebook.toArray(new Phrase[phrasebook.size()]));
            }

            return null;
        }
    }
}
