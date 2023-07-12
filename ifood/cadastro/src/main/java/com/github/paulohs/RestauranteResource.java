package com.github.paulohs;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;

@Path("/restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestauranteResource {

    @GET
    public List<Restaurante> listaRestaurantes() {

        List<Restaurante> restaurantes = Restaurante.listAll();
        return restaurantes;
    }

    @POST
    @Transactional
    public Response adicionar(Restaurante dto) {
        dto.persist(dto);
        return Response.status(Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public void atualizar(@PathParam("id") Long id, Restaurante dto) {
        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(id);
        if (restauranteOp.isEmpty()) {
            throw new NotFoundException();
        }
        Restaurante restaurante = restauranteOp.get();

        restaurante.nome = dto.nome;
        restaurante.persist();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(id);
        restauranteOp.ifPresentOrElse(Restaurante::delete, () -> {
            throw new NotFoundException();
        });
    }


    //    PRATOS 
// TODO: 10/07/2023 refatorar o nivel de acesso das propriedades utilizando o Lombok
    @POST
    @Transactional
    @Tag(name = "Prato")
    @Path("{idRestaurante}/pratos")
    public Response adicionarPrato(@PathParam("idRestaurante") Long idRestaurante, Prato dtoPrato) {
        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
        if (restauranteOp.isEmpty()) {
            throw new NotFoundException("Restaurante não encontrado");
        }
        Prato prato = new Prato();
        prato.nome = dtoPrato.nome;
        prato.descricao = dtoPrato.descricao;
        prato.preco = dtoPrato.preco;
        prato.restaurante = restauranteOp.get();
        prato.persist();
        return Response.status(Status.CREATED).build();
    }

    @GET
    @Tag(name = "Prato")
    @Path("{idRestaurante}/pratos")
    public List<Restaurante> buscarPratos(@PathParam("idRestaurante") Long idRestaurante) {
        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
        if (restauranteOp.isEmpty()) {
            throw new NotFoundException("Restaurante não encontrado");
        }
        return Prato.list("restaurante", restauranteOp.get());
    }

    @PUT
    @Transactional
    @Tag(name = "Prato")
    @Path("{idRestaurante}/pratos/{idPrato}")
    public void atualizarPrato(@PathParam("idRestaurante") Long idRestaurante, @PathParam("idPrato") Long idPrato, Prato dtoPrato) {
        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
        if (restauranteOp.isEmpty()) {
            throw new NotFoundException("Restaurante não encontrado");
        }

        Optional<Prato> pratoOp = Prato.findByIdOptional(idPrato);
        if (pratoOp.isEmpty()) {
            throw new NotFoundException("Prato não existe");
        }

        Prato prato = pratoOp.get();
        prato.preco = dtoPrato.preco;
        prato.persist();
    }

    @DELETE
    @Transactional
    @Tag(name = "Prato")
    @Path("{idRestaurante}/pratos/{idPrato}")
    public void deletarPrato(@PathParam("idRestaurante") Long idRestaurante, @PathParam("idPrato") Long idPrato) {
        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
        if (restauranteOp.isEmpty()) {
            throw new NotFoundException("Restaurante não encontrado");
        }
        Optional<Prato> pratoOp = Prato.findByIdOptional(idPrato);
        pratoOp.ifPresentOrElse(Prato::delete, () -> {
            throw new NotFoundException();
        });
    }

}
