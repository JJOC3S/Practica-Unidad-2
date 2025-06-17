import {
    Button,
    ComboBox,
    Dialog,
    Grid,
    GridColumn,
    Icon,
    Notification,
    TextField,
    VerticalLayout,
} from '@vaadin/react-components';
import { useCallback, useEffect, useState } from 'react';
import handleError from 'Frontend/views/_ErrorHandler';
import { Group, ViewToolbar } from 'Frontend/components/ViewToolbar';
import { CancionService, AlbumService, GeneroService } from '../generated/endpoints';

import Album from 'Frontend/generated/tercero/com/base/models/Album';
import Genero from 'Frontend/generated/tercero/com/base/models/Genero';
import TipoArchivoEnum from 'Frontend/generated/tercero/com/base/models/TipoArchivoEnum';

const labelsMap: Record<string, string> = {
    FISICO: 'Físico',
    VIRTUAL: 'Virtual',
};

type CancionMap = Record<string, string | undefined>;

function isValidNumber(value: string) {
    const num = Number(value);
    return !isNaN(num) && num > 0;
}

function formatDuracion(segundos: number | string): string {
    const s = typeof segundos === 'string' ? parseInt(segundos, 10) : segundos;
    if (isNaN(s)) return '00:00';
    const minutos = Math.floor(s / 60);
    const segundosRestantes = s % 60;
    return `${String(minutos).padStart(2, '0')}:${String(segundosRestantes).padStart(2, '0')}`;
}

function tipoArchivoRenderer({ item }: { item: CancionMap }) {
    if (!item.tipo) return null;
    return <span>{labelsMap[item.tipo] ?? item.tipo}</span>;
}

type CancionEntryFormProps = {
    onCancionCreated?: () => void;
};

function CancionEntryForm({ onCancionCreated }: CancionEntryFormProps) {
    const [dialogOpened, setDialogOpened] = useState(false);
    const [albums, setAlbums] = useState<Album[]>([]);
    const [generos, setGeneros] = useState<Genero[]>([]);
    const [tipos, setTipos] = useState<{ label: string; value: string }[]>([]);
    const [nombre, setNombre] = useState('');
    const [duracion, setDuracion] = useState('');
    const [tipo, setTipo] = useState<string | undefined>(undefined);
    const [albumId, setAlbumId] = useState<string | undefined>(undefined);
    const [generoId, setGeneroId] = useState<string | undefined>(undefined);

    useEffect(() => {
        AlbumService.lisAllAlbum()
            .then((data) => setAlbums((data ?? []).filter((item): item is Album => !!item)))
            .catch(handleError);
        GeneroService.lisAllGenero()
            .then((data) => setGeneros((data ?? []).filter((item): item is Genero => !!item)))
            .catch(handleError);
        const tiposLista = Object.entries(TipoArchivoEnum).map(([key]) => ({
            label: labelsMap[key] ?? key,
            value: key,
        }));
        setTipos(tiposLista);
    }, []);

    const open = () => setDialogOpened(true);
    const close = () => setDialogOpened(false);
    const createCancion = async () => {
        if (!nombre.trim() || !duracion.trim() || !tipo || !albumId || !generoId) {
            Notification.show('Faltan datos para crear la canción', { duration: 5000, position: 'top-center', theme: 'error' });
            return;
        }
        if (!isValidNumber(duracion)) {
            Notification.show('Duración inválida', { duration: 4000, position: 'top-center', theme: 'error' });
            return;
        }
        try {
            await CancionService.createCancion(nombre.trim(), duracion.trim(), tipo, albumId, generoId);
            onCancionCreated?.();
            setNombre(''); setDuracion(''); setTipo(undefined); setAlbumId(undefined); setGeneroId(undefined);
            setDialogOpened(false);
            Notification.show('Cancion creada exitosamente', { duration: 5000, position: 'bottom-end', theme: 'success' });
        } catch (error) {
            handleError(error);
        }
    };

    return (
        <>
            <Dialog aria-label="Registrar Cancion" draggable modeless opened={dialogOpened} onOpenedChanged={(e) => setDialogOpened(e.detail.value)}
                header={<h2 className="draggable" style={{ flex: 1, cursor: 'move', margin: 0, fontSize: '1.5em', fontWeight: 'bold', padding: 'var(--lumo-space-m) 0' }}>Registrar Canción</h2>}
                footerRenderer={() => (<><Button onClick={close}>Cancelar</Button><Button theme="primary" onClick={createCancion}>Registrar</Button></>)}
            >
                <VerticalLayout theme="spacing" style={{ width: '300px', maxWidth: '100%', alignItems: 'stretch' }}>
                    <TextField label="Nombre" placeholder="Ingrese el nombre de la canción" value={nombre} onValueChanged={(e) => setNombre(e.detail.value)} />
                    <TextField label="Duración (segundos)" placeholder="Ingrese la duración de la canción" value={duracion} onValueChanged={(e) => setDuracion(e.detail.value)} />
                    <ComboBox label="Tipo de archivo" items={tipos} itemLabelPath="label" itemValuePath="value" value={tipo} onValueChanged={(e) => setTipo(e.detail.value)} placeholder="Seleccione tipo de archivo" />
                    <ComboBox label="Álbum" items={albums} itemLabelPath="nombre" itemValuePath="id" value={albumId} onValueChanged={(e) => setAlbumId(e.detail.value)} placeholder="Seleccione un álbum" />
                    <ComboBox label="Género" items={generos} itemLabelPath="nombre" itemValuePath="id" value={generoId} onValueChanged={(e) => setGeneroId(e.detail.value)} placeholder="Seleccione un género" />
                </VerticalLayout>
            </Dialog>
            <Button onClick={open}>Registrar</Button>
        </>
    );
}

