package pe.gob.qw.vigilatucole.application;

import android.app.Application;

import org.greenrobot.greendao.database.Database;

import pe.gob.qw.vigilatucole.data.DaoMaster;
import pe.gob.qw.vigilatucole.data.DaoSession;


public class App extends Application {

    public static final boolean ENCRYPTED = true;
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "qwvtc-e" : "qwvtc");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("password") : helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
