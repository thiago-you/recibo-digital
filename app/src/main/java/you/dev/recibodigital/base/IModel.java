package you.dev.recibodigital.base;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

public interface IModel {

    /**
     * Set primary key
     */
    void setPrimaryKey(int id);

    /**
     * Save model
     */
    IModel save(Context context) throws SQLException;

    /**
     * Update model
     */
    IModel update(Context context) throws SQLException;

    /**
     * Delete model
     */
    void delete(Context context) throws SQLException;

    /**
     * Create model
     */
    IModel create(Context context) throws SQLException;

    /**
     * Parse model into JSON
     */
    JSONObject toJson() throws JSONException;

    /**
     * Parse model into content for SQLite
     */
    ContentValues toContentValues();
}
