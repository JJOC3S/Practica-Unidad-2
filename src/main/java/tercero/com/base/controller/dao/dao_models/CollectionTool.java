package tercero.com.base.controller.dao.dao_models;

import tercero.com.base.controller.DataStruc.List.LinkedList;
import tercero.com.base.controller.services.CancionService;
import tercero.com.base.models.Cancion;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
// Esta clase contiene metodos estaticos para ordenar, filtrar y buscar en colecciones de objetos o Map.
public class CollectionTool<E> {
    //constructor privado para evitar instanciacion
    private CollectionTool() {
    }
    // Ordena una lista de objetos o Map por un criterio y tipo de ordenamiento (1 para ascendente, -1 para descendente)
    public static <E> LinkedList<E> shellSort(LinkedList<E> lista, String criterio, Integer tipo) {
        if (lista == null || lista.isEmpty()) {
            return new LinkedList<>();
        }

        E[] array = lista.toArray();
        int n = array.length;

        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                E temp = array[i];
                int j;
                for (j = i; j >= gap && compare(array[j - gap], temp, criterio, tipo) > 0; j -= gap) {
                    array[j] = array[j - gap];
                }
                array[j] = temp;
            }
        }

        LinkedList<E> ordenada = new LinkedList<>();
        for (E e : array) {
            ordenada.add(e);
        }
        return ordenada;
    }
    // Implementa el algoritmo QuickSort para ordenar una LinkedList de objetos
    public static <E> LinkedList<E> quickSort(LinkedList<E> lista, String criterio, Integer tipo) {
        if (lista == null || lista.isEmpty()) {
            return new LinkedList<>();
        }

        E[] array = lista.toArray();
        quickSortArray(array, 0, array.length - 1, criterio, tipo);

        LinkedList<E> ordenada = new LinkedList<>();
        for (E e : array) {
            ordenada.add(e);
        }
        return ordenada;
    }
    // Implementa el algoritmo QuickSort para ordenar un array de objetos
    private static <E> void quickSortArray(E[] array, int low, int high, String criterio, Integer tipo) {
        if (low < high) {
            int pi = partition(array, low, high, criterio, tipo);
            quickSortArray(array, low, pi - 1, criterio, tipo);
            quickSortArray(array, pi + 1, high, criterio, tipo);
        }
    }

