package AVL;

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
    private String index;
    private ArrayList<TableRow> historic;

    public AVL(String index, NodeAVL node) {
        root = node;
        size = 0;
        this.index = index;
    }
    @Override
    protected boolean add(TableRow tableRow) {
        whereToPlace(root,tableRow);
        return false;
    }

    @Override
    protected void select(TableRowRestriction restrictions) {

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
        int where = actual.getRoot().compareTo(index, tableRow);
        if(where < 1){
            if(where == 0) {
                //Element duplicat no fer res
            } else {
                if(actual.isChildLeft()){
                    whereToPlace(actual.getChildLeft(), tableRow);
                } else {
                    actual.setChildLeft(new NodeAVL(tableRow));
                }
            }
        } else {
            if(actual.isChildRight()){
                whereToPlace(actual.getChildRight(), tableRow);
            } else {
                actual.setChildRight(new NodeAVL(tableRow));
            }

        }

    }
}
