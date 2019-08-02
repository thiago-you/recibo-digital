package you.dev.recibodigital.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import you.dev.recibodigital.R;
import you.dev.recibodigital.repository.ReciboRepository;

public class FragmentConfig extends Fragment {

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_config, container, false);

        /* inicializa os componentes da tela */
        initUI();

        return view;
    }

    private void initUI() {

        Button btnClean = view.findViewById(R.id.btnClean);
        btnClean.setOnClickListener(btnCleanClickListener);
    }


    private View.OnClickListener btnCleanClickListener =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (getContext() != null) {
                /* cria a instância do dialog */
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                /* configura o dialog de confirmação */
                builder.setMessage(R.string.config_alert)
                        .setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                    new ReciboRepository(getContext()).clearData();
                                    Toast.makeText(getContext(), getContext().getText(R.string.clean_success), Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });


                /* cria e exibe o dialog */
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    };
}
