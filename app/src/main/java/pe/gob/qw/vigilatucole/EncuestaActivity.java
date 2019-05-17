package pe.gob.qw.vigilatucole;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;
import pe.gob.qw.vigilatucole.application.App;
import pe.gob.qw.vigilatucole.data.DaoSession;

public class EncuestaActivity extends AppCompatActivity {

    @BindView(R.id.vp_encuesta)
    ViewPager vp_encuesta;

    private DaoSession daoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuesta);
        ButterKnife.bind(this);
        App app = (App) getApplication();
        daoSession = app.getDaoSession();
    }


}
