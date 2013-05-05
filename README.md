muni-fi-pb138-2013-videoteka
============================
School project from FI.MUNI.cz, **PB138** (spring 2013)

VideoWebGApps: Webová aplikace pro správu domácí videotéky
------------------------
**VideoWebGApps je webová aplikace, která umožňuje uživatelům spravovat svojí domácí videotéku/fonotéku.**

Aplikace je dostupná přes běžný webový prohlížeč a pro práci s videotékou nabízí především tyto funkce:
 - Vložit záznam o médiu do videotéky
 - Vyhledat médium podle názvu filmu, které je na médiu uložen
 - Vypsat všechna média a filmy, které obsahuje, podle kategorie
 - Přidat/odebrat kategorii médií
 - Změnit název kategorie médií
 - Exportovat veškerá data do tabulky ve formátu ODF
 - Importovat data z tabulky ve formátu ODF

Aplikace VideoWebGApps poskytuje veškeré funkce skrze grafické uživatelské rozhraní zobrazené ve webovém prohlížeči a veškerá data ukládá do jednoho souboru do úložiště GoogleDrive. Napojení na úložiště a autorizace aplikace vůči úložišti probíhá při inicializaci aplikace.

Data jsou ukládána v rámci úložiště do spreadsheet dokumentu, který obsahuje sadu záložek a v nich data ukládá podle principů definovaných v kapitole Fyzický model.

Vzhledem k tomu, že datový zdroj aplikace je běžně čitelný formát tabulkového procesoru a je dostupný uživateli skrze rozhraní GoogleDrive,má  uživatel možnost s datovým zdrojem videotéky pracovat i mimo aplikaci, což při zachování pravidel fyzického modelu není nijak na škodu. Aplikace VideoWebGApps však zajišťuje zachování integrity a formální správnosti dat a zároveň nabízí pohodlnější obshluhu skrze k tomuto účelu připravené grafické rozhraní.

Datový slovník
------------------------
Datovní slovník popisuje základní datové struktury a jejich přesný význam.

**Kategorie médií (Category)**
Kategorie médií je uživatelem vytvořená a libovolně pojmenovaná kategorie, která slouží především pro zpřehlednění videotéky. Kategorie může představovat např: horory, pohádky, živé konzerty apod.

**Typ média (MediumType)**
Typ média představuje popis datového média, jako je třeba CD, DVD, BlueRay, složka v PC...

**Médium (Medium)**
Médium je fyzický datový nosič: konkrétní CD, které je uložené v tom či onom šuplíku apod.

**Multimédium (Multimedia)**
Multimédium je konkrétní audio / video dílo. Představuje konkrétní film, který je reprezentovaný názvem.

**Typ multimédia (MultimediaType)**
Typ multimédia slouží pro další kategorizaci videotéky a obsahuje záznamy typu: video, hudba, hudební klip případně třeba i bitmapa či vektorová kresba.

**Datový formát (DataFormat)**
Datový formát je další kategorie pro zpřehlednění a třídění videotéky a je silně propojený s typem multimédia. Datový formát reprezentuje název datového formátu, jak je multimédium uložené, tedy např.: DivX, avi, mp3 apod.

Logický datový model
------------------------
Logický datový model ukazuje propojení jendnotlivých entit a jejich hierarchii.
<https://github.com/rhulek/muni-fi-pb138-2013-videoteka/blob/master/App_model/app-design/exportz.jpg>

Fyzický datový model
------------------------
Vzhledem k tomu, že jako vrstva persistence dat je GoogleDrive úložiště a data se ukládají do dokumentu ve formátu tabulkového procesoru, je tomu nutné také přizpůsobit mapování logického modelu na model fyzický.

Persistentní úložiště dat aplikace představuje jeden soubor ve formátu tabulkového procesoru.

**Kategorie médií** se ukládá vždy jako samostná záložka dokumentu.

**Médium** představuje jeden řádek v tabulce. Řádek je umístěn v dané záložce dokumentu podle toho, do jaké patří kategorie. Identifikátor média je uložen v uvozujícím sloupci čádku. Další sloupec řádku je vyhrazen pro uložení informace o typu média.

**Záznamy o** jednotlivých **multimédiích** na daném médiu jsou reprezentovány hodnotou buňky na řádku. Každé další multimédium za zapisuje do další volné buňky na řádku daného média.

**Typ multimédia** se ukládá ve strukturované formě (XML) do poznámky buňky konkrétního multimédia.

**Datový formát** multimédia se ukládá ve strukturované formě (XML) do poznámky buňky konkrétního multimédia.

Kvůli zajištění integrity dat se v datovém souboru nacházejí ještě speciální záložky, které obsahují seznamy s výčtovými typy (číselníky).
 - záložka **codeList_mediumType** obsahuje výčet povolených hodnot typů médií
 - záložka **codeList_multimediaType** obsahuje výčet povolených hodnot typů multimédií
 - záložka **codeList_dataFormat** obsahuje výčet povolených hodnot typů datových formátů
 
**Strukturovaná poznámka multimédia (buňky)**
Protože vyžadovaný datový formát tabulkového procesoru neumožňuje díky své dvou dimenzionální povaze ukládat libovolně strukturovaná data, tak bylo k ukládání dodatečných informací o multimédiu zvoleno jejich uchovávání v poznámce buňky ve formě XML s definovanou šablonou.
Strukturovaní XML poznámka v tomto momeňtě slouží jednak pro místo pro uchovávání dodatečných informací k multimédiu. Do budoucna však může představovat vhodný nástroj pro další rozšíření aplikace především z pohledu přidání dalších kategorií. Obdobný princip lze aplikovat i na entitu médium.

**Ukázka strukturované XML poznámky:**

    <multimediaData>
      <multimediumType>
        Hudební klip
      </multimediumType>
      <dataFormat>
        DivX
      </dataFormat>
      <otherExtensionPoint>
        DivX
      </otherExtensionPoint>
    </multimediaData>

Use case
------------------------
<https://github.com/rhulek/muni-fi-pb138-2013-videoteka/blob/master/App_model/app-design/use-case.jpg>
