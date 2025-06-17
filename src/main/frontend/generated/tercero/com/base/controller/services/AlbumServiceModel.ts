import { _getPropertyModel as _getPropertyModel_1, makeObjectEmptyValueCreator as makeObjectEmptyValueCreator_1, ObjectModel as ObjectModel_1 } from "@vaadin/hilla-lit-form";
import AlbumModel_1 from "../../models/AlbumModel.js";
import type AlbumService_1 from "./AlbumService.js";
class AlbumServiceModel<T extends AlbumService_1 = AlbumService_1> extends ObjectModel_1<T> {
    static override createEmptyValue = makeObjectEmptyValueCreator_1(AlbumServiceModel);
    get album(): AlbumModel_1 {
        return this[_getPropertyModel_1]("album", (parent, key) => new AlbumModel_1(parent, key, true));
    }
}
export default AlbumServiceModel;
