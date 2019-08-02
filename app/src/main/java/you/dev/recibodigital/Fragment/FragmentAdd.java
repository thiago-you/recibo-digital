package you.dev.recibodigital.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import you.dev.recibodigital.MainActivity;
import you.dev.recibodigital.R;
import you.dev.recibodigital.components.DatePickerBuilder;
import you.dev.recibodigital.model.ReciboVirtual;

public class FragmentAdd extends Fragment {

    private View view;
    private ReciboVirtual reciboVirtual;
    private AppCompatEditText editNome, editValor, editNumParcela, editObservacao, edtData;
    private SignaturePad signaturePad;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_add, container, false);

        initUi();

        return view;
    }

    private void initUi() {
        reciboVirtual = new ReciboVirtual();

        editNome = view.findViewById(R.id.editNome);
        editValor = view.findViewById(R.id.editValor);
        editNumParcela = view.findViewById(R.id.editNumParcela);
        editObservacao = view.findViewById(R.id.editObservacao);
        signaturePad = view.findViewById(R.id.signaturePad);

        edtData = view.findViewById(R.id.edtData);
        FloatingActionButton btnData = view.findViewById(R.id.btnData);
        edtData.setClickable(true);
        edtData.setOnClickListener(showCalendarOnClick(edtData));
        btnData.setOnClickListener(showCalendarOnClick(edtData));

        // set actual data as default value
        try {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            edtData.setText(formatter.format(new Date()));
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }

        Button btnAdd = view.findViewById(R.id.btnAddRecibo);
        btnAdd.setOnClickListener(btnAddOnClickListener);
    }

    private boolean validate() {
        boolean isValid = true;

        if (editNome.getText() == null || editNome.getText().toString().equals("")) {
            isValid = false;
        }
        if (editValor.getText() == null || editValor.getText().toString().equals("") || editValor.getText().toString().equals("0")) {
            isValid = false;
        }
        if (editNumParcela.getText() == null || editNumParcela.getText().toString().equals("") || editNumParcela.getText().toString().equals("0")) {
            isValid = false;
        }

        /* set model data */
        if (editNome.getText() != null) {
            reciboVirtual.setNome(editNome.getText().toString());
        }
        if (editValor.getText() != null) {
            reciboVirtual.setValor(Double.parseDouble(editValor.getText().toString()));
        }
        if (editNumParcela.getText() != null) {
            reciboVirtual.setParcelaNum(Integer.parseInt(editNumParcela.getText().toString()));
        }
        if (edtData.getText() != null && !edtData.getText().toString().equals("")) {
            try {
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                reciboVirtual.setData(formatter.parse(edtData.getText().toString()));
            } catch (ParseException e) {
                Log.e(getClass().getSimpleName(), e.getMessage(), e);
                reciboVirtual.setData(null);
            }
        }
        if (editObservacao.getText() != null) {
            reciboVirtual.setObservacao(editObservacao.getText().toString());
        }
        if (signaturePad.getTransparentSignatureBitmap() != null) {
            reciboVirtual.setAssinatura(signaturePad.getTransparentSignatureBitmap());
        }

        return isValid;
    }

    private final View.OnClickListener btnAddOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (validate()) {
                reciboVirtual = (ReciboVirtual) reciboVirtual.save(getContext());

                if (getActivity() != null) {
                    if (reciboVirtual.getId() > 0) {
                        Toast.makeText(getActivity(), getActivity().getText(R.string.recibo_success), Toast.LENGTH_LONG).show();
                        ((MainActivity) getActivity()).selectItem(MainActivity.FRAGMENT_HOME_ID);
                    } else {
                        /* cria a instância do dialog */
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                        /* configura o dialog de confirmação */
                        builder.setMessage(R.string.recibo_fail).setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                    }
                        });


                        /* cria e exibe o dialog */
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
            }
        }
    };

    private View.OnClickListener showCalendarOnClick(final AppCompatEditText editCalendar) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    /* force hide keyboard */
                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputManager != null) {
                        inputManager.hideSoftInputFromWindow((null == getActivity().getCurrentFocus())
                                ? null
                                : getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS
                        );
                    }

                    /* show calendar */
                    DatePickerBuilder builder = new DatePickerBuilder(getActivity(), editCalendar);
                    builder.show();
                }
            }
        };
    }
}
