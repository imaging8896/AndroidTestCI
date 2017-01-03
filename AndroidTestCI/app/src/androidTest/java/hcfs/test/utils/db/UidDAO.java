package hcfs.test.utils.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hcfs.test.utils.db.info.UidInfoAdapter;

public class UidDAO {

    private final String CLASSNAME = getClass().getSimpleName();

    public static final String TABLE_NAME = "uid";
    public static final String KEY_ID = "_id";
    public static final String PIN_STATUS_COLUMN = "pin_status";
    public static final String SYSTEM_APP_COLUMN = "system_app";
    public static final String ENABLED_COLUMN = "enabled";
    public static final String UID_COLUMN = "uid";
    public static final String PACKAGE_NAME_COLUMN = "package_name";
    public static final String EXTERNAL_DIR_COLUMN = "external_dir";
    public static final String BOOST_STATUS_COLUMN = "boost_status";
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PIN_STATUS_COLUMN + " INTEGER NOT NULL, " +
                    SYSTEM_APP_COLUMN + " INTEGER NOT NULL, " +
                    ENABLED_COLUMN + " INTEGER NOT NULL, " +
                    BOOST_STATUS_COLUMN + " INTEGER NOT NULL, " +
                    UID_COLUMN + " TEXT NOT NULL, " +
                    PACKAGE_NAME_COLUMN + " TEXT NOT NULL, " +
                    EXTERNAL_DIR_COLUMN + " TEXT)";

    private Context context;

    public UidDAO(Context context) {
        this.context = context;
    }

    public boolean update(String packageName, ContentValues cv) {
        try(SQLiteDatabase db = TeraDBHelper.getDataBase(context)) {
            String where = PACKAGE_NAME_COLUMN + "='" + packageName + "'";
            boolean isSuccess = db.update(TABLE_NAME, cv, where, null) > 0;
            if (isSuccess)
                Logs.d(CLASSNAME, "update", "packageName=" + packageName + ", cv: " + cv.toString());
            else
                Logs.e(CLASSNAME, "update", "packageName=" + packageName + ", cv: " + cv.toString());
            return isSuccess;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<UidInfoAdapter> getAll() {
        try(SQLiteDatabase db = TeraDBHelper.getDataBase(context)) {
            List<UidInfoAdapter> result = new ArrayList<>();
            Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null, null);
            while (cursor.moveToNext())
                result.add(getRecord(cursor));
            cursor.close();
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public UidInfoAdapter get(String packageName) {
        try(SQLiteDatabase db = TeraDBHelper.getDataBase(context)) {
            UidInfoAdapter uidInfo = null;
            String where = PACKAGE_NAME_COLUMN + "='" + packageName + "'";
            Cursor cursor = db.query(TABLE_NAME, null, where, null, null, null, null, null);
            if (cursor.moveToFirst())
                uidInfo = getRecord(cursor);
            cursor.close();
            return uidInfo;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private UidInfoAdapter getRecord(Cursor cursor) {
        UidInfoAdapter result = new UidInfoAdapter();
        result.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
        result.setPinned(cursor.getInt(cursor.getColumnIndex(PIN_STATUS_COLUMN)) != 0); // 0 unpin, 1 normal pin, 2 priority pin
        result.setSystemApp(cursor.getInt(cursor.getColumnIndex(SYSTEM_APP_COLUMN)) == 1);
        result.setEnabled(cursor.getInt(cursor.getColumnIndex(ENABLED_COLUMN)) == 1);
        result.setBoostStatus(cursor.getInt(cursor.getColumnIndex(BOOST_STATUS_COLUMN)));
        result.setUid(cursor.getInt(cursor.getColumnIndex(UID_COLUMN)));
        result.setPackageName(cursor.getString(cursor.getColumnIndex(PACKAGE_NAME_COLUMN)));

        String externalDir = cursor.getString(cursor.getColumnIndex(EXTERNAL_DIR_COLUMN));
        if (externalDir != null && !externalDir.isEmpty()) {
            String[] list = externalDir.split(",");
            result.setExternalDir(Arrays.asList(list));
        }
        Logs.e(CLASSNAME, "getRecord", "UidInfo=" + result);
        return result;
    }

    public boolean delete(String pkg) {
        try(SQLiteDatabase db = TeraDBHelper.getDataBase(context)) {
            String where = PACKAGE_NAME_COLUMN + "='" + pkg + "'";
            boolean isDeleted = db.delete(TABLE_NAME, where, null) > 0;
            if (isDeleted)
                Logs.d(CLASSNAME, "delete", "packageName=" + pkg);
            else
                Logs.e(CLASSNAME, "delete", "packageName=" + pkg);
            return isDeleted;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
