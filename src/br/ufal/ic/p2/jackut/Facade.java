package br.ufal.ic.p2.jackut;

import easyaccept.EasyAccept;

public class Facade {

    private Sistema sistema = new Sistema();

    public void zerarSistema() {
        sistema.zerarSistema();
    }

    public void encerrarSistema() {
        sistema.encerrarSistema();
    }

    public void criarUsuario(String login, String senha, String nome) {
        sistema.criarUsuario(login, senha, nome);
    }

    public String abrirSessao(String login, String senha) {
        return sistema.abrirSessao(login, senha);
    }

    public String getAtributoUsuario(String login, String atributo) {
        return sistema.getAtributoUsuario(login, atributo);
    }

    public void editarPerfil(String id, String atributo, String valor) {
        sistema.editarPerfil(id, atributo, valor);
    }

    public void adicionarAmigo(String id, String amigo) {
        sistema.adicionarAmigo(id, amigo);
    }

    public String ehAmigo(String login, String amigo) {
        return sistema.ehAmigo(login, amigo);
    }

    public String getAmigos(String login) {
        return sistema.getAmigos(login);
    }

    public void enviarRecado(String id, String destinatario, String recado) {
        sistema.enviarRecado(id, destinatario, recado);
    }

    public String lerRecado(String id) {
        return sistema.lerRecado(id);
    }
}