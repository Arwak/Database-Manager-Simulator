package AVL;

import DBMSi.Table;
import DBMSi.TableDataStructure;
import DBMSi.TableRow;
import DBMSi.TableRowRestriction;

import java.util.ArrayList;

/**
 * Created by ClaudiaPeiro on 21/6/17.
 */
public class AVL extends TableDataStructure {
    private NodeAVL root;
    private long size;
    private ArrayList<TableRow> historic;


    public AVL( String index, NodeAVL node) {
        root = node;
        size = 0;
        super.setIndex(index);
    }
    @Override
    protected boolean add(TableRow tableRow) {
        whereToPlace(root,tableRow);
        return false;
    }

    @Override
    protected void select(TableRowRestriction restrictions) {
        if (restrictions.test(root.getRoot())) {
            System.out.println("----------------------------------");
            for ( String key : root.getRoot().getContent().keySet() ) {
                System.out.print( key + "\t\t\t\t");
            }
            System.out.println("");
            System.out.println("----------------------------------");
            System.out.println(root.getRoot().toString());
            //TODO en cas que no estigui en root
        }
    }

    @Override
    protected boolean update(String field, TableRow row) {
        return false;
    }

    @Override
    protected boolean remove(String field, Object value) {
        return false;
    }

    @Override
    protected long size() {
        return size;
    }

    /**
     * Mètode que s'encarregarà de buscar en quina posició s'ha de situar
     * @param tableRow
     */
    private void whereToPlace(NodeAVL actual, TableRow tableRow) {
        if(actual == null){
            root = new NodeAVL(tableRow);
            return;
        }
        System.out.println(index);
        int where = actual.getRoot().compareTo(index, tableRow);
        if(where < 1){
            if(where == 0) {
                //Element duplicat no fer res
                System.out.println("Element duplicat!");
            } else {
                if(actual.isChildLeft()){
                    whereToPlace(actual.getChildLeft(), tableRow);
                } else {
                    size++;
                    actual.setChildLeft(new NodeAVL(tableRow));
                    actual.getChildLeft().setParent(actual);
                }
            }
        } else {
            if(actual.isChildRight()){
                whereToPlace(actual.getChildRight(), tableRow);
            } else {
                size++;
                actual.setChildRight(new NodeAVL(tableRow));
                actual.getChildRight().setParent(actual);
            }

        }

    }

    public static String getName() {
        return "AVL";
    }

    public NodeAVL InOrdre (NodeAVL actual) {
        if (actual.isChildLeft()) {
            return actual.getChildLeft();
        } else {
            if (actual.isChildRight()) {
                return actual.getChildRight();
            } else {
                return actual;
            }
        }
    }

}
