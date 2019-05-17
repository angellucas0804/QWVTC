package pe.gob.qw.vigilatucole;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pe.gob.qw.vigilatucole.adapter.PerfilAdapter;
import pe.gob.qw.vigilatucole.application.App;
import pe.gob.qw.vigilatucole.application.BaseActivity;
import pe.gob.qw.vigilatucole.data.DaoSession;
import pe.gob.qw.vigilatucole.util.ItemAnimation;

public class PerfilActivity extends BaseActivity {

    @BindView(R.id.rv_perfil)
    RecyclerView rv_perfil;

    private DaoSession daoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        ButterKnife.bind(this);
        App app = (App) getApplication();
        daoSession = app.getDaoSession();
        verPerfilList();
    }

    @OnClick(R.id.fab_agregar_perfil)
    public void agregar_perfil() {
        Intent intent = new Intent(PerfilActivity.this,AgregarEditarPerfilActivity.class);
        startActivity(intent);
    }

    private void verPerfilList(){
        PerfilAdapter adapter = new PerfilAdapter(this, daoSession.getAlumnoDao().loadAll(), ItemAnimation.BOTTOM_UP);
        rv_perfil.setHasFixedSize(true);
        rv_perfil.setLayoutManager(new LinearLayoutManager(this));
        rv_perfil.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        verPerfilList();
    }
}
