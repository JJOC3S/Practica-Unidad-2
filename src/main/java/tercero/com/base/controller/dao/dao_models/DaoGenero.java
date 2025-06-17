package tercero.com.base.controller.dao.dao_models;

import tercero.com.base.controller.DataStruc.List.LinkedList;
import tercero.com.base.controller.dao.AdapterDao;
import tercero.com.base.models.Genero;

public class DaoGenero extends AdapterDao<Genero> {
    private Genero obj;
    private LinkedList<Genero> aux;

    public DaoGenero() {
        super(Genero.class);
    }

    // getter and setter
    public Genero getObj() {
        if (obj == null) {
            this.obj = new Genero();

        }
        return this.obj;
    }

    public void setObj(Genero obj) {
        this.obj = obj;
    }

    public Boolean save() {
        try {
            this.obj.setId(this.listAll().getLength() + 1);
            this.persist(obj);
            return true;
        } catch (Exception e) {
            
            return false;
            // TODO: handle exception
        }
    }

    public Boolean update(Integer pos) {
        try {
            this.update(obj, pos - 1);
            return true;
        } catch (Exception e) {
            
            return false;
            // TODO: handle exception
        }
    }
    public LinkedList<Genero> getListAll() {
        if (aux == null) {
            this.aux = listAll();
        }
        return aux;
    }
    public Genero getGeneroById(Integer id)throws Exception {
        if (id != null) {
            return this.get(id);
        }
        return null;
    }

    public static void main(String[] args) {
        DaoGenero da = new DaoGenero();
        da.getObj().setId(da.listAll().getLength() + 1);
        da.getObj().setNombre("Heavy Cumbia");
        if (da.save()) {
            System.out.println("Guardado genero");
        } else {
            System.out.println("Error al guardar");

        }
      
    }

}
