package test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

/**
 * Utilitara pentru resetarea bazei de date Comirex.
 *
 * Ruleaza aceasta clasa (main) pentru a sterge toate datele si a recrea
 * schema goala. Nu modifica persistence.xml — proprietatea "create" este
 * transmisa dinamic si suprascrie "update" doar pentru aceasta rulare.
 *
 * Dupa reset, rulati testele in ordine: Test1 → Test2 → Test3 → Test4.
 */
public class ResetDatabase {

    public static void main(String[] args) {
        System.out.println("=== RESET BAZA DE DATE COMIREX ===");
        System.out.println("Se sterg toate tabelele si se recreeaza schema goala...");

        // Suprascrie hbm2ddl.auto cu "create" doar pentru aceasta rulare
        // "create" = DROP toate tabelele existente + CREATE tabele goale
        Map<String, String> props = new HashMap<>();
        props.put("hibernate.hbm2ddl.auto", "create");

        EntityManagerFactory emf = null;
        try {
            emf = Persistence.createEntityManagerFactory("ComirexPersistenceUnit", props);
            // Simpla initializare a EMF-ului este suficienta — Hibernate executa
            // DROP + CREATE automat la pornire cu hbm2ddl.auto=create
            System.out.println("Schema recreata cu succes. Baza de date este goala.");
            System.out.println("Puteti rula acum testele in ordine: Test1 → Test2 → Test3 → Test4.");
        } catch (Exception e) {
            System.err.println("Eroare la reset: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (emf != null && emf.isOpen()) {
                emf.close();
            }
        }

        System.out.println("=== RESET COMPLET ===");
    }
}
