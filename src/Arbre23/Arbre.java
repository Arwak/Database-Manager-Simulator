package Arbre23;

import DBMSi.TableDataStructure;
import DBMSi.TableRow;
import DBMSi.TableRowRestriction;

import java.util.ArrayList;

/**
 * Created by Xavier Roma on 22/6/17.
 */
public class Arbre extends TableDataStructure {
    private final static String EMPTY_VALUE = "GAYNS Ϲ(^.^)Ↄ ";
    private Node arrel;
    private long size;
    private ArrayList<TableRow> historic;

    private class Node {

        private Node esq;

        private Node mig;

        private Node dret;

        private Node pare;

        private TableRow tbdret;

        private TableRow tbesq;

        public Node () {
            esq = null;
            mig = null;
            dret = null;
            tbdret = null;
            tbesq = null;
            pare = null;
        }

        public Node (TableRow esquerra) {
            esq = null;
            mig = null;
            dret = null;
            tbdret = null;
            tbesq = esquerra;
            pare = null;
        }


        /*
            No haurà elements al mig i a la dreta si no n'hi ha a l'esquerra
         */
        private boolean esFulla() {

            return esq == null;
        }

    }

    public Arbre(String index, Node node) {
        arrel = node;
        size = 0;
        super.setIndex(index);
    }
    public Arbre(String index) {
        arrel = new Node();
        size = 0;
        super.setIndex(index);
    }

    public boolean afegir(TableRow hola){
        return add(hola);
    }

    @Override
    protected void showHistoric(String field, Object valor) {

    }

    @Override
    protected boolean add(TableRow tableRow) {

        if(arrel == null) {
            //tree is not yet created
            arrel = new Node();
        }
        if (arrel.tbesq == null) {
            //tree is empty
            arrel.tbesq = tableRow;

            return true;

        } else {

            if (insert(tableRow, search(tableRow, arrel))) {
                //tableRow inserted
                size++;
                return true;
            }
            //tableRow already exists
            return false;

        }

    }


    @Override
    protected void select(TableRowRestriction restrictions) {

    }

