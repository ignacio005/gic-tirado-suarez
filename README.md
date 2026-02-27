# Procesador de Gramáticas y Algoritmo CYK

## Descripción

Proyecto académico que implementa una **Gramática Libre de Contexto (CFG)** junto con transformaciones formales y el **algoritmo CYK** para el reconocimiento de cadenas.

El sistema permite definir una gramática, aplicar transformaciones estructurales y comprobar si una cadena pertenece al lenguaje generado.

---

## Conceptos implementados

- Gramáticas Libres de Contexto (CFG)
- Eliminación de λ-producciones
- Eliminación de producciones unitarias
- Eliminación de símbolos inútiles
- Conversión a **Forma Normal de Chomsky (CNF)**
- Implementación del algoritmo **CYK** (programación dinámica, complejidad O(n³))

---

## Estructuras de datos utilizadas

- `Map<Character, List<String>>` para las producciones
- `Set<Character>` para terminales y no terminales
- `List<String>` para gestión de reglas
- Arrays auxiliares para la tabla CYK

---

## Algoritmo CYK

Se implementa el algoritmo Cocke–Younger–Kasami (CYK) para comprobar si una cadena pertenece a una gramática en CNF.

El procedimiento:

1. Construye una tabla triangular de tamaño n × n
2. Inicializa los casos base con producciones terminales
3. Aplica programación dinámica para combinar subcadenas
4. Verifica si el axioma aparece en la celda final

Complejidad temporal: **O(n³)**

---

## Documentación

El repositorio incluye una memoria técnica detallada donde se explican las decisiones de diseño, estructuras utilizadas y justificación de los algoritmos implementados.

---

## Autores

Ignacio Tirado Meza  
Davo Fabio Suárez Álvarez  

Proyecto desarrollado en equipo (2 personas).
