package Menu;

import AVL.AVL;
import Arbre23.Arbre;
import Tree23.Arbre23;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Created by xavierromacastells on 12/5/17.
 * * Classe que gestiona la crida a les funcions pertinents per realitzar cada opció
 */
public class Gestor {
    private final static int PRIMERA_OPCIO = 1;
    private final static int NUMERO_OPCIONS = 8;

    /**
     * Funcio que entrada una opció fa la crida a la funcio que la gestiona
     * corresponent al primer menú
     * @param opcio Parametre que indica l'opció que ha de ser executada
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
    public static void gestionaOpcioSegonMenu(int opcio){
        switch (opcio) {
            case 1:
                gestioPrimeraMenu2();
                break;
            case 2:
                gestioSetenaMenu2();
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
        StringBuilder sb = new StringBuilder();
        Scanner     sc = new Scanner(System.in);    // Variable scanner
        String taula, columna, tipus;
        int estructura;
        boolean status = true;

        do {
            System.out.print("\nEnter a name for the table: ");
            taula = sc.nextLine();
            //status = tableAlreadyDefined(taula);
            if (!status) {
                System.out.print("Error, table ");
                System.out.print(taula);
                System.out.println(" already exists,");
            }
        } while (!status);

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
            System.out.print("Enter a column name for the new table ");
            System.out.print(taula);
            System.out.println(":");
            columna = sc.nextLine();
            //status = columnAlreadyDefined(taula, columna);
            if (!status) {
                System.out.print("Error, column ");
                System.out.print(columna);
                System.out.println(" already exists,");
            }
        } while (!status);

        System.out.println("Which kind of data stores this column?");
        tipus = sc.nextLine();

    }

    /**
     * Gestiona la segona opció
     */
    private static void gestioSegona(){
        Menu m;
        do {
            m = new Menu(PRIMERA_OPCIO, NUMERO_OPCIONS, Menu.ES_MENU_SECUNDARI);

            Gestor.gestionaOpcioSegonMenu(m.getOpcio());
        }while (m.getOpcio() != m.getNumOpcioSortir());

    }

    /**
     * Gestiona la tercera opció
     */
    private static void gestioTercera(){

    }

    /**
     * Gestiona la quarta opció
     */
    private static void gestioQuarta(){

    }

    /**
     * Gestiona la primera opció del submenu de create table
     *
     */
    private static void gestioPrimeraMenu2(){


    }

    /**
     * Gestiona la segona opció del submenu de create table
     */
    private static void gestioSegonaMenu2(){
        Menu m;
        do {
            m = new Menu(PRIMERA_OPCIO, NUMERO_OPCIONS, Menu.ES_MENU_SECUNDARI);

            Gestor.gestionaOpcioSegonMenu(m.getOpcio());
        }while (m.getOpcio() != m.getNumOpcioSortir());

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



}





