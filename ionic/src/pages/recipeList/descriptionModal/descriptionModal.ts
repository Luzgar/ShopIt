import {Component} from "@angular/core";
import {Http} from "@angular/http";
import {NavController, NavParams} from "ionic-angular";
import {RecipeService} from "../../../service/RecipeService";
import {SelectListToCompletePage} from "../../selectListToComplete/selectListToComplete";

@Component({
  selector: 'page-descriptionModal',
  templateUrl: 'descriptionModal.html',
  providers: [RecipeService]
})

export class DescriptionModalPage {
  key: number;
  name: any;
  initialNumberPerson: number;
  modifiedNumberPerson: number = 1;
  description: any;
  listInfos: any;
  listIngredients:  Array<{item: string, quantity: number, category: string, taken: boolean}>;


  constructor(public navCtrl: NavController,public navParams: NavParams, public recipeService: RecipeService, public http: Http) {
    this.key = navParams.get("key");
    this.listIngredients = [];
    this.create();
  }

  create(){
    this.recipeService.getRecipeById(this.key).subscribe(data => {
      this.name = data.name;
      this.initialNumberPerson = data.numberPerson;
      this.description = data.description;
      this.listInfos = data.ingredients;
      let new_item: {item: string, quantity: number, category: string, taken: boolean};
      var keys = Object.keys(this.listInfos);
      for(var i = 0; i < keys.length; ++i ) {
        new_item = {
          item: this.listInfos[keys[i]].item,
          quantity: this.listInfos[keys[i]].number,
          category: this.listInfos[keys[i]].category,
          taken: this.listInfos[keys[i]].taken
        };
        this.listIngredients.push(new_item);
      }

    });

  }

  deleteIngredient(ingredient){
    this.listIngredients.splice(this.listIngredients.indexOf(ingredient),1);
  }

  addToList(){
    let new_item: {item: string, quantity: number, category: string, taken: boolean};
    this.listIngredients = [];
    var keys = Object.keys(this.listInfos);
    for(var i = 0; i < keys.length; ++i ) {
      new_item = {
        item: this.listInfos[keys[i]].item,
        quantity: Math.ceil(this.modifiedNumberPerson/this.initialNumberPerson)*this.listInfos[keys[i]].number,
        category: this.listInfos[keys[i]].category,
        taken: this.listInfos[keys[i]].taken
      };
      this.listIngredients.push(new_item);
    }
    this.navCtrl.push(SelectListToCompletePage,{listIngredients: this.listIngredients, name: this.name});
  }
}
