import {Component} from "@angular/core";
import {Http} from "@angular/http";
import {NavController, NavParams, ToastController} from "ionic-angular";
import {RecipeService} from "../../../service/RecipeService";
import {RecipeListPage} from "../recipeList";
import { Storage } from '@ionic/storage';

@Component({
  selector: 'page-addRecipe',
  templateUrl: 'addRecipe.html',
  providers: [RecipeService]
})

export class AddRecipePage{

  listIngredients: Array<{ name_item: string, quantity: number, category: string, taken: boolean }>;

  titreInput: string;
  descriptionInput: string;
  nbPersonneInput: number;
  ingredient: string;
  categoryIngredientInput: string;
  userId: number;

  constructor(public storage: Storage, public navCtrl: NavController, public recipeService: RecipeService, public http: Http, public toastCtrl: ToastController) {
    this.storage.get("userId").then((data) => {
      if(data != null){
        this.userId = data;
      }
      else {
        this.userId = -1;
      }
    });
    this.create();
  }

  create(){
    this.ingredient = null;
    this.categoryIngredientInput = null;
    this.listIngredients = [];
  }

  removeIngredient(ingredient){
    this.listIngredients.splice(this.listIngredients.indexOf(ingredient));
  }



  addIngredient(){

    if(this.categoryIngredientInput != null) {
      let new_item: {name_item: string, quantity: number, category: string, taken: boolean};
      var cat;
      if(this.ingredient == "Pommes de terre")
        cat = "Féculents";
      if(this.ingredient == "Reblochon")
        cat = "Fromages";
      if(this.ingredient == "Lardons")
        cat = "Viandes";

      new_item = {
        name_item: this.ingredient,
        quantity: 1,
        category: cat,
        taken: false
      };
      this.listIngredients.push(new_item);
      this.ingredient = null;
      this.categoryIngredientInput = null;
    }
    else{
      let toast = this.toastCtrl.create({
        message: 'Choisir la catégorie de l\'aricle en appuyant sur la petite flèche' ,
        duration: 1500,
        position: 'bottom'
      });
      toast.present();
    }

  }

  sendRecipe()
  {
    this.recipeService.addRecipe(this.titreInput, this.nbPersonneInput, this.descriptionInput, this.listIngredients, this.userId)
    this.navCtrl.pop();
    let toast = this.toastCtrl.create({
      message: 'Votre recette a été ajoutée' ,
      duration: 1500,
      position: 'bottom'
    });
    toast.present();
  }

}

