import { UserService } from './../../service/UserService';
import { RecurrentService } from './../../service/RecurrentService';
import {Component, Pipe, PipeTransform} from '@angular/core';
import {AlertController, NavController, NavParams} from 'ionic-angular';
import {CategoryService} from "../../service/CategoryService";
import { Storage } from '@ionic/storage';
import {ListService} from "../../service/ListService";
import {InviteFriend} from "./inviteFriendModal/inviteFriendModal";
import {RepartitionService} from "../../service/RepartitionService";
import {RealTimeService} from "../../service/RealTimeService";
import { DomSanitizer } from '@angular/platform-browser';
import {ColorService} from "../../service/ColorService";
import { Network } from '@ionic-native/network';
import {MedalService} from "../../service/MedalService";


@Component({
  selector: 'page-shopList',
  templateUrl: 'shopList.html',
  providers: [CategoryService, ListService, RepartitionService, ColorService, RecurrentService, MedalService, UserService]
})
export class ShopListPage {

  color: string;
  colorList: any;
  listId: number;
  ownerPseudo: number;

  itemList: Array<{name_item: string, quantity: number, category: string, taken: boolean, idOwner: number, isOwned: boolean, notFound: boolean, color: string, price: number, recipe_container: string}>;
  colorPerContributor: any;

  dividedList: any;
  nameShopList: any;
  myInput: string;

  userId: number;
  userPseudo: string;

  contributors : any;

  subscription: any;
  internetSubscription: any;
  totalPrice: number;
  firstTime: boolean;
  constructor(public userService: UserService, public medalService: MedalService, public recurrentService: RecurrentService, public network : Network, public colorService: ColorService, private _sanitizer: DomSanitizer, public realTimeService : RealTimeService, public navCtrl: NavController, public navParams: NavParams, public alertCtrl : AlertController, private categoryService: CategoryService, private storage: Storage, private listService : ListService, private repartitionService: RepartitionService) {
    this.nameShopList = this.navParams.get('nameShopList');
    this.firstTime = this.navParams.get('firstTime');

    this.colorList = colorService.getAllColorName();
    this.listId = this.navParams.get('listId');

    if(this.listId < 0)
      this.color = "local-list";

    this.itemList = [];

    this.storage.get("userId").then((data) => {
      if(data != null){
        this.userId = data;
      }
      else {
        this.userId = -1;
      }
      if(this.firstTime && this.network.type != "none"){
        this.userService.isRecurrencyActivated(this.userId).subscribe((res) => {
          if(res)
            this.getAllLocalListName();
        });
      }
    });


    this.totalPrice = 0;

  }

  ionViewDidEnter(){

    if(this.listId < 0){

      this.storage.get(this.listId.toString()).then((data) => {
        /*if(this.network.type != "none"){
          if(data != null)
            this.calculateLocalList(data, 0, []);
          else {
            this.itemList = [];
            this.divideList();
          }
        }
        else{*/
          this.itemList = data;
          if(this.itemList == null)
            this.itemList = [];
          this.divideList();
      //}

      });
      this.internetSubscription = this.realTimeService.getInternetConnection().subscribe(message => {
        if(message){
          this.calculateLocalList(this.itemList, 0, []);
        }
      })
    }
    else {
      this.subscription = this.realTimeService.getNotifyListChange().subscribe(message => {
        this.loadList();
      });

      this.loadList();
    }
  }

  calculateLocalList(list, i, new_list){

    if(i < list.length){
      this.categoryService.getArticle(list[i].name_item.toString()).subscribe(result => {
        var category;
        if(result.count > 0){
          category = result.products[0].categories.split(',')[0];
        }
        else {
          category = "Autres";
        }
        this.listService.getArticlePriceByName(list[i].name_item).subscribe((res)=> {
          new_list.push({name_item: list[i].name_item, quantity: list[i].quantity, category: category, taken: list[i].taken, idOwner: list[i].idOwner, isOwned: list[i].isOwned, color: list[i].color, needHelp: false, price: res, recipe_container:  list[i].recipe_container });
          i++;
          this.calculateLocalList(list, i, new_list);
        });
      });
    }
    else {
      this.storage.set(this.listId.toString(), new_list);
      this.itemList = new_list;
      this.divideList();
    }
  }

