package es.ceu.gisi.modcomp.gic_algorithms;

import es.ceu.gisi.modcomp.gic_algorithms.exceptions.CFGAlgorithmsException;
import es.ceu.gisi.modcomp.gic_algorithms.interfaces.*;
import java.util.*;

/**
 * Esta clase contiene la implementación de las interfaces que establecen los
 * métodos necesarios para el correcto funcionamiento del proyecto de
 * programación de la asignatura Modelos de Computación.
 *
 * @author Sergio Saugar García <sergio.saugargarcia@ceu.es>
 */
public class CFGAlgorithms implements CFGInterface, WFCFGInterface, CNFInterface, CYKInterface {

    private Set<Character> nonterminals = new TreeSet();
    private Set<Character> terminals = new TreeSet();
    private Map<Character, List<String>> productions = new TreeMap();
    private Map<String, List<Character>> inverse = new TreeMap();
    private Character startsymbol;

    /**
     * Método que añade los elementos no terminales de la gramática.
     *
     * @param nonterminal Por ejemplo, 'S'
     *
     * @throws CFGAlgorithmsException Si el elemento no es una letra mayúscula o
     * si ya está en el conjunto.
     */
    public void addNonTerminal(char nonterminal) throws CFGAlgorithmsException {
        if (nonterminals.contains(nonterminal)) { // este condicional, comprueba que la letra no esté repetida en la gramática, si esto sucede lanza una extepción.
            throw new CFGAlgorithmsException("Ya hay un no terminal igual.");
        } else if (Character.isAlphabetic(nonterminal) && Character.isUpperCase(nonterminal)) { // este condicional, comprueba que la letra es mayúscula y se añade a la gramática.
            nonterminals.add(nonterminal);
        } else { // este else, hace que se lance una extepción si la letra no es mayúscula.
            throw new CFGAlgorithmsException("La letra no es mayúscula.");
        }

    }

    /**
     * Método que elimina el símbolo no terminal indicado de la gramática.
     * También debe eliminar todas las producciones asociadas a él y las
     * producciones en las que aparece.
     *
     * @param nonterminal Elemento no terminal a eliminar.
     *
     * @throws CFGAlgorithmsException Si el elemento no pertenece a la gramática
     */
    public void removeNonTerminal(char nonterminal) throws CFGAlgorithmsException {
        for (Character nt : productions.keySet()) {
            List<String> auxiliar = new ArrayList();
            for (String produccion : productions.get(nt)) {
                if (produccion.contains(String.valueOf(nonterminal))) {
                    auxiliar.add(produccion);
                }
            }

            for (String p : auxiliar) {
                removeProduction(nt, p);
            }

        }
        if (productions.containsKey(nonterminal)) {
            productions.remove(nonterminal);
        }
        if (nonterminals.contains(nonterminal)) {
            nonterminals.remove(nonterminal);
        } else {
            throw new CFGAlgorithmsException("El elemento no pertenece a la gramática.");
        }
    }

    /**
     * Método que devuelve un conjunto con todos los símbolos no terminales de
     * la gramática.
     *
     * @return Un conjunto con los no terminales definidos.
     */
    public Set<Character> getNonTerminals() {
        return nonterminals; // devuelve un noterminal.
    }

    /**
     * Método que añade los elementos terminales de la gramática.
     *
     * @param terminal Por ejemplo, 'a'
     *
     * @throws CFGAlgorithmsException Si el elemento no es una letra minúscula o
     * si ya está en el conjunto.
     */
    public void addTerminal(char terminal) throws CFGAlgorithmsException {
        if (terminals.contains(terminal)) { // este condicional, comprueba que la letra no esté repetida en la gramática, si esto sucede lanza una extepción.
            throw new CFGAlgorithmsException("Ya hay un terminal igual.");
        } else if (Character.isAlphabetic(terminal) && Character.isLowerCase(terminal)) { // este condicional, comprueba que la letra sea minúscula y se añade a la gramática.
            terminals.add(terminal);
        } else { // este else, hace que se lance una extepción si la letra no es minúscula.
            throw new CFGAlgorithmsException("La letra no es minúscula.");
        }

    }

