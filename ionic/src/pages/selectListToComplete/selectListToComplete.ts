
import {Component} from "@angular/core";
import {NavController, NavParams, ToastController} from "ionic-angular";
import { Storage } from '@ionic/storage';
import {ListService} from "../../service/ListService";
import {RecipeListPage} from "../recipeList/recipeList";


@Component({
  selector: 'page-selectListToComplete',
  templateUrl: 'selectListToComplete.html',
  providers : [ListService]

})
export class SelectListToCompletePage {

  listes: Array<{title: string, listId: number}>;
  listId: number;
  itemList: Array<{name_item: string, quantity: number, category: string, taken: boolean}>;
  nameShopList: any;
  listIngredients: any;
  nameRecipe: string;

  constructor(public navCtrl: NavController, public navParams: NavParams, public storage: Storage, private listService : ListService, private toastCtrl: ToastController) {
    this.listIngredients = this.navParams.get('listIngredients');
    this.nameRecipe = this.navParams.get('name');
    console.log(this.listIngredients);
        this.listes = [];
    this.itemList = [];
    this.storage.get('listesName').then((data) => {
      this.listes = data;
      console.log(this.listId);
      this.storage.get('userId').then((userId) => {
        this.listService.getAllCurrentTitleByUser(userId).subscribe(data => {
          //console.log(data);
          var keys = Object.keys(data);
          for(var i = 0; i < keys.length; ++i ){
            this.listes.push({title: data[keys[i]], listId: Number(keys[i])});
          }
        })
      });
    });
}

listeClick(liste){

  this.nameShopList = liste.title;
  this.listId = liste.listId;
  if(this.listId < 0){

    this.storage.get(this.listId.toString()).then((data) => {
      this.itemList = data;
      if(this.itemList == null)
        this.itemList = [];

    });
  }
  else {
    this.listService.getListById(liste.listId).subscribe(data => {
      console.log(data);
      for(var i = 0; i < data.length; ++i){
        //console.log(data.elements[i]);
        this.itemList.push({name_item: data.elements[i].item, category: data.elements[i].category, quantity: data.elements[i].number, taken: data.elements[i].taken});
      }
    })
  }

  for(var elmt in this.listIngredients){
    var new_item = {name_item: this.listIngredients[elmt].item, quantity: this.listIngredients[elmt].quantity, category: this.listIngredients[elmt].category, taken: false, recipe_container: this.nameRecipe};
    this.itemList.push(new_item);
    console.log(new_item);
  }
  if(this.listId < 0){
    console.log("local");
    console.log(this.itemList);
    this.storage.set(this.listId.toString(), this.itemList);
    this.navCtrl.setRoot(RecipeListPage);
    let toast = this.toastCtrl.create({
      message: 'La recette a bien été ajoutée',
      duration: 1500,
      position: 'top'
    });
    toast.present();
  }
  else {
    this.addArticleSharedList(this.itemList, 0);
  }
}

addArticleSharedList(ingredients, index){
  if(ingredients.length > index){
    this.listService.addArticle(this.listId, ingredients[index]).subscribe(()=>{
      index++;
      this.addArticleSharedList(ingredients, index);
    });
  }
  else {
    this.navCtrl.setRoot(RecipeListPage);
    let toast = this.toastCtrl.create({
      message: 'La recette a bien été ajoutée',
      duration: 1500,
      position: 'top'
    });
    toast.present();
  }
}

}
