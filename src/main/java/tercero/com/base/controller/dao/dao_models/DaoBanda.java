package tercero.com.base.controller.dao.dao_models;

import java.util.Date;

import tercero.com.base.controller.DataStruc.List.LinkedList;
import tercero.com.base.controller.dao.AdapterDao;
import tercero.com.base.models.Banda;

public class DaoBanda extends AdapterDao<Banda> {
    private Banda obj;
    private LinkedList<Banda> aux;

    public DaoBanda() {
        super(Banda.class);
        // TODO Auto-generated constructor stub
    }

    // getter and setter
    public Banda getObj() {
        if (obj == null) {
            this.obj = new Banda();

        }
        return this.obj;
    }

    public void setObj(Banda obj) {
        this.obj = obj;
    }

    public Boolean save() {
        try {
            obj.setId(this.listAll().getLength() + 1);
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
    public LinkedList<Banda> getListAll() {
        if (aux == null) {
            this.aux = listAll();
        }
        return aux;
    }


    public static void main(String[] args) {
        DaoBanda da = new DaoBanda();
        da.getObj().setId(da.listAll().getLength() + 1);
        da.getObj().setNombre("Corazon cerrano ");
        da.getObj().setFechaCreacion(new Date());
        if (da.save()) {
            System.out.println("Guardado");
        } else {
            System.out.println("Error al guardar");

        }
        
    }

}
