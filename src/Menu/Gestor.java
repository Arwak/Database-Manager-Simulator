package Menu;

import Estructures.*;
import CSV.CSVManange;
import DBMSi.*;


import java.util.*;

//TODO update guardi que fa per opció visualitzar acabar update
//TODO control lletres en tria de quin arbre

/**
 * Created by XRoma i Clupspv on 12/5/17.
 * * Classe que gestiona la crida a les funcions pertinents per realitzar cada opció
 */
public class Gestor {
    private final static int PRIMERA_OPCIO = 1;
    private final static int NUMERO_OPCIONS = 8;
    private final static int NUMERO_OPCIONS_TERCER = 3;
    private static ArrayList<Table> taulesGestor;
    private static ArrayList<TableRowRestriction> restriction;
    private static int estructura;
    private static Table taulaTractant;
    private static int whereIsTheTable;


    /**
     * Funció que permet tenir còpia d'on es guardaran totes les taules que continguin
     * un arbre AVL de la base de dades
     * @param Tree
     */
    public static void setAVLTree(ArrayList<Table> Tree) {
        taulesGestor = Tree;
    }

    /**
     * Funció que entrada una opció fa la crida a la funció que la gestiona
     * corresponent al primer menú
     * @param opcio Paràmetre que indica l'opció que ha de ser executada
     */
    public static void gestionaOpcioAmbNumero(int opcio){
        switch (opcio) {
            case 1:
                gestioPrimera();
                break;
            case 2:
                gestioSegona();
                break;
            case 3:
                gestioTercera();
                break;
            case 4:
                gestioQuarta();
                break;
            case 5:
                break;
            default:
                System.out.println("Error, opció no disponible");
                break;
        }
    }

    /**
     * Funció que entrada una opció fa la crida a la funcio que la gestiona
     * corresponent al segon menu
     * @param opcio Parametre que indica l'opció que ha de ser executada
     */
    public static void gestionaOpcioSegonMenu(int opcio){
        switch (opcio) {
            case 1:
                gestioPrimeraMenu2();
                break;
            case 2:
                gestioSegonaMenu2();
                break;
            case 3:
                gestioTerceraMenu2();
                break;
            case 4:
                gestioCuartaMenu2();
                break;
            case 5:
                gestioCinquenaMenu2();
                break;
            case 6:
                gestioSisenaMenu2();
                break;
            case 7:
                gestioSetenaMenu2();
                break;
            case 8:
                gestioVuitenaMenu2();
                break;
            default:
                System.out.println("Error, opció no disponible");
                break;
        }
    }

    /**
     * Funcioóque entrada una opció fa la crida a la funcio que la gestiona
     * corresponent al terçer menu
     * @param opcio Parametre que indica l'opció que ha de ser executada
     */

    public static void gestionaOpcioTercerMenu(int opcio){
        switch (opcio) {
            case 1:
                gestioPrimeraMenu3();
                break;
            case 2:
                gestioSegonaMenu3();
                break;
            case 3:
                gestioTerceraMenu3();
                break;
            default:
                System.out.println("Error, opció no disponible");
                break;
        }
    }

