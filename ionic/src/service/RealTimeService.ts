import {Injectable} from "@angular/core";
import 'rxjs/add/operator/map';
import {Observable} from "rxjs/Observable";
import {Subject} from "rxjs/Subject";

@Injectable()
export class RealTimeService{

  private subject = new Subject<any>();
  private titleChange = new Subject<any>();
  private listChange = new Subject<any>();
  private connected = new Subject<boolean>();
  private internetConnection = new Subject<boolean>();

  notifyConnection(message: boolean){
    this.connected.next(message);
  }

  getNotifyConnection(): Observable<any> {
    return this.connected.asObservable();
  }

  notifyInternetConnection(message: boolean){
    this.internetConnection.next(message);
  }

  getInternetConnection(): Observable<any> {
    return this.internetConnection.asObservable();
  }

  notifyTitleChange(message: string){
    this.titleChange.next({text: message});
  }

  getNotifyTitleChange(): Observable<any> {
    return this.titleChange.asObservable();
  }

  notifyListChange(message: string){
    this.listChange.next({text: message});
  }

  getNotifyListChange(): Observable<any> {
    return this.listChange.asObservable();
  }

  sendMessage(message: string) {
    this.subject.next({ text: message });
  }

  clearMessage() {
    this.subject.next();
  }

  getMessage(): Observable<any> {
    return this.subject.asObservable();
  }


}