  attributeColorPerContributor(contributors, owner){
    this.colorPerContributor = {};
    var i = 0;
    if(owner.id == this.userId){
      this.userPseudo = owner.pseudo;
      this.color = this.colorList[i];
    }
    this.colorPerContributor[owner.id] = {pseudo: owner.pseudo, color: this.colorList[i]};
    for(; i < contributors.length; ++i){
      if(contributors[i].id == this.userId){
        this.userPseudo = contributors[i].pseudo;
        this.color = this.colorList[i+1];
      }
      this.colorPerContributor[contributors[i].id] = {pseudo: contributors[i].pseudo, color: this.colorList[i+1]};
    }
  }

  isUserExpulsed(contributors, owner): boolean{
    var isExpulsed = true;
    if(owner.id == this.userId)
      isExpulsed = false;
    for(var i = 0; i < contributors.length; ++i){
      if(contributors[i].id == this.userId)
        isExpulsed = false;
    }
    return isExpulsed;
  }

  getAllLocalListName(){
    this.storage.get("listesName").then((data) => {
      this.getAllLocalList([], data, 0);
    });
  }

  getAllLocalList(allList, listesName, index){
    if(listesName.length > index){
      var listId = listesName[index].listId;
      this.storage.get(listId.toString()).then((data) => {
        if(data != null){
          var tmp = [];
          for(var i = 0; i < data.length; ++i){
            tmp.push({item : data[i].name_item, quantity: data[i].quantity});
          }
          allList.push(tmp);
        }
        index++;
        this.getAllLocalList(allList, listesName, index);
      });
    }
    else {
      this.recurrentService.getRecurrentArticle(this.userId, allList).subscribe((res) => {
        console.log(res);
        var result = [];
        for(var i = 0; i < res.map.recurrent_items.myArrayList.length; ++i){
          result.push(res.map.recurrent_items.myArrayList[i].map);
        }
        this.proposeRecurrentList(result);
      });
    }
  }

  getCategoryAndAddList(list, index){
    if(list.length > index){
      this.categoryService.getArticle(list[index].name_item).subscribe(result => {

        var category;
        if(result.count > 0)
          category = result.products[0].categories.split(',')[0];
        else
          category = "Autres";
        list[index].category = category;
        index++;
        this.getCategoryAndAddList(list, index);
      });
    }
    else {
      this.itemList = list;
      this.storage.set(this.listId.toString(), list);
      this.divideList();
    }
  }

  proposeRecurrentList(items){
    var input = [];
    for(var i = 0; i < items.length; ++i){
      input.push({type: 'checkbox', label: items[i].item, value: items[i]});
    }

    if(input.length > 0){
      var title = 'Articles récurrents';
      var buttons = [
        {
          text: 'Cancel',
          handler: data => {
            //console.log('Cancel clicked');
          }
        },
        {
          text: 'Ajouter',
          handler: data => {
            var list = [];
            for(var i = 0; i < data.length; ++i){
              list.push({name_item: data[i].item, quantity: 1, category: data[i].category, taken: false, idOwner: -1, isOwned: false, notFound: false, color: "", price: data[i].price, recipe_container: data[i].recipe_container});
            }
            this.getCategoryAndAddList(list, 0);

          }
        }
      ];
      let prompt = this.alertCtrl.create({
        title: title,
        inputs : input,
        enableBackdropDismiss: false,
        buttons: buttons
      });
      prompt.present();
    }


  }

  loadList(){
    this.listService.getListById(this.listId).subscribe(data => {
      console.log(data);
      if(this.isUserExpulsed(data.contributors, data.owner)){
        this.expulseAlert();
      }
      else {
        this.contributors = data.contributors;
        this.attributeColorPerContributor(data.contributors, data.owner);
        this.ownerPseudo = data.owner.pseudo;
        this.itemList = [];
        for(var i = 0; i < data.elements.length; ++i){
          if(data.elements[i].userInCharge){
            this.itemList.push({name_item: data.elements[i].item, category: data.elements[i].category, quantity: data.elements[i].number, taken: data.elements[i].taken, idOwner: data.elements[i].userInCharge.id, isOwned: true, color: this.colorPerContributor[data.elements[i].userInCharge.id].color, notFound: data.elements[i].notFound, price: data.elements[i].price, recipe_container : data.elements[i].recipeContainer });
          }
          else {
            this.itemList.push({name_item: data.elements[i].item, category: data.elements[i].category, quantity: data.elements[i].number, taken: data.elements[i].taken, idOwner: -1, isOwned: false, color: "", notFound: data.elements[i].notFound, price: data.elements[i].price, recipe_container: data.elements[i].recipeContainer});
          }
        }
        console.log(this.itemList);
        this.divideList();
      }

    })
  }

