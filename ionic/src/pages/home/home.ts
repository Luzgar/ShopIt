import {Component} from '@angular/core';
import {Tabs} from 'ionic-angular';
import 'rxjs/add/operator/map';
import {ListService} from "../../service/ListService";
import 'rxjs/add/operator/catch';
import {UserService} from "../../service/UserService";
import {CurrentList} from "./currentList/currentList";
import {ArchivedList} from "./archiveList/archiveList";

@Component({
  selector: 'page-home',
  templateUrl: 'home.html',
  providers : [ListService, UserService,Tabs]
})
export class HomePage {



  tab1Root: any = CurrentList;
  tab2Root: any = ArchivedList;

  constructor() {

  }

}
