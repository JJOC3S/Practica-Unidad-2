import { EndpointRequestInit as EndpointRequestInit_1 } from "@vaadin/hilla-frontend";
import client_1 from "./connect-client.default.js";
import type LinkedList_1 from "./tercero/com/base/controller/DataStruc/List/LinkedList.js";
import type AlbumService_1 from "./tercero/com/base/controller/services/AlbumService.js";
import type GeneroService_1 from "./tercero/com/base/controller/services/GeneroService.js";
import type Cancion_1 from "./tercero/com/base/models/Cancion.js";
import type TipoArchivoEnum_1 from "./tercero/com/base/models/TipoArchivoEnum.js";
async function createCancion_1(nombre: string | undefined, duracion: string | undefined, tipo: string | undefined, idAlbum: string | undefined, idGenero: string | undefined, init?: EndpointRequestInit_1): Promise<void> { return client_1.call("CancionService", "createCancion", { nombre, duracion, tipo, idAlbum, idGenero }, init); }
async function getCancion_1(init?: EndpointRequestInit_1): Promise<Cancion_1 | undefined> { return client_1.call("CancionService", "getCancion", {}, init); }
async function getCancionById_1(id: number | undefined, init?: EndpointRequestInit_1): Promise<Cancion_1 | undefined> { return client_1.call("CancionService", "getCancionById", { id }, init); }
async function getTipoArchivoEnum_1(init?: EndpointRequestInit_1): Promise<Array<TipoArchivoEnum_1 | undefined> | undefined> { return client_1.call("CancionService", "getTipoArchivoEnum", {}, init); }
async function listAllCancion_1(init?: EndpointRequestInit_1): Promise<Array<Cancion_1 | undefined> | undefined> { return client_1.call("CancionService", "listAllCancion", {}, init); }
async function listAllCancions_1(init?: EndpointRequestInit_1): Promise<LinkedList_1<Cancion_1 | undefined> | undefined> { return client_1.call("CancionService", "listAllCancions", {}, init); }
async function listCanciones_1(init?: EndpointRequestInit_1): Promise<Array<Record<string, string | undefined> | undefined> | undefined> { return client_1.call("CancionService", "listCanciones", {}, init); }
async function main_1(args: Array<string | undefined> | undefined, init?: EndpointRequestInit_1): Promise<void> { return client_1.call("CancionService", "main", { args }, init); }
async function searchCancion_1(criterio: string | undefined, valor: string | undefined, metodo: string | undefined, init?: EndpointRequestInit_1): Promise<Array<Record<string, string | undefined> | undefined> | undefined> { return client_1.call("CancionService", "searchCancion", { criterio, valor, metodo }, init); }
async function setCancion_1(cancion: Cancion_1 | undefined, init?: EndpointRequestInit_1): Promise<void> { return client_1.call("CancionService", "setCancion", { cancion }, init); }
async function sortCancion_1(criterio: string | undefined, orden: number | undefined, metodo: string | undefined, init?: EndpointRequestInit_1): Promise<Array<Record<string, string | undefined> | undefined> | undefined> { return client_1.call("CancionService", "sortCancion", { criterio, orden, metodo }, init); }
async function toMap_1(cancion: Cancion_1 | undefined, as: AlbumService_1 | undefined, gs: GeneroService_1 | undefined, init?: EndpointRequestInit_1): Promise<Record<string, string | undefined> | undefined> { return client_1.call("CancionService", "toMap", { cancion, as, gs }, init); }
async function updateCancion_1(id: number | undefined, nombre: string | undefined, duracion: string | undefined, init?: EndpointRequestInit_1): Promise<void> { return client_1.call("CancionService", "updateCancion", { id, nombre, duracion }, init); }
export { createCancion_1 as createCancion, getCancion_1 as getCancion, getCancionById_1 as getCancionById, getTipoArchivoEnum_1 as getTipoArchivoEnum, listAllCancion_1 as listAllCancion, listAllCancions_1 as listAllCancions, listCanciones_1 as listCanciones, main_1 as main, searchCancion_1 as searchCancion, setCancion_1 as setCancion, sortCancion_1 as sortCancion, toMap_1 as toMap, updateCancion_1 as updateCancion };
