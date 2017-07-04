package Estructures;

import DBMSi.TableDataStructure;
import DBMSi.TableRow;
import DBMSi.TableRowRestriction;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by xavierromacastells on 4/7/17.
 */
public class TaulaHashI extends Taula {

    private Arbre taula[];

    public TaulaHashI(String index) {
        taula = new Arbre[MIDA_TAULA_R];
        size = 0;
        super.setIndex(index);

        for (int i = 0; i < MIDA_TAULA_R; i++)

            taula[i] = new Arbre(index);
    }

    @Override
    protected void showHistoric(String field, Object valor) {

    }

    @Override
    protected boolean add(TableRow tableRow) {
        Object obj = tableRow.getContent().get(index);
        int casella;

        //Here hash is calculated
        if (obj instanceof String) {

            casella = hashString((String) obj);

        } else {

            casella = hashInt((int) obj);

        }
        if (taula[casella].add(tableRow)) {

            size++;
            return true;

        } else {

            return false;
        }
    }

    @Override
    protected ArrayList<String> select(TableRowRestriction restrictions) {
        ArrayList<String> seleccio = new ArrayList<>();

        for (int i = 0; i < MIDA_TAULA_R; i++) {

            seleccio.addAll(taula[i].select(restrictions));

        }
        return seleccio;
    }

    @Override
    protected String selectUnique(TableRowRestriction restriction, String column) {
        String seleccio = null;

        for (int i = 0; i < MIDA_TAULA_R; i++) {

            seleccio = taula[i].selectUnique(restriction, column);

            if (seleccio != null) {

                return seleccio;
            }
        }
        return seleccio;
    }

    @Override
    protected ArrayList<HashMap> selectAllInformation(TableRowRestriction restriction) {
        ArrayList<HashMap> seleccio = new ArrayList<>();

        for (int i = 0; i < MIDA_TAULA_R; i++) {

            seleccio.addAll(taula[i].selectAllInformation(restriction));

        }
        return seleccio;
    }

    @Override
    protected boolean update(String field, TableRow row) {
        Object obj = row.getContent().get(field);
        int casella;

        //Here hash is calculated
        if (obj instanceof String) {

            casella = hashString((String) obj);

        } else {

            casella = hashInt((int) obj);

        }

        return taula[casella].update(field, row);
    }

    @Override
    protected boolean remove(String field, Object value) {
        TableRow tb = new TableRow();
        tb.addColumn(field, value);
        Object obj = tb.getContent().get(field);
        int casella;

        //Here hash is calculated
        if (obj instanceof String) {

            casella = hashString((String) obj);

        } else {

            casella = hashInt((int) obj);

        }
        if (taula[casella].remove(field, value)) {

            size--;
            return true;

        } else {

            return false;
        }
    }

    public static String getName() {
        return "Taula de Hash Improved";
    }
}
