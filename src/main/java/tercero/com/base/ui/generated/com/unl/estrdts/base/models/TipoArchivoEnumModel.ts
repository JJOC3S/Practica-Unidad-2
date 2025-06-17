import { _enum as _enum_1, EnumModel as EnumModel_1, makeEnumEmptyValueCreator as makeEnumEmptyValueCreator_1 } from "@vaadin/hilla-lit-form";
import TipoArchivoEnum_1 from "./TipoArchivoEnum";
class TipoArchivoEnumModel extends EnumModel_1<typeof TipoArchivoEnum_1> {
    static override createEmptyValue = makeEnumEmptyValueCreator_1(TipoArchivoEnumModel);
    readonly [_enum_1] = TipoArchivoEnum_1;
}
export default TipoArchivoEnumModel;
