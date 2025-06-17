package tercero.com.base.controller.services;

import java.util.Arrays;
import java.util.List;


import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import tercero.com.base.controller.dao.dao_models.DaoArtista;
import tercero.com.base.models.Artista;

@BrowserCallable
@AnonymousAllowed
public class ArtistaService {

    private DaoArtista db;
    public ArtistaService() {
        db = new DaoArtista();
    }

    public List<Artista> lisAllArtista(){
        return Arrays.asList(db.listAll().toArray());
        
    }
    
}
