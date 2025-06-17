import { _getPropertyModel as _getPropertyModel_1, makeObjectEmptyValueCreator as makeObjectEmptyValueCreator_1, ObjectModel as ObjectModel_1 } from "@vaadin/hilla-lit-form";
import GeneroModel_1 from "../../models/GeneroModel.js";
import type GeneroService_1 from "./GeneroService.js";
class GeneroServiceModel<T extends GeneroService_1 = GeneroService_1> extends ObjectModel_1<T> {
    static override createEmptyValue = makeObjectEmptyValueCreator_1(GeneroServiceModel);
    get genero(): GeneroModel_1 {
        return this[_getPropertyModel_1]("genero", (parent, key) => new GeneroModel_1(parent, key, true));
    }
}
export default GeneroServiceModel;
