package you.dev.recibodigital.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import you.dev.recibodigital.base.IModel;
import you.dev.recibodigital.base.IRepository;
import you.dev.recibodigital.model.ReciboVirtual;

@SuppressWarnings("unused WeakerAccess")
public class ReciboRepository extends SQLiteOpenHelper implements IRepository {

    public static final String REPOSITORY_NAME = "recibos_virtual";
    public static final int REPOSITORY_VERSION = 2;

    public static final String TABLE = "recibos";

    public ReciboRepository(Context context) {
        super(context, ReciboRepository.REPOSITORY_NAME, null, ReciboRepository.REPOSITORY_VERSION);
    }

    public void clearData() {
        SQLiteDatabase db = getWritableDatabase();
        /* delete all records */
        db.execSQL(ReciboRepository.dropTable());
        /* recreate tab le */
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(ReciboRepository.createTable());
        } catch (SQLException e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ReciboRepository.dropTable());
        onCreate(db);
    }

    public static String createTable() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE + " (" + getColumns() + ")";
    }

    private static String getColumns() {
        String columns = "";

        columns += " id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,";
        columns += " nome TEXT NOT NULL,";
        columns += " observacao TEXT,";
        columns += " valor REAL,";
        columns += " parcelaNum INTEGER DEFAULT 1,";
        columns += " data TEXT,";
        columns += " assinatura BLOB";

        return columns;
    }

    public static String dropTable() {
        return "DROP TABLE IF EXISTS " + TABLE;
    }

    public static String truncateTable() {
        return "DELETE FROM " + TABLE;
    }

    @Override
    public boolean exists(int id) throws SQLException {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE, null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);

        int exist = cursor.getCount();

        cursor.close();
        db.close();

        return exist > 0;
    }

    @Override
    public ReciboVirtual save(IModel entity) throws SQLException {
        ReciboVirtual recibo = (ReciboVirtual) entity;

        if (exists(recibo.getId())) {
            return update(entity);
        }

        return create(entity);
    }

    @Override
    public ReciboVirtual create(IModel entity) throws SQLException {
        SQLiteDatabase db = getWritableDatabase();

        long id = db.insert(TABLE, null, entity.toContentValues());
        db.close();

        /* set primary key */
        entity.setPrimaryKey((int) id);

        return (ReciboVirtual) entity;
    }

    @Override
    public ReciboVirtual update(IModel entity) throws SQLException {
        ReciboVirtual recibo = (ReciboVirtual) entity;
        SQLiteDatabase db = getWritableDatabase();

        int rows = db.update(TABLE, entity.toContentValues(), "id = " + recibo.getId(), null);
        db.close();

        if (rows <= 0) {
            throw new SQLException("Não foi possível alterar o Recibo. Por favor, tente novamente mais tarde.");
        }

        return recibo;
    }

    @Override
    public void delete(IModel entity) throws SQLException {
        ReciboVirtual recibo = (ReciboVirtual) entity;
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE, "id = " + recibo.getId(), null);
        db.close();
    }

    @Override
    public ReciboVirtual find(int id) throws SQLException {
        SQLiteDatabase db = getReadableDatabase();
        ReciboVirtual recibo = null;

        Cursor cursor = db.query(TABLE, null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor.getCount() > 0 && cursor.moveToNext()) {
            recibo = new ReciboVirtual(cursor);
        }

        cursor.close();
        db.close();

        return recibo;
    }

    public ArrayList<ReciboVirtual> all() throws SQLException {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<ReciboVirtual> recibos = new ArrayList<>();

        String orderBy = "id desc";

        Cursor cursor = db.query(TABLE, null, null, null, null, null, orderBy);

        while (cursor.moveToNext()) {
            ReciboVirtual recibo = new ReciboVirtual(cursor);
            recibos.add(recibo);
        }

        cursor.close();
        db.close();

        return recibos;
    }
}
