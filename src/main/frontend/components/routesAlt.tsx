import { RouterConfigurationBuilder } from '@vaadin/hilla-file-router/runtime.js';
import Flow from 'Frontend/generated/flow/Flow';
import fileRoutes from 'Frontend/generated/file-routes';
import MainLayout from 'Frontend/views/@layout';
import CancionListView from "Frontend/views/cancion-list";
import CancionDetailView from "Frontend/views/cancion-detail-view";

export const { router, routes } = new RouterConfigurationBuilder()
    .withReactRoutes([
        {
            element: <MainLayout />,
            handle: { title: 'Principal' },
            children: [
                {
                    path: '/cancion-list',
                    element: <CancionListView />,
                    handle: { title: 'Canciones' },
                },
                {
                    path: '/cancion/:id',
                    element: <CancionDetailView />,
                    handle: { title: 'Detalle de Canción' },
                },
            ],
        },
    ])
    .withFallback(Flow) // para compatibilidad con vistas Flow si hay
    .protect()
    .build();