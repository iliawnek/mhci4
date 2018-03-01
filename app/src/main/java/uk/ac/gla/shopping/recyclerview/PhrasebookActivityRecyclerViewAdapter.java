package uk.ac.gla.shopping.recyclerview;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.List;

import uk.ac.gla.shopping.activity.MainActivity;
import uk.ac.gla.shopping.database.PhraseDatabase;
import uk.ac.gla.shopping.databinding.PhrasebookRecyclerItemBinding;
import uk.ac.gla.shopping.entity.Phrase;

public class PhrasebookActivityRecyclerViewAdapter extends RecyclerView.Adapter<PhrasebookActivityRecyclerViewAdapter.PhrasebookActivityRecyclerViewHolder> {

    private final Context context;
    private List<Phrase> phrases;

    public PhrasebookActivityRecyclerViewAdapter(List<Phrase> phrases, Context context) {
        this.phrases = phrases;
        this.context = context;
    }

    @Override
    public PhrasebookActivityRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PhrasebookRecyclerItemBinding itemBinding = PhrasebookRecyclerItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);

        return new PhrasebookActivityRecyclerViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(PhrasebookActivityRecyclerViewHolder holder, int position) {
        Phrase phrase = phrases.get(position);
        holder.bind(phrase, this.context);
    }

    @Override
    public int getItemCount() {
        return phrases.size();
    }

    public void setPhrases(List<Phrase> phrases) {
        this.phrases = phrases;
        notifyDataSetChanged();
    }

    static class PhrasebookActivityRecyclerViewHolder extends RecyclerView.ViewHolder {

        final PhrasebookRecyclerItemBinding binding;

        PhrasebookActivityRecyclerViewHolder(PhrasebookRecyclerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Phrase item, Context context) {
            binding.phraseNameTextView.setText(item.getName());

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
                    PhraseDatabase.getInstance(context).phraseDao().updatePhrases(item);

                    return translation.getTranslatedText();
                }

                @Override
                protected void onPostExecute(String result) {
                    binding.phraseTranslationTextView.setText(result);
                }
            }

            if (item.getTranslation() == null) {
                new TranslationTask().execute(item.getName(), "en", "zh");
            } else {
                binding.phraseTranslationTextView.setText(item.getTranslation());
            }

            binding.executePendingBindings();
        }
    }
}