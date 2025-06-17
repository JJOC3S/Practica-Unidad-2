import { EndpointRequestInit as EndpointRequestInit_1 } from "@vaadin/hilla-frontend";
import client_1 from "./connect-client.default.js";
import type Album_1 from "./tercero/com/base/models/Album.js";
import type TipoArchivoEnum_1 from "./tercero/com/base/models/TipoArchivoEnum.js";
async function createAlbum_1(nombre: string | undefined, duracion: string | undefined, url: string | undefined, tipo: TipoArchivoEnum_1 | undefined, init?: EndpointRequestInit_1): Promise<void> { return client_1.call("AlbumService", "createAlbum", { nombre, duracion, url, tipo }, init); }
async function getAlbum_1(init?: EndpointRequestInit_1): Promise<Album_1 | undefined> { return client_1.call("AlbumService", "getAlbum", {}, init); }
async function getAlbumById_1(id: number | undefined, init?: EndpointRequestInit_1): Promise<Album_1 | undefined> { return client_1.call("AlbumService", "getAlbumById", { id }, init); }
async function lisAllAlbum_1(init?: EndpointRequestInit_1): Promise<Array<Album_1 | undefined> | undefined> { return client_1.call("AlbumService", "lisAllAlbum", {}, init); }
async function listTipoArchivo_1(init?: EndpointRequestInit_1): Promise<Array<string | undefined> | undefined> { return client_1.call("AlbumService", "listTipoArchivo", {}, init); }
async function setAlbum_1(album: Album_1 | undefined, init?: EndpointRequestInit_1): Promise<void> { return client_1.call("AlbumService", "setAlbum", { album }, init); }
async function updateAlbum_1(id: number | undefined, nombre: string | undefined, duracion: string | undefined, url: string | undefined, tipo: TipoArchivoEnum_1 | undefined, init?: EndpointRequestInit_1): Promise<void> { return client_1.call("AlbumService", "updateAlbum", { id, nombre, duracion, url, tipo }, init); }
export { createAlbum_1 as createAlbum, getAlbum_1 as getAlbum, getAlbumById_1 as getAlbumById, lisAllAlbum_1 as lisAllAlbum, listTipoArchivo_1 as listTipoArchivo, setAlbum_1 as setAlbum, updateAlbum_1 as updateAlbum };
