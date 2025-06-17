import { Dialog, TextField, ComboBox, Button, VerticalLayout } from '@vaadin/react-components';
import { Notification } from '@vaadin/react-components/Notification';
import { useSignal } from '@vaadin/hilla-react-signals';
import { useEffect, useState } from 'react';

import TipoArchivoEnum from '../generated/com/unl/estrdts/base/models/TipoArchivoEnum';
import { CancionService } from '../generated/endpoints';
import handleError from './_ErrorHandler';

type Props = {
    id: number;
    onClose: () => void;
    onUpdated: () => void;
};

export default function CancionDetailView({ id, onClose, onUpdated }: Props) {
    const dialogOpened = useSignal(true);
    const [tipos, setTipos] = useState<string[]>([]);
    const [loading, setLoading] = useState(false);

    const nombre = useSignal('');
    const duracion = useSignal('');
    const url = useSignal('');
    const tipo = useSignal('');

    useEffect(() => {
        async function fetchData() {
            try {
                const cancion = await CancionService.getCancionById(id);
                if (!cancion) throw new Error('No encontrada');
                nombre.value = cancion.nombre ?? '';
                duracion.value = cancion.duracion?.toString() ?? '';
                url.value = cancion.url ?? '';
                tipo.value = cancion.tipo ?? '';
            } catch (e) {
                handleError(e);
            }
        }

        CancionService.listTipoArchivo()
            .then(result => setTipos((result || []).filter((x): x is string => x !== undefined)))
            .catch(console.error);

        fetchData();
    }, [id]);

    const guardar = async () => {
        if (!nombre.value || !duracion.value || !url.value || !tipo.value) {
            Notification.show('Todos los campos son obligatorios', { theme: 'error' });
            return;
        }

        try {
            setLoading(true);
            await CancionService.updateCancion(
                id,
                nombre.value,
                parseInt(duracion.value),
                url.value,
                tipo.value as TipoArchivoEnum
            );
            Notification.show('Canción actualizada', { theme: 'success' });
            dialogOpened.value = false;
            onUpdated();
            onClose();
        } catch (e) {
            handleError(e);
        } finally {
            setLoading(false);
        }
    };

    const cancelar = () => {
        dialogOpened.value = false;
        onClose();
    };

    return (
        <Dialog opened={dialogOpened.value} onOpenedChanged={(e) => !e.detail.value && cancelar()}>
            <VerticalLayout>
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
                    label="Tipo"
                    items={tipos}
                    value={tipo.value}
                    onValueChanged={(e) => (tipo.value = e.detail.value)}
                />
                <div style={{ display: 'flex', gap: '1rem' }}>
                    <Button theme="primary" onClick={guardar} disabled={loading}>
                        Guardar
                    </Button>
                    <Button theme="tertiary" onClick={cancelar} disabled={loading}>
                        Cancelar
                    </Button>
                </div>
            </VerticalLayout>
        </Dialog>
    );
}
