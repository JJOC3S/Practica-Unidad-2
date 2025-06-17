package tercero.com.base.controller.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import tercero.com.base.controller.dao.dao_models.DaoGenero;
import tercero.com.base.models.Genero;
import tercero.com.base.models.TipoArchivoEnum;

@BrowserCallable
@AnonymousAllowed
public class GeneroService {

    private DaoGenero db;
    public GeneroService() {
        db = new DaoGenero();
    }

    public Genero getGenero() {
        return db.getObj();
    }
    public void setGenero(Genero genero) {
        db.setObj(genero);
    }
    public Genero getGeneroById(Integer id) throws Exception {
        if (id != null) {
            return db.getGeneroById(id);
        }
        return null;
    }

    public void createGenero(String nombre, String duracion, String url, TipoArchivoEnum tipo) throws Exception {
        if (nombre.trim().length() > 0 && duracion.trim().length() > 0 && url.trim().length() > 0 && tipo != null) {
            db.getObj().setId(lisAllGenero().size() + 1);
            db.getObj().setNombre(nombre);
            if (!db.save()) {
                throw new Exception("Error al guardar la genero");
            }
        }
    }
    public Boolean createGenero(Genero genero) throws Exception {
        if (genero != null) {
            db.setObj(genero);
            if (!db.save()) {
                throw new Exception("Error al guardar la genero");
            }
            return true;
        }
        return false;
    }

    public void updateGenero(Integer id, String nombre, String duracion, String url, TipoArchivoEnum tipo) throws Exception {
        if (id != null && nombre.trim().length() > 0 && duracion.trim().length() > 0 && url.trim().length() > 0 && tipo != null) {
            db.getObj().setId(id);
            db.getObj().setNombre(nombre);
            if (!db.update(id)) {
                throw new Exception("Error al actualizar la genero");
            }
        }
    }


    public List<Genero> lisAllGenero() {
        try {
            return Arrays.asList(db.listAll().toArray());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<String> listTipoArchivo() {
        List<String> lista = new ArrayList<>();
        for (TipoArchivoEnum tipo : TipoArchivoEnum.values()) {
            lista.add(tipo.name()); // o tipo.toString() si tienes un método personalizado
        }
        return lista;
    }

}