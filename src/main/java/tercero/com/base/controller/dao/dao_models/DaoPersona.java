package tercero.com.base.controller.dao.dao_models;

import tercero.com.base.controller.DataStruc.List.LinkedList;
import tercero.com.base.controller.dao.AdapterDao;
import tercero.com.base.models.Persona;

public class DaoPersona extends AdapterDao<Persona> {
    private Persona obj;
    private LinkedList<Persona> aux;

    public DaoPersona() {
        super(Persona.class);
        // TODO Auto-generated constructor stub
    }

    // getter and setter
    public Persona getObj() {
        if (obj == null) {
            this.obj = new Persona();

        }
        return this.obj;
    }

    public void setObj(Persona obj) {
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
    public LinkedList<Persona> getListAll() {
        if (aux == null) {
            this.aux = listAll();
        }
        return aux;
    }


    public static void main(String[] args) {
        DaoPersona da = new DaoPersona();
        da.getObj().setId(da.listAll().getLength() + 1);
        da.getObj().setUsuario("Juan");
        da.getObj().setEdad(20);
        if (da.save()) {
            System.out.println("Guardado");
        } else {
            System.out.println("Error al guardar");

        }
        
    }

}
