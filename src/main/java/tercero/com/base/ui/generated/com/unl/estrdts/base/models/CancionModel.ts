import { _getPropertyModel as _getPropertyModel_1, makeObjectEmptyValueCreator as makeObjectEmptyValueCreator_1, NumberModel as NumberModel_1, ObjectModel as ObjectModel_1, StringModel as StringModel_1 } from "@vaadin/hilla-lit-form";
import type Cancion_1 from "./Cancion";
import TipoArchivoEnumModel_1 from "./TipoArchivoEnumModel";
class CancionModel<T extends Cancion_1 = Cancion_1> extends ObjectModel_1<T> {
    static override createEmptyValue = makeObjectEmptyValueCreator_1(CancionModel);
    get id(): NumberModel_1 {
        return this[_getPropertyModel_1]("id", (parent, key) => new NumberModel_1(parent, key, true, { meta: { javaType: "java.lang.Integer" } }));
    }
    get nombre(): StringModel_1 {
        return this[_getPropertyModel_1]("nombre", (parent, key) => new StringModel_1(parent, key, true, { meta: { javaType: "java.lang.String" } }));
    }
    get id_genero(): NumberModel_1 {
        return this[_getPropertyModel_1]("id_genero", (parent, key) => new NumberModel_1(parent, key, true, { meta: { javaType: "java.lang.Integer" } }));
    }
    get duracion(): NumberModel_1 {
        return this[_getPropertyModel_1]("duracion", (parent, key) => new NumberModel_1(parent, key, true, { meta: { javaType: "java.lang.Integer" } }));
    }
    get url(): StringModel_1 {
        return this[_getPropertyModel_1]("url", (parent, key) => new StringModel_1(parent, key, true, { meta: { javaType: "java.lang.String" } }));
    }
    get tipo(): TipoArchivoEnumModel_1 {
        return this[_getPropertyModel_1]("tipo", (parent, key) => new TipoArchivoEnumModel_1(parent, key, true));
    }
    get id_album(): NumberModel_1 {
        return this[_getPropertyModel_1]("id_album", (parent, key) => new NumberModel_1(parent, key, true, { meta: { javaType: "java.lang.Integer" } }));
    }
}
export default CancionModel;
