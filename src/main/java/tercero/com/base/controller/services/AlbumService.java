package tercero.com.base.controller.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import tercero.com.base.controller.dao.dao_models.DaoAlbum;
import tercero.com.base.models.Album;
import tercero.com.base.models.Genero;
import tercero.com.base.models.TipoArchivoEnum;

@BrowserCallable
@AnonymousAllowed
public class AlbumService {

    private DaoAlbum db;
    public AlbumService() {
        db = new DaoAlbum();
    }

    public Album getAlbum() {
        return db.getObj();
    }
    public void setAlbum(Album album) {
        db.setObj(album);
    }
    public Album getAlbumById(Integer id) throws Exception {
        if (id != null) {
            return db.getAlbumById(id);
        }
        return null;
    }

    public void createAlbum(String nombre, String duracion, String url, TipoArchivoEnum tipo) throws Exception {
        if (nombre.trim().length() > 0 && duracion.trim().length() > 0 && url.trim().length() > 0 && tipo != null) {
            db.getObj().setId(lisAllAlbum().size() + 1);
            db.getObj().setNombre(nombre);

            if (!db.save()) {
                throw new Exception("Error al guardar la album");
            }
        }
    }
    public Boolean createAlbum(Album album) throws Exception {
        if (album != null) {
            db.setObj(album);
            if (!db.save()) {
                throw new Exception("Error al guardar la album");
            }
            return true;
        }
        return false;
    }

    public void updateAlbum(Integer id, String nombre, String duracion, String url, TipoArchivoEnum tipo) throws Exception {
        if (id != null && nombre.trim().length() > 0 && duracion.trim().length() > 0 && url.trim().length() > 0 && tipo != null) {
            db.getObj().setId(id);
            db.getObj().setNombre(nombre);
            if (!db.update(id)) {
                throw new Exception("Error al actualizar la album");
            }
        }
    }


    public List<Album> lisAllAlbum() {
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
