package br.ufal.ic.p2.jackut;

import java.io.*;
import java.util.*;

/**
 * Classe central de logica de negocio do Jackut.
 * Mantem o cadastro de usuarios, sessoes abertas e eh responsavel
 * pela persistencia dos dados em arquivo.
 */
public class Sistema {

    /** Caminho do arquivo de persistencia. */
    private static final String ARQUIVO_DADOS = "jackut_dados.ser";

    /** Mapa de usuarios indexado por login. */
    private Map<String, Usuario> usuarios;

    /** Mapa de sessoes abertas indexado pelo id da sessao. */
    private transient Map<String, Sessao> sessoes;

    /** Contador utilizado para gerar ids unicos de sessao. */
    private transient int contadorSessao;

    /**
     * Cria o sistema carregando dados do arquivo de persistencia, se existir.
     */
    public Sistema() {
        sessoes = new HashMap<>();
        contadorSessao = 0;
        usuarios = carregarDados();
    }

    /**
     * Apaga todos os dados do sistema e remove o arquivo de persistencia.
     */
    public void zerarSistema() {
        usuarios = new LinkedHashMap<>();
        sessoes = new HashMap<>();
        contadorSessao = 0;
        File arquivo = new File(ARQUIVO_DADOS);
        if (arquivo.exists()) {
            arquivo.delete();
        }
    }

    /**
     * Cria um novo usuario com os dados fornecidos.
     *
     * @param login login do novo usuario
     * @param senha senha do novo usuario
     * @param nome  nome de exibicao
     * @throws RuntimeException se login ou senha invalidos, ou login ja existente
     */
    public void criarUsuario(String login, String senha, String nome) {
        if (login == null || login.trim().isEmpty()) {
            throw new RuntimeException("Login inv�lido.");
        }
        if (senha == null || senha.trim().isEmpty()) {
            throw new RuntimeException("Senha inv�lida.");
        }
        if (usuarios.containsKey(login)) {
            throw new RuntimeException("Conta com esse nome j� existe.");
        }
        usuarios.put(login, new Usuario(login, senha, nome));
    }

    /**
     * Abre uma sessao para o usuario com login e senha fornecidos.
     *
     * @param login login do usuario
     * @param senha senha do usuario
     * @return id da sessao criada
     * @throws RuntimeException se login ou senha invalidos ou incorretos
     */
    public String abrirSessao(String login, String senha) {
        if (login == null || login.isEmpty() || senha == null || senha.isEmpty()) {
            throw new RuntimeException("Login ou senha inv�lidos.");
        }
        Usuario usuario = usuarios.get(login);
        if (usuario == null || !usuario.verificarSenha(senha)) {
            throw new RuntimeException("Login ou senha inv�lidos.");
        }
        String id = login + "_" + (++contadorSessao);
        Sessao sessao = new Sessao(id, usuario);
        sessoes.put(id, sessao);
        return id;
    }

    /**
     * Retorna o valor de um atributo do perfil de um usuario.
     *
     * @param login    login do usuario
     * @param atributo nome do atributo
     * @return valor do atributo
     * @throws RuntimeException se o usuario nao existir ou atributo nao preenchido
     */
    public String getAtributoUsuario(String login, String atributo) {
        Usuario usuario = getUsuarioOuErro(login);
        return usuario.getAtributo(atributo);
    }

    /**
     * Edita um atributo do perfil do usuario identificado pela sessao.
     *
     * @param id       id da sessao
     * @param atributo nome do atributo
     * @param valor    novo valor do atributo
     * @throws RuntimeException se a sessao nao existir
     */
    public void editarPerfil(String id, String atributo, String valor) {
        Sessao sessao = getSessaoOuErro(id);
        sessao.getUsuario().editarPerfil(atributo, valor);
    }

