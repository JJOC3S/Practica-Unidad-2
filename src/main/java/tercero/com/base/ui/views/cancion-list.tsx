import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { Button, ComboBox, DatePicker, Dialog, Grid, GridColumn, GridItemModel, TextField, VerticalLayout } from '@vaadin/react-components';
import { Notification } from '@vaadin/react-components/Notification';

import { useSignal } from '@vaadin/hilla-react-signals';
import handleError from 'Frontend/views/_ErrorHandler';
import { Group, ViewToolbar } from 'Frontend/components/ViewToolbar';

import { useDataProvider } from '@vaadin/hilla-react-crud';
import { CancionService } from "../generated/endpoints";


import Cancion from "../generated/com/unl/estrdts/base/models/Cancion";
import TipoArchivoEnum from "../generated/com/unl/estrdts/base/models/TipoArchivoEnum";
import { useEffect, useState } from 'react';

export const config: ViewConfig = {
  title: 'Cancion',
  menu: {
    icon: 'vaadin:clipboard-check',
    order: 3,
    title: 'Cancion',
  },
};

type CancionEntryFormProps = {
  onCancionCreated?: () => void;
};
// crear Cancion
function CancionEntryForm(props: CancionEntryFormProps) {
  const dialogOpened = useSignal(false);
  const [tipos, setTipos] = useState<String[]>([]);


  const open = () => {
    dialogOpened.value = true;
  };

  const close = () => {
    dialogOpened.value = false;
  };

  const nombre = useSignal('');
  const duracion = useSignal('');
  const url = useSignal('');
  const tipo = useSignal('');


  const createCancion = async () => {
    try {
      if (nombre.value.trim().length > 0 && duracion.value.trim().length > 0 && url.value.trim().length > 0 && tipo.value.trim().length > 0) {
        const tipoEnumValue = TipoArchivoEnum[tipo.value as keyof typeof TipoArchivoEnum];
        await CancionService.createCancion(nombre.value, duracion.value, tipoEnumValue);
        if (props.onCancionCreated) {
          props.onCancionCreated();
        }
        nombre.value = '';
        duracion.value = '';
        tipo.value = '';
        dialogOpened.value = false;
        Notification.show('Cancion creada exitosamente', { duration: 5000, position: 'bottom-end', theme: 'success' });
      } else {
        Notification.show('No se pudo crear, faltan datos', { duration: 5000, position: 'top-center', theme: 'error' });
      }
    } catch (error) {
      console.log(error);
      handleError(error);
    }
  };

  useEffect(() => {
    CancionService.listTipoArchivo()
      .then((result) => setTipos((result || []).filter((tipo): tipo is string => tipo !== undefined)))
      .catch(console.error);
  }, []);
 
  return (
    <>
      <Dialog
        aria-label="Registrar Banda"
        draggable
        modeless
        opened={dialogOpened.value}
        onOpenedChanged={(event) => {
          dialogOpened.value = event.detail.value;
        }}
        header={
          <h2
            className="draggable"
            style={{
              flex: 1,
              cursor: 'move',
              margin: 0,
              fontSize: '1.5em',
              fontWeight: 'bold',
              padding: 'var(--lumo-space-m) 0',
            }}
          >
            Registrar Cancion
          </h2>
        }
        footerRenderer={() => (
          <>
            <Button onClick={close}>Cancelar</Button>
            <Button theme="primary" onClick={createCancion}>
              Registrar
            </Button>
          </>
        )}
      >
        <VerticalLayout
          theme="spacing"
          style={{ width: '300px', maxWidth: '100%', alignItems: 'stretch' }}
        >
          <VerticalLayout style={{ alignItems: 'stretch' }}>
            <TextField label="Nombre"
              placeholder='Ingrese el nombre de la cancion'
              aria-label='Ingrese el nombre de la cancion'
              value={nombre.value}
              onValueChanged={(evt) => (nombre.value = evt.detail.value)}
            />
            <TextField label="Duracion"
              placeholder='Ingrese la duracion de la cancion'
              aria-label='Ingrese la duracion de la cancion'
              value={duracion.value}
              onValueChanged={(evt) => (duracion.value = evt.detail.value)}
            />
            <TextField label="Url"
              placeholder='Ingrese la url de la cancion'
              aria-label='Ingrese la url de la cancion'
              value={url.value}
              onValueChanged={(evt) => (url.value = evt.detail.value)}
            />
            <ComboBox
              label="Tipo de archivo"
              items={tipos}
              value={tipo.value}
              onValueChanged={(e) => (tipo.value = e.detail.value)}
              placeholder="Seleccione tipo de archivo"
            />
          </VerticalLayout>
        </VerticalLayout>
      </Dialog>
      <Button onClick={open}>Registrar</Button>

    </>
  );
}

//Actualizar Banda


const dateFormatter = new Intl.DateTimeFormat(undefined, {
  dateStyle: 'medium',
});

function link({ item }: { item: Cancion }) {
  return (
        
    <span>
       <Button >
        Eliminar
      </Button>
      <Button>
        Editar
      </Button>
    </span>
  );
}

function index({ model }: { model: GridItemModel<Cancion> }) {
  return (
    <span>
      {model.index + 1}
    </span>
  );
}



export default function CancionLisView() {
  const dataProvider = useDataProvider<Cancion>({
    list: async () => {
      const result = await CancionService.lisAllCancion();
      return (result ?? []).filter((c): c is Cancion => c !== undefined);
    },
  });

  return (
      <main className="w-full h-full flex flex-col box-border gap-s p-m">
        <ViewToolbar title="Canciones">
          <Group>
            <CancionEntryForm onCancionCreated={() => dataProvider.refresh()} />
          </Group>
        </ViewToolbar>
        <Grid dataProvider={dataProvider.dataProvider}>
          <GridColumn path="index" header="Index" renderer={index} />
          <GridColumn path="nombre" header="Nombre" />
          <GridColumn path="genero" header="Genero" />
          <GridColumn path="album" header="Album" />
          <GridColumn path="duracion" header="Duracion" />
          <GridColumn path="url" header="Url" />
          <GridColumn path="tipo" header="Tipo de Archivo" />
          <GridColumn header="Acciones" renderer={link} />
        </Grid>
      </main>
  );
}