    @Override
    protected void selectUnique(TableRowRestriction restriction, String column) {

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

    public static String getName() {
        return "Arbre 2-3";
    }

    /**
     *
     * @param row
     * @param node
     * @return
     */
    private Node search(TableRow row, Node node) {
        /*
       CERCAR un element ‘a’
        Si 2-node
            si a < x
                busquem pel 1r fill
            si a >= x
                busquem pel 2n fill
        Si 3-node
            si a < x
                busquem pel 1r fill
            si a >= x && a < y
                busuem pel 2n fill
            si a >= y
                busquem pel 3r fill

        Quan s’arriba a una fulla, si aquesta és igual a ‘a’ llavors la cerca és satisfactoria
         */
        Node aux;

        if (node.esFulla()) {

            aux = node;

        } else {
            if (node.tbdret == null) {
                // is 2-node
                if (row.compareTo(index, node.tbesq) == -1) {
                    aux = search(row, node.esq);
                } else {
                    aux = search(row, node.mig);
                }
            } else {
                // else is 3-node
                if (row.compareTo(index, node.tbesq) == -1) {
                    aux = search(row, node.esq);
                } else {
                    if (row.compareTo(index, node.tbdret) == -1) {
                        aux = search(row, node.mig);
                    } else {
                        aux = search(row, node.dret);
                    }

                }
            }
        }
        if (aux == null) {
            return arrel;
        }
        return aux;
    }




    private boolean insert (TableRow row, Node node){

        /*
        es CERCA ‘a’, quan s’arriba a les fulles i aquestes no contenen ‘a’

        Inseri un element ‘a’ a node
            Si 2-node
                ‘a’ passa a ser un nou fill, posantho en l’ordre correcte
            Si 3-node
                “node” -> hi posem els 2 més petits
                                o
                     -> hi posem els 2 més grans
                “node” serà afegit al pare
                    si 2-node
                        tot OK
                    si 3-node
                        split (el mateix proces)
         */
        boolean status = false;
        int comparacio;
        if (node.tbdret == null) {
            //is 2-node
            comparacio =  node.tbesq.compareTo(index, row);

            if (comparacio == 1) {
                //key in the node bigger than new key
                node.tbdret = node.tbesq;
                node.tbesq = row;
                status = true;
            } else if (comparacio == -1) {
                //key in the node smaller than new key
                node.tbdret = row;
                status = true;
            } else {
                //row already exists
            }
        } else {
            //is 3-node
            comparacio =  node.tbesq.compareTo(index, row);

            if (comparacio == 1) {
                //left key in the node bigger than new key
                //left key will be lifted
                Node esq = new Node(row);
                Node mig = new Node(node.tbdret);
                split(esq, mig, node.tbesq, node.pare);

            } else if (comparacio == -1) {

                comparacio = node.tbdret.compareTo(index, row);

                if (comparacio == 1) {
                    //right key in the node bigger than new key
                    //new key will be lifted
                    Node esq = new Node(node.tbesq);
                    Node mig = new Node(node.tbdret);

                    split(esq, mig, row, node.pare);

                } else if (comparacio == -1) {
                    //right key in the node smaller than new key
                    //right key will be lifted
                    Node esq = new Node(node.tbesq);
                    Node mig = new Node(row);

                    split(esq, mig, node.tbdret, node.pare);

                } else {
                    //row already exists
                }

            } else {
                // row already exists
            }



        }

        return status;
    }


    private void split(Node nou_esq, Node nou_mig, TableRow nou, Node pare) {

        if (pare != null) {
            if (pare.tbdret == null) {
                // is 2-node
                int comparacio = pare.tbesq.compareTo(index, nou);
                nou_esq.pare = pare;
                nou_mig.pare = pare;

                if (comparacio == -1) {
                    //left key is smaller than new key

                    pare.tbdret = nou;
                    pare.mig = nou_esq;
                    pare.dret = nou_mig;

                } else if (comparacio == 1) {
                    //left key is bigger than new key
                    pare.tbdret = pare.tbesq;
                    pare.tbesq = nou;
                    pare.esq = nou_esq;
                    pare.dret = pare.mig;
                    pare.mig = nou_mig;

                } else {
                    /**
                     * crec que no pot passar (que l'element ja hi sigui),
                     * comprovarho, si pot passar afegir als altres casos de sota
                     **/
                }

            } else {
                //is 3-node
                int comparacio = pare.tbesq.compareTo(index, nou);

                if (comparacio == 1) {
                    //left key is bigger than new key
                    //left key will be lifted
                    Node left = new Node(nou);
                    left.esq = nou_esq;
                    left.mig = nou_mig;
                    nou_esq.pare = nou_mig.pare = left;
                    Node mid = new Node(pare.tbdret);
                    mid.esq = pare.mig;
                    mid.mig = pare.dret;
                    pare.dret.pare = pare.mig.pare = mid;
                    //created 2 nodes, with the smallest and the biggest key from this node and the new key,
                    //we need to split this node and lift ip the mid key
                    split(left, mid, pare.tbesq, pare.pare);


                } else if (comparacio == -1) {
                    //left key is smaller than new key
                    comparacio = pare.tbdret.compareTo(index, nou);

                    if (comparacio == 1) {
                        //right key is bigger than new key
                        //new key will be lifted
                        Node left = new Node(pare.tbesq);
                        left.esq = pare.esq;
                        left.mig = nou_esq;
                        pare.esq.pare = nou_esq.pare = left;
                        Node mid = new Node(pare.tbdret);
                        mid.esq = nou_mig;
                        mid.mig = pare.dret;
                        pare.dret.pare = nou_mig.pare = mid;
                        split(left, mid, nou, pare.pare);

                    } else if (comparacio == -1) {
                        //right key is smaller than new key
                        //right key will be lifted
                        Node left = new Node(pare.tbesq);
                        left.esq = pare.esq;
                        left.mig = pare.mig;
                        pare.esq.pare = pare.mig.pare = left;
                        Node mig = new Node(nou);
                        mig.esq = nou_esq;
                        mig.mig = nou_mig;
                        nou_esq.pare = nou_mig.pare = mig;
                        split(left, mig, pare.tbdret, pare.pare);

                    }

                }
            }
        } else {
            pare = new Node();
            arrel = pare;
            pare.tbesq = nou;
            pare.esq = nou_esq;
            pare.mig = nou_mig;
            nou_esq.pare = nou_mig.pare = pare;
        }
    }

    public void testar() {
        test(arrel);
    }
    private void test(Node node) {

        if(node != null) {

            System.out.println(node.tbesq.toString());
            test(node.esq);
            test(node.mig);

            if (node.tbdret != null) {

                System.out.println(node.tbdret.toString());
                test(node.dret);
            }
        }
    }




}