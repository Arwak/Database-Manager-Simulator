package Menu;

import AVL.AVL;
import AVL.NodeAVL;
import Arbre23.Arbre;
import DBMSi.DataType;
import DBMSi.Table;
import DBMSi.TableRow;
import DBMSi.TableRowRestriction;


import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.awt.SystemColor.info;

/**
 * Created by xavierromacastells on 12/5/17.
 * * Classe que gestiona la crida a les funcions pertinents per realitzar cada opció
 */
public class Gestor {
    private final static int PRIMERA_OPCIO = 1;
    private final static int NUMERO_OPCIONS = 8;
    private static ArrayList<Table> taulesAVL;


    /**
     * Funció que permet tenir còpia d'on es guardaran totes les taules que continguin
     * un arbre AVL de la base de dades
     * @param AVLTree
     */
    public static void setAVLTree(ArrayList<Table> AVLTree) {
        taulesAVL = AVLTree;
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
     * Funcio que entrada una opció fa la crida a la funcio que la gestiona
     * correspoenent al segon menu
     * @param opcio Parametre que indica l'opció que ha de ser executada
     */
    public static void gestionaOpcioSegonMenu(int opcio, String nomTaula){
        switch (opcio) {
            case 1:
                gestioPrimeraMenu2(nomTaula);
                break;
            case 2:
                gestioSegonaMenu2(nomTaula);
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
     * Gestiona la primera opció
     *
     */
    private static void gestioPrimera(){
        Scanner     sc = new Scanner(System.in);    // Variable scanner
        String taula, columna, tipus;
        Table table = null;
        int estructura;
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
                System.err.println("Error, opcio no vàlida");
                estructura = -1;
                System.out.println("");

            }
        } while (estructura < 1 || 2 < estructura);

        do {
            System.out.print("\nEnter a name for the table: ");
            taula = sc.nextLine();
            taula = sc.nextLine();
            status = tableAlreadyDefined(taula, estructura);
            if (!status) {
                System.err.print("Error, table ");
                System.err.print(taula);
                System.err.println(" already exists,");
            }
        } while (!status);

        columna = askForColumn(taula, sc);
        dataType = searchDataType();

        switch (estructura) {
            case 1:
                table = new Table(taula, new AVL(columna, null));
                addColumnToTable(table, columna, dataType);

                break;
            case 2:
                //TODO Roma inserir table de tipus arbre23
                break;
        }

        System.out.println(table.getIndex());

        do {
            do {
                System.out.println("Would you like to add another column? [Y/N]");
                tipus = sc.nextLine();
                status = tipus.equals("Y")|| tipus.equals("N");


                if (!status) {
                    System.err.println("Error, it is a yer or no answer! You may only answer with Y or N!");
                }
            } while (!status);

            if (!("Y".equals(tipus))) {
                break;
            }

            columna = askForColumn(taula, sc);
            dataType = searchDataType();
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
                System.out.println("Which field?");
                tipus = sc.nextLine();
                if (table.getColumnNames().contains(tipus)) {
                    status = true;
                }
            }
            table.setIndex(tipus);
        }

        switch (estructura) {
            case 1:
                taulesAVL.add(table);
                break;
            case 2:
                //TODO ROMA afegir taula arbre 23
                break;

        }


    }


    /**
     * Funció que mostra per pantalla la petició d'una columna
     * @param taula nom de la taula
     * @param sc scanner per poder agafard nom de la columna
     * @return string amb el nom de la columna pel seu posterior tractament
     */
    private static String askForColumn (String taula, Scanner sc) {
        System.out.print("Enter a column name for the new table ");
        System.out.print(taula);
        System.out.println(":");
        return sc.nextLine();
    }


    /**
     * Funció que afegeix columnes a la taula
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
     * Funció que busca i comprova que el tipus de dada introduida sigui correcte
     * @return tipus de DataType, null si és incorrecte
     */
    private static DataType searchDataType () {
        Scanner sc = new Scanner(System.in);
        String tipus;
        Boolean status = false;
        DataType dataType = null;


        while (!status) {
            System.out.println("Which kind of data stores this column?");
            tipus = sc.nextLine();
            dataType = setiComprovaTipus(tipus);
            status = (dataType != null);
            if (!status) {
                System.err.print("Error, type ");
                System.err.print(tipus);
                System.err.println(" doesn't exists");
            }
        }
        return dataType;
    }


    /**
     * Funció que comprovarà si la taula que es desitja inserir ja existeix en el gestor
     * @param taula nom de la taula que es desitja inserir
     * @param estructura en quina estructura es desitja inserir (1-AVL, 2-Arbre2-3)
     * @return retornarà cert si no està ja definida, fals altrament
     */
    private static boolean tableAlreadyDefined(String taula, int estructura) {

        switch (estructura) {
            case 1:
                return !(taulesAVL.contains(taula));

            case 2:
                //Comprovació si existeix per arbre 23
                break;

        }
        return false;

    }

