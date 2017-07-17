package Estructures;

import DBMSi.TableDataStructure;
import DBMSi.TableRow;
import DBMSi.TableRowRestriction;

import java.util.*;

/**
 * Created by XRoma i Clupspv on 21/6/17.
 *
 * Classe encarregada de gestionar tot allò relatiu amb els arbres AVL.
 *
 */
public class AVL extends TableDataStructure {
    private NodeAVL root;
    private long size;
    private ArrayList<TableRow> historic;


    /**
     * Constructor de la classe
     * @param index clau primària de l'arbre
     * @param node primer node. Serà el node des d'on partirà tot l'arbre.
     */
    public AVL( String index, NodeAVL node) {
        root = node;
        size = 0;
        historic = new ArrayList<>();
        super.setIndex(index);
    }

    public AVL() {
        size = 0;
        historic = new ArrayList<>();
    }

    public void setIndex (String index) {
        super.setIndex(index);
    }


    /**
     * Mètode que permet veure l'historial d'accions. Sabent que es tindran
     * guardades todes les modificacions en un arraylist caldrà restringir aquelles
     * que es dessitgin veure.
     * @param field columna que esta restringida
     * @param valor valor que està restringit
     */
    @Override
    protected void showHistoric(String field, Object valor) {

        for (TableRow aHistoric : historic) {
            if (aHistoric.compareTo(field, valor) == 0) {
                System.out.println(aHistoric.toString());
            }
        }
        System.out.println("\n\n");
    }


    /**
     * Mètode que permet afegir en l'estructura una nova fila. Mirarà si és un element
     * duplicat. En cas contrari, realitzarà una cerca dicotòmica per situar el node en
     * l'extrem pertinent. Realitzarà l'esmentada búsqueda comparant el valor de l'index.
     * Un cop acabat el procés d'addició d'una nova fila es recalcularan les alçades i
     * es comprovarà si cal realitzar algun moviment. En cas afirmatiu, rebalancejarà l'arbre
     * amb el moviment pertinent.
     * @param tableRow La nova fila a afegir.
     *
     * @return sempre fals
     */
    @Override
    protected boolean add(TableRow tableRow) {
        whereToPlace(root,tableRow);
        return false;
    }


    /**
     * Mètode que retorna array de strings pertinents a les files que compleixen unes certes
     * restriccions.
     * @param restrictions  Restriccions per tal de filtrar files en la visualització.
     * @return Arraylists de strings on estan les files que han complert les restioccions
     */
    @Override
    protected ArrayList<String> select(TableRowRestriction restrictions) {
        ArrayList<String> what = new ArrayList<>();
        return whatToShow(root, restrictions, what);
    }


    /**
     * Mètode que permet retornar una string amb el contingut d'una específica columna i fila.
     * @param restriction Restriccions per tal de filtrar files en la visualització.
     * @param column Restriccions per tal de filtrar tant sols el valor d'una columna en la visualització.
     * @return
     */
    @Override
    protected String selectUnique(TableRowRestriction restriction, String column) {
        return whatToShowUnique(root, restriction, column);
    }


    /**
     * Mètode que permet retornar el hashMap que es troba en el contigut del node que
     * compleixi la restricció solicitada.
     * @param restriction  restricció que ha de complir.
     * @return arrayList de hashmaps.
     */
    @Override
    protected ArrayList<HashMap> selectAllInformation(TableRowRestriction restriction) {
        ArrayList<HashMap> what = new ArrayList<>();
        return whatToContent(root, restriction, what);
    }


    /**
     * Mètode que s'utilitza per actualitzar la informació d'un arbre. En cas que
     * l'element no existeixi retornarà un error.
     * @param field El camp pel qual cercar la fila existent.
     * @param row   El contingut actualitzat de la fila.
     * @return fals en cas de trobar algun problema, cert altrament.
     */
    @Override
    protected boolean update(String field, TableRow row) {
        return whatToModify(root, row, field);
    }


