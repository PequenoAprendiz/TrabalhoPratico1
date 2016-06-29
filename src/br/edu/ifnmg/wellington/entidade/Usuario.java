/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.wellington.entidade;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Were
 */
public class Usuario {

    protected String nome;
    protected String login;
    protected String senha;
    protected List<String> grupoUsuario = new ArrayList<String>();
    protected int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public List<String> getGrupoUsuario() {
        return this.grupoUsuario;
    }

    public void setGrupoUsuario(String grupoUsuario) {
        this.grupoUsuario.add(grupoUsuario);
    }

    @Override
    public String toString() {
        return "Usuario{" + "nome=" + nome + ", login=" + login + ", senha=" + senha + '}';
    }

}
