package Menu;


import java.util.Scanner;

/**
 * Created by xavierromacastells on 12/5/17.
 */
public class Menu {


    public static final int ES_MENU_PRINCIPAL = 0;
    public static final int ES_MENU_SECUNDARI = 1;
    public static final int ES_MENU_TERCER = 2;
    private int numOpcioSortir;
    private int numOpcions;
    private int opcioInicial;
    private String nomTaula;

    private int opcio;  // Variable que guarda l'opcio introduida per l'usuari
    /**
     * Constructor de la classe
     * Gestiona tota la classe amb una crida
     */
    public Menu(int opcioInicial, int numOpcions, int typeMenu) {
        this.opcioInicial = opcioInicial;
        this.numOpcions = numOpcions;
        this.numOpcioSortir = numOpcions;
        opcio = 0;
        procMenu(typeMenu);
        while (opcio < opcioInicial || opcio > numOpcions) {
            procMenu(typeMenu);
        }
    }

    public int getOpcio() {
        return opcio;
    }

    /**
     * Procediment que recolleix el procediment necessari per imprimir, llegir
     * i gestiona l'opcio
     */
    private void procMenu(int typeMenu) {

        if (typeMenu == ES_MENU_PRINCIPAL) {
            printMenu();
        } else {
            if (typeMenu == ES_MENU_SECUNDARI) {
                printMenuSecundari();
            } else {
                printMenuTercer();
            }

        }
        llegeixTeclat();
    }

    /**
     * Procediment que imprimeix el menu principal per consola
     */
    private void printMenu() {
        System.out.println("1. Create table");
        System.out.println("2. Manage table");
        System.out.println("3. Visualize tables");
        System.out.println("4. Visualize historical data");
        System.out.println("5. Shut down");
        System.out.print("\nSelect an option: ");
    }

    /**
     * Procediment que imprimeix el menu secundari per consola
     */
    private void printMenuSecundari() {
        System.out.println("\t1. Insert");
        System.out.println("\t2. Show row by index");
        System.out.println("\t3. Select");
        System.out.println("\t4. Update row");
        System.out.println("\t5. Remove row by index");
        System.out.println("\t6. Import from CSV");
        System.out.println("\t7. Export to CSV");
        System.out.println("\t8. Main menu");
    }

    private void printMenuTercer() {
        System.out.println("\t1. Stablish condition");
        System.out.println("\t2. Execute select");
        System.out.println("\t3. Exit");
    }

    /**
     * Procdiment que llegeix l'entrada del teclat per consola
     */
    private void llegeixTeclat() {

        Scanner     sc = new Scanner(System.in);    // Variable scanner

        /* Controla que l'entrada sigui númerica */
        try {
            /* Llegim el teclat */
            opcio = sc.nextInt();
        } catch (java.util.InputMismatchException e) {
            System.err.println("Error, opcio no vàlida");

        }
            /* Opcio dins del rang [OPCIO_INICIAL..NUM_OPCIONS]? */
        if (opcio < opcioInicial || opcio > numOpcions){

            System.out.println("ERROR: Opcio no valida");
            System.out.println("");
            /* Es forca valor per defecte [0] */
            opcio = 0;
        }




    }

    public void setNumOpcioSortir(int numOpcioSortir){
        this.numOpcioSortir = numOpcioSortir;
    }

    public int getNumOpcioSortir(){
        return numOpcioSortir;
    }

    public void setNomTaula (String nomTaula) {
        this.nomTaula = nomTaula;
    }
}