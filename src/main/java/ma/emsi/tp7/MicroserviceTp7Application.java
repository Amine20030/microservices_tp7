package ma.emsi.tp7;

import ma.emsi.tp7.entities.Compte;
import ma.emsi.tp7.entities.TypeCompte;
import ma.emsi.tp7.repositories.CompteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class MicroserviceTp7Application {

    public static void main(String[] args) {
        SpringApplication.run(MicroserviceTp7Application.class, args);
    }

    @Bean
    CommandLineRunner start(CompteRepository compteRepository) {
        return args -> {
            compteRepository.save(new Compte(null, Math.random() * 9000, new Date(), TypeCompte.EPARGNE));
            compteRepository.save(new Compte(null, Math.random() * 9000, new Date(), TypeCompte.COURANT));
            compteRepository.save(new Compte(null, Math.random() * 9000, new Date(), TypeCompte.EPARGNE));

            System.out.println("================================================================================");
            System.out.println("  TP 7 : INITIALISATION DES COMPTES BANCAIRES (JAX-RS / JERSEY)");
            System.out.println("================================================================================");
            compteRepository.findAll().forEach(compte -> {
                System.out.println("-> Compte ID " + compte.getId() + " | Solde: " + String.format("%.2f", compte.getSolde()) + " DH | Type: " + compte.getType());
            });
            System.out.println("================================================================================");
        };
    }
}
