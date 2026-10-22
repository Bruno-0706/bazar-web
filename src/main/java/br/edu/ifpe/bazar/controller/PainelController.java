package br.edu.ifpe.bazar.controller;

import br.edu.ifpe.bazar.model.UsuarioAutenticado;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.edu.ifpe.bazar.model.Lote;
import br.edu.ifpe.bazar.service.LoteService;
import java.sql.SQLException;
import java.util.List;
import java.time.LocalDate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import br.edu.ifpe.bazar.model.Produto;
import java.util.Objects;

@Controller
public class PainelController {

	private final LoteService loteService = new LoteService();
	
	@GetMapping("/fiscalizador/lotes")
	public String painelFiscalizador(
	        HttpSession sessao, Model modelo) {

	    UsuarioAutenticado usuario =
	            (UsuarioAutenticado) sessao.getAttribute("usuario");

	    if (usuario == null || !usuario.isFiscalizador()) {
	        return "redirect:/login";
	    }

	    try {
	        List<Lote> lotes =
	                loteService.listarLotesDoFiscalizador(usuario.getId());

	        modelo.addAttribute("usuario", usuario);
	        modelo.addAttribute("lotes", lotes);

	    } catch (SQLException e) {
	        modelo.addAttribute(
	                "erro", "Não foi possível carregar os lotes.");
	        modelo.addAttribute("lotes", List.of());
	        modelo.addAttribute("usuario", usuario);
	    }

	    return "fiscalizador/lotes";
	}

	@GetMapping("/donatario/lotes")
	public String listarLotesDoDonatario(
	        HttpSession sessao,
	        Model model) {

	    UsuarioAutenticado usuario =
	            (UsuarioAutenticado) sessao.getAttribute("usuario");

	    if (usuario == null || !usuario.isDonatario()) {
	        return "redirect:/login";
	    }

	    try {
	        model.addAttribute("usuario", usuario);
	        model.addAttribute(
	                "lotes",
	                loteService.listarLotesRecebidos(usuario.getId())
	        );

	        return "donatario/lotes";

	    } catch (SQLException e) {
	        model.addAttribute("usuario", usuario);
	        model.addAttribute(
	                "lotes",
	                java.util.Collections.emptyList()
	        );
	        model.addAttribute(
	                "erro",
	                "Não foi possível carregar os lotes recebidos."
	        );

	        return "donatario/lotes";
	    }
	}
    
    @GetMapping("/fiscalizador/lotes/novo")
    public String exibirFormularioLote(
    		HttpSession sessao, Model modelo) {
    	
    	UsuarioAutenticado usuario =
    			(UsuarioAutenticado) sessao.getAttribute("usuario");
    	
    	if (usuario == null || !usuario.isFiscalizador()) {
    		return "redirect:/login";
    	}
    	
    	modelo.addAttribute("usuario", usuario);
    	return "fiscalizador/novo-lote";
    }
    
    @PostMapping("/fiscalizador/lotes/novo")
    public String cadastrarLote(
    		@RequestParam(required = false) String dataEntrega,
    		@RequestParam(required = false) String observacao,
    		HttpSession sessao,
    		Model modelo) {
    	
    	UsuarioAutenticado usuario =
    			(UsuarioAutenticado) sessao.getAttribute("usuario");
    	
    	if (usuario == null || !usuario.isFiscalizador()) {
    		return "redirect:/login";
    	}
    	
    	try {
    		Lote lote = new Lote();
    		
    		if (dataEntrega != null && !dataEntrega.isBlank()) {
    			lote.setDataEntrega(LocalDate.parse(dataEntrega));
    		}
    		
    		lote.setObservacao(observacao);
    		lote.setFiscalizadorId(usuario.getId());
    		
    		loteService.cadastrarLote(lote);
    		
    		return "redirect:/fiscalizador/lotes";
    		
    	} catch (java.time.format.DateTimeParseException e) {
    		modelo.addAttribute("erro", "A data informada é inválida.");
    	} catch (SQLException | IllegalArgumentException e) {
    		modelo.addAttribute("erro", "Não foi possível cadastrar o lote.");
    	}
    	
    	modelo.addAttribute("usuario", usuario);
    	return "fiscalizador/novo-lote";
    }
    
