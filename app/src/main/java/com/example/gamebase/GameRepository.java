package com.example.gamebase;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

public class GameRepository {
    private TitleDao mTitleDao;
    private List<GameTitle> gameTitles;

    GameRepository(Application application){
        TitleRoomDatabase db = TitleRoomDatabase.getDatabase(application);
        mTitleDao = db.titleDao();
        gameTitles = mTitleDao.getAllTitles();
    }
    List<GameTitle> getGameTitles(){
        return gameTitles;
    }
    public void insert(GameTitle title){
        new insertAsyncTask(mTitleDao).execute(title);
    }
    private static class insertAsyncTask extends AsyncTask<GameTitle, Void, Void> {

        private TitleDao mAsyncTaskDao;

        insertAsyncTask(TitleDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final GameTitle... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

}
