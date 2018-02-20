package uk.ac.gla.shopping.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import uk.ac.gla.shopping.databinding.RecyclerItemBinding;
import uk.ac.gla.shopping.entity.ShoppingListItem;

public class ShoppingListActivityRecyclerViewAdapter extends RecyclerView.Adapter<ShoppingListActivityRecyclerViewAdapter.ShoppingListActivityRecyclerViewHolder> {

    private List<ShoppingListItem> shoppingListItems;

    public ShoppingListActivityRecyclerViewAdapter(List<ShoppingListItem> shoppingListItems) {
        this.shoppingListItems = shoppingListItems;
    }

    @Override
    public ShoppingListActivityRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerItemBinding itemBinding = RecyclerItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);

        return new ShoppingListActivityRecyclerViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(ShoppingListActivityRecyclerViewHolder holder, int position) {
        String shoppingListItemName = shoppingListItems.get(position).getName();
        holder.bind(shoppingListItemName);
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

        RecyclerItemBinding binding;

        ShoppingListActivityRecyclerViewHolder(RecyclerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(String blogPostTitle) {
            binding.shoppingListItemTextView.setText(blogPostTitle);
            binding.executePendingBindings();
        }
    }
}