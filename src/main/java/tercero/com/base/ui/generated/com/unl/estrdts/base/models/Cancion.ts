import type TipoArchivoEnum_1 from "./TipoArchivoEnum";
interface Cancion {
    id?: number;
    nombre?: string;
    id_genero?: number;
    duracion?: number;
    url?: string;
    tipo?: TipoArchivoEnum_1;
    id_album?: number;
}
export default Cancion;