    /**
     * Mètode que s'utilitza per eliminar un element de l'arbre. Eliminarà el node sol·licitat, restribuirà
     * nodes si escau, recalcularà alçades i revisarà que l'arbre estigui balancejat.
     * @param field El nom del camp o columna.
     * @param value El valor que ha de tenir el camp.
     * @return fals en cas de trobar algun problema, cert altrament.
     */
    @Override
    protected boolean remove(String field, Object value) {
        whatToDelete(root, field, value, false);
        return true;
    }


    /**
     * Mètode que retorna el tamany de l'arbre.
     * @return tamany de l'arbre
     */
    @Override
    protected long size() {
        return size;
    }

    /**
     * Mètode que s'encarregarà de buscar en quina posició s'ha de situar mitjançant una
     * cerca dicotòmica. Trobada la posició s'inserirà el nou element, s'actualitzarà el
     * nombre d'elements totals que hi ha en l'arbre (variable size). A més a més, es
     * recalcularan les alçades que s'hagin pogut veure modificades a causa de la nova
     * inserció i es comprovarà que l'arbre estigui balancejat. Si escau realitzarà moviments
     * pertinents per rebalancejar-lo.
     * @param tableRow nou element a inserir
     */
    private void whereToPlace(NodeAVL actual, TableRow tableRow) {
        if(actual == null){
            root = new NodeAVL(tableRow);
            historic.add(tableRow);
            size++;
            return;
        }

        int where = actual.getRoot().compareTo(index, tableRow);
        if(where < 1){
            if(where == 0) {
                //Element duplicat no fer res
                System.err.println("Element duplicat!");
            } else {
                if(actual.getChildRight() != null){
                    whereToPlace(actual.getChildRight(), tableRow);
                } else {
                    size++;
                    actual.setChildRight(new NodeAVL(tableRow));
                    actual.getChildRight().setParent(actual);
                    historic.add(tableRow);
                    reCalculHeigh(actual);
                    calculBalance(actual, true);
                }
            }
        } else {
            if(actual.getChildLeft() != null){
                whereToPlace(actual.getChildLeft(), tableRow);
            } else {
                size++;
                actual.setChildLeft(new NodeAVL(tableRow));
                actual.getChildLeft().setParent(actual);
                historic.add(tableRow);
                reCalculHeigh(actual);
                calculBalance(actual, true);

            }

        }

    }


    /**
     * Mètode que recàlcula l'alçada del node. Comprova ambdues alçades (l'esquerra i la dreta)
     * i assignarà aquella que sigui major. Repetirà el procés fins arribar al node àrrel.
     * @param actual node del que se recalcularà l'alçada.
     */
    private void reCalculHeigh (NodeAVL actual) {
        int height;
        int right;
        int left;

        right = heightRight(actual);
        left = heightLeft(actual);

        height = (right >= left) ? right : left;
        actual.setHeight(height);

        if (actual.getParent() != null) {
            reCalculHeigh(actual.getParent());
        }
    }


    /**
     * Mètode que comprova que l'arbre etigui rebalancejat. Comprova si la diferència entre
     * la branca esquerra i la branca dreta sigui menor a 2. En cas que la diferència sigui major
     * cridararà a mètode encarregat de realitzar els moviments pertinets. Aquest comprovarà quin
     * moviment és necessari realitzar i el durà a terme. En cas contrari, repetirà el mateix procés
     * pel pare del node que s'està comprovant, fins arribar a l'àrrel.
     * @param actual
     * @param endarrere
     */
    private void calculBalance (NodeAVL actual, Boolean endarrere) {
        int left = heightLeft(actual);
        int right = heightRight(actual);
        int balance = left - right;

        if (Math.abs(balance) < 2) {
            if (actual.getParent() != null) {
                if (endarrere) {
                    calculBalance(actual.getParent(), true);
                }
            }

        } else {
            quinMoviment(actual, right, left);
        }

    }


