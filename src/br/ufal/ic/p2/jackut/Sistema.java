package br.ufal.ic.p2.jackut;

import java.io.*;
import java.util.*;

public class Sistema {

    private static final String ARQUIVO_DADOS = "jackut_dados.ser";
    private Map<String, Usuario> usuarios;
    private transient Map<String, Sessao> sessoes;
    private transient int contadorSessao;

    public Sistema() {
        sessoes = new HashMap<>();
        contadorSessao = 0;
        usuarios = carregarDados();
    }

    public void zerarSistema() {
        usuarios = new LinkedHashMap<>();
        sessoes = new HashMap<>();
        contadorSessao = 0;
        File arquivo = new File(ARQUIVO_DADOS);
        if (arquivo.exists()) arquivo.delete();
    }

    public void criarUsuario(String login, String senha, String nome) {
        if (login == null || login.trim().isEmpty())
            throw new RuntimeException("Login inv\u00e1lido.");
        if (senha == null || senha.trim().isEmpty())
            throw new RuntimeException("Senha inv\u00e1lida.");
        if (usuarios.containsKey(login))
            throw new RuntimeException("Conta com esse nome j\u00e1 existe.");
        usuarios.put(login, new Usuario(login, senha, nome));
    }

    public String abrirSessao(String login, String senha) {
        if (login == null || login.isEmpty() || senha == null || senha.isEmpty())
            throw new RuntimeException("Login ou senha inv\u00e1lidos.");
        Usuario usuario = usuarios.get(login);
        if (usuario == null || !usuario.verificarSenha(senha))
            throw new RuntimeException("Login ou senha inv\u00e1lidos.");
        String id = login + "_" + (++contadorSessao);
        sessoes.put(id, new Sessao(id, usuario));
        return id;
    }

    public String getAtributoUsuario(String login, String atributo) {
        return getUsuarioOuErro(login).getAtributo(atributo);
    }

    public void editarPerfil(String id, String atributo, String valor) {
        getSessaoOuErro(id).getUsuario().editarPerfil(atributo, valor);
    }

    public void adicionarAmigo(String id, String amigo) {
        Sessao sessao = getSessaoOuErro(id);
        Usuario usuario = sessao.getUsuario();
        if (usuario.getLogin().equals(amigo))
            throw new RuntimeException("Usu\u00e1rio n\u00e3o pode adicionar a si mesmo como amigo.");
        Usuario usuarioAmigo = getUsuarioOuErro(amigo);
        if (usuarioAmigo.temConvitePendentePara(usuario.getLogin())) {
            usuarioAmigo.aceitarConvite(usuario.getLogin());
            usuario.aceitarConvite(amigo);
        } else {
            usuario.enviarConvite(amigo);
        }
    }

    public String ehAmigo(String login, String amigo) {
        Usuario usuario = usuarios.get(login);
        if (usuario == null) return "false";
        return String.valueOf(usuario.ehAmigo(amigo));
    }

    public String getAmigos(String login) {
        return "{" + String.join(",", getUsuarioOuErro(login).getAmigos()) + "}";
    }

    public void enviarRecado(String id, String destinatario, String recado) {
        Sessao sessao = getSessaoOuErro(id);
        Usuario remetente = sessao.getUsuario();
        if (remetente.getLogin().equals(destinatario))
            throw new RuntimeException("Usu\u00e1rio n\u00e3o pode enviar recado para si mesmo.");
        getUsuarioOuErro(destinatario).receberRecado(recado);
    }

    public String lerRecado(String id) {
        return getSessaoOuErro(id).getUsuario().lerRecado();
    }

    public void encerrarSistema() { salvarDados(); }

    private Usuario getUsuarioOuErro(String login) {
        Usuario usuario = usuarios.get(login);
        if (usuario == null)
            throw new RuntimeException("Usu\u00e1rio n\u00e3o cadastrado.");
        return usuario;
    }
//comentario
    private Sessao getSessaoOuErro(String id) {
        if (id == null || id.isEmpty())
            throw new RuntimeException("Usu\u00e1rio n\u00e3o cadastrado.");
        Sessao sessao = sessoes.get(id);
        if (sessao == null)
            throw new RuntimeException("Usu\u00e1rio n\u00e3o cadastrado.");
        return sessao;
    }

    private void salvarDados() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_DADOS))) {
            oos.writeObject(usuarios);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Usuario> carregarDados() {
        File arquivo = new File(ARQUIVO_DADOS);
        if (!arquivo.exists()) return new LinkedHashMap<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (Map<String, Usuario>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar dados: " + e.getMessage());
            return new LinkedHashMap<>();
        }
    }
}
