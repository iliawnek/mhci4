package uk.ac.gla.shopping.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import uk.ac.gla.shopping.database.ShoppingListItemDatabase;
import uk.ac.gla.shopping.entity.ShoppingListItem;

public class ShoppingListItemsViewModel extends AndroidViewModel {
    private final LiveData<List<ShoppingListItem>> shoppingListItems;

    public ShoppingListItemsViewModel(@NonNull Application application) {
        super(application);

        shoppingListItems = ShoppingListItemDatabase
                .getInstance(getApplication())
                .shoppingListItemDao()
                .getAllShoppingListItems();
    }

    public LiveData<List<ShoppingListItem>> getShoppingListItems() {
        return shoppingListItems;
    }
}
