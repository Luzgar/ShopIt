import {Injectable} from "@angular/core";
import {Http, Headers, Response} from "@angular/http";
import 'rxjs/add/operator/map';

export class ListServiceMock{


  saveList(itemsList: any, listName: number, userId: number){


  }

  addCollaborateur(idList: number, pseudo: string){

  }

  removeCollaborateur(idList, pseudo){

  }



  addArticle(listId: number, article: any){

  }

  setQuantity(listId: number, item: string, new_quantity: number){


  }

  removeArticle(listId: number, article:any){

  }

  setUserForArticle(userId: number, item : any, idList: number){

  }

  getAllCurrentTitleByUser(idUser : number){
    return new Promise((resolve) => {
        resolve({"51":"Friday list","52":"Saturday list"});
    });

  }


  getAllArchiveTitleByUser(idUser : number){
    return new Promise((resolve) => {
        resolve({"51":"Friday list","52":"Saturday list"});
    });
  }


  getListById(idList : number){
    return new Promise((resolve) => {
        resolve({"id": 0, "name": "hoy", "owner": { "id": 0,"login": "bla","pswd": "rebla", "pseudo": "rerebla"},"elements":[{"item": "butter","number": 1,"taken": false,"category": "fresh product"}],"contributors":[{"id": 0,"login": "bla","pswd": "rebla","pseudo": "rerebla"}]});
    });
  }

  archiveShopList(idList : number){

  }


  setArticleTaken(idList, itemName, isTaken) {

  }

  removeShopList(idList : number){

  }

  getArticlePriceByName(item: string){
    return new Promise((resolve) => {
        resolve(24);
    });
  }

}
