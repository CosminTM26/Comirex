package ro.uaic.feaa.psi.comirex.forms.view;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import ro.uaic.feaa.psi.comirex.forms.*;
import ro.uaic.feaa.psi.comirex.model.entities.*;
import ro.uaic.feaa.psi.comirex.model.repository.MasterRepository;

public class ReceptieFormView extends JFrame {
	// Paleta de culori - Tematica Luminoasă (Originală)
	private static final Color BG_FRAME = Color.WHITE, BG_PANEL = Color.WHITE, TEXT_PRIMARY = Color.BLACK;
	private static final Color BORDURA = new Color(220, 224, 229), GRIS_HEADER = new Color(245, 247, 250), GRIS_GRID = new Color(226, 229, 234);
	private static final Color ALBASTRU = new Color(0, 110, 230), VERDE = new Color(33, 186, 69), ROSU = new Color(219, 40, 40), TOTAL_BG = new Color(230, 240, 255);

	private final ReceptieFormCtrl ctrl = new ReceptieFormCtrl();
	private boolean blocheazaSync;
	private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private java.util.List<DocInsotitor> toateDocumentele;
	private int indexDocCurent = -1;
	private final Map<LinieIntrare, Double> cantCmd = new HashMap<>();
	private final Map<LinieIntrare, String> obsLinie = new HashMap<>();

	private JLabel lblIndex;
	private JTextField txtNrReceptie, txtDataReceptie, txtObservatii, txtNrDocument, txtTotCmd, txtTotRec, txtDifTot;
	private JComboBox<Gestiune> cmbGestiune;
	private JComboBox<String> cmbReceptionatDe, cmbTipDocument;
	private JComboBox<Furnizor> cmbFurnizor;
	private JTable tabelLinii;
	private LinieIntrareTableModel tableModel;

	public ReceptieFormView() {
		super("Formular Recepție - Document Însoțitor");
		setSize(1100, 750); setLocationRelativeTo(null); setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getContentPane().setBackground(BG_FRAME);
		JPanel root = new JPanel(new BorderLayout(0, 6)); root.setBackground(BG_FRAME); root.setBorder(new EmptyBorder(8, 14, 8, 14));

		JPanel nord = new JPanel(); nord.setBackground(BG_FRAME); nord.setLayout(new BoxLayout(nord, BoxLayout.Y_AXIS));
		nord.add(buildNav()); nord.add(Box.createVerticalStrut(4));
		nord.add(buildDateRec()); nord.add(Box.createVerticalStrut(4)); nord.add(buildDocRef());

		root.add(nord, BorderLayout.NORTH); root.add(buildTabel(), BorderLayout.CENTER); root.add(buildSud(), BorderLayout.SOUTH);
		setContentPane(root);
		reseteaza(); incarcaListe(); ataseazaEvt();
	}

	private void reseteaza() {
		ctrl.getFormData().setOperatieSelectata(ReceptieFormData.RECEPTIE_CU_FACTURA);
		ctrl.documentNou(); indexDocCurent = -1; toateDocumentele = null; cantCmd.clear(); obsLinie.clear(); populeaza(); actIdx();
	}