type CancionDetailEditDialogProps = {
    cancion: CancionMap;
    opened: boolean;
    onClose: () => void;
    onCancionUpdated: () => void;
};

function CancionDetailEditDialog({ cancion, opened, onClose, onCancionUpdated }: CancionDetailEditDialogProps) {
    const [isEditing, setIsEditing] = useState(false);
    const [nombre, setNombre] = useState('');
    const [duracion, setDuracion] = useState('');

    useEffect(() => {
        if (cancion) {
            setNombre(cancion.nombre ?? '');
            setDuracion(cancion.duracion ?? '');
            setIsEditing(false); // Siempre empieza en modo "detalle"
        }
    }, [cancion]);

    const handleUpdate = async () => {
        if (!nombre.trim() || !duracion.trim()) {
            Notification.show('El nombre y la duración son obligatorios.', { theme: 'error' });
            return;
        }
        if (!isValidNumber(duracion)) {
            Notification.show('La duración debe ser un número positivo.', { theme: 'error' });
            return;
        }

        const id = cancion.id ? parseInt(cancion.id, 10) : undefined;
        if (id === undefined || isNaN(id)) {
            Notification.show('Error: ID de canción no válido.', { theme: 'error' });
            return;
        }

        try {
            await CancionService.updateCancion(id, nombre, duracion);
            Notification.show('Canción actualizada exitosamente', { theme: 'success' });
            onCancionUpdated();
        } catch (error) {
            handleError(error);
        }
    };

    const renderFooter = () => {
        if (isEditing) {
            return (
                <>
                    <Button onClick={() => setIsEditing(false)}>Cancelar Edición</Button>
                    <Button theme="primary" onClick={handleUpdate}>Guardar Cambios</Button>
                </>
            );
        }
        return (
            <>
                <Button onClick={onClose}>Cerrar</Button>
                <Button theme="primary" onClick={() => setIsEditing(true)}>Editar</Button>
            </>
        );
    };

    return (
        <Dialog
            aria-label="Detalle de Canción" draggable modeless opened={opened}
            onOpenedChanged={(e) => !e.detail.value && onClose()}
            header={
                <h2 className="draggable" style={{ flex: 1, cursor: 'move', margin: 0, fontSize: '1.5em', fontWeight: 'bold' }}>
                    {isEditing ? `Editando: ${cancion.nombre}` : `Detalle: ${cancion.nombre}`}
                </h2>
            }
            footerRenderer={renderFooter}
        >
            {isEditing ? (
                <VerticalLayout theme="spacing" style={{ width: '350px', maxWidth: '100%' }}>
                    <TextField label="Nombre" value={nombre} onValueChanged={(e) => setNombre(e.detail.value)} />
                    <TextField label="Duración (segundos)" value={duracion} onValueChanged={(e) => setDuracion(e.detail.value)} />
                </VerticalLayout>
            ) : (
                <VerticalLayout theme="spacing-s" style={{ width: '350px', maxWidth: '100%', fontSize: 'var(--lumo-font-size-m)' }}>
                    <div className='flex justify-between'><strong>Álbum:</strong><span>{cancion.album}</span></div>
                    <div className='flex justify-between'><strong>Género:</strong><span>{cancion.genero}</span></div>
                    <div className='flex justify-between'><strong>Duración:</strong><span>{formatDuracion(cancion.duracion ?? '0')}</span></div>
                    <div className='flex justify-between'><strong>Tipo de Archivo:</strong><span>{labelsMap[cancion.tipo ?? ''] ?? cancion.tipo}</span></div>
                    <div className='flex justify-between items-center'><strong>URL:</strong><span>{cancion.url || 'No disponible'}</span></div>
                </VerticalLayout>
            )}
        </Dialog>
    );
}

