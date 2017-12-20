import {Component} from "@angular/core";
import {RealTimeService} from "../../../service/RealTimeService";
import {AlertController, NavController} from "ionic-angular";
import {Http} from "@angular/http";
import {UserService} from "../../../service/UserService";
import {ListService} from "../../../service/ListService";
import { Storage } from '@ionic/storage';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import {ShopListPage} from "../../shopList/shopList";

@Component({
  selector: 'archiveList',
  templateUrl: 'archiveList.html',
  providers: [ListService, UserService]
})

export class ArchivedList {

  listes: Array<{ title: string, listId: number }>;
  listId: number;
  userId: number;

  subscription: any;

  constructor(private realTimeService: RealTimeService, private navCtrl: NavController, private alertCtrl: AlertController, private storage: Storage, private http: Http, private listService: ListService, private userService: UserService) {



  }

  ionViewDidEnter(){
    this.listes = [];
    this.storage.get("userId").then((data) => {
      if(data != null){
        this.userId = data;
      }
      else {
        console.log("NOT CONNECTED");
      }
      this.loadList();
    });
  }

  loadList(){
    var array = [];
    this.storage.get('listesArchives').then((data) => {
      if(data !=null){
        array = data;
      }
      this.listService.getAllArchiveTitleByUser(this.userId).subscribe(
        res => {
          var keys = Object.keys(res);
          for(var i = 0; i < keys.length; ++i ){
            array.push({title: res[keys[i]], listId: Number(keys[i])});
          }
          this.listes = array;
        },
        err => {
          this.listes = array;
        }
      );

    });
  }

  async deleteArchivedList(liste){
    if(liste.listId<0){
      var array = [];
      this.storage.get('listesArchives').then((data) => {
        if(data !=null){
          array = data;
          array.splice(array.indexOf(liste),1);
          this.storage.set('listesArchives',array);
        }
      });
    }
    else{
      this.listService.removeShopList(liste.listId);
    }
    this.listes.splice(this.listes.indexOf(liste),1);
  }

  listeClick(liste){
    this.navCtrl.push(ShopListPage,{listId: liste.listId, nameShopList: liste.title, firstTime: false});
  }
}