  expulseAlert(){
    let alert = this.alertCtrl.create({
      title: 'Vous ne faites plus parti de la liste !',
      enableBackdropDismiss: false,
      buttons: [
        {
          text: 'Ok',
          handler: data => {
            this.navCtrl.pop();
          }
        }
      ]
    });
    alert.present();
  }




  divideList(){
    this.dividedList = {};
    for(var i = 0; i < this.itemList.length; ++i){
      var category = this.itemList[i].category;
      if(!this.dividedList[category]){
        this.dividedList[category] = [];
      }
      this.dividedList[category].push(this.itemList[i]);
    }
  }


  addQuantity(item){
    item.quantity += 1;
    if(this.listId < 0){
      this.storage.set(this.listId.toString(), this.itemList);
    }
    else {
      this.listService.setQuantity(this.listId, item.name_item, item.quantity);
    }

  }

  cancelDeletion(item, key){
    item.quantity = 1;
  }

  deleteItem(item, category){
    this.itemList.splice(this.itemList.indexOf(item),1);
    this.dividedList[category].splice(this.dividedList[category].indexOf(item),1);
    if(this.dividedList[category].length == 0){
      delete this.dividedList[category];
    }
    if(this.listId < 0){
      this.storage.set(this.listId.toString(), this.itemList);
    }
    else {
      this.listService.removeArticle(this.listId, item.name_item);
    }
    this.divideList();
  }

  removeQuantity(item, key){
    if(item.quantity>=1){

      item.quantity -= 1;
      if(this.listId < 0){
        this.storage.set(this.listId.toString(), this.itemList);
      }
      else {
        this.listService.setQuantity(this.listId, item.name_item, item.quantity);
      }
    }

  }

  addItem(item){
    if(this.network.type != "none"){ // Si internet connection
      this.categoryService.getArticle(item.toString()).subscribe(result => {
        var category;

        if(result.count > 0){
          category = result.products[0].categories.split(',')[0];
        }
        else {
          category = "Autres";
        }
        this.listService.getArticlePriceByName(item).subscribe((res)=> {
          var new_item = {name_item: item, category: category, quantity: 1, taken: false, idOwner: -1, isOwned: false, color: "", notFound: false, price: res, recipe_container: ""};
          console.log(new_item);
          this.itemList.push(new_item);
          this.divideList();
          if(this.listId < 0){
            this.storage.set(this.listId.toString(), this.itemList);
          }
          else {
            this.listService.addArticle(this.listId, new_item).subscribe();
          }
        });
      });
    }
    else {
      var category = "Temporaire (Aucune connexion)";
      var new_item = {name_item: item, category: category, quantity: 1, taken: false, idOwner: -1, isOwned: false, color: "", notFound: false, price: -1,  recipe_container: ""};
      this.itemList.push(new_item);
      this.divideList();
      if(this.listId < 0){
        this.storage.set(this.listId.toString(), this.itemList);
      }
    }

  }



  dividebyCategory(listId){
    this.repartitionService.getCategoryRepartition(listId).subscribe(()=> {
      this.loadList();
    });
  }

  dividebyPrice(listId){
    this.repartitionService.getCategoryPrice(listId).subscribe(() => {
      this.loadList();
    });
  }

  reserve($event, item){
    if(item.idOwner == -1){
      item.isOwned = !item.isOwned;
      item.idOwner = this.userId;
      item.color = this.colorPerContributor[this.userId].color;

      if(item.notFound){
        item.notFound = false;
        this.listService.setNeedHelp(this.listId, item.name_item, item.notFound).subscribe((message) => {
          this.listService.setUserForArticle(this.userId, item, this.listId);
        });
      }
      else {
        this.listService.setUserForArticle(this.userId, item, this.listId);
      }
    }
    else if(this.userId == item.idOwner){
      item.isOwned = !item.isOwned;
      item.idOwner = -1;
      this.listService.setUserForArticle(-1, item, this.listId);
    }

  }

