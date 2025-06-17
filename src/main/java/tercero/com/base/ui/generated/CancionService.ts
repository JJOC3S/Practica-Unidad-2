import {EndpointRequestInit, EndpointRequestInit as EndpointRequestInit_1} from "@vaadin/hilla-frontend";
import type Cancion_1 from "./com/unl/estrdts/base/models/Cancion";
import  TipoArchivoEnum_1 from "./com/unl/estrdts/base/models/TipoArchivoEnum";
import client_1 from "./connect-client.default";
import TipoArchivoEnum from "./com/unl/estrdts/base/models/TipoArchivoEnum";

export async function getCancionById(id: number, init?: EndpointRequestInit_1): Promise<Cancion_1 | undefined> {
    return client_1.call("CancionService", "getCancionById", { id }, init);
}
async function createCancion_1(nombre: string | undefined, duracion: string | undefined, tipo: TipoArchivoEnum_1 | undefined, init?: EndpointRequestInit_1): Promise<void> { return client_1.call("CancionService", "createCancion", { nombre, duracion, tipo }, init); }
async function lisAllCancion_1(init?: EndpointRequestInit_1): Promise<Cancion_1[]> {
    const result = await client_1.call("CancionService", "lisAllCancion", {}, init);
    return result ?? [];
}
async function listTipoArchivo_1(init?: EndpointRequestInit_1): Promise<Array<string | undefined> | undefined> {
    return client_1.call("CancionService", "listTipoArchivo", {}, init);
}
async function updateCancion_1(id: number | undefined, nombre: string | undefined, duracion: number, url: string | undefined, tipo: TipoArchivoEnum | undefined, init?: EndpointRequestInit): Promise<void> { return client_1.call("CancionService", "updateCancion", { id, nombre, duracion, url, tipo }, init); }
export { createCancion_1 as createCancion, lisAllCancion_1 as lisAllCancion, listTipoArchivo_1 as listTipoArchivo, updateCancion_1 as updateCancion };
