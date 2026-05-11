package test;

import java.util.List;
import org.junit.Assert;
import ro.uaic.feaa.psi.comirex.forms.ReceptieFormCtrl;
import ro.uaic.feaa.psi.comirex.forms.ReceptieFormData;
import ro.uaic.feaa.psi.comirex.model.entities.*;
import ro.uaic.feaa.psi.comirex.model.repository.MasterRepository;

public class TestScenariul3Stornare {

    public static void main(String[] args) {
        pregatesteDateDeTest();
        System.out.println("--- Rulare Scenariul 3: Stornare (document original selectat automat) ---");

        ReceptieFormCtrl form = new ReceptieFormCtrl();
        form.getFormData().setOperatieSelectata(ReceptieFormData.STORNARE);
        form.documentNou();

        // Verificam ca tipul documentului s-a setat corect automat
        Assert.assertEquals("Tipul documentului trebuie sa fie STORNARE",
                ReceptieFormCtrl.STORNARE,
                form.getFormData().getDocumentCurent().getTipDocument());

        form.getFormData().getDocumentCurent().setNumarDocument("STOR-001");

        // Incarcam FCT-001 din BD — creata fie de Scenariul 1, fie de pregatesteDateDeTest()
        List<DocInsotitor> documente = form.getFormData().getDocRepo().findDocInsotitoriAll();
        DocInsotitor docOriginal = documente.stream()
                .filter(d -> "FCT-001".equals(d.getNumarDocument()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("FCT-001 nu a fost gasita in BD!"));

        // Initializeaza stornarea — preia automat furnizorul, gestiunea si liniile cu cantitati negate
        // Utilizatorul NU scrie manual valori negative
        form.initiazaStornareDocument(docOriginal);

        // Verificam ca linia a fost copiata cu cantitate negativa
        LinieIntrare linie = form.getFormData().getReceptieSelectata().getLiniiIntrare().get(0);
        Assert.assertTrue("Cantitatea stornata trebuie sa fie negativa", linie.getCantitate() < 0);

        // Salvam documentul de stornare in baza de date
        form.salveazaModificariDocument();

        Assert.assertNotNull("Salvarea stornarii a esuat!", form.getFormData().getDocumentCurent().getId());
        System.out.println("  OK - Documentul de Stornare a fost salvat cu succes in BD cu ID: "
                + form.getFormData().getDocumentCurent().getId());
    }

    /**
     * Pregateste datele de test necesare Scenariului 3.
     *
     * Testele ruleaza intotdeauna in ordine (1 → 2 → 3 → 4), deci FCT-001
     * este garantat salvata in BD de catre TestScenariul1Factura.
     * Aici se asigura doar datele master (furnizori, gestiuni, produse).
     */
    private static void pregatesteDateDeTest() {
        MasterRepository repo = new MasterRepository();

        if (repo.findFurnizoriAll().isEmpty()) {
            repo.beginTransaction();
            for (int i = 0; i < 3; i++) {
                Furnizor f = new Furnizor();
                f.setCodFurnizor("F" + (1000 + i));
                f.setNumeFurnizor("Furnizor Test " + (i + 1));
                f.setCui("RO" + (10000000 + i));
                f.setAdresa("Adresa " + (1000 + i));
                f.setSold(0.0);
                repo.addFurnizor(f);
            }
            repo.commitTransaction();
        }

        if (repo.findGestiuniAll().isEmpty()) {
            repo.beginTransaction();
            Gestiune g1 = new Gestiune();
            g1.setCodGestiune("G001");
            g1.setDenumireGestiune("Depozit Central");
            g1.setNumeGestionar("Popescu Ion");
            g1.setAdresa("Iasi, str. Pacurari 1");
            repo.addGestiune(g1);
            Gestiune g2 = new Gestiune();
            g2.setCodGestiune("G002");
            g2.setDenumireGestiune("Magazin Centru");
            g2.setNumeGestionar("Ionescu Maria");
            g2.setAdresa("Iasi, str. Stefan cel Mare 5");
            repo.addGestiune(g2);
            repo.commitTransaction();
        }

        if (repo.findProduseAll().isEmpty()) {
            repo.beginTransaction();
            Produs p1 = new Produs();
            p1.setCodProdus("M-101");
            p1.setDenumire("Cuie zincate 3x60 mm");
            p1.setUm("kg");
            p1.setPretVanzare(11.5);
            p1.setCostMediuPonderat(8.5);
            repo.addProdus(p1);
            Produs p2 = new Produs();
            p2.setCodProdus("M-205");
            p2.setDenumire("Suruburi autofirante 4x40");
            p2.setUm("buc");
            p2.setPretVanzare(0.45);
            p2.setCostMediuPonderat(0.35);
            repo.addProdus(p2);
            Produs p3 = new Produs();
            p3.setCodProdus("S-312");
            p3.setDenumire("Surubelnita plata 6x150mm");
            p3.setUm("buc");
            p3.setPretVanzare(15.0);
            p3.setCostMediuPonderat(12.0);
            repo.addProdus(p3);
            repo.commitTransaction();
        }

    }
}
