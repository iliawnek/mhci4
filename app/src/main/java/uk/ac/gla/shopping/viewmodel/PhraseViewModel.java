package uk.ac.gla.shopping.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import uk.ac.gla.shopping.database.PhraseDatabase;
import uk.ac.gla.shopping.entity.Phrase;

public class PhraseViewModel extends AndroidViewModel {
    private final LiveData<List<Phrase>> phrases;

    public PhraseViewModel(@NonNull Application application) {
        super(application);

        phrases = PhraseDatabase
                .getInstance(getApplication())
                .phraseDao()
                .getAllPhrases();
    }

    public LiveData<List<Phrase>> getPhrases() {
        return phrases;
    }
}
