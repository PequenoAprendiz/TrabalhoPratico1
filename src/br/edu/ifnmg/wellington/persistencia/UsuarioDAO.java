package br.edu.ifnmg.wellington.persistencia;

import br.edu.ifnmg.wellington.entidade.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Were
 */
public class UsuarioDAO {

    private static final String SQL_INSERT = "INSERT INTO USUARIO(NOME, LOGIN, SENHA) VALUES (?,?,?)";
    private static final String SQL_INSERT_GRUPOTRABALHO = "INSERT INTO GRUPOTRABALHO(IDUSUARIO, NOMEGRUPOTRABALHO) VALUES (?,?)";
    private static final String SQL_SELECT = "SELECT ID, NOME, LOGIN, SENHA  FROM USUARIO ORDER BY NOME";
    private static final String SQL_SELECT_GRUPOUSUARIO = "SELECT IDUSUARIO, NOMEGRUPOTRABALHO FROM GRUPOTRABALHO";
    private static final String SQL_UPDATE_SENHA = "UPDATE USUARIO SET  SENHA = ?   WHERE LOGIN = ?";
    private static final String SQL_UPDATE = "UPDATE USUARIO SET  NOME = ?, LOGIN = ?, SENHA = ?  WHERE ID = ?";
    private static final String SQL_DELETE = "DELETE FROM USUARIO WHERE LOGIN = ?";
    private static final String SQL_DELETE_GRUPOTRAB = "DELETE FROM GRUPOTRABALHO WHERE IDUSUARIO = ?";
    private static final String SQL_SELECT_POR_LOGIN = "SELECT  ID, NOME, LOGIN, SENHA FROM USUARIO where login = ?";

    public void salvarUsuario(Usuario usuarioEmEdicao) throws SQLException {
        Connection conexao = null;
        PreparedStatement comando = null;

        try {
            //Recupera a conexão
            conexao = BancoDadosUtil.getConnection();
            //Cria o comando de inserir dados
            comando = conexao.prepareStatement(SQL_INSERT);
            //Atribui os parâmetros (Note que no BD o index inicia por 1)
            comando.setString(1, usuarioEmEdicao.getNome());
            comando.setString(2, usuarioEmEdicao.getLogin());
            comando.setString(3, usuarioEmEdicao.getSenha());
            comando.execute();
            conexao.commit();
            this.salvarGrupoUsuario(usuarioEmEdicao);

            //System.out.println("Manobra cadastrada com sucesso!");
        } catch (Exception e) {
            //Caso aconteça alguma exeção é feito um rollback para o banco de
            //dados retornar ao seu estado anterior.
            if (conexao != null) {
                conexao.rollback();
            }
            throw e;
        } finally {
            //Todo objeto que referencie o banco de dados deve ser fechado
            BancoDadosUtil.fecharChamadasBancoDados(conexao, comando);
        }
    }

    public List<Usuario> buscarTodosUsuarios() throws SQLException {
        Connection conexao = null;
        PreparedStatement comando = null;
        PreparedStatement comandoConsultaGrupoTrabalho = null;
        ResultSet resultado = null;
        ResultSet resultadoBuscaGrupoTrabalho = null;
        List<Usuario> usuarios = new ArrayList<>();
        try {
            //Recupera a conexão
            conexao = BancoDadosUtil.getConnection();
            //Cria o comando de consulta dos dados
            comando = conexao.prepareStatement(SQL_SELECT);
            comandoConsultaGrupoTrabalho = conexao.prepareStatement(SQL_SELECT_GRUPOUSUARIO);
            //comando.setInt(1, idUsuario);
            //Executa o comando e obtém o resultado da consulta
            resultado = comando.executeQuery();
            resultadoBuscaGrupoTrabalho = comandoConsultaGrupoTrabalho.executeQuery();
            //O método next retornar boolean informando se existe um próximo
            //elemento para iterar

            while (resultado.next()) {
                Usuario usuario = this.extrairLinhaResultadoBuscarTodosUsuarios(resultado);
                //Adiciona um item à lista que será retornada
                while (resultadoBuscaGrupoTrabalho.next()) {
                    String nome = this.extrairLinhaResultadoBuscarGrupoTrabalho(resultadoBuscaGrupoTrabalho);
                    //Adiciona um item à lista que será retornada
                    usuario.setGrupoUsuario(nome);
                    // usuarios.add(usuario);
                }
                usuarios.add(usuario);
            }
        } finally {
            //Todo objeto que referencie o banco de dados deve ser fechado
            BancoDadosUtil.fecharChamadasBancoDados(conexao, comando, resultado);
        }
        return usuarios;
    }

    private Usuario extrairLinhaResultadoBuscarTodosUsuarios(ResultSet resultado1) throws SQLException {
        //Instancia um novo objeto e atribui os valores vindo do BD
        //(Note que no BD o index inicia por 1)
        Usuario usuario = new Usuario();
        usuario.setId(resultado1.getInt(1));
        usuario.setNome(resultado1.getString(2));
        usuario.setLogin(resultado1.getString(3));
        usuario.setSenha(resultado1.getString(4));

        return usuario;
    }

    private String extrairLinhaResultadoBuscarGrupoTrabalho(ResultSet resultadoBuscaGrupoTrabalho) throws SQLException {

        String nome = resultadoBuscaGrupoTrabalho.getString(2);
        return nome;
    }

