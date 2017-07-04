package Estructures;

import DBMSi.TableDataStructure;
import DBMSi.TableRow;
import DBMSi.TableRowRestriction;

/**
 * Created by xavierromacastells on 4/7/17.
 */
public class TaulaHashI extends TableDataStructure {

    private final static int MIDA_TAULA_R = 100;

    private long size;

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
    protected void select(TableRowRestriction restrictions) {
        for (int i = 0; i < MIDA_TAULA_R; i++) {
            taula[i].select(restrictions);
        }
    }

    @Override
    protected void selectUnique(TableRowRestriction restriction, String column) {
        for (int i = 0; i < MIDA_TAULA_R; i++) {
            taula[i].selectUnique(restriction, column);
        }
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
        TableRow tb = (TableRow) value;
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

    @Override
    protected long size() {
        return size;
    }

    private int hashString(String clau) {
        int mida = clau.length();
        int suma = 0;
        clau = clau.toLowerCase();
        for (int i = 0; i < mida; i++) {
            suma+= (clau.charAt(i) - 'a');
        }
        return suma % MIDA_TAULA_R;
    }

    private int hashInt(int clau) {
        return clau % MIDA_TAULA_R;
    }

    public static String getName() {
        return "Taula de Hash";
    }
}
