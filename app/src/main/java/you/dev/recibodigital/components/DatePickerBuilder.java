package you.dev.recibodigital.components;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Locale;

import you.dev.recibodigital.R;

@SuppressWarnings("unused")
public class DatePickerBuilder {

    private Calendar calendar;
    private DatePickerDialog dialog;

    /**
     * Configura um date picker padrão para exibição
     */
    public DatePickerBuilder(Context context, DatePickerDialog.OnDateSetListener dateSetListener) {
        calendar = Calendar.getInstance();

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        this.dialog = new DatePickerDialog(context, dateSetListener, year, month, day);
    }

    /**
     * Config date picker
     */
    public DatePickerBuilder(Context context, final EditText mDatePicker) {
        calendar = Calendar.getInstance();

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        this.dialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mDatePicker.setText(formatDate(year, month, dayOfMonth));
            }
        }, year, month, day);

        // set new button (title + click listener)
        this.dialog.setButton(this.dialog.BUTTON_NEUTRAL, context.getText(R.string.btn_clear_date), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDatePicker.clearFocus();
                mDatePicker.setText("");
            }
        });
    }

    /**
     * Show date picker
     */
    public void show() {
        if (dialog != null) {
            dialog.show();
        }
    }

    private String formatDate(int year, int month, int day) {
        String date = day + "/" + (month + 1) + "/" + year;

        try {
            date = String.format(Locale.getDefault(), "%02d", day) + "/" + String.format(Locale.getDefault(), "%02d", (month + 1)) + "/" + year;
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }

        return date;
    }
}