// Particiona el array para el algoritmo QuickSort
    private static <E> int partition(E[] array, int low, int high, String criterio, Integer tipo) {
        E pivot = array[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (compare(array[j], pivot, criterio, tipo) < 0) {
                i++;
                swap(array, i, j);
            }
        }

        swap(array, i + 1, high);
        return i + 1;
    }
    //El intercambio de dos elementos en un array esta en todos lados, asi que lo pongo como metodo estatico
    private static <E> void swap(E[] array, int i, int j) {
        E temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    // Compara dos elementos basados en un criterio y tipo de ordenamiento
    public static <E> int compare(E element1, E element2, String criterio, Integer tipo) {
        Object value1 = getAtributeValues(element1, criterio);
        Object value2 = getAtributeValues(element2, criterio);

        int result = compareValues(value1, value2);
        // Si el tipo es 1, se ordena de menor a mayor, si es -1, de mayor a menor
        return tipo == 1 ? result : -result;
    }
    //busqyeda binaria comun criterio de busqueda,
    public static <E> Integer binarySearch(LinkedList<E> lista, String criterio, Object valor) {
        if (lista == null || lista.isEmpty()) {
            return -1;
        }
        if (valor == null) {
            return -1;
        }

        E[] array = lista.toArray();
        int left = 0;
        int right = array.length - 1;

        System.out.println("=== INICIO BÚSQUEDA BINARIA ===");
        System.out.println("Buscando valor: " + valor);
        System.out.println("Array length: " + array.length);

        // Primero vamos a verificar manualmente dónde está tu elemento
        for (int i = 0; i < array.length; i++) {
            Object elementValue = getAtributeValues(array[i], criterio);
            if (compareValues(elementValue, valor) == 0) {
                System.out.println("VERIFICACIÓN MANUAL: Elemento encontrado en índice " + i + " con valor: " + elementValue);
            }
        }

        while (left <= right) {
            int mid = left + (right - left) / 2;
            Object midValue = getAtributeValues(array[mid], criterio);

            System.out.println("Left: " + left + ", Right: " + right + ", Mid: " + mid);
            System.out.println("Valor en mid[" + mid + "]: " + midValue);

            if (midValue == null) {
                System.out.println("Valor nulo en mid, moviendo right");
                right = mid - 1;
                continue;
            }

            int comparison = compareValues(midValue, valor);
            System.out.println("Comparación: " + comparison + " (midValue vs valor)");

            if (comparison == 0) {
                System.out.println("¡ENCONTRADO! Retornando índice: " + mid);
                return mid;
            } else if (comparison < 0) {
                System.out.println("midValue < valor, buscando a la derecha");
                left = mid + 1;
            } else {
                System.out.println("midValue > valor, buscando a la izquierda");
                right = mid - 1;
            }
        }

        System.out.println("NO ENCONTRADO");
        return -1;
    }
    //A partir de una lista, se ordena por criterio de busqueda, y se agregan los elementos aprrtir de del primero que cumple el criterio
    public static <E> LinkedList<E> binarySearchRange(LinkedList<E> lista, String criterio, Object valor) {
        E[] array = CollectionTool.quickSort(lista, criterio, 1).toArray();
        int start = obtenerPosicionDeInicio(array, criterio, valor);
        if (start == -1) {
            return new LinkedList<>();
        }
        LinkedList<E> result = new LinkedList<>();
        String valorStr = String.valueOf(valor).toLowerCase();
        for (int i = start; i < array.length; i++) {
            Object currentVal = getAtributeValues(array[i], criterio);
            if (currentVal != null && String.valueOf(currentVal).toLowerCase().startsWith(valorStr)) {
                result.add(array[i]);
            } else {
                break;
            }
        }
        return result;
    }
    //En una lista ordenada los valores iguales estan juntos,
    // por lo que se puede usar una busqueda binaria para encontrar el primer elemento que cumple el criterio
    private static <E> int obtenerPosicionDeInicio(E[] lista, String criterio, Object valor) {
        if (lista == null || lista.length == 0 || valor == null) {
            return -1;
        }
        int left = 0;
        int right = lista.length - 1;
        String valorStr = String.valueOf(valor).toLowerCase();

        while (left <= right) {
            int mid = left + (right - left) / 2;
            Object midValue = getAtributeValues(lista[mid], criterio);

            if (midValue == null) {
                right = mid - 1;
                continue;
            }

            String midValueStr = String.valueOf(midValue).toLowerCase();

            if (midValueStr.startsWith(valorStr)) {
                if (mid == 0) {
                    return mid;
                }
                Object prevValue = getAtributeValues(lista[mid - 1], criterio);
                if (prevValue == null || !String.valueOf(prevValue).toLowerCase().startsWith(valorStr)) {
                    return mid;
                }
                right = mid - 1;
            } else if (midValueStr.compareTo(valorStr) < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }
// Filtra una lista de objetos o Map por un criterio y un valor, devolviendo una nueva lista con los elementos que cumplen la condicion
    //solo se tienen en cuenta los que empiezan con el valor, no los que lo contienen, aun no trabaja buien con rangos numericos
    public static <E> LinkedList<E> filter(LinkedList<E> lista, String criterio, Object valor) {
        LinkedList<E> result = new LinkedList<>();
        if (lista == null || lista.isEmpty() || valor == null) {
            return result;
        }
        String valorStr = String.valueOf(valor).toLowerCase();
        for (E element : lista.toArray()) {
            Object value = getAtributeValues(element, criterio);
            if (value != null && String.valueOf(value).toLowerCase().startsWith(valorStr)) {
                result.add(element);
            }
        }
        return result;
    }
    // Obtiene el valor del atributo especificado de un elemento, ya sea un objeto o Campo de un Map.

    public static <E> Object getAtributeValues(E element, String criterio) {
        if (element instanceof Map) {
            return ((Map<?, ?>) element).get(criterio);
        }

        Object value = null;
        String formattedCriterio = criterio.substring(0, 1).toUpperCase() + criterio.substring(1);
        try {
            value = element.getClass().getMethod("get" + formattedCriterio).invoke(element);
        } catch (NoSuchMethodException e) {
            System.err.println("Metodo no encontrado: " + e.getMessage());
        } catch (InvocationTargetException e) {
            System.err.println("Error al invocar el metodo: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
        }
        return value;
    }
    // Compara dos valores y devuelve un entero que indica su orden, pero los nulos siempre al final
    // para respetar los tipos de datos, se intenta convertir a Double si son cadenas, no se si los numero viene como Strings
    private static int compareValues(Object value1, Object value2) {
        if (value1 == null && value2 == null) return 0;
        //Los nulos siempre al final
        if (value1 == null) return 1;
        if (value2 == null) return -1;

        if (value1 instanceof String && value2 instanceof String) {
            String s1 = (String) value1;
            String s2 = (String) value2;
            try {
                Double num1 = Double.parseDouble(s1);
                Double num2 = Double.parseDouble(s2);
                return num1.compareTo(num2);
            } catch (NumberFormatException e) {
                return s1.compareToIgnoreCase(s2);
            }
        }

        if (value1 instanceof Enum && value2 instanceof Enum) {
            Enum<?> enum1 = (Enum<?>) value1;
            Enum<?> enum2 = (Enum<?>) value2;
            if (enum1.getClass() == enum2.getClass()) {
                return Integer.compare(enum1.ordinal(), enum2.ordinal());
            }
            return enum1.name().compareToIgnoreCase(enum2.name());
        }

        if (value1 instanceof Enum) {
            return ((Enum<?>) value1).name().compareToIgnoreCase(value2.toString());
        }
        if (value2 instanceof Enum) {
            return value1.toString().compareToIgnoreCase(((Enum<?>) value2).name());
        }

        if (value1 instanceof Comparable && value2 instanceof Comparable) {
            try {
                return ((Comparable<Object>) value1).compareTo(value2);
            } catch (ClassCastException e) {
                return value1.toString().compareToIgnoreCase(value2.toString());
            }
        }
        return value1.toString().compareToIgnoreCase(value2.toString());
    }

    public static void main(String[] args) throws Exception {

    }
}