    /**
     * Mètode que comprovarà quin moviment cal realitzar-se a partir dels valors de
     * right i left introduits. Aquests comprovaran quin és major i posteriorment
     * es repetirà el procés amb el fill esquerra o dret per poder diferenciar
     * un moviment LL d'un moviment LR per exemple.
     * @param actual  node que està desbalancejat i que cal arreglar.
     * @param right alçada dreta.
     * @param left alçada esquerra.
     */
    private void quinMoviment (NodeAVL actual, int right, int left) {
        if (right > left) {
            NodeAVL quinAMirar = actual.getChildRight();
            right = heightRight(quinAMirar);
            left = heightLeft(quinAMirar);

            if (right > left) {
                movimentRR(actual);
            } else {
                //System.out.println("Toca moviment RL de: " + actual.getRoot().toString());
                movimentRL(actual);
            }

        } else {
            NodeAVL quinAMirar = actual.getChildLeft();
            right = heightRight(quinAMirar);
            left = heightLeft(quinAMirar);

            if (right  > left) {
                //System.out.println("Toca moviment LR de: " + actual.getRoot().toString());
                movimentLR(actual);

            } else {
                movimentLL(actual);
            }


        }

    }


    /**
     * Mètode que retorna l'alçada del fill esquerra. Serà la seva més 1 ja que les alçades
     * comencen amb l'alçada 0.
     * @param actual node del que es vol saber l'alçada esquerra.
     * @return alçada del fill esquerre. 0 en cas de no tenir fill esquerre.
     */
    private int heightLeft (NodeAVL actual) {
        int i = 0;
        if (actual.getChildLeft() != null) {
           i = actual.getChildLeft().getHeight() + 1;
        }
        return i;
    }


    /**
     * Mètode que retorna l'alçada del fill dret. Serà la seva més 1 ja que les alçades
     * comencen amb l'alçada 0.
     * @param actual node del que es vol saber l'alçada dreta.
     * @return alçada del fill dret. 0 en cas de no tenir fill dret.
     */
    private int heightRight (NodeAVL actual) {
        int i = 0;
        if (actual.getChildRight() != null) {
            i = actual.getChildRight().getHeight() + 1;
        }
        return i;
    }


    /**
     * Mètode que realitzarà el moviement right-left. Aquest realitzarà una crida d'un moviment
     * left-left del fill dret del node desbalancejat. Posteriorment realitzarà la crida al moviment
     * right-right del node que s'havia desbalancejat inicialment.
     * @param actual node del que s'ha detectat un desbalanceig right-left.
     */
    private void movimentRL (NodeAVL actual) {
        movimentLL(actual.getChildRight());
        movimentRR(actual);
    }

    /**
     * Mètode que realitzarà el moviement left-right. Aquest realitzarà una crida d'un moviment
     * right-right del fill esquerre del node desbalancejat. Posteriorment realitzarà la crida al moviment
     * left-left del node que s'havia desbalancejat inicialment.
     * @param actual node del que s'ha detectat un desbalanceig left-right.
     */
    private void movimentLR (NodeAVL actual) {
        movimentRR(actual.getChildLeft());
        movimentLL(actual);
    }


    /**
     * Mètode que s'encarrega de realitzar un moviment right-right. Situara al node desbalancejat
     * com a fill esquerre del seu fill dret. En cas de ja tenir un fill esquerre aquest
     * caldrà recolocar-lo tant a ell com a la seva descendència com a fill del nou fill esquerre.
     * Caldrà recalcular alçades i si escau recalcular balancejos.
     * @param actual node del qual s'ha detectat un desbalanceig right-right.
     */
    private void movimentRR (NodeAVL actual) {
        //System.out.println("Toca moviment RR! De: " + actual.getRoot().toString());

        NodeAVL row = new NodeAVL(actual);
        NodeAVL pare = actual.getParent();

        actual = actual.getChildRight();
        if (pare == null) {
            actual.setParent(null);
        } else {

        }
        actual.setParent(pare);

        NodeAVL childLeft = null;

        if (actual.getChildLeft() != null) {
            childLeft = actual.getChildLeft();
            actual.setChildLeft(null);
        }

        actual.setChildLeft(row);
        actual.getChildLeft().setParent(actual);
        actual.getChildLeft().setChildRight(null);

        assignaEnRoot(actual);

        if (childLeft != null) {
            recolocate(actual.getChildLeft(), childLeft);
        }

       if (childLeft == null && actual.getChildRight() != null) {
           reCalculHeigh(actual.getChildRight());
       }
       reCalculHeigh(actual.getChildLeft());
   }


