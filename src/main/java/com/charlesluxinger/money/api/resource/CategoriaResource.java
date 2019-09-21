package com.charlesluxinger.money.api.resource;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.charlesluxinger.money.api.event.InsertResourceEvent;
import com.charlesluxinger.money.api.model.Categoria;
import com.charlesluxinger.money.api.repository.CategoriaRepository;

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private ApplicationEventPublisher publisher;

	// @CrossOrigin(maxAge = 10, origins = { "http://localhost:8000" })
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
	public List<Categoria> findAll() {
		return categoriaRepository.findAll();
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and #oauth2.hasScope('write')")
	public ResponseEntity<Categoria> insert(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
		Categoria categoriaInserted = categoriaRepository.save(categoria);

		publisher.publishEvent(new InsertResourceEvent(this, response, categoriaInserted.getId()));

		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaInserted);
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
	public ResponseEntity<Categoria> findById(@PathVariable Long id) {
		Optional<Categoria> categoria = this.categoriaRepository.findById(id);

		return (categoria.isPresent() ? ResponseEntity.ok(categoria.get()) : ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_REMOVER_CATEGORIA') and #oauth2.hasScope('write')")
	public void remove(@PathVariable long id) {
		categoriaRepository.deleteById(id);
	}
}
