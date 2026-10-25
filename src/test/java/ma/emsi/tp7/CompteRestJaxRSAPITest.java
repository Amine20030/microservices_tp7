package ma.emsi.tp7;

import ma.emsi.tp7.entities.Compte;
import ma.emsi.tp7.entities.TypeCompte;
import ma.emsi.tp7.repositories.CompteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompteRestJaxRSAPITest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CompteRepository compteRepository;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/banque/comptes";
    }

    private Long sampleId;

    @BeforeEach
    void setUp() {
        compteRepository.deleteAll();
        Compte c1 = compteRepository.save(new Compte(null, 5000.0, new Date(), TypeCompte.COURANT));
        compteRepository.save(new Compte(null, 12000.0, new Date(), TypeCompte.EPARGNE));
        sampleId = c1.getId();
    }

    @Test
    @DisplayName("1. Récupération de tous les comptes au format JSON")
    void testGetAllComptesJson() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.GET,
                entity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("5000");
        assertThat(response.getBody()).contains("COURANT");
    }

    @Test
    @DisplayName("2. Récupération de tous les comptes au format XML")
    void testGetAllComptesXml() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.GET,
                entity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType().toString()).contains("application/xml");
    }

    @Test
    @DisplayName("3. Consultation d'un compte par son ID")
    void testGetCompteById() {
        ResponseEntity<Compte> response = restTemplate.getForEntity(
                getBaseUrl() + "/" + sampleId,
                Compte.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(sampleId);
        assertThat(response.getBody().getType()).isEqualTo(TypeCompte.COURANT);
    }

    @Test
    @DisplayName("4. Création d'un nouveau compte bancaire (POST)")
    void testCreateCompte() {
        Compte newCompte = new Compte(null, 7500.0, new Date(), TypeCompte.EPARGNE);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Compte> request = new HttpEntity<>(newCompte, headers);

        ResponseEntity<Compte> response = restTemplate.postForEntity(
                getBaseUrl(),
                request,
                Compte.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getSolde()).isEqualTo(7500.0);
    }

    @Test
    @DisplayName("5. Suppression d'un compte existant (DELETE)")
    void testDeleteCompte() {
        ResponseEntity<Void> response = restTemplate.exchange(
                getBaseUrl() + "/" + sampleId,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(compteRepository.findById(sampleId)).isEmpty();
    }
}
