from docx import Document
from docx.shared import Pt, Cm, RGBColor, Inches
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.style import WD_STYLE_TYPE
from docx.oxml.ns import qn
from docx.oxml import OxmlElement
import copy

doc = Document()

# ── Margini pagina ──────────────────────────────────────────────────────────
for section in doc.sections:
    section.top_margin    = Cm(2.5)
    section.bottom_margin = Cm(2.5)
    section.left_margin   = Cm(2.5)
    section.right_margin  = Cm(2.5)

# ── Stiluri de baza ─────────────────────────────────────────────────────────
normal = doc.styles['Normal']
normal.font.name = 'Times New Roman'
normal.font.size = Pt(12)

def set_font(run, bold=False, italic=False, size=12, color=None):
    run.font.name = 'Times New Roman'
    run.font.size = Pt(size)
    run.font.bold = bold
    run.font.italic = italic
    if color:
        run.font.color.rgb = RGBColor(*color)

def heading(text, level=1):
    p = doc.add_paragraph()
    p.alignment = WD_ALIGN_PARAGRAPH.LEFT
    run = p.add_run(text)
    set_font(run, bold=True, size=14 if level == 1 else 12)
    p.paragraph_format.space_before = Pt(12)
    p.paragraph_format.space_after  = Pt(6)
    return p

def para(text, justify=True, space_after=6):
    p = doc.add_paragraph()
    if justify:
        p.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
    run = p.add_run(text)
    set_font(run)
    p.paragraph_format.space_after = Pt(space_after)
    p.paragraph_format.first_line_indent = Cm(1.25)
    return p

def para_mixed(parts, justify=True, space_after=6, indent=True):
    """parts = list of (text, bold, italic, mono)"""
    p = doc.add_paragraph()
    if justify:
        p.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
    if indent:
        p.paragraph_format.first_line_indent = Cm(1.25)
    p.paragraph_format.space_after = Pt(space_after)
    for text, bold, italic, mono in parts:
        run = p.add_run(text)
        run.font.bold   = bold
        run.font.italic = italic
        run.font.name   = 'Courier New' if mono else 'Times New Roman'
        run.font.size   = Pt(10) if mono else Pt(12)
    return p

def bullet(text, level=0):
    p = doc.add_paragraph(style='List Bullet')
    p.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
    run = p.add_run(text)
    set_font(run)
    p.paragraph_format.left_indent  = Cm(1.0 + level * 0.6)
    p.paragraph_format.space_after  = Pt(3)
    return p

def numbered(text):
    p = doc.add_paragraph(style='List Number')
    p.alignment = WD_ALIGN_PARAGRAPH.JUSTIFY
    run = p.add_run(text)
    set_font(run)
    p.paragraph_format.left_indent = Cm(1.0)
    p.paragraph_format.space_after = Pt(3)
    return p

def code_block(lines):
    for line in lines:
        p = doc.add_paragraph()
        p.alignment = WD_ALIGN_PARAGRAPH.LEFT
        p.paragraph_format.space_before = Pt(0)
        p.paragraph_format.space_after  = Pt(0)
        p.paragraph_format.left_indent  = Cm(1.0)
        # fundal gri deschis
        pPr = p._p.get_or_add_pPr()
        shd = OxmlElement('w:shd')
        shd.set(qn('w:val'),   'clear')
        shd.set(qn('w:color'), 'auto')
        shd.set(qn('w:fill'),  'F2F2F2')
        pPr.append(shd)
        run = p.add_run(line)
        run.font.name = 'Courier New'
        run.font.size = Pt(9)
    doc.add_paragraph().paragraph_format.space_after = Pt(4)

def caption(text):
    p = doc.add_paragraph()
    p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    run = p.add_run(text)
    set_font(run, italic=True, size=11)
    p.paragraph_format.space_after = Pt(10)

# ════════════════════════════════════════════════════════════════════════════
# ANTET
# ════════════════════════════════════════════════════════════════════════════
antet = doc.add_paragraph()
antet.alignment = WD_ALIGN_PARAGRAPH.CENTER
for line in [
    "Universitatea Alexandru Ioan Cuza din Iași\n",
    "Facultatea de Economie și Administrarea Afacerilor\n",
    "An 3, Informatică Economică\n",
    "Proiectarea Sistemelor Informaționale\n",
]:
    run = antet.add_run(line)
    set_font(run, size=11)

