import {Injectable} from "@angular/core";
import {Http, Headers, Response} from "@angular/http";
import 'rxjs/add/operator/map';

@Injectable()
export class MedalService{

  ipAdress: string;

  constructor(private http: Http){
    this.ipAdress = "192.168.43.102";
  }


  getUserMedalExposed(userId: number){
    return this.http.get("http://"+this.ipAdress+":8080/shopit-backend/medal/getusermedalexposed/"+userId).map((res:Response) => res.json());
  }

  getUserMedalUnlocked(userId: number){
    return this.http.get("http://"+this.ipAdress+":8080/shopit-backend/medal/getusermedalsunlocked/"+userId).map((res:Response) => res.json());
  }

  setMedalExposed(userId: number, medalExposed: string){
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');

    let body = {
      id: userId,
      medal: "FISH"
    };

    return this.http.post('http://'+this.ipAdress+':8080/shopit-backend/medal/setusermedalexposed', JSON.stringify(body), {headers: headers}).subscribe();

  }

}
