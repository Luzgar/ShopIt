import { Component } from '@angular/core';
import {NavController, AlertController} from "ionic-angular";
import {SelectListToCompletePage} from "../selectListToComplete/selectListToComplete";
import {Http} from "@angular/http";
import {RecipeService} from "../../service/RecipeService";
import {DescriptionModalPage} from "./descriptionModal/descriptionModal";
import {AddRecipePage} from "./addRecipe/addRecipe";
import {SearchRecipePage} from "./searchRecipe/searchRecipe";


@Component({
  selector: 'recipeList.scss',
  templateUrl: 'recipeList.html',
  providers : [RecipeService]

})
export class RecipeListPage {

  listeRecettes: Array<{title: string, listId: number}>;
  listIngredients:  Array<{item: string, quantity: number, category: string, taken: boolean}>;


  constructor(public navCtrl: NavController, public http: Http, private recipeService : RecipeService) {
    
    
  }

  ionViewDidEnter(){
    this.listeRecettes = [];
    this.listIngredients= [];
    this.recipeService.getAllRecipes().subscribe(res => {
      console.log("recipes =  "+res);
      var keys = Object.keys(res);
      for(var i = 0; i < keys.length; ++i ){
        this.listeRecettes.push({title: res[keys[i]], listId: Number(keys[i])});
      }
    });
  }

  recetteClick(key){
    this.navCtrl.push(DescriptionModalPage,{key: key});
  }

  suggestRecipe(){
    this.navCtrl.push(AddRecipePage);
  }

  searchRecipe(){
    this.navCtrl.push(SearchRecipePage);
  }
}
