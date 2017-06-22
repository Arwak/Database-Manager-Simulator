import Menu.Gestor;
import Menu.Menu;

/**
 * Created by xavierromacastells on 12/5/17.
 */
public class Main {
    private final static int PRIMERA_OPCIO = 1;
    private final static int NUMERO_OPCIONS = 5;
    public static void main(String[] args) throws Exception {

        gestioMenu();

    }

    /**
     * Procediment que uneix la gestio del Menu
     */
    private static void gestioMenu(){
        Menu m;
        do {
            m = new Menu(PRIMERA_OPCIO, NUMERO_OPCIONS, Menu.ES_MENU_PRINCIPAL);
            Gestor.gestionaOpcioAmbNumero(m.getOpcio());
        } while (m.getOpcio() != m.getNumOpcioSortir());
    }

}