    /**
     * Método que elimina el símbolo terminal indicado de la gramática. También
     * debe eliminar todas las producciones en las que aparece.
     *
     * @param terminal Elemento terminal a eliminar.
     *
     * @throws CFGAlgorithmsException Si el elemento no pertenece a la gramática
     */
    public void removeTerminal(char terminal) throws CFGAlgorithmsException {
        for (Character nt : productions.keySet()) { // bucle que recorre los no terminales del mapa producciones.
            List<String> auxiliar = new ArrayList(); // lista auxiliar.
            for (String produccion : productions.get(nt)) { // bucle que recorre las producciones del mapa.
                if (produccion.contains(String.valueOf(terminal))) { // condicional que comprueba si la producción contiene al terminal y añade la producción a la lista auxiliar.
                    auxiliar.add(produccion);
                }
            }

            for (String p : auxiliar) { // bucle que muestra el contenido la lista auxiliar y elimina la producción.
                removeProduction(nt, p);
            }

        }
        if (terminals.contains(terminal)) { // el condicional comprueba si el terminal está en el conjunto y lo elimina de él.
            terminals.remove(terminal);
        } else { // este else, hace que se lance una extepción si no está contenido en los terminales.
            throw new CFGAlgorithmsException("El elemento no pertenece a la gramática.");
        }

    }

    /**
     * Método que devuelve un conjunto con todos los símbolos terminales de la
     * gramática.
     *
     * @return Un conjunto con los terminales definidos.
     */
    public Set<Character> getTerminals() {
        return terminals; // devuelve conjunto de terminales.
    }

    /**
     * Método que indica, de los elementos no terminales, cuál es el axioma de
     * la gramática.
     *
     * @param nonterminal Por ejemplo, 'S'
     *
     * @throws CFGAlgorithmsException Si el elemento insertado no forma parte
     * del conjunto de elementos no terminales.
     */
    public void setStartSymbol(char nonterminal) throws CFGAlgorithmsException {
        if (this.nonterminals.contains(nonterminal)) { // este condicional, comprueba que el no terminal está comprendido en el conjunto.
            this.startsymbol = nonterminal; // fijo que este no terminal es el axioma.
        } else { // este else hace que se lance una extepción si no forma parte del conjunto de los no terminales.
            throw new CFGAlgorithmsException("El elemento insertado no forma parate de los elementos no terminales.");
        }

    }

    /**
     * Método que devuelve el axioma de la gramática.
     *
     * @return El axioma de la gramática
     *
     * @throws CFGAlgorithmsException Si el axioma todavía no ha sido
     * establecido.
     */
    public Character getStartSymbol() throws CFGAlgorithmsException {
        if (startsymbol != null) { // este condicional, comprueba si está vacío.
            return startsymbol;
        } else { // este else, en el caso de que el axioma no esté establecido lanza una extepción.
            throw new CFGAlgorithmsException("El axioma no a sido establecido.");
        }
    }

    /**
     * Método utilizado para construir la gramática. Admite producciones de tipo
     * 2. También permite añadir producciones a lambda (lambda se representa con
     * el caracter 'l' -- ele minúscula). Se permite añadirla en cualquier no
     * terminal.
     *
     * @param nonterminal A
     * @param production Conjunto de elementos terminales y no terminales.
     *
     * @throws CFGAlgorithmsException Si está compuesta por elementos
     * (terminales o no terminales) no definidos previamente.
     */
    public void addProduction(char nonterminal, String production) throws CFGAlgorithmsException {

        if (!nonterminals.contains(nonterminal)) { // este condicional comprueba que si no esta contenido el no terminal en el conjunto lanza extepción.
            throw new CFGAlgorithmsException("Estás utilizando elementos terminales o no terminales no definidos en el conjunto.");
        }
        for (int i = 0; i < production.length(); i++) { // bucle que recorre letra por letra la producción introducida.
            char letter = production.charAt(i);
            if (!(letter == 'l' || nonterminals.contains(letter) || terminals.contains(letter))) { // este condicional comprueba que este en terminales, no terminales o tenga l y sino se cumple lanzo extepción.
                throw new CFGAlgorithmsException("Estás utilizando elementos terminales o no terminales no definidos en el conjunto.");
            }

        }
        productions.putIfAbsent(nonterminal, new ArrayList<>()); // el putIfAbsent comprueba que no esté repetido y lo añade.
        if (productions.get(nonterminal).contains(production)) {
            throw new CFGAlgorithmsException();
        }
        productions.get(nonterminal).add(production);
        inverse.putIfAbsent(production, new ArrayList<>());
        inverse.get(production).add(nonterminal);
    }

