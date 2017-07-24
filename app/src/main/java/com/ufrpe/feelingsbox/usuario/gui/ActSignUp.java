package com.ufrpe.feelingsbox.usuario.gui;
/*
 * Tela de Cadastro
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ufrpe.feelingsbox.R;
import com.ufrpe.feelingsbox.infra.Mask;
import com.ufrpe.feelingsbox.usuario.dominio.Pessoa;
import com.ufrpe.feelingsbox.usuario.dominio.SexoEnum;
import com.ufrpe.feelingsbox.usuario.dominio.Usuario;
import com.ufrpe.feelingsbox.usuario.usuarioservices.UsuarioService;
import com.ufrpe.feelingsbox.usuario.usuarioservices.ValidacaoCadastro;
import com.ufrpe.feelingsbox.usuario.persistencia.PessoaDAO;
import com.ufrpe.feelingsbox.usuario.persistencia.UsuarioDAO;

import static com.ufrpe.feelingsbox.usuario.dominio.SexoEnum.SexoEnumLista;

public class ActSignUp extends AppCompatActivity {

    //Declarando os Elementos da Tela(activity)
    private UsuarioDAO usuarioDAO;
    private PessoaDAO pessoaDAO;
    private EditText edtNome, edtNick, edtEmail, edtNasc, edtSenha;
    Spinner spinner;

    //Lista para por no Spinner
    private String[] listaSexo = SexoEnumLista();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_sign_up);

        //Encontrando Elemento da Tela(activity)
        edtNome = (EditText)findViewById(R.id.edtNome);
        edtNick = (EditText)findViewById(R.id.edtNick);
        edtEmail = (EditText)findViewById(R.id.edtEmail);
        edtNasc = (EditText)findViewById(R.id.edtNasc);
        edtNasc.addTextChangedListener(Mask.insert("##/##/####", edtNasc));
        edtSenha = (EditText)findViewById(R.id.edtSenha);

        //ArrayAdapter é usado preparar a lista da por no Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listaSexo);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Encontrando o Spinner e colocando a lista adaptada
        spinner = (Spinner)findViewById(R.id.spnSexo);
        spinner.setAdapter(adapter);

        //Metodo para quando um elemento do Spinner é selecionado()
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    //Ação ao Clicar no botão Cadastrar
    public void validarCadastrar(View view){
        //Pegando os valores dos EditText
        String nome     = edtNome.getText().toString();
        String nick     = edtNick.getText().toString();
        String email    = edtEmail.getText().toString();
        String nasc     = edtNasc.getText().toString();
        String senha    = edtSenha.getText().toString();

        //Pegando os valor do Spinner
        String sexoTexto = (String) spinner.getSelectedItem();     //Retorna a String do elemento selecionado do Spinner
        //long sexoId = spinner.getSelectedItemId();                  //Retorna o ID do elemento selecionado do Spinner
        //int sexoPosicao = spinner.getSelectedItemPosition();        //Retorna a Posição do elemento selecionado do Spinner

        ValidacaoCadastro validacaoCadastro = new ValidacaoCadastro();
        if(validacaoCadastro.isCampoVazio(nome)){
            edtNome.requestFocus();
            edtNome.setError("Nome Inválido");
        }

        else if(!validacaoCadastro.isNickValido(nick)){
            edtNick.requestFocus();
            edtNick.setError("Apelido Inválido ou Inexistente.");
        }

        else if(!validacaoCadastro.isEmailValido(email)){
            edtEmail.requestFocus();
            edtEmail.setError("Endereço de Email Inválido.");
        }

        else if(!validacaoCadastro.isNascValido(nasc)){
            edtNasc.requestFocus();
            edtNasc.setError("Data Inválida.");
        }

        else if(!validacaoCadastro.isSenhaValida(senha)){
            edtSenha.requestFocus();
            edtSenha.setError("Senha fora do padrão.");
        }

        UsuarioService service = new UsuarioService();
        try {
            service.cadastrar(nome, sexoTexto, nasc, nick, email, senha);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(ActSignUp.this, ActHome.class);
        startActivity(intent);
        finish();


    }

    public void cancelarCadastro(View view){
        //Trocando a Tela (Cancelar cadastro)
        //Intent it = new Intent(ActSignUp.this, ActLogin.class);
        //startActivity(it);
        finish();
    }


}

/*"Print" para fim de testes
            Toast.makeText(this,    "Nome: "+ nome +
                    ", Nick: " + nick +
                    ", Email: " + email +
                    ", Data: " + nasc +
                    ", Senha: "+ senha +
                    ", Sexo: " + sexoTexto,
                    Toast.LENGTH_SHORT).show();*/