    /**
     * Adiciona um amigo ao usuario da sessao informada.
     * O relacionamento e efetivado apenas quando ambos os lados se adicionam.
     *
     * @param id    id da sessao do usuario
     * @param amigo login do usuario a adicionar como amigo
     * @throws RuntimeException diversas condicoes de erro
     */
    public void adicionarAmigo(String id, String amigo) {
        Sessao sessao = getSessaoOuErro(id);
        Usuario usuario = sessao.getUsuario();

        if (usuario.getLogin().equals(amigo)) {
            throw new RuntimeException("Usu�rio n�o pode adicionar a si mesmo como amigo.");
        }

        Usuario usuarioAmigo = getUsuarioOuErro(amigo);

        if (usuarioAmigo.temConvitePendentePara(usuario.getLogin())) {
            usuarioAmigo.aceitarConvite(usuario.getLogin());
            usuario.aceitarConvite(amigo);
        } else {
            usuario.enviarConvite(amigo);
        }
    }

    /**
     * Verifica se dois usuarios sao amigos confirmados.
     *
     * @param login login do primeiro usuario
     * @param amigo login do segundo usuario
     * @return "true" se forem amigos, "false" caso contrario
     */
    public String ehAmigo(String login, String amigo) {
        Usuario usuario = usuarios.get(login);
        if (usuario == null) return "false";
        return String.valueOf(usuario.ehAmigo(amigo));
    }

    /**
     * Retorna a lista de amigos do usuario no formato {login1,login2,...}.
     *
     * @param login login do usuario
     * @return string com a lista de amigos
     * @throws RuntimeException se o usuario nao existir
     */
    public String getAmigos(String login) {
        Usuario usuario = getUsuarioOuErro(login);
        List<String> amigos = usuario.getAmigos();
        return "{" + String.join(",", amigos) + "}";
    }

    /**
     * Envia um recado ao destinatario indicado.
     *
     * @param id           id da sessao do remetente
     * @param destinatario login do destinatario
     * @param recado       texto do recado
     * @throws RuntimeException se a sessao for invalida, destinatario nao existir
     *                          ou o usuario tentar enviar recado a si mesmo
     */
    public void enviarRecado(String id, String destinatario, String recado) {
        Sessao sessao = getSessaoOuErro(id);
        Usuario remetente = sessao.getUsuario();

        if (remetente.getLogin().equals(destinatario)) {
            throw new RuntimeException("Usu�rio n�o pode enviar recado para si mesmo.");
        }

        Usuario usuarioDestino = getUsuarioOuErro(destinatario);
        usuarioDestino.receberRecado(recado);
    }

    /**
     * Le e remove o primeiro recado da fila do usuario com sessao aberta.
     *
     * @param id id da sessao
     * @return texto do primeiro recado
     * @throws RuntimeException se a sessao for invalida ou nao houver recados
     */
    public String lerRecado(String id) {
        Sessao sessao = getSessaoOuErro(id);
        return sessao.getUsuario().lerRecado();
    }

    /**
     * Grava os dados em arquivo e encerra o sistema.
     */
    public void encerrarSistema() {
        salvarDados();
    }

    // --- Metodos auxiliares privados ---

    private Usuario getUsuarioOuErro(String login) {
        Usuario usuario = usuarios.get(login);
        if (usuario == null) {
            throw new RuntimeException("Usu�rio n�o cadastrado.");
        }
        return usuario;
    }

    private Sessao getSessaoOuErro(String id) {
        if (id == null || id.isEmpty()) {
            throw new RuntimeException("Usu�rio n�o cadastrado.");
        }
        Sessao sessao = sessoes.get(id);
        if (sessao == null) {
            throw new RuntimeException("Usu�rio n�o cadastrado.");
        }
        return sessao;
    }

    @SuppressWarnings("unchecked")
    private void salvarDados() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ARQUIVO_DADOS))) {
            oos.writeObject(usuarios);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Usuario> carregarDados() {
        File arquivo = new File(ARQUIVO_DADOS);
        if (!arquivo.exists()) {
            return new LinkedHashMap<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(arquivo))) {
            return (Map<String, Usuario>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar dados: " + e.getMessage());
            return new LinkedHashMap<>();
        }
    }
}
