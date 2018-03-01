package uk.ac.gla.shopping.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import uk.ac.gla.shopping.dao.PhraseDAO;
import uk.ac.gla.shopping.entity.Phrase;

@Database(entities = {Phrase.class}, version = 1, exportSchema = false)
public abstract class PhraseDatabase extends RoomDatabase {
    private static PhraseDatabase INSTANCE;

    public static PhraseDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                    context.getApplicationContext(),
                    PhraseDatabase.class,
                    "PhraseDatabase")
                    .build();
        }

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public abstract PhraseDAO phraseDao();
}
