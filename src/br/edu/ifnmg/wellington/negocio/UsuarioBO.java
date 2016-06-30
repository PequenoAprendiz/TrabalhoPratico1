package br.edu.ifnmg.wellington.negocio;

import br.edu.ifnmg.wellington.entidade.Usuario;
import br.edu.ifnmg.wellington.exception.LoginInvalidoException;
import br.edu.ifnmg.wellington.persistencia.UsuarioDAO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Were
 */
public class UsuarioBO {

    public void verificaDados(Usuario usuarioEmEdicao, int estadoTela) throws SQLException {       
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario u = usuarioDAO.buscarUsuarioPorLogin(usuarioEmEdicao.getLogin());
        
        if(u.getLogin().equals(usuarioEmEdicao.getLogin())){
            throw  new LoginInvalidoException("Já existe usuario com este login!");
        }else if (usuarioEmEdicao.getLogin().trim().isEmpty() && usuarioEmEdicao.getSenha().trim().isEmpty()) {
            throw new RuntimeException();
        } else if (estadoTela == 1) {         
            this.editarUsuario(usuarioEmEdicao);
        } else {            
            usuarioDAO.salvarUsuario(usuarioEmEdicao);
        }
    }

    public List<Usuario> buscarTodosUsuarios() throws SQLException {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        return usuarioDAO.buscarTodosUsuarios();
    }   

    public void atualizarSenha(Usuario usuarioEmEdicao) throws SQLException {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarioDAO.atualizarSenha(usuarioEmEdicao);
    }

    public Usuario buscarUsuarioPorLogin(String login) throws SQLException {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        return usuarioDAO.buscarUsuarioPorLogin(login);
    }

    public void excluirUsuario(Usuario  usuario) throws SQLException {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarioDAO.removerUsuario(usuario);
    }

    public void editarUsuario(Usuario usuario) throws SQLException {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        usuarioDAO.editarUsuario(usuario);
    }

}
