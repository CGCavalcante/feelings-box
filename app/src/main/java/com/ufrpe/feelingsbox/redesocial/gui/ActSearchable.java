package com.ufrpe.feelingsbox.redesocial.gui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.SearchRecentSuggestions;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ufrpe.feelingsbox.R;
import com.ufrpe.feelingsbox.infra.GuiUtil;
import com.ufrpe.feelingsbox.infra.adapter.post.PostRecyclerAdapter;
import com.ufrpe.feelingsbox.infra.adapter.post.RecyclerViewOnClickListenerhack;
import com.ufrpe.feelingsbox.infra.provider.SearchableProvider;
import com.ufrpe.feelingsbox.redesocial.dominio.Post;

import java.util.ArrayList;
import java.util.List;

public class ActSearchable extends AppCompatActivity implements RecyclerViewOnClickListenerhack{
    private ArrayList<? extends Post> mList;
    private ArrayList<? extends Post> mListAux;
    private RecyclerView mRecyclerView;
    private PostRecyclerAdapter adapter;
    private CoordinatorLayout clContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{super.onCreate(savedInstanceState);
        setContentView(R.layout.act_searchable);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState != null){
            mList    = savedInstanceState.getParcelableArrayList("mList");
            mListAux = savedInstanceState.getParcelableArrayList("mListAux");
        } else {
            mListAux = new ArrayList<>();
        }

        clContainer = (CoordinatorLayout) findViewById(R.id.cl_container);

            mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        //mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        adapter = new PostRecyclerAdapter(this, (List<Post>) mListAux);
        adapter.setRecyclerViewOnClickListenerhack(this);
        mRecyclerView.setAdapter(adapter);

        handleSearch(getIntent());
        } catch (Exception e){
            GuiUtil.myToast(this, e);
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent); //Evita problemas ao rotacionar a tela.
        handleSearch(intent);
    }

    public void handleSearch(Intent intent){
        if(Intent.ACTION_SEARCH.equalsIgnoreCase(intent.getAction())){
            String stringBusca = intent.getStringExtra(SearchManager.QUERY);

            buscarPosts(stringBusca);

            SearchRecentSuggestions searchRecentSuggestions = new SearchRecentSuggestions(this,
                    SearchableProvider.AUTHORITY,
                    SearchableProvider.MODE);
            searchRecentSuggestions.saveRecentQuery(stringBusca, null);

        }
    }

    public void buscarPosts(String stringBusca){
        mListAux.clear();
        //mListAux = Inserir a busca baseada na stringBusca!!!

        mRecyclerView.setVisibility(mListAux.isEmpty() ? View.GONE : View.VISIBLE);
        if(mListAux.isEmpty()){
            TextView textView = new TextView(this);
            textView.setText(R.string.lbl_nehum_post_encontrado);
            textView.setTextColor(getResources().getColor(R.color.colorUserFont));
            textView.setId(1);
            textView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            textView.setGravity(Gravity.CENTER);

            clContainer.addView(textView);
        } else if(clContainer.findViewById(1) != null){
            clContainer.removeView(clContainer.findViewById(1));

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        outState.putParcelableArrayList("mList", (ArrayList<? extends Parcelable>) mList);
        outState.putParcelableArrayList("mListAux", (ArrayList<? extends Parcelable>) mListAux);

        super.onSaveInstanceState(outState);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_act_searchable, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView;
        MenuItem item = menu.findItem(R.id.action_pesquisar);

        searchView = (SearchView) item.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //searchView.setQueryHint("Pesquisar post");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.action_delete:
                SearchRecentSuggestions searchRecentSuggestions = new SearchRecentSuggestions(this,
                        SearchableProvider.AUTHORITY,
                        SearchableProvider.MODE);
                searchRecentSuggestions.clearHistory();
                GuiUtil.myToastShort(this, getString(R.string.print_histórico_apagado));
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClickListener(View view, int position) {

    }

    @Override
    public void onLongPressClickListener(View view, int position) {

    }
}