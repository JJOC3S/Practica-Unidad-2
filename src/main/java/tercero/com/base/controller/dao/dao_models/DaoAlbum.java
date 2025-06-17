package tercero.com.base.controller.dao.dao_models;

import java.util.Date;

import tercero.com.base.controller.DataStruc.List.LinkedList;
import tercero.com.base.controller.dao.AdapterDao;
import tercero.com.base.models.Album;

public class DaoAlbum extends AdapterDao<Album> {
    private Album obj;
    private LinkedList<Album> listAll;

    public DaoAlbum() {
        super(Album.class);
        // TODO Auto-generated constructor stub
    }

    // getter and setter
    public Album getObj() {
        if (obj == null) {
            this.obj = new Album();

        }
        return this.obj;
    }
    public Album getObjById(Integer id) throws Exception {
        if (id != null) {
            return this.get(id);
        }
        return null;
    }
    public void setObj(Album obj) {
        this.obj = obj;
    }

    public Boolean save() {
        try {
            this.persist(obj);
            return true;
        } catch (Exception e) {
            
            return false;
            // TODO: handle exception
        }
    }

    public Boolean update(Integer pos) {
        try {
            this.update(obj, pos);
            return true;
        } catch (Exception e) {
            
            return false;
            // TODO: handle exception
        }
    }
    public LinkedList<Album> getListAll() { // Obtiene la lista de objetos
        if (listAll == null) { // Si la lista es nula
            this.listAll = listAll(); // Invoca el método listAll() para obtener la lista de objetos
        }
        return listAll; // Devuelve la lista de objetos de la variable listAll
    }
    public Album getAlbumById(Integer id)throws Exception { // Obtiene un objeto Album por su id
        if (id != null) { // Si el id no es nulo
            return this.get(id); // Invoca el método get() para obtener el objeto Album por su id
        }
        return null; // Si el id es nulo, devuelve null
    }

    public static void main(String[] args) {
        DaoAlbum da = new DaoAlbum();
        da.getObj().setId(da.listAll().getLength() + 1);
         da.getObj().setFecha(new Date());
        da.getObj().setId_Banda(2);
        da.getObj().setNombre("Sexolandia 1");
        if (da.save()) {
            System.out.println("Guardado");
        } else {
            System.out.println("Error al guardar");

        }
        System.out.println(da.getListAll().getLength());
       
        
    }

}
