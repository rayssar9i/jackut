package br.ufal.ic.p2.jackut;

import java.io.Serializable;
import java.util.*;

/**
 * Representa um usuario cadastrado no sistema Jackut.
 * Mantem as informacoes de conta, perfil, amigos e recados.
 */
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Login unico que identifica o usuario no sistema. */
    private final String login;

    /** Senha de acesso do usuario. */
    private String senha;

    /** Nome pelo qual o usuario e conhecido na rede. */
    private String nome;

    /** Atributos do perfil, armazenados como pares chave-valor. */
    private Map<String, String> perfil;

    /**
     * Logins dos usuarios que este usuario enviou convite de amizade,
     * mas que ainda nao foram aceitos.
     */
    private Set<String> convitesPendentes;

    /**
     * Logins dos amigos confirmados deste usuario.
     * A amizade e mutua: ambos os lados precisam adicionar um ao outro.
     */
    private List<String> amigos;

    /** Fila de recados recebidos, em ordem de chegada (FIFO). */
    private Queue<String> recados;

    /**
     * Cria um novo usuario com os dados fornecidos.
     *
     * @param login login unico do usuario
     * @param senha senha de acesso
     * @param nome  nome de exibicao na rede
     */
    public Usuario(String login, String senha, String nome) {
        this.login = login;
        this.senha = senha;
        this.nome = nome;
        this.perfil = new LinkedHashMap<>();
        this.convitesPendentes = new LinkedHashSet<>();
        this.amigos = new ArrayList<>();
        this.recados = new LinkedList<>();
    }

    /**
     * Retorna o login do usuario.
     *
     * @return login
     */
    public String getLogin() {
        return login;
    }

    /**
     * Verifica se a senha fornecida corresponde a senha do usuario.
     *
     * @param senha senha a verificar
     * @return true se a senha for correta
     */
    public boolean verificarSenha(String senha) {
        return this.senha.equals(senha);
    }

    /**
     * Retorna o valor de um atributo do perfil do usuario.
     * O atributo "nome" e especial e retorna o nome do usuario.
     *
     * @param atributo nome do atributo
     * @return valor do atributo
     * @throws RuntimeException se o atributo nao estiver preenchido
     */
    public String getAtributo(String atributo) {
        if ("nome".equals(atributo)) {
            return nome;
        }
        if (!perfil.containsKey(atributo)) {
            throw new RuntimeException("Atributo n�o preenchido.");
        }
        return perfil.get(atributo);
    }

    /**
     * Define ou atualiza o valor de um atributo do perfil.
     * O atributo "nome" e especial e atualiza o nome do usuario.
     *
     * @param atributo nome do atributo
     * @param valor    valor a armazenar
     */
    public void editarPerfil(String atributo, String valor) {
        if ("nome".equals(atributo)) {
            this.nome = valor;
        } else {
            perfil.put(atributo, valor);
        }
    }

    /**
     * Envia um convite de amizade a outro usuario.
     * Registra o login do destinatario nos convites pendentes deste usuario.
     *
     * @param loginAmigo login do usuario a ser adicionado
     * @throws RuntimeException se o convite ja foi enviado ou se ja sao amigos
     */
    public void enviarConvite(String loginAmigo) {
        if (amigos.contains(loginAmigo)) {
            throw new RuntimeException("Usu�rio j� est� adicionado como amigo.");
        }
        if (convitesPendentes.contains(loginAmigo)) {
            throw new RuntimeException("Usu�rio j� est� adicionado como amigo, esperando aceita��o do convite.");
        }
        convitesPendentes.add(loginAmigo);
    }

    /**
     * Aceita um convite de amizade recebido de outro usuario.
     * Adiciona o outro usuario a lista de amigos e remove o convite pendente.
     *
     * @param loginAmigo login de quem enviou o convite
     */
    public void aceitarConvite(String loginAmigo) {
        convitesPendentes.remove(loginAmigo);
        if (!amigos.contains(loginAmigo)) {
            amigos.add(loginAmigo);
        }
    }

    /**
     * Verifica se este usuario possui convite pendente enviado para o login informado.
     *
     * @param loginAmigo login a verificar
     * @return true se ha convite pendente
     */
    public boolean temConvitePendentePara(String loginAmigo) {
        return convitesPendentes.contains(loginAmigo);
    }

    /**
     * Verifica se o usuario informado e amigo confirmado deste usuario.
     *
     * @param loginAmigo login do possivel amigo
     * @return true se for amigo
     */
    public boolean ehAmigo(String loginAmigo) {
        return amigos.contains(loginAmigo);
    }

    /**
     * Retorna a lista de logins dos amigos confirmados.
     *
     * @return lista de amigos
     */
    public List<String> getAmigos() {
        return Collections.unmodifiableList(amigos);
    }

    /**
     * Adiciona um recado a fila de recados deste usuario.
     *
     * @param recado texto do recado
     */
    public void receberRecado(String recado) {
        recados.add(recado);
    }

    /**
     * Remove e retorna o primeiro recado da fila.
     *
     * @return texto do primeiro recado
     * @throws RuntimeException se nao houver recados
     */
    public String lerRecado() {
        if (recados.isEmpty()) {
            throw new RuntimeException("N�o h� recados.");
        }
        return recados.poll();
    }
}
