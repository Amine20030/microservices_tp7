package ma.emsi.tp7.api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ma.emsi.tp7.entities.Compte;
import ma.emsi.tp7.repositories.CompteRepository;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
@Path("/banque")
public class CompteRestJaxRSAPI {

    private final CompteRepository compteRepository;

    public CompteRestJaxRSAPI(CompteRepository compteRepository) {
        this.compteRepository = compteRepository;
    }

    @GET
    @Path("/comptes")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getAllComptes() {
        List<Compte> comptes = compteRepository.findAll();
        return Response.ok(comptes).build();
    }

    @GET
    @Path("/comptes/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response getCompteById(@PathParam("id") Long id) {
        Optional<Compte> compte = compteRepository.findById(id);
        return compte.map(c -> Response.ok(c).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Path("/comptes")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response createCompte(Compte compte) {
        if (compte.getDateCreation() == null) {
            compte.setDateCreation(new Date());
        }
        Compte saved = compteRepository.save(compte);
        return Response.status(Response.Status.CREATED).entity(saved).build();
    }

    @PUT
    @Path("/comptes/{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response updateCompte(@PathParam("id") Long id, Compte compte) {
        return compteRepository.findById(id).map(existing -> {
            existing.setSolde(compte.getSolde());
            if (compte.getType() != null) {
                existing.setType(compte.getType());
            }
            if (compte.getDateCreation() != null) {
                existing.setDateCreation(compte.getDateCreation());
            }
            Compte updated = compteRepository.save(existing);
            return Response.ok(updated).build();
        }).orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/comptes/{id}")
    public Response deleteCompte(@PathParam("id") Long id) {
        if (compteRepository.existsById(id)) {
            compteRepository.deleteById(id);
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
