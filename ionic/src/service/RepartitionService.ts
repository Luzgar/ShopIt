import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import 'rxjs/add/operator/map';

@Injectable()
export class RepartitionService{

  ipAdress: string;

  constructor(private http: Http){
    this.ipAdress = "192.168.43.102";
  }


  getCategoryRepartition(idList: number){
    return this.http.get("http://"+this.ipAdress+":8080/shopit-backend/repartition/docategoryrepartition/"+idList);
  }

  getCategoryPrice(idList: number){
    return this.http.get("http://"+this.ipAdress+":8080/shopit-backend/repartition/dopricerepartition/"+idList);
  }


}

