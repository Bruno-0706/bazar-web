package br.edu.ifpe.bazar.controller;

import br.edu.ifpe.bazar.model.UsuarioAutenticado;
import br.edu.ifpe.bazar.service.AutenticacaoService;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

	private final AutenticacaoService autenticacaoService = 
			new AutenticacaoService();
	
	@GetMapping("/")
	public String inicio() {
		return "redirect:/login";
	}
	
	@GetMapping("/login")
	public String exibirLogin(HttpSession sessao) {
		UsuarioAutenticado usuario =
				(UsuarioAutenticado) sessao.getAttribute("usuario");
		
		if (usuario != null) {
			return usuario.isFiscalizador()
					? "redirect:/fiscalizador/lotes"
					: "redirect:/donatario/lotes";
		}
		
		return "login";
	}
	
	@PostMapping("/login")
	public String autenticar(
			@RequestParam String email,
			@RequestParam String senha,
			HttpSession sessao,
			Model modelo) {
		
		try {
			UsuarioAutenticado usuario =
					autenticacaoService.autenticar(email, senha);
			
			if (usuario == null) {
				modelo.addAttribute(
						"erro", "E-mail ou senha inválidos.");
				return "login";
			}
			
			sessao.setAttribute("usuario", usuario);
			
			if (usuario.isFiscalizador()) {
				return "redirect:/fiscalizador/lotes";
			}
			
			return "redirect:/donatario/lotes";
			
		} catch (SQLException e) {
			modelo.addAttribute(
					"erro", "Não foi possível realizar login. Tente novamente.");
				return "login";			
		}
	}
	
	@PostMapping("/logout")
	public String sair(HttpSession sessao) {
		sessao.invalidate();
		return "redirect:/login";
	}
}
