package br.edu.ifpe.bazar.service;

import br.edu.ifpe.bazar.model.OrgaoDonatario;
import br.edu.ifpe.bazar.model.OrgaoFiscalizador;
import br.edu.ifpe.bazar.model.UsuarioAutenticado;
import br.edu.ifpe.bazar.repository.OrgaoDonatarioRepository;
import br.edu.ifpe.bazar.repository.OrgaoFiscalizadorRepository;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;

public class AutenticacaoService {

	private final OrgaoFiscalizadorRepository fiscalizadorRepository =
			new OrgaoFiscalizadorRepository();
	
	private final OrgaoDonatarioRepository donatarioRepository =
			new OrgaoDonatarioRepository();
	
	private final BCryptPasswordEncoder passwordEncoder =
			new BCryptPasswordEncoder();
	
	public UsuarioAutenticado autenticar(
			String email, String senha) throws SQLException {
		
		if (email == null || senha == null
				|| email.isBlank() || senha.isBlank()) {
			return null;
		}
		
		email = email.trim();
		
		OrgaoFiscalizador fiscalizador =
				fiscalizadorRepository.buscarPorEmail(email);
		
		OrgaoDonatario donatario =
				donatarioRepository.buscarPorEmail(email);
		
		if (fiscalizador != null && donatario != null) {
			return null;
		}
		
		if (fiscalizador != null
				&& passwordEncoder.matches(
						senha, fiscalizador.getSenha())) {
			
			return new UsuarioAutenticado(
					fiscalizador.getId(),
					fiscalizador.getNome(),
					fiscalizador.getEmail(),
					"FISCALIZADOR"
				);
		}
		
		if (donatario != null
				&& passwordEncoder.matches(
						senha, donatario.getSenha())) {
			
			return new UsuarioAutenticado(
					donatario.getId(),
					donatario.getNome(),
					donatario.getEmail(),
					"DONATARIO"
				);
		}
		
		return null;
	}
}
