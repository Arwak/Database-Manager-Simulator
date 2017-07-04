package Estructures;

import DBMSi.TableDataStructure;
import DBMSi.TableRow;
import DBMSi.TableRowRestriction;

/**
 * Taula d'adreçament tancat directa amb zona d'excedents dinamica
 * Created by xavierromacastells on 4/7/17.
 */
public class TaulaHash extends TableDataStructure {

    private final static int MIDA_TAULA_R = 100;

    private long size;

    private Node taula[];

    private class Node {

        //No te clau ja que 'valor' conté la clau

        private TableRow valor;

        private Node seg;

        public Node () {
            valor = null;
            seg = null;
        }

        public Node (TableRow valor) {
            this.valor = valor;
            seg = null;
        }

    }

    public TaulaHash(String index) {
        taula = new Node[MIDA_TAULA_R];
        size = 0;
        super.setIndex(index);
    }

    @Override
    protected void showHistoric(String field, Object valor) {

    }

    @Override
    protected boolean add(TableRow tableRow) {
        Object obj = tableRow.getContent().get(index);
        Node escombra, aux;
        //Here hash is calculated
        if (obj instanceof String) {
            escombra = taula[hashString((String) obj)];
        } else {
            escombra = taula[hashInt((int) obj)];
        }
        aux = escombra;

        //Looking for the key (tableRow)
        while (escombra != null) {
            if (escombra.valor.compareTo(index, tableRow) == 0) {
                //key(tableRow) is already in the table
                return false;
            } else {
                escombra = escombra.seg;
            }
        }
        //If code gets here, then the key is not yet in the table
        if (aux != null) {
            //The table is empty for this hash
            aux.valor = tableRow;
        } else {
            escombra = new Node(tableRow);
            escombra.seg = aux.seg;
            aux.seg = escombra;
        }
        size++;

        return true;
    }

    @Override
    protected void select(TableRowRestriction restrictions) {
        Node escombra;
        for (int i = 0; i < MIDA_TAULA_R; i++) {
            escombra = taula[i];
            while (escombra != null) {
                if (restrictions.test(escombra.valor))
                    System.out.println(escombra.valor.toString());
                escombra = escombra.seg;
            }
        }

    }

    @Override
    protected void selectUnique(TableRowRestriction restriction, String column) {
        Node escombra;
        for (int i = 0; i < MIDA_TAULA_R; i++) {
            escombra = taula[i];
            while (escombra != null) {
                if (restriction.test(escombra.valor))
                    System.out.println(escombra.valor.getContent().get(column).toString());
                escombra = escombra.seg;
            }
        }
    }

    @Override
    protected boolean update(String field, TableRow row) {
        Object obj = row.getContent().get(field);
        Node escombra;
        if (obj instanceof String) {
            escombra = taula[hashString((String) obj)];
        } else {
            escombra = taula[hashInt((int) obj)];
        }
        while (escombra != null) {
            if (escombra.valor.compareTo(field, row) == 0) {
                //value found, time to update it
                escombra.valor = row;
                return true;
            } else {
                escombra = escombra.seg;
            }
        }
        return false;
    }

    @Override
    protected boolean remove(String field, Object value) {
        TableRow tb = (TableRow) value;
        Object obj = tb.getContent().get(field);
        Node escombra;
        int casella;
        if (obj instanceof String) {
            casella = hashString((String) obj);
            escombra = taula[casella];
        } else {
            casella = hashInt((int) obj);
            escombra = taula[casella];
        }

        if (escombra != null) {
            if (taula[casella].valor.compareTo(field, tb) == 0) {
                taula[casella] = taula[casella].seg;
                size--;
                return true;
            }
        } else {
            return false;
        }
        while (escombra.seg != null) {
            if (escombra.seg.valor.compareTo(field, tb) == 0) {
                //value found, time to remove it
                escombra.seg = escombra.seg.seg;
                size--;

            }
            escombra = escombra.seg;
        }
        return false;
    }

    @Override
    protected long size() {
        return 0;
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






















