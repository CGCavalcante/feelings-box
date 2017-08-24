package com.ufrpe.feelingsbox.redesocial.redesocialservices;

import android.content.Context;

import com.ufrpe.feelingsbox.redesocial.dominio.Comentario;
import com.ufrpe.feelingsbox.redesocial.dominio.Post;
import com.ufrpe.feelingsbox.redesocial.dominio.Sessao;
import com.ufrpe.feelingsbox.redesocial.persistencia.ComentarioDAO;
import com.ufrpe.feelingsbox.redesocial.persistencia.PosTagDAO;
import com.ufrpe.feelingsbox.redesocial.persistencia.PostDAO;
import com.ufrpe.feelingsbox.redesocial.persistencia.RelacaoSegDAO;
import com.ufrpe.feelingsbox.redesocial.persistencia.SessaoDAO;
import com.ufrpe.feelingsbox.redesocial.persistencia.SugestaoDAO;
import com.ufrpe.feelingsbox.redesocial.persistencia.TagDAO;
import com.ufrpe.feelingsbox.usuario.dominio.Pessoa;
import com.ufrpe.feelingsbox.usuario.dominio.Usuario;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RedeServices {
    private Sessao sessao = Sessao.getInstancia();
    private Post post;
    private PosTagDAO posTagDAO;
    private SessaoDAO sessaoDAO;
    private PostDAO postDAO;
    private ComentarioDAO comentarioDAO;
    private Context context;
    private Comentario comentario;
    private RelacaoSegDAO relacaoSegDAO;
    private SugestaoDAO sugestaoDAO;
    private TagDAO tagDAO;

    private static final int UM = 1;

    public RedeServices(Context context) {
        this.context = context;
    }

    public void salvarPost(String texto) {
        postDAO = new PostDAO(context);
        post = new Post();
        post.setTexto(texto);
        post.setDataHora();
        post.setIdUsuario(sessao.getUsuarioLogado().getId());
        long idPost = postDAO.inserirPost(post);
        post.setId(idPost);

        TagDAO tagDAO = new TagDAO(context);
        PosTagDAO posTagDAO = new PosTagDAO(context);
        String regexHashTag = "(#\\w+)";

        Pattern pattern = Pattern.compile(regexHashTag);
        Matcher matcher = pattern.matcher(texto);
        while (matcher.find()) {
            String hashTagStr = matcher.group(UM);
            if (tagDAO.getTagTexto(hashTagStr) == null) {
                tagDAO.inserirTag(hashTagStr.toLowerCase());
            }
            posTagDAO.inserirTagIdPost(hashTagStr.toLowerCase(), idPost);
        }

    }

    public void salvarComentario(String texto, long idPost){
        comentarioDAO = new ComentarioDAO(context);
        comentario = new Comentario();
        comentario.setTexto(texto);
        comentario.setDataHora();
        comentario.setIdUsuario(sessao.getUsuarioLogado().getId());
        comentario.setIdPost(idPost);
        long idComentario = comentarioDAO.inserirComentario(comentario);
        comentario.setId(idComentario);
    }

    public List<Post> exibirPosts() {
        postDAO = new PostDAO(context);
        return postDAO.getPostsByOrderId();
    }

    public void finalizarSessao(){
        sessaoDAO = new SessaoDAO(context);
        sessaoDAO.removerPessoa();
    }

    public List<Post> buscarPosts(String tag){
        posTagDAO = new PosTagDAO(context);
        return posTagDAO.getPostByTag(tag);
    }

    public List<Post> exibirPosts(long id){
        postDAO = new PostDAO(context);
        return postDAO.getPostByUser(id);
    }

    public List<Comentario> exibirComentarios(long idPost){
        comentarioDAO = new ComentarioDAO(context);
        return comentarioDAO.getComentariorioByPost(idPost);
    }

    public void seguirUser(long idUser, long idSeguir){
        relacaoSegDAO = new RelacaoSegDAO(context);
        relacaoSegDAO.inserirRelSeguidores(idUser, idSeguir);
    }

    public void deletarUser(long idSeguidor, long idSeguido){
        relacaoSegDAO = new RelacaoSegDAO(context);
        relacaoSegDAO.deletarRelSeguidores(idSeguidor, idSeguido);
    }

    public List<Usuario> listarSeguidores(long id){
        relacaoSegDAO = new RelacaoSegDAO(context);
        return relacaoSegDAO.getSeguidoresUser(id);
    }

    public List<Usuario> listarSeguidos(long id){
        relacaoSegDAO = new RelacaoSegDAO(context);
        return relacaoSegDAO.getSeguidosUser(id);
    }

    public long qtdSeguidores(long id){
        relacaoSegDAO = new RelacaoSegDAO(context);
        return relacaoSegDAO.getQtdSeguidoresUser(id);
    }

    public long qtdSeguidos(long id){
        relacaoSegDAO = new RelacaoSegDAO(context);
        return relacaoSegDAO.getQtdSeguidosUser(id);
    }

    public boolean verificacaoSeguidor(long idSeguidor, long idSeguido){
        relacaoSegDAO = new RelacaoSegDAO(context);
        return relacaoSegDAO.verificaSeguidor(idSeguidor, idSeguido);
    }
    public Pessoa verificarSessao(){
        sessaoDAO = new SessaoDAO(context);
        return sessaoDAO.getPessoaLogada();
    }

    public List<Post> postsEmAlta(){
        sugestaoDAO = new SugestaoDAO(context);
        postDAO = new PostDAO(context);
        LinkedHashSet<Post> listaFiltrada = new LinkedHashSet<>();
        listaFiltrada.addAll(sugestaoDAO.getPostsMaisComentadosHoje());
        listaFiltrada.addAll(postDAO.getPostsByOrderId());
        List<Post> listaFiltradaFinal = new ArrayList<>();
        listaFiltradaFinal.addAll(listaFiltrada);
        return listaFiltradaFinal;
    }

    public List<String> listaTags(){
        tagDAO = new TagDAO(context);
        return tagDAO.getTagsByOrder();
    }
}