    @GetMapping("/fiscalizador/lotes/{id}/produtos/novo")
    public String exibirFormularioProduto(
            @org.springframework.web.bind.annotation.PathVariable int id,
            HttpSession sessao,
            Model modelo) {

        UsuarioAutenticado usuario =
                (UsuarioAutenticado) sessao.getAttribute("usuario");

        if (usuario == null || !usuario.isFiscalizador()) {
            return "redirect:/login";
        }

        try {
            boolean lotePertenceAoFiscalizador =
                    loteService.listarLotesDoFiscalizador(usuario.getId())
                            .stream()
                            .anyMatch(lote -> lote.getId() == id
                                    && "DISPONIVEL".equals(lote.getStatus()));

            if (!lotePertenceAoFiscalizador) {
                return "redirect:/fiscalizador/lotes";
            }

            modelo.addAttribute("usuario", usuario);
            modelo.addAttribute("loteId", id);
            return "fiscalizador/novo-produto";

        } catch (SQLException e) {
            return "redirect:/fiscalizador/lotes";
        }
    }

    @PostMapping("/fiscalizador/lotes/{id}/produtos/novo")
    public String cadastrarProduto(
            @org.springframework.web.bind.annotation.PathVariable int id,
            @RequestParam String nome,
            @RequestParam(required = false) String descricao,
            HttpSession sessao,
            Model modelo) {

        UsuarioAutenticado usuario =
                (UsuarioAutenticado) sessao.getAttribute("usuario");

        if (usuario == null || !usuario.isFiscalizador()) {
            return "redirect:/login";
        }

        try {
            boolean lotePertenceAoFiscalizador =
                    loteService.listarLotesDoFiscalizador(usuario.getId())
                            .stream()
                            .anyMatch(lote -> lote.getId() == id
                                    && "DISPONIVEL".equals(lote.getStatus()));

            if (!lotePertenceAoFiscalizador) {
                return "redirect:/fiscalizador/lotes";
            }

            Produto produto = new Produto();
            produto.setNome(nome);
            produto.setDescricao(descricao);
            produto.setLoteId(id);

            loteService.cadastrarProduto(produto);

            return "redirect:/fiscalizador/lotes";

        } catch (SQLException | IllegalArgumentException e) {
            modelo.addAttribute("usuario", usuario);
            modelo.addAttribute("loteId", id);
            modelo.addAttribute("erro", "Não foi possível cadastrar o produto.");
            return "fiscalizador/novo-produto";
        }
    }
    
    @GetMapping("/fiscalizador/lotes/{id}/produtos")
    public String visualizarProdutos(
            @org.springframework.web.bind.annotation.PathVariable("id") int id,
            HttpSession sessao,
            Model model) {

        UsuarioAutenticado usuario =
                (UsuarioAutenticado) sessao.getAttribute("usuario");

        if (usuario == null || !usuario.isFiscalizador()) {
            return "redirect:/login";
        }

        try {
            // fiscalizador só pode acessar os próprios lotes
            Lote lote = loteService.listarLotesDoFiscalizador(usuario.getId())
                    .stream()
                    .filter(l -> l.getId() == id)
                    .findFirst()
                    .orElse(null);

            if (lote == null) {
                return "redirect:/fiscalizador/lotes";
            }

            model.addAttribute("usuario", usuario);
            model.addAttribute("lote", lote);
            model.addAttribute("produtos", loteService.listarProdutos(id));

            return "fiscalizador/produtos";

        } catch (SQLException e) {
            model.addAttribute("erro",
                    "Não foi possível carregar os produtos do lote.");
            model.addAttribute("usuario", usuario);
            model.addAttribute("loteId", id);
            model.addAttribute("produtos", java.util.Collections.emptyList());

            //return "fiscalizador/produtos"; maybe
            return "redirect:/fiscalizador/lotes";
        }
    }
    

