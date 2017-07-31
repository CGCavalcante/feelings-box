package com.ufrpe.feelingsbox.redesocial.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ufrpe.feelingsbox.R;
import com.ufrpe.feelingsbox.infra.GuiUtil;
import com.ufrpe.feelingsbox.infra.ValidacaoService;

import java.util.zip.Inflater;

public class ActCriarPost extends AppCompatActivity {
    private TextView edtTags, edtTexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_criar_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtTags = (TextView) findViewById(R.id.edtTags);
        edtTexto = (TextView) findViewById(R.id.edtTexto);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_act_criar_post, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_cancelar:
                finish();

                break;
            case R.id.action_postar:
                String tags = edtTags.getText().toString();
                String texto = edtTexto.getText().toString();

                ValidacaoService validarPost = new ValidacaoService();
                boolean postVazio = false;
                if (validarPost.isCampoVazio(tags)){
                    edtTags.requestFocus();
                    edtTags.setError("Adicione tags para seu post.");
                    postVazio = true;
                }
                if (validarPost.isCampoVazio(texto)){
                    edtTexto.requestFocus();
                    edtTexto.setError("Digite algo para publicar.");
                    postVazio = true;
                }

                if (!postVazio){
                    GuiUtil.myToast(this, "Postado com sucesso.");
                    finish();
                }


                break;

            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