  taken($event, item){
    item.taken = !item.taken;
    if(this.listId < 0){
      this.storage.set(this.listId.toString(), this.itemList);
    }
    else {
      this.listService.setArticleTaken(this.listId, item.name_item, item.taken);
    }

  }

  needHelp($event, item){
    if(item.idOwner == this.userId){
      item.notFound = true;
      item.isOwned = false;
      item.idOwner = -1;

      this.listService.setNeedHelp(this.listId, item.name_item, item.notFound).subscribe((message) => {
        this.listService.setUserForArticle(-1, item, this.listId);
      });
    }
  }

  inviteFriends(){
    var collabInfos = [];
    if(this.userId == -1 && this.network.type != "none"){ //Todo : Verifier en + si Connection Internet
      alert("Vous n'êtes pas connecté !");
      return;
    }

    if(this.listId < 0){
      this.storage.remove(this.listId.toString());
      this.storage.get('listesName').then((data) => {
        var res = data;
        res.splice(res.indexOf({title: this.nameShopList, listId: this.listId}), 1);
        this.storage.set('listesName', res);
        this.listService.saveList(this.itemList, this.nameShopList, Date.now(), this.userId).subscribe(response => {
          this.listId = response.json();
          this.navCtrl.push(InviteFriend, {collabInfos: collabInfos, listId: this.listId, ownerPseudo: this.ownerPseudo});
        });
      });
    }
    else {
      var keys = Object.keys(this.colorPerContributor);
      this.getUserExposedMedal(keys, collabInfos, 0);
    }
  }



  getUserExposedMedal(keys, collabInfos, index){
    console.log(keys);
    console.log(collabInfos);
    console.log(index);
    this.medalService.getUserMedalExposed(Number(keys[index])).subscribe(res => {
      console.log(res);
      if(keys.length > index){
        console.log(res);
        collabInfos.push({
          pseudo: this.colorPerContributor[keys[index]].pseudo,
          id: keys[index],
          color: this.colorPerContributor[keys[index]].color,
          medal: res
        });
        index++;
        console.log(collabInfos);
        if(keys.length > index) {
          this.getUserExposedMedal(keys, collabInfos, index);
        }else{
          this.navCtrl.push(InviteFriend, {collabInfos: collabInfos, listId: this.listId, ownerPseudo: this.ownerPseudo, color: this.color});
        }
      }
    });


  }

  blockquoteColor(item){
    if(item.isOwned)
      return this._sanitizer.bypassSecurityTrustStyle("5px solid "+this.colorService.getHexaByName(item.color));
    else
      return this._sanitizer.bypassSecurityTrustStyle("5px solid #bdc3c7");
  }

  getTotalPrice(){
    this.totalPrice = 0;
    for(var i = 0; i < this.itemList.length; ++i){
      this.totalPrice += this.itemList[i].price*this.itemList[i].quantity;
    }
  }

  getRepartitionSummary(){
    var pricesPerUser = {};
    console.log(this.contributors);
    pricesPerUser[this.userId] = {total : 0, pseudo: this.userPseudo};
    for(var i = 0; i < this.contributors.length; ++i){
      pricesPerUser[this.contributors[i].id] = {total : 0, pseudo : this.contributors[i].pseudo};
    }
    console.log(pricesPerUser);

    for(var j = 0; j < this.itemList.length; ++j){
      if(this.itemList[j].isOwned){
        pricesPerUser[this.itemList[j].idOwner].total += this.itemList[j].price * this.itemList[j].quantity;
      }
    }

    console.log(pricesPerUser);
    var keys = Object.keys(pricesPerUser);
    var messageAlert = "<ul>";
    for(var k = 0 ; k < keys.length; ++k){
      messageAlert += "<li>"+pricesPerUser[keys[k]].pseudo + " : "+ pricesPerUser[keys[k]].total + "€</li>"
    }
    messageAlert += "</ul>";
    let prompt = this.alertCtrl.create({
      title: 'Recapitulatif',
      message : messageAlert,
      buttons: [
        {
          text: 'Ok',
          handler: data => {
          }
        }
      ]
    });
    prompt.present();
  }

}


@Pipe({name: 'keys'})
export class KeysPipe implements PipeTransform {
  transform(value, args:string[]) : any {
    let keys = [];
    for (let key in value) {
      keys.push({key: key, value: value[key]});
    }
    return keys;
  }
}
