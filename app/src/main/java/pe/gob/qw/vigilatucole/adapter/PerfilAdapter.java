package pe.gob.qw.vigilatucole.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import pe.gob.qw.vigilatucole.AgregarEditarPerfilActivity;
import pe.gob.qw.vigilatucole.EncuestaActivity;
import pe.gob.qw.vigilatucole.R;
import pe.gob.qw.vigilatucole.data.Alumno;
import pe.gob.qw.vigilatucole.model.Encuesta;
import pe.gob.qw.vigilatucole.util.ItemAnimation;

public class PerfilAdapter extends RecyclerView.Adapter<PerfilAdapter.ViewHolder> {

    private Encuestalistener encuestalistener;
    private Context context;
    private List<Alumno> alumnos;
    private int animation;
    private LayoutInflater inflater;

    public PerfilAdapter(Context context, List<Alumno> alumnos, int animation) {
        this.context = context;
        this.alumnos = alumnos;
        this.animation = animation;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(inflater.inflate(R.layout.item_alumno, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Alumno alumno = alumnos.get(i);
        viewHolder.tv_nombres.setText(alumno.getNvNombres());
        viewHolder.tv_apellidos.setText(alumno.getNvApePatMat());
        viewHolder.tv_colegio.setText(alumno.getNvColegio());
        viewHolder.tv_total_puntos.setText(alumno.getLngPuntaje()+" Puntos");
        viewHolder.cv_alumno.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(context, AgregarEditarPerfilActivity.class)
                        .putExtra("KEY_EDITAR_PERFIL",String.valueOf(alumno.getId()));
                context.startActivity(intent);
                return false;
            }
        });
        viewHolder.cv_alumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (encuestalistener != null){
                    encuestalistener.setItme(alumno);
                }



            }
        });
        setAnimation(viewHolder.itemView, i);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                on_attach = false;
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return alumnos.size();
    }

    private int lastPosition = -1;
    private boolean on_attach = true;

    private void setAnimation(View view, int position) {
        if (position > lastPosition) {
            ItemAnimation.animate(view, on_attach ? position : -1, animation);
            lastPosition = position;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_nombres)
        TextView tv_nombres;
        @BindView(R.id.tv_apellidos)
        TextView tv_apellidos;
        @BindView(R.id.tv_colegio)
        TextView tv_colegio;
        @BindView(R.id.tv_total_puntos)
        TextView tv_total_puntos;
        @BindView(R.id.cv_alumno)
        CardView cv_alumno;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setlistener(Encuestalistener encuestalistener){
        this.encuestalistener = encuestalistener;
    }

    public interface Encuestalistener{

        void setItme(Alumno alumno);
    }


}