doc.add_paragraph()

titlu = doc.add_paragraph()
titlu.alignment = WD_ALIGN_PARAGRAPH.CENTER
run = titlu.add_run("Proiect — Sistem de Gestiune Comirex\nModulul: Recepție Document Însoțitor")
set_font(run, bold=True, size=14)
doc.add_paragraph()

# ════════════════════════════════════════════════════════════════════════════
# 3. MODELUL FIZIC
# ════════════════════════════════════════════════════════════════════════════
heading("3. Modelul fizic", level=1)

para(
    "Până aici ne-am ocupat de modelul conceptual, redat prin diagrama de clase ce includea "
    "entitățile persistente, realizată folosind standardul UML, și de modelul logic al datelor, "
    "pentru care a fost adoptat modelul relațional, obținut prin transformarea modelului "
    "conceptual."
)
para(
    "Acum ne vom ocupa de modelul fizic. El presupune implementarea modelelor conceptual și logic "
    "folosind un limbaj de programare și un SGBD. Crearea modelului fizic presupune ca în prealabil "
    "să fie alese tehnologiile/platformele de implementare, urmând ca modelele conceptual și logic "
    "să fie transformate în funcție de particularitățile tehnologiilor alese."
)
para(
    "Modelul fizic de persistență presupune două straturi: obiectual, care conține clasele de "
    "obiecte ale aplicației, și relațional, care include tabelele bazei de date. Ele pot fi obținute "
    "prin două transformări:"
)
numbered(
    "transformarea diagramei de clase UML în codul de implementare al claselor, folosind sintaxa "
    "limbajului de programare ales (în cazul nostru Java 21);"
)
numbered(
    "transformarea diagramei entitate-relație în schema bazei de date, folosind SGBD-ul ales "
    "(în cazul nostru PostgreSQL 14+), prin intermediul framework-ului ORM Hibernate 5.4."
)
para(
    "Ambele faze de transformare sunt automatizate prin intermediul framework-ului Hibernate ORM, "
    "care sincronizează clasele Java cu schema bazei de date pe baza adnotărilor JPA (@Entity, "
    "@OneToMany, @ManyToOne etc.). Configurația hibernate.hbm2ddl.auto=update permite ca schema "
    "bazei de date să fie generată și actualizată automat la pornirea aplicației."
)

# ── Tehnologii ───────────────────────────────────────────────────────────────
heading("3.1 Tehnologii și platforme utilizate", level=2)

para(
    "Proiectul Comirex este implementat folosind următorul stack tehnologic:"
)
bullet("Limbaj de programare: Java 21")
bullet("Build tool: Apache Maven 3")
bullet("Framework ORM: Hibernate 5.4.12.Final (JPA 2.2)")
bullet("Bază de date: PostgreSQL 14+ (localhost:5433/Comirex)")
bullet("Interfață grafică: Java Swing (Nimbus Look & Feel)")
bullet("Modelare UML: PlantUML (diagramă de clase + diagrame de secvențe)")
doc.add_paragraph()

# ── Entitati ─────────────────────────────────────────────────────────────────
heading("3.2 Stratul obiectual — Clasele entitate", level=2)

para(
    "Clasele entitate reprezintă obiectele persistente ale aplicației. Fiecare clasă este "
    "adnotată cu @Entity și extinde AbstractEntity, care furnizează câmpurile comune tuturor "
    "entităților: id, version, dateCreated, dateUpdated."
)

para(
    "Ierarhia de clase entitate utilizată în modulul Recepție Document Însoțitor este prezentată "
    "în continuare."
)

# AbstractEntity
para_mixed([("AbstractEntity", True, False, False), (" — clasa de bază pentru toate entitățile persistente:", False, False, False)])
code_block([
    "@MappedSuperclass",
    "public abstract class AbstractEntity {",
    "    @Id",
    "    @GeneratedValue(strategy = GenerationType.IDENTITY)",
    "    private Long id;",
    "    @Version",
    "    private Integer version;",
    "    private Date dateCreated;",
    "    private Date dateUpdated;",
    "}",
])

