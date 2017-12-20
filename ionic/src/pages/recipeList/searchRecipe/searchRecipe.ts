import {Component} from "@angular/core";
import {Http} from "@angular/http";
import {NavController, NavParams, ToastController} from "ionic-angular";
import {RecipeService} from "../../../service/RecipeService";
import {DescriptionModalPage} from "../descriptionModal/descriptionModal";

@Component({
  selector: 'page-searchRecipe',
  templateUrl: 'searchRecipe.html',
  providers: [RecipeService]
})

export class SearchRecipePage {

  inputIngredient: string;
  requestedIngredients: string;
  listRecette:  Array<{title: string, listId: number}>;

  constructor(public navCtrl: NavController, public recipeService: RecipeService, public http: Http) {
    this.create();
  }

  create(){
    this.inputIngredient = null;
    this.requestedIngredients = null;
    this.listRecette = [];
  }

  searchRecipeByIngredient(){
    this.recipeService.getRecipeByIngredients(this.requestedIngredients).subscribe(res => {
      var keys = Object.keys(res);
      for(var i = 0; i < keys.length; ++i ){
        this.listRecette.push({title: res[keys[i]], listId: Number(keys[i])});
        console.log(res[keys[i]]);
      }
    });
  }

  addToParams(ingredient){
    if(this.requestedIngredients == null)
      this.requestedIngredients = ingredient;
    else{
      this.requestedIngredients += ","+ingredient
    }
    this.inputIngredient = null;
  }

  recetteClick(key){
    this.navCtrl.push(DescriptionModalPage,{key: key});
  }


}
