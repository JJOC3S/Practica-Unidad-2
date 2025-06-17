package tercero.com.base.ui.view;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Anchor;
import tercero.com.base.controller.services.AlbumService;
import tercero.com.base.controller.services.GeneroService;
import tercero.com.base.models.Album;
import tercero.com.base.models.Genero;
import tercero.com.base.models.TipoArchivoEnum;
import tercero.com.base.ui.component.ViewToolbar;
import tercero.com.base.models.Cancion;

import tercero.com.base.controller.services.CancionService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.List;
import java.util.stream.Stream;

@PageTitle("Cancion List Flow")
@Menu(order = 0, icon = "vaadin:clipboard-check", title = "Cancion List Flow")
public class CancionListView extends Main {

    private final CancionService cancionService;
    private final AlbumService albumService = new AlbumService();
    private final GeneroService generoService = new GeneroService();

    private final TextField nombre;
    private final ComboBox<Genero> generoCombo;
    private final TextField duracion;
    private final ComboBox<String> tipo;
    private final ComboBox<Album> albumCombo;
    private final Button createBtn;
    private final Grid<Cancion> cancionGrid;

    public CancionListView(CancionService cancionService) throws Exception {
        this.cancionService = cancionService;

        // Campos
        nombre = new TextField("Nombre");
        nombre.setPlaceholder("Nombre de la canción");
        nombre.setMinWidth("20em");

        generoCombo = new ComboBox<>("Género");
        List<Genero> generos = generoService.lisAllGenero();
        generoCombo.setItems(generos);
        generoCombo.setItemLabelGenerator(Genero::getNombre);
        generoCombo.setMinWidth("12em");

        duracion = new TextField("Duración (segundos)");
        duracion.setPlaceholder("Ej: 245");
        duracion.setMinWidth("7em");

        tipo = new ComboBox<>("Tipo");
        tipo.setItems(Stream.of(TipoArchivoEnum.values()).map(Enum::name).toList());
        tipo.setPlaceholder("Tipo de archivo");
        tipo.setMinWidth("7em");

        albumCombo = new ComboBox<>("Álbum");
        List<Album> albums = albumService.lisAllAlbum();
        albumCombo.setItems(albums);
        albumCombo.setItemLabelGenerator(Album::getNombre);
        albumCombo.setMinWidth("12em");

        // Botón Crear
        createBtn = new Button("Crear", event -> {
            try {
                createCancion();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        createBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Grid
        cancionGrid = new Grid<>(Cancion.class, false);
        cancionGrid.setItems(cancionService.listAllCancion());
        cancionGrid.addColumn(Cancion::getNombre).setHeader("Nombre");
        cancionGrid.addColumn(cancion -> {
            if (cancion.getId_genero() == null) return "(sin género)";
            try {
                return generoService.getGeneroById(cancion.getId_genero()).getNombre();
            } catch (Exception e) {
                return "(error)";
            }
        }).setHeader("Género");
        cancionGrid.addColumn(c -> formatDuration(c.getDuracion())).setHeader("Duración");
        cancionGrid.addColumn(Cancion::getUrl).setHeader("URL");
        cancionGrid.addColumn(Cancion::getTipo).setHeader("Tipo");
        cancionGrid.addColumn(c -> {
            if (c.getId_album() == null) return "(sin álbum)";
            try {
                return albumService.getAlbumById(c.getId_album()).getNombre();
            } catch (Exception e) {
                return "(error)";
            }
        }).setHeader("Álbum");
        cancionGrid.addComponentColumn(cancion -> {
            String url = "cancion/" + cancion.getId();
            Anchor link = new Anchor(url, "Ver");
            link.getElement().setAttribute("router-link", true);
            return link;
        }).setHeader("Detalle");
        cancionGrid.setSizeFull();

        // Diseño y layout
        setSizeFull();
        addClassNames(LumoUtility.BoxSizing.BORDER, LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN,
                LumoUtility.Padding.MEDIUM, LumoUtility.Gap.SMALL);

        add(new ViewToolbar("Cancion List Flow", ViewToolbar.group(
                nombre, generoCombo, duracion, tipo, albumCombo, createBtn
        )));
        add(cancionGrid);
    }

    private void createCancion() throws Exception {
        if (nombre.isEmpty()) {
            Notification.show("El nombre no puede estar vacío", 3000, Notification.Position.BOTTOM_END)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        if (tipo.isEmpty()) {
            Notification.show("Debe seleccionar un tipo de archivo", 3000, Notification.Position.BOTTOM_END)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        if (duracion.isEmpty() || !duracion.getValue().matches("\\d+")) {
            Notification.show("Duración inválida", 3000, Notification.Position.BOTTOM_END)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        TipoArchivoEnum tipoEnum = TipoArchivoEnum.valueOf(tipo.getValue());

        Cancion nuevaCancion = new Cancion();
        nuevaCancion.setNombre(nombre.getValue());
        nuevaCancion.setDuracion(Integer.parseInt(duracion.getValue()));
        nuevaCancion.setTipo(tipoEnum);
        nuevaCancion.setUrl("cancion/" + nombre.getValue().toLowerCase().trim().replace(" ", "_"));

        if (generoCombo.getValue() != null) {
            nuevaCancion.setId_genero(generoCombo.getValue().getId());
        }

        if (albumCombo.getValue() != null) {
            nuevaCancion.setId_album(albumCombo.getValue().getId());
        }

        cancionService.createCancion(nuevaCancion);
        cancionGrid.setItems(cancionService.listAllCancion());

        nombre.clear();
        generoCombo.clear();
        duracion.clear();
        tipo.clear();
        albumCombo.clear();

        Notification.show("Canción registrada", 3000, Notification.Position.BOTTOM_END)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    private String formatDuration(Integer duracionSegundos) {
        if (duracionSegundos == null || duracionSegundos < 0) {
            return "00:00";
        }
        int minutos = duracionSegundos / 60;
        int segundos = duracionSegundos % 60;
        return String.format("%02d:%02d", minutos, segundos);
    }
}