    /**
     * Mètode que s'encarrega de realitzar un moviment left-left. Situara al node desbalancejat
     * com a fill dret del seu fill esquerre. En cas de ja tenir un fill dret aquest
     * caldrà recolocar-lo tant a ell com a la seva descendència com a fill del nou fill dret.
     * Caldrà recalcular alçades i si escau recalcular balancejos.
     * @param actual node del qual s'ha detectat un desbalanceig left-left.
     */
    private void movimentLL (NodeAVL actual) {
        //System.out.println("Toca moviment LL! De: " + actual.getRoot().toString());

        NodeAVL row = new NodeAVL(actual);
        NodeAVL pare = actual.getParent();

        actual = actual.getChildLeft();

        if (pare == null) {
            actual.setParent(null);
        } else {

        }
        actual.setParent(pare);

        NodeAVL childRight = null;

        if (actual.getChildRight() != null) {
            childRight = actual.getChildRight();
            actual.setChildRight(null);
        }

        actual.setChildRight(row);
        actual.getChildRight().setParent(actual);
        actual.getChildRight().setChildLeft(null);

        assignaEnRoot(actual);

        if (childRight != null) {
            recolocate(actual.getChildRight(), childRight);
        }

        if (childRight == null && actual.getChildLeft()!= null) {
            reCalculHeigh(actual.getChildLeft());

        }
        reCalculHeigh(actual.getChildRight());
    }


    /**
     * Mètode que s'utilitza per dessignar parentesc, relaciona al pare amb el seu fill.
     * Comprovarà si cal relacionar-lo amb el fill esquerre, dret o amb l'àrrel de l'arbre.
     * @param actual node del qual es vol assignar el parentesc.
     */
    private void assignaEnRoot(NodeAVL actual) {
        if  (actual.getParent() == null) {
            root = actual;
        } else {
            if (actual.getParent().getRoot().compareTo(index, actual.getRoot()) < 1) {
                actual.getParent().setChildRight(actual);
            } else {
                actual.getParent().setChildLeft(actual);
            }
        }
    }


    /**
     * Algorisme per buscar segons una restricció realitza una búsqueda preOrdre
     * @param actual node actual que s'està buscant
     * @param restriction restricció segons la qual s'està buscant
     */
    private ArrayList<String> whatToShow (NodeAVL actual, TableRowRestriction restriction, ArrayList<String> what) {
        if(actual == null) {
            return what;
        }
        if (restriction.test(actual.getRoot())) {
            what.add(actual.toString());
            //System.out.println(actual.getRoot().toString());
        }

        whatToShow(actual.getChildLeft(), restriction, what);
        whatToShow(actual.getChildRight(), restriction, what);

        return what;

    }


    /**
     * Mètode que s'utilitzarà per emmagamatzemar el contingut d'un node que compleixi una certa restricció.
     * @param actual node que s'està comprovant.
     * @param restriction restricció que ha de complir.
     * @param what variable on s'anirà emmagamatzemant el contingut.
     * @return arrayList de hashMap on estarà el contingut d'una fila.
     */
    private ArrayList<HashMap> whatToContent (NodeAVL actual, TableRowRestriction restriction, ArrayList<HashMap> what) {
        if(actual == null) {
            return what;
        }
        if (restriction.test(actual.getRoot())) {
            what.add(actual.getRoot().getContent());

        }

        whatToContent(actual.getChildLeft(), restriction, what);
        whatToContent(actual.getChildRight(), restriction, what);

        return what;

    }