	@GetMapping("/donatario/lotes/{id}/produtos")
	public String visualizarProdutosDoDonatario(@org.springframework.web.bind.annotation.PathVariable("id") int id,
			HttpSession sessao, Model model) {

		UsuarioAutenticado usuario = (UsuarioAutenticado) sessao.getAttribute("usuario");

		if (usuario == null || !usuario.isDonatario()) {
			return "redirect:/login";
		}

		try {
			Lote lote = loteService.listarLotesRecebidos(usuario.getId()).stream().filter(l -> l.getId() == id)
					.findFirst().orElse(null);

			if (lote == null) {
				return "redirect:/donatario/lotes";
			}

			model.addAttribute("usuario", usuario);
			model.addAttribute("lote", lote);
			model.addAttribute("produtos", loteService.listarProdutos(id));

			return "donatario/produtos";

		} catch (SQLException e) {
			return "redirect:/donatario/lotes";
		}
	}
	

	@GetMapping("/fiscalizador/lotes/{id}/doar")
	public String exibirFormularioDoacao(@org.springframework.web.bind.annotation.PathVariable("id") int id,
			HttpSession sessao, Model model) {

		UsuarioAutenticado usuario = (UsuarioAutenticado) sessao.getAttribute("usuario");

		if (usuario == null || !usuario.isFiscalizador()) {
			return "redirect:/login";
		}

		try {
			Lote lote = loteService.listarLotesDoFiscalizador(usuario.getId()).stream().filter(l -> l.getId() == id)
					.findFirst().orElse(null);

			if (lote == null || !"DISPONIVEL".equals(lote.getStatus())) {
				return "redirect:/fiscalizador/lotes";
			}

			model.addAttribute("usuario", usuario);
			model.addAttribute("lote", lote);
			model.addAttribute("donatarios", loteService.listarDonatarios());

			return "fiscalizador/doar-lote";

		} catch (SQLException e) {
			return "redirect:/fiscalizador/lotes";
		}
	}

	@PostMapping("/fiscalizador/lotes/{id}/doar")
	public String confirmarDoacao(@org.springframework.web.bind.annotation.PathVariable("id") int id,
			@org.springframework.web.bind.annotation.RequestParam("donatarioId") int donatarioId, HttpSession sessao,
			Model model) {

		UsuarioAutenticado usuario = (UsuarioAutenticado) sessao.getAttribute("usuario");

		if (usuario == null || !usuario.isFiscalizador()) {
			return "redirect:/login";
		}

		try {
			Lote lote = loteService.listarLotesDoFiscalizador(usuario.getId()).stream().filter(l -> l.getId() == id)
					.findFirst().orElse(null);

			if (lote == null || !"DISPONIVEL".equals(lote.getStatus())) {
				return "redirect:/fiscalizador/lotes";
			}

			loteService.doarLote(id, donatarioId);

			return "redirect:/fiscalizador/lotes";

		} catch (SQLException | IllegalArgumentException e) {
			try {
				Lote lote = loteService.listarLotesDoFiscalizador(usuario.getId()).stream().filter(l -> l.getId() == id)
						.findFirst().orElse(null);

				if (lote == null) {
					return "redirect:/fiscalizador/lotes";
				}

				model.addAttribute("usuario", usuario);
				model.addAttribute("lote", lote);
				model.addAttribute("donatarios", loteService.listarDonatarios());
				model.addAttribute("erro", "Não foi possível realizar a doação. Verifique os dados e tente novamente.");

				return "fiscalizador/doar-lote";

			} catch (SQLException ex) {
				return "redirect:/fiscalizador/lotes";
			}
		}
	}

    
}
