
package br.edu.ifpe.bazar;

import br.edu.ifpe.bazar.model.OrgaoFiscalizador;
import br.edu.ifpe.bazar.model.UsuarioAutenticado;
import br.edu.ifpe.bazar.repository.OrgaoFiscalizadorRepository;
import br.edu.ifpe.bazar.service.AutenticacaoService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TesteAutenticacao {

    public static void main(String[] args) throws Exception {

        String email = "pf.login@bazar.com";
        String senha = "Teste@123";

        OrgaoFiscalizadorRepository repositorio =
            new OrgaoFiscalizadorRepository();

        // Cadastrar o usuário somente se ele ainda não existir.
        if (repositorio.buscarPorEmail(email) == null) {
            BCryptPasswordEncoder encoder =
                new BCryptPasswordEncoder();

            OrgaoFiscalizador orgao =
                new OrgaoFiscalizador();

            orgao.setNome("Polícia Federal - Teste de Login");
            orgao.setDescricao("Usuário para teste de autenticação");
            orgao.setEmail(email);
            orgao.setSenha(encoder.encode(senha));

            repositorio.criar(orgao);
        }

        AutenticacaoService autenticacao =
            new AutenticacaoService();

        UsuarioAutenticado usuario =
            autenticacao.autenticar(email, senha);

        if (usuario != null) {
            System.out.println("Login realizado com sucesso!");
            System.out.println("Nome: " + usuario.getNome());
            System.out.println("Tipo: " + usuario.getTipo());
        } else {
            System.out.println("Falha na autenticação.");
        }

        UsuarioAutenticado senhaErrada =
            autenticacao.autenticar(email, "senha-errada");

        System.out.println(
            "Senha incorreta rejeitada: "
            + (senhaErrada == null)
        );
    }
}
