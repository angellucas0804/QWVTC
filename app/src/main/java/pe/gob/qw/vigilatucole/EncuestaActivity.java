package pe.gob.qw.vigilatucole;

import android.graphics.PorterDuff;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import pe.gob.qw.vigilatucole.adapter.ViewPagerAdapter;
import pe.gob.qw.vigilatucole.application.App;
import pe.gob.qw.vigilatucole.data.DaoSession;

public class EncuestaActivity extends AppCompatActivity {

    @BindView(R.id.vp_encuesta)
    ViewPager vp_encuesta;

    @BindView(R.id.ll_dots)
    LinearLayout ll_dots;

    private DaoSession daoSession;

    private static final int MAX_PAGE = 12;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuesta);
        ButterKnife.bind(this);
        App app = (App) getApplication();
        daoSession = app.getDaoSession();
        bottomProgressDots(0);
        viewPagerAdapter = new ViewPagerAdapter();
        vp_encuesta.setAdapter(viewPagerAdapter);
    }

    private void bottomProgressDots(int current_index) {
        ImageView[] dots = new ImageView[MAX_PAGE];

        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle);
            dots[i].setColorFilter(getResources().getColor(R.color.grey_20), PorterDuff.Mode.SRC_IN);
            ll_dots.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current_index].setImageResource(R.drawable.shape_circle);
            dots[current_index].setColorFilter(getResources().getColor(R.color.orange_400), PorterDuff.Mode.SRC_IN);
        }
    }


}
