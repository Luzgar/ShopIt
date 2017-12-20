import {Injectable} from "@angular/core";
import {Http, Headers, Response} from "@angular/http";
import 'rxjs/add/operator/map';

@Injectable()
export class RecipeService{


  ipAdress: string;

  constructor(private http: Http){
    this.ipAdress = "192.168.43.102";
  }


  getAllRecipes(){
    console.log(this.http.get("getallrecipes() = "+"http://localhost:8080/shopit-backend/recipe/allrecipe").map((res:Response) => res.json()))
    return this.http.get("http://"+this.ipAdress+":8080/shopit-backend/recipe/allrecipe")
      .map((res:Response) => res.json());
  }

  getRecipeById(idRecipe : number){
    return this.http.get("http://"+this.ipAdress+":8080/shopit-backend/recipe/getrecipebyid/"+idRecipe)
      .map((res:Response) => res.json());
  }

  getRecipeByIngredients(listIngredients: string){
    return this.http.get("http://"+this.ipAdress+":8080/shopit-backend/recipe/recipebyingredients/"+listIngredients)
      .map((res:Response) => res.json());
  }

  addRecipe(name: string, number_person: number, description: string, listeIngredients: any, userId: number){
    let headers = new Headers();
    headers.append('Content-Type', 'application/json');

    let body = {
      name: name,
      number_person: number_person,
      description: description,
      ingredients: listeIngredients,
      author_id: userId
    };

    console.log(JSON.stringify(body));

    this.http.post('http://'+this.ipAdress+':8080/shopit-backend/recipe/addrecipe', JSON.stringify(body), {headers: headers}).subscribe(data => {
      console.log(data);
    });
  }




}
