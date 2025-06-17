package tercero.com.base.controller.services;

import java.util.*;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import tercero.com.base.controller.dao.dao_models.*;
import tercero.com.base.models.Cancion;
import tercero.com.base.models.TipoArchivoEnum;
import tercero.com.base.controller.DataStruc.List.LinkedList;

@BrowserCallable
@AnonymousAllowed
public class CancionService {

    private final DaoCancion db;

    public CancionService() {
        db = new DaoCancion();
    }

    public Cancion getCancion() {
        return db.getObj();
    }
    public void setCancion(Cancion cancion) {
        db.setObj(cancion);
    }
    public Cancion getCancionById(Integer id) throws Exception {
        if (id != null) {
            return db.getCancionById(id);
        }
        return null;
    }

    public void createCancion(String nombre, String duracion, String tipo, String idAlbum, String idGenero) throws Exception {
        if (nombre.trim().length() > 0 && duracion.trim().length() > 0 && tipo != null) {

            Cancion nueva = new Cancion();
            nueva.setNombre(nombre.trim());
            nueva.setDuracion(Integer.parseInt(duracion));
            nueva.setId_album(Integer.valueOf(idAlbum));
            nueva.setId_genero(Integer.valueOf(idGenero));

            nueva.setTipo(TipoArchivoEnum.valueOf(tipo));

            db.setObj(nueva);
            if (!db.save()) {
                throw new Exception("Error al guardar la canción");
            }
        }
    }

    public Boolean createCancion(Cancion cancion) throws Exception {
        if (cancion != null) {
            db.setObj(cancion);
            int id = db.listAll().getLast().getId() + 1;
            db.getObj().setUrl("cancion/" + id);
            if (!db.save()) {
                throw new Exception("Error al guardar la cancion");
            }
            return true;
        }
        return false;
    }

    public void updateCancion(Integer id, String nombre, String duracion) throws Exception {
        if (id == null || nombre == null || nombre.trim().isEmpty()
                || duracion == null || duracion.trim().isEmpty()){
            throw new IllegalArgumentException("Parámetros inválidos para la canción");
        }
        int duracionNum;
        try {
            duracionNum = Integer.parseInt(duracion);
            if (duracionNum <= 0) {
                throw new IllegalArgumentException("La duración debe ser un número positivo");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Duración inválida: debe ser un número");
        }
        db.setObj(db.getCancionById(id));
        System.out.println("Actualizando canción con ID: " + db.getObj().getNombre());
        db.getObj().setNombre(nombre.trim());
        db.getObj().setDuracion(duracionNum);

        if (!db.update(id)) {
            throw new Exception("Error al actualizar la canción");
        }
    }
    public LinkedList<Cancion> listAllCancions() {
        try {
            return this.db.listAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar las canciones", e);
        }
    }
    public List<HashMap<String,String>> searchCancion(String criterio, String valor, String metodo) {
        if (criterio.trim().isEmpty() || valor.trim().isEmpty() || metodo.trim().isEmpty()) {
            throw new IllegalArgumentException("Criterio, valor y método no pueden estar vacíos");
        }
        List<HashMap<String, String>> cancionesEncontradas = new ArrayList<>();
        try {
            LinkedList<Cancion> canciones = db.search(criterio, valor, metodo);
            if (!canciones.isEmpty()) {
                AlbumService as = new AlbumService();
                GeneroService gs = new GeneroService();
                for (Cancion cancion : canciones.toArray()) {
                    cancionesEncontradas.add(toMap(cancion, as, gs));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar: " + e.getMessage(), e);
        }
        return cancionesEncontradas;
    }
    public List<HashMap<String, String>> sortCancion(String criterio, Integer orden, String metodo) throws Exception {
        Cancion[] canciones = db.orderBy(criterio, orden, metodo).toArray();
        List<HashMap<String, String>> cancionesOrdenadas = new ArrayList<>();
        AlbumService as = new AlbumService();
        GeneroService gs = new GeneroService();
        for (Cancion cancion : canciones) {
            cancionesOrdenadas.add(toMap(cancion, as, gs));
        }
        return  cancionesOrdenadas;
    }

    public List<Cancion> listAllCancion() {
        try {
            return Arrays.asList(db.listAll().toArray());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public TipoArchivoEnum[] getTipoArchivoEnum() {
        return TipoArchivoEnum.values();
    }
    public static void main(String[] args) throws Exception {
        CancionService cancionService = new CancionService();
        List<HashMap<String, String>> filtradas = cancionService.searchCancion("Tipo", "Fisico", "sequential");
        System.out.println("Canciones Filtradas por Tipo 'Fisico':");
        for (HashMap<String, String> cancion : filtradas) {
            System.out.println(cancion);
        }
        System.out.println("--------------------------------------------------");
        List<HashMap<String, String>> filtradas2 = cancionService.searchCancion("Tipo", "Fisico", "sequential_binary");
        System.out.println("Canciones Filtradas por Tipo 'Fisico' (Búsqueda Binaria):");
        for (HashMap<String, String> cancion : filtradas2) {
            System.out.println(cancion);
        }
        System.out.println("--------------------------------------------------");
        List<HashMap<String, String>> cancionesOrdenadas = new CancionService().sortCancion("nombre", 1, "Shell");
        System.out.println("Canciones Ordenadas por nombre (ShellSort):");
        for (HashMap<String, String> cancion : cancionesOrdenadas) {
            System.out.println(cancion);
        }

        System.out.println("--------------------------------------------------");
        //lista todas las canciones
        List<HashMap<String, String>> todasCanciones = cancionService.listCanciones();
        System.out.println("Todas las Canciones:");
        for (HashMap<String, String> cancion : todasCanciones) {
            System.out.println(cancion);
        }



    }
    public List<HashMap<String,String> > listCanciones() throws Exception {
        Cancion[] canciones = db.listAll().toArray();
        List<HashMap<String, String>> cancionesList = new ArrayList<>();
        if(canciones.length > 0) {
            AlbumService as = new AlbumService();
            GeneroService gs = new GeneroService();
            for (Cancion cancion : canciones) {
                cancionesList.add(toMap(cancion, as, gs));
            }
        }
        return cancionesList;
    }

    public HashMap<String, String> toMap(Cancion cancion, AlbumService as, GeneroService gs) throws Exception {
        HashMap<String, String> cancionMap = new HashMap<>();
        cancionMap.put("id", cancion.getId().toString());
        cancionMap.put("nombre", cancion.getNombre());
        cancionMap.put("duracion", String.valueOf(cancion.getDuracion()));
        cancionMap.put("tipo", cancion.getTipo().toString());
        cancionMap.put("url", cancion.getUrl());
        cancionMap.put("album", as.getAlbumById(cancion.getId_album()).getNombre());
        cancionMap.put("genero", gs.getGeneroById(cancion.getId_genero()).getNombre());
        return cancionMap;
    }


}