# Document
para_mixed([("Document", True, False, False), (" — clasa de bază pentru toate documentele aplicației:", False, False, False)])
code_block([
    "@Entity",
    "@Inheritance(strategy = InheritanceType.JOINED)",
    "public class Document extends AbstractEntity {",
    "    private String tipDocument;",
    "    private String numarDocument;",
    "    @Temporal(TemporalType.DATE)",
    "    private Date dataDocument;",
    "    @Temporal(TemporalType.TIMESTAMP)",
    "    private Date dataOperare;",
    "}",
])

# DocInsotitor
para_mixed([("DocInsotitor", True, False, False), (" — documentul însoțitor (factură, aviz sau stornare):", False, False, False)])
code_block([
    "@Entity",
    "public class DocInsotitor extends Document {",
    "    @Column(unique = true)",
    "    private String codDocInsot;",
    "    private String mijlocTransport;",
    "    @ManyToOne",
    "    private Furnizor furnizor;",
    "    @ManyToOne",
    "    private DocInsotitor docInsotitorReferinta; // referinta stornare",
    "    @OneToMany(mappedBy = \"docInsotitor\",",
    "               cascade = CascadeType.ALL, fetch = FetchType.EAGER)",
    "    private Set<Receptie> receptii = new HashSet<>();",
    "}",
])

# Receptie
para_mixed([("Receptie", True, False, False), (" — recepția de marfă asociată unui document însoțitor:", False, False, False)])
code_block([
    "@Entity",
    "public class Receptie extends AbstractEntity {",
    "    @Column(unique = true)",
    "    private String codNIR;",
    "    private String nrNIR;",
    "    @Temporal(TemporalType.DATE)",
    "    private Date dataReceptie;",
    "    @ManyToOne",
    "    private DocInsotitor docInsotitor;",
    "    @ManyToOne",
    "    private Gestiune gestiune;",
    "    @OneToMany(mappedBy = \"receptie\",",
    "               cascade = CascadeType.ALL, fetch = FetchType.EAGER)",
    "    private Set<LinieIntrare> liniiIntrare = new HashSet<>();",
    "}",
])

# LinieIntrare
para_mixed([("LinieIntrare", True, False, False), (" — o linie de articol din recepție (cantitate poate fi negativă la stornare):", False, False, False)])
code_block([
    "@Entity",
    "public class LinieIntrare extends AbstractEntity {",
    "    private Double cantitate = 0.0;  // negativa pentru Stornare",
    "    private Double pretAchizitie = 0.0;",
    "    private Double pretVanzare   = 0.0;",
    "    private Double adaos         = 0.0;",
    "    @ManyToOne",
    "    private Receptie receptie;",
    "    @ManyToOne",
    "    private Produs produs;",
    "}",
])

# Stoc
para_mixed([("Stoc", True, False, False), (" — stocul curent al unui produs într-o gestiune, actualizat automat la salvare:", False, False, False)])
code_block([
    "@Entity",
    "public class Stoc extends AbstractEntity {",
    "    private Double stocCurent      = 0.0;",
    "    private Double stocInitialLuna = 0.0;",
    "    private Double stocInitialAn   = 0.0;",
    "    @ManyToOne",
    "    private Produs produs;",
    "    @ManyToOne",
    "    private Gestiune gestiune;",
    "}",
])

# ── Repository ───────────────────────────────────────────────────────────────
heading("3.3 Stratul de acces la date — Repository", level=2)

para(
    "Clasele repository realizează accesul la baza de date prin intermediul EntityManager-ului "
    "furnizat de Hibernate. Toate extind AbstractRepository, care oferă operațiile CRUD de bază "
    "(create, update, delete) și gestionarea tranzacțiilor."
)

