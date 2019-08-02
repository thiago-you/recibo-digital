package you.dev.recibodigital.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import you.dev.recibodigital.R;
import you.dev.recibodigital.model.ReciboVirtual;
import you.dev.recibodigital.repository.ReciboRepository;

@SuppressWarnings("WeakerAccess unused")
public class ReciboAdapter extends RecyclerView.Adapter<ReciboAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<ReciboVirtual> list;
    private double valorTotal;

    public ReciboAdapter(Activity activity, ArrayList<ReciboVirtual> list, double valorTotal) {
        this.activity = activity;
        this.list = list;
        this.valorTotal = valorTotal;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recibo_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final ReciboVirtual recibo = list.get(position);

        holder.view.setOnLongClickListener(deleteItemLongClick(recibo, activity, position));

        // seta os dados do recibo
        holder.txtNome.setText(activity.getString(R.string.fmt_recibo_nome, recibo.getNome()));
        holder.txtNumParcela.setText(activity.getString(R.string.fmt_recibo_parcela, String.valueOf(recibo.getParcelaNum())));
        holder.txtValor.setText(activity.getString(R.string.fmt_recibo_valor, recibo.getValor()));
        holder.txtValorTotal.setText(activity.getString(R.string.fmt_recibo_valor_total, valorTotal));
        holder.txtObservacao.setText(activity.getString(R.string.fmt_recibo_observacao, recibo.getObservacao()));

        try {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            holder.txtData.setText(activity.getString(R.string.fmt_recibo_data, formatter.format(recibo.getData())));
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), e.getMessage(), e);
        }

        // valida e seta a assinatura do responsável
        if (recibo.getAssinatura() != null) {
            holder.signatureImg.setImageBitmap(recibo.getAssinatura());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    public void remove(int position) {
        this.list.remove(position);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private ImageView signatureImg;
        private TextView txtNome, txtNumParcela, txtValor, txtValorTotal, txtObservacao, txtData;

        public ViewHolder(View view) {
            super(view);

            this.view = view;
            txtNome = view.findViewById(R.id.txtNome);
            txtNumParcela = view.findViewById(R.id.txtNumParcela);
            txtValor = view.findViewById(R.id.txtValor);
            txtValorTotal = view.findViewById(R.id.txtValorTotal);
            txtObservacao = view.findViewById(R.id.txtObservacao);
            txtData = view.findViewById(R.id.txtData);
            signatureImg = view.findViewById(R.id.signaturePad);
        }

    }

    private View.OnLongClickListener deleteItemLongClick(final ReciboVirtual recibo, final Activity activity, final int itemPosition) {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(android.view.View v) {
                /* cria a instância do dialog */
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                /* configura o dialog de confirmação */
                builder.setMessage(R.string.delete_alert)
                        .setPositiveButton(R.string.btn_confirm, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                /* delete item */
                                new ReciboRepository(activity).delete(recibo);

                                /* remove item and update adapter */
                                remove(itemPosition);
                                notifyDataSetChanged();

                                /* success msg */
                                Toast.makeText(activity, activity.getText(R.string.delete_success), Toast.LENGTH_LONG).show();
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

                return true;
            }
        };
    }
}