    /**
     * Elimina la producción indicada del elemento no terminal especificado.
     *
     * @param nonterminal Elemento no terminal al que pertenece la producción
     * @param production Producción a eliminar
     *
     * @return True si la producción ha sido correctamente eliminada
     *
     * @throws CFGAlgorithmsException Si la producción no pertenecía a ese
     * elemento no terminal.
     */
    public boolean removeProduction(char nonterminal, String production) throws CFGAlgorithmsException {
        for (int i = 0; i < productions.get(nonterminal).size(); i++) { //bucle que recorre la lista de producciones del no terminal introducido
            if (productions.get(nonterminal).get(i).equals(production)) { //consigue la producción de la posicion por la que va en el bucle que recorre la lista de producciones y comprueba si la producción por la que va en el bucle es igual a la producción que buscamos
                productions.get(nonterminal).remove(production); //elimina la produccion del no terminal introducido
                //inverse.get(production).remove(nonterminal);
                return true; //devuelve true
            }
        }
        throw new CFGAlgorithmsException("Error: La producción no pertenecía a ese elemento no terminal."); //excepción que se ejecuta cuando el bucle recorrido no llega a devolver un true, eso será porque se habrá comprobado que no hay ninguna producción en la lista de producciones igual a la produccion introducida

    }

    /**
     * Devuelve una lista de String que representan todas las producciones que
     * han sido agregadas a un elemento no terminal.
     *
     * @param nonterminal Elemento no terminal del que se buscan las
     * producciones
     *
     * @return Devuelve una lista de String donde cada String es la parte
     * derecha de cada producción
     */
    public List<String> getProductions(char nonterminal) {
        return productions.get(nonterminal); // devuelve la lista de producciones de ese no terminal.

    }

    /**
     * Devuelve un String que representa todas las producciones que han sido
     * agregadas a un elemento no terminal.
     *
     * @param nonterminal
     *
     * @return Devuelve un String donde se indica, el elemento no terminal, el
     * símbolo de producción "::=" y las producciones agregadas separadas, única
     * y exclusivamente por una barra '|' (no incluya ningún espacio). Por
     * ejemplo, si se piden las producciones del elemento 'S', el String de
     * salida podría ser: "S::=aBb|bC|dC". Las producciones DEBEN IR ORDENADAS
     * POR ORDEN ALFABÉTICO.
     */
    public String getProductionsToString(char nonterminal) {
        String symbol = "::=";
        List<String> lista = productions.get(nonterminal); // señalo la lista del map productions.
        if (lista != null) { // este condicional devuelve " ", si es null.
            Collections.sort(lista); // ordeno lista.
            for (int i = 0; i < lista.size(); i++) { // el bucle, da cada elemento de la lista y los separa con una barra.
                symbol = symbol + lista.get(i);
                if (i < lista.size() - 1) {
                    symbol = symbol + "|";
                }
            }
        } else {
            return "";
        }
        return nonterminal + symbol;

    }

    /**
     * Devuelve un String con la gramática completa. Todos los elementos no
     * terminales deberán aparecer por orden alfabético (A,B,C...).
     *
     * @return Devuelve el agregado de hacer getProductionsToString sobre todos
     * los elementos no terminales ORDENADOS POR ORDEN ALFABÉTICO.
     */
    public String getGrammar() {
        String grammar = ""; // creo variable vacía.
        grammar = getTerminals().toString() + ","; // guardo los terminales en la variable.
        grammar += getNonTerminals().toString() + ","; // guardo los no terminales en la variable.
        for (Character noterminal : nonterminals) { // bucle para que dé los noterminales del conjunto.
            grammar += getProductionsToString(noterminal); // guardo las producciones en la variable.
        }
        if (startsymbol != null) { // condicional que comprueba que el axioma no sea nulo.
            grammar += "," + startsymbol; // guardo el axioma en la variable.
        }
        return grammar;
    }

    /**
     * Elimina todos los elementos que se han introducido hasta el momento en la
     * gramática (elementos terminales, no terminales, axioma y producciones),
     * dejando el algoritmo listo para volver a insertar una gramática nueva.
     */
    public void deleteGrammar() {
        nonterminals.clear(); // borra todo el contenido de noterminales.
        terminals.clear(); // borra todo el contenido de terminales.
        productions.clear(); // borra todo el contenido de producciones.
        this.startsymbol = null; // borra el valor del axioma y le da null.

    }

