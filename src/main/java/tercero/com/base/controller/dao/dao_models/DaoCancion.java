package tercero.com.base.controller.dao.dao_models;

import tercero.com.base.controller.DataStruc.List.LinkedList;
import tercero.com.base.controller.dao.AdapterDao;
import tercero.com.base.controller.exceptions.ListEmptyException;
import tercero.com.base.models.Cancion;


public class DaoCancion extends AdapterDao<Cancion> {
    private Cancion obj;
    private LinkedList<Cancion> aux;

    public DaoCancion() {
        super(Cancion.class);
        // TODO Auto-generated constructor stub
    }

    // getter and setter
    public Cancion getObj() {
        if (obj == null) {
            this.obj = new Cancion();

        }
        return this.obj;
    }

    public void setObj(Cancion obj) {
        this.obj = obj;
    }

    public Boolean save() throws ListEmptyException {
        int id = this.listAll().getLast().getId() + 1;
        try {
            this.obj.setId(id);
            this.persist(obj);
            return true;
        } catch (Exception e) {

            return false;
            // TODO: handle exception
        }
    }

    public Boolean update(Integer Id) {
        try {
            this.update_by_id(this.obj, Id);
            return true;
        } catch (Exception e) {

            return false;
            // TODO: handle exception
        }
    }

    public Cancion getCancionById(Integer id)throws Exception {
        if (id != null) {
            return this.get(id);
        }
        return null;
    }

    public LinkedList<Cancion> getListAll() {
        if (aux == null) {
            this.aux = listAll();
        }
        return aux;
    }

    public void setListAll(LinkedList<Cancion> aux) {
        this.aux = aux;
    }

    public LinkedList<Cancion> orderBy(String criterio, Integer tipo, String metodo) {
        LinkedList<Cancion> listToSort = this.listAll();
        if (listToSort == null || listToSort.isEmpty()) {
            return new LinkedList<>();
        }
        LinkedList<Cancion> ordenada = new LinkedList<>();
        if(metodo.equalsIgnoreCase("Shell")) {
            ordenada = CollectionTool.shellSort(listToSort, criterio, tipo);
        } else if (metodo.equalsIgnoreCase("Quick")) {
            ordenada = CollectionTool.quickSort(listToSort, criterio, tipo);
        } else {
            throw new IllegalArgumentException("Método de ordenamiento no soportado: " + metodo);
        }
        return ordenada;
    }

    public LinkedList<Cancion> search(String criterio, String valor, String metodo) {
        LinkedList<Cancion> cancionesEncontradas = new LinkedList<>();
        try {
            LinkedList<Cancion> canciones = this.listAll();

            if(metodo.equalsIgnoreCase("sequential")) {
                cancionesEncontradas = CollectionTool.filter(canciones, criterio, valor);
            } else if (metodo.equalsIgnoreCase("sequential_binary")) {
                cancionesEncontradas = CollectionTool.binarySearchRange(canciones, criterio, valor);
            } else {
                throw new IllegalArgumentException("Método de búsqueda no soportado: " + metodo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cancionesEncontradas;
    }
    private int getIndex(Integer id) {
        return CollectionTool.binarySearch(this.listAll(), "id", this.getObj().getId());
    }

    public static void main(String[] args) throws Exception {
        DaoCancion dao = new DaoCancion();
        dao.setObj(dao.getCancionById(9));
        System.out.println("Current Cancion: " + dao.getObj().getNombre());
        dao.getObj().setNombre("Test Song");
        dao.getObj().setDuracion(35);
        System.out.println("Saving Cancion: " + dao.getObj().getNombre() + "id: " + dao.getObj().getId());
        dao.update(9);
        // Save the song
        System.out.println("Updated Cancion: " + dao.getCancionById(9).getNombre());
    }



}
