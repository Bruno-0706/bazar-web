
package br.edu.ifpe.bazar;

import br.edu.ifpe.bazar.model.OrgaoDonatario;
import br.edu.ifpe.bazar.repository.OrgaoDonatarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TesteOrgaoDonatario {

    public static void main(String[] args) throws Exception {
        BCryptPasswordEncoder encoder =
                new BCryptPasswordEncoder();

        OrgaoDonatario donatario = new OrgaoDonatario();

        donatario.setNome("Donatário de Teste");
        donatario.setEndereco("Endereço de teste");
        donatario.setTelefone("(81) 99999-9999");
        donatario.setDescricao("Instituição utilizada para testes");
        donatario.setEmail("donatario.teste@bazar.com");
        donatario.setSenha(encoder.encode("Donatario@123"));

        OrgaoDonatarioRepository repository =
                new OrgaoDonatarioRepository();

        repository.criar(donatario);

        System.out.println("Donatário cadastrado com sucesso.");
    }
}