    /**
     * Mètode que s'utilitzarà per retornar el contingut de la intersecció d'una columna i d'una fila que
     * compleixi una certa restricció.
     * @param actual node que s'està comprovant si compleix la restricció.
     * @param restriction restricció que s'està comprovant.
     * @param column columna d'on s'ha d'extreure la informació.
     * @return string que equival a la intersecció entre amdbos, retorna null en cas de no trobar-ne cap que
     * ho compleixi.
     */
    private String whatToShowUnique (NodeAVL actual, TableRowRestriction restriction, String column) {
        String what;
        if(actual == null) {
            return null;
        }
        if (restriction.test(actual.getRoot())) {
            what = actual.getRoot().getContent().get(column).toString();
            return what;

        }

        what = whatToShowUnique(actual.getChildLeft(), restriction, column);
        if (what == null) {
            what = whatToShowUnique(actual.getChildRight(), restriction, column);
        }

        return what;
    }


    /**
     * Mètode que modifica el valor d'un node de l'arbre. Afegirà la modificació a l'arrayList de
     * modificacions. Primer caldrà fer una cerca dicotòmica per saber on es troba el node que cal
     * modificar. És tindrà en compte el valor de l'index atès que aquest camp no és pot modificar.
     * @param actual node actual del qual es comprovarà si és el node a modificar.
     * @param tableRow nou contingut de tableRow que se li ha d'assignar al node.
     * @param field columna a comparar.
     * @return cert en cas de no trobar cap problema, fals altrament.
     */
    private Boolean whatToModify (NodeAVL actual, TableRow tableRow, String field) {
        Boolean problems = false;
        if(actual == null){
            System.err.println("Element not found!");
            return false;
        }
        int where = actual.getRoot().compareTo(field, tableRow);

        while (where != 0) {
           if (where < 1) {
               if (actual.getChildRight() != null) {
                   actual = actual.getChildRight();
               } else {
                   problems = true;
               }
           } else {
               if (actual.getChildLeft() != null) {
                   actual = actual.getChildLeft();
               } else {
                   problems = true;
               }
           }
           where = actual.getRoot().compareTo(field, tableRow);
        }

        if (!problems) {
            showHistoric(index, 0);
            actual.setRoot(tableRow);
            historic.add(getUpdated(actual, tableRow));
            return true;
        } else {
            System.err.println("Element not found!");
            return false;
        }

    }


    /**
     * Mètode que realitzarà una cerca dicotòmica de l'element que s'ha d'esborrar. Realitzarà procés
     * d'eliminació (fer que ni pares ni fills tinguin rastre de l'element esborrat) i es recalcularan
     * les alçades dels nodes afectats. Posteiroment es comprovarà si ha patit algun desbalanceig i
     * en cas afirmatiu es procedirà amb el moviment pertinent.
     * @param actual node del qual es comprovarà si és el node que cal esborrar.
     * @param field columna mitjançant la qual es realitza la cerca.
     * @param value valor que identifica el que s'ha d'esborrar.
     * @param right cert si ve de fill dret, fals si prové de fill esquerre.
     */
    private void whatToDelete (NodeAVL actual, String field, Object value, Boolean right) {
        if(actual == null){
            return;
        }
        if (actual.equals(root) && root.getChildLeft() == null && root.getChildRight() == null) {
            root = null;
            return;
        }

        int where = actual.getRoot().compareTo(field, value);
        if (where < 1) {
            if (where == 0) {
                if (actual.getChildLeft() == null && actual.getChildRight() == null) {
                    if (actual.getParent() != null) {
                        if (right) {
                            actual.getParent().setChildRight(null);

                        } else {
                            actual.getParent().setChildLeft(null);

                        }
                        size--;
                        calculBalance(actual.getParent(), true);
                    }

                } else {
                    if (actual.getChildLeft() != null) {
                        actual.setRoot(buscaQuin(actual.getChildLeft(), false));
                        if (actual.getParent() != null) {
                            actualitzaCalculBalanceig(right, actual.getParent().getChildRight(), actual.getParent().getChildLeft());
                        } else {
                            if (actual.getChildLeft() != null) {
                                reCalculHeigh(actual.getChildLeft());
                                calculBalance(actual.getChildLeft(), true);
                            }
                            size--;
                        }

                    } else {
                        if (actual.getParent() != null) {
                            actual.getParent().setChildRight(actual.getChildRight());
                            actual.getChildRight().setParent(actual.getParent());
                            size--;
                            actualitzaCalculBalanceig(right, actual.getParent().getChildRight(), actual.getParent().getChildLeft());
                        } else {
                            root.setRoot(root.getChildRight().getRoot());
                            root.setChildRight(null);
                            size--;
                            calculBalance(root, true);
                        }


                    }
                }
            } else {
                whatToDelete(actual.getChildRight(), field, value, true);
            }
        } else {
            whatToDelete(actual.getChildLeft(), field, value, false);
        }



    }


