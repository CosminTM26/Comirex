package test;

import ro.uaic.feaa.psi.comirex.model.entities.*;
import ro.uaic.feaa.psi.comirex.model.repository.DocumentRepository;
import ro.uaic.feaa.psi.comirex.model.repository.MasterRepository;
import java.util.*;

public class test {

    public static void main(String[] args) {
        MasterRepository masterRepo = new MasterRepository();
        DocumentRepository docRepo = new DocumentRepository();

        try {
            masterRepo.beginTransaction(); // Tranzacție pentru date de bază [cite: 121, 128]

            // 1. POPULARE GESTIUNI [cite: 548, 737-741]
            List<Gestiune> gestiuni = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                Gestiune g = new Gestiune();
                g.setCodGestiune("G0" + i);
                g.setDenumireGestiune("Depozit " + (i <= 5 ? "Central " : "Regional ") + i);
                g.setNumeGestionar("Gestionar " + i);
                g.setAdresa("Adresa Gestiune " + i);
                masterRepo.addGestiune(g);
                gestiuni.add(g);
            }

            // 2. POPULARE FURNIZORI [cite: 548, 672-688]
            List<Furnizor> furnizori = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                Furnizor f = new Furnizor();
                f.setCodFurnizor("F" + (100 + i));
                f.setNumeFurnizor("Furnizor Materiale Constructii " + i);
                f.setCui("RO" + (1000000 + i));
                f.setAdresa("Strada Furnizorului nr. " + i);
                f.setSold(0.0);
                masterRepo.addFurnizor(f);
                furnizori.add(f);
            }

            // 3. POPULARE PRODUSE [cite: 548, 757-765]
            List<Produs> produse = new ArrayList<>();
            String[] categorii = {"Feronerie", "Scule", "Materiale", "Finisaje"};
            for (int i = 1; i <= 10; i++) {
                Produs p = new Produs();
                p.setCodProdus("P-" + (100 + i));
                p.setDenumire("Articol Tehnic " + i);
                p.setUm(i % 3 == 0 ? "kg" : "buc");
                p.setCategorie(categorii[i % 4]);
                p.setPretVanzare(20.0 + i * 5);
                p.setCostMediuPonderat(15.0 + i * 4);
                p.setStocMinim(10.0);
                masterRepo.addProdus(p);
                produse.add(p);
            }

            masterRepo.commitTransaction(); // Salvăm nomenclatoarele [cite: 127]

            // 4. POPULARE DOCUMENTE ȘI RECEPȚII [cite: 64, 774]
            docRepo.beginTransaction();
            for (int i = 0; i < 10; i++) {
                // Document Însoțitor [cite: 1887]
                DocInsotitor di = new DocInsotitor();
                di.setTipDocument(i % 2 == 0 ? "Factură" : "Aviz");
                di.setNumarDocument("DOC-" + (1000 + i));
                di.setDataDocument(new Date());
                di.setDataOperare(new Date());
                di.setFurnizor(furnizori.get(i));

                // Recepție (NIR) [cite: 1918]
                Receptie r = new Receptie();
                r.setNrNIR("NIR-2026-" + i);
                r.setDataReceptie(new Date());
                r.setGestiune(gestiuni.get(i));
                r.setFacturaPrimita(0);

                // Adăugăm recepția la document (Relație bidirecțională) [cite: 1901-1903]
                di.addReceptie(r);

                // Linii Recepție [cite: 734-736, 2212]
                for (int j = 0; j < 2; j++) {
                    LinieIntrare li = new LinieIntrare();
                    Produs p = produse.get((i + j) % 10);
                    li.setProdus(p);
                    li.setCantitate(50.0 + i);
                    li.setPretAchizitie(p.getCostMediuPonderat());
                    li.setPretVanzare(p.getPretVanzare());
                    li.setAdaos(li.getPretVanzare() - li.getPretAchizitie());
                    r.addLinieIntrare(li); // Presupunem metoda add în Receptie similar cu DocInsotitor
                }

                docRepo.saveDocument(di); // Salvare în cascadă [cite: 64, 420]
            }

            // 5. POPULARE STOCURI [cite: 549, 743-748]
            for (int i = 0; i < 10; i++) {
                Stoc s = new Stoc();
                s.setGestiune(gestiuni.get(i));
                s.setProdus(produse.get(i));
                s.setStocCurent(100.0 + i * 10);
                s.setStocInitialAn(0.0);
                s.setStocInitialLuna(0.0);
                masterRepo.addStoc(s); // Notă: Stocul se adaugă prin MasterRepo
            }

            docRepo.commitTransaction();
            System.out.println("Baza de date a fost populată cu succes cu câte 10 valori pentru fiecare entitate!");

        } catch (Exception e) {
            System.err.println("Eroare la popularea bazei de date: " + e.getMessage());
            e.printStackTrace();
        }
    }
}