    /**
     * Método que comprueba si la gramática dada de alta es una gramática
     * independiente del contexto.
     *
     * @return true Si la gramática es una gramática independiente del contexto.
     */
    public boolean isCFG() {
        return true;
    }

    /**
     * Método que comprueba si la gramática almacenada tiene reglas innecesarias
     * (A::=A).
     *
     * @return True si contiene ese tipo de reglas
     */
    public boolean hasUselessProductions() {
        for (Character nonterminal : productions.keySet()) { // este bucle me devuelve las claves del mapa productions y las guarda en nonterminal.
            for (String production : productions.get(nonterminal)) { // este bucle me devuelve los valores de productions y las guarda en production.
                if (production.equals(nonterminal.toString())) {  // condicional para que si se cumple que production es igual a nonterminal guarda en answer true y rompe el primer bucle.
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Método que elimina las reglas innecesarias de la gramática almacenada.
     *
     * @return Devuelve una lista de producciones (un String de la forma "A::=A"
     * por cada producción), con todas las reglas innecesarias eliminadas.
     */
    public List<String> removeUselessProductions() {
        List<String> productionstoremove = new ArrayList<>(); // lista de eliminados.
        List<String> formattedlist = new ArrayList<>(); // lista a devolver.
        for (Character nonterminal : productions.keySet()) { // bucle que me da los noterminales de productions.
            for (String production : productions.get(nonterminal)) { // bucle que me da las producciones de productions.
                if (production.equals(nonterminal.toString())) {
                    productionstoremove.add(production); // añado producció a eliminar.
                    formattedlist.add(nonterminal + "::=" + production); // añado el terminal y no terminal.
                }
            }
            productions.get(nonterminal).removeAll(productionstoremove); // elimino de la lista del mapa los que coincidan de la lista productionstoremove.
        }

        return formattedlist; // devolver formattedlist.
    }

    /**
     * Método que elimina los símbolos inútiles de la gramática almacenada.
     *
     * @return Devuelve una lista con todos los símbolos no terminales y
     * terminales eliminados.
     */
    public List<Character> removeUselessSymbols() {
        // Algoritmo 1
        Set<Character> viejo = new HashSet<>();
        Set<Character> nuevo = new HashSet<>();
        for (Character nonterminal : productions.keySet()) {
            for (String production : productions.get(nonterminal)) {
                if (production.length() == 1 && terminals.contains(production.charAt(0))) {
                    nuevo.add(nonterminal);
                    break;
                }
            }
        }
        while (!viejo.equals(nuevo)) {
            viejo.clear();
            viejo.addAll(nuevo);
            for (Character nt : viejo) {
                for (Character nonterminal : productions.keySet()) {
                    for (String production : productions.get(nonterminal)) {
                        if (production.length() == 1 && viejo.contains(production.charAt(0))) {
                            nuevo.add(nonterminal);
                            break;
                        }
                    }
                }
            }
        }
        if (!nuevo.containsAll(nonterminals)) {
            List<Character> nonterminalstodeleted = new ArrayList();
            for (char nonterminal : nonterminals) {
                if (!nuevo.contains(nonterminal)) {
                    nonterminalstodeleted.add(nonterminal);
                }
            }
            nonterminals.clear();
            nonterminals.addAll(nuevo);
            for (char nonterminal : nonterminalstodeleted) {
                productions.remove(nonterminal);

            }
        }
        for (Character nonterminal : productions.keySet()) {
            List<String> productionsAux = new ArrayList();
            for (String production : productions.get(nonterminal)) {
                if (production.length() == 1 && (terminals.contains(production.charAt(0)) || nonterminals.contains(production.charAt(0)))) {
                    productionsAux.add(production);
                }
            }
            productions.replace(nonterminal, productionsAux);
        }
        // Algoritmo 2
        Set<Character> viejont = new HashSet<>();
        Set<Character> nuevont = new HashSet<>();
        nuevont.add(startsymbol);
        Set<Character> viejot = new HashSet<>();
        Set<Character> nuevot = new HashSet<>();

        while (!viejont.equals(nuevont) || !nuevot.equals(viejot)) {// el error creo q está aqui preguntar.
            viejont.clear();
            viejot.clear();
            viejont.addAll(nuevont);
            viejot.addAll(nuevot);
            for (char nonterminal : viejont) {
                for (String production : productions.get(nonterminal)) {
                    if (production.length() == 1 && nonterminals.contains(production.charAt(0))) {// revisar
                        nuevont.add(production.charAt(0));
                    }
                    if (production.length() == 1 && terminals.contains(production.charAt(0))) {
                        nuevot.add(production.charAt(0));
                    }
                }
            }
        }
        if (!nuevont.containsAll(nonterminals)) {
            List<Character> nonterminalstodeleted = new ArrayList();
            for (char nonterminal : nonterminals) {
                if (!nuevont.contains(nonterminal)) {
                    nonterminalstodeleted.add(nonterminal);
                }
            }
            nonterminals.clear();
            nonterminals.addAll(nuevont);
            for (char nonterminal : nonterminalstodeleted) {
                productions.remove(nonterminal);

            }
        }
        if (!nuevot.containsAll(terminals)) {
            terminals.clear();
            terminals.addAll(nuevot);
        }
        for (Character nonterminal : productions.keySet()) {
            List<String> productionsAux = new ArrayList();
            for (String production : productions.get(nonterminal)) {
                if (production.length() == 1 && (terminals.contains(production.charAt(0)) || nonterminals.contains(production.charAt(0)))) {
                    productionsAux.add(production);
                }
            }
            productions.replace(nonterminal, productionsAux);
        }

        List<Character> result = new ArrayList();
        result.addAll(nuevont);
        return result;

    }

    /**
     * Método que comprueba si la gramática almacenada tiene reglas no
     * generativas (reglas lambda). Excepto S::=l si sólo es para reconocer la
     * palabra vacía.
     *
     * @return True si contiene ese tipo de reglas
     */
    public boolean hasLambdaProductions() {
        return !((inverse.get("l") == null) || (inverse.get("l").isEmpty()));
    }

    /**
     * Método que elimina todas las reglas no generativas de la gramática
     * almacenada. La única regla que puede quedar es S::=l y debe haber sido
     * sustituida (y, por lo tanto, devuelta en la lista de producciones
     * "eliminadas").
     *
     * @return Devuelve una lista de no terminales que tenían reglas no
     * generativas y han sido tratadas.
     */
    public List<Character> removeLambdaProductions() {
        Set<Character> anulable = new HashSet<>();
        List<Character> resultado = new ArrayList<>();

        // Paso 1: Identificar los no terminales que se puedan transformar en lambda
        boolean cambio = false;
        do {
            for (Character nonterminal : productions.keySet()) {
                for (String produccion : productions.get(nonterminal)) {
                    if (produccion.equals("l")) {
                        if (anulable.add(nonterminal)) {
                            cambio = true;
                        }
                    }
                }
            }
        } while (cambio = false);

        // Paso 2: Generar nuevas producciones excluyendo los no terminales que se transforman en lambda
        Map<Character, List<String>> nuevasProducciones = new HashMap<>();
        for (Character nonterminal : productions.keySet()) {
            List<String> produccionAuxiliar = new ArrayList<>();
            for (String produccion : productions.get(nonterminal)) {
                if (!produccion.equals("l")) {
                    produccionAuxiliar.add(produccion);
                    for (int i = 0; i < produccion.length(); i++) {
                        if (anulable.contains(produccion.charAt(i))) {
                            String nuevaProduccion = produccion.substring(0, i) + produccion.substring(i + 1);
                            if (!nuevaProduccion.isEmpty() && !produccionAuxiliar.contains(nuevaProduccion)) {
                                produccionAuxiliar.add(nuevaProduccion);
                            }
                        }
                    }
                }
            }
            nuevasProducciones.put(nonterminal, produccionAuxiliar);
        }

        // Paso 3: Añadir la transformación de lambda al axioma en el caso de que sea necesario
        if (anulable.contains(startsymbol)) {
            List<String> produccionAxioma = nuevasProducciones.getOrDefault(startsymbol, new ArrayList<>());
            produccionAxioma.add("l");
            nuevasProducciones.put(startsymbol, produccionAxioma);
        }

        // Actualizar las producciones
        productions = nuevasProducciones;

        // Devolver los no terminales que tenían producciones lambda
        resultado.addAll(anulable);
        return resultado;
    }

    /**
     * Método que comprueba si la gramática almacenada tiene reglas unitarias
     * (A::=B).
     *
     * @return True si contiene ese tipo de reglas
     */
    public boolean hasUnitProductions() {
        for (char nonterminal : productions.keySet()) {
            for (String production : productions.get(nonterminal)) {
                // if de lenth 1
                for (char letter : production.toCharArray()) {
                    if (letter == nonterminal) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Método que elimina las reglas unitarias de la gramática almacenada.
     *
     * @return Devuelve una lista de producciones (un String de la forma "A::=B"
     * por cada producción), con todas las reglas unitarias eliminadas.
     */
    public List<String> removeUnitProductions() {
        //List<String> unitproductionstoremove = new ArrayList<>();
        //for (Character nonterminal : productions.keySet()) {
        //    for (String production : productions.get(nonterminal)) {
        //        for (char letter : production.toCharArray()) {
        //            if (production.length() == 1 && letter == nonterminal) {
        //                unitproductionstoremove.add(production);
        //                //removeProduction(nonterminal, production);
        //            }
        //
        //        }
        //        inverse.get(production).remove(nonterminal);
        //    }
        //    productions.get(nonterminal).removeAll(unitproductionstoremove);
        //A::=B B::=C
        //A -> B -> C (cola)
        //unitproductionstoremove.clear();
        // }
        //return unitproductionstoremove;
        return null;

    }

    /**
     * Método que transforma la gramática almacenada en una gramática bien
     * formada: - 1. Elimina las reglas innecesarias. - 2. Elimina las reglas no
     * generativas. - 3. Elimina las reglas unitarias. - 4. Elimina los símbolo
     * inútiles.
     */
    public void transformToWellFormedGrammar() {
        removeUselessProductions();// innecesarias
        removeLambdaProductions();// no generativa
        removeUnitProductions();// unitarias
        removeUselessSymbols();// inutiles

    }

    /**
     * Método que chequea que las producciones estén en Forma Normal de Chomsky.
     *
     * @param nonterminal A
     * @param production A::=BC o A::=a (siendo B, C no terminales definidos
     * previamente y a terminal definido previamente). Se acepta S::=l si S es
     * no terminal y axioma.
     *
     * @throws CFGAlgorithmsException Si no se ajusta a Forma Normal de Chomsky
     * o si está compuesta por elementos (terminales o no terminales) no
     * definidos previamente.
     */
    public void checkCNFProduction(char nonterminal, String production) throws CFGAlgorithmsException {
        if (production.equals("l")) { // condicional que comprueba el caso de producción igual a l.
            if (nonterminal != startsymbol) { // si el no terminal no es el axioma lanzo extepción.
                throw new CFGAlgorithmsException("La producción l solo está permitida para el axioma.");
            }
        } else if (production.length() == 1) { // condicional que comprueba que la producción tiene longitud 1.
            char symbol = production.charAt(0);
            if (!Character.isLowerCase(symbol)) { // si no es minúscula la letra en la posición 0 lanzo extepción.
                throw new CFGAlgorithmsException("Una producción de longitud 1 debe ser un terminal.");
            }
        } else if (production.length() == 2) { // condicional que comprueba que la producción tiene longitud 2.
            char symbol1 = production.charAt(0);
            char symbol2 = production.charAt(1);
            if (!Character.isUpperCase(symbol1) || !Character.isUpperCase(symbol2)) { // si no es mayúcula la letra en la posición 0 o posición 1 lanzo extepción.
                throw new CFGAlgorithmsException("Una producción de longitud 2 debe consistir en dos no terminales.");
            }
        } else { // este else, lanza una extepción si no se cumple ninguna de las condiciones anteriores.
            throw new CFGAlgorithmsException("La producción no se ajusta a la Forma Normal de Chomsky.");
        }
    }

    /**
     * Método que comprueba si la gramática dada de alta se encuentra en Forma
     * Normal de Chomsky. Es una precondición para la aplicación del algoritmo
     * CYK.
     *
     * @return true Si la gramática está en Forma Normal de Chomsky
     */
    public boolean isCNF() {
        for (Character nonterminal : productions.keySet()) {
            for (String production : productions.get(nonterminal)) {
                if (production.equals("l")) { // condicional que comprueba el caso de producción igual a l.
                    if (nonterminal != startsymbol) { // si el no terminal no es el axioma devuelvo false.
                        return false;
                    }
                } else if (production.length() == 1) { // condicional que comprueba que la producción tiene longitud 1.
                    char symbol = production.charAt(0);
                    if (!Character.isLowerCase(symbol)) { // si no es minúscula la letra en la posición 0 devuelvo false.
                        return false;
                    }
                } else if (production.length() == 2) { // condicional que comprueba que la producción tiene longitud 2.
                    char symbol1 = production.charAt(0);
                    char symbol2 = production.charAt(1);
                    if (!Character.isUpperCase(symbol1) || !Character.isUpperCase(symbol2)) { // si no es mayúcula la letra en la posición 0 o posición 1 devuelvo false.
                        return false;
                    }
                } else { // este else, devuelve false si no se cumple ninguna de las condiciones anteriores.
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Método que transforma la gramática almacenada en su Forma Normal de
     * Chomsky equivalente.
     *
     * @throws CFGAlgorithmsException Si la gramática de la que partimos no es
     * una gramática bien formada.
     */
    public void transformIntoCNF() throws CFGAlgorithmsException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    /**
     * Método que indica si una palabra pertenece al lenguaje generado por la
     * gramática que se ha introducido. Se utilizará el algoritmo CYK para
     * decidir si la palabra pertenece al lenguaje.
     *
     * La gramática deberá estar en FNC.
     *
     * @param word La palabra a verificar, tiene que estar formada sólo por
     * elementos no terminales.
     *
     * @return TRUE si la palabra pertenece, FALSE en caso contrario
     *
     * @throws CFGAlgorithmsException Si la palabra proporcionada no está
     * formada sólo por terminales, si está formada por terminales que no
     * pertenecen al conjunto de terminales definido para la gramática
     * introducida, si la gramática es vacía o si el autómata carece de axioma.
     */
    public boolean isDerivedUsignCYK(String word) throws CFGAlgorithmsException {
        if (!isCNF()) {
            throw new CFGAlgorithmsException("La gramática no está en Forma Normal de Chomsky.");
        }

        if (word.isEmpty()) {
            return false;
        }

        for (int i = 0; i < word.length(); i++) {
            char letter = word.charAt(i);
            if (!terminals.contains(letter)) {
                throw new CFGAlgorithmsException("La palabra contiene caracteres que no están en el conjunto de terminales.");
            }
        }

        if (productions.isEmpty() || startsymbol == null) {
            throw new CFGAlgorithmsException("La gramática está vacía o no tiene axioma.");
        }

        int n = word.length();
        int r = nonterminals.size();

        Set<Character>[][] table = new HashSet[n][n];
        for (int i = 0; i < n; i++) {
            table[i][0] = new HashSet<>();
            char terminal = word.charAt(i);
            for (Map.Entry<Character, List<String>> entry : productions.entrySet()) {
                char nonterminal = entry.getKey();
                for (String production : entry.getValue()) {
                    if (production.length() == 1 && production.charAt(0) == terminal) {
                        table[i][0].add(nonterminal);
                    }
                }
            }
        }

        for (int l = 2; l <= n; l++) {
            for (int s = 0; s <= n - l; s++) {
                table[s][l - 1] = new HashSet<>();
                for (int p = 1; p < l; p++) {
                    Set<Character> bSet = table[s][p - 1];
                    Set<Character> cSet = table[s + p][l - p - 1];
                    for (char b : bSet) {
                        for (char c : cSet) {
                            for (Map.Entry<Character, List<String>> entry : productions.entrySet()) {
                                char a = entry.getKey();
                                for (String production : entry.getValue()) {
                                    if (production.length() == 2 && production.charAt(0) == b && production.charAt(1) == c) {
                                        table[s][l - 1].add(a);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return table[0][n - 1].contains(startsymbol);
    }

    /**
     * Método que, para una palabra, devuelve un String que contiene todas las
     * celdas calculadas por el algoritmo CYK (la visualización debe ser similar
     * al ejemplo proporcionado en el PDF que contiene el paso a paso del
     * algoritmo).
     *
     * @param word La palabra a verificar, tiene que estar formada sólo por
     * elementos no terminales.
     *
     * @return Un String donde se vea la tabla calculada de manera completa,
     * todas las celdas que ha calculado el algoritmo.
     *
     * @throws CFGAlgorithmsException Si la palabra proporcionada no está
     * formada sólo por terminales, si está formada por terminales que no
     * pertenecen al conjunto de terminales definido para la gramática
     * introducida, si la gramática es vacía o si carece de axioma.
     */
    public String algorithmCYKStateToString(String word) throws CFGAlgorithmsException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