para_mixed([("MasterRepository", True, False, False), (" — gestionează entitățile master: Furnizor, Gestiune, Produs, Stoc:", False, False, False)])
code_block([
    "public class MasterRepository extends AbstractRepository {",
    "    public List<Furnizor>  findFurnizoriAll()  { ... }",
    "    public Furnizor        findFurnizorById(Long id) { ... }",
    "    public List<Gestiune>  findGestiuniAll()   { ... }",
    "    public List<Produs>    findProduseAll()    { ... }",
    "    public List<Stoc>      findStocByProdusGestiune(Long pid, Long gid) { ... }",
    "    public Stoc            addStoc(Stoc s)     { ... }",
    "    public Stoc            updateStoc(Stoc s)  { ... }",
    "}",
])

para_mixed([("DocumentRepository", True, False, False), (" — gestionează documentele și recepțiile:", False, False, False)])
code_block([
    "public class DocumentRepository extends AbstractRepository {",
    "    public DocInsotitor      saveDocInsotitor(DocInsotitor d) { ... }",
    "    public List<DocInsotitor> findDocInsotitoriAll()          { ... }",
    "    public Receptie          saveReceptie(Receptie r)         { ... }",
    "}",
])

# ════════════════════════════════════════════════════════════════════════════
# 3.1 MVC + diagrama de clase
# ════════════════════════════════════════════════════════════════════════════
heading("3.4 Proiectarea arhitecturii programelor — Șablonul MVC", level=2)

para(
    "Formularul de recepție este implementat conform șablonului MVC (Model-View-Controller) "
    "adaptat pentru aplicații desktop Swing. Arhitectura cuprinde cinci tipuri de clase, "
    "fiecare cu responsabilități bine definite:"
)
bullet("Entity — clasele persistente gestionate de Hibernate (descrise la §3.2).")
bullet("Repository — clasele de acces la date (descrise la §3.3).")
bullet("Model (FormData) — ReceptieFormData: adaptorul modelului de domeniu pentru formular; "
       "menține starea formularului și încarcă listele în mod lazy (la primul acces).")
bullet("Controller — ReceptieFormCtrl: implementează logica aplicației ca răspuns la acțiunile "
       "utilizatorului; coordonează FormData și Repository-urile.")
bullet("View — ReceptieFormView: interfața grafică Swing; afișează datele din FormData și "
       "transmite acțiunile utilizatorului către Controller.")
doc.add_paragraph()

para_mixed([("ReceptieFormData", True, False, False), (" (Model) — starea formularului, cu lazy-loading:", False, False, False)])
code_block([
    "public class ReceptieFormData {",
    "    private DocInsotitor documentCurent;",
    "    private Receptie     receptieSelectata;",
    "    private String       operatieSelectata;",
    "    private List<Furnizor> listaFurnizori;   // lazy: incarca la primul acces",
    "    private List<Gestiune> listaGestiuni;",
    "    private List<Produs>   listaProduse;",
    "    private MasterRepository   masterRepo = new MasterRepository();",
    "    private DocumentRepository docRepo    = new DocumentRepository();",
    "    public static final String RECEPTIE_CU_FACTURA = \"Receptie cu Factura\";",
    "    public static final String RECEPTIE_CU_AVIZ    = \"Receptie cu Aviz\";",
    "    public static final String STORNARE             = \"Stornare\";",
    "    // getteri/setteri + lazy-loading in getListaFurnizori() etc.",
    "}",
])

para_mixed([("ReceptieFormCtrl", True, False, False), (" (Controller) — logica formularului și actualizarea stocului:", False, False, False)])
code_block([
    "public class ReceptieFormCtrl {",
    "    private ReceptieFormData formData = new ReceptieFormData();",
    "    public static final String FACTURA  = \"Factura\";",
    "    public static final String AVIZ     = \"Aviz\";",
    "    public static final String STORNARE = \"Stornare\";",
    "",
    "    public void documentNou()                     { ... }",
    "    public void selectieFurnizor(Furnizor f)      { ... }",
    "    public void adaugaReceptie()                  { ... }",
    "    public void adaugaLinieIntrare()              { ... }",
    "    public void initiazaStornareDocument(",
    "                    DocInsotitor docOriginal)     { ... }",
    "    public void salveazaModificariDocument()      {",
    "        // salveaza DocumentRepository + actualizeazaStoc()",
    "    }",
    "    private void actualizeazaStoc(DocInsotitor d) {",
    "        // stocCurent += cantitate (negativa pentru Stornare)",
    "    }",
    "}",
])

