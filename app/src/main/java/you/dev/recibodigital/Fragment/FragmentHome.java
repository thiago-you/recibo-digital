package you.dev.recibodigital.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import you.dev.recibodigital.R;
import you.dev.recibodigital.adapter.ReciboAdapter;
import you.dev.recibodigital.model.ReciboVirtual;
import you.dev.recibodigital.repository.ReciboRepository;

public class FragmentHome extends Fragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        /* set fragment view */
        view = inflater.inflate(R.layout.fragment_home, container, false);

        /* init UI components */
        initUI();

        return view;
    }

    private void initUI() {
        /* busca os recibos */
        ArrayList<ReciboVirtual> recibos = new ReciboRepository(getContext()).all();

        double valorTotal = 0.0;
        /* calcula o valor total */
        for (ReciboVirtual recibo : recibos) {
            valorTotal += recibo.getValor();
        }

        /* init adapter */
        ReciboAdapter adapter = new ReciboAdapter(getActivity(), recibos, valorTotal);

        /* set and init view list */
        RecyclerView recyclerView = view.findViewById(R.id.listaRecibos);
        recyclerView.setAdapter(adapter);
    }
}
