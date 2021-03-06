package com.ufrpe.feelingsbox.infra.adapter.tabs;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ufrpe.feelingsbox.R;
import com.ufrpe.feelingsbox.infra.adapter.post.PostFragment;
import com.ufrpe.feelingsbox.redesocial.dominio.Sessao;

import static com.ufrpe.feelingsbox.redesocial.dominio.BundleEnum.TAB;

public class TabsAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private String[] titles;

    public TabsAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
        titles = new String[]{mContext.getString(R.string.title_tab_home),
                              mContext.getString(R.string.title_tab_favoritos),
                              mContext.getString(R.string.title_tab_recomendados)};
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new PostFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TAB.getValor(), position);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return (titles[position]);
    }


}