para(
    "Diagrama de clase completă, în format PlantUML, este disponibilă în fișierul "
    "DiagramaClase_MVC.puml din directorul docs/ al proiectului. Ea include toate cele cinci "
    "tipuri de clase cu relațiile de moștenire, compoziție și dependență dintre ele."
)

# ════════════════════════════════════════════════════════════════════════════
# 3.2 Diagrame de secvente
# ════════════════════════════════════════════════════════════════════════════
heading("3.5 Proiectarea aspectelor dinamice — Diagrame de secvențe", level=2)

para(
    "Pentru proiectarea aspectelor dinamice ale sistemului au fost construite patru diagrame de "
    "secvențe, câte una pentru fiecare membru al echipei, fiecare acoperind un scenariu de lucru "
    "specific formularului de recepție. Diagramele sunt disponibile în directorul docs/ al "
    "proiectului, în format PlantUML."
)

# Tabel scenarii
table = doc.add_table(rows=5, cols=4)
table.style = 'Table Grid'
headers = ["Fișier .puml", "Student", "Scenariu", "Particularitate"]
for i, h in enumerate(headers):
    cell = table.rows[0].cells[i]
    run  = cell.paragraphs[0].add_run(h)
    set_font(run, bold=True, size=11)
    cell.paragraphs[0].alignment = WD_ALIGN_PARAGRAPH.CENTER

rows_data = [
    ("Scenariu1_ReceptieCuFactura.puml", "Student 1",
     "Recepție cu Factură",
     "Flux complet: documentNou → selectieFurnizor → adaugaReceptie → adaugaLinieIntrare → salveaza + actualizare stoc"),
    ("Scenariu2_ReceptieCuAviz.puml", "Student 2",
     "Recepție cu Aviz (2 linii)",
     "Două linii de intrare, produse diferite; assert size==2 înainte de salvare"),
    ("Scenariu3_Stornare.puml", "Student 3",
     "Stornare document",
     "Dialog selectare document original; cantități negate automat; stocCurent scade"),
    ("Scenariu4_Stergere.puml", "Student 4",
     "Ștergere linie din memorie",
     "2 linii adăugate, una ștearsă înainte de salvare; BD conține o singură linie"),
]
for ri, (f, s, sc, part) in enumerate(rows_data):
    cells = table.rows[ri + 1].cells
    for ci, text in enumerate([f, s, sc, part]):
        run = cells[ci].paragraphs[0].add_run(text)
        set_font(run, size=10, italic=(ci == 0))

doc.add_paragraph()

para(
    "Diagrama de secvențe pentru metoda salveazaModificariDocument() ilustrează interacțiunea "
    "dintre toate componentele MVC la salvarea unui document: View apelează Controller-ul, care "
    "deschide o tranzacție prin DocumentRepository, persistă documentul prin cascade "
    "(DocInsotitor → Receptie → LinieIntrare), actualizează stocul prin MasterRepository "
    "(addStoc sau updateStoc), și finalizează tranzacția cu commit."
)

para(
    "Diagrama de secvențe pentru scenariul de Stornare ilustrează fluxul nou implementat: "
    "utilizatorul selectează tipul Stornare din combo-ul formularului, ceea ce declanșează "
    "automat un dialog de alegere a documentului original. Prin apelul "
    "initiazaStornareDocument(docOriginal), controller-ul preia furnizorul și gestiunea din "
    "documentul original și copiază toate liniile cu cantitățile negate (Math.abs × -1), "
    "fără ca utilizatorul să introducă valori negative manual."
)

# ── Salvare ──────────────────────────────────────────────────────────────────
out = r"C:\Users\Cosmin\Desktop\Facultate\Anul 3\Sem2\Proiectarea sistemelor informationale\Proiect\comirex\docs\ModelulFizic_Comirex.docx"
doc.save(out)
print(f"Salvat: {out}")