    /**
     * Gestiona la segona opció
     */
    private static void gestioSegona(){
        Scanner     sc = new Scanner(System.in);
        int tamanyAVL = taulesAVL.size();
        String what;

        //int tamany23
        //TODO endreçar-ho alfabèticament
        for (int i = 0; i < tamanyAVL; i++) {
            System.out.println(taulesAVL.get(i).getName());
        }
        if (tamanyAVL > 0) {
            do {
                System.out.println("Which table do you want to manage?");
                what = sc.nextLine();
            } while (taulesAVL.contains(what));

            Menu m;
            do {
                m = new Menu(PRIMERA_OPCIO, NUMERO_OPCIONS, Menu.ES_MENU_SECUNDARI);

                Gestor.gestionaOpcioSegonMenu(m.getOpcio(), what);
            }while (m.getOpcio() != m.getNumOpcioSortir());
        } else {
            System.out.println("There are no tables to manage!");
        }


    }

    /**
     * Gestiona la tercera opció
     */
    private static void gestioTercera(){
        for (int i = 0; i < taulesAVL.size(); i++) {
            System.out.println(taulesAVL.get(i));
            for (int a = 0; a < taulesAVL.get(i).getColumnNames().size(); a++) {
                System.out.println(taulesAVL.get(i).getColumnNames());
            }
        }
    }

    /**
     * Gestiona la quarta opció
     */
    private static void gestioQuarta(){

    }

    /**
     * Gestiona la primera opció del submenu de create table
     *
     * @param nomTaula
     */
    private static void gestioPrimeraMenu2(String nomTaula){
        Scanner     sc = new Scanner(System.in);
        int i = searchForTable(nomTaula, 1);
        List<String> columnNames = taulesAVL.get(i).getColumnNames();
        List<DataType> columnTypes = taulesAVL.get(i).getColumnTypes();
        int tamany = columnNames.size();
        ArrayList<String> what = new ArrayList<>();

        for (int aux = 0; aux < tamany; aux++) {
            if (columnTypes.get(aux).toString().matches("(?i)^[aeiouy].*$")) {
                System.out.print("Enter an ");
                System.out.print(columnTypes.get(aux).toString());

            } else {
                System.out.print("Enter a ");
                System.out.print(columnTypes.get(aux).toString());
            }
            System.out.print(" value for ");
            System.out.print(columnNames.get(aux));
            System.out.print(":");
            String whatToInsert = sc.nextLine();
            what.add(whatToInsert);

        }

        TableRow row = new TableRow();
        for (int aux = 0; aux < tamany; aux++) {
            row.addColumn(columnNames.get(aux), what.get(aux));
        }
        taulesAVL.get(i).addRow(row);
        System.out.println("Row added correctly!");


    }

    private static int searchForTable (String nomTable, int estructura) {
        switch (estructura) {
            case 1:
                int tamany = taulesAVL.size();
                for (int i = 0; i < tamany; i++) {
                    if (taulesAVL.get(i).getName().equals(nomTable)) {
                        return i;
                    }
                }
                break;
        }
        return -1;
    }

    /**
     * Gestiona la segona opció del submenu de create table
     * @param nomTaula
     */
    private static void gestioSegonaMenu2(String nomTaula){
        Scanner sc = new Scanner(System.in);
        int i = searchForTable(nomTaula, 1);
        String index = taulesAVL.get(i).getIndex();
        String type = taulesAVL.get(i).getColumnType(index).toString();

        if (type.matches("(?i)^[aeiouy].*$")) {
            System.out.print("Enter an ");

        } else {
            System.out.print("Enter a ");
        }
        System.out.print(type + " for " + index + ": ");
        String what = sc.nextLine();
        TableRowRestriction restriction = new TableRowRestriction();
        restriction.addRestriction(index, what, TableRowRestriction.RESTRICTION_EQUALS);
        taulesAVL.get(i).selectRows(restriction);



    }

    /**
     * Gestiona la tercera opció del submenu de create table
     */
    private static void gestioTerceraMenu2(){

    }

    /**
     * Gestiona la tercera opció del submenu de create table
     */
    private static void gestioCuartaMenu2(){

    }

    /**
     * Gestiona la Cinquena opció del submenu de create table
     */
    private static void gestioCinquenaMenu2(){

    }

    /**
     * Gestiona la Sisena opció del submenu de create table
     */
    private static void gestioSisenaMenu2(){

    }

    /**
     * Gestiona la setena opció del submenu de create table
     */
    private static void gestioSetenaMenu2(){

    }

    /**
     * Gestiona la vuitena opció del submenu de create table
     */
    private static void gestioVuitenaMenu2(){

    }


    /**
     * Comprova si existeix DataStructure seleccionat
     * @param tipus tipus de dataStructure introduit per l'usuari
     * @return retornarà el DataStructure addient, null en cas que no existeixi
     */
    public static DataType setiComprovaTipus(String tipus) {
        if(tipus.equals(DataType.BOOLEAN.toString())) {
            return DataType.BOOLEAN;
        }

        if(tipus.equals(DataType.CHAR.toString())) {
            return DataType.CHAR;
        }

        if(tipus.equals(DataType.FLOAT.toString())) {
            return DataType.FLOAT;
        }

        if(tipus.equals(DataType.LONG.toString())) {
            return DataType.LONG;
        }

        if(tipus.equals(DataType.INT.toString())) {
            return DataType.INT;
        }

        if(tipus.equals(DataType.DOUBLE.toString())) {
            return DataType.DOUBLE;
        }

        if(tipus.equals(DataType.INT.toString())) {
            return DataType.INT;
        }

        if(tipus.equals(DataType.TEXT.toString())) {
            return DataType.TEXT;
        }
        return null;
    }
}





