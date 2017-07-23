package Estructures;

import DBMSi.TableRow;
import DBMSi.TableRowRestriction;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by XRoma i Clupspv on 21/6/17.
 */
public class TaulaHashI extends Taula {

    private Arbre23 taula[];
    private ArrayList<TableRow> historic;

    public TaulaHashI(String index) {
        taula = new Arbre23[MIDA_TAULA_R];
        size = 0;
        super.setIndex(index);
        historic = new ArrayList<>();

        /*for (int i = 0; i < MIDA_TAULA_R; i++)
            taula[i] = new Arbre23(index);*/
    }

    @Override
    protected void showHistoric(String field, Object valor) {
        for (TableRow aHistoric : historic) {
            if (aHistoric.compareTo(field, valor) == 0) {
                System.out.println(aHistoric.toString());
            }
        }
        System.out.println("\n\n");
    }

    @Override
    protected boolean add(TableRow tableRow) {
        Object obj = tableRow.getContent().get(index);
        int casella;

        //Here hash is calculated
        if (obj instanceof String) {

            casella = DJBHash((String) obj);

        } else {

            casella = otherHashInt((int) obj);

        }
        if (taula[casella] == null) {
            taula[casella] = new Arbre23(index);
        }
        if (taula[casella].add(tableRow)) {
            historic.add(tableRow);
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
            if (taula[i] != null) {
                seleccio.addAll(taula[i].select(restrictions));
            }

        }
        return seleccio;
    }

    @Override
    protected String selectUnique(TableRowRestriction restriction, String column) {
        String seleccio = null;

        for (int i = 0; i < MIDA_TAULA_R; i++) {
            if (taula[i] != null) {
                seleccio = taula[i].selectUnique(restriction, column);
            }
        }
        return seleccio;
    }

    @Override
    protected ArrayList<HashMap> selectAllInformation(TableRowRestriction restriction) {
        ArrayList<HashMap> seleccio = new ArrayList<>();

        for (int i = 0; i < MIDA_TAULA_R; i++) {

            if (taula[i] != null) {
                seleccio.addAll(taula[i].selectAllInformation(restriction));
            }

        }
        return seleccio;
    }

    @Override
    protected boolean update(String field, TableRow row) {
        Object obj = row.getContent().get(field);
        int casella;

        //Here hash is calculated
        if (obj instanceof String) {

            casella = DJBHash((String) obj);

        } else {

            casella = otherHashInt((int) obj);

        }

        if (taula[casella] == null) {
            return false;
        }
        historic.add(row);
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

            casella = DJBHash((String) obj);

        } else {

            casella = otherHashInt((int) obj);

        }


        if (taula[casella] == null) {
            return false;
        }

        if (taula[casella].remove(field, value)) {
            if (taula[casella].select(new TableRowRestriction()) == null) {
                taula[casella] = null;
            }
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
