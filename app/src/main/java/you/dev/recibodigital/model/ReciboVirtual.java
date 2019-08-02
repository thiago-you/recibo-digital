package you.dev.recibodigital.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import you.dev.recibodigital.base.IModel;
import you.dev.recibodigital.repository.ReciboRepository;

@SuppressWarnings("unused")
public class ReciboVirtual implements IModel, Parcelable {

    private int id;
    private String nome;
    private String observacao;
    private double valor;
    private int parcelaNum;
    private Date data;
    private Bitmap assinatura;

    public ReciboVirtual() {

    }

    public ReciboVirtual(Cursor cursor) {
        id = cursor.getInt(cursor.getColumnIndex("id"));
        nome = cursor.getString(cursor.getColumnIndex("nome"));
        observacao = cursor.getString(cursor.getColumnIndex("observacao"));
        valor = cursor.getDouble(cursor.getColumnIndex("valor"));
        parcelaNum = cursor.getInt(cursor.getColumnIndex("parcelaNum"));

        // formata a data
        data = ReciboVirtual.stringToDate(cursor.getString(cursor.getColumnIndex("data")));

        try {
            byte[] foto = cursor.getBlob(cursor.getColumnIndex("assinatura"));
            if (foto != null) {
                assinatura = BitmapFactory.decodeByteArray(foto, 0, foto.length);
            }
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Houve um erro ao ler a assinatura do recibo. Por favor, tente novamente mais tarde.", e);
        }
    }

    private ReciboVirtual(Parcel in) {
        id = in.readInt();
        nome = in.readString();
        observacao = in.readString();
        valor = in.readDouble();
        parcelaNum = in.readInt();

        // converte e seta a data
        long tmpData = in.readLong();
        data = tmpData == -1 ? null : new Date(tmpData);

        // seta a assinatura (bitmap/img)
        assinatura = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<ReciboVirtual> CREATOR = new Creator<ReciboVirtual>() {
        @Override
        public ReciboVirtual createFromParcel(Parcel in) {
            return new ReciboVirtual(in);
        }

        @Override
        public ReciboVirtual[] newArray(int size) {
            return new ReciboVirtual[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getParcelaNum() {
        return parcelaNum;
    }

    public void setParcelaNum(int parcelaNum) {
        this.parcelaNum = parcelaNum;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Bitmap getAssinatura() {
        return assinatura;
    }

    public void setAssinatura(Bitmap assinatura) {
        this.assinatura = assinatura;
    }

    @Override
    public void setPrimaryKey(int id) {
        this.id = id;
    }

    @Override
    public IModel save(Context context) throws SQLException {
        return new ReciboRepository(context).save(this);
    }

    @Override
    public IModel update(Context context) throws SQLException {
        return new ReciboRepository(context).update(this);
    }

    @Override
    public void delete(Context context) throws SQLException {
        new ReciboRepository(context).delete(this);
    }

    @Override
    public IModel create(Context context) throws SQLException {
        return new ReciboRepository(context).create(this);
    }

    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();

        json.put("id", this.id);
        json.put("nome", this.nome);
        json.put("observacao", this.observacao);
        json.put("parcelaNum", this.parcelaNum);

        if (this.data != null) {
            json.put("data", dateToString(this.data, "yyyy-MM-dd HH:mm:ss"));
        }

        if (this.assinatura != null) {
            json.put("assinatura", this.assinatura);
        }

        return json;
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues content = new ContentValues();
        ByteArrayOutputStream streamAssinatura = new ByteArrayOutputStream();

        if (this.id > 0) {
            content.put("id", this.id);
        }

        content.put("nome", this.nome);
        content.put("observacao", this.observacao);
        content.put("valor", this.valor);
        content.put("parcelaNum", this.parcelaNum);

        if (this.data != null) {
            content.put("data", dateToString(this.data, "dd/MM/yyyy HH:mm:ss"));
        } else {
            content.put("data", "");
        }

        if (this.assinatura != null) {
            this.assinatura.compress(Bitmap.CompressFormat.PNG, 100, streamAssinatura);
            content.put("assinatura", streamAssinatura.toByteArray());
        } else {
            content.put("assinatura", "");
        }

        return content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nome);
        dest.writeString(observacao);
        dest.writeDouble(valor);
        dest.writeInt(parcelaNum);
        dest.writeLong(data != null ? data.getTime() : -1);
        dest.writeParcelable(assinatura, flags);
    }

    private static Date stringToDate(String value) {
        String logMsg = "";
        Date date = null;

        //  lista de formatos
        String[] formatList = new String[] {
                "dd/MM/yyyy",
                "yyyy-MM-dd",
                "dd/MM/yyyy HH:mm:ss",
                "dd/MM/yyyy HH:mm:ss",
        };

        if (value != null && !value.equals("")) {
            for (String format : formatList) {
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
                    date = formatter.parse(value);

                    // clear log MSG
                    logMsg = "";
                    break;
                } catch (Exception e) {
                    date = null;
                    logMsg = e.getMessage();
                }
            }

            // set log
            if (!logMsg.equals("")) {
                Log.e(ReciboVirtual.class.getSimpleName(), logMsg);
            }
        }

        return date;
    }

    private static String dateToString(Date value, String pattern) {
        String date = "";

        if (value != null) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.getDefault());
                date = formatter.format(value);
            } catch (Exception e) {
                date = null;
                Log.e(ReciboVirtual.class.getSimpleName(), e.getMessage());
            }
        }

        return date;
    }
}
