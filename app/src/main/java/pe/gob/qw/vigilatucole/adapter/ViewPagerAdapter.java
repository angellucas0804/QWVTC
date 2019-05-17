package pe.gob.qw.vigilatucole.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Objects;

import pe.gob.qw.vigilatucole.R;

public class ViewPagerAdapter extends PagerAdapter {

    Context context;
    private ArrayList<Integer> integers;

    public ViewPagerAdapter(Context context, ArrayList<Integer> integers) {
        this.context = context;
        this.integers = integers;
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = Objects.requireNonNull(layoutInflater).inflate(R.layout.item_encuesta,container,false);
        return view;
    }

    @Override
    public int getCount() {
        return integers.size();
    }

}
