import './StyleDetail.css';
import {
    TextField,
    ComboBox,
    Button,
    VerticalLayout,
} from '@vaadin/react-components';
import { Notification } from '@vaadin/react-components/Notification';
import { useSignal } from '@vaadin/hilla-react-signals';
import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import TipoArchivoEnum from '../generated/tercero/com/base/models/TipoArchivoEnum';
import { CancionService } from '../generated/endpoints';
import handleError from './_ErrorHandler';
import { ViewToolbar, Group } from 'Frontend/components/ViewToolbar';
import {ViewConfig} from "@vaadin/hilla-file-router/types.js";

const labelsMap: Record<keyof typeof TipoArchivoEnum, string> = {
    FISICO: 'Físico',
    VIRTUAL: 'Virtual',
};
export const config: ViewConfig = {
    title: 'No visible',
    menu: {
        exclude: true
    }
};
export default function CancionDetailView() {
    const { id } = useParams<{ id: string }>();
    const navigate = useNavigate();

    const [tipos, setTipos] = useState<
        { label: string; value: keyof typeof TipoArchivoEnum }[]
    >([]);
    const [loading, setLoading] = useState(false);
    const [cargandoDetalle, setCargandoDetalle] = useState(true);

    const nombre = useSignal('');
    const duracion = useSignal('');
    const url = useSignal('');
    const tipo = useSignal<keyof typeof TipoArchivoEnum | ''>('');

    useEffect(() => {
        if (!id) return;

        const fetchData = async () => {
            try {
                setCargandoDetalle(true);
                const cancion = await CancionService.getCancionById(Number(id));
                if (!cancion) throw new Error('Canción no encontrada');
                nombre.value = cancion.nombre ?? '';
                duracion.value = cancion.duracion?.toString() ?? '';
                url.value = cancion.url ?? '';
                tipo.value = cancion.tipo ?? '';
            } catch (e) {
                handleError(e);
            } finally {
                setCargandoDetalle(false);
            }
        };

        const tiposLista = Object.entries(TipoArchivoEnum).map(([key]) => ({
            label: labelsMap[key as keyof typeof TipoArchivoEnum] ?? key,
            value: key as keyof typeof TipoArchivoEnum,
        }));
        setTipos(tiposLista);

        fetchData();
    }, [id]);

    const guardar = async () => {
        if (
            !nombre.value.trim() ||
            !duracion.value.trim() ||
            !url.value.trim() ||
            !tipo.value
        ) {
            Notification.show('Todos los campos son obligatorios', { theme: 'error' });
            return;
        }

        const duracionNum = parseInt(formatDuracion(duracion.value));
        if (isNaN(duracionNum) || duracionNum <= 0) {
            Notification.show('Duración inválida', { theme: 'error' });
            return;
        }

        try {
            setLoading(true);
            await CancionService.updateCancion(
                Number(id),
                nombre.value.trim(),
                duracion.value.trim(),
                url.value.trim(),
                tipo.value as TipoArchivoEnum,
            );
            Notification.show('Canción actualizada', { theme: 'success' });
        } catch (e) {
            handleError(e);
        } finally {
            setLoading(false);
        }
    };

    if (cargandoDetalle) {
        return <p>Cargando detalles...</p>;
    }

    // @ts-ignore
    return (
        <>
            <ViewToolbar title="Detalle de Canción">
                <Group>
                    <Button theme="tertiary" onClick={() => navigate('/cancion-list')}>
                        Volver
                    </Button>
                </Group>
            </ViewToolbar>

            <main className="main-container">
                <VerticalLayout theme="spacing" className="section info-section">
                    <h3>Información de la Canción</h3>
                    <p>
                        <strong>Nombre:</strong> {nombre.value}
                    </p>
                    <p>
                        <strong>Duración:</strong> {formatDuracion(duracion.value)} segundos
                    </p>
                    <p>
                        <strong>URL:</strong>{' '}
                        <a href={url.value} target="_blank" rel="noopener noreferrer">
                            {url.value}
                        </a>
                    </p>
                    <p>
                        <strong>Tipo de archivo:</strong>{' '}
                        {labelsMap[tipo.value as keyof typeof TipoArchivoEnum]}
                    </p>
                </VerticalLayout>

                <VerticalLayout theme="spacing" className="section edit-section">
                    <h2>Editar Canción</h2>
                    <TextField
                        label="Nombre"
                        value={nombre.value}
                        onValueChanged={(e) => (nombre.value = e.detail.value)}
                    />
                    <TextField
                        label="Duración"
                        value={duracion.value}
                        onValueChanged={(e) => (duracion.value = e.detail.value)}
                    />
                    <TextField
                        label="URL"
                        value={url.value}
                        onValueChanged={(e) => (url.value = e.detail.value)}
                    />
                    <ComboBox
                        label="Tipo de archivo"
                        items={tipos}
                        value={tipo.value}
                        onValueChanged={(e) =>
                            (tipo.value = e.detail.value as keyof typeof TipoArchivoEnum)
                        }
                        placeholder="Seleccione tipo de archivo"
                    />
                    <div className="buttons-wrapper">
                        <Button theme="primary" onClick={guardar} disabled={loading}>
                            Guardar
                        </Button>
                    </div>
                </VerticalLayout>
            </main>

        </>
    );
}
function formatDuracion(segundos: number | string): string {
    const s = typeof segundos === 'string' ? parseInt(segundos) : segundos;
    if (isNaN(s)) return '00:00';
    const minutos = Math.floor(s / 60);
    const segundosRestantes = s % 60;
    return `${String(minutos).padStart(2, '0')}:${String(segundosRestantes).padStart(2, '0')}`;
}
