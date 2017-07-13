package Estructures;

import DBMSi.TableDataStructure;
import DBMSi.TableRow;
import DBMSi.TableRowRestriction;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Xavier Roma on 22/6/17.
 */
public class Arbre23 extends TableDataStructure {

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

        private void reset() {
            esq = null;
            mig = null;
            dret = null;
            tbdret = null;
            tbesq = null;
            pare = null;
        }

    }

    public Arbre23(String index, Node node) {
        arrel = node;
        size = 0;
        super.setIndex(index);
    }

    public Arbre23(String index) {
        arrel = new Node();
        size = 0;
        super.setIndex(index);
    }

    public void setIndex (String index) {
        super.setIndex(index);
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
            size++;
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
    protected ArrayList<String> select(TableRowRestriction restrictions) {

        ArrayList<String> seleccio = new ArrayList<>();

        buscarSelect(arrel, restrictions, seleccio);

        return seleccio;
    }

    @Override
    protected String selectUnique(TableRowRestriction restriction, String column) {
        String seleccio = null;

        buscarUnique(arrel, restriction, column, seleccio);

        return seleccio;
    }

    @Override
    protected ArrayList<HashMap> selectAllInformation(TableRowRestriction restriction) {
        ArrayList<HashMap> tot = new ArrayList<>();

        inOrder(arrel, tot);

        return tot;
    }

    @Override
    protected boolean update(String field, TableRow row) {
        Node node = buscarNode(row, arrel);

        if (node.tbesq.compareTo(index, row) == 0) {

            node.tbesq = row;
            return true;

        } else if (node.tbdret != null && node.tbdret.compareTo(index, row) == 0) {

            node.tbdret = row;
            return true;
        }

        return false;
    }

    @Override
    protected boolean remove(String field, Object value) {

        TableRow tb = new TableRow();
        tb.addColumn(index, value);
        Node toDelete = buscarNode(tb, arrel);

        if (toDelete.tbesq.compareTo(index, tb) != 0 && (toDelete.tbdret == null || toDelete.tbdret.compareTo(index, tb) != 0)) {
            return false;
        }

        if (toDelete.esq != null) {

            toDelete = swapToDelete(toDelete, tb);
        }

        size--;
        if (size == 0) {

            arrel.reset();

        } else {

            deleteLeaf(tb, toDelete);
        }
        return true;
    }

    @Override
    protected long size() {
        return size;
    }

    private void buscarSelect(Node node, TableRowRestriction rest, ArrayList<String> seleccio) {

        if(node != null && node.tbesq != null) {

            if (rest.test(node.tbesq)) {

                seleccio.add(node.tbesq.toString());

            }

            buscarSelect(node.esq, rest, seleccio);
            buscarSelect(node.mig, rest, seleccio);

            if (node.tbdret != null) {

                if (rest.test(node.tbdret)) {

                    seleccio.add(node.tbdret.toString());
                }

                buscarSelect(node.dret, rest, seleccio);
            }
        }
    }

    private void buscarUnique(Node node, TableRowRestriction rest, String column, String troballa) {

        if(node != null) {

            if (rest.test(node.tbesq)) {

                troballa = node.tbesq.getContent().get(column).toString();
            }

            buscarUnique(node.esq, rest, column, troballa);
            buscarUnique(node.mig, rest, column, troballa);

            if (node.tbdret != null) {

                if (rest.test(node.tbdret)) {

                    troballa = node.tbdret.getContent().get(column).toString();
                }

                buscarUnique(node.dret, rest, column, troballa);
            }
        }
    }

    public static String getName() {
        return "Arbre 2-3";
    }

    private Node buscarNode (TableRow row, Node node) {
        Node aux;


        if (node.esFulla()) {

            aux = node;

        } else {

            int comparacio = row.compareTo(index, node.tbesq);

            if (comparacio == 0) {

                return node;
            }
            if (node.tbdret == null) {
                // is 2-node

                if (comparacio == -1) {

                    aux = buscarNode(row, node.esq);

                } else {

                    aux = buscarNode(row, node.mig);

                }
            } else {
                // else is 3-node
                if (comparacio == -1) {

                    aux = buscarNode(row, node.esq);


                } else {

                    comparacio = row.compareTo(index, node.tbdret);

                    if (comparacio == 0) {

                        return node;
                    }
                    if (comparacio == -1) {

                        aux = buscarNode(row, node.mig);

                    } else {

                        aux = buscarNode(row, node.dret);

                    }

                }
            }
        }

        if (aux == null) {
            return arrel;
        }

        return aux;
    }

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
        int comparacio;

        if (node.tbdret == null) {
            //is 2-node

            comparacio =  node.tbesq.compareTo(index, row);

            if (comparacio == 1) {
                //key in the node bigger than new key

                node.tbdret = node.tbesq;
                node.tbesq = row;
                return true;

            } else if (comparacio == -1) {
                //key in the node smaller than new key

                node.tbdret = row;
                return true;

            } else {
                //row already exists
                return false;
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
                return true;

            } else if (comparacio == -1) {

                comparacio = node.tbdret.compareTo(index, row);

                if (comparacio == 1) {
                    //right key in the node bigger than new key
                    //new key will be lifted

                    Node esq = new Node(node.tbesq);
                    Node mig = new Node(node.tbdret);

                    split(esq, mig, row, node.pare);
                    return true;

                } else if (comparacio == -1) {
                    //right key in the node smaller than new key
                    //right key will be lifted

                    Node esq = new Node(node.tbesq);
                    Node mig = new Node(row);

                    split(esq, mig, node.tbdret, node.pare);
                    return true;

                } else {
                    //row already exists
                    return false;
                }

            } else {
                // row already exists
                return false;
            }



        }

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

    private Node swapToDelete (Node node, TableRow tb) {
        Node nodeFulla; // node on posarem el valor que volem borrar
        TableRow table; // var per fer el swap

        if (node.tbesq.compareTo(index, tb) == 0) {
            //tb to be deleted is in node.tbesq

            nodeFulla = bringNode(node.esq);

            if (nodeFulla.tbdret != null) {

                table = nodeFulla.tbdret;
                nodeFulla.tbdret = tb;

            } else {

                table = nodeFulla.tbesq;
                nodeFulla.tbesq = tb;

            }

            node.tbesq = table;

        } else {
            //tb to be deleted is in node.tbdret

            nodeFulla = bringNode(node.mig);

            if (nodeFulla.tbdret != null) {

                table = nodeFulla.tbdret;
                nodeFulla.tbdret = tb;

            } else {

                table = nodeFulla.tbesq;
                nodeFulla.tbesq = tb;
            }

            node.tbdret = table;
        }

        return nodeFulla;
    }

    private Node bringNode (Node node) {

        if (node.esq == null) {
            //is leaf

            return node;
        }
        if (node.dret != null) {

            return bringNode(node.dret);

        } else {

            return bringNode(node.mig);
        }

    }

    private void deleteLeaf (TableRow tb, Node actual) {

        if (actual.tbesq.compareTo(index, tb) == 0) {

            actual.tbesq = actual.tbdret;

            if (actual.tbdret == null) {

                balance(actual);

            } else {

                actual.tbdret = null;

            }

        } else {

            actual.tbdret = null;
        }

    }

    private void balance(Node actual) {

        if (actual.pare == null && actual.tbesq == null &&  actual.esq != null) {

            arrel = actual.esq;

        } else if (actual.pare.tbesq == null && actual.tbesq == null &&  actual.esq != null && actual.pare.mig == null) {
            arrel = actual.esq;
        } else {
            // [Start:case3:case 2]

            if (actual.pare.tbdret == null && actual.pare.esq.tbdret == null && actual.pare.mig.tbdret == null) {
                //daddy and childs 2-node

                if (actual.pare.mig == actual) {
                    //actual is the mid child

                    actual.pare.esq.tbdret = actual.pare.tbesq;
                    actual.pare.esq.dret = actual.esq;

                    if (actual.esq != null) {

                        actual.esq.pare = actual.pare.esq;
                    }

                    actual.pare.mig = null;
                    actual.pare.tbesq = null;
                    balance(actual.pare);

                } else if (actual.pare.esq == actual) {
                    //actual is the left child



                    actual.tbesq = actual.pare.tbesq;
                    actual.tbdret = actual.pare.mig.tbesq;
                    actual.mig = actual.pare.mig.esq;
                    actual.dret = actual.pare.mig.mig;

                    if (actual.pare.mig.esq != null) {

                        actual.pare.mig.esq.pare = actual;
                    }
                    actual.pare.mig = null;
                    actual.pare.tbesq = null;
                    balance(actual.pare);
                }

                // [End:case3:case 2]
            } else {
                //Father is 3-node [Start:case 2]

                if (actual.pare.dret == actual) {

                    //actual is the right node
                    if (actual.pare.mig.tbdret != null) {

                        //bingo the mid brother has two keys, time to borrow one
                        actual.tbesq = actual.pare.tbdret;
                        actual.pare.tbdret = actual.pare.mig.tbdret;
                        actual.pare.mig.tbdret = null;

                        actual.mig = actual.esq;
                        actual.esq = actual.pare.mig.dret;


                        if (actual.pare.mig.dret != null) {

                            actual.pare.mig.dret.pare = actual;
                        }
                        actual.pare.mig.dret = null;

                    } else {
                        //little nightmare [Start:case3-case1]
                        actual.pare.mig.tbdret = actual.pare.tbdret;
                        actual.pare.tbdret = null;
                        actual.pare.mig.dret = actual.esq;
                        actual.pare.dret = null;

                        if (actual.esq != null) {

                            actual.esq.pare = actual.pare.mig;
                        }
                        //[End:case3:case1]
                    }

                } else if (actual.pare.mig == actual) {
                    //actual is the mid node

                    if (actual.pare.esq.tbdret != null) {
                        //left brother has 2 keys

                        actual.tbesq = actual.pare.tbesq;
                        actual.pare.tbesq = actual.pare.esq.tbdret;
                        actual.pare.esq.tbdret = null;

                        actual.mig = actual.esq;
                        actual.esq = actual.pare.esq.dret;
                        actual.pare.esq.dret = null;

                        if (actual.esq != null) {

                            actual.esq.pare = actual;
                        }
                        //well IT HAD two keys ja ja haa

                    } else if (actual.pare.dret.tbdret != null) {
                        //right brother has two keys luckly is not my real-life brother

                        actual.tbesq = actual.pare.tbdret;
                        actual.pare.tbdret = actual.pare.dret.tbesq;
                        actual.pare.dret.tbesq = actual.pare.dret.tbdret;
                        actual.pare.dret.tbdret = null;

                        actual.mig = actual.pare.dret.esq;
                        actual.pare.dret.esq = actual.pare.dret.mig;
                        actual.pare.dret.mig = actual.pare.dret.dret;
                        actual.pare.dret.dret = null;

                        if (actual.mig != null) {

                            actual.mig.pare = actual;
                        }

                    } else {
                        //[Start:case3-case1]

                        if (actual.esq != null) {

                            actual.esq.pare = actual.pare.esq;
                            actual.pare.dret.esq.pare = actual;
                            actual.pare.dret.mig.pare = actual;
                        }

                        actual.pare.esq.tbdret = actual.pare.tbesq;
                        actual.pare.tbesq = actual.pare.tbdret;
                        actual.pare.tbdret = null;
                        actual.pare.esq.dret = actual.esq;
                        actual.pare.mig = actual.pare.dret;
                        actual.pare.dret = null;
                        //[End:case3:case1]
                    }

                } else if (actual.pare.esq == actual) {
                    //actual is the left brother

                    if (actual.pare.mig.tbdret != null) {
                        //mid brother has two keys

                        if (actual.pare.mig.esq != null) {

                            actual.pare.mig.esq.pare = actual;
                        }

                        actual.tbesq = actual.pare.tbesq;
                        actual.pare.tbesq = actual.pare.mig.tbesq;
                        actual.pare.mig.tbesq = actual.pare.mig.tbdret;
                        actual.pare.mig.tbdret = null;

                        actual.mig = actual.pare.mig.esq;
                        actual.pare.mig.esq = actual.pare.mig.mig;
                        actual.pare.mig.mig = actual.pare.mig.dret;
                        actual.pare.mig.dret = null;

                    } else {
                        //[Start:case3-case1]

                        actual.tbesq = actual.pare.tbesq;
                        actual.tbdret = actual.pare.mig.tbesq;
                        actual.pare.tbesq = actual.pare.tbdret;
                        actual.mig = actual.pare.mig.esq;
                        actual.dret = actual.pare.mig.mig;
                        actual.pare.mig = actual.pare.dret;
                        actual.pare.dret = null;
                        actual.pare.tbdret = null;

                        if (actual.mig != null) {

                            actual.mig.pare = actual;
                        }

                        if (actual.dret != null) {

                            actual.dret.pare = actual;
                        }
                        //[End:case3:case1]
                    }
                }
                // [End:case 2]
            }
        }
    }

    private void inOrder(Node actual, ArrayList<HashMap> ordre) {

        if (actual != null) {

            if (actual.esq == null) {
                //is leaf

                ordre.add(actual.tbesq.getContent());

                if (actual.tbdret != null)
                    ordre.add(actual.tbdret.getContent());

            } else if (actual.tbdret == null) {
                //2-node

                inOrder(actual.esq, ordre);
                ordre.add(actual.tbesq.getContent());
                inOrder(actual.mig, ordre);

            } else {
                //3-node

                inOrder(actual.esq, ordre);
                ordre.add(actual.tbesq.getContent());
                inOrder(actual.mig, ordre);
                ordre.add(actual.tbdret.getContent());
                inOrder(actual.dret, ordre);

            }
        }
    }

    public void preOrder() {

        if (arrel.tbesq != null) {

            preOrderI(arrel);
        }
        else System.out.println("The tree is empty");
    }

    private void preOrderI(Node current) {

        if(current != null) {

            System.out.println(current.tbesq.getContent().get(index).toString());
            preOrderI(current.esq);
            preOrderI(current.mig);

            if (current.tbdret != null) {

                System.out.println(current.tbdret.getContent().get(index).toString());
                    preOrderI(current.dret);

            }
        }
    }


}