	private JPanel buildNav() {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 2)); p.setBackground(BG_FRAME);
		JButton bP = new JButton("◀ Precedentul"), bN = new JButton("Următorul ▶");
		bP.addActionListener(e -> nav(-1)); bN.addActionListener(e -> nav(1));
		p.add(bP); p.add(lblIndex = lbl(" (nou) ", false)); p.add(bN);
		return p;
	}

	private JPanel buildDateRec() {
		JPanel p = sectiune("Date recepție");
		cmbGestiune = new JComboBox<>(); cmbGestiune.setRenderer(new DefaultListCellRenderer() {
			public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
				super.getListCellRendererComponent(l, v, i, s, f);
				if (v instanceof Gestiune) setText(((Gestiune)v).getDenumireGestiune()); return this;
			}
		});
		cmbReceptionatDe = new JComboBox<>(new String[]{"Popescu Ion", "Ionescu Maria"});
		addC(p, lbl("Nr. recepție:", true), 0, 0, 0, 1); addC(p, txtNrReceptie = input(""), 1, 0, 0.25, 1);
		addC(p, lbl("Data:", true), 2, 0, 0, 1); addC(p, txtDataReceptie = input(sdf.format(new Date())), 3, 0, 0.2, 1);
		addC(p, lbl("Gestiune:", true), 4, 0, 0, 1); addC(p, cmbGestiune, 5, 0, 0.35, 1);
		addC(p, lbl("Recepționat:", true), 0, 1, 0, 1); addC(p, cmbReceptionatDe, 1, 1, 0.25, 1);
		addC(p, lbl("Observații:", true), 2, 1, 0, 1); addC(p, txtObservatii = input(""), 3, 1, 0.8, 3);
		return p;
	}

	private JPanel buildDocRef() {
		JPanel p = sectiune("Document referință");
		cmbTipDocument = new JComboBox<>(new String[]{ReceptieFormCtrl.FACTURA, ReceptieFormCtrl.AVIZ, ReceptieFormCtrl.STORNARE});
		cmbFurnizor = new JComboBox<>(); cmbFurnizor.setRenderer(new DefaultListCellRenderer() {
			public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
				super.getListCellRendererComponent(l, v, i, s, f);
				if (v instanceof Furnizor) setText(((Furnizor)v).getNumeFurnizor()); return this;
			}
		});
		JButton bSrc = new JButton("..."); bSrc.setMargin(new Insets(2,4,2,4)); bSrc.addActionListener(e -> liveSearch());
		addC(p, lbl("Tip doc:", true), 0, 0, 0, 1); addC(p, cmbTipDocument, 1, 0, 0.3, 1);
		addC(p, lbl("Nr. doc:", true), 2, 0, 0, 1); addC(p, txtNrDocument = input(""), 3, 0, 0.3, 1);
		addC(p, lbl("Furnizor:", true), 4, 0, 0, 1); addC(p, cmbFurnizor, 5, 0, 0.4, 1); addC(p, bSrc, 6, 0, 0, 1);
		return p;
	}

	private JPanel buildTabel() {
		JPanel p = new JPanel(new BorderLayout(0,6)); p.setBackground(BG_FRAME);
		TitledBorder tb = new TitledBorder(new LineBorder(BORDURA, 1, true), "Detalii recepție marfă");
		tb.setTitleColor(TEXT_PRIMARY); tb.setTitleFont(tb.getTitleFont().deriveFont(Font.BOLD)); p.setBorder(tb);

		tabelLinii = new JTable(tableModel = new LinieIntrareTableModel()); tabelLinii.setRowHeight(30);
		tabelLinii.setSelectionBackground(ALBASTRU); tabelLinii.setSelectionForeground(Color.WHITE);
		tabelLinii.setGridColor(GRIS_GRID); tabelLinii.setShowGrid(true); tabelLinii.setFillsViewportHeight(true);

		tabelLinii.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {{ setOpaque(true); setHorizontalAlignment(LEFT); }
			public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
				super.getTableCellRendererComponent(t,v,s,f,r,c); setBackground(GRIS_HEADER); setForeground(TEXT_PRIMARY); setFont(getFont().deriveFont(Font.BOLD)); return this;
			}});

		tabelLinii.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
				Component cmp = super.getTableCellRendererComponent(t,v,s,f,r,c);
				if(c == 5 && v instanceof Number) setForeground(((Number)v).doubleValue() < 0 ? ROSU : TEXT_PRIMARY);
				if(c == 5) setHorizontalAlignment(RIGHT);
				if(c == 7) { JButton b = new JButton("×"); b.setForeground(ROSU); b.setBackground(BG_FRAME); b.setBorder(new LineBorder(ROSU,1)); return b; }
				return cmp;
			}});
		int[] w = {65, 180, 50, 100, 100, 75, 120, 50};
		for(int i=0; i<w.length; i++) tabelLinii.getColumnModel().getColumn(i).setPreferredWidth(w[i]);

		tabelLinii.addMouseListener(new MouseAdapter() { public void mouseClicked(MouseEvent e) {
			if(tabelLinii.columnAtPoint(e.getPoint())==7) stergRand(tabelLinii.rowAtPoint(e.getPoint())); }});

		JScrollPane sc = new JScrollPane(tabelLinii); sc.getViewport().setBackground(BG_PANEL); p.add(sc, BorderLayout.CENTER);
		JPanel b = new JPanel(new FlowLayout(FlowLayout.LEFT, 6,4)); b.setBackground(BG_FRAME);
		JButton bA = btn("+ Adăugare articol", VERDE, true), bS = btn("− Ștergere articol", ROSU, true);
		bA.addActionListener(e -> adaugRand()); bS.addActionListener(e -> stergRand(tabelLinii.getSelectedRow()));
		b.add(bA); b.add(bS); p.add(b, BorderLayout.SOUTH); return p;
	}

	private JPanel buildSud() {
		JPanel p = new JPanel(); p.setBackground(BG_FRAME); p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		JPanel pt = new JPanel(new FlowLayout(FlowLayout.CENTER, 16,6)); pt.setBackground(BG_FRAME); pt.setBorder(new LineBorder(GRIS_GRID,1));
		pt.add(lbl("Total comandat:", true)); pt.add(txtTotCmd = txtTot(false));
		pt.add(lbl("Total recepționat:", true)); pt.add(txtTotRec = txtTot(true));
		pt.add(lbl("Diferență totală:", true)); pt.add(txtDifTot = txtTot(false));
		p.add(pt); p.add(Box.createVerticalStrut(6));

		JPanel pb = new JPanel(new BorderLayout()); pb.setBackground(BG_FRAME);
		JPanel ps = new JPanel(new FlowLayout(FlowLayout.LEFT,6,0)); ps.setBackground(BG_FRAME);
		JPanel pd = new JPanel(new FlowLayout(FlowLayout.RIGHT,6,0)); pd.setBackground(BG_FRAME);
		JButton bAd = btn("Adăugare", null, false), bMd = btn("Modificare", null, false), bAn = btn("Anulare", null, false);
		JButton bRn = btn("Renunțare", null, false), bSv = btn("Salvare", ALBASTRU, true);
		bAd.addActionListener(e -> reseteaza()); bAn.addActionListener(e -> { if(JOptionPane.showConfirmDialog(this,"Anulați?","?",0)==0) reseteaza();});
		bMd.addActionListener(e -> JOptionPane.showMessageDialog(this,"Documentul este deja editabil.")); bRn.addActionListener(e->dispose()); bSv.addActionListener(e->salveaza());
		ps.add(bAd); ps.add(bMd); ps.add(bAn); pd.add(bRn); pd.add(bSv);
		pb.add(ps, BorderLayout.WEST); pb.add(pd, BorderLayout.EAST); p.add(pb); return p;
	}

	// Helpers UI compresate
	private JPanel sectiune(String t) { JPanel p = new JPanel(new GridBagLayout()); p.setBackground(BG_PANEL); TitledBorder tb = new TitledBorder(new LineBorder(BORDURA,1,true), t); tb.setTitleColor(TEXT_PRIMARY); tb.setTitleFont(tb.getTitleFont().deriveFont(Font.BOLD)); p.setBorder(BorderFactory.createCompoundBorder(tb, new EmptyBorder(4,6,4,6))); return p; }
	private void addC(JPanel p, Component c, int x, int y, double wx, int gw) { p.add(c, new GridBagConstraints(x,y,gw,1,wx,0,17,1,new Insets(4,5,4,5),0,0)); }
	private JTextField input(String t) { JTextField f = new JTextField(t); f.setBackground(Color.WHITE); f.setForeground(Color.BLACK); return f; }
	private JLabel lbl(String t, boolean b) { JLabel l = new JLabel(t); l.setForeground(TEXT_PRIMARY); if(b) l.setFont(l.getFont().deriveFont(Font.BOLD)); return l; }
	private JButton btn(String t, Color c, boolean p) { JButton b = new JButton(t); b.setFocusPainted(false); if(p){b.setBackground(c); b.setForeground(Color.WHITE); b.setOpaque(true); b.setBorderPainted(false);} else b.setForeground(Color.BLACK); return b; }
	private JTextField txtTot(boolean b) { JTextField t = new JTextField("0", 8); t.setEditable(false); t.setHorizontalAlignment(4); t.setForeground(TEXT_PRIMARY); t.setBackground(b?TOTAL_BG:BG_PANEL); t.setBorder(BorderFactory.createCompoundBorder(new LineBorder(b?ALBASTRU:BORDURA,b?2:1,true), new EmptyBorder(3,6,3,6))); return t; }

	private void incarcaListe() {
		blocheazaSync = true;

		// Incarcare Furnizori
		cmbFurnizor.removeAllItems();
		ctrl.getFormData().getListaFurnizori().forEach(cmbFurnizor::addItem);

		// Incarcare Gestiuni
		cmbGestiune.removeAllItems();
		java.util.List<Gestiune> gestiuni = ctrl.getFormData().getListaGestiuni();
		gestiuni.forEach(cmbGestiune::addItem);

		// INCARCARE GESTIONARI (Receptionat de)
		cmbReceptionatDe.removeAllItems();
		Set<String> gestionariUnici = new TreeSet<>(); // TreeSet pentru a-i avea ordonati alfabetic
		for (Gestiune g : gestiuni) {
			if (g.getNumeGestionar() != null && !g.getNumeGestionar().isEmpty()) {
				gestionariUnici.add(g.getNumeGestionar());
			}
		}
		gestionariUnici.forEach(cmbReceptionatDe::addItem);

		blocheazaSync = false;
	}
	private void ataseazaEvt() {
		cmbTipDocument.addActionListener(e -> {
			if (blocheazaSync || ctrl.getFormData().getDocumentCurent() == null) return;
			String t = (String) cmbTipDocument.getSelectedItem();
			ctrl.getFormData().getDocumentCurent().setTipDocument(t);
			ctrl.getFormData().setOperatieSelectata(
				t.equals(ReceptieFormCtrl.FACTURA) ? ReceptieFormData.RECEPTIE_CU_FACTURA :
				t.equals(ReceptieFormCtrl.AVIZ)    ? ReceptieFormData.RECEPTIE_CU_AVIZ :
				                                     ReceptieFormData.STORNARE);
			// La selectia Stornare: dialog pentru alegerea documentului original
			if (ReceptieFormCtrl.STORNARE.equals(t)) {
				selecteazaDocumentDeStornat();
			}
			tableModel.fireTableStructureChanged(); // actualizeaza antetul coloanei
		});
		cmbFurnizor.addActionListener(e -> { if(!blocheazaSync && cmbFurnizor.getSelectedItem()!=null) ctrl.selectieFurnizor((Furnizor)cmbFurnizor.getSelectedItem()); });
		cmbGestiune.addActionListener(e -> { if(!blocheazaSync && ctrl.getFormData().getReceptieSelectata()!=null) ctrl.getFormData().getReceptieSelectata().setGestiune((Gestiune)cmbGestiune.getSelectedItem()); });
	}

	/**
	 * Afiseaza un dialog pentru selectarea documentului original de stornat.
	 * Dupa selectie, populeza automat liniile cu cantitati negate.
	 * Daca utilizatorul anuleaza, revine la tipul Factura.
	 */
	private void selecteazaDocumentDeStornat() {
		// Incarca documentele existente, exclude stornarile
		java.util.List<DocInsotitor> documente = ctrl.getFormData().getDocRepo()
				.findDocInsotitoriAll().stream()
				.filter(d -> !ReceptieFormCtrl.STORNARE.equals(d.getTipDocument()))
				.collect(java.util.stream.Collectors.toList());

		if (documente.isEmpty()) {
			JOptionPane.showMessageDialog(this,
				"Nu există documente disponibile pentru stornare.\nCreați mai întâi o recepție cu Factură sau Aviz.",
				"Stornare", JOptionPane.WARNING_MESSAGE);
			// Revenim la Factura
			blocheazaSync = true;
			cmbTipDocument.setSelectedItem(ReceptieFormCtrl.FACTURA);
			ctrl.getFormData().getDocumentCurent().setTipDocument(ReceptieFormCtrl.FACTURA);
			blocheazaSync = false;
			return;
		}

		// Construieste lista pentru dialog
		String[] optiuni = documente.stream().map(d ->
			d.getTipDocument() + "  " + d.getNumarDocument() +
			(d.getFurnizor() != null ? "  —  " + d.getFurnizor().getNumeFurnizor() : ""))
			.toArray(String[]::new);

		String sel = (String) JOptionPane.showInputDialog(this,
			"Selectați documentul pe care doriți să îl stornați:",
			"Stornare — alegere document original",
			JOptionPane.PLAIN_MESSAGE, null, optiuni, optiuni[0]);

		if (sel == null) {
			// Utilizator a anulat — revenim la Factura
			blocheazaSync = true;
			cmbTipDocument.setSelectedItem(ReceptieFormCtrl.FACTURA);
			ctrl.getFormData().getDocumentCurent().setTipDocument(ReceptieFormCtrl.FACTURA);
			blocheazaSync = false;
			tableModel.fireTableStructureChanged();
			return;
		}

		int idx = java.util.Arrays.asList(optiuni).indexOf(sel);
		DocInsotitor docOriginal = documente.get(idx);

		// Initializeaza stornarea in controller (populeza linii cu cantitati negate)
		ctrl.initiazaStornareDocument(docOriginal);

		// Actualizeaza UI cu datele preluate din documentul original
		blocheazaSync = true;
		if (docOriginal.getFurnizor() != null) cmbFurnizor.setSelectedItem(docOriginal.getFurnizor());
		Receptie rec = ctrl.getFormData().getReceptieSelectata();
		if (rec != null && rec.getGestiune() != null) cmbGestiune.setSelectedItem(rec.getGestiune());
		blocheazaSync = false;

		tableModel.fireTableDataChanged();
		tableModel.fireTableStructureChanged();
		calc();

		JOptionPane.showMessageDialog(this,
			"Document stornat: " + docOriginal.getTipDocument() + " " + docOriginal.getNumarDocument() +
			"\nLiniile au fost preluate automat cu cantități negative.\n" +
			"Puteți ajusta cantitățile înainte de salvare.",
			"Stornare pregătită", JOptionPane.INFORMATION_MESSAGE);
	}

	private void populeaza() {
		blocheazaSync = true;
		DocInsotitor d = ctrl.getFormData().getDocumentCurent();
		Receptie r = ctrl.getFormData().getReceptieSelectata();

		if (d != null) {
			// "Necunoscut" nu exista in combo — defaultam la Factura
			String tip = d.getTipDocument();
			if (tip == null || ReceptieFormCtrl.NECUNOSCUT.equals(tip)) tip = ReceptieFormCtrl.FACTURA;
			cmbTipDocument.setSelectedItem(tip);
			txtNrDocument.setText(d.getNumarDocument() != null ? d.getNumarDocument() : "");
			if (d.getFurnizor() != null) cmbFurnizor.setSelectedItem(d.getFurnizor());
		}

		if (r != null) {
			txtNrReceptie.setText(r.getNrNIR() != null ? r.getNrNIR() : "");
			if (r.getDataReceptie() != null) txtDataReceptie.setText(sdf.format(r.getDataReceptie()));
			if (r.getGestiune() != null) {
				cmbGestiune.setSelectedItem(r.getGestiune());
				// Selectam automat gestionarul alocat acelei gestiuni
				cmbReceptionatDe.setSelectedItem(r.getGestiune().getNumeGestionar());
			}
		} else {
			txtNrReceptie.setText("");
			txtDataReceptie.setText(sdf.format(new Date()));
		}

		tableModel.fireTableDataChanged();
		calc();
		blocheazaSync = false;
	}


	private void calc() {
		double c=0, r=0; Receptie rec = ctrl.getFormData().getReceptieSelectata();
		if(rec!=null) for(LinieIntrare l : rec.getLiniiIntrare()) { c+=cantCmd.getOrDefault(l,0.0); r+=l.getCantitate()!=null?l.getCantitate():0.0; }
		txtTotCmd.setText(String.format("%.2f", c)); txtTotRec.setText(String.format("%.2f", r)); txtDifTot.setText(String.format("%.2f", r-c)); txtDifTot.setForeground(r-c<0?ROSU:TEXT_PRIMARY);
	}

	private void actIdx() { if(lblIndex!=null) lblIndex.setText((toateDocumentele==null||toateDocumentele.isEmpty()||indexDocCurent<0)?" (nou) ":" "+(indexDocCurent+1)+"/"+toateDocumentele.size()+" "); }

	private void nav(int dir) {
		if(toateDocumentele==null) toateDocumentele = ctrl.getFormData().getDocRepo().findDocInsotitoriAll();
		if(toateDocumentele.isEmpty()) { JOptionPane.showMessageDialog(this,"Nu există documente salvate."); return; }
		indexDocCurent = (indexDocCurent + dir + toateDocumentele.size()) % toateDocumentele.size();
		DocInsotitor d = toateDocumentele.get(indexDocCurent); ctrl.getFormData().setDocumentCurent(d);
		ctrl.getFormData().setReceptieSelectata(d.getReceptii().isEmpty()?null:d.getReceptii().get(0));
		cantCmd.clear(); obsLinie.clear(); populeaza(); actIdx();
	}

	private void adaugRand() {
		if(ctrl.getFormData().getReceptieSelectata()==null) {
			ctrl.adaugaReceptie(); Receptie r = ctrl.getFormData().getReceptieSelectata(); r.setNrNIR(txtNrReceptie.getText());
			try{ r.setDataReceptie(sdf.parse(txtDataReceptie.getText())); }catch(Exception ignored){}
			if(cmbGestiune.getSelectedItem()!=null) r.setGestiune((Gestiune)cmbGestiune.getSelectedItem());
		}
		java.util.List<Produs> prd = ctrl.getFormData().getListaProduse(); if(prd.isEmpty()) return;
		String[] arr = prd.stream().map(p -> (p.getCodProdus()!=null?p.getCodProdus()+"-":"")+p.getDenumire()).toArray(String[]::new);
		String sel = (String)JOptionPane.showInputDialog(this,"Selectați produs:","Adăugare articol",-1,null,arr,arr[0]); if(sel==null) return;
		int idx = Arrays.asList(arr).indexOf(sel); ctrl.adaugaLinieIntrare();
		LinieIntrare l = ctrl.getFormData().getReceptieSelectata().getLiniiIntrare().get(ctrl.getFormData().getReceptieSelectata().getLiniiIntrare().size()-1);
		Produs p = prd.get(idx); l.setProdus(p); l.setPretAchizitie(p.getCostMediuPonderat()!=null?p.getCostMediuPonderat():0.0);
		tableModel.fireTableDataChanged(); calc();
	}

	private void stergRand(int row) {
		Receptie r = ctrl.getFormData().getReceptieSelectata(); if(r==null||row<0||row>=r.getLiniiIntrare().size()) return;
		LinieIntrare l = r.getLiniiIntrare().get(row); r.removeLinieIntrare(l); cantCmd.remove(l); obsLinie.remove(l); tableModel.fireTableDataChanged(); calc();
	}

	private void salveaza() {
		if(txtNrReceptie.getText().trim().isEmpty() || txtNrDocument.getText().trim().isEmpty() || tabelLinii.getRowCount()==0) {
			JOptionPane.showMessageDialog(this, "Completați toate datele și adăugați minim un produs!", "Eroare", 2); return; }
		try {
			DocInsotitor d = ctrl.getFormData().getDocumentCurent();
			if(d!=null) { d.setNumarDocument(txtNrDocument.getText()); if(ctrl.getFormData().getReceptieSelectata()!=null) {
				ctrl.getFormData().getReceptieSelectata().setNrNIR(txtNrReceptie.getText());
				try{ctrl.getFormData().getReceptieSelectata().setDataReceptie(sdf.parse(txtDataReceptie.getText()));}catch(Exception ex){} } }
			ctrl.salveazaModificariDocument(); toateDocumentele = ctrl.getFormData().getDocRepo().findDocInsotitoriAll(); indexDocCurent = toateDocumentele.size()-1;
			actIdx(); JOptionPane.showMessageDialog(this, "Documentul a fost salvat cu succes!");
		} catch (Exception ex) { JOptionPane.showMessageDialog(this, "Eroare SQL: " + ex.getMessage(), "Eroare", 0); }
	}

	private void liveSearch() {
		java.util.List<Furnizor> fz = ctrl.getFormData().getListaFurnizori(); if(fz.isEmpty()) return;
		JDialog d = new JDialog(this, "Căutare Avansată Furnizor", true); d.setSize(400,300); d.setLocationRelativeTo(this); d.setLayout(new BorderLayout()); d.getContentPane().setBackground(BG_FRAME);
		JTextField ts = input(""); JPanel ps = new JPanel(new BorderLayout(5,5)); ps.setBackground(BG_FRAME); ps.setBorder(new EmptyBorder(10,10,0,10)); ps.add(lbl("Căutare:",true), BorderLayout.WEST); ps.add(ts, BorderLayout.CENTER);
		DefaultListModel<Furnizor> lm = new DefaultListModel<>(); fz.forEach(lm::addElement);
		JList<Furnizor> ls = new JList<>(lm); ls.setBackground(BG_PANEL); ls.setForeground(TEXT_PRIMARY); ls.setSelectionBackground(ALBASTRU); ls.setSelectionForeground(Color.WHITE);
		ls.setCellRenderer(new DefaultListCellRenderer() { public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
			super.getListCellRendererComponent(l,v,i,s,f); if(v instanceof Furnizor) setText(((Furnizor)v).getNumeFurnizor() + " (" + ((Furnizor)v).getCui() + ")"); return this; }});
		ts.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e){f();} public void removeUpdate(DocumentEvent e){f();} public void changedUpdate(DocumentEvent e){f();}
			void f() { String t=ts.getText().toLowerCase(); lm.clear(); fz.stream().filter(x->x.getNumeFurnizor().toLowerCase().contains(t)||(x.getCui()!=null&&x.getCui().toLowerCase().contains(t))).forEach(lm::addElement); }
		});
		Runnable sel = ()->{ if(ls.getSelectedValue()!=null) { cmbFurnizor.setSelectedItem(ls.getSelectedValue()); d.dispose(); }};
		ls.addMouseListener(new MouseAdapter(){public void mouseClicked(MouseEvent e){if(e.getClickCount()==2)sel.run();}});
		JPanel pb = new JPanel(new FlowLayout(FlowLayout.RIGHT)); pb.setBackground(BG_FRAME); JButton bs=btn("Selectează",VERDE,true), bc=btn("Renunțare",null,false); bs.addActionListener(e->sel.run()); bc.addActionListener(e->d.dispose()); pb.add(bc); pb.add(bs);
		d.add(ps, BorderLayout.NORTH); d.add(new JScrollPane(ls), BorderLayout.CENTER); d.add(pb, BorderLayout.SOUTH); d.setVisible(true);
	}

	/** Returneaza true daca documentul curent este de tip Stornare. */
	private boolean esteStornare() {
		DocInsotitor d = ctrl.getFormData().getDocumentCurent();
		return d != null && ReceptieFormCtrl.STORNARE.equals(d.getTipDocument());
	}

	private class LinieIntrareTableModel extends AbstractTableModel {
		private final String[] COLOANE_NORMAL  = {"Cod", "Denumire produs", "UM", "Cant. comandată", "Cant. recepționată", "Diferență", "Observații", "Acțiuni"};
		private final String[] COLOANE_STORNARE= {"Cod", "Denumire produs", "UM", "Cant. comandată", "Cantitate retur",    "Diferență", "Observații", "Acțiuni"};

		public int getRowCount() { return ctrl.getFormData().getReceptieSelectata()==null?0:ctrl.getFormData().getReceptieSelectata().getLiniiIntrare().size(); }
		public int getColumnCount() { return 8; }
		public String getColumnName(int i) { return esteStornare() ? COLOANE_STORNARE[i] : COLOANE_NORMAL[i]; }
		public boolean isCellEditable(int r, int c) { return c==3||c==4||c==6; }

		public Object getValueAt(int r, int c) {
			if (ctrl.getFormData().getReceptieSelectata() == null) return "";
			LinieIntrare l = ctrl.getFormData().getReceptieSelectata().getLiniiIntrare().get(r);
			Produs p = l.getProdus();
			double cant = l.getCantitate() != null ? l.getCantitate() : 0.0;
			switch(c) {
				case 0: return p!=null?p.getCodProdus():"";
				case 1: return p!=null?p.getDenumire():"";
				case 2: return p!=null?p.getUm():"";
				case 3: return cantCmd.getOrDefault(l, 0.0);
				// In modul Stornare: afisam valoarea absoluta (pozitiva) — mai natural pentru utilizator
				case 4: return esteStornare() ? Math.abs(cant) : cant;
				case 5: return cant - cantCmd.getOrDefault(l, 0.0);
				case 6: return obsLinie.getOrDefault(l,"");
				default: return "×";
			}
		}

		public void setValueAt(Object v, int r, int c) {
			if (ctrl.getFormData().getReceptieSelectata() == null) return;
			LinieIntrare l = ctrl.getFormData().getReceptieSelectata().getLiniiIntrare().get(r);
			if (c == 3) {
				try { cantCmd.put(l, Double.parseDouble(v.toString().replace(",","."))); } catch(Exception ignored){}
			} else if (c == 4) {
				try {
					double val = Double.parseDouble(v.toString().replace(",","."));
					// In modul Stornare: utilizatorul scrie pozitiv (ex: 5), stocam negativ (-5)
					if (esteStornare()) val = -Math.abs(val);
					l.setCantitate(val);
				} catch(Exception ignored){}
			} else if (c == 6) {
				obsLinie.put(l, v != null ? v.toString() : "");
			}
			fireTableRowsUpdated(r, r);
			calc();
		}
	}

	public static void main(String[] args) {
		try { for(UIManager.LookAndFeelInfo i : UIManager.getInstalledLookAndFeels()) if("Nimbus".equals(i.getName())) { UIManager.setLookAndFeel(i.getClassName()); break; } } catch (Exception ignored) {}
		seedDateMasterDacaENevoie();
		SwingUtilities.invokeLater(() -> new ReceptieFormView().setVisible(true));
	}

	private static void seedDateMasterDacaENevoie() {
		MasterRepository repo = new MasterRepository();
		if(repo.findFurnizoriAll().isEmpty()) {
			repo.beginTransaction();

			Furnizor f = new Furnizor();
			f.setCodFurnizor("F1"); f.setNumeFurnizor("SC Ferro Metal SRL"); f.setAdresa("Iași"); f.setCui("RO10000001");
			repo.addFurnizor(f);

			Gestiune g = new Gestiune();
			g.setCodGestiune("G1"); g.setDenumireGestiune("Depozit Central"); g.setNumeGestionar("Popescu Ion"); g.setAdresa("Iași");
			repo.addGestiune(g);

			Produs p1 = new Produs();
			p1.setCodProdus("M-101"); p1.setDenumire("Cuie zincate 3x60 mm"); p1.setUm("kg"); p1.setPretVanzare(11.5); p1.setCostMediuPonderat(8.5);
			repo.addProdus(p1);

			Produs p2 = new Produs();
			p2.setCodProdus("M-205"); p2.setDenumire("Șuruburi autofirante 4x40"); p2.setUm("buc"); p2.setPretVanzare(0.45); p2.setCostMediuPonderat(0.35);
			repo.addProdus(p2);

			repo.commitTransaction();
		}
	}
}