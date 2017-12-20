import { Observable } from 'rxjs/Observable';
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
  selector: 'currentList',
  templateUrl: 'currentList.html',
  providers: [ListService, UserService]
})

export class CurrentList {

  listes: Array<{title: string, listId: number}>;
  listId: number;
  userId : number;

  subscription: any;

  constructor(private realTimeService: RealTimeService, private navCtrl: NavController, private alertCtrl: AlertController, private storage: Storage, private http: Http, private listService : ListService, private userService: UserService) {
    
  }

  ionViewDidEnter(){
    this.listes = [];
    this.storage.get("userId").then((data) => {
      if(data != null){
        console.log("USERID : "+ data);
        this.userId = data;
      }
      else {
        console.log("NOT CONNECTED");
      }
      this.loadList();
    });

    this.loadList();

    this.subscription = this.realTimeService.getNotifyTitleChange().subscribe(message => {
      this.loadList();
    });
  }

  loadList(){
    var arrayCurrent = [];
    this.storage.get('listesName').then((data) => {
      if(data != null)
        arrayCurrent = data;
      this.listService.getAllCurrentTitleByUser(this.userId).subscribe(
        res => {
          var keys = Object.keys(res);
          for(var i = 0; i < keys.length; ++i ){
            arrayCurrent.push({title: res[keys[i]], listId: Number(keys[i])});
          }
          this.listes = arrayCurrent;
        },
        err => {
          this.listes = arrayCurrent;
        }
      );
    });
  }

  listeClick(liste){
    this.navCtrl.push(ShopListPage,{listId: liste.listId, nameShopList: liste.title, firstTime: false});
  }

  addList(title: string, id: number){
    return this.storage.get('listesName').then((res) => {
      let array = [];
      if(res != null)
      {
        array = res;

        array.push({title: title, listId: id});
        this.storage.set('listesName', array);
      }
      else
      {
        array.push({title: title, listId: id});
        this.storage.set('listesName', array);
      }
      this.listes.push({title: title, listId: id});
      this.navCtrl.push(ShopListPage,{listId: id, nameShopList: title, firstTime: true});
    });

    
  }

  async deleteCurrentList(liste){
    if(liste.listId<0){
      var array = [];
      this.storage.get('listesName').then((data) => {
        if(data !=null){
          array = data;
          array.splice(array.indexOf(liste), 1);
          this.listes.splice(this.listes.indexOf(liste),1);
          this.storage.set('listesName',array);
        }
      });

    }
    else{
      this.listes.splice(this.listes.indexOf(liste),1);
      this.listService.removeShopList(liste.listId);
    }
    
  }

  addListAlert() {
    let prompt = this.alertCtrl.create({
      title: 'Nouvelle liste',
      message: "Rentrer ici le titre de votre nouvelle liste",
      inputs: [
        {
          name: 'title',
          placeholder: 'Title'
        },
      ],
      buttons: [
        {
          text: 'Cancel',
          handler: data => {
            //console.log('Cancel clicked');
          }
        },
        {
          text: 'Save',
          handler: data => {
            var id = Date.now()*-1;
            this.addList(data.title, id);
          }
        }
      ]
    });
    prompt.present();
  }

  async archiveShopList(liste){
    console.log(liste);
    if(liste.listId<0){
      this.storage.get('listesArchives').then((res) => {
        let array = [];
        if(res != null)
        {
          array = res;
          array.push(liste);
        }
        else
        {
          array.push(liste);
        }

        this.storage.set('listesArchives', array);
      });

    }
    else{
      this.listService.archiveShopList(liste.listId);
    }
    this.storage.get('listesName').then((data) => {
      var res = data;
      res.splice(res.indexOf(liste),1);
      this.storage.set('listesName', res);
    });

    this.listes.splice(this.listes.indexOf(liste), 1);
  }

}
