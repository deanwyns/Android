package hogent.hogentprojecteniii_groep10.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hogent.hogentprojecteniii_groep10.models.Vacation;

/**
 * Zal de databank aanmaken, openen en sluiten.
 * Bewerkingen op de databank zal ook hier gebeuren.
 */
public class VacationDataSource {
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;
    private String[] allColumns = {SQLiteHelper.COLUMN_ID,
            SQLiteHelper.COLUMN_TITLE,
            SQLiteHelper.COLUMN_DESCRIPTION,
            SQLiteHelper.COLUMN_PROMO_TEXT,
            SQLiteHelper.COLUMN_LOCATION,
            SQLiteHelper.COLUMN_BEGIN_DATE,
            SQLiteHelper.COLUMN_END_DATE,
            SQLiteHelper.COLUMN_AGE_FROM,
            SQLiteHelper.COLUMN_AGE_TO,
            SQLiteHelper.COLUMN_TRANSPORTATION,
            SQLiteHelper.COLUMN_MAX_PARTICIPANTS,
            SQLiteHelper.COLUMN_BASE_COST,
            SQLiteHelper.COLUMN_ONE_MEMBER_COST,
            SQLiteHelper.COLUMN_TWO_MEMBER_COST,
            SQLiteHelper.COLUMN_TAX_DEDUCTABLE};


    public VacationDataSource(Context context) {
        dbHelper = new SQLiteHelper(context);
    }

    /**
     * Opent de database
     * @throws SQLException
     */
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Sluit de database
     */
    public void close() {
        dbHelper.close();
    }

    /**
     * Zal een vakantie toevoegen aan de databank
     * @param vacation de vakantie die toegevoegd zal worden
     * @return de vakantie die is toegevoegd
     */
    public Vacation createVacation(Vacation vacation) {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_TITLE, vacation.getTitle());
        values.put(SQLiteHelper.COLUMN_DESCRIPTION, vacation.getDescription());
        values.put(SQLiteHelper.COLUMN_PROMO_TEXT, vacation.getPromoText());
        values.put(SQLiteHelper.COLUMN_LOCATION, vacation.getLocation());
        values.put(SQLiteHelper.COLUMN_BEGIN_DATE, df.format(vacation.getBeginDate()));
        values.put(SQLiteHelper.COLUMN_END_DATE, df.format(vacation.getEndDate()));
        values.put(SQLiteHelper.COLUMN_AGE_FROM, vacation.getAgeFrom());
        values.put(SQLiteHelper.COLUMN_AGE_TO, vacation.getAgeTo());
        values.put(SQLiteHelper.COLUMN_TRANSPORTATION, vacation.getTransportation());
        values.put(SQLiteHelper.COLUMN_MAX_PARTICIPANTS, vacation.getMaxParticipants());
        values.put(SQLiteHelper.COLUMN_BASE_COST, vacation.getBaseCost());
        values.put(SQLiteHelper.COLUMN_ONE_MEMBER_COST, vacation.getOneBmMemberCost());
        values.put(SQLiteHelper.COLUMN_TWO_MEMBER_COST, vacation.getTwoBmMemberCost());
        values.put(SQLiteHelper.COLUMN_TAX_DEDUCTABLE, vacation.isTaxDeductable());

        long insertId = database.insert(SQLiteHelper.TABLE_VACATIONS, null,
                values);
        Cursor cursor = database.query(SQLiteHelper.TABLE_VACATIONS,
                allColumns, SQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Vacation newVacation = cursorToVacation(cursor);
        cursor.close();
        return newVacation;
    }

    /**
     * Verwijdert een vakantie uit de databank
     * @param vacation de vakantie die zal worden verwijderd
     */
    public void deleteVacation(Vacation vacation) {
        long id = vacation.getId();
        System.out.println("Vacation deleted with id: " + id);
        database.delete(SQLiteHelper.TABLE_VACATIONS, SQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    /**
     * Geeft alle vakanties uit de databank terug
     * @return alle vakanties uit de databank
     */
    public List<Vacation> getAllVacations() {
        List<Vacation> vacations = new ArrayList<Vacation>();

        Cursor cursor = database.query(SQLiteHelper.TABLE_VACATIONS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Vacation vacation = cursorToVacation(cursor);
            vacations.add(vacation);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return vacations;
    }

    /**
     * Hulpmethode om een vakantie uit een vursor te halen.
     * @param cursor de cursor waar de vakantie wordt opgehaald
     * @return de opgehaalde vakantie
     */
    private Vacation cursorToVacation(Cursor cursor) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        long id = cursor.getLong(0);
        String title = cursor.getString(1);
        String description = cursor.getString(2);
        String promoText = cursor.getString(3);
        String location = cursor.getString(4);
        Date beginDate = null;
        Date endDate = null;
        try {
            beginDate = simpleDateFormat.parse(cursor.getString(5));
            endDate = simpleDateFormat.parse(cursor.getString(6));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int ageFrom = cursor.getInt(7);
        int ageTo = cursor.getInt(8);
        String transportation = cursor.getString(9);
        int maxParticipants = cursor.getInt(10);
        double baseCost = cursor.getDouble(11);
        double oneBmMemberCost = cursor.getDouble(12);
        double twoBmMemberCost = cursor.getDouble(13);
        int taxDeductable = cursor.getInt(14);

        Vacation vacation =
                new Vacation(id, title, description, promoText, location, beginDate, endDate,
                        ageFrom, ageTo, transportation, maxParticipants, baseCost, oneBmMemberCost,
                        twoBmMemberCost, taxDeductable);

        return vacation;
    }
}
