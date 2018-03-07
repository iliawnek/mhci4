package uk.ac.gla.shopping.recyclerview;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.List;

import uk.ac.gla.shopping.activity.MainActivity;
import uk.ac.gla.shopping.database.ShoppingListItemDatabase;
import uk.ac.gla.shopping.databinding.RecyclerItemBinding;
import uk.ac.gla.shopping.entity.ShoppingListItem;

public class ShoppingListActivityRecyclerViewAdapter extends RecyclerView.Adapter<ShoppingListActivityRecyclerViewAdapter.ShoppingListActivityRecyclerViewHolder> {

    private final Context context;
    private List<ShoppingListItem> shoppingListItems;

    public ShoppingListActivityRecyclerViewAdapter(List<ShoppingListItem> shoppingListItems, Context context) {
        this.shoppingListItems = shoppingListItems;
        this.context = context;
    }

    @Override
    public ShoppingListActivityRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerItemBinding itemBinding = RecyclerItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);

        return new ShoppingListActivityRecyclerViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(ShoppingListActivityRecyclerViewHolder holder, int position) {
        ShoppingListItem shoppingListItem = shoppingListItems.get(position);
        holder.bind(shoppingListItem, this.context);
    }

    @Override
    public int getItemCount() {
        return shoppingListItems.size();
    }

    public void setShoppingListItems(List<ShoppingListItem> shoppingListItems) {
        this.shoppingListItems = shoppingListItems;
        notifyDataSetChanged();
    }

    static class ShoppingListActivityRecyclerViewHolder extends RecyclerView.ViewHolder {

        final RecyclerItemBinding binding;

        ShoppingListActivityRecyclerViewHolder(RecyclerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ShoppingListItem item, Context context) {
            binding.shoppingListItemNameTextView.setText(item.getName());

            binding.shoppingListItemCheckBox.setChecked(item.isDone());

            binding.deleteButton.setVisibility(item.isDone() ? View.VISIBLE : View.GONE);
            binding.optionsButton.setVisibility(item.isDone() ? View.GONE : View.VISIBLE);

            binding.deleteButton.setOnClickListener(new
            class TranslationTask extends AsyncTask<String, Void, String> {
                @Override
                protected String doInBackground(String... params) {
                    Translate translate = TranslateOptions.newBuilder().setApiKey(MainActivity.API_KEY).build().getService();
                    Translation translation =
                            translate.translate(
                                    params[0],
                                    Translate.TranslateOption.sourceLanguage(params[1]),
                                    Translate.TranslateOption.targetLanguage(params[2]));

                    // Store translation in DB
                    item.setTranslation(translation.getTranslatedText());
                    ShoppingListItemDatabase.getInstance(context).shoppingListItemDao().updateShoppingListItems(item);

                    return translation.getTranslatedText();
                }

                @Override
                protected void onPostExecute(String result) {
                    binding.shoppingListItemTranslationTextView.setText(result);
                }
            });

            binding.shoppingListItemCheckBox.setOnCheckedChangeListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            ShoppingListItemDatabase.getInstance(view.getContext().getApplicationContext()).shoppingListItemDao().deleteShoppingListItems(item);
                            return null;
                        }
                    }.execute();
                }
            });

            CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged (CompoundButton compoundButton,boolean b){
                    if (b) {
                        item.setDone(true);
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {
                                ShoppingListItemDatabase.getInstance(context.getApplicationContext()).shoppingListItemDao().updateShoppingListItems(item);
                                return null;
                            }
                        }.execute();
                    } else {
                        item.setDone(false);
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {
                                ShoppingListItemDatabase.getInstance(context.getApplicationContext()).shoppingListItemDao().updateShoppingListItems(item);
                                return null;
                            }
                        }.execute();
                    }
                }
            }

            if (item.getTranslation() == null) {
                new TranslationTask().execute(item.getName(), "en", "zh");
            } else {
                binding.shoppingListItemTranslationTextView.setText(item.getTranslation());
            }

            binding.executePendingBindings();
        }
    }
}