    /**
     * Mètode actualitza el balanceig del fill pertinent.
     * @param right si es tracta del fill dret valdrà cert, fals altrament.
     * @param childRight fill dret.
     * @param childLeft fill esquerre.
     */
    private void actualitzaCalculBalanceig (Boolean right, NodeAVL childRight, NodeAVL childLeft) {
        if (right) {
            calculBalance(childRight, true);
        } else {
            calculBalance(childLeft, true);
        }
    }


    /**
     * Mètode que cerca el pare del fill esquerre més esquerre. És a dir, cerca sempre l'element de la branca
     * esquerra fins arribar al final.
     * @param childLeft fill esquerre.
     * @param right si prové de la dreta.
     * @return pare de l'últim fill esquerre més esquerre.
     */
    private TableRow buscaQuin(NodeAVL childLeft, Boolean right) {
        TableRow what;
        if (childLeft.getChildRight() != null) {
            what = buscaQuin(childLeft.getChildRight(), true);
        } else {
            if (right) {
                if (childLeft.getParent().getParent() != null) {
                    childLeft.getParent().setChildRight(null);
                } else {
                    childLeft.getParent().setChildRight(null);
                    if (root.getChildRight() != null) {
                        reCalculHeigh(root.getChildRight());
                        calculBalance(root.getChildRight(), true);
                    }
                }

            } else {
                if (childLeft.getParent().getParent() != null) {
                    childLeft.getParent().setChildLeft(childLeft.getChildLeft());
                } else {
                    childLeft.getParent().setChildLeft(null);
                    if (root.getChildLeft() != null) {
                        reCalculHeigh(root.getChildLeft());
                        calculBalance(root.getChildLeft(), true);
                    }
                }

            }
            what = childLeft.getRoot();
        }
        return what;
    }


    /**
     * Es traspassarà la informació d'un node i un tableRow. Sút
     * @param actual node on se li
     * @param tableRow tableRow d'on s'obtindrà la informació per combinarla amb la del node
     * @return tableRow amb informació
     */
    private TableRow getUpdated (NodeAVL actual, TableRow tableRow) {
        Set<String> keys = tableRow.getContent().keySet();
        TableRow updatedRow;
        updatedRow = actual.getRoot();
        for (String key : keys) {
            updatedRow.getContent().put(key, tableRow.getContent().get(key));
        }
        return updatedRow;
    }

    /**
     * @return nom que permet distingir l'arbre.
     */
    public static String getName() {
        return "AVL";
    }


    /**
     * Recoloca el node que ha quedat sense pare.
     * @param actual node actual que s'està verificant si compleix les condicions.
     * @param find node que cal recolocar.
     */
    private void recolocate (NodeAVL actual, NodeAVL find) {
        int where = actual.getRoot().compareTo(index, find.getRoot());

        if (where < 1) {
            if (where == 0) {
                reCalculHeigh(actual);

            } else {
                if (actual.getChildRight() != null) {
                    recolocate(actual.getChildRight(), find);
                } else {
                    actual.setChildRight(find);
                    actual.getChildRight().setParent(actual);
                    if (actual.getChildLeft() != null) {
                        actual.getChildLeft().getParent().setChildRight(find);
                    }
                    reCalculHeigh(actual);
                }

            }

        } else {
            if (actual.getChildLeft() != null) {
                recolocate(actual.getChildLeft(), find);
            } else {
                actual.setChildLeft(find);
                actual.getChildLeft().setParent(actual);
                if (actual.getChildRight() != null) {
                    actual.getChildRight().getParent().setChildLeft(find);
                }
                reCalculHeigh(actual);
            }

        }
    }

}