    public void atualizarSenha(Usuario usuario) throws SQLException {
        Connection conexao = null;
        PreparedStatement comando = null;
        try {
            //Recupera a conexão
            conexao = BancoDadosUtil.getConnection();
            //Cria o comando de inserir dados
            comando = conexao.prepareStatement(SQL_UPDATE_SENHA);
            //Atribui os parâmetros (Note que no BD o index inicia por 1)                 
            comando.setString(1, usuario.getSenha());
            comando.setString(2, usuario.getLogin());

            //Executa o comando
            comando.execute();
            //Persiste o comando no banco de dados
            conexao.commit();
        } catch (Exception e) {
            //Caso aconteça alguma exeção é feito um rollback para o banco de
            //dados retornar ao seu estado anterior.
            if (conexao != null) {
                conexao.rollback();
            }
            throw e;
        } finally {
            //Todo objeto que referencie o banco de dados deve ser fechado
            BancoDadosUtil.fecharChamadasBancoDados(conexao, comando);
        }
    }

    public void removerUsuario(Usuario usuario) throws SQLException {
        Connection conexao = null;
        PreparedStatement comando = null;

        ///id = 4;
        try {
            //Recupera a conexão
            conexao = BancoDadosUtil.getConnection();
            //Cria o comando de inserir dados
            comando = conexao.prepareStatement(SQL_DELETE);
            //Atribui os parâmetros (Note que no BD o index inicia por 1)
            comando.setString(1, usuario.getLogin());
            //Executa o comando
            comando.execute();
             conexao.commit();
            comando = conexao.prepareStatement(SQL_DELETE_GRUPOTRAB);
            comando.setInt(1, usuario.getId());
            comando.execute();
            //Persiste o comando no banco de dados
            conexao.commit();
        } catch (Exception e) {
            //Caso aconteça alguma exeção é feito um rollback para o banco de
            //dados retornar ao seu estado anterior.
            if (conexao != null) {
                conexao.rollback();
            }
            throw e;
        } finally {
            //Todo objeto que referencie o banco de dados deve ser fechado
            BancoDadosUtil.fecharChamadasBancoDados(conexao, comando);
        }
    }

    public void editarUsuario(Usuario usuario) throws SQLException {
        Connection conexao = null;
        PreparedStatement comando = null;
        try {
            //Recupera a conexão
            conexao = BancoDadosUtil.getConnection();
            //Cria o comando de inserir dados
            comando = conexao.prepareStatement(SQL_UPDATE);
            //Atribui os parâmetros (Note que no BD o index inicia por 1)     
            comando.setString(1, usuario.getNome());
            comando.setString(2, usuario.getLogin());
            comando.setString(3, usuario.getSenha());
            comando.setInt(4, usuario.getId());
            //Executa o comando
            comando.execute();
            //Persiste o comando no banco de dados
            conexao.commit();
            this.salvarGrupoUsuario(usuario);
        } catch (Exception e) {
            //Caso aconteça alguma exeção é feito um rollback para o banco de
            //dados retornar ao seu estado anterior.
            if (conexao != null) {
                conexao.rollback();
            }
            throw e;
        } finally {
            //Todo objeto que referencie o banco de dados deve ser fechado
            BancoDadosUtil.fecharChamadasBancoDados(conexao, comando);
        }
    }

    public Usuario buscarUsuarioPorLogin(String login) throws SQLException {
        Connection conexao = null;
        PreparedStatement comando = null;
        ResultSet resultado = null;
        Usuario usuario = new Usuario();
        try {
            //Recupera a conexão
            conexao = BancoDadosUtil.getConnection();
            //Cria o comando de consulta dos dados
            comando = conexao.prepareStatement(SQL_SELECT_POR_LOGIN);

            comando.setString(1, login);
            //Executa o comando e obtém o resultado da consulta
            resultado = comando.executeQuery();
            //O método next retornar boolean informando se existe um próximo
            //elemento para iterar
            while (resultado.next()) {
                usuario = this.buscarUsuarioPorLogin(resultado);
            }
        } finally {
            //Todo objeto que referencie o banco de dados deve ser fechado
            BancoDadosUtil.fecharChamadasBancoDados(conexao, comando, resultado);
        }
        return usuario;
    }

    private Usuario buscarUsuarioPorLogin(ResultSet resultado) throws SQLException {
        Usuario u = new Usuario();
        u.setId(resultado.getInt(1));
        u.setNome(resultado.getString(2));
        u.setLogin(resultado.getString(3));
        u.setSenha(resultado.getString(4));
        return u;
    }

    private void salvarGrupoUsuario(Usuario usuarioEmEdicao) throws SQLException {
        Connection conexao = null;
        PreparedStatement comando = null;

        try {
            //Recupera a conexão
            conexao = BancoDadosUtil.getConnection();
            //Cria o comando de inserir dados
            comando = conexao.prepareStatement(SQL_INSERT_GRUPOTRABALHO);
            //Atribui os parâmetros (Note que no BD o index inicia por 1)

            Usuario usuario = new Usuario();
            usuario = this.buscarUsuarioPorLogin(usuarioEmEdicao.getLogin());
            if (!usuarioEmEdicao.getGrupoUsuario().isEmpty()) {
                for (String nome : usuarioEmEdicao.getGrupoUsuario()) {
                    comando.setInt(1, usuario.getId());
                    comando.setString(2, nome);
                    comando.execute();
                }
            }

            //Persiste o comando no banco de dados
            conexao.commit();

            //System.out.println("Manobra cadastrada com sucesso!");
        } catch (Exception e) {
            //Caso aconteça alguma exeção é feito um rollback para o banco de
            //dados retornar ao seu estado anterior.
            if (conexao != null) {
                conexao.rollback();
            }
            throw e;
        } finally {
            //Todo objeto que referencie o banco de dados deve ser fechado
            BancoDadosUtil.fecharChamadasBancoDados(conexao, comando);
        }
    }

}