    /**
     * Gestiona la primera opció
     *
     */
    private static void gestioPrimera(){
        Scanner     sc = new Scanner(System.in);    // Variable scanner
        String taula, columna, tipus;
        Table table = null;
        boolean status;
        DataType dataType;

        do {
            System.out.println("Select a data structure for the table:");
            System.out.print(" 1. ");
            System.out.println(AVL.getName());
            System.out.print(" 2. ");
            System.out.println(Arbre.getName());
            try {
                estructura = sc.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.err.println("Error, opció no vàlida");
                estructura = -1;
                System.out.println("");

            }
        } while (estructura < 1 || 2 < estructura);

        do {
            System.out.print("\nEnter a name for the table: ");
            taula = sc.nextLine();
            taula = sc.nextLine();
            status = tableAlreadyDefined(taula);
            if (!status) {
                System.err.print("Error, table ");
                System.err.print(taula);
                System.err.println(" already exists,");
            }
        } while (!status);

        columna = DatabaseInput.askForColumnInicial(taula);
        dataType = DatabaseInput.searchDataType();

        switch (estructura) {
            case 1:
                table = new Table(taula, new AVL(columna, null));
                addColumnToTable(table, columna, dataType);

                break;
            case 2:
                table = new Table(taula, new Arbre(columna));
                addColumnToTable(table, columna, dataType);
                break;
            case 3:
                table = new Table(taula, new TaulaHashI(columna));
                addColumnToTable(table, columna, dataType);
                break;
            case 4:
                table = new Table(taula, new TaulaHash(columna));
                addColumnToTable(table, columna, dataType);
                break;
        }

        do {
            System.out.println("Would you like to add another column? [Y/N]");
            tipus = DatabaseInput.askYesNo();
            if (!("Y".equals(tipus))) {
                break;
            }

            columna = DatabaseInput.askForColumnInicial(taula);
            dataType = DatabaseInput.searchDataType();
            addColumnToTable(table, columna, dataType);

        } while (tipus.equals("Y"));

        int tamany = table.getColumnNames().size();
        if (tamany > 1) {
            System.out.println("Select a column from the list to use it as an index:");

            for (int i = 0; i < tamany; i ++) {
                System.out.println(table.getColumnNames().get(i));
            }

            status = false;
            while (!status) {
                tipus = DatabaseInput.readIndexColumnName(); //pregunta which field?
                if((searchForColumn(table.getColumnNames(), tipus) > -1) && DatabaseInput.isCorrectType(table.getColumnType(tipus))) {
                    status = true;
                } else {
                    System.err.println("The index must be INT or TEXT!");
                }
            }
            table.setIndex(tipus);
        }

        taulesGestor.add(table);
    }