/*
        esborrar ‘a’ d’una fulla

cas 1
	la fulla te 2 claus, borrem la que toque i les ordenem si cal
cas 2
		9.			->				7

	4.7		‘a’.	->		4				9

T1	T2	T3	Ta		->	T1		T2		T3		Ta

	Si germa adjaçent de ‘a’ te 2 claus, aleshores el germa pasa 1 clau al pare i el
	pare pasa la clau a on estava ‘a’, si el germa adjaçent nms te 1 clau pero el
	germa més lluny en te dos, aleshores aquest pasa una clau al pare, el pare li
	pasa al germa del mig i el del mig li torna l’altra clau al pare que aquest li dona
	al fill on estava ‘a’
cas 3
	Cap germa te més d’una clau
        Cas 1

        		4.9				->				6

	2			6		‘a’		->		2.4				9

T1		T2	T3		T4	 T		->	T1	T2	T3		T4		T

            Pare te 2 claus
            Pare pasa una clau al germa que li falta, es desfa un fill i un
            altre fill agafa una clau i esdeve 3-node els germans agafen els
            arbres que tenia el germa desfet

        Cas 2
            l’unic cas recursiu
	        El node esborrat només te un germa (pare 2-node), germa es 2-node.
	        No podem solucionar el problema amb aquest nivell, així que unim el pare amb
	        el germa no esborrat, aquest germa agafarà l’arbre del germa esborrat i
	        pasarem el problema cap als nivells superiors
	*/