package com.github.paulohs;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

//@Path("/pratos")
//@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_JSON)
public class PratoResource {

    @GET
    public List<Prato> listaPratos() {
        return Prato.listAll();
    }

    @POST
    @Transactional
    public Response adicionar(Prato dto) {
        dto.persist();
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public void atualizar(@PathParam("id") long id, Prato dto) {
        Optional<Prato> pratoOp = Prato.findByIdOptional(id);
        if (pratoOp.isEmpty()) {
            throw new NotFoundException();
        }
        Prato prato = pratoOp.get();

        prato.nome = dto.nome;
        prato.persist();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public void delete(@PathParam("id") Long id) {
        Optional<Prato> pratroOp = Prato.findByIdOptional(id);
        pratroOp.ifPresentOrElse(Prato::delete, () -> {
            throw new NotFoundException();
        });
    }
}