export default function CancionListView() {
    const [canciones, setCanciones] = useState<CancionMap[]>([]);
    const [selectedCancion, setSelectedCancion] = useState<CancionMap | null>(null);
    const [detailDialogOpened, setDetailDialogOpened] = useState(false);
    const [criterioOrden, setCriterioOrden] = useState<string | null>(null);
    const [orden, setOrden] = useState<'asc' | 'desc' | null>(null);
    const [metodoOrdenacion, setMetodoOrdenacion] = useState('QUICK');
    const [criterioBusqueda, setCriterioBusqueda] = useState('nombre');
    const [valorBusqueda, setValorBusqueda] = useState('');
    const [metodoBusqueda, setMetodoBusqueda] = useState('sequential');
    const [accion, setAccion] = useState<'list' | 'sort' | 'search'>('list');

    const metodosDeOrdenacion = [{ label: 'Quick Sort', value: 'QUICK' }, { label: 'Shell Sort', value: 'SHELL' }];
    const criteriosDeBusqueda = [{ label: 'Nombre', value: 'nombre' }, { label: 'Tipo', value: 'tipo' }];
    const metodosDeBusqueda = [{ label: 'Secuencial', value: 'sequential' }, { label: 'Secuencial Binaria', value: 'sequential_binary' }];

    const fetchData = useCallback(async () => {
        try {
            let result: CancionMap[] | undefined;
            if (accion === 'search' && valorBusqueda.trim()) {
                result = (await CancionService.searchCancion(criterioBusqueda, valorBusqueda.trim(), metodoBusqueda)) as CancionMap[];
            } else if (accion === 'sort' && criterioOrden && orden) {
                const ordenNum = orden === 'asc' ? 1 : -1;
                result = (await CancionService.sortCancion(criterioOrden, ordenNum, metodoOrdenacion)) as CancionMap[];
            } else {
                result = (await CancionService.listCanciones()) as CancionMap[];
            }
            setCanciones(result?.filter((c): c is CancionMap => !!c) ?? []);
        } catch (error) {
            handleError(error);
            setCanciones([]);
        }
    }, [accion, valorBusqueda, criterioBusqueda, metodoBusqueda, criterioOrden, orden, metodoOrdenacion]);

    useEffect(() => {
        fetchData();
    }, [fetchData]);

    const handleSort = (path: string, direction: 'asc' | 'desc') => {
        setAccion('sort');
        setCriterioOrden(path);
        setOrden(direction);
    };

    const handleSearch = () => {
        if (!valorBusqueda.trim()) {
            Notification.show('Ingrese un valor para buscar.', { theme: 'contrast', position: 'bottom-start' });
            return;
        }
        setAccion('search');
        setCriterioOrden(null);
        setOrden(null);
    };

    const clearActions = () => {
        setAccion('list');
        setCriterioOrden(null);
        setOrden(null);
        setValorBusqueda('');
    };

    const handleUrlClick = (cancion: CancionMap) => {
        setSelectedCancion(cancion);
        setDetailDialogOpened(true);
    };

    const SortableHeader = ({ path, title }: { path: string, title: string }) => {
        const isSortingAsc = criterioOrden === path && orden === 'asc';
        const isSortingDesc = criterioOrden === path && orden === 'desc';
        const activeColor = 'var(--lumo-primary-text-color)';
        const inactiveColor = 'var(--lumo-contrast-60pct)';
        return (
            <div style={{ display: 'flex', alignItems: 'center', gap: 'var(--lumo-space-m)' }}>
                <span>{title}</span>
                <div style={{ display: 'flex', flexDirection: 'column', lineHeight: '1' }}>
                    <Icon icon="vaadin:caret-up" onClick={() => handleSort(path, 'asc')} style={{ color: isSortingAsc ? activeColor : inactiveColor, cursor: 'pointer', height: '12px' }} />
                    <Icon icon="vaadin:caret-down" onClick={() => handleSort(path, 'desc')} style={{ color: isSortingDesc ? activeColor : inactiveColor, cursor: 'pointer', height: '12px' }} />
                </div>
            </div>
        );
    };

    return (
        <main className="w-full h-full flex flex-col box-border gap-s p-m">
            <ViewToolbar title="Canciones">
                <div className="flex items-end gap-s ml-auto">
                    <ComboBox label="Método Ordenación" items={metodosDeOrdenacion} value={metodoOrdenacion} onValueChanged={(e) => setMetodoOrdenacion(e.detail.value)} />
                    <CancionEntryForm onCancionCreated={clearActions} />
                </div>
            </ViewToolbar>

            <div className="p-s rounded-l" style={{ background: 'var(--lumo-contrast-5pct)' }}>
                 <Group>
                    <ComboBox label="Buscar por" items={criteriosDeBusqueda} value={criterioBusqueda} onValueChanged={(e) => setCriterioBusqueda(e.detail.value)} />
                    <TextField label="Valor" placeholder="Ingrese valor..." value={valorBusqueda} onValueChanged={(e) => setValorBusqueda(e.detail.value)} />
                    <ComboBox label="Método Búsqueda" items={metodosDeBusqueda} value={metodoBusqueda} onValueChanged={(e) => setMetodoBusqueda(e.detail.value)} />
                    <Button onClick={handleSearch} theme="primary">Buscar</Button>
                    <Button onClick={clearActions} title="Limpiar búsqueda y orden"><Icon icon="vaadin:refresh"/></Button>
                </Group>
            </div>

            <Grid items={canciones} className="flex-grow">
                <GridColumn headerRenderer={() => <SortableHeader path="nombre" title="Nombre" />} path="nombre" />
                <GridColumn headerRenderer={() => <SortableHeader path="duracion" title="Duración" />} renderer={({ item }) => <span>{item.duracion != null ? formatDuracion(item.duracion) : '00:00'}</span>} />
                <GridColumn headerRenderer={() => <SortableHeader path="tipo" title="Tipo" />} renderer={tipoArchivoRenderer} />
                <GridColumn path="album" header="Álbum" />
                <GridColumn path="genero" header="Género" />
                <GridColumn
                    header="URL"
                    renderer={({ item }) => (
                        <span
                            style={{ color: 'var(--lumo-primary-text-color)', textDecoration: 'underline', cursor: 'pointer' }}
                            onClick={() => handleUrlClick(item)}
                        >
                            {item.url || 'Ver Detalles'}
                        </span>
                    )}
                />
            </Grid>

            {selectedCancion && (
                <CancionDetailEditDialog
                    cancion={selectedCancion}
                    opened={detailDialogOpened}
                    onClose={() => setDetailDialogOpened(false)}
                    onCancionUpdated={() => {
                        setDetailDialogOpened(false);
                        fetchData();
                    }}
                />
            )}
        </main>
    );
}