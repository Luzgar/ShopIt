import {Injectable} from "@angular/core";
import {Http, Headers, Response} from "@angular/http";
import 'rxjs/add/operator/map';

@Injectable()
export class UserService{

  ipAdress: string;

  constructor(private http: Http){
    this.ipAdress = "192.168.43.102";
  }


  createAccount(login : string, pseudo : string, pwd : string){
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');

    let body = {
      login: login,
      password: pwd,
      pseudo: pseudo
    };



    this.http.post('http://'+this.ipAdress+':8080/shopit-backend/usercare/adduser', JSON.stringify(body), {headers: headers})
      .subscribe(data => {
        console.log(data);
    })
  }

  authenticate(login, password){
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');

    let body = {
      login: login,
      password: password
    };

    return this.http.post('http://'+this.ipAdress+':8080/shopit-backend/usercare/login', JSON.stringify(body), {headers: headers}).map((res:Response) => res.json());
  }

  sendDeviceToken(userId, token){
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');

    let body = {
      id: userId,
      token: token
    };

    return this.http.post('http://'+this.ipAdress+':8080/shopit-backend/usercare/settoken', JSON.stringify(body), {headers: headers}).map((res:Response) => res.json());
  }

  isLoginExisting(login){
    return this.http.get("http://"+this.ipAdress+":8080/shopit-backend/usercare/loginexists/"+login).map((res:Response) => res.json());
  }

  isPseudoExisting(pseudo){

    return this.http.get("http://"+this.ipAdress+":8080/shopit-backend/usercare/pseudoexists/"+pseudo).map((res:Response) => res.json());
  }

  setListLifeSpan(userId: number, duration: number){
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');

    let body = {
      id: userId,
      lifespan: duration
    };

    return this.http.post('http://'+this.ipAdress+':8080/shopit-backend/usercare/setlifespanlist', JSON.stringify(body), {headers: headers}).subscribe();
  }

  getListLifeSpan(userId: number){
    return this.http.get("http://"+this.ipAdress+":8080/shopit-backend/usercare/getlifespanlist/"+userId).map((res:Response) => res.json());
  }

  isRecurrencyActivated(userId){
    return this.http.get("http://"+this.ipAdress+":8080/shopit-backend/usercare/getuserdoreccurency/"+userId).map((res:Response) => res.json());
  }

  activateRecurrency(userId: number, isActivated: boolean) {
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');
    
    let body = {
      id: userId,
      reccurency: isActivated
    }

    this.http.post('http://'+this.ipAdress+':8080/shopit-backend/usercare/setuserdoreccurency', JSON.stringify(body), {headers: headers}).subscribe();
  }

}
