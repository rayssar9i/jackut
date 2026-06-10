package br.ufal.ic.p2.jackut;

/**
 * Representa uma sessão aberta de um usuário no sistema Jackut.
 * Uma sessão é criada ao abrir sessão com login e senha válidos e
 * é identificada por um id único.
 */
public class Sessao {

    /** Identificador único desta sessão. */
    private final String id;

    /** Usuário associado a esta sessão. */
    private final Usuario usuario;

    /**
     * Cria uma nova sessão para o usuário informado.
     *
     * @param id      identificador único da sessão
     * @param usuario usuário que abriu a sessão
     */
    public Sessao(String id, Usuario usuario) {
        this.id = id;
        this.usuario = usuario;
    }

    /**
     * Retorna o identificador desta sessão.
     *
     * @return id da sessão
     */
    public String getId() {
        return id;
    }

    /**
     * Retorna o usuário associado a esta sessão.
     *
     * @return usuário da sessão
     */
    public Usuario getUsuario() {
        return usuario;
    }
}