    /**
     * Gestiona la segona opció
     */
    private static void gestioSegona(){
        Scanner     sc = new Scanner(System.in);
        int tamany = taulesGestor.size();
        String what;

        Collections.sort(taulesGestor, new Comparator<Table>() {
            @Override
            public int compare(Table o1, Table o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        for (int i = 0; i < tamany; i++) {
            System.out.println(taulesGestor.get(i).getName());
        }

        if (tamany > 0) {
            do {
                System.out.println("Which table do you want to manage?");
                what = sc.nextLine();
                whereIsTheTable = searchForTable(what);
            } while (whereIsTheTable < 0);

            Menu m;
            do {
                m = new Menu(PRIMERA_OPCIO, NUMERO_OPCIONS, Menu.ES_MENU_SECUNDARI);
                restriction = new ArrayList<TableRowRestriction>();
                Gestor.gestionaOpcioSegonMenu(m.getOpcio());
            }while (m.getOpcio() != m.getNumOpcioSortir());
        } else {
            System.out.println("There are no tables to manage!");
        }


    }


    /**
     * Gestiona la tercera opció
     */
    private static void gestioTercera(){
        int tamany = taulesGestor.size();
        for (int i = 0; i < tamany; i++) {
            mostrarTaules(taulesGestor.get(i));
        }
    }


    /**
     * Gestiona la quarta opció
     */
    private static void gestioQuarta(){
        int tamany = taulesGestor.size();
        Collections.sort(taulesGestor, new Comparator<Table>() {
            @Override
            public int compare(Table o1, Table o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        for (int i = 0; i < tamany; i++) {
            System.out.println("\n");
            System.out.println("----------------------------------------------");
            System.out.println((i + 1) + ".-\t\tTable: " + taulesGestor.get(i).getName());
            System.out.println("\t\t" + "Rows: " + taulesGestor.get(i).getRowsNumber());
            System.out.println("----------------------------------------------");
        }

        int what;
        do {
            what = DatabaseInput.askForaTable();
        } while (!(what > 0 && what <= tamany));

        String index = taulesGestor.get(what - 1).getIndex();
        Object which = DatabaseInput.readColumnValue(taulesGestor.get(what - 1).getColumnType(index), index);

        System.out.println(taulesGestor.get(what - 1).toString());
        taulesGestor.get(what - 1).showHistoric(index, which);

    }


    /**
     * Gestiona la primera opció del submenu de create table
     *
     */
    private static void gestioPrimeraMenu2(){
        Scanner     sc = new Scanner(System.in);
        Boolean problems = false;
        List<String> columnNames = taulaTractant.getColumnNames();
        List<DataType> columnTypes = taulaTractant.getColumnTypes();
        int tamany = columnNames.size();
        ArrayList<Object> what = new ArrayList<>();

        for (int aux = 0; aux < tamany; aux++) {
            try {
                what.add(DatabaseInput.readColumnValue(columnTypes.get(aux), columnNames.get(aux)));
            } catch (NumberFormatException e) {
                System.err.println("Wrong type of data!");
                problems = true;
                break;
            }
        }
        if (!problems) {
            TableRow row = new TableRow();
            for (int aux = 0; aux < tamany; aux++) {
                row.addColumn(columnNames.get(aux), what.get(aux));
            }

            taulesGestor.get(whereIsTheTable).addRow(row);
            System.out.println("Row added correctly!");
        }
    }


    /**
     * Gestiona la segona opció del submenu de create table
     *
     */
    private static void gestioSegonaMenu2(){
        String index = taulaTractant.getIndex();
        try {
            Object what = DatabaseInput.readColumnValue(taulaTractant.getColumnType(index), index);
            TableRowRestriction restriction = new TableRowRestriction();
            restriction.addRestriction(index, what, TableRowRestriction.RESTRICTION_EQUALS);
            System.out.println("----------------------------------");
            for ( String key : taulaTractant.getColumnNames() ) {
                System.out.print( key + "\t\t\t\t");
            }
            System.out.println("");
            System.out.println("----------------------------------");
            ArrayList<String> whatToShow = taulesGestor.get(whereIsTheTable).selectRows(restriction);
            for (String aWhatToShow : whatToShow) {
                System.out.println(aWhatToShow);
            }
        } catch (NumberFormatException e) {
            System.err.println("Wrong type of data");
        }
    }

    /**
     * Gestiona la tercera opció del submenu de create table
     */
    private static void gestioTerceraMenu2(){
        Menu m;
        do {
            System.out.println("------SELECT FROM " + taulaTractant.getName() + " ------");
            m = new Menu(PRIMERA_OPCIO, NUMERO_OPCIONS_TERCER, Menu.ES_MENU_TERCER);
            Gestor.gestionaOpcioTercerMenu(m.getOpcio());
        } while (m.getOpcio() != m.getNumOpcioSortir());
    }

    /**
     * Gestiona la primera opció del submenu de manage table que es troba en select
     */
    private static void gestioPrimeraMenu3() {
        String column = askForColumn();

        try {
            Object what = DatabaseInput.readColumnValue(taulaTractant.getColumnType(column), column);
            System.out.println(what);
            TableRowRestriction rest = addRestriction(what, column);
            restriction.add(rest);
        } catch (NumberFormatException e) {
            System.err.println("Wrong type of data!");
        }
    }

    /**
     * Gestiona la segona opció del submenu de manage table que es troba en select
     */
    private static void gestioSegonaMenu3() {
        System.out.println(taulaTractant.toString());
        if (restriction.size() < 1) {
            ArrayList<String> whatToShow = taulaTractant.selectRows(new TableRowRestriction());
            for (String aWhatToShow : whatToShow) {
                System.out.println(aWhatToShow);
            }

        } else {
            for (TableRowRestriction aRestriction : restriction) {
                ArrayList<String> whatToShow = taulaTractant.selectRows(aRestriction);
                for (String aWhatToShow : whatToShow) {
                    System.out.println(aWhatToShow);
                }

            }
        }

    }


    /**
     * Gestiona la tercera opció del submenu de manage table que es troba en select
     */
    private static void gestioTerceraMenu3() {
        restriction = new ArrayList<TableRowRestriction>();
    }


    /**
     * Gestiona la quarta opció del submenu de create table
     */
    private static void gestioCuartaMenu2(){
        String index = taulaTractant.getIndex();
        List<String> listOfColumns = taulaTractant.getColumnNames();
        Boolean problem = false;

        try {
            Object what = DatabaseInput.readColumnValue(taulaTractant.getColumnType(index), index);
            TableRowRestriction restriction = new TableRowRestriction();
            restriction.addRestriction(index, what, TableRowRestriction.RESTRICTION_EQUALS);
            TableRow row = new TableRow();
            row.addColumn(index, what);

            for (String column : listOfColumns) {
                if (column.equals(index)) {
                    continue;
                }


                String valor = taulaTractant.selectUnique(restriction, column);

                if (valor == null) {
                    problem = true;
                    break;
                }
                System.out.print("Do you want to modify the column '" + column + "' with " + valor);
                System.out.println(" value? [Y/N]");
                String answer = DatabaseInput.askYesNo();
                if (answer.equals("Y")) {
                    Object newValue = DatabaseInput.readColumnValue(taulaTractant.getColumnType(column), column);
                    row.addColumn(column, newValue);
                }
            }

            if (!problem) {
                if (taulesGestor.get(whereIsTheTable).updateRow(row)) {
                    taulaTractant = taulesGestor.get(whereIsTheTable);
                    System.out.println("Row modified. Updating table " + taulaTractant.getName() + " with the changes done.");
                }
            } else {
                System.err.println("The value you entered is not in the tree sorry!");
            }



        } catch (NumberFormatException e) {
            System.err.println("Wrong type of data");
        }
    }


    /**
     * Gestiona la Cinquena opció del submenu de create table
     */
    private static void gestioCinquenaMenu2(){
        String index = taulaTractant.getIndex();

        try {
            Object what = DatabaseInput.readColumnValue(taulaTractant.getColumnType(index), index);
            TableRowRestriction restriction = new TableRowRestriction();
            restriction.addRestriction(index, what, TableRowRestriction.RESTRICTION_EQUALS);
            ArrayList<String> whatToShow = taulaTractant.selectRows(restriction);
            if (whatToShow.size() == 0) {
                System.err.println("The value you entered is not in the tree sorry!");
                return;
            }

            System.out.println(taulaTractant.toString());
            for (String aWhatToShow : whatToShow) {
                System.out.println(aWhatToShow);
            }

            System.out.println("Are you sure you want to delete this row [Y/N]");
            String answer = DatabaseInput.askYesNo();
            if (answer.equals("Y")) {
                taulaTractant.removeRow(what);
                //taulesGestor.get(whereIsTheTable).removeRow(what);
            }

        } catch (NumberFormatException e) {
            System.err.println("Wrong type of data");
        }
    }


    /**
     * Gestiona la Sisena opció del submenu de create table
     */
    private static void gestioSisenaMenu2(){
        System.out.println("--- CSV Import for table " + taulaTractant.getName() + " ---");
        String file = DatabaseInput.askForCSVFile();
        CSVManange.setPath(file);
        System.out.println("Loading file data...");
        ArrayList<TableRow> whatToInsert = CSVManange.readCSV(taulaTractant.getColumnNames(), taulaTractant.getColumnTypes());
        for (TableRow aWhatToInsert : whatToInsert) {
            taulaTractant.addRow(aWhatToInsert);
        }
        System.out.println("Data loaded successfully. A total of " + taulaTractant.getRowsNumber() + " new rows have been inserted" +
        " into " + taulaTractant.getName() + ".");
    }


    /**
     * Gestiona la setena opció del submenu de create table
     */
    private static void gestioSetenaMenu2(){
        System.out.println("Genereting CSV file for table " + taulaTractant.getName() + " ...");
        ArrayList<HashMap> values = taulaTractant.selectOnlyColumns(new TableRowRestriction());
        CSVManange.prepareFileToExport(taulaTractant.getName());
        CSVManange.writeLines(values, taulaTractant.getColumnNames().size());
        CSVManange.endFileToExport();
        System.out.println(taulaTractant.getName() + ".csv file generated successfully with a total of " + taulaTractant.getRowsNumber() + " rows.");
    }


    /**
     * Gestiona la vuitena opció del submenu de create table
     */
    private static void gestioVuitenaMenu2(){

    }


    /************************ PARLAR AMB ROMA D'ON FICAR AQUESTS MÈTODES ******************/


    /**
     * Mètode que realitza búqueda d'una taula a partir d'una estructura. Assignarnarà valor  taulaTractant
     * per facilitar el posterior tractament de la taula
     * @param nomTable nom de la taula a buscar
     * @return on es troba la taula
     */
    private static int searchForTable (String nomTable) {
        int tamany = taulesGestor.size();
        for (int i = 0; i < tamany; i++) {
            if (taulesGestor.get(i).getName().equals(nomTable)) {
                taulaTractant = taulesGestor.get(i);
                if (taulesGestor.get(i).getDataStructure() instanceof AVL) {
                    estructura = 1;
                } else {
                    estructura = 2;
                }
                return i;
            }
        }
        return -1;

    }

    /**
     * Crida a la funció pertinent per tal que faci la petició d'una columna i comprova si la columna ja existeix
      * @return el nom de la columna en cas que aquesta no hagi estat ja inserida
     */

    private static String askForColumn(){
        Boolean status;
        String column;
        do {
            column = DatabaseInput.askForColumn();
            status = searchForColumn(taulaTractant.getColumnNames(), column) > -1;
        } while (!status);

        return column;
    }


    /**
     * Funció que afegeix columnes a la taula en cas que no sigui possible retornarà missatge d'error
     * @param table taula on s'inserirà la columna
     * @param columna columna que s'inserirà a la taula
     * @param dataType tipus de la columna que s'inserirà
     */
    private static void addColumnToTable (Table table, String columna, DataType dataType) {
        if(table.addColumn(columna, dataType)) {
            System.out.println("Columna added correctly.");
        } else {
            System.err.println("Something went wrong while adding the column! Column is repeated!");
        }

    }


    /**
     * Funció que comprovarà si la taula que es desitja inserir ja existeix en el gestor
     * @param taula nom de la taula que es desitja inserir
     * @return retornarà cert si no està ja definida, fals altrament
     */
    private static boolean tableAlreadyDefined(String taula) {
        for (int i = 0; i < taulesGestor.size(); i++) {
           if (taulesGestor.get(i).getName().equals(taula)) {
               return false;
           }
        }
        return true;
    }


    /**
     * Funció que busca si la columna solicitada existeix en la llista facilitada
     * @param possible llista de columnes existents
     * @param column columna que es desitja comprovar si està
     * @return -1 en cas que no existeixi, altrament index on es troba respecte la llista
     */
    private static int searchForColumn(List<String> possible, String column) {
        int tamany = possible.size();
        for (int a = 0; a < tamany; a++) {
            if (possible.get(a).equals(column)) {
                return a;
            }
        }
        return -1;

    }


    /**
     * Funció que retorna la restricció ja feta segons l'index i a quina columna se li ha de fer la restricció
     * @param toWhat valor restrictiu
     * @param field a quina columna realitzar restricció
     * @return restricció
     */
    private static TableRowRestriction addRestriction (Object toWhat, String field) {
        TableRowRestriction tableRowRestriction = new TableRowRestriction();
        int rest = DatabaseInput.readRestrictionType(toWhat);
        tableRowRestriction.addRestriction(field, toWhat, rest);
        return  tableRowRestriction;
    }


    /**
     * Mètode que mostra per pantalla la informaicó pertinent a les columnes d'una taula
     * i els seus respectiu tipus. Mostrarà també el nombre de files per cada taula
     * @param table taula respecte la qual es mostrarà
     */
    private static void mostrarTaules (Table table) {
        System.out.print("Table ");
        System.out.println(table.getName());
        System.out.println("\t----------------------------------------------");
        System.out.println("\tColumn\t\t\t\t\tData Type");
        System.out.println("\t----------------------------------------------");
        int size = table.getColumnNames().size();
        for (int a = 0; a < size; a++) {
            System.out.print("\t"+ String.format("%-20s ", table.getColumnNames().get(a)));
            System.out.println("\t"+ String.format("%-20s ", table.getColumnTypes().get(a)));
        }
        System.out.println("\n\t----------------------------------------------");
        System.out.println("\tNumber of rows in this table: " + table.getRowsNumber());
        System.out.println("\n\n");
    }
}





