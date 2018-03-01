package uk.ac.gla.shopping.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import uk.ac.gla.shopping.entity.Phrase;

@Dao
public interface PhraseDAO {
    @Query("SELECT * FROM Phrase")
    LiveData<List<Phrase>> getAllPhrases();

    @Query("SELECT * FROM Phrase WHERE id = :id LIMIT 1")
    LiveData<List<Phrase>> findPhraseById(long id);

    @Query("SELECT * FROM Phrase WHERE name LIKE :name LIMIT 1")
    LiveData<List<Phrase>> findPhraseByName(String name);

    @Query("SELECT COUNT(*) FROM Phrase")
    int rowCount();

    @Insert
    void insertPhrases(Phrase... Phrases);

    @Update
    void updatePhrases(Phrase... Phrases);

    @Delete
    void deletePhrases(Phrase... Phrases);
}