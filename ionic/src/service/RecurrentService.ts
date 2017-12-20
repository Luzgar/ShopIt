import {Injectable} from "@angular/core";
import {Http, Headers, Response} from "@angular/http";
import 'rxjs/add/operator/map';

@Injectable()
export class RecurrentService{

  ipAdress: string;

  constructor(private http: Http){
    this.ipAdress = "192.168.43.102";
  }

  getRecurrentArticle(userId, localList){
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');

    let body = {
      id: userId,
      lists: localList,
    };

    console.log(JSON.stringify(body));

    return this.http.post('http://'+this.ipAdress+':8080/shopit-backend/recurrency/highest_occurrences', JSON.stringify(body), {headers: headers}).map((res:Response) => res.json());
  }

}

