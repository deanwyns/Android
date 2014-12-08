package hogent.hogentprojecteniii_groep10.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Laat toe om vakanties op te slaan en te lezen indien er geen internet aanwezig is.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_VACATIONS = "vacations";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PROMO_TEXT = "promoText";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_BEGIN_DATE = "beginDate";
    public static final String COLUMN_END_DATE = "endDate";
    public static final String COLUMN_AGE_FROM = "ageFrom";
    public static final String COLUMN_AGE_TO = "ageTo";
    public static final String COLUMN_TRANSPORTATION = "transportation";
    public static final String COLUMN_MAX_PARTICIPANTS = "maxParticipants";
    public static final String COLUMN_BASE_COST = "baseCost";
    public static final String COLUMN_ONE_MEMBER_COST = "oneBmMemberCost";
    public static final String COLUMN_TWO_MEMBER_COST = "twoBmMemberCost";
    public static final String COLUMN_TAX_DEDUCTABLE = "taxDeductable";

    private static final String DATABASE_NAME = "vacations.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String TABLE_CREATE = "create table "
            + TABLE_VACATIONS + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TITLE + " text not null,"
            + COLUMN_DESCRIPTION + " text not null,"
            + COLUMN_PROMO_TEXT + " text,"
            + COLUMN_LOCATION + " text not null,"
            + COLUMN_BEGIN_DATE + " text not null,"
            + COLUMN_END_DATE + " text not null,"
            + COLUMN_AGE_FROM + " integer not null,"
            + COLUMN_AGE_TO + " integer not null,"
            + COLUMN_TRANSPORTATION + " text not null,"
            + COLUMN_MAX_PARTICIPANTS + " integer not null,"
            + COLUMN_BASE_COST + " real not null,"
            + COLUMN_ONE_MEMBER_COST + " real not null,"
            + COLUMN_TWO_MEMBER_COST + " real not null,"
            + COLUMN_TAX_DEDUCTABLE + " integer not null"
            + ");";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Zal de tabel aanmaken met behulp van het TABLE_CREATE statement
     * @param db de databank waarop het statement zal uitgevoerd worden.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    /**
     * Indien de databank geüpgrade wordt, zal de oude versie verwijderd worden en een nieuwe gemaakt worden.
     * @param db de databank waarop het statement zal uitgevoerd worden.
     * @param oldVersion de oude versie van de databank
     * @param newVersion de nieuwe versie van de databank
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(SQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VACATIONS);
        onCreate(db);
    }
}
