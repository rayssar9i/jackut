package br.ufal.ic.p2.jackut;

/**
 * Sessao aberta de um usuario no Jackut.
 * Contem id unico e o usuario logado.
 */
public class Sessao {

    /** Identificador unico da sessao. */
    private final String id;

    /** Usuario logado nesta sessao. */
    private final Usuario usuario;

    /**
     * Cria uma nova sessao para o usuario informado.
     *
     * @param id      identificador unico da sessao
     * @param usuario usuario logado
     */
    public Sessao(String id, Usuario usuario) {
        this.id = id;
        this.usuario = usuario;
    }

    /**
     * Retorna o id da sessao.
     *
     * @return id da sessao
     */
    public String getId() {
        return id;
    }

    /**
     * Retorna o usuario desta sessao.
     *
     * @return usuario da sessao
     */
    public Usuario getUsuario() {
        return usuario;
    }
}
