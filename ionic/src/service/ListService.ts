import {Injectable} from "@angular/core";
import {Http, Headers, Response} from "@angular/http";
import 'rxjs/add/operator/map';

@Injectable()
export class ListService{

  ipAdress: string;

  constructor(private http: Http){
    this.ipAdress = "192.168.43.102";
  }

  saveList(itemsList: any, listName: number, timestamp: number, userId: number){

    let headers = new Headers();
    headers.append('Content-Type', 'application/json');

    let body = {
      id: userId,
      name: listName,
      timestamp: timestamp,
      items: itemsList
    };

    console.log(JSON.stringify(body));

    return this.http.post('http://'+this.ipAdress+':8080/shopit-backend/shoplist/register', JSON.stringify(body), {headers: headers});
  }

  addCollaborateur(idList: number, pseudo: string){
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');

    let body = {
      id: idList,
      pseudo: pseudo
    };

    console.log(JSON.stringify(body));

    this.http.post('http://'+this.ipAdress+':8080/shopit-backend/sharing/addcontributor', JSON.stringify(body), {headers: headers}).subscribe(data=> {
      console.log(data);
    });

  }

  removeCollaborateur(idList, pseudo){
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');

    let body = {
      id: idList,
      pseudo: pseudo
    };

    console.log(JSON.stringify(body));

    this.http.post('http://'+this.ipAdress+':8080/shopit-backend/sharing/removecontributor', JSON.stringify(body), {headers: headers}).subscribe(data=> {
      console.log(data);
    });
  }



  addArticle(listId: number, article: any){
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');
    console.log(article);
    let body = {
      id: listId,
      item : {
        name_item: article.name_item,
        quantity: article.quantity,
        taken: article.taken,
        category: article.category,
        recipe_container: article.recipe_container
      }

    };
    console.log(body);
    console.log(JSON.stringify(body));

    return this.http.post('http://'+this.ipAdress+':8080/shopit-backend/shoplist/additem', JSON.stringify(body), {headers: headers});
  }

  setQuantity(listId: number, item: string, new_quantity: number){

    let headers = new Headers();
    headers.append('Content-Type', 'application/json');

    let body = {
      id: listId,
      item: item,
      new_quantity: new_quantity
    };

    console.log(JSON.stringify(body));

    this.http.post('http://'+this.ipAdress+':8080/shopit-backend/shoplist/setquantity', JSON.stringify(body), {headers: headers}).subscribe(data => {
      console.log(data);
    });
  }

  removeArticle(listId: number, article:any){
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');

    let body = {
      id: listId,
      item: article
    };

    console.log(JSON.stringify(body));

    this.http.post('http://'+this.ipAdress+':8080/shopit-backend/shoplist/removeitem', JSON.stringify(body), {headers: headers}).subscribe(data => {
      console.log(data);
    });
  }

  async setUserForArticle(userId: number, item : any, idList: number){
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');

    let body = {
      idshoplist: idList,
      iduser: userId,
      item: item.name_item
    };

    console.log(JSON.stringify(body));

    this.http.post('http://'+this.ipAdress+':8080/shopit-backend/repartition/setuserincharge', JSON.stringify(body), {headers: headers}).subscribe(data => {
      console.log(data);
    });
  }

  getAllCurrentTitleByUser(idUser : number){
    return this.http.get("http://"+this.ipAdress+":8080/shopit-backend/shoplist/allcurrenttitles/"+idUser)
      .map((res:Response) => res.json());
  }


  getAllArchiveTitleByUser(idUser : number){
    return this.http.get("http://"+this.ipAdress+":8080/shopit-backend/shoplist/allarchivetitles/"+idUser)
      .map((res:Response) => res.json());
  }


  getListById(idList : number){
    return this.http.get("http://"+this.ipAdress+":8080/shopit-backend/shoplist/listdetails/"+idList)
      .map((res:Response) => res.json());
  }

  archiveShopList(idList : number){
      let headers = new Headers();
      headers.append('Content-Type', 'application/json');

      let body = {
        id: idList
      };

      console.log(JSON.stringify(body));

      this.http.post('http://'+this.ipAdress+':8080/shopit-backend/shoplist/archiveshoplist', JSON.stringify(body), {headers: headers}).subscribe(data => {
        console.log(data);
      });
  }


  setArticleTaken(idList, itemName, isTaken) {
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');

    let body = {
      id: idList,
      item: itemName,
      istaken: isTaken
    };

    console.log(JSON.stringify(body));

    this.http.post('http://'+this.ipAdress+':8080/shopit-backend/shoplist/setTaken', JSON.stringify(body), {headers: headers}).subscribe(data => {
      console.log(data);
    });
  }

  removeShopList(idList : number){
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');

    let body = {
      id: idList
    };

    console.log(JSON.stringify(body));

    this.http.post('http://'+this.ipAdress+':8080/shopit-backend/shoplist/removeshoplist', JSON.stringify(body), {headers: headers}).subscribe(data => {
      console.log(data);
    });

  }

  getArticlePriceByName(item: string){
    return this.http.get("http://"+this.ipAdress+":8080/shopit-backend/price/getprice/"+item)
      .map((res:Response) => res.json());
  }

  setNeedHelp(idList: number, itemName: string, help : boolean){
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');

    let body = {
      id: idList,
      item: itemName,
      is_notfound: help
    };

    console.log(JSON.stringify(body));

    return this.http.post('http://'+this.ipAdress+':8080/shopit-backend/shoplist/setnotfound', JSON.stringify(body), {headers: headers});
  }

}
