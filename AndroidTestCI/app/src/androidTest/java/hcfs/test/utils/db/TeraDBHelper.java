package hcfs.test.utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import java.io.File;

import hcfs.test.utils.MgmtAppUtils;

public class TeraDBHelper extends SQLiteOpenHelper {

    private static final String THIS_CLASS = TeraDBHelper.class.getSimpleName();

    // RD defined constant
    public static final String DATABASE_NAME = "uid.db";
    public static final String DB_DIR = "/data/data/" + MgmtAppUtils.PACKAGE + "/databases/";
    public static final int VERSION = 2;

    private static TeraDBHelper teraDBHelper;

    private TeraDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_DIR + name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // we should not be here !
        throw new RuntimeException("Unable to find Tera DB.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //
    }

    public static SQLiteDatabase getDataBase(Context context) {
        if (teraDBHelper == null)
            teraDBHelper = new TeraDBHelper(context, DATABASE_NAME, null, VERSION);
        if(!new File(DB_DIR + DATABASE_NAME).exists())
            throw new RuntimeException("Unable to find DB file.");
        return teraDBHelper.getWritableDatabase();
    }
}
