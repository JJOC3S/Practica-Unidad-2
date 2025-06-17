import { EndpointRequestInit as EndpointRequestInit_1 } from "@vaadin/hilla-frontend";
import client_1 from "./connect-client.default.js";
import type Genero_1 from "./tercero/com/base/models/Genero.js";
import type TipoArchivoEnum_1 from "./tercero/com/base/models/TipoArchivoEnum.js";
async function createGenero_1(nombre: string | undefined, duracion: string | undefined, url: string | undefined, tipo: TipoArchivoEnum_1 | undefined, init?: EndpointRequestInit_1): Promise<void> { return client_1.call("GeneroService", "createGenero", { nombre, duracion, url, tipo }, init); }
async function getGenero_1(init?: EndpointRequestInit_1): Promise<Genero_1 | undefined> { return client_1.call("GeneroService", "getGenero", {}, init); }
async function getGeneroById_1(id: number | undefined, init?: EndpointRequestInit_1): Promise<Genero_1 | undefined> { return client_1.call("GeneroService", "getGeneroById", { id }, init); }
async function lisAllGenero_1(init?: EndpointRequestInit_1): Promise<Array<Genero_1 | undefined> | undefined> { return client_1.call("GeneroService", "lisAllGenero", {}, init); }
async function listTipoArchivo_1(init?: EndpointRequestInit_1): Promise<Array<string | undefined> | undefined> { return client_1.call("GeneroService", "listTipoArchivo", {}, init); }
async function setGenero_1(genero: Genero_1 | undefined, init?: EndpointRequestInit_1): Promise<void> { return client_1.call("GeneroService", "setGenero", { genero }, init); }
async function updateGenero_1(id: number | undefined, nombre: string | undefined, duracion: string | undefined, url: string | undefined, tipo: TipoArchivoEnum_1 | undefined, init?: EndpointRequestInit_1): Promise<void> { return client_1.call("GeneroService", "updateGenero", { id, nombre, duracion, url, tipo }, init); }
export { createGenero_1 as createGenero, getGenero_1 as getGenero, getGeneroById_1 as getGeneroById, lisAllGenero_1 as lisAllGenero, listTipoArchivo_1 as listTipoArchivo, setGenero_1 as setGenero, updateGenero_1 as updateGenero };
