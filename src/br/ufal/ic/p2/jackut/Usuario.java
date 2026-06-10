package br.ufal.ic.p2.jackut;

import java.io.Serializable;
import java.util.*;

public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String login;
    private String senha;
    private String nome;
    private Map<String, String> perfil;
    private Set<String> convitesPendentes;
    private List<String> amigos;
    private Queue<String> recados;

    public Usuario(String login, String senha, String nome) {
        this.login = login;
        this.senha = senha;
        this.nome = nome;
        this.perfil = new LinkedHashMap<>();
        this.convitesPendentes = new LinkedHashSet<>();
        this.amigos = new ArrayList<>();
        this.recados = new LinkedList<>();
    }

    public String getLogin() { return login; }

    public boolean verificarSenha(String senha) { return this.senha.equals(senha); }

    public String getAtributo(String atributo) {
        if ("nome".equals(atributo)) return nome;
        if (!perfil.containsKey(atributo))
            throw new RuntimeException("Atributo n\u00e3o preenchido.");
        return perfil.get(atributo);
    }

    public void editarPerfil(String atributo, String valor) {
        if ("nome".equals(atributo)) this.nome = valor;
        else perfil.put(atributo, valor);
    }

    public void enviarConvite(String loginAmigo) {
        if (amigos.contains(loginAmigo))
            throw new RuntimeException("Usu\u00e1rio j\u00e1 est\u00e1 adicionado como amigo.");
        if (convitesPendentes.contains(loginAmigo))
            throw new RuntimeException("Usu\u00e1rio j\u00e1 est\u00e1 adicionado como amigo, esperando aceita\u00e7\u00e3o do convite.");
        convitesPendentes.add(loginAmigo);
    }

    public void aceitarConvite(String loginAmigo) {
        convitesPendentes.remove(loginAmigo);
        if (!amigos.contains(loginAmigo)) amigos.add(loginAmigo);
    }

    public boolean temConvitePendentePara(String loginAmigo) {
        return convitesPendentes.contains(loginAmigo);
    }

    public boolean ehAmigo(String loginAmigo) { return amigos.contains(loginAmigo); }

    public List<String> getAmigos() { return Collections.unmodifiableList(amigos); }

    public void receberRecado(String recado) { recados.add(recado); }

    public String lerRecado() {
        if (recados.isEmpty())
            throw new RuntimeException("N\u00e3o h\u00e1 recados.");
        return recados.poll();
    }
}
