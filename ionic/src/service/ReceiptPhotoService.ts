import {Injectable} from "@angular/core";
import {Http, Headers} from "@angular/http";
import 'rxjs/add/operator/map';

@Injectable()
export class ReceiptPhotoService{


  ipAdress: string;

  constructor(private http: Http) {
    this.ipAdress = "192.168.43.102";

  }

  sendReceiptPhoto(photo, userId){
    let headers = new Headers();
    headers.append('Content-Type', 'text/plain');

    let body = {
      img: photo,
      author_id: userId,
    };
    //console.log(photo);

    return this.http.post('http://'+this.ipAdress+':8080/shopit-backend/price/processimage', JSON.stringify(body), {headers: headers}).subscribe();
  }

}
